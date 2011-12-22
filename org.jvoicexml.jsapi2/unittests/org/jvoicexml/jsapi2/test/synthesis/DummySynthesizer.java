package org.jvoicexml.jsapi2.test.synthesis;

import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.VocabularyManager;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.synthesis.BaseSynthesizer;
import org.jvoicexml.jsapi2.test.DummyAudioManager;
import org.jvoicexml.jsapi2.test.DummySpeechEventExecutor;

/**
 * Dummy implementation of a {@link javax.spech.synthesis.Synthesizer}
 * for test purposes.
 * @author Dirk Schnelle-Walka
 */
public class DummySynthesizer extends BaseSynthesizer {

    /**
     * {@inheritDoc}
     */
    protected Speakable getSpeakable(String text) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    protected void handleAllocate() throws EngineStateException,
            EngineException, AudioException, SecurityException {
    }

    /**
     * {@inheritDoc}
     */
    protected boolean handleCancel() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    protected boolean handleCancel(int id) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    protected boolean handleCancelAll() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    protected void handleDeallocate() {
    }

    /**
     * {@inheritDoc}
     */
    protected void handlePause() {
    }

    /**
     * {@inheritDoc}
     */
    protected boolean handleResume() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    protected void handleSpeak(int id, String item) {
    }

    /**
     * {@inheritDoc}
     */
    protected void handleSpeak(int id, Speakable item) {
    }

    /**
     * {@inheritDoc}
     */
    protected AudioManager createAudioManager() {
        return new DummyAudioManager();
    }

    /**
     * {@inheritDoc}
     */
    protected SpeechEventExecutor createSpeechEventExecutor() {
        return new DummySpeechEventExecutor();
    }

    /**
     * {@inheritDoc}
     */
    protected VocabularyManager createVocabularyManager() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        return null;
    }
}
