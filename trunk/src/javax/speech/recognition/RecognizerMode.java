/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 249 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
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

import java.util.Locale;

import javax.speech.EngineMode;

public class RecognizerMode extends EngineMode {
    public static RecognizerMode DEFAULT = new RecognizerMode();

    public static Integer LARGE_SIZE = new Integer(0);

    public static Integer MEDIUM_SIZE = new Integer(1);

    public static Integer SMALL_SIZE = new Integer(2);

    public static Integer VERY_LARGE_SIZE = new Integer(3);

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

    public boolean equals(Object mode) {
        return super.equals(mode);
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

    public int hashCode() {
        return super.hashCode();
    }

    public boolean match(EngineMode require) {
        return false;
    }

    public String toString() {
        return super.toString();
    }
}
