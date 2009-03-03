package org.jvoicexml.jsapi2.jse.protocols.playback;

import java.net.URLConnection;
import java.io.IOException;
import java.net.URL;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.net.URI;
import java.net.URISyntaxException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioSystem;
import java.net.UnknownServiceException;
import javax.sound.sampled.SourceDataLine;

import org.jvoicexml.jsapi2.jse.protocols.JavaSoundParser;

/**
 * A {@link URLConnection} for the playback protocol.
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 * @version 1.0
 */
public class PlaybackURLConnection extends URLConnection {

    /** Microfone access point. */
    private SourceDataLine line;

    /** Write point given to clients. */
    private OutputStream outputStream;

    /** The audio format to use. */
    private AudioFormat audioFormat;

    /**
     * Constructs a new object.
     *
     * @param url
     *            URL
     */
    public PlaybackURLConnection(final URL url)
        throws UnsupportedOperationException {
        super(url);

        // Validate the kind of input supported
        if (!url.getAuthority().equals("audio")) {
            throw new UnsupportedOperationException(
                    "Can only process 'audio'. " + url.getAuthority()
                            + " is unsupported");
        }
    }

    /**
     * {@inheritDoc}
     *
     * Closes any open line.
     */
    protected void finalize() throws Throwable {
        if (line != null) {
            line.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void connect() throws IOException {
        if (!connected) {
            // Get audio format that will open playback
            AudioFormat format = getAudioFormat();

            // Representation of the line that will be opened
            DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                    format);

            // Checks if line is supported
            if (!AudioSystem.isLineSupported(info)) {
                throw new IOException("Cannot open the requested line: "
                        + info.toString());
            }

            // Obtain, open and start the line.
            try {
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format, AudioSystem.NOT_SPECIFIED);

                // Starts the line
                line.start();
            } catch (LineUnavailableException ex) {
                throw new IOException("Line is unavailable");
            }
            connected = true;
        }
    }

    /**
     * Given URI parameters, constructs an AudioFormat.
     *
     * @return AudioFormat
     * @exception IOException
     *            error determining the audio format.
     */
    public AudioFormat getAudioFormat() throws IOException {
        if (audioFormat == null) {
            final URL url = getURL();
            try {
                audioFormat = JavaSoundParser.parse(url);
            } catch (URISyntaxException e) {
                throw new IOException(e.getMessage());
            }
        }

        return audioFormat;
    }

    /**
     * {@inheritDoc}
     * Throws an {@link UnknownServiceException}.
     *
     * @throws IOException
     *         input streams are not supported by the capture protocol.
     */
    public InputStream getInputStream()
        throws IOException {
        throw new UnknownServiceException("Cannot write to a capture device");
    }

    /**
     * {@inheritDoc}
     */
    public OutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = new SourceDataLineOutputStream();
        }
        return outputStream;
    }

    private class SourceDataLineOutputStream extends OutputStream {

        /**
         * Writes the specified byte to this output stream.
         * 
         * @param b
         *            int
         * @throws IOException
         */
        public void write(int b) throws IOException {
            if (line != null) {
                byte[] _b = new byte[1];
                _b[0] = (byte) b;
                line.write(_b, 0, 1);
            }
        }

        /**
         * Writes b.length bytes from the specified byte array to this output
         * stream.
         * 
         * @param b
         *            byte[]
         * @throws IOException
         */
        public void write(byte[] b) throws IOException {
            if (line != null) {
                line.write(b, 0, b.length);
            }
        }

        /**
         * Writes len bytes from the specified byte array starting at offset off
         * to this output stream.
         * 
         * @param b
         *            byte[]
         * @param off
         *            int
         * @param len
         *            int
         * @throws IOException
         */
        public void write(byte[] b, int off, int len) throws IOException {
            if (line != null) {
                line.write(b, off, len);
            }
        }
    }

}
