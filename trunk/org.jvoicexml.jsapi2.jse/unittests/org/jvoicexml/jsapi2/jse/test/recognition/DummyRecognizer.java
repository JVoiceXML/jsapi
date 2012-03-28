/**
 * 
 */
package org.jvoicexml.jsapi2.jse.test.recognition;

import java.io.InputStream;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.recognition.RecognizerMode;

import org.jvoicexml.jsapi2.jse.ThreadSpeechEventExecutor;
import org.jvoicexml.jsapi2.jse.recognition.JseBaseRecognizer;

/**
 * Dummy implementation of a recognizer for test purposes.
 * @author Dirk Schnelle-Walka
 *
 */
public class DummyRecognizer extends JseBaseRecognizer {
    /**
     * Construts a new object.
     */
    public DummyRecognizer() {
        super(new RecognizerMode());
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#getBuiltInGrammars()
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Vector getBuiltInGrammars() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#handleAllocate()
     */
    @Override
    protected void handleAllocate() throws EngineStateException,
            EngineException, AudioException, SecurityException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#handleDeallocate()
     */
    @Override
    protected void handleDeallocate() {
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#handlePause()
     */
    @Override
    protected void handlePause() {
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#handlePause(int)
     */
    @Override
    protected void handlePause(int flags) {
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#handleResume()
     */
    @Override
    protected boolean handleResume(InputStream in) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    protected boolean setGrammars(Vector grammarDefinition) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void handleRequestFocus() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void handleReleaseFocus() {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SpeechEventExecutor createSpeechEventExecutor() {
        return new ThreadSpeechEventExecutor();
    }

    @Override
    protected AudioFormat getAudioFormat() {
        // TODO Auto-generated method stub
        return null;
    }
}
