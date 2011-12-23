package org.jvoicexml.jsapi2.test;

import java.io.InputStream;
import java.io.OutputStream;

import javax.speech.AudioException;
import javax.speech.AudioManager;

import org.jvoicexml.jsapi2.AudioFormat;
import org.jvoicexml.jsapi2.BaseAudioManager;

/**
 * Dummy implementation of an {@link AudioManager} for test purposes.
 * @author Dirk SChnelle-Walka
 */
public class DummyAudioManager extends BaseAudioManager {

    /**
     * {@inheritDoc}
     */
    public AudioFormat getAudioFormat() {
        return new AudioFormat("ulaw", 8000.0f, 16, 1, 16, 8000, false);
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getInputStream() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public OutputStream getOutputStream() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    protected void handleAudioStart() throws AudioException {
    }

    /**
     * {@inheritDoc}
     */
    protected void handleAudioStop() throws AudioException {
    }

    /**
     * {@inheritDoc}
     */
    public void setMediaLocator(String locator, InputStream stream)
            throws AudioException, IllegalStateException,
            IllegalArgumentException, SecurityException {
    }

    /**
     * {@inheritDoc}
     */
    public void setMediaLocator(String locator, OutputStream stream)
            throws AudioException, IllegalStateException,
            IllegalArgumentException, SecurityException {
    }
}
