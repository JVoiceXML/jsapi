package javax.speech.synthesis;

import java.util.EventListener;

import javax.speech.EngineListener;
import javax.speech.SpeechEventListener;

public interface SynthesizerListener extends EngineListener, EventListener,
        SpeechEventListener {
    void synthesizerUpdate(SynthesizerEvent e);
}
