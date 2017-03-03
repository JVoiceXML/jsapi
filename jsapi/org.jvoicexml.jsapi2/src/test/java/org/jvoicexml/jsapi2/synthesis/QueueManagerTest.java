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
import javax.speech.synthesis.SpeakableEvent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvoicexml.jsapi2.mock.synthesis.MockSpeakableListener;
import org.jvoicexml.jsapi2.mock.synthesis.MockSynthesizer;

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
        synthesizer = new MockSynthesizer();
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.jse.synthesis.QueueManager#appendItem(javax.speech.synthesis.Speakable, javax.speech.synthesis.SpeakableListener)}.
     * @exception Exception
     *            test failed.
     */
    @Test(timeout = 2000)
    public void testAppendItemSpeakableSpeakableListener() throws Exception {
        QueueManager manager = synthesizer.getQueueManager();
        AudioSegment segment = new AudioSegment("http://nowhere", "test");
        MockSpeakableListener listener = new MockSpeakableListener();
        manager.appendItem(segment, listener);
        QueueItem item = manager.getQueueItem();
        Assert.assertNotNull(item);
        Assert.assertEquals(segment, item.getAudioSegment());
        Assert.assertEquals(listener, item.getListener());
        listener.waitForSize(2);
        final SpeakableEvent started = listener.getEvent(0);
        Assert.assertEquals(SpeakableEvent.SPEAKABLE_STARTED, started.getId());
        Assert.assertEquals(segment.getMarkupText(), started.getSource());
        final SpeakableEvent ended = listener.getEvent(1);
        Assert.assertEquals(SpeakableEvent.SPEAKABLE_ENDED, ended.getId());
        Assert.assertEquals(segment.getMarkupText(), ended.getSource());
    }

}
