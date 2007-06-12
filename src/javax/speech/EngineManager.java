/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package javax.speech;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.speech.spi.EngineFactory;
import javax.speech.spi.EngineListFactory;

public class EngineManager {
	private static List engineListFactories = new java.util.ArrayList();

	private static SpeechEventExecutor executor;

	static {
		final InputStream input = EngineManager.class
				.getResourceAsStream("/speech.properties");
		final Properties props = new Properties();
		try {
			props.load(input);
		} catch (IOException e) {
			// Ignore.
		}

		final Collection keys = props.keySet();
		final Iterator iterator = keys.iterator();
		while (iterator.hasNext()) {
			String className = (String) iterator.next();
			try {
				registerEngineListFactory(className);
			} catch (IllegalArgumentException e) {
				// Ignore.
			} catch (EngineException e) {
				// Ignore.
			} catch (SecurityException e) {
				// Ignore.
			}
		}
	}

	public static EngineList availableEngines(EngineMode require) {
		final List modes = new java.util.ArrayList();

		final Iterator iterator = engineListFactories.iterator();
		while (iterator.hasNext()) {
			final EngineListFactory factory = (EngineListFactory) iterator
					.next();
			EngineList list = factory.createEngineList(require);
			Enumeration currentModes = list.elements();
			while (currentModes.hasMoreElements()) {
				final EngineMode mode = (EngineMode) currentModes.nextElement();
				modes.add(mode);
			}
		}

		EngineMode[] foundModes = new EngineMode[modes.size()];
		modes.toArray(foundModes);

		return new EngineList(foundModes);
	}

	public static Engine createEngine(EngineMode require)
			throws EngineException {
		if (require == null) {
			throw new IllegalArgumentException(
					"An engine mode must be specified to create an engine!");
		}

		final Locale defaultLocale = Locale.getDefault();
		final EngineList list = availableEngines(require);

		final Enumeration enumeration = list.elements();
		EngineFactory preferredFactory = null;
		Boolean preferredFactoryRunning = null;
		while (enumeration.hasMoreElements()) {
			final EngineMode mode = (EngineMode) enumeration.nextElement();
			if (mode instanceof EngineFactory) {
				final EngineFactory factory = (EngineFactory) mode;
				if (preferredFactory == null) {
					preferredFactory = factory;
					preferredFactoryRunning = mode.getRunning();
				}

				final Boolean currentFactoryRunning = mode.getRunning();
				if (Boolean.TRUE.equals(currentFactoryRunning)) {
					if (!Boolean.TRUE.equals(preferredFactoryRunning)) {
						preferredFactory = factory;
					}
				}
			}
		}

		if (preferredFactory == null) {
			return null;
		}

		return preferredFactory.createEngine();
	}

	public static SpeechEventExecutor getSpeechEventExecutor() {
		return executor;
	}

	public static void setSpeechEventExecutor(
			SpeechEventExecutor speechEventDispatcher) {
		executor = speechEventDispatcher;
	}

	public static String getVersion() {
		return "2.0.0.0";
	}

	public static void registerEngineListFactory(String className)
			throws EngineException {
		final Class clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("'" + className
					+ "' cannot be loaded!");
		}

		if (!clazz.isAssignableFrom(EngineListFactory.class)) {
			throw new IllegalArgumentException("'" + className
					+ "' does not implement "
					+ EngineListFactory.class.getCanonicalName());
		}

		final EngineListFactory engineListFactory;
		try {
			engineListFactory = (EngineListFactory) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("'" + className
					+ "' cannot be created!");
		} catch (IllegalAccessException e) {
			throw new SecurityException("'" + className
					+ "' cannot be created!");
		}

		engineListFactories.add(engineListFactory);
	}
}
