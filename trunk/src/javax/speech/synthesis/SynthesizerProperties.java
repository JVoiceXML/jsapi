package javax.speech.synthesis;

import javax.speech.EngineProperties;

public interface SynthesizerProperties extends EngineProperties {
    int DEFAULT_VOLUME = 0;

    int MAX_VOLUME = 1;

    int MEDIUM_VOLUME = 2;

    int MIN_VOLUME = 3;

    int OBJECT_LEVEL = 4;

    int QUEUE_LEVEL = 5;

    int WORD_LEVEL = 6;

    int getInterruptibility();

    int getPitch();

    int getPitchRange();

    int getSpeakingRate();

    Voice getVoice();

    int getVolume();

    void setInterruptibility(int level);

    void setPitch(int hertz);

    void setPitchRange(int hertz);

    void setPriority(int priority);

    void setSpeakingRate(int wpm);

    void setVoice(Voice voice);

    void setVolume(int volume);
}
