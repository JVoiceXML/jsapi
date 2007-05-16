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

import java.util.Iterator;

public class Word {
	public static final long ABBREVIATION = 1;

	public static final long ACOUSTIC = ABBREVIATION << 1;

	public static final long ADJECTIVE =  ACOUSTIC << 1;

	public static final long ADVERB = ADJECTIVE << 1;

	public static final long AUXILIARY = ADVERB << 1;

	public static final long CARDINAL = AUXILIARY << 1;

	public static final long CONJUNCTION = CARDINAL << 1;

	public static final long CONTRACTION = CONJUNCTION << 1;

	public static final long DETERMINER = CONTRACTION << 1;

	public static final long DONT_CARE = DETERMINER << 1;

	public static final long NOUN = DONT_CARE << 1;

	public static final long OTHER = NOUN << 1;

	public static final long PREPOSITION = OTHER << 1;

	public static final long PRONOUN = PREPOSITION << 1;

	public static final long PROPER_ADJECTIVE = PRONOUN << 1;

	public static final long PROPER_NOUN = PROPER_ADJECTIVE << 1;

	public static final long VERB = PROPER_NOUN << 1;

	public static final long UNKNOWN = 0;
	
	private String text;

	private String[] pronunciations;

	private String spokenForm;

	private AudioSegment audioSegment;

	private long categories;

	public Word(String text, String[] pronunciations, String spokenForm,
			AudioSegment audioSegment, long categories) {
		if (text == null) {
			throw new IllegalArgumentException(
					"Written form text must be specified");
		}
		this.text = text;
		this.pronunciations = pronunciations;
		this.spokenForm = spokenForm;
		this.audioSegment = audioSegment;
		this.categories = categories;
	}

	public AudioSegment getAudioSegment() {
		return audioSegment;
	}

	public long getCategories() {
		return categories;
	}

	public String[] getPronunciations() {
		return pronunciations;
	}

	public String getSpokenForm() {
		return spokenForm;
	}

	public String getText() {
		return text;
	}

	public String toString() {
		final StringBuffer str = new StringBuffer();

		str.append(getClass());
		str.append("[");

		str.append(text);

		if (pronunciations == null) {
			str.append(pronunciations);
		} else {
			str.append("[");
			int max = pronunciations.length;
			for (int i=0; i<max; i++) {
				str.append(pronunciations[i]);
				if (i != max -1) {
					str.append(",");
				}
			}
			str.append("]");
		}
		
		str.append(spokenForm);
		str.append(audioSegment);
		str.append(categories);
		
		str.append("]");

		return str.toString();
	}
}
