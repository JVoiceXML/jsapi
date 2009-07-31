package org.jvoicexml.jsapi2.jse.synthesis.freetts;

import java.util.Locale;

import javax.speech.Engine;
import javax.speech.EngineException;
import javax.speech.spi.EngineFactory;
import javax.speech.synthesis.SynthesizerMode;


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
     * Constructs a new object.
     */
    public FreeTTSSynthesizerMode() {
    }

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
        super(engineName, modeName, null, null, null, voices);
    }


    /**
     * Constructs a FreeTTSSynthesizer with the properties of this mode
     * descriptor.
     *
     * @return a synthesizer that matches the mode
     *
     * @throws IllegalArgumentException  if the properties of this
     * 		descriptor do not match any known engine or mode
     * @throws EngineException if the engine could not be created
     * @throws SecurityException if the caller does not have
     * 		permission to use the speech engine
     */
    public Engine createEngine() throws IllegalArgumentException,
            EngineException, SecurityException {
        return new FreeTTSSynthesizer(this);
    }
}
