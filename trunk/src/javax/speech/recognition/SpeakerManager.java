package javax.speech.recognition;

public interface SpeakerManager {
    void createSpeaker(SpeakerProfile speaker);

    void deleteSpeaker(SpeakerProfile speaker);

    SpeakerProfile getCurrentSpeaker();

    SpeakerManagerUI getSpeakerManagerUI();

    SpeakerProfile[] listKnownSpeakers();

    void renameSpeaker(SpeakerProfile oldSpeaker, SpeakerProfile newSpeaker);

    void restoreCurrentSpeaker();

    void saveCurrentSpeaker();

    void setCurrentSpeaker(SpeakerProfile speaker);
}
