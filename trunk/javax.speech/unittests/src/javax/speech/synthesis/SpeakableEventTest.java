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
 * @author Dirk Schnelle
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

        String[] attributes;
        Exception error = null;

        try {
            attributes = event1.getAttributes();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        attributes = event2.getAttributes();
        assertEquals(attrs, attributes);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        error = null;
        try {
            attributes = event3.getAttributes();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        error = null;
        try {
            attributes = event4.getAttributes();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        error = null;
        try {
            attributes = event5.getAttributes();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        error = null;
        try {
            attributes = event6.getAttributes();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        error = null;
        try {
            attributes = event7.getAttributes();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        error = null;
        try {
            attributes = event8.getAttributes();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getAudioPosition()}.
     */
    public void testGetAudioPosition() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);

        int pos;
        Exception error = null;

        try {
            pos = event1.getAudioPosition();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        try {
            pos = event2.getAudioPosition();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        error = null;
        pos = event3.getAudioPosition();
        assertEquals(audioPosition, pos);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        error = null;
        try {
            pos = event4.getAudioPosition();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        error = null;
        try {
            pos = event5.getAudioPosition();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        error = null;
        try {
            pos = event6.getAudioPosition();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        error = null;
        try {
            pos = event7.getAudioPosition();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        error = null;
        try {
            pos = event8.getAudioPosition();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);
    }

    /**
     * Test method for {@link javax.speech.synthesis.SpeakableEvent#getIndex()}.
     */
    public void testGetIndex() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);

        int index;
        Exception error = null;

        try {
            index = event1.getIndex();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        error = null;
        try {
            index = event2.getIndex();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        error = null;
        try {
            index = event3.getIndex();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        error = null;
        try {
            index = event4.getIndex();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        error = null;
        try {
            index = event5.getIndex();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        error = null;
        index = event6.getIndex();
        assertEquals(1, index);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        error = null;
        try {
            index = event7.getIndex();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        error = null;
        try {
            index = event8.getIndex();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getNewVoice()}.
     */
    public void testGetNewVoice() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);

        Voice voice;
        Exception error = null;

        try {
            voice = event1.getNewVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        error = null;
        try {
            voice = event2.getNewVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        error = null;
        try {
            voice = event3.getNewVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        error = null;
        try {
            voice = event4.getNewVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        error = null;
        try {
            voice = event5.getNewVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        error = null;
        try {
            voice = event6.getNewVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        error = null;
        try {
            voice = event7.getNewVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        error = null;
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

        Voice voice;
        Exception error = null;

        try {
            voice = event1.getOldVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        error = null;
        try {
            voice = event2.getOldVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        error = null;
        try {
            voice = event3.getOldVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        error = null;
        try {
            voice = event4.getOldVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        error = null;
        try {
            voice = event5.getOldVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        error = null;
        try {
            voice = event6.getOldVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        error = null;
        try {
            voice = event7.getNewVoice();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        error = null;
        voice = event8.getOldVoice();
        assertEquals(oldVoice, voice);
    }

    /**
     * Test method for {@link javax.speech.synthesis.SpeakableEvent#getPhones()}.
     */
    public void testGetPhones() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);

        PhoneInfo[] phoneInfos;
        Exception error = null;

        try {
            phoneInfos = event1.getPhones();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        error = null;
        try {
            phoneInfos = event2.getPhones();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        error = null;
        try {
            phoneInfos = event3.getPhones();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        error = null;
        try {
            phoneInfos = event4.getPhones();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        error = null;
        try {
            phoneInfos = event5.getPhones();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        error = null;
        phoneInfos = event6.getPhones();
        assertEquals(phones, phoneInfos);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        error = null;
        try {
            phoneInfos = event7.getPhones();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        error = null;
        try {
            phoneInfos = event8.getPhones();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getRealizedValue()}.
     */
    public void testGetRealizedValue() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);

        int realizedValue;
        Exception error = null;

        try {
            realizedValue = event1.getRealizedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        error = null;
        try {
            realizedValue = event2.getRealizedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        error = null;
        try {
            realizedValue = event3.getRealizedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        error = null;
        realizedValue = event4.getRealizedValue();
        assertEquals(realized, realizedValue);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        error = null;
        try {
            realizedValue = event5.getRealizedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        error = null;
        try {
            realizedValue = event6.getRealizedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        error = null;
        try {
            realizedValue = event7.getRealizedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        error = null;
        try {
            realizedValue = event8.getRealizedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.SpeakableEvent#getRequestedValue()}.
     */
    public void testGetRequestedValue() {
        final SpeakableEvent event1 = new SpeakableEvent(source,
                SpeakableEvent.TOP_OF_QUEUE, 42);

        int requestedValue;
        Exception error = null;

        try {
            requestedValue = event1.getRequestedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        error = null;
        try {
            requestedValue = event2.getRequestedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        error = null;
        try {
            requestedValue = event3.getRequestedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        error = null;
        requestedValue = event4.getRequestedValue();
        assertEquals(requested, requestedValue);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        error = null;
        try {
            requestedValue = event5.getRequestedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        error = null;
        try {
            requestedValue = event6.getRequestedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        error = null;
        try {
            requestedValue = event7.getRealizedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        error = null;
        try {
            requestedValue = event8.getRequestedValue();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);
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
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
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
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
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

        int type;
        Exception error = null;

        try {
            type = event1.getType();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final String[] attrs = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source,
                SpeakableEvent.ELEMENT_REACHED, 46, "textInfo",
                SpeakableEvent.ELEMENT_OPEN, attrs);
        error = null;
        type = event2.getType();
        assertEquals(SpeakableEvent.ELEMENT_OPEN, type);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source,
                SpeakableEvent.MARKER_REACHED, 49, "textInfo2", audioPosition);
        error = null;
        try {
            type = event3.getType();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final int requested = 100;
        final int realized = 99;
        final SpeakableEvent event4 = new SpeakableEvent(source,
                SpeakableEvent.PROSODY_UPDATED, 51, "textInfo3",
                SpeakableEvent.PROSODY_RATE, requested, realized);
        error = null;
        type = event4.getType();
        assertEquals(SpeakableEvent.PROSODY_RATE, type);

        final int wordStart = 17;
        final int wordEnd = 23;
        final SpeakableEvent event5 = new SpeakableEvent(source,
                SpeakableEvent.WORD_STARTED, 55, "textInfo4", wordStart,
                wordEnd);
        error = null;
        try {
            type = event5.getType();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source,
                SpeakableEvent.PHONEME_STARTED, 59, "textInfo5", phones, 1);
        error = null;
        try {
            type = event6.getType();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        type = event7.getType();
        assertEquals(SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE, type);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        error = null;
        try {
            type = event8.getType();
        } catch (IllegalStateException e) {
            error = e;
        }
        assertNotNull(error);
    }


    /**
     * Test method for {@link javax.speech.SpeechEvent#paramString()}.
     */
    public void testParamString() {
        final SpeakableEvent event1 = new SpeakableEvent(source, 43, 44);
        final String str1 = event1.paramString();
        assertTrue("id not found in toString", str1.indexOf("43") >= 0);

        final String[] attributes = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source, 45, 46,
                "textInfo", 47, attributes);
        final String str2 = event2.paramString();
        assertTrue("id not found in toString", str2.indexOf("45") >= 0);

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source, 48, 49,
                "textInfo2", audioPosition);
        final String str3 = event3.paramString();
        assertTrue("id not found in toString", str3.indexOf("48") >= 0);

        final SpeakableEvent event4 = new SpeakableEvent(source, 50, 51,
                "textInfo3", 51, 52, 53);
        final String str4 = event4.paramString();
        assertTrue("id not found in toString", str4.indexOf("50") >= 0);

        final SpeakableEvent event5 = new SpeakableEvent(source, 54, 55,
                "textInfo4", 56, 57);
        final String str5 = event5.paramString();
        assertTrue("id not found in toString", str5.indexOf("54") >= 0);

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source, 58, 59,
                "textInfo5", phones, 60);
        final String str6 = event6.paramString();
        assertTrue("id not found in toString", str6.indexOf("58") >= 0);

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        final String str7 = event7.paramString();
        assertTrue("id not found in toString", str7.indexOf("60") >= 0);

        final Voice oldVoice = new Voice(SpeechLocale.US, "mary",
                Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        final Voice newVoice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE,
                Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final SpeakableEvent event8 = new SpeakableEvent(source,
                SpeakableEvent.VOICE_CHANGED, 60, "textInfo7", oldVoice,
                newVoice);
        final String str8 = event8.paramString();
        assertTrue("id not found in toString", str8.indexOf(Integer
                .toString(SpeakableEvent.VOICE_CHANGED)) >= 0);
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#toString()}.
     */
    public void testToString() {
        final SpeakableEvent event1 = new SpeakableEvent(source, 43, 44);
        final String str1 = event1.toString();
        assertTrue("id not found in toString", str1.indexOf("43") >= 0);
        String paramString = event1.paramString();
        assertTrue("toString not longer than paramString",
                str1.length() > paramString.length());

        final String[] attributes = new String[] { "attribute1", "attribute2" };
        final SpeakableEvent event2 = new SpeakableEvent(source, 45, 46,
                "textInfo", 47, attributes);
        final String str2 = event2.toString();
        assertTrue("id not found in toString", str2.indexOf("45") >= 0);
        String paramString2 = event1.paramString();
        assertTrue("toString not longer than paramString",
                str2.length() > paramString2.length());

        final int audioPosition = 46738;
        final SpeakableEvent event3 = new SpeakableEvent(source, 48, 49,
                "textInfo2", audioPosition);
        final String str3 = event3.toString();
        assertTrue("id not found in toString", str3.indexOf("48") >= 0);
        String paramString3 = event3.paramString();
        assertTrue("toString not longer than paramString",
                str3.length() > paramString3.length());

        final SpeakableEvent event4 = new SpeakableEvent(source, 50, 51,
                "textInfo3", 51, 52, 53);
        final String str4 = event4.toString();
        assertTrue("id not found in toString", str4.indexOf("50") >= 0);
        String paramString4 = event4.paramString();
        assertTrue("toString not longer than paramString",
                str4.length() > paramString4.length());

        final SpeakableEvent event5 = new SpeakableEvent(source, 54, 55,
                "textInfo4", 56, 57);
        final String str5 = event5.toString();
        assertTrue("id not found in toString", str5.indexOf("54") >= 0);
        String paramString5 = event5.paramString();
        assertTrue("toString not longer than paramString",
                str5.length() > paramString5.length());

        final PhoneInfo[] phones = new PhoneInfo[] { new PhoneInfo("ph1", 1),
                new PhoneInfo("ph2", 2) };
        final SpeakableEvent event6 = new SpeakableEvent(source, 58, 59,
                "textInfo5", phones, 60);
        final String str6 = event6.toString();
        assertTrue("id not found in toString", str6.indexOf("58") >= 0);
        String paramString6 = event6.paramString();
        assertTrue("toString not longer than paramString",
                str6.length() > paramString6.length());

        final SpeakableEvent event7 = new SpeakableEvent(source,
                SpeakableEvent.SPEAKABLE_FAILED, 60, "textInfo6",
                SpeakableEvent.SPEAKABLE_FAILURE_RECOVERABLE);
        final String str7 = event7.toString();
        assertTrue("id not found in toString", str7.indexOf(Integer
                .toString(SpeakableEvent.SPEAKABLE_FAILED)) >= 0);
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
        assertTrue("id not found in toString", str8.indexOf(Integer
                .toString(SpeakableEvent.VOICE_CHANGED)) >= 0);
        String paramString8 = event8.paramString();
        assertTrue("toString not longer than paramString",
                str8.length() > paramString8.length());
    }
}
