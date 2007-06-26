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

package javax.speech.recognition;

import java.util.Locale;

import javax.speech.EngineMode;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RecognizerMode}.
 * 
 * @author Dirk Schnelle
 */
public class RecognizerModeTest extends TestCase {
    /**
     * Test method for
     * {@link javax.speech.recognition.RecognizerMode#getMarkupSupport()}.
     */
    public void testGetMarkupSupport() {
        final RecognizerMode mode1 = new RecognizerMode();
        assertNull(mode1.getMarkupSupport());

        final RecognizerMode mode2 = new RecognizerMode(Locale.US);
        assertNull(mode2.getMarkupSupport());

        final String engineName1 = "name1";
        final String modeName1 = "mode1";
        final Boolean running1 = Boolean.TRUE;
        final Boolean supportsLetterToSound1 = Boolean.TRUE;
        final Boolean markupSupport1 = Boolean.TRUE;
        final Integer vocabSupport1 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales1 = new Locale[] { Locale.US, Locale.GERMAN };
        final SpeakerProfile[] profiles1 = new SpeakerProfile[] { new SpeakerProfile() };
        final RecognizerMode mode3 = new RecognizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1,
                vocabSupport1, locales1, profiles1);
        assertEquals(EngineMode.FULL, mode3.getMarkupSupport());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RecognizerMode#match(javax.speech.EngineMode)}.
     */
    public void testMatch() {
        final RecognizerMode mode1 = new RecognizerMode();
        final RecognizerMode mode2 = new RecognizerMode();
        assertTrue(mode1.match(mode2));

        final RecognizerMode mode3 = new RecognizerMode(Locale.US);
        assertFalse(mode1.match(mode3));

        final RecognizerMode mode4 = new RecognizerMode(Locale.US);
        assertTrue(mode3.match(mode4));

        final RecognizerMode mode5 = new RecognizerMode(Locale.GERMAN);
        assertFalse(mode3.match(mode5));

        final String engineName1 = "name1";
        final String modeName1 = "mode1";
        final Boolean running1 = Boolean.TRUE;
        final Boolean supportsLetterToSound1 = Boolean.TRUE;
        final Boolean markupSupport1 = Boolean.TRUE;
        final Integer vocabSupport1 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales1 = new Locale[] { Locale.US, Locale.GERMAN };
        final SpeakerProfile[] profiles1 = new SpeakerProfile[] { new SpeakerProfile() };
        final RecognizerMode mode6 = new RecognizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1,
                vocabSupport1, locales1, profiles1);
        assertFalse(mode3.match(mode6));
        assertTrue(mode6.match(mode1));
        assertTrue(mode6.match(mode3));

        final String engineName2 = "name1";
        final String modeName2 = "mode1";
        final Boolean running2 = Boolean.TRUE;
        final Boolean supportsLetterToSound2 = Boolean.TRUE;
        final Boolean markupSupport2 = Boolean.TRUE;
        final Integer vocabSupport2 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales2 = new Locale[] { Locale.US, Locale.GERMAN };
        final SpeakerProfile[] profiles2 = new SpeakerProfile[] { new SpeakerProfile() };
        final RecognizerMode mode7 = new RecognizerMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2,
                vocabSupport2, locales2, profiles2);
        assertTrue(mode6.match(mode7));
        assertTrue(mode7.match(mode6));

        final String engineName3 = "name1";
        final String modeName3 = "mode1";
        final Boolean running3 = Boolean.TRUE;
        final Boolean supportsLetterToSound3 = Boolean.TRUE;
        final Boolean markupSupport3 = Boolean.TRUE;
        final Integer vocabSupport3 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales3 = new Locale[] { Locale.US };
        final SpeakerProfile[] profiles3 = new SpeakerProfile[] {
                new SpeakerProfile(), new SpeakerProfile() };
        final RecognizerMode mode8 = new RecognizerMode(engineName3, modeName3,
                running3, supportsLetterToSound3, markupSupport3,
                vocabSupport3, locales3, profiles3);
        assertTrue(mode6.match(mode8));
        assertTrue(mode8.match(mode6));
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RecognizerMode#equals(java.lang.Object)}.
     */
    public void testEqualsObject() {
        final RecognizerMode mode1 = new RecognizerMode();
        assertFalse(mode1.equals("test"));
        assertTrue(mode1.equals(mode1));

        final EngineMode engineMode = new EngineMode();
        assertTrue(mode1.match(engineMode));

        final RecognizerMode mode2 = new RecognizerMode();
        assertTrue(mode1.equals(mode2));

        final RecognizerMode mode3 = new RecognizerMode(Locale.US);
        assertFalse(mode1.equals(mode3));

        final RecognizerMode mode4 = new RecognizerMode(Locale.US);
        assertTrue(mode3.equals(mode4));

        final RecognizerMode mode5 = new RecognizerMode(Locale.GERMAN);
        assertFalse(mode3.equals(mode5));

        final String engineName1 = "name1";
        final String modeName1 = "mode1";
        final Boolean running1 = Boolean.TRUE;
        final Boolean supportsLetterToSound1 = Boolean.TRUE;
        final Boolean markupSupport1 = Boolean.TRUE;
        final Integer vocabSupport1 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales1 = new Locale[] { Locale.US, Locale.GERMAN };
        final SpeakerProfile[] profiles1 = new SpeakerProfile[] { new SpeakerProfile() };
        final RecognizerMode mode6 = new RecognizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1,
                vocabSupport1, locales1, profiles1);
        assertFalse(mode3.equals(mode6));

        final String engineName2 = "name1";
        final String modeName2 = "mode1";
        final Boolean running2 = Boolean.TRUE;
        final Boolean supportsLetterToSound2 = Boolean.TRUE;
        final Boolean markupSupport2 = Boolean.TRUE;
        final Integer vocabSupport2 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales2 = new Locale[] { Locale.US, Locale.GERMAN };
        final SpeakerProfile[] profiles2 = new SpeakerProfile[] { new SpeakerProfile() };
        final RecognizerMode mode7 = new RecognizerMode(engineName2, modeName2,
                running2, supportsLetterToSound2, markupSupport2,
                vocabSupport2, locales2, profiles2);
        assertTrue(mode6.equals(mode7));
        assertTrue(mode7.equals(mode6));

        final String engineName3 = "name1";
        final String modeName3 = "mode1";
        final Boolean running3 = Boolean.TRUE;
        final Boolean supportsLetterToSound3 = Boolean.TRUE;
        final Boolean markupSupport3 = Boolean.TRUE;
        final Integer vocabSupport3 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales3 = new Locale[] { Locale.US };
        final SpeakerProfile[] profiles3 = new SpeakerProfile[] {
                new SpeakerProfile(), new SpeakerProfile() };
        final RecognizerMode mode8 = new RecognizerMode(engineName3, modeName3,
                running3, supportsLetterToSound3, markupSupport3,
                vocabSupport3, locales3, profiles3);
        assertFalse(mode6.equals(mode8));
        assertFalse(mode8.equals(mode6));
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RecognizerMode#getLocales()}.
     */
    public void testGetLocales() {
        final RecognizerMode mode1 = new RecognizerMode();
        assertNull(mode1.getLocales());

        final RecognizerMode mode2 = new RecognizerMode(Locale.US);
        final Locale[] loc2 = mode2.getLocales();
        assertEquals(1, loc2.length);
        assertEquals(Locale.US, loc2[0]);

        final String engineName1 = "name1";
        final String modeName1 = "mode1";
        final Boolean running1 = Boolean.TRUE;
        final Boolean supportsLetterToSound1 = Boolean.TRUE;
        final Boolean markupSupport1 = Boolean.TRUE;
        final Integer vocabSupport1 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales1 = new Locale[] { Locale.US, Locale.GERMAN };
        final SpeakerProfile[] profiles1 = new SpeakerProfile[] { new SpeakerProfile() };
        final RecognizerMode mode3 = new RecognizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1,
                vocabSupport1, locales1, profiles1);
        final Locale[] loc3 = mode3.getLocales();
        assertEquals(2, loc3.length);
        assertEquals(Locale.US, loc3[0]);
        assertEquals(Locale.GERMAN, loc3[1]);
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RecognizerMode#getSpeakerProfiles()}.
     */
    public void testGetSpeakerProfiles() {
        final RecognizerMode mode1 = new RecognizerMode();
        assertNull(mode1.getSpeakerProfiles());

        final RecognizerMode mode2 = new RecognizerMode(Locale.US);
        assertNull(mode2.getSpeakerProfiles());

        final String engineName1 = "name1";
        final String modeName1 = "mode1";
        final Boolean running1 = Boolean.TRUE;
        final Boolean supportsLetterToSound1 = Boolean.TRUE;
        final Boolean markupSupport1 = Boolean.TRUE;
        final Integer vocabSupport1 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales1 = new Locale[] { Locale.US, Locale.GERMAN };
        final SpeakerProfile[] profiles1 = new SpeakerProfile[] { new SpeakerProfile() };
        final RecognizerMode mode3 = new RecognizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1,
                vocabSupport1, locales1, profiles1);
        final SpeakerProfile[] prof3 = mode3.getSpeakerProfiles();
        assertEquals(1, prof3.length);
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RecognizerMode#getVocabSupport()}.
     */
    public void testGetVocabSupport() {
        final RecognizerMode mode1 = new RecognizerMode();
        assertNull(mode1.getVocabSupport());

        final RecognizerMode mode2 = new RecognizerMode(Locale.US);
        assertNull(mode2.getVocabSupport());

        final String engineName1 = "name1";
        final String modeName1 = "mode1";
        final Boolean running1 = Boolean.TRUE;
        final Boolean supportsLetterToSound1 = Boolean.TRUE;
        final Boolean markupSupport1 = Boolean.TRUE;
        final Integer vocabSupport1 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales1 = new Locale[] { Locale.US, Locale.GERMAN };
        final SpeakerProfile[] profiles1 = new SpeakerProfile[] { new SpeakerProfile() };
        final RecognizerMode mode3 = new RecognizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1,
                vocabSupport1, locales1, profiles1);
        assertEquals(RecognizerMode.SMALL_SIZE, mode3.getVocabSupport());
    }

    /**
     * Test method for {@link javax.speech.EngineMode#hashCode()}.
     */
    public void testHashCode() {
        final RecognizerMode mode1 = new RecognizerMode();
        final RecognizerMode mode2 = new RecognizerMode();
        assertTrue(mode1.hashCode() != mode2.hashCode());
    }

    /**
     * Test method for {@link javax.speech.EngineMode#toString()}.
     */
    public void testToString() {
        final RecognizerMode mode1 = new RecognizerMode();
        final String str1 = mode1.toString();
        assertNotNull(str1);

        final RecognizerMode mode2 = new RecognizerMode(Locale.US);
        final String str2 = mode2.toString();
        assertNotNull(str2);
        assertTrue(str2.indexOf(Locale.US.toString()) > 0);

        final String engineName1 = "name1";
        final String modeName1 = "mode1";
        final Boolean running1 = Boolean.TRUE;
        final Boolean supportsLetterToSound1 = Boolean.TRUE;
        final Boolean markupSupport1 = Boolean.TRUE;
        final Integer vocabSupport1 = RecognizerMode.SMALL_SIZE;
        final Locale[] locales1 = new Locale[] { Locale.US, Locale.GERMAN };
        final SpeakerProfile[] profiles1 = new SpeakerProfile[] { new SpeakerProfile() };
        final RecognizerMode mode3 = new RecognizerMode(engineName1, modeName1,
                running1, supportsLetterToSound1, markupSupport1,
                vocabSupport1, locales1, profiles1);
        final String str3 = mode3.toString();
        assertNotNull(str3);
        assertTrue(str3.indexOf(engineName1) > 0);
        assertTrue(str3.indexOf(modeName1) > 0);
        assertTrue(str3.indexOf(Locale.US.toString()) > 0);
        assertTrue(str3.indexOf(Locale.GERMAN.toString()) > 0);
    }
}
