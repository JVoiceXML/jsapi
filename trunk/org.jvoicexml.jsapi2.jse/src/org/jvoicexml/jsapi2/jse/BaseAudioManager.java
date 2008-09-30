/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: lyncher $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */
package org.jvoicexml.jsapi2.jse;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.SequenceInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.speech.AudioEvent;
import javax.speech.AudioException;
import javax.speech.AudioListener;
import javax.speech.AudioManager;
import javax.speech.EngineStateException;

/**
 * Supports the JSAPI 2.0 <code>AudioManager</code>
 * interface.  Actual JSAPI implementations might want to extend
 * or modify this implementation.
 */
public abstract class BaseAudioManager implements AudioManager {
    /**
     * List of <code>AudioListeners</code> registered for
     * <code>AudioEvents</code> on this object.
     */
    protected List<AudioListener> audioListeners;

    protected int audioMask;

    protected String mediaLocator = null;

    protected BaseEngine engine;
    
    protected AudioInputStream ais;

    /**
     * Audio format of the audio natively produced
     * by the engine
     */
    protected AudioFormat engineAudioFormat;

    protected AudioFormat targetAudioFormat;


    protected AudioFormatConverter formatConverter;

    /**
     * Class constructor.
     */
    public BaseAudioManager() {
        audioListeners = new ArrayList<AudioListener>();
        audioMask = AudioEvent.DEFAULT_MASK;
        engineAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                            16000,
                                            16,
                                            1,
                                            2,
                                            16000,
                                            false);
        targetAudioFormat = engineAudioFormat;
    }

    /**
     * Sets the engine.
     * @param engine the engine.
     */
    public void setEngine(BaseEngine engine) {
        this.engine = engine;
    }

    /**
     * Requests notification of <code>AudioEvents</code> from the
     * <code>AudioManager</code>.
     *
     * @param listener the listener to add
     */
    public void addAudioListener(AudioListener listener) {
        synchronized (audioListeners) {
            if (!audioListeners.contains(listener)) {
                audioListeners.add(listener);
            }
        }
    }

    /**
     * Removes an <code>AudioListener</code> from the list of
     * <code>AudioListeners</code>.
     *
     * @param listener the listener to remove
     */
    public void removeAudioListener(AudioListener listener) {
        synchronized (audioListeners) {
            audioListeners.remove(listener);
        }
    }

    public int getAudioMask() {
        return audioMask;
    }

    public void setAudioMask(int mask) {
        audioMask = mask;
    }

    /**
     * {@inheritDoc}
     */
    public abstract void audioStart() throws SecurityException,
            AudioException, EngineStateException;

    protected URLConnection openURLConnection() throws IOException {
        if (mediaLocator == null) {
            return null;
        }

        final URL url;
        try {
            url = new URL(mediaLocator);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ex);
        }

        //Open a connection to URL
        URLConnection connection = url.openConnection();
        connection.connect();
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    public abstract void audioStop() throws SecurityException,
            AudioException, EngineStateException;

    public void setMediaLocator(String locator) throws AudioException,
            AudioException, EngineStateException, IllegalArgumentException,
            SecurityException {

        //Insure that engine is DEALLOCATED
        if (engine.testEngineState(engine.DEALLOCATED) == false) {
            throw new EngineStateException(
                    "Engine is not DEALLOCATED. Cannot setMediaLocator");
        }

        if (!isSupportsAudioManagement()) {
            throw new SecurityException(
                    "AudioManager has no permission to access audio resources");
        }

        //Insure that media locator is supported
        if (isSupportedMediaLocator(locator) == false) {
            throw new AudioException("Unsupported locator: " + locator);
        }

        mediaLocator = locator;
    }

    public String getMediaLocator() {
        return mediaLocator;
    }

    /**
     * @todo THis is just a dummy implementation
     *
     * @param mediaLocator String
     * @return String[]
     * @throws IllegalArgumentException
     */
    public String[] getSupportedMediaLocators(String mediaLocator) throws
            IllegalArgumentException {
        return new String[] {mediaLocator};
    }

    public boolean isSupportedMediaLocator(String mediaLocator) throws
            IllegalArgumentException {

        String[] supportedMediaLocators = getSupportedMediaLocators(
                mediaLocator);

        return (supportedMediaLocators == null ? false : true);
    }

    /**
     * Checks if audio management is supported.
     * @return <code>true</code> if audio management is supported.
     */
    protected boolean isSupportsAudioManagement() {
        String management =
            System.getProperty("javax.speech.supports.audio.management");
        return Boolean.valueOf(management).equals(Boolean.TRUE);
    }

    /**
     * @todo Initial implementation
     *
     * @param audioManager AudioManager
     * @return boolean
     */
    public boolean isSameChannel(AudioManager audioManager) {
        return (audioManager.getMediaLocator() == mediaLocator);
    }

    protected void postAudioEvent(int eventId, int audioLevel) {
        if ((getAudioMask() & eventId) == eventId) {
            final AudioEvent event = new AudioEvent(engine, eventId);

            Runnable r = new Runnable() {
                public void run() {
                    fireAudioEvent(event);
                }
            };

            try {
                engine.getSpeechEventExecutor().execute(r);
            } catch (RuntimeException ex) {
                //Ignore exception
                ex.printStackTrace();
            }
        }
    }

    public void fireAudioEvent(AudioEvent event) {
        synchronized (audioListeners) {
            for (AudioListener listener : audioListeners) {
                listener.audioUpdate(event);
            }
        }
    }

    /**
     * Retrieves the output stream associated with the given media locator.
     * @return output stream.
     */
    public abstract OutputStream getOutputStream();

    /**
     * Retrieves the input stream associated with the given media locator.
     * @return input stream.
     */
    public abstract InputStream getInputStream();

    /**
     * Given URI parameters, constructs an AudioFormat
     *
     * @return AudioFormat
     */
    protected AudioFormat getAudioFormat() {
        //Get matching URI to extract query parameters
        URI uri = null;
        try {
            uri = new URI(mediaLocator);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
            //Continue and give back a default AudioFormat
        }

        //Initialize parameters
        Map<String, String> parameters =
            new java.util.HashMap<String, String>();
        if (uri.getQuery() != null) {
            String[] parametersString = uri.getQuery().split("\\&");
            for (String part : parametersString) {
                String[] queryElement = part.split("\\=");
                parameters.put(queryElement[0], queryElement[1]);
            }
        }

        //Default values for AudioFormat parameters
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float sampleRate = 16000;
        int bits = 16;
        int channels = 1;
        boolean endian = false;
        boolean signed = true;

        //Change default values as specified
        String signedStr = parameters.get("signed");
        if (signedStr != null) {
            signed = Boolean.valueOf(signedStr);
        }

        String encodingStr = parameters.get("encoding");
        if (encodingStr != null) {
            if (encodingStr.equals("pcm")) {
                encoding = (signed == true ? AudioFormat.Encoding.PCM_SIGNED :
                            AudioFormat.Encoding.PCM_UNSIGNED);
            } else if (encodingStr.equals("alaw")) {
                encoding = AudioFormat.Encoding.ALAW;
                endian = false;
            } else if (encodingStr.equals("ulaw")) {
                encoding = AudioFormat.Encoding.ULAW;
                endian = false;
            } else if (encodingStr.equals("gsm")) {
                /** @todo GSM not supported by AudioFormat */
                System.err.println("GSM not supported by AudioFormat... review");
            }
        }

        String rateStr = parameters.get("rate");
        if (rateStr != null) {
            sampleRate = Float.valueOf(rateStr);
        }

        String bitsStr = parameters.get("bits");
        if (bitsStr != null) {
            bits = Integer.valueOf(bitsStr);
            if (bits == 8) {
                endian = false;
            }
        }

        String channelsStr = parameters.get("channels");
        if (channelsStr != null) {
            channels = Integer.valueOf(channelsStr);
        }

        String endianStr = parameters.get("endian");
        if (endianStr != null) {
            if (endianStr.equals("little")) {
                endian = false;
            } else if (endianStr.equals("big")) {
                endian = true;
            }
        }

        //Constructs the AudioFormat
        return new AudioFormat(encoding, sampleRate, bits, channels, bits / 8,
                               sampleRate, endian);
    }

    public void setEngineAudioFormat(AudioFormat audioFormat) {
        engineAudioFormat = audioFormat;
    }

    public AudioFormat getEngineAudioFormat() {
        return engineAudioFormat;
    }

    public AudioFormat getTargetAudioFormat() {
        return targetAudioFormat;
    }

    /**
     * @todo Insure that this is robust...
     *
     * @param is InputStream
     * @param sourceFormat AudioFormat
     * @param targetFormat AudioFormat
     * @return InputStream
     */
    protected InputStream getConvertedStream(InputStream is,
                                           AudioFormat sourceFormat,
                                           AudioFormat targetFormat) {
        /** @todo Compare more preciselly AudioFormat (not using AudioFormat.matches()) */
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
    private OutputStream getConvertedStream(final OutputStream os,
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
            final InputStream is = getConvertedStream(pis, engineAudioFormat,
                    targetFormat);

            return pos;
            /*  new Thread(new Runnable() {
                  public void run() {
                      FileOutputStream synt_conv = null;
                      try {
                          synt_conv = new FileOutputStream(File.
             createTempFile("synth", ".raw", new File(".")));
                      } catch (Exception ex) {
                          ex.printStackTrace();
                      }
                      int br;
                      byte[] buffer = new byte[512];
                      try {
                          while ((br = is.read(buffer)) != -1) {
                              os.write(buffer, 0, br);
                              synt_conv.write(buffer, 0, br);
                          }
                      } catch (IOException ex) {
                          ex.printStackTrace();
                      }

                      try {
                          os.close();
                          is.close();
                      } catch (IOException ex) {
                          ex.printStackTrace();
                      }
                  }
              }, "AudioCopyer").start();*/

            //Return write point for synthesizer
           // return new OutputStreamConverter(pos, is, os);

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


    private int getAudioFormatBytesPerSecond(final AudioFormat audioFormat) {
        int bps = audioFormat.getChannels();
        bps *= audioFormat.getSampleRate();
        bps *= (audioFormat.getSampleSizeInBits() / 8);
        return bps;
    }

    /**
     * Reallocates an array with a new size, and copies the contents
     * of the old array to the new array.
     * @param oldArray  the old array, to be reallocated.
     * @param newSize   the new array size.
     * @return          A new array with the same contents.
     */
    private static Object resizeArray (Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(
            elementType,newSize);
        int preserveLength = Math.min(oldSize,newSize);
        if (preserveLength > 0)
            System.arraycopy (oldArray,0,newArray,0,preserveLength);
        return newArray;
    }

    public ByteArrayInputStream getConvertedAudio(byte[] in) {
        if (formatConverter == null) {
            return new ByteArrayInputStream(in);
        } else {
            return formatConverter.getConvertedAudio(in);
        }
    }

    /**
     *
     * @todo Have to process silence bytes other than zero!
     *
     * @param in byte[]
     * @return ByteArrayInputStream
     */
    public ByteArrayInputStream getConvertedAudio_____OLD(byte[] in) {

        //Generate 100ms os silence to compensate conversion loss
        byte silenceSample = 0;
        int bps = getAudioFormatBytesPerSecond(engineAudioFormat);
        byte[] silence = new byte[bps / 10];
        for (int i = 0; i < silence.length; i++) {
            silence[i] = silenceSample;
        }

        //Prepare streams
        ByteArrayInputStream sourceBais = new ByteArrayInputStream(in);
        ByteArrayInputStream silenceBais = new ByteArrayInputStream(silence);
        SequenceInputStream sib = new SequenceInputStream(sourceBais, silenceBais);

        try {
            InputStream convertedStream = getConvertedStream(sib, engineAudioFormat, targetAudioFormat);

            //Allocate an array for 1 second of audio in target format
            byte[] convertedArray = new byte[getAudioFormatBytesPerSecond(targetAudioFormat)];
            int offset = 0;
            int br;
            int bytesPerRead = 512;
            do {
                //Realloc array?
                int availableSize = convertedArray.length - offset;
                if (availableSize < bytesPerRead) {
                    convertedArray = (byte[])resizeArray(convertedArray, convertedArray.length * 2);
                }

                //Read converted data and write it in array
                br = convertedStream.read(convertedArray, offset, bytesPerRead);
                if (br != -1) {
                    offset += br;
                }
                else {
                    break;
                }
            } while (true);

            convertedStream.close();

            sib.close();
            silenceBais.close();
            sourceBais.close();

            //Return a new ByteArrayInputStream
            return new ByteArrayInputStream(convertedArray, 0, offset);

        } catch (Exception ex) {
            ex.printStackTrace();

            //Do not convert audio
            return new ByteArrayInputStream(in);
        }

    }


    protected class AudioFormatConverter {

        private final PipedInputStream pipedInputStream;
        private final PipedOutputStream pipedOutputStream;

        private final InputStream convertedInputStream;

        private final AudioFormat sourceFormat;
        private final AudioFormat targetFormat;

        private final int pipeSize;


        public AudioFormatConverter(AudioFormat sourceFormat, AudioFormat targetFormat) throws
                IOException {

            this.sourceFormat = sourceFormat;
            this.targetFormat = targetFormat;

            //Conversion pipeline
            pipeSize = getAudioFormatBytesPerSecond(sourceFormat) * 40;
            pipedInputStream = new PipedInputStream(pipeSize);
            pipedOutputStream = new PipedOutputStream(pipedInputStream);

            convertedInputStream = getConvertedStream(pipedInputStream, sourceFormat, targetFormat);

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

        public ByteArrayInputStream getConvertedAudio(byte[] in) {

            //Make sure that pipeline is "as clean as possible"
            //if (convertedInputStream.available() > 0) {}

            //Allocate an array for 1 second of audio in target format
            byte[] convertedArray = new byte[getAudioFormatBytesPerSecond(targetFormat)];
            int offset = 0;
            int br = -1;
            int bytesPerRead = 512;
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
                }
                else {
                    noMoreInput = true;


                    if (insertedSilence == false) {
                        //Generate 100ms os silence to compensate conversion loss
                        byte silenceSample = 0;
                        int bps = getAudioFormatBytesPerSecond(sourceFormat);
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
                    convertedArray = (byte[])resizeArray(convertedArray, convertedArray.length * 2);
                }

                //Read converted data and write it in array
                try {
                    if ((noMoreInput == true) && (convertedInputStream.available() < (getAudioFormatBytesPerSecond(sourceFormat)) / 10)) {

                        //Read the flushed audio
                        br = convertedInputStream.read(convertedArray, offset,
                                bytesPerRead);

                        //and go away
                        br = -1;

                        //clearing the pipeline
                        if (pipedInputStream.available() > 0) {
                            byte[] clearBuffer = new byte[pipedInputStream.available()];
                            pipedInputStream.read(clearBuffer);
                        }

                    }
                    else {
                        br = convertedInputStream.read(convertedArray, offset,
                                bytesPerRead);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return null;
                }
                if (br != -1) {
                    offset += br;
                }
                else {
                    break;
                }
            } while (true);

            //Return a new ByteArrayInputStream
            return new ByteArrayInputStream(convertedArray, 0, offset);
        }
    }

  /*  private class OutputStreamConverter extends OutputStream {
        private final OutputStream sourceOut;
        private final InputStream convertedStream;
        private final OutputStream targetOut;
        private long totalWriteTime = 0;

        public OutputStreamConverter(OutputStream sourceOut,
                                     InputStream convertedStream,
                                     OutputStream targetOut) {
            this.sourceOut = sourceOut;
            this.convertedStream = convertedStream;
            this.targetOut = targetOut;
        }

        public void write(int b) throws IOException {
            byte[] buffer = new byte[1];
            buffer[0] = (byte) b;
            write(buffer, 0, 1);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            long sTime = System.currentTimeMillis();
            sourceOut.write(b, off, len);
            sourceOut.flush();

            byte[] buffer = new byte[(int) Math.ceil(convertedStream.available() /
                    10)];
            int br = convertedStream.read(buffer);
            if (br != -1) {
                targetOut.write(buffer, 0, br);
            }

            long eTime = System.currentTimeMillis();
            long wTime = eTime - sTime;
            totalWriteTime += wTime;
            System.err.println("------OutputStreamConverter write time: " +
                               wTime + " total: " + totalWriteTime);

        }

        public void flush() throws IOException {
            System.err.println("------OutputStreamConverter flushing stream");

            sourceOut.flush();

            do {
                int bAvailable = convertedStream.available();
                if (bAvailable > 768) {
                    byte[] buffer = new byte[convertedStream.available() / 10];
                    int br = convertedStream.read(buffer);
                    if (br != -1) {
                        targetOut.write(buffer, 0, br);
                    }
                } else {
                    break;
                }
            } while (true);
            targetOut.flush();

            System.err.println(
                    "------OutputStreamConverter flushed audio_time: " +
                    totalWriteTime);
            totalWriteTime = 0;
        }

        public void close() throws IOException {
            flush();
            sourceOut.close();
            targetOut.close();
        }
    }*/
}

