package javax.speech;

public interface Engine {
    long ALLOCATED = 1;

    long ALLOCATING_RESOURCES = 2;

    int ASYNCHRONOUS_MODE = 3;

    long DEALLOCATED = 4;

    long DEALLOCATING_RESOURCES = 5;

    long DEFOCUSED = 6;

    long ERROR_OCCURRED = 7;

    long FOCUSED = 8;

    int IMMEDIATE_MODE = 9;

    long PAUSED = 10;

    long RESUMED = 11;

    void allocate();

    void allocate(int mode);

    void deallocate();

    void deallocate(int mode);

    AudioManager getAudioManager();

    int getEngineMask();

    EngineMode getEngineMode();

    long getEngineState();

    VocabularyManager getVocabularyManager();

    void pause();

    boolean resume();

    void setEngineMask(int mask);

    boolean testEngineState(long state);

    void waitEngineState(long state);
}
