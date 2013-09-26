/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An base implementation for JSR 113.
 *
 * Copyright (C) 2007-2009 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


/**
 * Utility to convert audio from a source format to another format.
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 *
 */
public final class AudioFormatConverter {

    /** The source audio format. */
    private final AudioFormat sourceFormat;
    /** The target audio format. */
    private final AudioFormat targetFormat;

    /**
     * Creates a new object.
     * @param source the audio format to convert from
     * @param target the audio format to convert to
     */
    public AudioFormatConverter(final AudioFormat source,
            final AudioFormat target) {
        sourceFormat = source;
        targetFormat = target;
    }

    /**
     * Convert the given audio data from the source format to the target format.
     * @param in bytes to convert.
     * @return converted bytes stream
     * @throws IOException
     *         error converting
     */
    public InputStream getConvertedAudio(final byte[] in)
        throws IOException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(in);
        return getConvertedStream(stream);
    }

    /**
     * Converts the audio coming from the given input stream from the source
     * audio format into the target audio format.
     *
     * @param is the stream that delivers audio in the source format
     * @return an input stream that delivers audio in the target format
     */
    public InputStream getConvertedStream(final InputStream is) {
        if (sourceFormat.matches(targetFormat)) {
            return is;
        }

        // Describe source stream as an AudioFormat
        final AudioInputStream sourceStream = new AudioInputStream(is,
                sourceFormat, AudioSystem.NOT_SPECIFIED);
        return AudioSystem.getAudioInputStream(targetFormat, sourceStream);
    }

    /**
     * Converts the audio written to the given output stream from the source
     * audio format into the target audio format.
     *
     * @param os the stream where audio in the source format is written to
     * @return an output stream where audio in the target format is written to
     * @exception IOException error redirecting the stream
     */
    public OutputStream getConvertedStream(final OutputStream os) 
            throws IOException {
        if (sourceFormat.matches(targetFormat)) {
            return os;
        }

        // Basic Conversion support
        final PipedInputStream pis = new PipedInputStream(16000000);
        final PipedOutputStream pos = new PipedOutputStream(pis);

        // Describe source audio
        getConvertedStream(pis);

        return pos;
    }
}
