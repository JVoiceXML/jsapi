/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 68 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2012 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.jsapi2.jse.synthesis.freetts;

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
import java.io.OutputStream;



/**
 * Provides partial support for a JSAPI 2.0 synthesizer for the FreeTTS speech
 * synthesis system.
 * @author Dirk Schnelle-Walka
 */
public class FreeTTSSynthesizer extends JseBaseSynthesizer {
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
    public void handleAllocate()
            throws EngineStateException, EngineException, AudioException,
            SecurityException {
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
     */
    @Override
    public void handleDeallocate() throws EngineStateException,
        EngineException, AudioException {
        setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
        getQueueManager().cancelAllItems();
        getQueueManager().terminate();

        // Close the audio. This should flush out any queued audio data

        if (audioPlayer != null) {
            try {
                audioPlayer.close();
            } catch (IOException e) {
                throw new AudioException(e.getMessage());
            }
        }
    }

    /**
     * Pauses the output.
     */
    @Override
    public void handlePause() {
        audioPlayer.pause();
    }

    /**
     * Resumes the output.
     */
    @Override
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
            final InputStream in;
            try {
                in = player.getAudioBytes();
            } catch (IOException e) {
                LOGGER.warning(e.getLocalizedMessage());
                return;
            } finally {
                player.reset();
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
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleSpeak(int id, Speakable item) {
        handleSpeak(id, new FreeTTSSpeakableImpl(transformer.transform(item.getMarkupText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleSpeak(int id, String text) {
        handleSpeak(id, new FreeTTSSpeakableImpl(text));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleCancelAll() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleCancel(int id) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleCancel() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AudioFormat getAudioFormat() {
        return new AudioFormat(8000f, 16, 1, true, true);
    }
}
