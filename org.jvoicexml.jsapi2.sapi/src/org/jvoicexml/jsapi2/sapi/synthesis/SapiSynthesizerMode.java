/**
 * 
 */
package org.jvoicexml.jsapi2.sapi.synthesis;

import javax.speech.Engine;
import javax.speech.EngineException;
import javax.speech.SpeechLocale;
import javax.speech.spi.EngineFactory;
import javax.speech.synthesis.SynthesizerMode;
import javax.speech.synthesis.Voice;

/**
 * Synthesizer mode for SAPI.
 * @author Dirk Schnelle-Walka
 *
 */
public final class SapiSynthesizerMode extends SynthesizerMode
    implements EngineFactory {
    /**
     * Constructs a new object.
     */
    public SapiSynthesizerMode() {
        super();
    }

    /**
     * Constructs a new object.
     * @param locale  the locale associated with this mode
     */
    public SapiSynthesizerMode(final SpeechLocale locale) {
        super(locale);
    }

    /**
     * Constructs a new object.
     * @param engineName  the name of the engine
     * @param modeName   the name of the mode
     */
    public SapiSynthesizerMode(final String engineName,
            final String modeName,
            final Boolean running, final Boolean supportsLetterToSound,
            final Boolean supportsMarkup, final Voice[] voices) {
        super(engineName, modeName, running, supportsLetterToSound,
                supportsMarkup, voices);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Engine createEngine() throws IllegalArgumentException,
            EngineException {
        return new SapiSynthesizer(this);
    }

}
