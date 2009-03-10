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
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.speech.AudioException;
import javax.speech.AudioManager;

import org.jvoicexml.jsapi2.BaseAudioManager;

/**
 * Supports the JSAPI 2.0 <code>AudioManager</code>
 * interface.  Actual JSAPI implementations might want to extend
 * or modify this implementation.
 */
public abstract class JseBaseAudioManager extends BaseAudioManager implements AudioManager {
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
    public JseBaseAudioManager() {
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
     * Retrieves the audio format converter.
     * @return the audio format converter.
     */
    public AudioFormatConverter getAudioFormatConverter() {
        return formatConverter;
    }

    /**
     * Opens the connection to the configured media locator.
     * @return opened connection
     * @throws IOException
     *         error opening the connection.
     */
    protected URLConnection openURLConnection() throws IOException {
        final String locator = getMediaLocator();
        if (locator == null) {
            return null;
        }

        final URL url;
        try {
            url = new URL(locator);
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
        final String locator = getMediaLocator();
        if (locator != null) {
            //Get matching URI to extract query parameters
            URI uri = null;
            try {
                uri = new URI(locator);
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

    /**
     * {@inheritDoc}
     *
     * Closes the format converter. May be overridden to handle further cleanup.
     */
    @Override
    protected void handleAudioStop() throws AudioException {
        if (formatConverter != null) {
            formatConverter.close();
            formatConverter = null;
        }
    }
}

