/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 249 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
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

/**
 * Test case for {@link javax.speech.EngineEvent}.
 * 
 * @author Dirk Schnelle
 *
 */
public class EngineEventTest extends TestCase {
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
     * Test method for {@link javax.speech.EngineEvent#getNewEngineState()}.
     */
    public void testGetNewEngineState() {
        final Throwable problem = new Exception();
        final EngineEvent event = new EngineEvent(engine, 43, 
                EngineEvent.ENGINE_DEALLOCATED, 
                EngineEvent.ENGINE_ALLOCATING_RESOURCES, problem);
        assertEquals(EngineEvent.ENGINE_ALLOCATING_RESOURCES,
                event.getNewEngineState());
    }

    /**
     * Test method for {@link javax.speech.EngineEvent#getOldEngineState()}.
     */
    public void testGetOldEngineState() {
        final Throwable problem = new Exception();
        final EngineEvent event = new EngineEvent(engine, 43, 
                EngineEvent.ENGINE_DEALLOCATED, 
                EngineEvent.ENGINE_ALLOCATING_RESOURCES, problem);
        assertEquals(EngineEvent.ENGINE_DEALLOCATED,
                event.getOldEngineState());
    }

    /**
     * Test method for {@link javax.speech.EngineEvent#getEngineError()}.
     */
    public void testGetEngineError() {
        final Throwable problem = new Exception();
        final EngineEvent event = new EngineEvent(engine, 43, 
                EngineEvent.ENGINE_DEALLOCATED, 
                EngineEvent.ENGINE_ALLOCATING_RESOURCES, problem);
        assertNull(event.getEngineError());

        final EngineEvent event2 = new EngineEvent(engine, 
                EngineEvent.ENGINE_ERROR, 
                EngineEvent.ENGINE_DEALLOCATED, 
                EngineEvent.ENGINE_ALLOCATING_RESOURCES, problem);
        assertEquals(problem, event2.getEngineError());
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#paramString()}.
     */
    public void testParamString() {
        final EngineEvent event = new EngineEvent(engine, 43, 
                EngineEvent.ENGINE_DEALLOCATED, 
                EngineEvent.ENGINE_ALLOCATING_RESOURCES, null);
        final String str = event.paramString();
        assertTrue(str.indexOf("43") >= 0);
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#toString()}.
     */
    public void testToString() {
        final EngineEvent event = new EngineEvent(engine, 44, 
                EngineEvent.ENGINE_DEALLOCATED, 
                EngineEvent.ENGINE_ALLOCATING_RESOURCES, null);
        final String str = event.toString();
        assertTrue(str.indexOf("44") >= 0);
    }

}
