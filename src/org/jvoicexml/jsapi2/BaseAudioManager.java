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
import java.util.Vector;
import javax.speech.AudioException;
import javax.speech.EngineStateException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.speech.AudioEvent;
import java.util.Enumeration;
import javax.speech.recognition.Recognizer;
import javax.speech.synthesis.Synthesizer;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
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
    protected Vector audioListeners;

    protected int audioMask;

    protected String mediaLocator = "";

    protected BaseEngine engine;

    protected OutputStream outputStream;

    protected InputStream inputStream;

    /**
     * Class constructor.
     */
    public BaseAudioManager(BaseEngine engine) {
        this.engine = engine;
        audioListeners = new Vector();
        audioMask = AudioEvent.DEFAULT_MASK;
        inputStream = null;
        outputStream = null;
        mediaLocator = "";
    }

    /**
     * Requests notification of <code>AudioEvents</code> from the
     * <code>AudioManager</code>.
     *
     * @param listener the listener to add
     */
    public void addAudioListener(AudioListener listener) {
        if (!audioListeners.contains(listener)) {
            audioListeners.addElement(listener);
        }
    }

    /**
     * Removes an <code>AudioListener</code> from the list of
     * <code>AudioListeners</code>.
     *
     * @param listener the listener to remove
     */
    public void removeAudioListener(AudioListener listener) {
        audioListeners.removeElement(listener);
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
            outputStream = os;
        } else {
            InputStream is = null;
            try {
                is = urlConnection.getInputStream();
            } catch (IOException ex) {
                throw new AudioException("Cannot get InputStream from URL: " +
                                         ex.getMessage());
            }
            inputStream = is;
        }

        postAudioEvent(AudioEvent.AUDIO_STARTED, AudioEvent.AUDIO_LEVEL_MIN);

    }

    public void audioStop() throws SecurityException,
            AudioException, EngineStateException {

        if (System.getProperty("javax.speech.supports.audio.management") == null) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
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
        Enumeration listeners = audioListeners.elements();
        while (listeners.hasMoreElements()) {
            AudioListener al = (AudioListener) listeners.nextElement();
            ((AudioListener) al).audioUpdate(event);
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }


}

