package javax.speech.recognition;

public interface Grammar {
    int ACTIVATION_FOCUS = 0;

    int ACTIVATION_GLOBAL = 1;

    int ACTIVATION_MODAL = 2;

    void addGrammarListener(GrammarListener listener);

    void addResultListener(ResultListener listener);

    int getActivationMode();

    Recognizer getRecognizer();

    String getReference();

    boolean isActive();

    boolean isEnabled();

    void removeGrammarListener(GrammarListener listener);

    void removeResultListener(ResultListener listener);

    void setActivationMode(int mode);

    void setEnabled(boolean flag);
}
