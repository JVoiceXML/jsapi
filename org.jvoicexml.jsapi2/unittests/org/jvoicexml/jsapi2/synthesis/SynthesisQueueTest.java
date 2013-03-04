/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 296 $
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

package org.jvoicexml.jsapi2.synthesis;

import javax.speech.AudioSegment;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jvoicexml.jsapi2.mock.synthesis.MockSynthesizer;

/**
 * Test cases for {@link SynthesisQueue}.
 * @author Dirk Schnelle-Walka
 *
 */
public class SynthesisQueueTest extends TestCase {
    /** The test object. */
    private SynthesisQueue queue;

    /**
     * Set up the test environment.
     * @exception Exception
     *            error setting up the test environment
     */
    protected void setUp() throws Exception {
        super.setUp();
        final BaseSynthesizer synthesizer = new MockSynthesizer();
        final QueueManager manager = new QueueManager(synthesizer);
        queue = manager.getSynthesisQueue();
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.synthesis.SynthesisQueue#getNextQueueItem()}.
     */
    public void testGetNextQueueItem() {
        final AudioSegment segment1 =
                new AudioSegment("http://localhost", "test"); 
        final AudioSegment segment2 =
                new AudioSegment("http://foreignhost", "test2"); 
        final int firstId = queue.appendItem(segment1, null);
        final QueueItem firstItem = queue.getNextQueueItem();
        Assert.assertEquals(firstId, firstItem.getId());
        Assert.assertEquals(segment1, firstItem.getAudioSegment());
        queue.removeQueueItem(firstItem);
        final int secondId = queue.appendItem(segment2, null);
        final QueueItem secondItem = queue.getNextQueueItem();
        Assert.assertEquals(secondId, secondItem.getId());
        Assert.assertEquals(segment2, secondItem.getAudioSegment());
        queue.removeQueueItem(secondItem);
    }

    /**
     * Test method for {@link SynthesisQueue#getQueueItem(int)}.
     */
    public void testGetQueueItem() {
        final AudioSegment segment1 =
                new AudioSegment("http://localhost", "test"); 
        final AudioSegment segment2 =
                new AudioSegment("http://foreignhost", "test2"); 
        final int firstId = queue.appendItem(segment1, null);
        final int secondId = queue.appendItem(segment2, null);
        final QueueItem item1 = queue.getQueueItem(firstId);
        Assert.assertEquals(segment1, item1.getAudioSegment());
        final QueueItem item2 = queue.getQueueItem(secondId);
        Assert.assertEquals(segment2, item2.getAudioSegment());
        final QueueItem item3 = queue.getQueueItem(-1);
        Assert.assertNull(item3);
    }

    /**
     * Test method for {@link SynthesisQueue#isQueueEmpty()}.
     */
    public void testIsQueueEmpty() {
        Assert.assertTrue(queue.isQueueEmpty());
        final AudioSegment segment1 =
                new AudioSegment("http://localhost", "test");
        final int id = queue.appendItem(segment1, null);
        Assert.assertFalse(queue.isQueueEmpty());
        final QueueItem item = queue.getQueueItem(id);
        queue.removeQueueItem(item);
        Assert.assertTrue(queue.isQueueEmpty());
    }

    /**
     * Test method for {@link SynthesisQueue#cancelFirstItem()},
     */
    public void testCancelFirstItem() {
        Assert.assertTrue(queue.isQueueEmpty());
        final AudioSegment segment1 =
                new AudioSegment("http://localhost", "test"); 
        final AudioSegment segment2 =
                new AudioSegment("http://foreignhost", "test2"); 
        queue.appendItem(segment1, null);
        final int secondId = queue.appendItem(segment2, null);
        Assert.assertFalse(queue.isQueueEmpty());
        Assert.assertTrue(queue.cancelFirstItem());
        final QueueItem secondItem = queue.getNextQueueItem();
        Assert.assertEquals(secondId, secondItem.getId());
        Assert.assertTrue(queue.cancelFirstItem());
        Assert.assertTrue(queue.isQueueEmpty());
        Assert.assertFalse(queue.cancelFirstItem());
    }

    /**
     * Test method for {@link SynthesisQueue#cancelItem(int)},
     */
    public void testCancelItem() {
        Assert.assertTrue(queue.isQueueEmpty());
        final AudioSegment segment1 =
                new AudioSegment("http://localhost", "test"); 
        final AudioSegment segment2 =
                new AudioSegment("http://foreignhost", "test2"); 
        final int firstId = queue.appendItem(segment1, null);
        final int secondId = queue.appendItem(segment2, null);
        Assert.assertFalse(queue.isQueueEmpty());
        Assert.assertTrue(queue.cancelItem(secondId));
        final QueueItem firstItem = queue.getNextQueueItem();
        Assert.assertEquals(firstId, firstItem.getId());
        Assert.assertTrue(queue.cancelItem(firstId));
        Assert.assertTrue(queue.isQueueEmpty());
        Assert.assertFalse(queue.cancelItem(-1));
    }
}
