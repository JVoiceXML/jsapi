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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.jvoicexml.jsapi2.jse.recognition.LineInputStream;

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

    private String mediaLocator;

    protected Engine engine;

    protected AudioInputStream ais;

    /**
     * Audio format of the audio natively produced by the engine.
     */
    protected AudioFormat engineAudioFormat;

    /** Audio format of that is being received or that is being delivered. */
    protected AudioFormat targetAudioFormat;

    /** Converter from the source (synthesizer) to the target format. */
    private AudioFormatConverter formatConverter;

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
     * @param value the engine.
     */
    public void setEngine(final Engine value) {
        engine = value;
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
    public void removeAudioListener(final AudioListener listener) {
        synchronized (audioListeners) {
            audioListeners.remove(listener);
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
     * Opens the connection to the configured media locator.
     * @return opened connection
     * @throws IOException
     *         error opening the connection.
     */
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
        final URLConnection connection = url.openConnection();
        connection.connect();
        return connection;
    }

    /**
     * Opens the audio format converter to convert from the given source
     * format into the given target format.
     * <p>
     * This method must be called in the {@link #audioStart()} method.
     * </p>
     * @param source the source audio format
     * @param target the target audio format.
     * @return the audio format converter.
     * @throws IOException
     *         error opening the format converter
     */
    protected AudioFormatConverter openAudioFormatConverter(
            final AudioFormat source, final AudioFormat target)
        throws IOException {
        formatConverter = new AudioFormatConverter(this, source, target);
        return formatConverter;
    }

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

        if (formatConverter != null) {
            formatConverter.close();
            formatConverter = null;
        }

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

        final String[] supportedMediaLocators = getSupportedMediaLocators(
                mediaLocator);

        return supportedMediaLocators == null ? false : true;
    }

    /**
     * Checks if audio management is supported.
     * @return <code>true</code> if audio management is supported.
     */
    protected boolean isSupportsAudioManagement() {
        final String management =
            System.getProperty("javax.speech.supports.audio.management");
        return Boolean.valueOf(management).equals(Boolean.TRUE);
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
     * @param eventId
     * @param audioLevel
     */
    protected void postAudioEvent(final int eventId, final int audioLevel) {
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
     * Given URI parameters, constructs an {@link AudioFormat} from the
     * parameters specified in the URI.
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

    /**
     * Sets the audio format that is being used by this engine.
     * @param audioFormat new audio format.
     */
    public void setEngineAudioFormat(final AudioFormat audioFormat) {
        engineAudioFormat = audioFormat;
    }

    /**
     * Retrieves the audio format that is used by this engine.
     * @return audio format used by this engine.
     */
    public AudioFormat getEngineAudioFormat() {
        return engineAudioFormat;
    }

    /**
     * Retrieves the target audio format.
     * @return target audio format.
     */
    public AudioFormat getTargetAudioFormat() {
        return targetAudioFormat;
    }
}

