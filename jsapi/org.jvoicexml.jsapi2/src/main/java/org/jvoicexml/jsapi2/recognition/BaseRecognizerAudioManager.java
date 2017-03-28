/*
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2015 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package org.jvoicexml.jsapi2.recognition;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.speech.AudioException;
import javax.speech.Engine;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.BaseAudioManager;
import org.jvoicexml.jsapi2.protocols.JavaSoundParser;

/**
 * Supports the JSAPI 2.0 {@link javax.speech.AudioManager} interface. Actual JSAPI
 * implementations might want to extend or modify this implementation
 * for {@link javax.speech.recognition.Recognizer}.
 */
public class BaseRecognizerAudioManager extends BaseAudioManager {
    /** Logger instance. */
    private static final Logger LOGGER =
        Logger.getLogger(BaseRecognizerAudioManager.class.getCanonicalName());

    /** The input stream for the recognizer. */
    private InputStream inputStream;

    /**
     * Constructs a new object.
     * @param engine the associated engine
     * @param format native engine audio format
     */
    public BaseRecognizerAudioManager(final Engine engine,
            final AudioFormat format) {
        super(engine, format);
    }

    /**
     * Opens the URL with the given locator. This also determines the target
     * audio format by
     * <ol>
     * <li>trying to determine the format from the given URL</li>
     * <li>
     *   parsing the sound format encoded in the URL if the previous method
     *   fails.</li>
     * </ol>
     * @param locator the URL to open
     * @return opened connection to the URL
     * @throws AudioException 
     *         error opening the stream
     */
    private InputStream openUrl(final String locator) throws AudioException {
        // Clear a possible previous audio format
        setTargetAudioFormat(null);
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "opening locator at: '" + locator + "'");
        }
        
        // Determine the audio format
        AudioFormat targetFormat = null;
        try {
            final URL url = new URL(locator);
            final AudioFileFormat format =
                AudioSystem.getAudioFileFormat(url);
            targetFormat = format.getFormat();
            setTargetAudioFormat(targetFormat);
        } catch (MalformedURLException e) {
            throw new AudioException(e.getMessage());
        } catch (UnsupportedAudioFileException e) {
            throw new AudioException(e.getMessage());
        } catch (IOException e) {
            throw new AudioException(e.getMessage());
        }
        
        // Open the connection to retrieve an input stream
        try {
            final URLConnection urlConnection = openURLConnection(false);
            final InputStream source = urlConnection.getInputStream();
            return new BufferedInputStream(source);
        } catch (IOException ex) {
            throw new AudioException("Cannot get InputStream from URL: "
                    + ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleAudioStart() throws AudioException {
        // Just convert samples if we already have the correct stream
        if (inputStream instanceof AudioInputStream) {
            final AudioInputStream stream = (AudioInputStream) inputStream;
            inputStream = handleAudioStart(stream);
            return;
        } else {
            final String locator = getMediaLocator();
            handleAudioStart(locator);
        }
    }

    /**
     * Starts audio for the given audio input stream.
     * @param stream the current stream
     * @return a converting stream
     */
    private AudioInputStream handleAudioStart(
            final AudioInputStream stream) {
        final AudioFormat format = stream.getFormat();
        setTargetAudioFormat(format);
        final AudioFormat engineFormat = getEngineAudioFormat();
        return AudioSystem.getAudioInputStream(engineFormat, stream);
    }


    /**
     * Starts audio processing for the given media locator.
     * @param locator the media locator to use
     * @throws AudioException
     *          error starting the audio
     */
    private void handleAudioStart(final String locator) throws AudioException {
        final AudioFormat format;
        // Open URL described in locator
        if (locator == null || locator.isEmpty()
                || locator.startsWith("capture")) {
            if (locator != null && locator.startsWith("capture")) {
                try {
                    format = parseAudioFormat(locator);
                } catch (URISyntaxException e) {
                    throw new AudioException(e.getMessage());
                }
            } else {
                // Use the microphone with the engine audio format
                format = getEngineAudioFormat();
                setTargetAudioFormat(format);
            }
            try {
                inputStream = openMicrophone(format);
            } catch (LineUnavailableException e) {
                throw new AudioException(e.getMessage());
            }
        } else {
            inputStream = openUrl(locator);
            format = getTargetAudioFormat();
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "using target audio format {0}",
                    format);
        }
        final AudioInputStream stream = new AudioInputStream(inputStream,
                    format, AudioSystem.NOT_SPECIFIED);
        final AudioFormat engineFormat = getEngineAudioFormat();
        inputStream = AudioSystem.getAudioInputStream(engineFormat,
                        stream);
    }

    /**
     * Opens the microphone with the given audio format.
     * @param format the audio format to use.
     * @return opened input stream to the microphone
     * @throws LineUnavailableException
     *          error opening the microphone
     */
    private InputStream openMicrophone(final AudioFormat format)
            throws LineUnavailableException {
        final TargetDataLine lineLocalMic =
                AudioSystem.getTargetDataLine(format);
        final InputStream source = new AudioInputStream(lineLocalMic);
        lineLocalMic.open();
        lineLocalMic.start();
        return new BufferedInputStream(source);
    }

    /**
     * Parses the audio format from the given media locator.
     * @param locator the media locator with the audio format
     * @return parsed audio format
     * @throws URISyntaxException 
     *           error converting the locator into a URI representation
     */
    private AudioFormat parseAudioFormat(final String locator)
            throws URISyntaxException {
        final URI uri = new URI(locator);
        final AudioFormat format = JavaSoundParser.parse(uri);
        setTargetAudioFormat(format);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Got AudioFormat: {0}",
                    format.toString());
        }
        return format;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleAudioStop() throws AudioException {
        if (inputStream == null) {
            return;
        }

        // Release IO
        try {
            inputStream.close();
        } catch (IOException ex) {
            throw new AudioException(ex.getMessage());
        } finally {
            inputStream = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMediaLocator(final String locator, final InputStream stream)
            throws AudioException {
        super.setMediaLocator(locator);
        inputStream = stream;
    }

    /**
     * {@inheritDoc}
     *
     * Throws an {@link IllegalArgumentException} since output streams are not
     * supported.
     */
    public void setMediaLocator(final String locator,
            final OutputStream stream)
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
    public final OutputStream getOutputStream() {
        throw new IllegalArgumentException("output streams are not supported");
    }
}
