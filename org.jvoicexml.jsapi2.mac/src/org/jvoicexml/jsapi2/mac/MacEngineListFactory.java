package org.jvoicexml.jsapi2.mac;
import javax.speech.EngineList;
import javax.speech.EngineMode;
import javax.speech.recognition.RecognizerMode;
import javax.speech.spi.EngineListFactory;
import javax.speech.synthesis.SynthesizerMode;
import javax.speech.synthesis.Voice;

import org.jvoicexml.jsapi2.mac.recognition.MacRecognizerMode;
import org.jvoicexml.jsapi2.mac.synthesis.MacSynthesizerMode;

/**
 * Factory for the SAPI engines. 
 * @author Dirk Schnelle-Walka
 *
 */
public class MacEngineListFactory implements EngineListFactory {
    static {
        System.loadLibrary("Jsapi2MacBridge");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EngineList createEngineList(final EngineMode require) {
        if (require instanceof SynthesizerMode) {
            final SynthesizerMode mode = (SynthesizerMode) require;
            final Voice[] voices = macGetVoices();
            final SynthesizerMode[] features = new SynthesizerMode[] {
                    new MacSynthesizerMode(null, mode.getEngineName(),
                            mode.getRunning(), mode.getSupportsLetterToSound(),
                            Boolean.TRUE, voices)
            };
            return new EngineList(features);
        }            
        if (require instanceof RecognizerMode) {
                final RecognizerMode[] features = new RecognizerMode[] {
                        new MacRecognizerMode()
            };
            return new EngineList(features);
        }
        
        return null;
    }

    /**
     * Retrieves all voices.
     * @return all voices
     */
    private native Voice[] macGetVoices();

}
