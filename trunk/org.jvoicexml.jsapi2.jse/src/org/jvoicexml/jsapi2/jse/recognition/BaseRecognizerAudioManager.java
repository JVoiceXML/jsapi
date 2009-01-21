/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */
package org.jvoicexml.jsapi2.jse.recognition;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import javax.speech.AudioEvent;
import javax.speech.AudioException;
import javax.speech.Engine;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.jse.BaseAudioManager;

/**
 * Supports the JSAPI 2.0 <code>AudioManager</code> interface. Actual JSAPI
 * implementations might want to extend or modify this implementation.
 */
public class BaseRecognizerAudioManager extends BaseAudioManager {
    /** The input stream for the recognizer. */
    protected InputStream inputStream;

    /**
     * Class constructor.
     */
    public BaseRecognizerAudioManager() {
    }

    /**
     * {@inheritDoc}
     */
    public void audioStart() throws SecurityException, AudioException,
            EngineStateException {

        if ((mediaLocator != null) && !isSupportsAudioManagement()) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        targetAudioFormat = getAudioFormat();

        // Open URL described in locator
        final InputStream is;
        if (mediaLocator == null) {
            is = new LineInputStream(this);
        } else {
            final URLConnection urlConnection;
            try {
                urlConnection = openURLConnection();
            } catch (IOException e) {
                throw new AudioException(e.getMessage());
            }

            try {
                is = urlConnection.getInputStream();
            } catch (IOException ex) {
                throw new AudioException("Cannot get InputStream from URL: "
                        + ex.getMessage());
            }
        }

        // Configure audio conversions
        inputStream = getConvertedStream(is, targetAudioFormat,
                engineAudioFormat);
        postAudioEvent(AudioEvent.AUDIO_STARTED, AudioEvent.AUDIO_LEVEL_MIN);
    }

    /**
     * {@inheritDoc}
     */
    public void audioStop() throws SecurityException, AudioException,
            EngineStateException {

        if (!isSupportsAudioManagement()) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        // Release IO
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (formatConverter != null) {
            formatConverter.close();
            formatConverter = null;
        }

        postAudioEvent(AudioEvent.AUDIO_STOPPED, AudioEvent.AUDIO_LEVEL_MIN);

    }

    public void setMediaLocator(String locator, InputStream stream)
            throws AudioException, EngineStateException,
            IllegalArgumentException, SecurityException {

        if (!isSupportsAudioManagement()) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        // Insure that engine is DEALLOCATED
        if (!engine.testEngineState(Engine.DEALLOCATED)) {
            throw new EngineStateException(
                    "Engine is not DEALLOCATED. Cannot setMediaLocator");
        }

        mediaLocator = locator;
        this.inputStream = stream;
    }

    /**
     * {@inheritDoc}
     */
    public void setMediaLocator(String locator, OutputStream stream)
            throws AudioException, EngineStateException,
            IllegalArgumentException, SecurityException {
        throw new IllegalArgumentException("output streams are not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream getOutputStream() {
        throw new IllegalArgumentException("output streams are not supported");
    }
}
