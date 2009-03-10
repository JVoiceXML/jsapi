/**
 * 
 */
package org.jvoicexml.jsapi2.jse.synthesis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

import org.jvoicexml.jsapi2.jse.JseBaseAudioManager;

/**
 * A simple {@link OutputStream} that writes the data to the local speaker.
 *
 * @author Dirk Schnelle-Walka
 *
 */
public class ClipOutputStream extends OutputStream implements LineListener {
    /** The audio buffer. */
    private ByteArrayOutputStream buffer;

    /** The clip to use to play back the audio. */
    private Clip clip;

    /** Synchronization of start and end play back. */
    private final Semaphore sem;

    /** The audio manager to use. */
    private final JseBaseAudioManager manager;

    /**
     * Constructs a new object.
     * @param audioManager the audio manger
     */
    public ClipOutputStream(final JseBaseAudioManager audioManager) {
        buffer = new ByteArrayOutputStream();
        sem = new Semaphore(1);
        manager = audioManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final int b) throws IOException {
        buffer.write(b);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final byte[] b, final int off, final int len)
        throws IOException {
        buffer.write(b, off, len);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final byte[] b) throws IOException {
        buffer.write(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws IOException {
        final AudioFormat format = manager.getTargetAudioFormat();
        try {
            final DataLine.Info info = new DataLine.Info(Clip.class, format);

            try {
                sem.acquire();
            } catch (InterruptedException e) {
                throw new IOException(e.getMessage(), e);
            }
            clip = (Clip) AudioSystem.getLine(info);
            byte[] bytes = buffer.toByteArray();
            buffer.reset();
            clip.open(format, bytes, 0, bytes.length);
            clip.addLineListener(this);
            clip.start();
        } catch (LineUnavailableException e) {
            throw new IOException(e.getMessage(), e);
        }

        // Wait until all data has been played back.
        try {
            sem.acquire();
            sem.release();
        } catch (InterruptedException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final LineEvent event) {
        if ((event.getType() == LineEvent.Type.CLOSE)
                || (event.getType() == LineEvent.Type.STOP)) {
            sem.release();
        }
    }
}
