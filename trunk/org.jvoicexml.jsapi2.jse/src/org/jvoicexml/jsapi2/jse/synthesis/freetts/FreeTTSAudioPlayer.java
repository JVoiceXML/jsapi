package org.jvoicexml.jsapi2.jse.synthesis.freetts;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;

import org.jvoicexml.jsapi2.jse.BaseAudioManager;

import com.sun.speech.freetts.audio.AudioPlayer;

/**
 * Audioplayer for the JSAPI 2 base implementation.
 *
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 * @version 1.0
 */
public class FreeTTSAudioPlayer implements AudioPlayer {
    /** The collected audio data. */
    private ByteArrayOutputStream buffer;

    private BaseAudioManager baseAudioManager;

    /** The audio format to use. */
    private AudioFormat audioFormat;

    /**
     * Constructs a new object.
     * @param baseAudioManager the audio manager.
     */
    public FreeTTSAudioPlayer(BaseAudioManager baseAudioManager) {
        this.baseAudioManager = baseAudioManager;
        buffer = new ByteArrayOutputStream();
        audioFormat = baseAudioManager.getEngineAudioFormat();
    }

    /**
     * {@inheritDoc}
     */
    public void begin(int size) {
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
        buffer.reset();
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
    public void setAudioFormat(AudioFormat format) {
        audioFormat = format;
    }

    /**
     * {@inheritDoc}
     */
    public void setVolume(float volume) {
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
    public boolean write(byte[] audioData) {
        try {
            buffer.write(audioData);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean write(byte[] audioData, int offset, int size) {
        buffer.write(audioData, offset, size);
        return true;
    }

    /**
     * Retrieves the collected audio data.
     * @return the collected audio data.
     */
    public byte[] getAudioBytes() throws IOException {
        byte[] res = buffer.toByteArray();
        ByteArrayInputStream bais = baseAudioManager.getConvertedAudio(res);
        res = new byte[bais.available()];
        try {
            bais.read(res);
        } catch (IOException ex) {
        }

        return res;
    }
}
