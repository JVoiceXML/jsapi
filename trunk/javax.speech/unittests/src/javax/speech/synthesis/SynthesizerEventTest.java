/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package javax.speech.synthesis;

import javax.speech.test.synthesis.TestSynthesizer;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.synthesizer.SynthesizerEvent}.
 * 
 * @author Dirk Schnelle
 * 
 */
public class SynthesizerEventTest extends TestCase {
    /** The test synthesizer. */
    private Synthesizer synthesizer;

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();

        synthesizer = new TestSynthesizer();
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SynthesizerEvent#isTopOfQueueChanged()}.
     */
    public void testIsTopOfQueueChanged() {
        final Throwable problem = new Exception();
        boolean topOfQueueChanged = false;
        final SynthesizerEvent event = new SynthesizerEvent(synthesizer, 42,
                SynthesizerEvent.QUEUE_EMPTIED,
                SynthesizerEvent.SYNTHESIZER_BUFFER_READY, problem,
                topOfQueueChanged);
        assertFalse(event.isTopOfQueueChanged());

        final Throwable problem2 = new Exception();
        boolean topOfQueueChanged2 = false;
        final SynthesizerEvent event2 = new SynthesizerEvent(synthesizer,
                SynthesizerEvent.QUEUE_UPDATED, SynthesizerEvent.QUEUE_EMPTIED,
                SynthesizerEvent.SYNTHESIZER_BUFFER_READY, problem2,
                topOfQueueChanged2);
        assertFalse(event2.isTopOfQueueChanged());

        final Throwable problem3 = new Exception();
        boolean topOfQueueChanged3 = true;
        final SynthesizerEvent event3 = new SynthesizerEvent(synthesizer,
                SynthesizerEvent.QUEUE_UPDATED, SynthesizerEvent.QUEUE_EMPTIED,
                SynthesizerEvent.SYNTHESIZER_BUFFER_READY, problem3,
                topOfQueueChanged3);
        assertTrue(event3.isTopOfQueueChanged());
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#paramString()}.
     */
    public void testParamString() {
        final Throwable problem = new Exception();
        boolean topOfQueueChanged = false;
        final SynthesizerEvent event = new SynthesizerEvent(synthesizer, 42,
                SynthesizerEvent.QUEUE_EMPTIED,
                SynthesizerEvent.SYNTHESIZER_BUFFER_READY, problem,
                topOfQueueChanged);
        final String str = event.paramString();
        assertTrue(str.indexOf("42") >= 0);
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#toString()}.
     */
    public void testToString() {
        final Throwable problem = new Exception();
        boolean topOfQueueChanged = false;
        final SynthesizerEvent event = new SynthesizerEvent(synthesizer, 42,
                SynthesizerEvent.QUEUE_EMPTIED,
                SynthesizerEvent.SYNTHESIZER_BUFFER_READY, problem,
                topOfQueueChanged);
        final String str = event.toString();
        assertTrue(str.indexOf("42") >= 0);
    }

}
