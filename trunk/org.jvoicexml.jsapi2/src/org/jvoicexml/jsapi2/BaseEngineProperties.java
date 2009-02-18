/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This class is based on work by SUN Microsystems and
 * Carnegie Mellon University
 *
 * Copyright 1998-2003 Sun Microsystems, Inc.
 *
 * Portions Copyright 2001-2004 Sun Microsystems, Inc.
 * Portions Copyright 1999-2001 Language Technologies Institute,
 * Carnegie Mellon University.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * Permission is hereby granted, free of charge, to use and distribute
 * this software and its documentation without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of this work, and to
 * permit persons to whom this work is furnished to do so, subject to
 * the following conditions:
 *
 * 1. The code must retain the above copyright notice, this list of
 *    conditions and the following disclaimer.
 * 2. Any modifications must be clearly marked as such.
 * 3. Original authors' names are not deleted.
 * 4. The authors' names are not used to endorse or promote products
 *    derived from this software without specific prior written
 *   permission.
 *
 * SUN MICROSYSTEMS, INC., CARNEGIE MELLON UNIVERSITY AND THE
 * CONTRIBUTORS TO THIS WORK DISCLAIM ALL WARRANTIES WITH REGARD TO THIS
 * SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS, IN NO EVENT SHALL SUN MICROSYSTEMS, INC., CARNEGIE MELLON
 * UNIVERSITY NOR THE CONTRIBUTORS BE LIABLE FOR ANY SPECIAL, INDIRECT OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF
 * USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */
package org.jvoicexml.jsapi2;

import javax.speech.EngineProperties;
import javax.speech.SpeechEventExecutor;

import java.util.Enumeration;
import java.util.Vector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.speech.Engine;

/**
 * Supports the JSAPI 2.0 <code>EngineProperties</code>
 * interface.
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 */
public abstract class BaseEngineProperties implements EngineProperties {
    /**
     * List of <code>PropertyChangeListeners</code> registered for
     * <code>PropertyChangeEvents</code> on this object.
     */
    protected Vector propertyChangeListeners;

    /** The engine for which these properties apply. */
    private final Engine engine;

    /**
     * The base location to resolve relative URLs against for this Engine
     * instance.
     */
    private String baseUri;

    /** The priority for this engine instance. */
    private int priority;

    /**
     * Constructs a new object.
     * @param eng the engine for which these properties apply.
     */
    protected BaseEngineProperties(final Engine eng) {
        propertyChangeListeners = new Vector();
        engine = eng;
    }

    /**
     * Retrieves the engine.
     * @return the engine
     */
    protected Engine getEngine() {
        return engine;
    }

    /**
     * Returns all properties to reasonable defaults
     * for the <code>Engine</code>.  A
     * <code>PropertyChangeEvent</code> is issued
     * for each property that changes as the reset takes effect.
     */
    public void reset() {
        setPriority(EngineProperties.NORM_TRUSTED_PRIORITY);
        setBase("");
    }

    /**
     * {@inheritDoc}
     */
    public int getPriority() {
        return priority;
    }

    /**
     * {@inheritDoc}
     */
    public void setPriority(final int prio) {
        postPropertyChangeEvent("priority",
                new Integer(this.priority), new Integer(prio));
        priority = prio;
    }

    /**
     * {@inheritDoc}
     */
    public void setBase(final String uri) {
        postPropertyChangeEvent("base",
                new Integer(this.priority), new Integer(priority));
        baseUri = uri;
    }

    /**
     * {@inheritDoc}
     */
    public String getBase() {
        return baseUri;
    }

    /**
     * {@inheritDoc}
     */
    public void addPropertyChangeListener(
            final PropertyChangeListener listener) {
        if (!propertyChangeListeners.contains(listener)) {
            propertyChangeListeners.addElement(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removePropertyChangeListener(
            final PropertyChangeListener listener) {
        propertyChangeListeners.removeElement(listener);
    }

    /**
     * Generates a
     * <code>PropertyChangeEvent</code> for an <code>Object</code> value
     * and posts it to the event queue using the configured
     * {@link SpeechEventExecutor}.
     *
     * @param propName the name of the property
     * @param oldValue the old value
     * @param newValue the new value
     *
     * @see #firePropertyChangeEvent
     * @see #dispatchSpeechEvent
     */
    protected void postPropertyChangeEvent(final String propName,
                                           final Object oldValue,
                                           final Object newValue) {

        if (propertyChangeListeners.size() < 1) {
            return;
        }

        final PropertyChangeEvent event = new PropertyChangeEvent(this,
                propName,
                oldValue,
                newValue);


        final Runnable runnable = new Runnable() {
            public void run() {
                firePropertyChangeEvent(event);
            }
        };

        final SpeechEventExecutor executor = engine.getSpeechEventExecutor();
        executor.execute(runnable);
    }

    /**
     * Sends a <code>PropertyChangeEvent</code>
     * to all <code>PropertyChangeListeners</code> registered with
     * this object.
     *
     * @param event the <code>PropertyChangeEvent</code> to send
     *
     * @see #firePropertyChangeEvent
     * @see #dispatchSpeechEvent
     */
    public void firePropertyChangeEvent(final PropertyChangeEvent event) {
        Enumeration e = propertyChangeListeners.elements();
        while (e.hasMoreElements()) {
            PropertyChangeListener listener =
                (PropertyChangeListener) e.nextElement();
            listener.propertyChange(event);
        }
    }
}
