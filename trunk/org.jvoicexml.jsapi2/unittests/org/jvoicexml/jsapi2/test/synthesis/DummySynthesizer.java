/**
 * 
 */
package org.jvoicexml.jsapi2.test.synthesis;

import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.AudioSegment;
import javax.speech.EngineException;
import javax.speech.EngineMode;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.VocabularyManager;
import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SpeakableException;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerListener;
import javax.speech.synthesis.SynthesizerProperties;

import org.jvoicexml.jsapi2.ThreadSpeechEventExecutor;

/**
 * @author dirk
 *
 */
public class DummySynthesizer implements Synthesizer {

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#addSpeakableListener(javax.speech.synthesis.SpeakableListener)
     */
    public void addSpeakableListener(SpeakableListener listener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#addSynthesizerListener(javax.speech.synthesis.SynthesizerListener)
     */
    public void addSynthesizerListener(SynthesizerListener listener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#cancel()
     */
    public boolean cancel() throws EngineStateException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#cancel(int)
     */
    public boolean cancel(int id) throws EngineStateException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#cancelAll()
     */
    public boolean cancelAll() throws EngineStateException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#getPhonemes(java.lang.String)
     */
    public String getPhonemes(String text) throws EngineStateException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#getSpeakableMask()
     */
    public int getSpeakableMask() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#getSynthesizerProperties()
     */
    public SynthesizerProperties getSynthesizerProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#pause()
     */
    public void pause() throws EngineStateException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#removeSpeakableListener(javax.speech.synthesis.SpeakableListener)
     */
    public void removeSpeakableListener(SpeakableListener listener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#removeSynthesizerListener(javax.speech.synthesis.SynthesizerListener)
     */
    public void removeSynthesizerListener(SynthesizerListener listener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#resume()
     */
    public boolean resume() throws EngineStateException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#setSpeakableMask(int)
     */
    public void setSpeakableMask(int mask) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#speak(javax.speech.AudioSegment, javax.speech.synthesis.SpeakableListener)
     */
    public int speak(AudioSegment audio, SpeakableListener listener)
            throws SpeakableException, EngineStateException,
            IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#speak(javax.speech.synthesis.Speakable, javax.speech.synthesis.SpeakableListener)
     */
    public int speak(Speakable speakable, SpeakableListener listener)
            throws SpeakableException, EngineStateException {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#speak(java.lang.String, javax.speech.synthesis.SpeakableListener)
     */
    public int speak(String text, SpeakableListener listener)
            throws EngineStateException {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.speech.synthesis.Synthesizer#speakMarkup(java.lang.String, javax.speech.synthesis.SpeakableListener)
     */
    public int speakMarkup(String synthesisMarkup, SpeakableListener listener)
            throws SpeakableException, EngineStateException {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#allocate()
     */
    public void allocate() throws AudioException, EngineException,
            EngineStateException, SecurityException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#allocate(int)
     */
    public void allocate(int mode) throws IllegalArgumentException,
            AudioException, EngineException, EngineStateException,
            SecurityException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#deallocate()
     */
    public void deallocate() throws AudioException, EngineException,
            EngineStateException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#deallocate(int)
     */
    public void deallocate(int mode) throws IllegalArgumentException,
            AudioException, EngineException, EngineStateException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#getAudioManager()
     */
    public AudioManager getAudioManager() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#getEngineMask()
     */
    public int getEngineMask() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#getEngineMode()
     */
    public EngineMode getEngineMode() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#getEngineState()
     */
    public long getEngineState() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#getSpeechEventExecutor()
     */
    public SpeechEventExecutor getSpeechEventExecutor() {
        return new ThreadSpeechEventExecutor();
    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#getVocabularyManager()
     */
    public VocabularyManager getVocabularyManager() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#setEngineMask(int)
     */
    public void setEngineMask(int mask) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#setSpeechEventExecutor(javax.speech.SpeechEventExecutor)
     */
    public void setSpeechEventExecutor(SpeechEventExecutor speechEventExecutor) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#testEngineState(long)
     */
    public boolean testEngineState(long state) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#waitEngineState(long)
     */
    public long waitEngineState(long state) throws InterruptedException,
            IllegalArgumentException, IllegalStateException {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.speech.Engine#waitEngineState(long, long)
     */
    public long waitEngineState(long state, long timeout)
            throws InterruptedException, IllegalArgumentException,
            IllegalStateException {
        // TODO Auto-generated method stub
        return 0;
    }

}
