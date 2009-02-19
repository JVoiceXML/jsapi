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
package org.jvoicexml.jsapi2.jse.synthesis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import javax.speech.AudioEvent;
import javax.speech.AudioException;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.jse.AudioFormatConverter;
import org.jvoicexml.jsapi2.jse.BaseAudioManager;

/**
 * Supports the JSAPI 2.0 <code>AudioManager</code> interface. Actual JSAPI
 * implementations might want to extend or modify this implementation.
 */
public class BaseSynthesizerAudioManager extends BaseAudioManager {
    /** The output stream from the synthesizer. */
    private OutputStream outputStream;

    /**
     * Class constructor.
     */
    public BaseSynthesizerAudioManager() {
    }

    /**
     * {@inheritDoc}
     */
    public void audioStart() throws SecurityException, AudioException,
            EngineStateException {
        final String locator = getMediaLocator();
        if ((locator != null) && !isSupportsAudioManagement()) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        if (locator == null) {
            outputStream = new ClipOutputStream(this);
        } else {
            // Open URL described in locator
            final URLConnection urlConnection;
            try {
                urlConnection = openURLConnection();
            } catch (IOException e) {
                throw new AudioException(e.getMessage());
            }

            // Gets IO from that connection
            try {
                outputStream = urlConnection.getOutputStream();
            } catch (IOException ex) {
                throw new AudioException("Cannot get OutputStream from URL: "
                        + ex.getMessage());
            }

            targetAudioFormat = getAudioFormat();
        }
        // Configure audio conversions
        try {
            openAudioFormatConverter(engineAudioFormat, targetAudioFormat);
        } catch (IOException e) {
            throw new AudioException(e.getMessage());
        }
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

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        closeAudioFormatConverter();

        postAudioEvent(AudioEvent.AUDIO_STOPPED, AudioEvent.AUDIO_LEVEL_MIN);
    }

    /**
     * Retrieves the output stream from the synthesizer.
     * @return the output stream.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }


    public void setMediaLocator(final String locator, final OutputStream stream)
            throws AudioException {
        super.setMediaLocator(locator);
        this.outputStream = stream;
    }

    /**
     * {@inheritDoc}
     */
    public void setMediaLocator(String locator, InputStream stream)
            throws AudioException, EngineStateException,
            IllegalArgumentException, SecurityException {
        throw new IllegalArgumentException("input streams are not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() {
        throw new IllegalArgumentException("input streams are not supported");
    }
}
