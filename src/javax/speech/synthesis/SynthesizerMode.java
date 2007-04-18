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

package javax.speech.synthesis;

import java.util.Locale;

import javax.speech.EngineMode;

public class SynthesizerMode extends EngineMode {
    public static SynthesizerMode DEFAULT = null;

    private Voice[] voices;

    public SynthesizerMode() {
    }

    public SynthesizerMode(Locale locale) {

    }

    public SynthesizerMode(String engineName, String modeName, Boolean running,
            Boolean supportsLetterToSound, Integer markupSupport, Voice[] voices) {
        super(engineName, modeName, running, supportsLetterToSound,
                markupSupport);
        this.voices = voices;
    }

    public boolean equals(Object mode) {
        return false;
    }

    public Integer getMarkupSupport() {
        return super.getMarkupSupport();
    }

    public Voice[] getVoices() {
        return voices;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean match(EngineMode require) {
        return super.match(require);
    }

    public String toString() {
        return super.toString();
    }
}
