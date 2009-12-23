package org.jvoicexml.jsapi2.synthesis;

import java.util.Enumeration;
import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.AudioSegment;
import javax.speech.EngineEvent;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.synthesis.PhoneInfo;
import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SpeakableException;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerEvent;
import javax.speech.synthesis.SynthesizerListener;
import javax.speech.synthesis.SynthesizerMode;
import javax.speech.synthesis.SynthesizerProperties;

import org.jvoicexml.jsapi2.BaseEngine;
import org.jvoicexml.jsapi2.BaseEngineProperties;


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
public abstract class BaseSynthesizer extends BaseEngine
    implements Synthesizer {
    protected Vector speakableListeners;
    protected SynthesizerProperties synthesizerProperties;
    protected int speakableMask;
    protected final QueueManager queueManager;

    public BaseSynthesizer() {
        this(null);
    }

    public BaseSynthesizer(SynthesizerMode engineMode) {
        super(engineMode);
        speakableListeners = new Vector();
        final SynthesizerProperties props =
            new BaseSynthesizerProperties(this);
        setSynthesizerProperties(props);
        speakableMask = SpeakableEvent.DEFAULT_MASK;
        setEngineMask(getEngineMask() | SynthesizerEvent.DEFAULT_MASK);
        queueManager = new QueueManager(this);
    }

    /**
     * {@inheritDoc}
     */
    public void fireEvent(final EngineEvent event) {
        synchronized (engineListeners) {
            final SynthesizerEvent synthesizerEvent =
                (SynthesizerEvent) event;
            Enumeration enumeration = engineListeners.elements();
            while (enumeration.hasMoreElements()) {
                final SynthesizerListener listener =
                    (SynthesizerListener) enumeration.nextElement();
                listener.synthesizerUpdate(synthesizerEvent);
            }
        }
    }

    /**
     * postEngineEvent
     *
     * @param oldState long
     * @param newState long
     * @param eventType int
     * @todo Implement this org.jvoicexml.jsapi2.jse.j2se.BaseEngine method
     */
    public void postEngineEvent(long oldState, long newState, int eventType) {
        final SynthesizerEvent event = new SynthesizerEvent(this, eventType,
                oldState, newState, null, false);
        /** @todo Change after adding the queue */

        postEngineEvent(event);
    }

    protected void postSynthesizerEvent(long oldState, long newState,
            int eventType, boolean changedTopOfQueue) {
        switch (eventType){
        case SynthesizerEvent.QUEUE_UPDATED :
        case SynthesizerEvent.QUEUE_EMPTIED :
            break;
        default:
            changedTopOfQueue = false;
        }
        final SynthesizerEvent event = new SynthesizerEvent(this,
                eventType,
                oldState,
                newState,
                null,
                changedTopOfQueue);

        postEngineEvent(event);

    }

    protected void postSpeakableEvent(final SpeakableEvent event,
            final SpeakableListener extraSpeakableListener) {
        if ((getSpeakableMask() & event.getId()) == event.getId()) {
            try {
                final SpeechEventExecutor executor = getSpeechEventExecutor();
                executor.execute(new Runnable() {
                    public void run() {
                        fireSpeakableEvent(event, extraSpeakableListener);
                    }
                });
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Utility function to send a speakable event to all grammar listeners.
     */
    public void fireSpeakableEvent(SpeakableEvent event,
            SpeakableListener extraSpeakableListener) {
        if (extraSpeakableListener != null) {
            extraSpeakableListener.speakableUpdate(event);
        }

        if (speakableListeners != null) {
            final Enumeration e = speakableListeners.elements();
            while (e.hasMoreElements()) {
                final SpeakableListener sl =
                    (SpeakableListener) e.nextElement();
                sl.speakableUpdate(event);
            }
        }
    }

    protected boolean isValid(long state) {
        if (testEngineState(QUEUE_EMPTY | QUEUE_NOT_EMPTY))
            return false;

        return super.isValid(state);
    }

    public void addSpeakableListener(SpeakableListener listener) {
        if (!speakableListeners.contains(listener)) {
            speakableListeners.addElement(listener);
        }
    }

    public void removeSpeakableListener(SpeakableListener listener) {
        speakableListeners.removeElement(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void addSynthesizerListener(
            final SynthesizerListener listener) {
        addEngineListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeSynthesizerListener(
            final SynthesizerListener listener) {
        removeEngineListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    public boolean cancel() throws EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        // Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
                return false;
            }
        }
        return queueManager.cancelItem();
    }

    public boolean cancel(int id) throws IllegalArgumentException,
            EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        // Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
                return false;
            }
        }
        return queueManager.cancelItem(id);
    }

    public boolean cancelAll() throws EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        // Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
                return false;
            }
        }
        return queueManager.cancelAllItems();
    }

    public String getPhonemes(String text) throws EngineStateException {
        return "";
    }

    public SynthesizerProperties getSynthesizerProperties() {
        return synthesizerProperties;
    }

    public void setSynthesizerProperties(
            SynthesizerProperties synthesizerProperties) {
        this.synthesizerProperties = synthesizerProperties;
        if (synthesizerProperties instanceof BaseEngineProperties) {
            final BaseEngineProperties props =
                (BaseEngineProperties) synthesizerProperties;
            addEnginePropertyChangeRequestListener(props);
        }
    }

    public void setSpeakableMask(int mask) {
        speakableMask = mask;
    }

    public int getSpeakableMask() {
        return speakableMask;
    }

    public int speak(AudioSegment audio, SpeakableListener listener)
        throws EngineStateException, IllegalArgumentException {
        return queueManager.appendItem(audio, listener);
    }


    public int speak(Speakable speakable, SpeakableListener listener)
            throws EngineStateException, SpeakableException,
            IllegalArgumentException {

        // Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
                return -1;
            }
        }

        return queueManager.appendItem(speakable, listener);
    }

    /**
     * {@inheritDoc}
     */
    public int speak(final String text, final SpeakableListener listener)
            throws EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        // Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                final long delay = 300;
                waitEngineState(ALLOCATED, delay);
            } catch (InterruptedException ex) {
                throw new EngineStateException(
                        "wait engine state interrupted: " + ex.getMessage());
            }
        }

        return queueManager.appendItem(getSpeakable(text), listener, text);
    }

    public int speakMarkup(final String synthesisMarkup,
            SpeakableListener listener) throws EngineStateException,
            SpeakableException, IllegalArgumentException {

        // Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
                return -1;
            }
        }

        Speakable speakable = new Speakable() {
            public String getMarkupText() {
                return synthesisMarkup;
            }
        };

        return queueManager.appendItem(speakable, listener);
    }

    /**
     * {@inheritDoc}
     */
    protected final void baseAllocate() throws EngineStateException,
            EngineException, AudioException, SecurityException {

        // Starts AudioManager
        final AudioManager audioManager = getAudioManager();
        audioManager.audioStart();

        // Procceed to real engine allocation
        handleAllocate();
        long[] states = setEngineState(CLEAR_ALL_STATE, ALLOCATED | RESUMED
                | DEFOCUSED);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_ALLOCATED);
    }

    /**
     * Perform the real allocation of the synthesizer.
     * @throws AudioException
     *          if any audio request fails 
     * @throws EngineException
     *          if an allocation error occurred or the Engine is not
     *          operational. 
     * @throws EngineStateException
     *          if called for an Engine in the DEALLOCATING_RESOURCES state 
     * @throws SecurityException
     *          if the application does not have permission for this Engine
     */
    protected abstract void handleAllocate() throws EngineStateException,
        EngineException, AudioException, SecurityException;


    /**
     * Called from the <code>deallocate</code> method. Override this in
     * subclasses.
     *
     * @throws EngineException
     *                 if this <code>Engine</code> cannot be deallocated.
     */
    protected boolean baseDeallocate() throws EngineStateException,
            EngineException, AudioException {

        // Stops AudioManager
        final AudioManager audioManager = getAudioManager();
        audioManager.audioStop();

        // Procceed to real engine deallocation
        boolean status = handleDeallocate();
        if (status) {
            long states[] = setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
            postEngineEvent(states[0], states[1],
                    EngineEvent.ENGINE_DEALLOCATED);
        }

        return status;
    }

    protected boolean basePause() {
        return handlePause();
    }

    /**
     * Called from the <code>resume</code> method. Override in subclasses.
     *
     * @todo Handle grammar updates
     */
    protected boolean baseResume() {
        return handleResume();
    }

    abstract protected boolean handleDeallocate();

    abstract protected boolean handlePause();

    abstract protected boolean handleResume();

    /** @todo Is it really needed? */
    protected abstract Speakable getSpeakable(String text);

    /**
     * Speak the item with the given id.
     * @param id id of the text to speak
     * @param item the text to speak
     */
    protected abstract void handleSpeak(int id, String item);

    protected abstract void handleSpeak(int id, Speakable item);

    /**
     * Returns a <code>String</code> of the names of all the
     * <code>Engine</code> states in the given <code>Engine</code> state.
     *
     * @param state
     *                the bitmask of states
     *
     * @return a <code>String</code> containing the names of all the states
     *         set in <code>state</code>
     */
    protected String stateToString(long state) {
        StringBuffer buf = new StringBuffer(super.stateToString(state));
        if ((state & Synthesizer.QUEUE_EMPTY) != 0)
            buf.append(" QUEUE_EMPTY ");
        if ((state & Synthesizer.QUEUE_EMPTY) != 0)
            buf.append(" QUEUE_EMPTY ");
        return buf.toString();
    }

    protected abstract boolean handleCancel();

    protected abstract boolean handleCancelAll();

    protected abstract boolean handleCancel(final int id);

    /**
     * Sets AudioSegment in a queueItem.
     *
     * @param id current id
     * @param audioSegment the audio segment.
     */
    protected void setAudioSegment(final int id,
            final AudioSegment audioSegment) {
        queueManager.setAudioSegment(id, audioSegment);
    }

    /**
     * Utility method to set words in a queue item.
     *
     * @param itemId the id of the queued item
     * @param String[] words
     */
    protected void setWords(int itemId, String[] words) {
        queueManager.setWords(itemId, words);
    }

    /**
     * Set words times in a queueItem (Not JSAPI2)
     *
     * @param itemId int
     * @param float[] words
     */
    protected void setWordsStartTimes(int itemId, float[] starttimes) {
        queueManager.setWordsStartTimes(itemId, starttimes);
    }

    protected void setPhonesInfo(int itemId, PhoneInfo[] phonesinfo) {
        queueManager.setPhonesInfo(itemId, phonesinfo);
    }

    /**
     * Retrieves the queue manager.
     * @return the queue manager
     */
    protected QueueManager getQueueManager() {
        return queueManager;
    }


}
