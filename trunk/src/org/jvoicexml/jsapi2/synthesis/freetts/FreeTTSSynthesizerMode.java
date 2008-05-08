package org.jvoicexml.jsapi2.synthesis.freetts;

import javax.speech.synthesis.SynthesizerMode;
import javax.speech.spi.EngineFactory;
import java.util.Locale;
import javax.speech.Engine;
import javax.speech.EngineException;

/**
 * Copyright 2003 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */



/**
 * Represents a SynthesizerModeDesc for the
 * FreeTTSSynthesizer. A FreeTTSSynthesizerModeDesc adds
 * an audio player to the standard mode items.
 */
public class FreeTTSSynthesizerMode extends SynthesizerMode implements
        EngineFactory {

    //   private Vector<FreeTTSVoice> allVoices;


    /**
     * Creates a fully-specified descriptor.
     * Any of the features may be <code>null</code>.
     *
     * @param engineName  the name of the engine
     * @param modeName   the name of the mode
     * @param locale  the locale associated with this mode
     */
    public FreeTTSSynthesizerMode(String engineName, String modeName,
                                  Locale locale, FreeTTSVoice[] voices) {
//        super(engineName, modeName, false, false, locale, Boolean.FALSE, null);
        //super(locale);
        super(engineName, modeName, null, null, null, voices);
        //  this.allVoices = allVoices;
    }


    /**
     * Constructs a FreeTTSSynthesizer with the properties of this mode
     * descriptor.
     *
     * @return a synthesizer that mathes the mode
     *
     * @throws IllegalArgumentException  if the properties of this
     * 		descriptor do not match any known engine or mode
     * @throws EngineException if the engine could not be created
     * @throws SecurityException if the caller does not have
     * 		permission to use the speech engine
     */
    public Engine createEngine() throws IllegalArgumentException,
            EngineException, SecurityException {
        FreeTTSSynthesizer s = new FreeTTSSynthesizer(this);
        return s;

    }
}
