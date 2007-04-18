package javax.speech.recognition;

import javax.speech.SpeechEvent;

public class GrammarEvent extends SpeechEvent {
    public static int DEFAULT_MASK = 0;

    public static int GRAMMAR_ACTIVATED = 1;

    public static int GRAMMAR_CHANGES_COMMITTED = 2;

    public static int GRAMMAR_CHANGES_REJECTED = 3;

    public static int GRAMMAR_DEACTIVATED = 4;

    private boolean enabledChanged;

    private boolean definitionChanged;

    private GrammarException grammarException;

    public GrammarEvent(Object source, int id) {
        super(source, id);
    }

    public GrammarEvent(Grammar source, int id, boolean enabledChanged,
            boolean definitionChanged, GrammarException grammarException) {
        super(source, id);

        this.enabledChanged = enabledChanged;
        this.definitionChanged = definitionChanged;
        this.grammarException = grammarException;
    }

    public GrammarException getGrammarException() {
        return grammarException;
    }

    public boolean isDefinitionChanged() {
        return definitionChanged;
    }

    public boolean isEnabledChanged() {
        return enabledChanged;
    }

    public String paramString() {
        return super.paramString();
    }
}
