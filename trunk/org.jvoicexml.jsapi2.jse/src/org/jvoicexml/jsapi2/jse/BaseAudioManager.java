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

package org.jvoicexml.jsapi2.jse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.speech.AudioEvent;
import javax.speech.AudioException;
import javax.speech.AudioListener;
import javax.speech.AudioManager;
import javax.speech.Engine;
import javax.speech.EngineStateException;

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
    protected List<AudioListener> audioListeners;

    protected int audioMask;

    protected String mediaLocator;

    protected BaseEngine engine;

    protected AudioInputStream ais;

    /**
     * Audio format of the audio natively produced
     * by the engine.
     */
    protected AudioFormat engineAudioFormat;

    /** Audio format to be delivered. */
    protected AudioFormat targetAudioFormat;

    /** Converter from the source (synthesizer) to the target format. */
    protected AudioFormatConverter formatConverter;

    /**
     * Class constructor.
     */
    public BaseAudioManager() {
        audioListeners = new ArrayList<AudioListener>();
        audioMask = AudioEvent.DEFAULT_MASK;
        engineAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                            8000,
                                            16,
                                            1,
                                            2,
                                            8000,
                                            false);
        targetAudioFormat = engineAudioFormat;
    }

    /**
     * Sets the engine.
     * @param engine the engine.
     */
    public void setEngine(final BaseEngine engine) {
        this.engine = engine;
    }

    /**
     * Retrieves the audio format converter.
     * @return the audio format converter.
     */
    public AudioFormatConverter getAudioFormatConverter() {
        return formatConverter;
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
                audioListeners.add(listener);
            }
        }
    }

    /**
     * Removes an <code>AudioListener</code> from the list of
     * <code>AudioListeners</code>.
     *
     * @param listener the listener to remove
     */
    public void removeAudioListener(AudioListener listener) {
        synchronized (audioListeners) {
            audioListeners.remove(listener);
        }
    }

    public int getAudioMask() {
        return audioMask;
    }

    public void setAudioMask(int mask) {
        audioMask = mask;
    }

    /**
     * {@inheritDoc}
     */
    public abstract void audioStart() throws SecurityException,
            AudioException, EngineStateException;

    protected URLConnection openURLConnection() throws IOException {
        if (mediaLocator == null) {
            return null;
        }

        final URL url;
        try {
            url = new URL(mediaLocator);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ex);
        }

        //Open a connection to URL
        URLConnection connection = url.openConnection();
        connection.connect();
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    public abstract void audioStop() throws SecurityException,
            AudioException, EngineStateException;

    public void setMediaLocator(String locator) throws AudioException,
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

    public String getMediaLocator() {
        return mediaLocator;
    }

    /**
     * @todo THis is just a dummy implementation
     *
     * @param mediaLocator String
     * @return String[]
     * @throws IllegalArgumentException
     */
    public String[] getSupportedMediaLocators(String mediaLocator) throws
            IllegalArgumentException {
        return new String[] {mediaLocator};
    }

    public boolean isSupportedMediaLocator(String mediaLocator) throws
            IllegalArgumentException {

        String[] supportedMediaLocators = getSupportedMediaLocators(
                mediaLocator);

        return supportedMediaLocators == null ? false : true;
    }

    /**
     * Checks if audio management is supported.
     * @return <code>true</code> if audio management is supported.
     */
    protected boolean isSupportsAudioManagement() {
        String management =
            System.getProperty("javax.speech.supports.audio.management");
        return Boolean.valueOf(management).equals(Boolean.TRUE);
    }

    /**
     * @todo Initial implementation
     *
     * @param audioManager AudioManager
     * @return boolean
     */
    public boolean isSameChannel(AudioManager audioManager) {
        return (audioManager.getMediaLocator() == mediaLocator);
    }

    protected void postAudioEvent(int eventId, int audioLevel) {
        if ((getAudioMask() & eventId) == eventId) {
            final AudioEvent event = new AudioEvent(engine, eventId);

            Runnable r = new Runnable() {
                public void run() {
                    fireAudioEvent(event);
                }
            };

            try {
                engine.getSpeechEventExecutor().execute(r);
            } catch (RuntimeException ex) {
                //Ignore exception
                ex.printStackTrace();
            }
        }
    }

    public void fireAudioEvent(AudioEvent event) {
        synchronized (audioListeners) {
            for (AudioListener listener : audioListeners) {
                listener.audioUpdate(event);
            }
        }
    }

    /**
     * Retrieves the output stream associated with the given media locator.
     * @return output stream.
     */
    public abstract OutputStream getOutputStream();

    /**
     * Retrieves the input stream associated with the given media locator.
     * @return input stream.
     */
    public abstract InputStream getInputStream();

    /**
     * Given URI parameters, constructs an AudioFormat
     *
     * @return AudioFormat
     */
    protected AudioFormat getAudioFormat() {
        //Initialize parameters
        Map<String, String> parameters =
            new java.util.HashMap<String, String>();
        if (mediaLocator != null) {
            //Get matching URI to extract query parameters
            URI uri = null;
            try {
                uri = new URI(mediaLocator);
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
                //Continue and give back a default AudioFormat
            }

            if (uri.getQuery() != null) {
                String[] parametersString = uri.getQuery().split("\\&");
                for (String part : parametersString) {
                    String[] queryElement = part.split("\\=");
                    parameters.put(queryElement[0], queryElement[1]);
                }
            }
        }

        //Default values for AudioFormat parameters
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float sampleRate = 16000;
        int bits = 16;
        int channels = 1;
        boolean endian = false;
        boolean signed = true;

        //Change default values as specified
        String signedStr = parameters.get("signed");
        if (signedStr != null) {
            signed = Boolean.valueOf(signedStr);
        }

        String encodingStr = parameters.get("encoding");
        if (encodingStr != null) {
            if (encodingStr.equals("pcm")) {
                encoding = (signed ? AudioFormat.Encoding.PCM_SIGNED :
                            AudioFormat.Encoding.PCM_UNSIGNED);
            } else if (encodingStr.equals("alaw")) {
                encoding = AudioFormat.Encoding.ALAW;
                endian = false;
            } else if (encodingStr.equals("ulaw")) {
                encoding = AudioFormat.Encoding.ULAW;
                endian = false;
            } else if (encodingStr.equals("gsm")) {
                /** @todo GSM not supported by AudioFormat */
                System.err.println("GSM not supported by AudioFormat... review");
            }
        }

        String rateStr = parameters.get("rate");
        if (rateStr != null) {
            sampleRate = Float.valueOf(rateStr);
        }

        String bitsStr = parameters.get("bits");
        if (bitsStr != null) {
            bits = Integer.valueOf(bitsStr);
            if (bits == 8) {
                endian = false;
            }
        }

        String channelsStr = parameters.get("channels");
        if (channelsStr != null) {
            channels = Integer.valueOf(channelsStr);
        }

        String endianStr = parameters.get("endian");
        if (endianStr != null) {
            if (endianStr.equals("little")) {
                endian = false;
            } else if (endianStr.equals("big")) {
                endian = true;
            }
        }

        //Constructs the AudioFormat
        return new AudioFormat(encoding, sampleRate, bits, channels, bits / 8,
                               sampleRate, endian);
    }

    public void setEngineAudioFormat(AudioFormat audioFormat) {
        engineAudioFormat = audioFormat;
    }

    public AudioFormat getEngineAudioFormat() {
        return engineAudioFormat;
    }

    public AudioFormat getTargetAudioFormat() {
        return targetAudioFormat;
    }
}

