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
package javax.speech.recognition;

import javax.speech.mock.recognition.MockRecognizer;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RecognizerEvent}.
 * 
 * @author Dirk Schnelle
 * 
 */
public class RecognizerEventTest extends TestCase {
    /** The recognizer. */
    private Recognizer recognizer;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        recognizer = new MockRecognizer();
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RecognizerEvent#getAudioPosition()}.
     */
    public void testGetAudioPosition() {
        final Throwable problem = new Exception();
        long audioPosition = RecognizerEvent.UNKNOWN_AUDIO_POSITION;
        final RecognizerEvent event = new RecognizerEvent(recognizer,
                RecognizerEvent.ENGINE_ERROR,
                RecognizerEvent.CHANGES_COMMITTED,
                RecognizerEvent.ENGINE_ALLOCATING_RESOURCES, problem,
                null, audioPosition);

        assertEquals(audioPosition, event.getAudioPosition());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RecognizerEvent#getGrammarException()}.
     */
    public void testGetGrammarException() {
        long audioPosition = RecognizerEvent.UNKNOWN_AUDIO_POSITION;
        final RecognizerEvent event = new RecognizerEvent(recognizer, 42,
                RecognizerEvent.CHANGES_COMMITTED,
                RecognizerEvent.ENGINE_ALLOCATING_RESOURCES, null,
                null, audioPosition);

        assertNull(event.getGrammarException());

        final GrammarException grammarException2 = new GrammarException();
        long audioPosition2 = RecognizerEvent.UNKNOWN_AUDIO_POSITION;
        final RecognizerEvent event2 = new RecognizerEvent(recognizer,
                RecognizerEvent.CHANGES_REJECTED,
                RecognizerEvent.CHANGES_COMMITTED,
                RecognizerEvent.ENGINE_ALLOCATING_RESOURCES, null,
                grammarException2, audioPosition2);

        assertEquals(grammarException2, event2.getGrammarException());
    }

    /**
     * Test method for {@link javax.speech.AudioEvent#paramString()}.
     */
    public void testParamString() {
        long audioPosition = RecognizerEvent.UNKNOWN_AUDIO_POSITION;
        final RecognizerEvent event = new RecognizerEvent(recognizer, 42,
                RecognizerEvent.CHANGES_COMMITTED,
                RecognizerEvent.ENGINE_ALLOCATING_RESOURCES, null,
                null, audioPosition);
        final String str = event.paramString();
        assertTrue("id not found in paramString", str.indexOf("42") >= 0);
    }

    /**
     * Test method for {@link javax.speech.AudioEvent#toString()}.
     */
    public void testToString() {
        long audioPosition = RecognizerEvent.UNKNOWN_AUDIO_POSITION;
        final RecognizerEvent event = new RecognizerEvent(recognizer, 43,
                RecognizerEvent.CHANGES_COMMITTED,
                RecognizerEvent.ENGINE_ALLOCATING_RESOURCES, null,
                null, audioPosition);
        final String str = event.toString();
        assertTrue("id not found in toString", str.indexOf("43") >= 0);

        String paramString = event.paramString();
        assertTrue("toString not longer than paramString",
                str.length() > paramString.length());
    }
}
