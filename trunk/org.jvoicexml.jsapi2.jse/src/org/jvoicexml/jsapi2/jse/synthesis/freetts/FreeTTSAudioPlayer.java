package org.jvoicexml.jsapi2.jse.synthesis.freetts;


import com.sun.speech.freetts.audio.AudioPlayer;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;

import org.jvoicexml.jsapi2.jse.BaseAudioManager;


/**
 * <p>Title: JSAPI2Engines</p>
 *
 * <p>Description: JSAPI 2.0 Engines implementations</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: INESC-ID L2F</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class FreeTTSAudioPlayer implements AudioPlayer {

    private OutputStream targetStream;

    private FileOutputStream fos;

    private List<byte[]> audioBytes;

    private List<byte[]> audioDuringPause;

    private boolean paused;

    private BaseAudioManager baseAudioManager;

    public FreeTTSAudioPlayer(OutputStream targetStream,
                              BaseAudioManager baseAudioManager) {
        this.baseAudioManager = baseAudioManager;
        this.targetStream = targetStream;
        audioDuringPause = new ArrayList<byte[]>();
        audioBytes = new ArrayList<byte[]>();
        paused = false;

        try {
            fos = new FileOutputStream("freetts_dump.raw");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Starts the output of a set of data.
     *
     * @param size the size of data in bytes to be output before
     *   <code>end</code> is called.
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public void begin(int size) {
    }

    /**
     * Cancels all queued output.
     *
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public void cancel() {
    }

    /**
     * Waits for all audio playback to stop, and closes this AudioPlayer.
     *
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public void close() {
    }

    /**
     * Waits for all queued audio to be played
     *
     * @return <code>true</code> if the audio played to completion;
     *   otherwise <code> false </code> if the audio was stopped
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public boolean drain() {
        return false;
    }

    /**
     * Signals the end of a set of data.
     *
     * @return <code>true</code> if the audio was output properly, <code>
     *   false</code> if the output was cancelled or interrupted.
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public boolean end() {
        return true;
    }

    /**
     * Retrieves the audio format for this player
     *
     * @return the current audio format
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public AudioFormat getAudioFormat() {
        return null;
    }

    /**
     * Gets the amount of audio played since the last resetTime
     *
     * @return the amount of audio in milliseconds
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public long getTime() {
        return 0L;
    }

    /**
     * Returns the current volume.
     *
     * @return the current volume (between 0 and 1)
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public float getVolume() {
        return 0.0F;
    }

    /**
     * Pauses all audio output on this player.
     *
     */
    public void pause() {
        paused = true;
    }

    /**
     * Prepares for another batch of output.
     *
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public void reset() {
    }

    /**
     *
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public void resetTime() {
    }

    /**
     *
     */
    public void resume() {
        //Writes pending audio
        synchronized (audioDuringPause) {
            for (byte[] buffer : audioDuringPause) {
                try {
                    fos.write(buffer);
                    targetStream.write(buffer);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            paused = false;
        }
    }

    /**
     * Sets the audio format to use for the next set of outputs.
     *
     * @param format the audio format
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public void setAudioFormat(AudioFormat format) {
    }

    /**
     * Sets the current volume.
     *
     * @param volume the new volume (between 0 and 1)
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public void setVolume(float volume) {
    }

    /**
     *
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public void showMetrics() {
    }

    /**
     *
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public void startFirstSampleTimer() {
    }

    /**
     * Writes the given bytes to the audio stream
     *
     * @param audioData audio data to write to the device
     * @return <code>true</code> of the write completed successfully, <code>
     *   false </code>if the write was cancelled.
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public boolean write(byte[] audioData) {
        byte[] a = audioData.clone();
        try {
            fos.write(audioData);
        } catch (IOException ex) {
        }
        audioBytes.add(a);
        return true;
        //return write(audioData, 0, audioData.length);
    }

    /**
     * Writes the given bytes to the audio stream
     *
     * @param audioData audio data to write to the device
     * @param offset the offset into the buffer
     * @param size the number of bytes to write.
     * @return <code>true</code> of the write completed successfully, <code>
     *   false </code>if the write was cancelled.
     * @todo Implement this com.sun.speech.freetts.audio.AudioPlayer method
     */
    public boolean write(byte[] audioData, int offset, int size) {
        if (paused == false) {
            try {
                fos.write(audioData, offset, size);
                targetStream.write(audioData, offset, size);
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        else {
            synchronized (audioDuringPause) {
                byte[] buffer;
                if ((offset != 0) && (size != audioData.length)) {
                    buffer = new byte[size - offset];
                    System.arraycopy(audioData, offset, buffer, 0, size - offset);
                }
                else {
                    buffer = audioData;
                }
                audioDuringPause.add(buffer);
            }
        }
        return true;
    }

    public void clearAudioBytes(){
        audioBytes.clear();
    }

    public byte[] getAudioBytes(){
        int size=0;
        for (int i=0; i<audioBytes.size(); ++i){
            size += audioBytes.get(0).length;
        }

        byte[] res = new byte[size];

        for (int i=0,writer=0; i<audioBytes.size(); ++i){
            for (int j=0; j<audioBytes.get(i).length; ++j)
                res[writer++] = audioBytes.get(i)[j];
        }

        ByteArrayInputStream bais = baseAudioManager.getConvertedAudio(res);
        res = new byte[bais.available()];
        try {
            bais.read(res);
        } catch (IOException ex) {
        }

        return res;
    }

}
