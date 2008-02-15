/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: lyncher $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This class is based on work by SUN Microsystems and
 * Carnegie Mellon University
 *
 * Copyright 1998-2003 Sun Microsystems, Inc.
 *
 * Portions Copyright 2001-2004 Sun Microsystems, Inc.
 * Portions Copyright 1999-2001 Language Technologies Institute,
 * Carnegie Mellon University.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * Permission is hereby granted, free of charge, to use and distribute
 * this software and its documentation without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of this work, and to
 * permit persons to whom this work is furnished to do so, subject to
 * the following conditions:
 *
 * 1. The code must retain the above copyright notice, this list of
 *    conditions and the following disclaimer.
 * 2. Any modifications must be clearly marked as such.
 * 3. Original authors' names are not deleted.
 * 4. The authors' names are not used to endorse or promote products
 *    derived from this software without specific prior written
 *   permission.
 *
 * SUN MICROSYSTEMS, INC., CARNEGIE MELLON UNIVERSITY AND THE
 * CONTRIBUTORS TO THIS WORK DISCLAIM ALL WARRANTIES WITH REGARD TO THIS
 * SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS, IN NO EVENT SHALL SUN MICROSYSTEMS, INC., CARNEGIE MELLON
 * UNIVERSITY NOR THE CONTRIBUTORS BE LIABLE FOR ANY SPECIAL, INDIRECT OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF
 * USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */
package org.jvoicexml.jsapi2;


import javax.speech.AudioManager;
import javax.speech.AudioListener;
import javax.speech.AudioException;
import javax.speech.EngineStateException;
import javax.speech.AudioEvent;
import javax.speech.recognition.Recognizer;
import javax.speech.synthesis.Synthesizer;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Supports the JSAPI 2.0 <code>AudioManager</code>
 * interface.  Actual JSAPI implementations might want to extend
 * or modify this implementation.
 */
public class BaseAudioManager implements AudioManager {
    /**
     * List of <code>AudioListeners</code> registered for
     * <code>AudioEvents</code> on this object.
     */
    protected List<AudioListener> audioListeners;

    protected int audioMask;

    protected String mediaLocator = "";

    protected BaseEngine engine;

    protected OutputStream outputStream;

    protected InputStream inputStream;

    protected AudioInputStream ais;

    /**
     *  Audio format of the audio nativelly produced
     * by the engine
     */
    protected AudioFormat engineAudioFormat;

    /**
     * Class constructor.
     */
    public BaseAudioManager(BaseEngine engine) {
        this.engine = engine;
        audioListeners = new ArrayList<AudioListener>();
        audioMask = AudioEvent.DEFAULT_MASK;
        inputStream = null;
        outputStream = null;
        mediaLocator = "";
        engineAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                            16000,
                                            16,
                                            1,
                                            2,
                                            16000,
                                            false);
    }

    /**
     * Requests notification of <code>AudioEvents</code> from the
     * <code>AudioManager</code>.
     *
     * @param listener the listener to add
     */
    public void addAudioListener(AudioListener listener) {
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

    public void audioStart() throws SecurityException,
            AudioException, EngineStateException {

        if (System.getProperty("javax.speech.supports.audio.management") == null) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        //Open URL described in locator
        URL url = null;
        try {
            url = new URL(mediaLocator);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ex);
        }

        //Open a connection to URL
        URLConnection urlConnection = null;
        try {
            urlConnection = url.openConnection();
            urlConnection.connect();
        } catch (IOException ex) {
            throw new AudioException("Cannot open connection to locator URL: " +
                                     ex.getMessage());
        }

        //Gets IO from that connection
        if (engine instanceof Synthesizer) {
            OutputStream os = null;
            try {
                os = urlConnection.getOutputStream();
            } catch (IOException ex) {
                throw new AudioException("Cannot get OutputStream from URL: " +
                                         ex.getMessage());
            }
            outputStream = getConvertedStream(os, engineAudioFormat,
                                              getAudioFormat(url));
        } else {
            InputStream is = null;
            try {
                is = urlConnection.getInputStream();
            } catch (IOException ex) {
                throw new AudioException("Cannot get InputStream from URL: " +
                                         ex.getMessage());
            }

            //Configure audio conversions
            inputStream = getConvertedStream(is, getAudioFormat(url), engineAudioFormat);
        }

        postAudioEvent(AudioEvent.AUDIO_STARTED, AudioEvent.AUDIO_LEVEL_MIN);

    }

    public void audioStop() throws SecurityException,
            AudioException, EngineStateException {

        if (System.getProperty("javax.speech.supports.audio.management") == null) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        //Release IO
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        postAudioEvent(AudioEvent.AUDIO_STOPPED, AudioEvent.AUDIO_LEVEL_MIN);

    }

    public void setMediaLocator(String locator) throws AudioException,
            AudioException, EngineStateException, IllegalArgumentException,
            SecurityException {

        //Insure that engine is DEALLOCATED
        if (engine.testEngineState(engine.DEALLOCATED) == false) {
            throw new EngineStateException(
                    "Engine is not DEALLOCATED. Cannot setMediaLocator");
        }

        if (System.getProperty("javax.speech.supports.audio.management") == null) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        //Insure that media locator is supported
        if (isSupportedMediaLocator(locator) == false) {
            throw new AudioException("Unsupported locator: " + locator);
        }

        mediaLocator = locator;
    }

    public void setMediaLocator(String locator, InputStream stream) throws
            AudioException, EngineStateException, IllegalArgumentException,
            SecurityException {

        if (System.getProperty("javax.speech.supports.audio.management") == null) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        if (engine instanceof Synthesizer) {
            throw new IllegalArgumentException(
                    "Engine doesn't support OutputStreams");
        }

        //Insure that engine is DEALLOCATED
        if (engine.testEngineState(engine.DEALLOCATED) == false) {
            throw new EngineStateException(
                    "Engine is not DEALLOCATED. Cannot setMediaLocator");
        }

        mediaLocator = locator;
        this.inputStream = inputStream;
    }

    public void setMediaLocator(String locator, OutputStream stream) throws
            AudioException, EngineStateException, IllegalArgumentException,
            SecurityException {

        //Check that audio IO can be made
        if (System.getProperty("javax.speech.supports.audio.management") == null) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        if (engine instanceof Recognizer) {
            throw new IllegalArgumentException(
                    "Engine doesn't support OutputStreams");
        }

        //Insure that engine is DEALLOCATED
        if (engine.testEngineState(engine.DEALLOCATED) == false) {
            throw new EngineStateException(
                    "Engine is not DEALLOCATED. Cannot setMediaLocator");
        }

        //     Connection c = javax.microedition.io.Connector.open("");

        mediaLocator = locator;
        this.outputStream = outputStream;
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

        return (supportedMediaLocators == null ? false : true);
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
            final AudioEvent event = new AudioEvent(engine, eventId, audioLevel);

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
            for (AudioListener listener: audioListeners) {
                listener.audioUpdate(event);
            }
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Given URI parameters, constructs an AudioFormat
     *
     * @return AudioFormat
     */
    private AudioFormat getAudioFormat(URL mediaLocator) {
        //Get matching URI to extract query parameters
        URI uri = null;
        try {
            uri = mediaLocator.toURI();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
            //Continue and give back a default AudioFormat
        }

        //Initialize parameters
        HashMap<String, String> parameters = new HashMap<String, String>();
        if (uri.getQuery() != null) {
            String[] parametersString = uri.getQuery().split("\\&");
            for (String part : parametersString) {
                String[] queryElement = part.split("\\=");
                parameters.put(queryElement[0], queryElement[1]);
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
                encoding = (signed == true ? AudioFormat.Encoding.PCM_SIGNED :
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

    /**
     * @todo Insure that this is robust...
     *
     * @param is InputStream
     * @param sourceFormat AudioFormat
     * @param targetFormat AudioFormat
     * @return InputStream
     */
    private InputStream getConvertedStream(InputStream is,
                                           AudioFormat sourceFormat,
                                           AudioFormat targetFormat) {
        /** @todo Compare more preciselly AudioFormat (not using AudioFormat.matches()) */
        if (sourceFormat.matches(targetFormat)) {
            return is;
        }

        //Describe source stream as an AudioFormat
        AudioInputStream sourceStream = new AudioInputStream(is, sourceFormat,
                AudioSystem.NOT_SPECIFIED);

        //Convert endianess
        sourceStream = convertEndianess(sourceStream, targetFormat);

        //Convert number of channels
        sourceStream = convertChannels(sourceStream, targetFormat);

        //Convert sample rate
        sourceStream = convertSampleRate(sourceStream, targetFormat);

        //Convert encoding
        sourceStream = convertEncoding(sourceStream, targetFormat);

        //Convert sample size
        sourceStream = convertSampleSize(sourceStream, targetFormat);

        return sourceStream;
    }

    /**
     * @todo Insure that this is robust...
     *
     * @param os OutputStream
     * @param engineAudioFormat AudioFormat
     * @param audioFormat AudioFormat
     * @return OutputStream
     */
    private OutputStream getConvertedStream(final OutputStream os,
                                            AudioFormat engineAudioFormat,
                                            AudioFormat targetFormat) {
        /** @todo Compare more preciselly AudioFormat (not using AudioFormat.matches()) */
        if (engineAudioFormat.matches(targetFormat)) {
            return os;
        }

        try {
            //Basic Conversion support
            PipedInputStream pis = new PipedInputStream(16000000);
            PipedOutputStream pos = new PipedOutputStream(pis);

            //Describe source audio
            final InputStream is = getConvertedStream(pis, engineAudioFormat,
                    targetFormat);

            new Thread(new Runnable() {
                public void run() {
                    FileOutputStream synt_conv = null;
                    try {
                        synt_conv = new FileOutputStream(File.
                                createTempFile("synth", ".raw", new File(".")));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    int br;
                    byte[] buffer = new byte[512];
                    try {
                        while ((br = is.read(buffer)) != -1) {
                            os.write(buffer, 0, br);
                            synt_conv.write(buffer, 0, br);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    try {
                        os.close();
                        is.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }, "AudioCopyer").start();

            //Return write point for synthesizer
            return pos;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        /** @todo Should never reach this point */
        return os;
    }


    private AudioInputStream convertEndianess(AudioInputStream ais,
                                              AudioFormat targetFormat) {
        if (ais.getFormat().isBigEndian() == targetFormat.isBigEndian()) {
            return ais;
        }

        AudioFormat newFormat = new AudioFormat(ais.getFormat().getEncoding(),
                                                ais.getFormat().getSampleRate(),
                                                ais.getFormat().
                                                getSampleSizeInBits(),
                                                ais.getFormat().getChannels(),
                                                ais.getFormat().getFrameSize(),
                                                ais.getFormat().getFrameRate(),
                                                targetFormat.isBigEndian());

        return AudioSystem.getAudioInputStream(newFormat, ais);
    }

    private AudioInputStream convertChannels(AudioInputStream ais,
                                             AudioFormat targetFormat) {
        if (ais.getFormat().getChannels() == targetFormat.getChannels()) {
            return ais;
        }
        AudioFormat newFormat = new AudioFormat(ais.getFormat().getEncoding(),
                                                ais.getFormat().getSampleRate(),
                                                ais.getFormat().
                                                getSampleSizeInBits(),
                                                targetFormat.getChannels(),
                                                ais.getFormat().getFrameSize(),
                                                ais.getFormat().getFrameRate(),
                                                ais.getFormat().isBigEndian());

        return AudioSystem.getAudioInputStream(newFormat, ais);

    }

    private AudioInputStream convertSampleRate(AudioInputStream ais,
                                               AudioFormat targetFormat) {
        if (ais.getFormat().getSampleRate() == targetFormat.getSampleRate()) {
            return ais;
        }
        AudioFormat newFormat = new AudioFormat(ais.getFormat().getEncoding(),
                                                targetFormat.getSampleRate(),
                                                ais.getFormat().
                                                getSampleSizeInBits(),
                                                ais.getFormat().getChannels(),
                                                ais.getFormat().getFrameSize(),
                                                targetFormat.getFrameRate(),
                                                ais.getFormat().isBigEndian());

        return AudioSystem.getAudioInputStream(newFormat, ais);
    }

    private AudioInputStream convertEncoding(AudioInputStream ais,
                                             AudioFormat targetFormat) {
        if (ais.getFormat().getEncoding() == targetFormat.getEncoding()) {
            return ais;
        }
        AudioFormat newFormat = new AudioFormat(targetFormat.getEncoding(),
                                                ais.getFormat().getSampleRate(),
                                                targetFormat.
                                                getSampleSizeInBits(),
                                                ais.getFormat().getChannels(),
                                                targetFormat.getFrameSize(),
                                                ais.getFormat().getFrameRate(),
                                                ais.getFormat().isBigEndian());

        return AudioSystem.getAudioInputStream(newFormat, ais);
    }


    private AudioInputStream convertSampleSize(AudioInputStream ais,
                                               AudioFormat targetFormat) {
        if (ais.getFormat().getSampleSizeInBits() ==
            targetFormat.getSampleSizeInBits()) {
            return ais;
        }
        AudioFormat newFormat = new AudioFormat(ais.getFormat().getEncoding(),
                                                ais.getFormat().getSampleRate(),
                                                targetFormat.
                                                getSampleSizeInBits(),
                                                ais.getFormat().getChannels(),
                                                ais.getFormat().getFrameSize(),
                                                ais.getFormat().getFrameRate(),
                                                ais.getFormat().isBigEndian());

        return AudioSystem.getAudioInputStream(newFormat, ais);
    }


}

