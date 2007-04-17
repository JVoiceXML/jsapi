package javax.speech.synthesis;

import javax.speech.SpeechEvent;

public class SpeakableEvent extends SpeechEvent {
    public static int DEFAULT_MASK = 0;

    public static int ELEMENT_CLOSE = 1;

    public static int ELEMENT_EMPTY = 2;

    public static int ELEMENT_OPEN = 3;

    public static int ELEMENT_REACHED = 4;

    public static int FATAL_MARKUP_FAILURE = 5;

    public static int MARKER_REACHED = 6;

    public static int MARKUP_FAILED = 7;

    public static int PHONEME_STARTED = 8;

    public static int PROSODY_CONTOUR = 9;

    public static int PROSODY_PITCH = 10;

    public static int PROSODY_PITCH_RANGE = 11;

    public static int PROSODY_RATE = 12;

    public static int PROSODY_UPDATED = 13;

    public static int PROSODY_VOLUME = 14;

    public static int RECOVERABLE_MARKUP_FAILURE = 15;

    public static int SPEAKABLE_CANCELLED = 16;

    public static int SPEAKABLE_ENDED = 17;

    public static int SPEAKABLE_PAUSED = 18;

    public static int SPEAKABLE_RESUMED = 19;

    public static int SPEAKABLE_STARTED = 20;

    public static int TOP_OF_QUEUE = 21;

    public static int UNKNOWN_AUDIO_POSITION = 22;

    public static int UNRECOVERABLE_MARKUP_FAILURE = 23;

    public static int UNSUPPORTED_ALPHABET = 24;

    public static int UNSUPPORTED_AUDIO = 25;

    public static int UNSUPPORTED_LANGUAGE = 26;

    public static int UNSUPPORTED_PHONEME = 27;

    public static int UNSUPPORTED_VOICE = 28;

    public static int VOICE_CHANGED = 29;

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

    SpeakableEvent(Object source, int id, int requestId, String textInfo,
            int type, String[] attributes) {
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
        return attributes;
    }

    public int getAudioPosition() {
        return audioPosition;
    }

    public String getDescription() {
        return description;
    }

    public int getIndex() {
        return index;
    }

    public Voice getNewVoice() {
        return newVoice;
    }

    public Voice getOldVoice() {
        return oldVoice;
    }

    public PhoneInfo[] getPhones() {
        return phones;
    }

    public int getRealizedValue() {
        throw new IllegalStateException("not implemented");
    }

    public int getRequestedValue() {
        throw new IllegalStateException("not implemented");
    }

    public int getRequestId() {
        return requestId;
    }

    public String getTextInfo() {
        return textInfo;
    }

    public int getType() {
        return type;
    }

    public int getWordEnd() {
        return wordEnd;
    }

    public int getWordStart() {
        return wordStart;
    }

    public String paramString() {
        return super.toString();
    }
}
