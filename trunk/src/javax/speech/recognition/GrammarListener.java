package javax.speech.recognition;

import javax.speech.SpeechEventListener;

public interface GrammarListener extends SpeechEventListener {
    void grammarUpdate(GrammarEvent e);
}
