/**
 * 
 */
package org.jvoicexml.jsapi2.synthesis;

import javax.speech.AudioSegment;

import org.jvoicexml.jsapi2.test.synthesis.DummySynthesizer;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Test cases for {@link PlayQueue}.
 * @author Dirk Schnelle-Walka
 *
 */
public class PlayQueueTest extends TestCase {
    /** The test object. */
    private PlayQueue queue;

    /**
     * Set up the test environment.
     * @exception Exception
     *            error setting up the test environment
     */
    protected void setUp() throws Exception {
        super.setUp();
        final BaseSynthesizer synthesizer = new DummySynthesizer();
        final QueueManager manager = new QueueManager(synthesizer);
        queue = manager.getPlayQueue();
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.synthesis.PlayQueue#getNextQueueItem()}.
     */
    public void testGetNextQueueItem() {
        final AudioSegment segment1 =
                new AudioSegment("http://localhost", "test");
        final QueueItem item1 = new QueueItem(1, segment1, null);
        item1.setSynthesized(true);
        queue.addQueueItem(item1);
        final AudioSegment segment2 =
                new AudioSegment("http://foreignhost", "test2");
        final QueueItem item2 = new QueueItem(2, segment2, null);
        item2.setSynthesized(true);
        queue.addQueueItem(item2);
        Assert.assertEquals(item1, queue.getNextQueueItem());
        queue.cancelItemAtTopOfQueue();
        Assert.assertEquals(item2, queue.getNextQueueItem());
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.synthesis.PlayQueue#getNextQueueItem()}.
     */
    public void testGetNextQueueItemNotSynthesized() {
        final AudioSegment segment1 =
                new AudioSegment("http://localhost", "test");
        final QueueItem item1 = new QueueItem(1, segment1, null);
        queue.addQueueItem(item1);
        final Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    item1.setSynthesized(true);
                    queue.itemChanged(item1);
                } catch (InterruptedException e) {
                    Assert.fail(e.getMessage());
                }
            }
        };
        thread.start();
        final QueueItem fetchedItem = queue.getNextQueueItem();
        Assert.assertTrue(fetchedItem.isSynthesized());
        Assert.assertEquals(item1, fetchedItem);
    }
}
