package javax.speech.recognition;

import javax.speech.SpeechEventListener;

public interface ResultListener extends SpeechEventListener {
    void resultUpdate(ResultEvent e);
}
