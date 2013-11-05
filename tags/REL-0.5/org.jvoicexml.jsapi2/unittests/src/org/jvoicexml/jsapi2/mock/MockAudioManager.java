package org.jvoicexml.jsapi2.mock;

import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;
import javax.speech.AudioException;

import org.jvoicexml.jsapi2.BaseAudioManager;

/**
 * Dummy implementation of an {@link BaseAudioManager} for test purposes.
 * @author Dirk Schnelle-Walka
 */
public class MockAudioManager extends BaseAudioManager {
    /**
     * Creates a new object.
     */
    public MockAudioManager() {
        super (new MockEngine());
    }

    /**
     * {@inheritDoc}
     */
    public AudioFormat getAudioFormat() {
        return new AudioFormat(AudioFormat.Encoding.ULAW,
                8000.0f, 16, 1, 16, 8000, false);
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
