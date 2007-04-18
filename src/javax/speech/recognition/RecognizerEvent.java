package javax.speech.recognition;

import javax.speech.Engine;
import javax.speech.EngineEvent;

public class RecognizerEvent extends EngineEvent {
    public static int CHANGES_COMMITTED = 0;

    public static int CHANGES_REJECTED = 1;

    public static int DEFAULT_MASK = 2;

    public static int RECOGNIZER_BUFFERING = 3;

    public static int RECOGNIZER_LISTENING = 4;

    public static int RECOGNIZER_NOT_BUFFERING = 5;

    public static int RECOGNIZER_PROCESSING = 6;

    public static int SPEECH_STARTED = 7;

    public static int SPEECH_STOPPED = 8;

    static int UNKNOWN_AUDIO_POSITION = 9;

    private GrammarException grammarException;

    private long audioPosition;

    public RecognizerEvent(Recognizer source, int id, long oldEngineState,
            long newEngineState, Throwable problem,
            GrammarException grammarException, long audioPosition) {
        super(source, id, oldEngineState, newEngineState, problem);

        this.grammarException = grammarException;
        this.audioPosition = audioPosition;
    }

    public long getAudioPosition() {
        return audioPosition;
    }

    public GrammarException getGrammarException() {
        return grammarException;
    }

    public String paramString() {
        return super.paramString();
    }
}
