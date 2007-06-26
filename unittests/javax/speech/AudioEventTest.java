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

package javax.speech;

import javax.speech.test.TestEngine;

import junit.framework.TestCase;

public class AudioEventTest extends TestCase {
    /** The test engine. */
    private Engine engine;
    
    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        engine = new TestEngine();
    }

    /**
     * Test method for {@link javax.speech.AudioEvent#paramString()}.
     */
    public void testParamString() {
        AudioEvent event = new AudioEvent(engine, 43);
        String str = event.paramString();
        assertTrue(str.indexOf("43") >= 0);

        AudioEvent event2 = new AudioEvent(engine, 44, 45);
        String str2 = event2.paramString();
        assertTrue(str2.indexOf("44") >= 0);
    }

    /**
     * Test method for {@link javax.speech.AudioEvent#toString()}.
     */
    public void testToString() {
        AudioEvent event = new AudioEvent(engine, 46);
        String str = event.toString();
        
        assertTrue(str.indexOf("46") >= 0);

        AudioEvent event2 = new AudioEvent(engine, 47, 48);
        String str2 = event2.paramString();
        assertTrue(str2.indexOf("47") >= 0);
    }

    public void testGetAudioLevel() {
        AudioEvent event = new AudioEvent(engine, 46);
        assertEquals(AudioEvent.AUDIO_LEVEL_MIN, event.getAudioLevel());

        AudioEvent eventMin = new AudioEvent(engine, 46, 
                AudioEvent.AUDIO_LEVEL_MIN);
        assertEquals(AudioEvent.AUDIO_LEVEL_MIN, eventMin.getAudioLevel());

        AudioEvent eventQuiet = new AudioEvent(engine, 46, 
                AudioEvent.AUDIO_LEVEL_QUIET);
        assertEquals(AudioEvent.AUDIO_LEVEL_QUIET, eventQuiet.getAudioLevel());

        AudioEvent eventLoud = new AudioEvent(engine, 46, 
                AudioEvent.AUDIO_LEVEL_LOUD);
        assertEquals(AudioEvent.AUDIO_LEVEL_LOUD, eventLoud.getAudioLevel());

        AudioEvent eventMax = new AudioEvent(engine, 46, 
                AudioEvent.AUDIO_LEVEL_MAX);
        assertEquals(AudioEvent.AUDIO_LEVEL_MAX, eventMax.getAudioLevel());
    }
}
