/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 68 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2012 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package org.jvoicexml.jsapi2;

/**
 * Simple container for an audio format.
 * 
 * @author Dirk Schnelle-Walka
 */
public final class AudioFormat {
    /**
     * The audio encoding technique used by this format.
     */
    private String encoding;

    /**
     * The number of samples played or recorded per second, for sounds that have
     * this format.
     */
    private float sampleRate;

    /**
     * The number of bits in each sample of a sound that has this format.
     */
    private int sampleSizeInBits;

    /**
     * The number of audio channels in this format (1 for mono, 2 for stereo).
     */
    private int channels;

    /**
     * The number of bytes in each frame of a sound that has this format.
     */
    private int frameSize;

    /**
     * The number of frames played or recorded per second, for sounds that have
     * this format.
     */
    private float frameRate;

    /**
     * Indicates whether the audio data is stored in big-endian or little-endian
     * order.
     */
    private boolean bigEndian;

    /**
     * Constructs an <code>AudioFormat</code> with the given parameters.
     * 
     * @param enc
     *            the audio encoding technique
     * @param smpRate
     *            the number of samples per second
     * @param smpSize
     *            the number of bits in each sample
     * @param chan
     *            the number of channels (1 for mono, 2 for stereo, and so on)
     * @param frmSize
     *            the number of bytes in each frame
     * @param frmRate
     *            the number of frames per second
     * @param bigend
     *            indicates whether the data for a single sample is stored in
     *            big-endian byte order (<code>false</code> means little-endian)
     */
    public AudioFormat(final String enc, final float smpRate,
            final int smpSize, final int chan, final int frmSize,
            final float frmRate, final boolean bigend) {
        encoding = enc;
        sampleRate = smpRate;
        sampleSizeInBits = smpSize;
        channels = chan;
        frameSize = frmSize;
        frameRate = frmRate;
        bigEndian = bigend;
    }

    /**
     * Obtains the type of encoding for sounds in this format.
     * 
     * @return the encoding type
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Obtains the sample rate.
     * 
     * @return the number of samples per second
     * 
     * @see #getFrameRate()
     */
    public float getSampleRate() {
        return sampleRate;
    }

    /**
     * Obtains the size of a sample.
     * 
     * @return the number of bits in each sample
     * 
     * @see #getFrameSize()
     */
    public int getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    /**
     * Obtains the number of channels. 
     * 
     * @return The number of channels (1 for mono, 2 for stereo, etc.
     */
    public int getChannels() {
        return channels;
    }

    /**
     * Obtains the frame size in bytes.
     * 
     * @return the number of bytes per frame
     * 
     * @see #getSampleSizeInBits()
     */
    public int getFrameSize() {
        return frameSize;
    }

    /**
     * Obtains the frame rate in frames per second.
     * 
     * @return the number of frames per second
     * 
     * @see #getSampleRate()
     */
    public float getFrameRate() {
        return frameRate;
    }

    /**
     * Indicates whether the audio data is stored in big-endian or little-endian
     * byte order. If the sample size is not more than one byte, the return
     * value is irrelevant.
     * 
     * @return <code>true</code> if the data is stored in big-endian byte order,
     *         <code>false</code> if little-endian
     */
    public boolean isBigEndian() {

        return bigEndian;
    }
}
