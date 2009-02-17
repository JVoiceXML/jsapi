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
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URLConnection;

import javax.sound.sampled.AudioFormat;
import javax.speech.AudioEvent;
import javax.speech.AudioException;
import javax.speech.Engine;
import javax.speech.EngineStateException;
import javax.speech.recognition.Recognizer;

import org.jvoicexml.jsapi2.jse.AudioFormatConverter;
import org.jvoicexml.jsapi2.jse.BaseAudioManager;

/**
 * Supports the JSAPI 2.0 <code>AudioManager</code> interface. Actual JSAPI
 * implementations might want to extend or modify this implementation.
 */
public class BaseSynthesizerAudioManager extends BaseAudioManager {
    protected OutputStream outputStream;

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

        if ((mediaLocator != null) && !isSupportsAudioManagement()) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        if (mediaLocator == null) {
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
        try {
            formatConverter = new AudioFormatConverter(this, engineAudioFormat,
                    targetAudioFormat);
        } catch (Exception ex) {
             throw new AudioException("Cannot solve required audio formats: " + ex.getMessage());
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

        if (formatConverter != null) {
            formatConverter.close();
            formatConverter = null;
        }

        postAudioEvent(AudioEvent.AUDIO_STOPPED, AudioEvent.AUDIO_LEVEL_MIN);
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * @todo Insure that this is robust...
     *
     * @param os
     *            OutputStream
     * @param engineAudioFormat
     *            AudioFormat
     * @param audioFormat
     *            AudioFormat
     * @return OutputStream
     */
    private OutputStream getConvertedStream(final OutputStream os,
            AudioFormat engineAudioFormat, AudioFormat targetFormat) {
        /**
         * @todo Compare more preciselly AudioFormat (not using
         *       AudioFormat.matches())
         */
        if (engineAudioFormat.matches(targetFormat)) {
            return os;
        }

        try {
            // Basic Conversion support
            PipedInputStream pis = new PipedInputStream();
            PipedOutputStream pos = new PipedOutputStream(pis);

            // Describe source audio
            final InputStream is = getConvertedStream(pis, engineAudioFormat,
                    targetFormat);

            return pos;
            /*
             * new Thread(new Runnable() { public void run() { FileOutputStream
             * synt_conv = null; try { synt_conv = new FileOutputStream(File.
             * createTempFile("synth", ".raw", new File("."))); } catch
             * (Exception ex) { ex.printStackTrace(); } int br; byte[] buffer =
             * new byte[512]; try { while ((br = is.read(buffer)) != -1) {
             * os.write(buffer, 0, br); synt_conv.write(buffer, 0, br); } }
             * catch (IOException ex) { ex.printStackTrace(); }
             *
             * try { os.close(); is.close(); } catch (IOException ex) {
             * ex.printStackTrace(); } } }, "AudioCopyer").start();
             */

            // Return write point for synthesizer
            // return new OutputStreamConverter(pos, is, os);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        /** @todo Should never reach this point */
        return os;
    }

    public void setMediaLocator(String locator, OutputStream stream)
            throws AudioException, EngineStateException,
            IllegalArgumentException, SecurityException {

        // Check that audio IO can be made
        if (System.getProperty("javax.speech.supports.audio.management") == null) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        if (engine instanceof Recognizer) {
            throw new IllegalArgumentException(
                    "Engine doesn't support OutputStreams");
        }

        // Insure that engine is DEALLOCATED
        if (!engine.testEngineState(Engine.DEALLOCATED)) {
            throw new EngineStateException(
                    "Engine is not DEALLOCATED. Cannot setMediaLocator");
        }

        // Connection c = javax.microedition.io.Connector.open("");

        mediaLocator = locator;
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
