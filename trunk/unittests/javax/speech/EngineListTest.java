/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate: $
 * Author:  $LastChangedBy: $
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

import javax.speech.recognition.RecognizerMode;
import javax.speech.synthesis.SynthesizerMode;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.EngineList}.
 * 
 * @author Dirk Schnelle
 */
public class EngineListTest extends TestCase {
    /** The engine list to test. */
    private EngineList list;
    
    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        final EngineMode[] modes = new EngineMode[] {
                new EngineMode(),
                new SynthesizerMode(),
                new RecognizerMode()
        };
        list = new EngineList(modes);
    }

    /**
     * Test method for {@link javax.speech.EngineList#anyMatch(javax.speech.EngineMode)}.
     */
    public void testAnyMatch() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link javax.speech.EngineList#elementAt(int)}.
     */
    public void testElementAt() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link javax.speech.EngineList#elements()}.
     */
    public void testElements() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link javax.speech.EngineList#orderByMatch(javax.speech.EngineMode)}.
     */
    public void testOrderByMatch() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link javax.speech.EngineList#rejectMatch(javax.speech.EngineMode)}.
     */
    public void testRejectMatch() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link javax.speech.EngineList#removeElementAt(int)}.
     */
    public void testRemoveElementAt() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link javax.speech.EngineList#requireMatch(javax.speech.EngineMode)}.
     */
    public void testRequireMatch() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link javax.speech.EngineList#size()}.
     */
    public void testSize() {
        fail("Not yet implemented");
    }

}
