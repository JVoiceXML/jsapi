package org.jvoicexml.jsapi2.jse.synthesis.freetts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.AudioSegment;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SynthesizerMode;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestEvent;
import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.BaseAudioSegment;
import org.jvoicexml.jsapi2.jse.JseBaseAudioManager;
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;

import com.sun.speech.freetts.FreeTTSSpeakableImpl;
import com.sun.speech.freetts.audio.AudioPlayer;


/**
 * Copyright 2003 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */

/**
 * Provides partial support for a JSAPI 2.0 synthesizer for the FreeTTS speech
 * synthesis system.
 */
public class FreeTTSSynthesizer extends JseBaseSynthesizer
    implements EnginePropertyChangeRequestListener {
    /** Logger for this class. */
    private static final Logger LOGGER =
            Logger.getLogger(FreeTTSSynthesizer.class.getName());

    /**
     * The currently active voice for this synthesizer.
     */
    private FreeTTSVoice curVoice;

    /** The audio player to use. */
    private AudioPlayer audioPlayer;

    /** The ssml to jsml transformer. */
    private final Ssml2JsmlTransformer transformer = new Ssml2JsmlTransformer();

    /**
     * All voice output for this synthesizer goes through this central utterance
     * queue
     */
    // ////////////////////////////////////////////////////////////////////
    // private OutputQueue outputQueue;
    /**
     * Creates a new Synthesizer in the DEALLOCATED state.
     *
     * @param desc
     *                describes the allowed mode of operations for this
     *                synthesizer.
     */
    public FreeTTSSynthesizer(final FreeTTSSynthesizerMode desc) {
        super(desc);
        audioPlayer = null;
        super.setSynthesizerProperties(new FreeTTSEngineProperties(this));

        ((JseBaseAudioManager) getAudioManager()).setEngineAudioFormat(new
                AudioFormat(8000f, 16, 1, true, true));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleAllocate()  throws EngineStateException,
        EngineException, AudioException, SecurityException {
        boolean ok = false;
        SynthesizerMode synthesizerMode = (SynthesizerMode) getEngineMode();

        if (synthesizerMode.getVoices().length > 0) {
            FreeTTSVoice freettsVoice = (FreeTTSVoice) synthesizerMode
                    .getVoices()[0];
            ok = setCurrentVoice(freettsVoice);
        }

        if (ok) {
            JseBaseAudioManager manager = (JseBaseAudioManager) getAudioManager();
            audioPlayer = new FreeTTSAudioPlayer(manager);

            synchronized (engineStateLock) {
                long newState = ALLOCATED | RESUMED;
                newState |= (getQueueManager().isQueueEmpty() ? QUEUE_EMPTY
                        : QUEUE_NOT_EMPTY);
                setEngineState(CLEAR_ALL_STATE, newState);
            }
        } else {
            throw new AudioException("Can't allocate FreeTTS synthesizer");
        }
    }

    /**
     * Sets the given voice to be the current voice. If the voice cannot be
     * loaded, this call has no affect.
     *
     * @param voice
     *                the new voice.
     */
    protected boolean setCurrentVoice(FreeTTSVoice voice) {

        com.sun.speech.freetts.Voice freettsVoice = voice.getVoice();
        boolean ok = false;

        // Load the voice if it is not loaded.
        if (!freettsVoice.isLoaded()) {
            // ////////////////////////////////////////////////////////////////
            // freettsVoice.setOutputQueue(outputQueue);
            freettsVoice.allocate();
            /*
             * audio = freettsVoice.getAudioPlayer(); if (audio == null) { audio =
             * new com.sun.speech.freetts.audio.JavaClipAudioPlayer(); } if
             * (audio == null) { throw new EngineException("Can't get audio
             * player"); }
             */
            freettsVoice.setAudioPlayer(audioPlayer);
        }

        if (freettsVoice.isLoaded()) {
            curVoice = voice;
            ok = true;
            // notify the world of potential property changes
            // /////////////////////FreeTTSSynthesizerProperties props =
            // /////////////////////(FreeTTSSynthesizerProperties)
            // getSynthesizerProperties();
            // /////////////////////props.checkForPropertyChanges();
        }
        return ok;
    }

    /**
     * Handles a deallocation request. Cancels all pending items, terminates the
     * output handler, and posts the state changes.
     *
     * @throws EngineException
     *                 if a deallocation error occurs
     */
    public void handleDeallocate() {
        setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
        getQueueManager().cancelAllItems();
        getQueueManager().terminate();

        // Close the audio. This should flush out any queued audio data

        if (audioPlayer != null) {
            audioPlayer.close();
        }
    }

    /**
     * Factory method to create a BaseSynthesizerQueueItem.
     *
     * @return a queue item appropriate for this synthesizer
     */
    /*
     * protected BaseSynthesizerQueueItem createQueueItem() { return new
     * FreeTTSSynthesizerQueueItem(); }
     */

    /**
     * Returns an enumeration of the queue.
     *
     * @return an enumeration of the contents of the queue. This enumeration
     *         contains FreeTTSSynthesizerQueueItem objects
     *
     * @throws EngineStateError
     *                 if the engine was not in the proper state
     */
    /*
     * public Enumeration enumerateQueue() throws EngineStateException {
     * checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES); return
     * outputHandler.enumerateQueue(); }
     */

    /**
     * Places an item on the speaking queue and send the queue update event.
     *
     * @param item
     *                the item to place in the queue
     */
    /*
     * protected void appendQueue(BaseSynthesizerQueueItem item) {
     * outputHandler.appendQueue((FreeTTSSynthesizerQueueItem) item); }
     */

    /**
     * Cancels the item at the top of the queue.
     *
     * @throws EngineStateError
     *                 if the synthesizer is not in the proper state
     */
    /*
     * public void cancel() throws EngineStateException {
     * checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
     * outputHandler.cancelItem(); }
     */

    /**
     * Cancels a specific object on the queue.
     *
     * @param source
     *                the object to cancel
     *
     * @throws IllegalArgumentException
     *                 if the source object is not currently in the queue
     * @throws EngineStateError
     *                 the synthesizer is not in the proper state
     */
    /*
     * public void cancel(Object source) throws IllegalArgumentException,
     * EngineStateException { checkEngineState(DEALLOCATED |
     * DEALLOCATING_RESOURCES); outputHandler.cancelItem(source); }
     */

    /**
     * Cancels all items on the output queue.
     *
     * @throws EngineStateError
     */
    /*
     * public void cancelAll() throws EngineStateException {
     * checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
     * outputHandler.cancelAllItems(); }
     */

    /**
     * Pauses the output.
     */
    public void handlePause() {
        audioPlayer.pause();
    }

    /**
     * Resumes the output.
     */
    public boolean handleResume() {
        audioPlayer.resume();
        return true;
    }

    protected Speakable parseMarkup(String synthesisMarkup) {
        return null;
    }

    /**
     * Outputs the given queue item to the current voice
     *
     * @param item
     *                the item to output
     */
    private void handleSpeak(final int id,
            final FreeTTSSpeakableImpl speakElement) {
        com.sun.speech.freetts.Voice voice = curVoice.getVoice();
        voice.setAudioPlayer(audioPlayer);

        voice.speak(speakElement);

        if (audioPlayer instanceof FreeTTSAudioPlayer) {
            FreeTTSAudioPlayer player = (FreeTTSAudioPlayer) audioPlayer;
            InputStream in;
            try {
                in = new ByteArrayInputStream(player.getAudioBytes());
            } catch (IOException e) {
                LOGGER.warning(e.getLocalizedMessage());
                return;
            }
            final AudioManager manager = getAudioManager();
            final String locator = manager.getMediaLocator();
            final String markupText = speakElement.getText();
            final AudioSegment segment;
            if (locator == null) {
                segment = new BaseAudioSegment(markupText, in);
            } else {
                segment = new BaseAudioSegment(locator, markupText, in);
            }
            setAudioSegment(id, segment);
            player.reset();
        }
    }

    public void handleSpeak(int id, Speakable item) {
        handleSpeak(id, new FreeTTSSpeakableImpl(transformer.transform(item.getMarkupText())));
    }

    public void handleSpeak(int id, String text) {
        handleSpeak(id, new FreeTTSSpeakableImpl(text));
    }

    public boolean handleCancelAll() {
        return false;
    }

    public boolean handleCancel(int id) {
        return false;
    }

    public boolean handleCancel() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChangeRequest(
            final EnginePropertyChangeRequestEvent event) {
    }

    @Override
    protected AudioFormat getAudioFormat() {
        return new AudioFormat(8000f, 16, 1, true, true);
    }
}
