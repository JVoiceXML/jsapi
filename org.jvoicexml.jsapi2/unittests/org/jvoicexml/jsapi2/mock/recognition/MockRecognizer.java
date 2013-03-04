package org.jvoicexml.jsapi2.mock.recognition;

import java.io.InputStream;
import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultEvent;

import org.jvoicexml.jsapi2.mock.MockAudioManager;
import org.jvoicexml.jsapi2.mock.MockSpeechEventExecutor;
import org.jvoicexml.jsapi2.recognition.BaseRecognizer;

/**
 * Dummy implementation of a {@link javax.spech.recognition.Recognizer}
 * for test purposes.
 * @author Dirk Schnelle-Walka
 */
public class MockRecognizer extends BaseRecognizer {

    /**
     * {@inheritDoc}
     */
    protected GrammarManager createGrammarManager() {
        return new MockGrammarManager();
    }

    /**
     * {@inheritDoc}
     */
    protected void postResultEvent(Result result, ResultEvent event,
            SpeechEventExecutor executor) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    protected boolean processGrammar(Grammar grammar) throws GrammarException {
        return false;
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
    protected void handlePause(int flags) {
    }

    /**
     * {@inheritDoc}
     */
    protected boolean handleResume(InputStream in) throws EngineStateException {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    protected boolean setGrammars(Vector grammarDefinition) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    protected void handleRequestFocus() {
    }

    /**
     * {@inheritDoc}
     */
    protected void handleReleaseFocus() {
    }

    /**
     * {@inheritDoc}
     */
    public Vector getBuiltInGrammars() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    protected AudioManager createAudioManager() {
        return new MockAudioManager();
    }

    /**
     * {@inheritDoc}
     */
    protected SpeechEventExecutor createSpeechEventExecutor() {
        return new MockSpeechEventExecutor();
    }
}
