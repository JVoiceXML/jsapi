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
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.jvoicexml.jsapi2.BaseAudioManager;

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

        convertedInputStream = getConvertedStream(pipedInputStream,
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
                convertedArray = (byte[]) resizeArray(
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

    /**
     * @todo Insure that this is robust...
     *
     * @param is InputStream
     * @param sourceFormat AudioFormat
     * @param targetFormat AudioFormat
     * @return InputStream
     */
    public InputStream getConvertedStream(InputStream is,
                                           AudioFormat sourceFormat,
                                           AudioFormat targetFormat) {
        /** @todo Compare more precisely AudioFormat (not using AudioFormat.matches()) */
        if (sourceFormat.matches(targetFormat)) {
            return is;
        }

        //Describe source stream as an AudioFormat
        AudioInputStream sourceStream = new AudioInputStream(is, sourceFormat,
                AudioSystem.NOT_SPECIFIED);

        //Convert number of channels
        sourceStream = convertChannels(sourceStream, targetFormat);


        try{
            //Convert sample rate
            sourceStream = convertSampleRate(sourceStream, targetFormat);

            //Convert encoding
            sourceStream = convertEncoding(sourceStream, targetFormat);
        }catch(Exception e) {
            //Convert encoding
            sourceStream = convertEncoding(sourceStream, targetFormat);

            sourceStream = convertSampleRate(sourceStream, targetFormat);
        }

        //Convert sample size
        sourceStream = convertSampleSize(sourceStream, targetFormat);

        //Convert endianess
        sourceStream = convertEndianess(sourceStream, targetFormat);

        return sourceStream;
    }

    /**
     * @todo Insure that this is robust...
     *
     * @param os OutputStream
     * @param engineAudioFormat AudioFormat
     * @param audioFormat AudioFormat
     * @return OutputStream
     */
    public OutputStream getConvertedStream(final OutputStream os,
                                            AudioFormat engineAudioFormat,
                                            AudioFormat targetFormat) {
        /** @todo Compare more preciselly AudioFormat (not using AudioFormat.matches()) */
        if (engineAudioFormat.matches(targetFormat)) {
            return os;
        }

        try {
            //Basic Conversion support
            PipedInputStream pis = new PipedInputStream(16000000);
            PipedOutputStream pos = new PipedOutputStream(pis);

            //Describe source audio
            getConvertedStream(pis, engineAudioFormat, targetFormat);

            return pos;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        /** @todo Should never reach this point */
        return os;
    }


    private AudioInputStream convertEndianess(AudioInputStream ais,
                                              AudioFormat targetFormat) {
        if (ais.getFormat().isBigEndian() == targetFormat.isBigEndian()) {
            return ais;
        }

        AudioFormat newFormat = new AudioFormat(ais.getFormat().getEncoding(),
                                                ais.getFormat().getSampleRate(),
                                                ais.getFormat().
                                                getSampleSizeInBits(),
                                                ais.getFormat().getChannels(),
                                                ais.getFormat().getFrameSize(),
                                                ais.getFormat().getFrameRate(),
                                                targetFormat.isBigEndian());

        return AudioSystem.getAudioInputStream(newFormat, ais);
    }

    private AudioInputStream convertChannels(AudioInputStream ais,
                                             AudioFormat targetFormat) {
        if (ais.getFormat().getChannels() == targetFormat.getChannels()) {
            return ais;
        }
        AudioFormat newFormat = new AudioFormat(ais.getFormat().getEncoding(),
                                                ais.getFormat().getSampleRate(),
                                                ais.getFormat().
                                                getSampleSizeInBits(),
                                                targetFormat.getChannels(),
                                                ais.getFormat().getFrameSize(),
                                                ais.getFormat().getFrameRate(),
                                                ais.getFormat().isBigEndian());

        return AudioSystem.getAudioInputStream(newFormat, ais);

    }

    private AudioInputStream convertSampleRate(AudioInputStream ais,
                                               AudioFormat targetFormat) {
        if (ais.getFormat().getSampleRate() == targetFormat.getSampleRate()) {
            return ais;
        }
        AudioFormat newFormat = new AudioFormat(ais.getFormat().getEncoding(),
                                                targetFormat.getSampleRate(),
                                                ais.getFormat().
                                                getSampleSizeInBits(),
                                                ais.getFormat().getChannels(),
                                                ais.getFormat().getFrameSize(),
                                                targetFormat.getFrameRate(),
                                                ais.getFormat().isBigEndian());

        return AudioSystem.getAudioInputStream(newFormat, ais);
    }

    private AudioInputStream convertEncoding(AudioInputStream ais,
                                             AudioFormat targetFormat) {
        if (ais.getFormat().getEncoding() == targetFormat.getEncoding()) {
            return ais;
        }
        AudioFormat newFormat = new AudioFormat(targetFormat.getEncoding(),
                                                ais.getFormat().getSampleRate(),
                                                targetFormat.
                                                getSampleSizeInBits(),
                                                ais.getFormat().getChannels(),
                                                targetFormat.getFrameSize(),
                                                ais.getFormat().getFrameRate(),
                                                ais.getFormat().isBigEndian());

        return AudioSystem.getAudioInputStream(newFormat, ais);
    }


    private AudioInputStream convertSampleSize(AudioInputStream ais,
                                               AudioFormat targetFormat) {
        if (ais.getFormat().getSampleSizeInBits() ==
            targetFormat.getSampleSizeInBits()) {
            return ais;
        }
        AudioFormat newFormat = new AudioFormat(ais.getFormat().getEncoding(),
                                                ais.getFormat().getSampleRate(),
                                                targetFormat.
                                                getSampleSizeInBits(),
                                                ais.getFormat().getChannels(),
                                                ais.getFormat().getFrameSize(),
                                                ais.getFormat().getFrameRate(),
                                                ais.getFormat().isBigEndian());

        return AudioSystem.getAudioInputStream(newFormat, ais);
    }

    /**
     * Reallocates an array with a new size, and copies the contents
     * of the old array to the new array.
     * @param oldArray  the old array, to be reallocated.
     * @param newSize   the new array size.
     * @return          A new array with the same contents.
     */
    static Object resizeArray (Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(
            elementType,newSize);
        int preserveLength = Math.min(oldSize,newSize);
        if (preserveLength > 0)
            System.arraycopy (oldArray,0,newArray,0,preserveLength);
        return newArray;
    }
}
