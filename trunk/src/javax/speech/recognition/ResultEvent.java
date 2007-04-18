package javax.speech.recognition;

import javax.speech.SpeechEvent;

public class ResultEvent extends SpeechEvent {
    public static int AUDIO_RELEASED = 0;

    public static int DEFAULT_MASK = 1;

    public static int GRAMMAR_FINALIZED = 2;

    public static int RESULT_ACCEPTED = 3;

    public static int RESULT_CREATED = 4;

    public static int RESULT_REJECTED = 5;

    public static int RESULT_UPDATED = 6;

    public static int TRAINING_INFO_RELEASED = 7;

    private boolean tokensFinalized;

    private boolean unfinalizedTokensChanged;

    public ResultEvent(Result source, int id) {
        super(source, id);
    }

    public ResultEvent(Result source, int id, boolean tokensFinalized,
            boolean unfinalizedTokensChanged) {
        super(source, id);

        this.tokensFinalized = tokensFinalized;
        this.unfinalizedTokensChanged = unfinalizedTokensChanged;
    }

    public boolean isFinalizedChanged() {
        return tokensFinalized;
    }

    public boolean isUnfinalizedChanged() {
        return unfinalizedTokensChanged;
    }

    public String paramString() {
        return super.paramString();
    }
}
