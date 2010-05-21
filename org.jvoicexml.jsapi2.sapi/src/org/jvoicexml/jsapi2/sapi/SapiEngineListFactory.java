package org.jvoicexml.jsapi2.sapi;
import javax.speech.EngineList;
import javax.speech.EngineMode;
import javax.speech.spi.EngineListFactory;
import javax.speech.synthesis.SynthesizerMode;

import org.jvoicexml.jsapi2.sapi.synthesis.SapiSynthesizerMode;

/**
 * Factory for the SAPI engines. 
 * @author Dirk Schnelle-Walka
 *
 */
public class SapiEngineListFactory implements EngineListFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public EngineList createEngineList(final EngineMode require) {
        if (require instanceof SynthesizerMode) {
            final SynthesizerMode[] features = new SynthesizerMode[] {
                    new SapiSynthesizerMode()
            };
            return new EngineList(features);
        }
        return null;
    }

}
