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

package org.jvoicexml.jsapi2.jse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.sound.sampled.AudioFormat;

/**
 * Utility to convert audio from a source format to another format.
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 *
 */
public class AudioFormatConverter {

    private final PipedInputStream pipedInputStream;
    private final PipedOutputStream pipedOutputStream;

    private final InputStream convertedInputStream;
    /** The source audio format. */
    private final AudioFormat sourceFormat;
    /** The target audio format. */
    private final AudioFormat targetFormat;

    private final int pipeSize;

    public AudioFormatConverter(BaseAudioManager manager,
            AudioFormat sourceFormat, AudioFormat targetFormat)
        throws IOException {
        this.sourceFormat = sourceFormat;
        this.targetFormat = targetFormat;

        //Conversion pipeline
        pipeSize = getAudioFormatBytesPerSecond(sourceFormat) * 40;
        pipedInputStream = new PipedInputStream(pipeSize);
        pipedOutputStream = new PipedOutputStream(pipedInputStream);

        convertedInputStream = manager.getConvertedStream(pipedInputStream,
                sourceFormat, targetFormat);

    }

    public void close() {
        try {
            pipedInputStream.close();
            pipedOutputStream.close();
            convertedInputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
        //Make sure that pipeline is "as clean as possible"
        //if (convertedInputStream.available() > 0) {}

        //Allocate an array for 1 second of audio in target format
        byte[] convertedArray =
            new byte[getAudioFormatBytesPerSecond(targetFormat)];
        int offset = 0;
        int br = -1;
        final int bytesPerRead = 512;
        int writeOffset = 0;
        int writeSize = 0;
        boolean noMoreInput = false;
        boolean insertedSilence = false;
        do {

            //Inject new audio in pipeline
            if (writeOffset < in.length) {
                writeSize = pipeSize - pipedInputStream.available();
                writeSize = Math.min(writeSize, in.length - writeOffset);
                try {
                    pipedOutputStream.write(in, writeOffset,
                            writeSize);
                    writeOffset += writeSize;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.err.println("Off: " + writeOffset);
                    System.err.println("WrtSz: " + writeSize);
                    System.err.println("BUfferSz: " + in.length);

                }
                pipedOutputStream.flush();
            } else {
                noMoreInput = true;

                if (!insertedSilence) {
                    //Generate 100ms os silence to compensate conversion loss
                    byte silenceSample = 0;
                    int bps =
                        getAudioFormatBytesPerSecond(sourceFormat);
                    byte[] silence = new byte[bps / 10];
                    for (int i = 0; i < silence.length; i++) {
                        silence[i] = silenceSample;
                    }
                    pipedOutputStream.write(silence);
                    insertedSilence = true;
                }
            }

            //Realloc array?
            int availableSize = convertedArray.length - offset;
            if (availableSize < bytesPerRead) {
                convertedArray = (byte[]) BaseAudioManager.resizeArray(
                        convertedArray, convertedArray.length * 2);
            }

            //Read converted data and write it in array
            if (noMoreInput && (convertedInputStream.available()
                    < (getAudioFormatBytesPerSecond(sourceFormat)) / 10)) {

                //Read the flushed audio
                br = convertedInputStream.read(convertedArray, offset,
                        bytesPerRead);

                //and go away
                br = -1;

                //clearing the pipeline
                if (pipedInputStream.available() > 0) {
                    byte[] clearBuffer =
                        new byte[pipedInputStream.available()];
                    pipedInputStream.read(clearBuffer);
                }

            } else {
                br = convertedInputStream.read(convertedArray, offset,
                        bytesPerRead);
            }
            if (br != -1) {
                offset += br;
            } else {
                break;
            }
        } while (true);

        //Return a new ByteArrayInputStream
        return new ByteArrayInputStream(convertedArray, 0, offset);
    }

    /**
     * Retrieves the number of bytes per second for the given audio
     * format.
     * @param audioFormat the audio format
     * @return number of bytes per second.
     */
    public static int getAudioFormatBytesPerSecond(
            final AudioFormat audioFormat) {
        int bps = audioFormat.getChannels();
        bps *= audioFormat.getSampleRate();
        bps *= (audioFormat.getSampleSizeInBits() / 8);
        return bps;
    }
}
