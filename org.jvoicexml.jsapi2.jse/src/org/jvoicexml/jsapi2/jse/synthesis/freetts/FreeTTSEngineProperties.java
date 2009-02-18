package org.jvoicexml.jsapi2.jse.synthesis.freetts;

import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.Voice;

import org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizerProperties;


/**
 * Engine properties for FreeTTS.
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class FreeTTSEngineProperties extends BaseSynthesizerProperties {
    /**
     * Constructs a new object.
     * @param synthesizer the associated synthesizer
     */
    public FreeTTSEngineProperties(final Synthesizer synthesizer) {
        super(synthesizer);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setVoice(final Voice voice) {
        if (voice instanceof FreeTTSVoice) {
            FreeTTSVoice freettsVoice = (FreeTTSVoice) voice;
            final FreeTTSSynthesizer engine = (FreeTTSSynthesizer) getEngine();
            boolean ok = engine.setCurrentVoice(freettsVoice);
            if (!ok) {
                return;
            }
        }

        super.setVoice(voice);
    }
}
