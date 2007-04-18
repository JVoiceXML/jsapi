package javax.speech.recognition;

import javax.speech.EngineListener;

public interface RecognizerListener extends EngineListener {
    void recognizerUpdate(RecognizerEvent e);
}
