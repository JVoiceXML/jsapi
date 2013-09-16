package org.jvoicexml.jsapi2.mock.recognition;

import java.io.InputStream;
import java.util.Collection;

import javax.sound.sampled.AudioFormat;
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

import org.jvoicexml.jsapi2.BaseEngineProperties;
import org.jvoicexml.jsapi2.jse.recognition.JseBaseRecognizer;
import org.jvoicexml.jsapi2.mock.MockAudioManager;
import org.jvoicexml.jsapi2.mock.MockSpeechEventExecutor;
import org.jvoicexml.jsapi2.recognition.GrammarDefinition;

/**
 * Dummy implementation of a {@link javax.spech.recognition.Recognizer}
 * for test purposes.
 * @author Dirk Schnelle-Walka
 */
public class MockRecognizer extends JseBaseRecognizer {

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
    public Collection<Grammar> getBuiltInGrammars() {
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

    @Override
    protected boolean setGrammars(
            Collection<GrammarDefinition> grammarDefinition) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void handlePropertyChangeRequest(BaseEngineProperties properties,
            String propName, Object oldValue, Object newValue) {
        properties.commitPropertyChange(propName, oldValue, newValue);
    }

    @Override
    protected AudioFormat getAudioFormat() {
        // TODO Auto-generated method stub
        return null;
    }
}
