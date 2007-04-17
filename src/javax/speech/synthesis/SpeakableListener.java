package javax.speech.synthesis;

import java.util.EventListener;

import javax.speech.SpeechEventListener;

public interface SpeakableListener extends EventListener, SpeechEventListener {
    void speakableUpdate(SpeakableEvent e);
}
