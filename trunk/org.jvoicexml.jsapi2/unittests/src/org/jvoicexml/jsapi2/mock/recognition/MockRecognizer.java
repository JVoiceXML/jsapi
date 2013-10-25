package org.jvoicexml.jsapi2.mock.recognition;

import java.io.InputStream;
import java.util.Collection;

import javax.sound.sampled.AudioFormat;
import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.RecognizerMode;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultEvent;

import org.jvoicexml.jsapi2.BaseEngineProperties;
import org.jvoicexml.jsapi2.jse.recognition.BaseGrammarManager;
import org.jvoicexml.jsapi2.mock.MockSpeechEventExecutor;
import org.jvoicexml.jsapi2.recognition.BaseRecognizer;
import org.jvoicexml.jsapi2.recognition.GrammarDefinition;

/**
 * Dummy implementation of a {@link javax.spech.recognition.Recognizer}
 * for test purposes.
 * @author Dirk Schnelle-Walka
 */
public final class MockRecognizer extends BaseRecognizer {

    /**
     * Constructs a new object.
     */
    public MockRecognizer() {
        super(RecognizerMode.DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    protected GrammarManager createGrammarManager() {
        return new BaseGrammarManager(this);
    }

    /**
     * {@inheritDoc}
     */
    protected void postResultEvent(final Result result, final ResultEvent event,
            final SpeechEventExecutor executor) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    protected boolean processGrammar(final Grammar grammar)
            throws GrammarException {
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
    protected void handlePause(final int flags) {
    }

    /**
     * {@inheritDoc}
     */
    protected boolean handleResume(final InputStream in)
            throws EngineStateException {
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
    protected SpeechEventExecutor createSpeechEventExecutor() {
        return new MockSpeechEventExecutor();
    }

    @Override
    protected boolean setGrammars(
            final Collection<GrammarDefinition> grammarDefinition) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void handlePropertyChangeRequest(
            final BaseEngineProperties properties,
            final String propName, final Object oldValue,
            final Object newValue) {
        properties.commitPropertyChange(propName, oldValue, newValue);
    }

    @Override
    protected AudioFormat getAudioFormat() {
        return new AudioFormat(16000f, 16, 1, true, false);
    }
}
