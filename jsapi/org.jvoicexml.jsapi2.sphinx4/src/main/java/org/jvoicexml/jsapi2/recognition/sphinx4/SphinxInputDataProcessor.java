/*
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2017 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.jsapi2.recognition.sphinx4;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;

import edu.cmu.sphinx.frontend.BaseDataProcessor;
import edu.cmu.sphinx.frontend.Data;
import edu.cmu.sphinx.frontend.DataEndSignal;
import edu.cmu.sphinx.frontend.DataProcessingException;
import edu.cmu.sphinx.frontend.DataStartSignal;
import edu.cmu.sphinx.frontend.DoubleData;
import edu.cmu.sphinx.frontend.util.DataUtil;
import edu.cmu.sphinx.util.props.Configurable;
import edu.cmu.sphinx.util.props.PropertyException;
import edu.cmu.sphinx.util.props.PropertySheet;
import edu.cmu.sphinx.util.props.S4Boolean;
import edu.cmu.sphinx.util.props.S4Integer;

/**
 * A data processor to read the data from a given input stream and feed it into
 * the sphinx system.
 * 
 * @author Renato Cassaca
 * @author Stefan Radomski
 * @author Dirk Schnelle-Walka
 */
public class SphinxInputDataProcessor extends BaseDataProcessor
        implements Configurable {
    /** Logger for this class. */
    private static final Logger LOGGER = Logger
            .getLogger(SphinxInputDataProcessor.class.getName());

    @S4Integer(defaultValue = 16000)
    public static final String SAMPLING_RATE = "samplingRate";

    @S4Integer(defaultValue = 16)
    public static final String SAMPLE_SIZE_IN_BITS = "sampleSizeInBits";

    @S4Integer(defaultValue = 1)
    public static final String CHANNELS = "channels";

    @S4Boolean(defaultValue = false)
    public static final String SIGNED = "signed";

    @S4Boolean(defaultValue = false)
    public static final String BIG_ENDIAN = "bigEndian";

    /** The input stream from the audio manager. */
    private InputStream inputStream;

    /** Total number of read samples. */
    private long totalSamplesRead;

    /** sent DataStart signal */
    private boolean sentStarted = false;

    /** sent DataEnd signal */
    private boolean sentEnded = false;

    /** is running */
    private boolean running = true;

    private AudioFormat format;

    /**
     * Constructs a new object.
     */
    public SphinxInputDataProcessor() {
    }

    public void newProperties(PropertySheet ps) throws PropertyException {
        super.newProperties(ps);
        final int samplingRate = ps.getInt(SAMPLING_RATE);
        final int sampleSizeInBits = ps.getInt(SAMPLE_SIZE_IN_BITS);
        final int channels = ps.getInt(CHANNELS);
        final boolean signed = ps.getBoolean(SIGNED);
        final boolean bigEndian = ps.getBoolean(BIG_ENDIAN);
        format = new AudioFormat(samplingRate, sampleSizeInBits, channels,
                signed, bigEndian);
    }

    /**
     * Sets the input stream to read the audio from.
     * 
     * @param in
     *            the input stream.
     */
    public void setInputStream(final InputStream in) {
        inputStream = in;
    }

    /**
     * Start or stop this data processor
     * 
     * We need to send a DataStartSignal before commencing to send samples. When
     * we were stopped, send a DataEndSignal and return null for subsequent
     * calls to getData().
     * 
     * This way, we can stop this processor and have calls to recognize() return
     * immediately.
     * 
     * @param running
     *            {@code true} if notifications about the status change should
     *            be sent
     */
    public synchronized void isRunning(final boolean running) {
        if (this.running != running) {
            if (running) {
                sentStarted = false;
            } else {
                sentEnded = false;
            }
            this.running = running;
        }
    }

    /**
     * Retrieves the audio format.
     * 
     * @return the audio format used by this data processor.
     */
    public AudioFormat getAudioFormat() {
        return format;
    }

    /**
     * Returns the processed Data output.
     * 
     * @return an Data object that has been processed by this DataProcessor
     * @throws DataProcessingException
     *             if a data processor error occurs
     */
    @Override
    public Data getData() throws DataProcessingException {
        final AudioFormat format = getAudioFormat();
        int channels = format.getChannels();
        boolean signed = true;
        int sampleRate = (int) format.getSampleRate();
        int sampleSizeInBytes = format.getSampleSizeInBits() / 8;
        int frameSizeInBytes = sampleRate * sampleSizeInBytes * channels * 10
                / 1000;

        // we are not running anymore, but did not sent DataEnd yet
        if (!running && !sentEnded && sentStarted) {
            sentEnded = true;
            long duration = (long) (((double) totalSamplesRead
                    / (double) sampleRate * 1000.0));
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Sending end signal");
            }
            totalSamplesRead = 0;
            return new DataEndSignal(duration);
        }

        // we are running, but have not sent DataStart yet
        if (running && !sentStarted) {
            sentStarted = true;
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Sending start signal");
            }
            return new DataStartSignal(sampleRate);
        }

        // we are not running at all
        if (!running) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Sending null");
            }
            return null;
        }

        long firstSampleNumber = totalSamplesRead / channels;

        // Read data
        byte[] data = new byte[frameSizeInBytes];
        int numBytesRead = 0;
        while (numBytesRead == 0) {
            try {
                numBytesRead = inputStream.read(data);
            } catch (IOException e) {
                throw new DataProcessingException(e.getMessage());
            }
            if (numBytesRead == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new DataProcessingException(e.getMessage(), e);
                }
            }
        }
        if (numBytesRead == -1) {
            long duration = (long) (((double) totalSamplesRead
                    / (double) sampleRate * 1000.0));

            return new DataEndSignal(duration);
        }

        totalSamplesRead += (numBytesRead / sampleSizeInBytes);

        // Convert it to double
        final double[] samples = DataUtil.littleEndianBytesToValues(data, 0,
                numBytesRead, sampleSizeInBytes, signed);
        final long collectTime = System.currentTimeMillis();
        return new DoubleData(samples, (int) sampleRate, collectTime,
                firstSampleNumber);
    }
}
