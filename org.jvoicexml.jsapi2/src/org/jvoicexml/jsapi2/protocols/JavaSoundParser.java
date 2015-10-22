/*
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2015 JVoiceXML group - http://jvoicexml.sourceforge.net
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
 */

package org.jvoicexml.jsapi2.protocols;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;

/**
 * A parser for the javasound URL schema.
 *
 * @author Dirk Schnelle-Walka
 *
 */
public final class JavaSoundParser {
    /** The default sample rate. */
    public static final int DEFAULT_SAMPLE_RATE = 16000;

    /** Number of bits per byte. */
    private static final int BITS_PER_BYTE = 8;
    
    /** Constant for a signed byte stream. */
    private static final String SIGNED = "signed";
    
    /** Constant for an unsigned byte stream. */
    private static final String UNSIGNED = "unsigned";
    
    /** Constant for big-endian byte order. */
    private static final String BIG_ENDIAN = "big";
    
    /** Constant for little-endian byte order. */
    private static final String LITTLE_ENDIAN = "little";

    
    /**
     * Prevent construction from outside.
     */
    private JavaSoundParser() {
    }

    /**
     * Parses the given URI into an audio format.
     *
     * @param url
     *            the URL to parse.
     * @return audio format.
     * @throws URISyntaxException
     *         error parsing the URL
     */
    public static AudioFormat parse(final URL url) throws URISyntaxException {
        URI uri = url.toURI();
        return parse(uri);
    }
    
        /**
     * Parses the given URI into an audio format.
     *
     * @param uri
     *            the URI to parse.
     * @return audio format.
     * @throws URISyntaxException
     *         error parsing the URL
     */
    public static AudioFormat parse(final URI uri) throws URISyntaxException {
        final Map<String, String> parameters = new HashMap<String, String>();
        if (uri.getQuery() != null) {
            String[] parametersString = uri.getQuery().split("\\&");
            for (String part : parametersString) {
                String[] queryElement = part.split("\\=");
                if (queryElement.length == 2) {
                    parameters.put(queryElement[0], queryElement[1]);
                }
            }
        }

        // Default values for AudioFormat parameters
        AudioFormat.Encoding encoding = AudioFormat.Encoding.ULAW;
        float sampleRate = DEFAULT_SAMPLE_RATE;
        int bits = BITS_PER_BYTE;
        int channels = 1;
        boolean endian = true;
        boolean signed = true;

        // Change default values as specified
        final String signedStr = parameters.get("signed");
        if (signedStr != null) {
            if (signedStr.equalsIgnoreCase(UNSIGNED)
                    || signedStr.equalsIgnoreCase(Boolean.FALSE.toString())) {
                signed = false;
            } else {
                if (signedStr.equalsIgnoreCase(SIGNED)
                        || Boolean.valueOf(signedStr)) {
                    signed = true;
                }
            }
        }

        final String encodingStr = parameters.get("encoding");
        if (encodingStr != null) {
            if (encodingStr.equals("pcm")) {
                if (signed) {
                    encoding = AudioFormat.Encoding.PCM_SIGNED;
                } else {
                    encoding = AudioFormat.Encoding.PCM_UNSIGNED;
                }
            } else if (encodingStr.equals("alaw")) {
                encoding = AudioFormat.Encoding.ALAW;
            } else if (encodingStr.equals("ulaw")) {
                encoding = AudioFormat.Encoding.ULAW;
            } else if (encodingStr.equals("gsm")) {
                throw new URISyntaxException(uri.toString(),
                        "gsm is currently not supported!");
            }
        }

        final String rateStr = parameters.get("rate");
        if (rateStr != null) {
            sampleRate = Float.valueOf(rateStr);
        }

        final String bitsStr = parameters.get("bits");
        if (bitsStr != null) {
            bits = Integer.valueOf(bitsStr);
        }

        final String channelsStr = parameters.get("channels");
        if (channelsStr != null) {
            channels = Integer.valueOf(channelsStr);
        }

        final String endianStr = parameters.get("endian");
        if (endianStr != null) {
            if (endianStr.equalsIgnoreCase(LITTLE_ENDIAN)) {
                endian = false;
            } else if (endianStr.equalsIgnoreCase(BIG_ENDIAN)) {
                endian = true;
            }
        }

        // Construct the AudioFormat
        return new AudioFormat(encoding, sampleRate,
                bits, channels, channels * bits / BITS_PER_BYTE, sampleRate,
                endian);
    }
}
