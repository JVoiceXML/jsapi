package javax.speech.synthesis;

import javax.speech.AudioSegment;
import javax.speech.Engine;

public interface Synthesizer extends Engine {
    long QUEUE_EMPTY = 0;

    long QUEUE_NOT_EMPTY = 1;

    void addSpeakableListener(SpeakableListener listener);

    void addSynthesizerListener(SynthesizerListener listener);

    void cancel();

    void cancel(int id);

    void cancelAll();

    String getPhonemes(String text);

    int getSpeakableMask();

    SynthesizerProperties getSynthesizerProperties();

    void pause();

    void removeSpeakableListener(SpeakableListener listener);

    void removeSynthesizerListener(SynthesizerListener listener);

    boolean resume();

    void setSpeakableMask(int mask);

    int speak(AudioSegment audio, SpeakableListener listener);

    int speak(Speakable speakable, SpeakableListener listener);

    int speak(String text, SpeakableListener listener);

    int speakMarkup(String synthesisMarkup, SpeakableListener listener);
}
