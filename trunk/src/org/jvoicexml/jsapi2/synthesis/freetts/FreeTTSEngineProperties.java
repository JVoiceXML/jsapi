package org.jvoicexml.jsapi2.synthesis.freetts;

import org.jvoicexml.jsapi2.synthesis.BaseSynthesizerProperties;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.Voice;
import javax.speech.*;


/**
 * <p>Title: JSAPI2Engines</p>
 *
 * <p>Description: JSAPI 2.0 Engines implementations</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: INESC-ID L2F</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class FreeTTSEngineProperties extends BaseSynthesizerProperties {
    public FreeTTSEngineProperties(Synthesizer synthesizer) {
        super(synthesizer);
    }


    public void setVoice(Voice voice) {
        if (voice instanceof FreeTTSVoice) {
            FreeTTSVoice freettsVoice = (FreeTTSVoice)voice;
            boolean ok = ((FreeTTSSynthesizer) engine).setCurrentVoice(freettsVoice);
            if (!ok) return;

            super.setVoice(voice);
        }
    }
}
