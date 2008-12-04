/**
 * 
 */
package org.jvoicexml.jsapi2.jse.recognition;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.jvoicexml.jsapi2.jse.BaseAudioManager;

/**
 * An {@link InputStream} that reads the ata from the microphone.
 * @author Dirk Schnelle-Walka
 *
 */
public class LineInputStream extends InputStream {
    /** The line to read the audio from. */
    private TargetDataLine line;

    /** The audio manager to use. */
    private final BaseAudioManager manager;

    /**
     * Constructs a new object.
     * @param audioManager the audio manger
     */
    public LineInputStream(final BaseAudioManager audioManager) {
        manager = audioManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        byte[] buffer = new byte[1];
        return read(buffer, 0, buffer.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] buffer, int off, int len) throws IOException {
        if (line == null) {
            getLine();
        }

        return line.read(buffer, off, len);
    }


    /**
     * Retrieves a line to read the audio data.
     * @throws IOException
     *         error opening the line.
     */
    private void getLine() throws IOException {
        final AudioFormat format = manager.getTargetAudioFormat();
        try {
            final DataLine.Info info =
                new DataLine.Info(TargetDataLine.class, format);

            line =
                (TargetDataLine) AudioSystem.getLine(info);

        } catch (LineUnavailableException e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
