package org.jvoicexml.jsapi2.jse.synthesis;

import java.util.logging.Logger;

import javax.speech.AudioManager;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerMode;

import org.jvoicexml.jsapi2.synthesis.BaseSynthesizer;


/**
 * <p>
 * Title: JSAPI 2.0
 * </p>
 *
 * <p>
 * Description: An independent reference implementation of JSR 113
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 *
 * <p>
 * Company: JVoiceXML group - http://jvoicexml.sourceforge.net
 * </p>
 *
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 * @version $Revision: $
 */
public abstract class JseBaseSynthesizer extends BaseSynthesizer
    implements Synthesizer {
    /** Logger for this class. */
    private static final Logger LOGGER =
            Logger.getLogger(JseBaseSynthesizer.class.getName());

    public JseBaseSynthesizer() {
        this(null);
    }

    public JseBaseSynthesizer(SynthesizerMode engineMode) {
        super(engineMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AudioManager createAudioManager() {
        final BaseSynthesizerAudioManager manager =
            new BaseSynthesizerAudioManager();
        manager.setEngine(this);
        return manager;
    }
}
