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

import javax.speech.SpeechLocale;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.synthesis.SpeakableEvent}.
 * 
 * @author Dirk Schnelle-Walka
 */
public class SpeakableEventTest extends TestCase {
    private Object source;

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();

        source = new Object();
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getAttributes()}.
     */
    public void testGetAttributes() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);
        assertEquals(0, event1.getAttributes().length);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        assertEquals(attrs, event2.getAttributes());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        assertEquals(0, event3.getAttributes().length);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        assertEquals(0, event4.getAttributes().length);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        assertEquals(0, event5.getAttributes().length);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        assertEquals(0, event6.getAttributes().length);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        assertEquals(0, event7.getAttributes().length);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        assertEquals(0, event8.getAttributes().length);
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getAudioPosition()}.
     */
    public void testGetAudioPosition() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);
        assertEquals(SpeakableEvent.UNKNOWN_AUDIO_POSITION,
                event1.getAudioPosition());

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        assertEquals(SpeakableEvent.UNKNOWN_AUDIO_POSITION,
                event2.getAudioPosition());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        assertEquals(audioPosition, event3.getAudioPosition());

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        assertEquals(SpeakableEvent.UNKNOWN_AUDIO_POSITION,
                event4.getAudioPosition());

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        assertEquals(SpeakableEvent.UNKNOWN_AUDIO_POSITION,
                event5.getAudioPosition());

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        assertEquals(SpeakableEvent.UNKNOWN_AUDIO_POSITION,
                event6.getAudioPosition());

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                17);
        assertEquals(17, event7.getAudioPosition());

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        assertEquals(SpeakableEvent.UNKNOWN_AUDIO_POSITION,
                event8.getAudioPosition());
    }

    /**
     * Test method for {@link javax.speech.synthesis.SpeakableEvent#getIndex()}.
     */
    public void testGetIndex() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);

        int index;
        assertEquals(SpeakableEvent.UNKNOWN_INDEX, event1.getIndex());

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        assertEquals(SpeakableEvent.UNKNOWN_INDEX, event2.getIndex());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        assertEquals(SpeakableEvent.UNKNOWN_INDEX, event3.getIndex());

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        assertEquals(SpeakableEvent.UNKNOWN_INDEX, event4.getIndex());

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        assertEquals(SpeakableEvent.UNKNOWN_INDEX, event5.getIndex());

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        index = event6.getIndex();
        assertEquals(1, index);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        assertEquals(SpeakableEvent.UNKNOWN_INDEX, event7.getIndex());

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        assertEquals(SpeakableEvent.UNKNOWN_INDEX, event8.getIndex());
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getNewVoice()}.
     */
    public void testGetNewVoice() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);

        Voice voice;
        assertNull(event1.getNewVoice());

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        assertNull(event2.getNewVoice());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        assertNull(event3.getNewVoice());

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        assertNull(event4.getNewVoice());

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        assertNull(event5.getNewVoice());

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        assertNull(event6.getNewVoice());

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        assertNull(event7.getNewVoice());

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        voice = event8.getNewVoice();
        assertEquals(newVoice, voice);
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getOldVoice()}.
     */
    public void testGetOldVoice() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);
        assertNull(event1.getOldVoice());

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        assertNull(event2.getOldVoice());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        assertNull(event3.getOldVoice());

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        assertNull(event4.getOldVoice());

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        assertNull(event5.getOldVoice());

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        assertNull(event6.getOldVoice());

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        assertNull(event7.getOldVoice());

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        assertEquals(oldVoice, event8.getOldVoice());
    }

    /**
     * Test method for {@link javax.speech.synthesis.SpeakableEvent#getPhones()}.
     */
    public void testGetPhones() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);

        PhoneInfo[] phoneInfos;
        Exception error = null;
        assertEquals(0, event1.getPhones().length);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        assertEquals(0, event2.getPhones().length);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        assertEquals(0, event3.getPhones().length);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        assertEquals(0, event4.getPhones().length);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        assertEquals(0, event5.getPhones().length);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        assertEquals(phones, event6.getPhones());

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        assertEquals(0, event7.getPhones().length);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        assertEquals(0, event8.getPhones().length);
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getRealizedValue()}.
     */
    public void testGetRealizedValue() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE, event1.getRealizedValue());


        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE, event2.getRealizedValue());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE, event3.getRealizedValue());

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        assertEquals(realized, event4.getRealizedValue());

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE, event5.getRealizedValue());

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE, event6.getRealizedValue());

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE, event7.getRealizedValue());

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE, event8.getRealizedValue());
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getRequestedValue()}.
     */
    public void testGetRequestedValue() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE,
                event1.getRequestedValue());

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE,
                event2.getRequestedValue());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE,
                event3.getRequestedValue());

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        assertEquals(requested, event4.getRequestedValue());

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE,
                event5.getRequestedValue());

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE,
                event6.getRequestedValue());

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE,
                event7.getRequestedValue());

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        assertEquals(SpeakableEvent.UNKNOWN_VALUE,
                event8.getRequestedValue());
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getRequestId()}.
     */
    public void testGetRequestId() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);
        assertEquals(42, event1.getRequestId());

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        assertEquals(46, event2.getRequestId());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        assertEquals(49, event3.getRequestId());

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        assertEquals(51, event4.getRequestId());

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        assertEquals(55, event5.getRequestId());

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        assertEquals(59, event6.getRequestId());

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        assertEquals(60, event7.getRequestId());

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 61, "textInfo7", oldVoice,
                newVoice);
        assertEquals(61, event8.getRequestId());
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getTextInfo()}.
     */
    public void testGetTextInfo() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);

        String textinfo;

        textinfo = event1.getTextInfo();
        assertNull(textinfo);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        textinfo = event2.getTextInfo();
        assertEquals("textInfo", textinfo);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        textinfo = event3.getTextInfo();
        assertEquals("textInfo2", textinfo);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        textinfo = event4.getTextInfo();
        assertEquals("textInfo3", textinfo);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        textinfo = event5.getTextInfo();
        assertEquals("textInfo4", textinfo);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        textinfo = event6.getTextInfo();
        assertEquals("textInfo5", textinfo);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        textinfo = event7.getTextInfo();
        assertEquals("textInfo6", textinfo);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        textinfo = event8.getTextInfo();
        assertEquals("textInfo7", textinfo);
    }

    /**
     * Test method for {@link javax.speech.synthesis.SpeakableEvent#getType()}.
     */
    public void testGetType() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);
        assertEquals(SpeakableEvent.UNKNOWN_TYPE, event1.getType());

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        assertEquals(SpeakableEvent.ELEMENT_OPEN, event2.getType());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        assertEquals(SpeakableEvent.UNKNOWN_TYPE, event3.getType());

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        assertEquals(SpeakableEvent.PROSODY_RATE, event4.getType());

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        assertEquals(SpeakableEvent.UNKNOWN_TYPE, event5.getType());

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        assertEquals(SpeakableEvent.UNKNOWN_TYPE, event6.getType());

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        assertEquals(SpeakableEvent.UNKNOWN_TYPE,
                event7.getType());

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        assertEquals(SpeakableEvent.UNKNOWN_TYPE, event8.getType());
    }


    /**
     * Test method for {@link javax.speech.SpeechEvent#paramString()}.
     */
    public void testParamString() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);
        final String str1 = event1.paramString();
        assertTrue("id not found in toString",
                str1.indexOf("TOP_OF_QUEUE") >= 0);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        final String str2 = event2.paramString();
        assertTrue("id not found in toString",
                str2.indexOf("ELEMENT_REACHED") >= 0);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        final String str3 = event3.paramString();
        assertTrue("id not found in toString",
                str3.indexOf("MARKER_REACHED") >= 0);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        final String str4 = event4.paramString();
        assertTrue("id not found in toString",
                str4.indexOf("PROSODY_UPDATED") >= 0);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        final String str5 = event5.paramString();
        assertTrue("id not found in toString",
                str5.indexOf("WORD_STARTED") >= 0);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        final String str6 = event6.paramString();
        assertTrue("id not found in toString",
                str6.indexOf("PHONEME_STARTED") >= 0);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        final String str7 = event7.paramString();
        assertTrue("id not found in toString",
                str7.indexOf("MARKER_REACHED") >= 0);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        final String str8 = event8.paramString();
        assertTrue("id not found in toString",
                str8.indexOf("VOICE_CHANGED") >= 0);
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#toString()}.
     */
    public void testToString() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);
        final String str1 = event1.toString();
        assertTrue("id not found in toString", str1.indexOf("TOP_OF_QUEUE") >= 0);
        String paramString = event1.paramString();
        assertTrue("toString not longer than paramString",
                str1.length() > paramString.length());

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        final String str2 = event2.toString();
        assertTrue("id not found in toString",
                str2.indexOf("ELEMENT_REACHED") >= 0);
        String paramString2 = event1.paramString();
        assertTrue("toString not longer than paramString",
                str2.length() > paramString2.length());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        final String str3 = event3.toString();
        assertTrue("id not found in toString",
                str3.indexOf("MARKER_REACHED") >= 0);
        String paramString3 = event3.paramString();
        assertTrue("toString not longer than paramString",
                str3.length() > paramString3.length());

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        final String str4 = event4.toString();
        assertTrue("id not found in toString",
                str4.indexOf("PROSODY_UPDATED") >= 0);
        String paramString4 = event4.paramString();
        assertTrue("toString not longer than paramString",
                str4.length() > paramString4.length());

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        final String str5 = event5.toString();
        assertTrue("id not found in toString",
                str5.indexOf("WORD_STARTED") >= 0);
        String paramString5 = event5.paramString();
        assertTrue("toString not longer than paramString",
                str5.length() > paramString5.length());

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        final String str6 = event6.toString();
        assertTrue("id not found in toString",
                str6.indexOf("PHONEME_STARTED") >= 0);
        String paramString6 = event6.paramString();
        assertTrue("toString not longer than paramString",
                str6.length() > paramString6.length());

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        final String str7 = event7.toString();
        assertTrue("id not found in toString",
                str7.indexOf("MARKER_REACHED") >= 0);
        String paramString7 = event7.paramString();
        assertTrue("toString not longer than paramString",
                str7.length() > paramString7.length());

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        final String str8 = event8.toString();
        assertTrue("id not found in toString",
                str8.indexOf("VOICE_CHANGED") >= 0);
        String paramString8 = event8.paramString();
        assertTrue("toString not longer than paramString",
                str8.length() > paramString8.length());
    }
}
