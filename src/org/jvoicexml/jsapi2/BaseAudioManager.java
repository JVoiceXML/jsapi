/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: $
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
    protected Vector listeners;

    protected int audioMask;

    private String mediaLocator = "";

    /**
     * Class constructor.
     */
    public BaseAudioManager() {
        listeners = new Vector();
        audioMask = AudioEvent.DEFAULT_MASK;
    }

    /**
     * Requests notification of <code>AudioEvents</code> from the
     * <code>AudioManager</code>.
     *
     * @param listener the listener to add
     */
    public void addAudioListener(AudioListener listener) {
        if (!listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }

    /**
     * Removes an <code>AudioListener</code> from the list of
     * <code>AudioListeners</code>.
     *
     * @param listener the listener to remove
     */
    public void removeAudioListener(AudioListener listener) {
        listeners.removeElement(listener);
    }

    public int getAudioMask() {
        return audioMask;
    }

    public void setAudioMask(int mask) {
        audioMask = mask;
    }

    public void audioStart() throws SecurityException, AudioException {
    }

    public void audioStop() throws SecurityException, AudioException {
    }

    public void setMediaLocator(String locator) throws AudioException,
            AudioException, EngineStateException, IllegalArgumentException,
            SecurityException {
        mediaLocator = locator;
    }

    public void setMediaLocator(String locator, InputStream stream) throws
            AudioException,
            AudioException, EngineStateException, IllegalArgumentException,
            SecurityException {
        mediaLocator = locator;
    }

    public void setMediaLocator(String locator, OutputStream stream) throws
            AudioException,
            AudioException, EngineStateException, IllegalArgumentException,
            SecurityException {
        mediaLocator = locator;
    }

    public String getMediaLocator() {
        return mediaLocator;
    }

    public String[] getSupportedMediaLocators(String mediaLocator) throws
            IllegalArgumentException {
        return null;
    }

    public boolean isSupportedMediaLocator(String mediaLocator) throws
            IllegalArgumentException {
        return false;
    }

    public boolean isSameChannel(AudioManager audioManager) {
        return false;
    }
}

