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

package javax.speech.synthesis;

import java.util.Collection;

import javax.speech.SpeechEvent;

public class SpeakableEvent extends SpeechEvent {
    // Events.
    public static int TOP_OF_QUEUE = 1;

    public static int SPEAKABLE_STARTED = TOP_OF_QUEUE << 1;

    public static int ELEMENT_REACHED = SPEAKABLE_STARTED << 1;

    public static int VOICE_CHANGED = ELEMENT_REACHED << 1;

    public static int PROSODY_UPDATED = VOICE_CHANGED << 1;

    public static int MARKER_REACHED = PROSODY_UPDATED << 1;

    public static int WORD_STARTED = MARKER_REACHED << 1;

    public static int PHONEME_STARTED = WORD_STARTED << 1;

    public static int MARKUP_FAILED = PHONEME_STARTED << 1;

    public static int SPEAKABLE_PAUSED = MARKUP_FAILED << 1;

    public static int SPEAKABLE_RESUMED = SPEAKABLE_PAUSED << 1;

    public static int SPEAKABLE_CANCELLED = SPEAKABLE_RESUMED << 1;

    public static int SPEAKABLE_ENDED = SPEAKABLE_CANCELLED << 1;

    public static int DEFAULT_MASK = MARKER_REACHED | MARKUP_FAILED
            | SPEAKABLE_CANCELLED | SPEAKABLE_STARTED | SPEAKABLE_ENDED
            | SPEAKABLE_PAUSED | SPEAKABLE_RESUMED | VOICE_CHANGED;

    // Types.
    public static int ELEMENT_OPEN = 1;

    public static int ELEMENT_CLOSE = 2;

    public static int ELEMENT_EMPTY = 3;

    public static int FATAL_MARKUP_FAILURE = 4;

    public static int PROSODY_CONTOUR = 5;

    public static int PROSODY_PITCH = 6;

    public static int PROSODY_PITCH_RANGE = 7;

    public static int PROSODY_RATE = 8;

    public static int PROSODY_VOLUME = 9;

    public static int RECOVERABLE_MARKUP_FAILURE = 10;

    public static int UNRECOVERABLE_MARKUP_FAILURE = 11;

    public static int UNSUPPORTED_ALPHABET = 12;

    public static int UNSUPPORTED_AUDIO = 13;

    public static int UNSUPPORTED_LANGUAGE = 14;

    public static int UNSUPPORTED_PHONEME = 15;

    public static int UNSUPPORTED_VOICE = 16;

    // Unknown adio position.
    public static int UNKNOWN_AUDIO_POSITION = -1;

    private int requestId;

    private String textInfo;

    private int audioPosition;

    private int wordStart;

    private int wordEnd;

    private int type;

    private int requested;

    private int realized;

    private String description;

    private String[] attributes;

    private PhoneInfo[] phones;

    private int index;

    private Voice newVoice;

    private Voice oldVoice;

    public SpeakableEvent(Object source, int id, int requestId) {
        super(source, id);

        this.requestId = requestId;
    }

    public SpeakableEvent(Object source, int id, int requestId,
            String textInfo, int audioPosition) {
        this(source, id, requestId);

        this.textInfo = textInfo;
        this.audioPosition = audioPosition;
    }

    public SpeakableEvent(Object source, int id, int requestId,
            String textInfo, int wordStart, int wordEnd) {
        this(source, id, requestId);

        this.textInfo = textInfo;
        this.wordStart = wordStart;
        this.wordEnd = wordEnd;
    }

    public SpeakableEvent(Object source, int id, int requestId,
            String textInfo, int type, int requested, int realized) {
        this(source, id, requestId);

        this.textInfo = textInfo;
        this.type = type;
        this.requested = requested;
        this.realized = realized;
    }

    public SpeakableEvent(Object source, int id, int requestId,
            String textInfo, int type, String description) {
        this(source, id, requestId);

        this.textInfo = textInfo;
        this.type = type;
        this.description = description;
    }

    public SpeakableEvent(Object source, int id, int requestId,
            String textInfo, int type, String[] attributes) {
        this(source, id, requestId);
        this.textInfo = textInfo;
        this.type = type;
        this.attributes = attributes;
    }

    public SpeakableEvent(Object source, int id, int requestId,
            String textInfo, PhoneInfo[] phones, int index) {
        this(source, id, requestId);

        this.textInfo = textInfo;
        this.phones = phones;
        this.index = index;
    }

    public SpeakableEvent(Object source, int id, int requestId,
            String textInfo, Voice oldVoice, Voice newVoice) {
        this(source, id, requestId);

        this.textInfo = textInfo;
        this.newVoice = newVoice;
        this.oldVoice = oldVoice;
    }

    public String[] getAttributes() {
        final int id = getId();
        if (id == ELEMENT_REACHED) {
            return attributes;
        }

        throw new IllegalStateException("Event does not include attributes!");
    }

    public int getAudioPosition() {
        final int id = getId();
        if (id == MARKER_REACHED) {
            return audioPosition;
        }

        throw new IllegalStateException("Event does not include an audio"
                + " position");
    }

    public String getDescription() {
        final int id = getId();
        if (id == MARKUP_FAILED) {
            return description;
        }

        throw new IllegalStateException("Event does not include a description");
    }

    public int getIndex() {
        final int id = getId();
        if (id == PHONEME_STARTED) {
            return index;
        }

        throw new IllegalStateException("Event does not include an index");
    }

    public Voice getNewVoice() {
        final int id = getId();
        if (id == VOICE_CHANGED) {
            return newVoice;
        }

        throw new IllegalStateException("Event does not include a new voice");
    }

    public Voice getOldVoice() {
        final int id = getId();
        if (id == VOICE_CHANGED) {
            return oldVoice;
        }

        throw new IllegalStateException("Event does not include an old voice");
    }

    public PhoneInfo[] getPhones() {
        final int id = getId();
        if (id == PHONEME_STARTED) {
            return phones;
        }

        throw new IllegalStateException("Event does not include a phone info");
    }

    public int getRealizedValue() {
        final int id = getId();
        if (id == PROSODY_UPDATED) {
            return realized;
        }

        throw new IllegalStateException("Event does not include a realized"
                + " value");
    }

    public int getRequestedValue() {
        final int id = getId();
        if (id == PROSODY_UPDATED) {
            return requested;
        }

        throw new IllegalStateException("Event does not include a requested"
                + " value");
    }

    public int getRequestId() {
        return requestId;
    }

    public String getTextInfo() {
        return textInfo;
    }

    public int getType() {
        // TODO Check if there is an error in the specification.
        // The MARKER_REACHED does not provide a type.
        final int id = getId();
        if ((id == ELEMENT_REACHED) || (id == MARKUP_FAILED)
                || (id == PROSODY_UPDATED)) {
            return type;
        }

        throw new IllegalStateException("Event does not include a type!");
    }

    public int getWordEnd() {
        final int id = getId();
        if (id == WORD_STARTED) {
            return wordEnd;
        }

        throw new IllegalStateException("Event does not include a word end!");
    }

    public int getWordStart() {
        final int id = getId();
        if (id == WORD_STARTED) {
            return wordStart;
        }

        throw new IllegalStateException("Event does not include a word start!");
    }

    /**
     * {@inheritDoc}
     */
    protected Collection getParameters() {
        final Collection parameters = super.getParameters();

        final int id = getId();

        final Integer typeObject = new Integer(type);
        parameters.add(typeObject);
        final Integer requestIdObject = new Integer(requestId);
        parameters.add(requestIdObject);
        if (id == PROSODY_UPDATED) {
            final Integer requestedObject = new Integer(requested);
            parameters.add(requestedObject);
        }
        parameters.add(textInfo);
        if (id == MARKER_REACHED) {
            final Integer audioPositionObject = new Integer(audioPosition);
            parameters.add(audioPositionObject);
        }
        if (id == WORD_STARTED) {
            final Integer wordStartObject = new Integer(wordStart);
            parameters.add(wordStartObject);
            final Integer wordEndObject = new Integer(wordEnd);
            parameters.add(wordEndObject);
            parameters.add(newVoice);
            parameters.add(oldVoice);
        }
        if (id == MARKUP_FAILED) {
            parameters.add(description);
        }
        if (id == ELEMENT_REACHED) {
            parameters.add(attributes);
        }
        if (id == PHONEME_STARTED) {
            parameters.add(phones);
            final Integer indexObject = new Integer(index);
            parameters.add(indexObject);
        }

        return parameters;
    }
}
