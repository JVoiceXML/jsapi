/**
 * 
 */
package org.jvoicexml.jsapi2.jse.recognition;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.jvoicexml.jsapi2.jse.BaseAudioManager;

/**
 * An {@link InputStream} that reads the d^^ata from the microphone.
 * @author Dirk Schnelle-Walka
 *
 */
public final class LineInputStream extends InputStream {
    /** Logger for this class. */
    private static final Logger LOGGER =
            Logger.getLogger(LineInputStream.class.getName());

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
    public int read(final byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(final byte[] buffer, final int off, final int len)
        throws IOException {
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

            line = (TargetDataLine) AudioSystem.getLine(info);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("opened line " + line);
            }
            line.open();
            line.start();
        } catch (LineUnavailableException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (line != null) {
            line.stop();
            line = null;
        }
        super.close();
    }

}
