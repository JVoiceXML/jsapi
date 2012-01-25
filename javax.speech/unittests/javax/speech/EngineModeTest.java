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

import javax.speech.synthesis.SynthesizerMode;
import javax.speech.test.DummyEngineMode;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.EngineMode}.
 * 
 * @author Dirk Schnelle
 */
public class EngineModeTest extends TestCase {

    /**
     * Test method for {@link javax.speech.EngineMode#hashCode()}.
     */
    public void testHashCode() {
        final EngineMode mode1 = new DummyEngineMode();
        final EngineMode mode2 = new DummyEngineMode();
        assertTrue(mode1.hashCode() != mode2.hashCode());
    }

    /**
     * Test method for {@link javax.speech.EngineMode#getEngineName()}.
     */
    public void testGetEngineName() {
        final EngineMode mode1 = new DummyEngineMode();
        assertNull(mode1.getEngineName());

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.TRUE;
        Boolean markupSupport1 = Boolean.TRUE;
        final EngineMode mode2 = new DummyEngineMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1);
        assertEquals(engineName1, mode2.getEngineName());

        String engineName2 = null;
        String modeName2 = "mode2";
        Boolean running2 = Boolean.TRUE;
        Boolean supportsLetterToSound2 = Boolean.TRUE;
        Boolean markupSupport2 = Boolean.TRUE;
        final EngineMode mode3 = new DummyEngineMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2);
        assertNull(mode3.getEngineName());
    }

    /**
     * Test method for {@link javax.speech.EngineMode#getSupportsMarkup()}.
     */
    public void testGetSupportsMarkup() {
        final EngineMode mode1 = new DummyEngineMode();
        assertNull(mode1.getSupportsMarkup());

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.TRUE;
        Boolean markupSupport1 = Boolean.TRUE;
        final EngineMode mode2 = new DummyEngineMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1);
        assertEquals(Boolean.TRUE, mode2.getSupportsMarkup());

        String engineName2 = "name2";
        String modeName2 = "mode2";
        Boolean running2 = Boolean.TRUE;
        Boolean supportsLetterToSound2 = Boolean.TRUE;
        Boolean markupSupport2 = Boolean.FALSE;
        final EngineMode mode3 = new DummyEngineMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2);
        assertEquals(Boolean.FALSE, mode3.getSupportsMarkup());

        String engineName3 = "name3";
        String modeName3 = "mode3";
        Boolean running3 = Boolean.TRUE;
        Boolean supportsLetterToSound3 = Boolean.TRUE;
        Boolean markupSupport3 = null;
        final EngineMode mode4 = new DummyEngineMode(engineName3, modeName3,
                running3, supportsLetterToSound3, markupSupport3);
        assertNull(mode4.getSupportsMarkup());
    }

    /**
     * Test method for {@link javax.speech.EngineMode#getModeName()}.
     */
    public void testGetModeName() {
        final EngineMode mode1 = new DummyEngineMode();
        assertNull(mode1.getModeName());

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.TRUE;
        Boolean markupSupport1 = Boolean.TRUE;
        final EngineMode mode2 = new DummyEngineMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1);
        assertEquals(modeName1, mode2.getModeName());

        String engineName2 = "name2";
        String modeName2 = null;
        Boolean running2 = Boolean.TRUE;
        Boolean supportsLetterToSound2 = Boolean.TRUE;
        Boolean markupSupport2 = Boolean.TRUE;
        final EngineMode mode3 = new DummyEngineMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2);
        assertNull(mode3.getModeName());
    }

    /**
     * Test method for {@link javax.speech.EngineMode#getRunning()}.
     */
    public void testGetRunning() {
        final EngineMode mode1 = new DummyEngineMode();
        assertNull(mode1.getRunning());

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.TRUE;
        Boolean markupSupport1 = Boolean.TRUE;
        final EngineMode mode2 = new DummyEngineMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1);
        assertEquals(Boolean.TRUE, mode2.getRunning());

        String engineName2 = "name2";
        String modeName2 = "mode2";
        Boolean running2 = Boolean.FALSE;
        Boolean supportsLetterToSound2 = Boolean.TRUE;
        Boolean markupSupport2 = Boolean.TRUE;
        final EngineMode mode3 = new DummyEngineMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2);
        assertEquals(Boolean.FALSE, mode3.getRunning());

        String engineName3 = "name3";
        String modeName3 = "mode3";
        Boolean running3 = null;
        Boolean supportsLetterToSound3 = Boolean.TRUE;
        Boolean markupSupport3 = null;
        final EngineMode mode4 = new DummyEngineMode(engineName3, modeName3,
                running3, supportsLetterToSound3, markupSupport3);
        assertNull(mode4.getRunning());
    }

    /**
     * Test method for
     * {@link javax.speech.EngineMode#getSupportsLetterToSound()}.
     */
    public void testGetSupportsLetterToSound() {
        final EngineMode mode1 = new DummyEngineMode();
        assertNull(mode1.getSupportsLetterToSound());

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.TRUE;
        Boolean markupSupport1 = Boolean.TRUE;
        final EngineMode mode2 = new DummyEngineMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1);
        assertEquals(Boolean.TRUE, mode2.getSupportsLetterToSound());

        String engineName2 = "name2";
        String modeName2 = "mode2";
        Boolean running2 = Boolean.TRUE;
        Boolean supportsLetterToSound2 = Boolean.FALSE;
        Boolean markupSupport2 = Boolean.TRUE;
        final EngineMode mode3 = new DummyEngineMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2);
        assertEquals(Boolean.FALSE, mode3.getSupportsLetterToSound());

        String engineName3 = "name3";
        String modeName3 = "mode3";
        Boolean running3 = Boolean.TRUE;
        Boolean supportsLetterToSound3 = null;
        Boolean markupSupport3 = null;
        final EngineMode mode4 = new DummyEngineMode(engineName3, modeName3,
                running3, supportsLetterToSound3, markupSupport3);
        assertNull(mode4.getSupportsLetterToSound());
    }

    /**
     * Test method for
     * {@link javax.speech.EngineMode#match(javax.speech.EngineMode)}.
     */
    public void testMatch() {
        final EngineMode mode1 = new DummyEngineMode();
        assertTrue(mode1.match((EngineMode) null));

        final EngineMode mode2 = new DummyEngineMode();
        assertTrue(mode1.match(mode2));

        final SynthesizerMode synthesizerMode = new SynthesizerMode();
        assertTrue(mode1.match(synthesizerMode));

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.TRUE;
        Boolean markupSupport1 = Boolean.TRUE;
        final EngineMode mode3 = new DummyEngineMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1);
        assertTrue(mode3.match(mode1));
        assertFalse(mode1.match(mode3));

        String engineName2 = "name1";
        String modeName2 = "mode1";
        Boolean running2 = Boolean.TRUE;
        Boolean supportsLetterToSound2 = Boolean.TRUE;
        Boolean markupSupport2 = Boolean.TRUE;
        final EngineMode mode4 = new DummyEngineMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2);
        assertTrue(mode3.match(mode4));

        String engineName3 = "name2";
        String modeName3 = "mode1";
        Boolean running3 = Boolean.TRUE;
        Boolean supportsLetterToSound3 = Boolean.TRUE;
        Boolean markupSupport3 = Boolean.TRUE;
        final EngineMode mode5 = new DummyEngineMode(engineName3, modeName3,
                running3, supportsLetterToSound3, markupSupport3);
        assertFalse(mode3.match(mode5));
    }

    /**
     * Test method for {@link javax.speech.EngineMode#equals(java.lang.Object)}.
     */
    public void testEqualsObject() {
        final EngineMode mode1 = new DummyEngineMode();
        assertFalse(mode1.equals("test"));

        final EngineMode mode2 = new DummyEngineMode();
        assertTrue(mode1.equals(mode2));

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.TRUE;
        Boolean markupSupport1 = Boolean.TRUE;
        final EngineMode mode3 = new DummyEngineMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1);
        assertFalse(mode1.equals(mode3));

        String engineName2 = "name1";
        String modeName2 = "mode1";
        Boolean running2 = Boolean.TRUE;
        Boolean supportsLetterToSound2 = Boolean.TRUE;
        Boolean markupSupport2 = Boolean.TRUE;
        final EngineMode mode4 = new DummyEngineMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2);
        assertTrue(mode3.equals(mode4));

        String engineName3 = "name2";
        String modeName3 = "mode1";
        Boolean running3 = Boolean.TRUE;
        Boolean supportsLetterToSound3 = Boolean.TRUE;
        Boolean markupSupport3 = Boolean.TRUE;
        final EngineMode mode5 = new DummyEngineMode(engineName3, modeName3,
                running3, supportsLetterToSound3, markupSupport3);
        assertFalse(mode3.equals(mode5));
    }

    /**
     * Test method for {@link javax.speech.EngineMode#toString()}.
     */
    public void testToString() {
        final EngineMode mode1 = new DummyEngineMode();
        assertNotNull(mode1.toString());

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.TRUE;
        Boolean markupSupport1 = Boolean.TRUE;
        final EngineMode mode2 = new DummyEngineMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1);
        assertEquals(Boolean.TRUE, mode2.getSupportsLetterToSound());
        String str2 = mode2.toString();
        assertNotNull(str2);
        assertTrue(str2.indexOf(engineName1) > 0);
        assertTrue(str2.indexOf(modeName1) > 0);
    }

}
