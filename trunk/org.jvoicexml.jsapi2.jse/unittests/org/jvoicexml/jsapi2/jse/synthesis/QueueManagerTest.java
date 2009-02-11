/**
 * 
 */
package org.jvoicexml.jsapi2.jse.synthesis;

import javax.speech.AudioSegment;
import javax.speech.synthesis.SpeakableListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.jvoicexml.jsapi2.jse.test.synthesis.DummySpeakableListener;
import org.jvoicexml.jsapi2.jse.test.synthesis.DummySynthesizer;

/**
 * Test cases for {@link QueueManager}.
 * @author Dirk Schnelle-Walka
 *
 */
public final class QueueManagerTest {
    /** Synthesizer. */
    private BaseSynthesizer synthesizer;

    /**
     * Set up the test environment.
     */
    @Before
    public void setUp() {
        synthesizer = new DummySynthesizer();
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.jse.synthesis.QueueManager#appendItem(javax.speech.synthesis.Speakable, javax.speech.synthesis.SpeakableListener)}.
     * @exception Exception
     *            test failed.
     */
    @Test
    public void testAppendItemSpeakableSpeakableListener() throws Exception {
        QueueManager manager = synthesizer.getQueueManager();
        AudioSegment segment = new AudioSegment(null, "test");
        SpeakableListener listener = new DummySpeakableListener();
        manager.appendItem(segment, listener);
        QueueItem item = manager.getQueueItem();
        Assert.assertNotNull(item);
        Assert.assertEquals(segment, item.getAudioSegment());
        Assert.assertEquals(listener, item.getListener());
        Thread.sleep(10000);
    }

}
