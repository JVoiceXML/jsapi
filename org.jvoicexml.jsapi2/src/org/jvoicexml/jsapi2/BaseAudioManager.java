/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: lyncher $
 *
 * JSAPI - An base implementation for JSR 113.
 *
 * Copyright (C) 2007-2009 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2;

import java.util.Enumeration;
import java.util.Vector;

import javax.speech.AudioEvent;
import javax.speech.AudioException;
import javax.speech.AudioListener;
import javax.speech.AudioManager;
import javax.speech.Engine;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;

/**
 * Supports the JSAPI 2.0 <code>AudioManager</code>
 * interface.  Actual JSAPI implementations might want to extend
 * or modify this implementation.
 */
public abstract class BaseAudioManager implements AudioManager {
    /**
     * List of <code>AudioListeners</code> registered for
     * <code>AudioEvents</code> on this object.
     */
    private Vector audioListeners;

    protected int audioMask;

    /** The media locator. */
    private String mediaLocator;

    /** The associated engine. */
    private Engine engine;

    /**
     * Class constructor.
     */
    public BaseAudioManager() {
        audioListeners = new Vector();
        audioMask = AudioEvent.DEFAULT_MASK;
    }

    /**
     * Sets the engine.
     * @param value the engine.
     */
    public void setEngine(final Engine value) {
        engine = value;
    }

    /**
     * Requests notification of <code>AudioEvents</code> from the
     * <code>AudioManager</code>.
     *
     * @param listener the listener to add
     */
    public void addAudioListener(final AudioListener listener) {
        synchronized (audioListeners) {
            if (!audioListeners.contains(listener)) {
                audioListeners.addElement(listener);
            }
        }
    }

    /**
     * Removes an <code>AudioListener</code> from the list of
     * <code>AudioListeners</code>.
     *
     * @param listener the listener to remove
     */
    public void removeAudioListener(final AudioListener listener) {
        synchronized (audioListeners) {
            audioListeners.removeElement(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getAudioMask() {
        return audioMask;
    }

    /**
     * {@inheritDoc}
     */
    public void setAudioMask(final int mask) {
        audioMask = mask;
    }

    /**
     * {@inheritDoc}
     */
    public void audioStart() throws SecurityException,
            AudioException, EngineStateException {
        final String locator = getMediaLocator();
        if ((locator != null) && !isSupportsAudioManagement()) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        handleAudioStart();

        postAudioEvent(AudioEvent.AUDIO_STARTED, AudioEvent.AUDIO_LEVEL_MIN);
    }

    /**
     * Handles further processing if the audio output has to be started by
     * a call to {@link #audioStart()}.
     * @throws AudioException
     *         error stopping
     */
    protected abstract void handleAudioStart() throws AudioException;

    /**
     * {@inheritDoc}
     */
    public void audioStop() throws SecurityException,
            AudioException, EngineStateException {
        if (!isSupportsAudioManagement()) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        handleAudioStop();

        postAudioEvent(AudioEvent.AUDIO_STOPPED, AudioEvent.AUDIO_LEVEL_MIN);

    }

    /**
     * Handles further processing if the audio output has to be stopped by
     * a call to {@link #audioStop()}.
     * @throws AudioException
     *         error stopping
     */
    protected abstract void handleAudioStop() throws AudioException;

    /**
     * {@inheritDoc}
     */
    public void setMediaLocator(final String locator) throws AudioException,
            AudioException, EngineStateException, IllegalArgumentException,
            SecurityException {

        //Insure that engine is DEALLOCATED
        if (!engine.testEngineState(Engine.DEALLOCATED)) {
            throw new EngineStateException(
                    "Engine is not DEALLOCATED. Cannot setMediaLocator");
        }

        if (!isSupportsAudioManagement()) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        //Insure that media locator is supported
        if (!isSupportedMediaLocator(locator)) {
            throw new AudioException("Unsupported locator: " + locator);
        }

        mediaLocator = locator;
    }

    /**
     * {@inheritDoc}
     */
    public String getMediaLocator() {
        return mediaLocator;
    }

    /**
     * {@inheritDoc}
     * @todo This is just a dummy implementation
     */
    public String[] getSupportedMediaLocators(final String locator)
        throws IllegalArgumentException {
        return new String[] {mediaLocator};
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSupportedMediaLocator(final String locator)
        throws IllegalArgumentException {

        final String[] supportedMediaLocators = getSupportedMediaLocators(
                locator);

        if (supportedMediaLocators == null) {
            return false;
        }
        return true;
    }

    /**
     * Checks if audio management is supported.
     * @return <code>true</code> if audio management is supported.
     */
    protected boolean isSupportsAudioManagement() {
        final String management =
            System.getProperty("javax.speech.supports.audio.management");
        if (management == null) {
            return false;
        }
        return management.equalsIgnoreCase("true");
    }

    /**
     * {@inheritDoc}
     *
     * This implementation checks only for equal media locators.
     */
    public boolean isSameChannel(final AudioManager audioManager) {
        if (audioManager == null) {
            return false;
        }
        final String otherLocator = audioManager.getMediaLocator();
        if (otherLocator == null) {
            return mediaLocator == null;
        }
        return mediaLocator.equalsIgnoreCase(otherLocator);
    }

    /**
     * Notifies all listeners about the audio event using the configures
     * {@link javax.speech.SpeechEventExecutor}.
     * @param eventId id of the audio event.
     * @param audioLevel
     */
    protected void postAudioEvent(final int eventId, final int audioLevel) {
        if ((getAudioMask() & eventId) == eventId) {
            final AudioEvent event = new AudioEvent(engine, eventId);

            Runnable runnable = new Runnable() {
                public void run() {
                    fireAudioEvent(event);
                }
            };

            try {
                final SpeechEventExecutor executor =
                    engine.getSpeechEventExecutor();
                executor.execute(runnable);
            } catch (RuntimeException ex) {
                //Ignore exception
                ex.printStackTrace();
            }
        }
    }

    /**
     * Notifies all {@link AudioListener}s about the given event.
     * This method runs within the configured {@link SpeechEventExecutor}.
     * @param event the event.
     */
    public void fireAudioEvent(final AudioEvent event) {
        synchronized (audioListeners) {
            final Enumeration enumeration = audioListeners.elements();
            while (enumeration.hasMoreElements()) {
                final AudioListener listener =
                    (AudioListener) enumeration.nextElement();
                listener.audioUpdate(event);
            }
        }
    }
}

