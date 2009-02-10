/**
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
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 *
 */
public class AudioFormatConverter {

    private final PipedInputStream pipedInputStream;
    private final PipedOutputStream pipedOutputStream;

    private final InputStream convertedInputStream;

    private final AudioFormat sourceFormat;
    private final AudioFormat targetFormat;

    private final int pipeSize;

    private final BaseAudioManager manager;

    public AudioFormatConverter(BaseAudioManager manager,
            AudioFormat sourceFormat, AudioFormat targetFormat) throws
            IOException {
        this.manager = manager;
        this.sourceFormat = sourceFormat;
        this.targetFormat = targetFormat;

        //Conversion pipeline
        pipeSize = manager.getAudioFormatBytesPerSecond(sourceFormat) * 40;
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

    public ByteArrayInputStream getConvertedAudio(byte[] in) throws IOException {
        //Make sure that pipeline is "as clean as possible"
        //if (convertedInputStream.available() > 0) {}

        //Allocate an array for 1 second of audio in target format
        byte[] convertedArray =
            new byte[manager.getAudioFormatBytesPerSecond(targetFormat)];
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
                try {
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
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                noMoreInput = true;

                if (!insertedSilence) {
                    //Generate 100ms os silence to compensate conversion loss
                    byte silenceSample = 0;
                    int bps =
                        manager.getAudioFormatBytesPerSecond(sourceFormat);
                    byte[] silence = new byte[bps / 10];
                    for (int i = 0; i < silence.length; i++) {
                        silence[i] = silenceSample;
                    }
                    try {
                        pipedOutputStream.write(silence);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    insertedSilence = true;
                }
            }

            //Realloc array?
            int availableSize = convertedArray.length - offset;
            if (availableSize < bytesPerRead) {
                convertedArray =(byte[]) manager.resizeArray(
                        convertedArray, convertedArray.length * 2);
            }

            //Read converted data and write it in array
            try {
                if (noMoreInput && (convertedInputStream.available()
                        < (manager.getAudioFormatBytesPerSecond(sourceFormat))
                        / 10)) {

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
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
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
}
