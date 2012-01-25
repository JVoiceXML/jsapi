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

package javax.speech.synthesis;

import javax.speech.EngineMode;
import javax.speech.SpeechLocale;
import javax.speech.test.DummyEngineMode;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.synthesis.SynthesizerMode}.
 * 
 * @author Dirk Schnelle
 */
public class SynthesizerModeTest extends TestCase {

    /**
     * Test method for {@link javax.speech.synthesis.SynthesizerMode#hashCode()}.
     */
    public void testHashCode() {
        SynthesizerMode mode1 = new SynthesizerMode();
        SynthesizerMode mode2 = new SynthesizerMode();
        assertTrue(mode1.hashCode() != mode2.hashCode());
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SynthesizerMode#getMarkupSupport()}.
     */
    public void testGetMarkupSupport() {
        SynthesizerMode mode1 = new SynthesizerMode();
        assertNull(mode1.getMarkupSupport());

        SynthesizerMode mode2 = new SynthesizerMode(SpeechLocale.US);
        assertNull(mode2.getMarkupSupport());

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.FALSE;
        Boolean markupSupport1 = Boolean.TRUE;
        Voice[] voices1 = new Voice[] { new Voice(), new Voice() };
        SynthesizerMode mode3 = new SynthesizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1, voices1);
        assertEquals(Boolean.TRUE, mode3.getMarkupSupport());

        String engineName2 = "name2";
        String modeName2 = "mode2";
        Boolean running2 = Boolean.TRUE;
        Boolean supportsLetterToSound2 = Boolean.FALSE;
        Boolean markupSupport2 = Boolean.FALSE;
        Voice[] voices2 = new Voice[] { new Voice(), new Voice() };
        SynthesizerMode mode4 = new SynthesizerMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2, voices2);
        assertEquals(Boolean.FALSE, mode4.getMarkupSupport());

        String engineName3 = "name3";
        String modeName3 = "mode3";
        Boolean running3 = Boolean.TRUE;
        Boolean supportsLetterToSound3 = Boolean.FALSE;
        Boolean markupSupport3 = null;
        Voice[] voices3 = new Voice[] { new Voice(), new Voice() };
        SynthesizerMode mode5 = new SynthesizerMode(engineName3, modeName3,
                running3, supportsLetterToSound3, markupSupport3, voices3);
        assertNull(mode5.getMarkupSupport());
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SynthesizerMode#match(javax.speech.EngineMode)}.
     */
    public void testMatch() {
        final SynthesizerMode mode1 = new SynthesizerMode();
        assertTrue(mode1.match((SynthesizerMode) null));

        final SynthesizerMode mode2 = new SynthesizerMode();
        assertTrue(mode1.match(mode2));

        final EngineMode engineMode = new DummyEngineMode();
        assertTrue(mode1.match(engineMode));

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.FALSE;
        Boolean markupSupport1 = Boolean.TRUE;
        Voice[] voices1 = new Voice[] { new Voice(), new Voice() };
        SynthesizerMode mode3 = new SynthesizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1, voices1);
        assertFalse(mode1.match(mode3));
        assertTrue(mode3.match(mode1));

        String engineName2 = "name1";
        String modeName2 = "mode1";
        Boolean running2 = Boolean.TRUE;
        Boolean supportsLetterToSound2 = Boolean.FALSE;
        Boolean markupSupport2 = Boolean.TRUE;
        Voice[] voices2 = new Voice[] { new Voice(), new Voice() };
        SynthesizerMode mode4 = new SynthesizerMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2, voices2);
        assertTrue(mode3.match(mode4));
        assertTrue(mode4.match(mode3));
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SynthesizerMode#equals(java.lang.Object)}.
     */
    public void testEqualsObject() {
        final SynthesizerMode mode1 = new SynthesizerMode();
        assertFalse(mode1.equals("test"));

        final SynthesizerMode mode2 = new SynthesizerMode();
        assertTrue(mode1.equals(mode2));

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.FALSE;
        Boolean markupSupport1 = Boolean.TRUE;
        Voice[] voices1 = new Voice[] { new Voice(), new Voice() };
        SynthesizerMode mode3 = new SynthesizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1, voices1);
        assertFalse(mode1.equals(mode3));

        String engineName2 = "name1";
        String modeName2 = "mode1";
        Boolean running2 = Boolean.TRUE;
        Boolean supportsLetterToSound2 = Boolean.FALSE;
        Boolean markupSupport2 = Boolean.TRUE;
        Voice[] voices2 = new Voice[] { new Voice(), new Voice() };
        SynthesizerMode mode4 = new SynthesizerMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2, voices2);
        assertTrue(mode3.equals(mode4));
        assertTrue(mode4.equals(mode3));

        String engineName3 = "name1";
        String modeName3 = "mode1";
        Boolean running3 = Boolean.TRUE;
        Boolean supportsLetterToSound3 = Boolean.FALSE;
        Boolean markupSupport3 = Boolean.TRUE;
        Voice[] voices3 = new Voice[] { new Voice() };
        SynthesizerMode mode5 = new SynthesizerMode(engineName3, modeName3,
                running3, supportsLetterToSound3, markupSupport3, voices3);
        assertFalse(mode3.equals(mode5));
        assertFalse(mode5.equals(mode3));
    }

    /**
     * Test method for {@link javax.speech.synthesis.SynthesizerMode#toString()}.
     */
    public void testToString() {
        final SynthesizerMode mode1 = new SynthesizerMode();
        assertNotNull(mode1.toString());

        final SynthesizerMode mode2 = new SynthesizerMode(SpeechLocale.US);
        assertNotNull(mode2.toString());

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.FALSE;
        Boolean markupSupport1 = Boolean.TRUE;
        Voice[] voices1 = new Voice[] { new Voice(), new Voice() };
        SynthesizerMode mode3 = new SynthesizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1, voices1);
        final String str3 = mode3.toString();
        assertTrue(str3.indexOf(engineName1) > 0);
        assertTrue(str3.indexOf(modeName1) > 0);
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SynthesizerMode#getVoices()}.
     */
    public void testGetVoices() {
        SynthesizerMode mode1 = new SynthesizerMode();
        assertNull(mode1.getVoices());

        SynthesizerMode mode2 = new SynthesizerMode(SpeechLocale.US);
        Voice[] voices = mode2.getVoices();
        assertNotNull(voices);
        assertEquals(1, voices.length);
        assertEquals(SpeechLocale.US, voices[0].getSpeechLocale());

        String engineName1 = "name1";
        String modeName1 = "mode1";
        Boolean running1 = Boolean.TRUE;
        Boolean supportsLetterToSound1 = Boolean.FALSE;
        Boolean markupSupport1 = Boolean.TRUE;
        Voice[] voices1 = new Voice[] { new Voice(), new Voice() };
        SynthesizerMode mode3 = new SynthesizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1, voices1);
        voices = mode3.getVoices();
        assertNotNull(voices);
        assertEquals(2, voices.length);
    }
}
