/**
 * 
 */
package org.jvoicexml.jsapi2.synthesis;

import javax.speech.AudioSegment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvoicexml.jsapi2.mock.synthesis.MockSynthesizer;

/**
 * Test cases for {@link PlayQueue}.
 * @author Dirk Schnelle-Walka
 *
 */
public class PlayQueueTest {
    /** The test object. */
    private PlayQueue queue;

    /**
     * Set up the test environment.
     * @exception Exception
     *            error setting up the test environment
     */
    @Before
    public void setUp() throws Exception {
        final BaseSynthesizer synthesizer = new MockSynthesizer();
        final QueueManager manager = new QueueManager(synthesizer);
        queue = manager.getPlayQueue();
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.synthesis.PlayQueue#getNextQueueItem()}.
     */
    @Test
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
    @Test
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
