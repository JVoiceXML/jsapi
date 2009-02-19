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
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.jse.AudioFormatConverter;
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
        final String locator = getMediaLocator();
        if ((locator != null) && !isSupportsAudioManagement()) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        targetAudioFormat = getAudioFormat();

        // Open URL described in locator
        final InputStream is;
        if (locator == null) {
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
        final AudioFormatConverter converter;
        try {
            converter = openAudioFormatConverter(targetAudioFormat,
                    engineAudioFormat);
        } catch (IOException e) {
            throw new AudioException(e.getMessage());
        }
        inputStream = converter.getConvertedStream(is, targetAudioFormat,
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
        closeAudioFormatConverter();

        postAudioEvent(AudioEvent.AUDIO_STOPPED, AudioEvent.AUDIO_LEVEL_MIN);
    }

    public void setMediaLocator(final String locator, final InputStream stream)
            throws AudioException {
        super.setMediaLocator(locator);
        inputStream = stream;
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
