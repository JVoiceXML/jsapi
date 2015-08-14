/**
 * 
 */
package org.jvoicexml.jsapi2.protocols.capture;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link CaptureURLConnection}.
 * @author Dirk Schnelle-Walka
 *
 */
public final class CaptureURLConnectionTest {
    /**
     * Set up the test environment.
     */
    @Before
    public void setUp() {
        System.setProperty("java.protocol.handler.pkgs",
            "org.jvoicexml.jsapi2.protocols");
    }

    /**
     * Test method for {@link net.sourceforge.gjtapi.protocols.PlaybackURLConnection#getInputStream()}.
     * @exception Exception
     *            test failed.
     */
    @Test
    public void testGetInputStream() throws Exception {
        final URL url =
            new URL("capture://audio?rate=16000&bits=16&channels=2&endian=big&encoding=pcm&signed=true");
        final CaptureURLConnection connection = new CaptureURLConnection(url);
        final InputStream input = connection.getInputStream();
        byte[] buffer = new byte[1024];
        Assert.assertEquals(buffer.length, input.read(buffer));
    }

    /**
     * Test method for {@link net.sourceforge.gjtapi.protocols.PlaybackURLConnection#getOutputStream()}.
     * @exception Exception
     *            test failed.
     */
    @Test(expected = IOException.class)
    public void testGetOutputStream() throws Exception {
        final URL url =
            new URL("playback://audio?rate=8000&channels=1&encoding=pcm");
        final CaptureURLConnection connection = new CaptureURLConnection(url);
        connection.connect();
        final OutputStream output = connection.getOutputStream();
    }

}
