/**
 * 
 */
package org.jvoicexml.jsapi2.test;

import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.EngineEvent;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.VocabularyManager;

import org.jvoicexml.jsapi2.BaseEngine;

/**
 * An engine for test purposes.
 * @author Dirk Schnelle-Walka
 */
public class DummyEngine extends BaseEngine {
    public DummyEngine() {
        super(null);
    }

    /**
     * {@inheritDoc}
     */
    protected void baseAllocate() throws AudioException, EngineException,
            EngineStateException, SecurityException {
    }

    /**
     * {@inheritDoc}
     */
    protected AudioManager createAudioManager() {
        return new DummyAudioManager();;
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
    protected SpeechEventExecutor createSpeechEventExecutor() {
        return new DummySpeechEventExecutor();
    }

    /**
     * {@inheritDoc}
     */
    protected void baseDeallocate() throws EngineStateException,
            EngineException, AudioException {
    }

    /**
     * {@inheritDoc}
     */
    protected void basePause() throws EngineStateException {
    }

    /**
     * {@inheritDoc}
     */
    protected boolean baseResume() throws EngineStateException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    protected void fireEvent(EngineEvent event) {
    }

    /**
     * {@inheritDoc}
     */
    protected EngineEvent createStateTransitionEngineEvent(long oldState, long newState,
            int id) {
        return new DummyEngineEvent(this, id, oldState, newState, null);
    }
}
