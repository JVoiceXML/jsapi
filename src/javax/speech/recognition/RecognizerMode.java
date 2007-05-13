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

package javax.speech.recognition;

import java.util.Collection;
import java.util.Locale;

import javax.speech.EngineMode;

public class RecognizerMode extends EngineMode {
	public static RecognizerMode DEFAULT = new RecognizerMode();

	public static Integer SMALL_SIZE = new Integer(10);

	public static Integer MEDIUM_SIZE = new Integer(100);

	public static Integer LARGE_SIZE = new Integer(1000);

	public static Integer VERY_LARGE_SIZE = new Integer(10000);

	private Integer vocabSupport;

	private Locale[] locales;

	private SpeakerProfile[] profiles;

	public RecognizerMode() {
	}

	public RecognizerMode(Locale locale) {
		locales = new Locale[1];

		locales[0] = locale;
	}

	public RecognizerMode(String engineName, String modeName, Boolean running,
			Boolean supportsLetterToSound, Integer markupSupport,
			Integer vocabSupport, Locale[] locales, SpeakerProfile[] profiles) {
		super(engineName, modeName, running, supportsLetterToSound,
				markupSupport);
		this.vocabSupport = vocabSupport;
		this.locales = locales;
		this.profiles = profiles;
	}

	public boolean equals(Object object) {
		if (!(object instanceof RecognizerMode)) {
			return false;
		}

		final RecognizerMode mode = (RecognizerMode) object;
		final Locale[] otherLocales = mode.getLocales();
		final boolean localesMatch;
		if (locales == null) {
			localesMatch = (otherLocales == null);
		} else {
			if ((otherLocales != null)
					&& (locales.length == otherLocales.length)) {
				boolean match = true;
				for (int i = 0; (i < locales.length) && match; i++) {
					final Locale locale = locales[i];
					final Locale otherLocale = otherLocales[i];
					if (!locale.equals(otherLocale)) {
						match = false;
					}
				}

				localesMatch = match;
			} else {
				localesMatch = false;
			}
		}

		final SpeakerProfile[] otherProfiles = mode.getSpeakerProfiles();
		final boolean profilesMatch;
		if (profiles == null) {
			profilesMatch = (otherProfiles == null);
		} else {
			if ((otherProfiles != null)
					&& (profiles.length == otherProfiles.length)) {
				boolean match = true;
				for (int i = 0; (i < profiles.length) && match; i++) {
					final SpeakerProfile profile = profiles[i];
					final SpeakerProfile otherProfile = otherProfiles[i];
					if (!profile.equals(otherProfile)) {
						match = false;
					}
				}

				profilesMatch = match;
			} else {
				profilesMatch = false;
			}
		}

		return localesMatch && profilesMatch;
	}

	public Locale[] getLocales() {
		return locales;
	}

	public Integer getMarkupSupport() {
		return super.getMarkupSupport();
	}

	public SpeakerProfile[] getSpeakerProfiles() {
		return profiles;
	}

	public Integer getVocabSupport() {
		return vocabSupport;
	}

	public boolean match(EngineMode require) {
		if (!super.match(require)) {
			return false;
		}

		if (require instanceof RecognizerMode) {
			final RecognizerMode mode = (RecognizerMode) require;
			final Locale[] otherLocales = mode.getLocales();
			if (otherLocales != null) {
				if (locales == null) {
					return false;
				}

				boolean match = false;
				for (int i = 0; (i < otherLocales.length) && !match; i++) {
					final Locale otherLocale = otherLocales[i];

					for (int k=0; k<locales.length; k++) {
						final Locale locale = locales[k];
						if (locale.equals(otherLocale)) {
							match = true;
						}
					}

					if (!match) {
						return false;
					}
				}
			}

			final SpeakerProfile[] otherProfiles = mode.getSpeakerProfiles();
			if (otherProfiles != null) {
				if (profiles == null) {
					return false;
				}

				boolean match = false;
				for (int i = 0; (i < otherProfiles.length) && !match; i++) {
					final SpeakerProfile otherProfile = otherProfiles[i];

					for (int k=0; k<profiles.length; k++) {
						final SpeakerProfile profile = profiles[k];
						if (profile.equals(otherProfile)) {
							match = true;
						}
					}

					if (!match) {
						return false;
					}
				}
			}
		}
		
		return true;
	}

	/**
	 * Creates a collection of all parameters.
	 * 
	 * @return collection of all parameters.
	 */
	protected Collection getParameters() {
		final Collection parameters = super.getParameters();

		parameters.add(vocabSupport);
		if (locales == null) {
			parameters.add(null);
		} else {
			final Collection col = new java.util.ArrayList();
			for (int i = 0; i < locales.length; i++) {
				col.add(locales[i]);
			}
			parameters.add(col);
		}

		if (profiles == null) {
			parameters.add(null);
		} else {
			final Collection col = new java.util.ArrayList();
			for (int i = 0; i < profiles.length; i++) {
				col.add(profiles[i]);
			}
			parameters.add(col);
		}

		return parameters;
	}
}