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

package javax.speech.synthesis;

import java.util.Collection;
import java.util.Locale;

import javax.speech.EngineMode;

public class SynthesizerMode extends EngineMode {
    public static final SynthesizerMode DEFAULT = new SynthesizerMode();

    private Voice[] voices;

    public SynthesizerMode() {
        super();
    }

    public SynthesizerMode(Locale locale) {
        super();

        voices = new Voice[1];
        voices[0] = new Voice(locale, null, Voice.GENDER_DONT_CARE,
                Voice.AGE_DONT_CARE, Voice.VARIANT_DONT_CARE);
    }

    public SynthesizerMode(String engineName, String modeName, Boolean running,
            Boolean supportsLetterToSound, Integer markupSupport, Voice[] voices) {
        super(engineName, modeName, running, supportsLetterToSound,
                markupSupport);

        this.voices = voices;
    }

    public boolean equals(Object object) {
        if (!(object instanceof SynthesizerMode)) {
            return false;
        }

        final SynthesizerMode mode = (SynthesizerMode) object;

        if (!super.equals(mode)) {
            return false;
        }

        final Voice[] otherVoices = mode.getVoices();
        if ((voices == null) && (otherVoices == null)) {
            return true;
        }

        if (voices.length != otherVoices.length) {
            return false;
        }
        
        for (int i=0; i< voices.length; i++) {
            final Voice voice = voices[i];
            final Voice otherVoice = otherVoices[i];
            if (!voice.equals(otherVoice)) {
                return false;
            }
        }
        
        return true;
    }

    public Integer getMarkupSupport() {
        return super.getMarkupSupport();
    }

    public Voice[] getVoices() {
        return voices;
    }

    public boolean match(EngineMode require) {
        if (!super.match(require)) {
            return false;
        }

        if (require instanceof SynthesizerMode) {
            final SynthesizerMode mode = (SynthesizerMode) require;
            Voice[] otherVoices = mode.getVoices();
            if (otherVoices != null) {
            	if (voices == null) {
            		return false;
            	}
            	
                for (int i=0; i< otherVoices.length; i++) {
                    Voice otherVoice = otherVoices[i];

                    boolean voiceMatch = false;
                    for (int k=0; (k<voices.length) && !voiceMatch; k++) {
                        final Voice voice = voices[k];
                        if (otherVoice.match(voice)) {
                            voiceMatch = true;
                        }
                    }

                    if (!voiceMatch) {
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

        if (voices == null) {
            parameters.add(null);
        } else {
            final Collection col = new java.util.ArrayList();
            for (int i = 0; i < voices.length; i++) {
                col.add(voices[i]);
            }
            parameters.add(col);
        }

        return parameters;
    }
}
