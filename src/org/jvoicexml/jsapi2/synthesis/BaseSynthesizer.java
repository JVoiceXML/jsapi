package org.jvoicexml.jsapi2.synthesis;

import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.SynthesizerListener;
import javax.speech.synthesis.SynthesizerProperties;
import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SynthesisException;
import javax.speech.EngineMode;
import javax.speech.EngineEvent;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.AudioSegment;
import org.jvoicexml.jsapi2.BaseEngine;
import javax.speech.synthesis.SynthesizerMode;
import java.util.Vector;
import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SynthesizerEvent;
import java.util.Enumeration;
import javax.speech.EngineListener;

/**
 * <p>Title: JSAPI 2.0</p>
 *
 * <p>Description: An independent reference implementation of JSR 113</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class BaseSynthesizer extends BaseEngine implements Synthesizer {

    protected Vector speakableListeners;
    protected BaseSynthesizerProperties synthesizerProperties;
    protected int speakableMask;

    public BaseSynthesizer() {
        this(null);
    }

    public BaseSynthesizer(SynthesizerMode engineMode) {
        super(engineMode);
        speakableListeners = new Vector();
        synthesizerProperties = new BaseSynthesizerProperties(this);
        speakableMask = SpeakableEvent.DEFAULT_MASK;
        setEngineMask(getEngineMask() | SynthesizerEvent.DEFAULT_MASK);
    }

    /**
     * fireEvent
     *
     * @param event EngineEvent
     */
    public void fireEvent(EngineEvent event) {
        Enumeration listeners = engineListeners.elements();
        while (listeners.hasMoreElements()) {
            EngineListener el = (EngineListener) listeners.nextElement();
            ((SynthesizerListener) el).synthesizerUpdate((SynthesizerEvent) event);
        }
    }


    /**
     * Called from the <code>allocate</code> method.
     *
     * @throws EngineException if problems are encountered
     * @return boolean
     * @todo Implement this org.jvoicexml.jsapi2.BaseEngine method
     */
    protected boolean handleAllocate() throws EngineException {
        return false;
    }

    /**
     * Called from the <code>deallocate</code> method.
     *
     * @throws EngineException if this <code>Engine</code> cannot be
     *   deallocated.
     * @return boolean
     * @todo Implement this org.jvoicexml.jsapi2.BaseEngine method
     */
    protected boolean handleDeallocate() throws EngineException {
        return false;
    }

    /**
     * Called from the <code>pause</code> method.
     *
     * @return boolean
     * @todo Implement this org.jvoicexml.jsapi2.BaseEngine method
     */
    protected boolean handlePause() {
        return false;
    }

    /**
     * Called from the <code>resume</code> method.
     *
     * @return boolean
     * @todo Implement this org.jvoicexml.jsapi2.BaseEngine method
     */
    protected boolean handleResume() {
        return false;
    }

    /**
     * postEngineEvent
     *
     * @param oldState long
     * @param newState long
     * @param eventType int
     * @todo Implement this org.jvoicexml.jsapi2.BaseEngine method
     */
    protected void postEngineEvent(long oldState, long newState, int eventType) {
        final SynthesizerEvent event = new SynthesizerEvent(this,
              eventType,
              oldState,
              newState,
              null,
              false);    /** @todo Change after adding the queue */

      postEngineEvent(event);
    }

    public void addSpeakableListener(SpeakableListener listener) {
        if (!speakableListeners.contains(listener))
            speakableListeners.addElement(listener);
    }

    public void removeSpeakableListener(SpeakableListener listener) {
        speakableListeners.removeElement(listener);
    }

    public void addSynthesizerListener(SynthesizerListener listener) {
        super.addEngineListener(listener);
    }

    public void removeSynthesizerListener(SynthesizerListener listener) {
        super.removeEngineListener(listener);
    }

    public void cancel() throws EngineStateException {
    }

    public void cancel(int id) throws IllegalArgumentException, EngineStateException {
    }

    public void cancelAll() throws EngineStateException {
    }

    public String getPhonemes(String text) throws EngineStateException {
        return "";
    }

    public SynthesizerProperties getSynthesizerProperties() {
        return synthesizerProperties;
    }

    public void setSpeakableMask(int mask) {
        speakableMask = mask;
    }

    public int getSpeakableMask() {
        return speakableMask;
    }

    public int speak(AudioSegment audio, SpeakableListener listener) throws
            EngineStateException, IllegalArgumentException {
        return 0;
    }

    public int speak(Speakable speakable, SpeakableListener listener) throws
            EngineStateException, SynthesisException, IllegalArgumentException {
        return 0;
    }

    public int speak(String text, SpeakableListener listener) throws
            EngineStateException {
        return 0;
    }

    public int speakMarkup(String synthesisMarkup, SpeakableListener listener) throws
            EngineStateException, SynthesisException, IllegalArgumentException {
        return 0;
    }
}
