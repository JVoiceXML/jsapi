/**
 * 
 */
package org.jvoicexml.jsapi2.jse;

import javax.sound.sampled.AudioFormat;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test cases for {@link AudioFormatConverter}.
 * @author Dirk Schnelle-Walka
 *
 */
public class AudioFormatConverterTest {

    /**
     * Test method for {@link org.jvoicexml.jsapi2.jse.AudioFormatConverter#getAudioFormatBytesPerSecond(javax.sound.sampled.AudioFormat)}.
     */
    @Test
    public void testGetAudioFormatBytesPerSecond() {
        final AudioFormat format1 = new AudioFormat(8000f, 16, 2, true, true);
        Assert.assertEquals(32000,
                AudioFormatConverter.getAudioFormatBytesPerSecond(format1));
        final AudioFormat format2 = new AudioFormat(8000f, 8, 1, true, true);
        Assert.assertEquals(8000,
                AudioFormatConverter.getAudioFormatBytesPerSecond(format2));
    }

}
