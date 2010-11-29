package org.jvoicexml.jsapi2.sapi.recognition;

import javax.speech.Engine;
import javax.speech.EngineException;
import javax.speech.SpeechLocale;
import javax.speech.recognition.RecognizerMode;
import javax.speech.spi.EngineFactory;

public final class SapiRecognizerMode extends RecognizerMode
    implements EngineFactory {

    /**
     * Constructs a new object.
     */
    public SapiRecognizerMode() {
        super("Microsoft SAPI", "SAPI", Boolean.FALSE, Boolean.TRUE,
                Boolean.TRUE, RecognizerMode.MEDIUM_SIZE, null, null);
    }

    /**
     * Constructs a new object.
     * @param locale  the locale associated with this mode
     */
    public SapiRecognizerMode(final SpeechLocale locale) {
        super(locale);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Engine createEngine() throws IllegalArgumentException,
            EngineException {
        return new SapiRecognizer(this);
    }

}
