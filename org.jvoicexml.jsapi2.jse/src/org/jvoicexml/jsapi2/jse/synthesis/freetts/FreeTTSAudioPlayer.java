package org.jvoicexml.jsapi2.jse.synthesis.freetts;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;

import org.jvoicexml.jsapi2.jse.AudioFormatConverter;
import org.jvoicexml.jsapi2.jse.JseBaseAudioManager;

import com.sun.speech.freetts.audio.AudioPlayer;

/**
 * Audioplayer for the JSAPI 2 base implementation.
 *
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 * @version 1.0
 */
public final class FreeTTSAudioPlayer implements AudioPlayer {
    /** The collected audio data. */
    private ByteArrayOutputStream buffer;

    /** Reference to the audio manager. */
    private JseBaseAudioManager baseAudioManager;

    /** The audio format to use. */
    private AudioFormat audioFormat;

    /**
     * Constructs a new object.
     * @param manager the audio manager.
     */
    public FreeTTSAudioPlayer(final JseBaseAudioManager manager) {
        baseAudioManager = manager;
        buffer = new ByteArrayOutputStream();
        audioFormat = baseAudioManager.getEngineAudioFormat();
    }

    /**
     * {@inheritDoc}
     */
    public void begin(final int size) {
    }

    /**
     * {@inheritDoc}
     */
    public void cancel() {
    }

    /**
     * {@inheritDoc}
     */
    public void close() {
    }

    /**
     * {@inheritDoc}
     */
    public boolean drain() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean end() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    /**
     * {@inheritDoc}
     */
    public long getTime() {
        return 0L;
    }

    /**
     * {@inheritDoc}
     */
    public float getVolume() {
        return 0.0F;
    }

    /**
     * {@inheritDoc}
     */
    public void pause() {
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        synchronized (buffer) {
            buffer.reset();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void resetTime() {
    }

    /**
     * {@inheritDoc}
     */
    public void resume() {
    }

    /**
     * {@inheritDoc}
     */
    public void setAudioFormat(final AudioFormat format) {
        audioFormat = format;
    }

    /**
     * {@inheritDoc}
     */
    public void setVolume(final float volume) {
    }

    /**
     * {@inheritDoc}
     */
    public void showMetrics() {
    }

    /**
     * {@inheritDoc}
     */
    public void startFirstSampleTimer() {
    }

    /**
     * {@inheritDoc}
     */
    public boolean write(final byte[] audioData) {
        try {
            synchronized (buffer) {
                buffer.write(audioData);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean write(final byte[] audioData, final int offset,
            final int size) {
        synchronized (buffer) {
            buffer.write(audioData, offset, size);
        }
        return true;
    }

    /**
     * Retrieves the collected audio data.
     * @return the collected audio data.
     * @exception IOException
     *            error reading the audio data
     */
    public InputStream getAudioBytes() throws IOException {
        byte[] res;
        synchronized (buffer) {
            res = buffer.toByteArray();
            buffer.reset();
        }
        final AudioFormatConverter converter =
            baseAudioManager.getAudioFormatConverter();
        return converter.getConvertedAudio(res);
    }
}
