/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
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

package javax.speech;

import javax.speech.test.DummySpeechEvent;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.SpeechEvent}.
 * 
 * @author Dirk Schnelle
 * 
 */
public class SpeechEventTest extends TestCase {

    /**
     * Test method for {@link javax.speech.SpeechEvent#getId()}.
     */
    public void testGetId() {
        SpeechEvent event = new DummySpeechEvent(new Object(), 42);
        assertEquals(42, event.getId());
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#paramString()}.
     */
    public void testParamString() {
        SpeechEvent event = new DummySpeechEvent(new Object(), 43);
        String str = event.paramString();

        assertTrue(str.indexOf("43") >= 0);
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#toString()}.
     */
    public void testToString() {
        SpeechEvent event = new DummySpeechEvent(new Object(), 44);
        String str = event.toString();

        assertTrue(str.indexOf("44") >= 0);
    }
}
