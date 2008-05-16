package org.jvoicexml.jsapi2.j2se.recognition;

import java.util.Vector;
import javax.speech.recognition.SpeakerManager;
import javax.speech.recognition.SpeakerProfile;
import javax.speech.recognition.SpeakerManagerUI;
import javax.speech.EngineStateException;

/**
 * <p>Title: JSAPI 2.0</p>
 *
 * <p>Description: An independent reference implementation of JSR 113</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class BaseSpeakerManager implements SpeakerManager {

    private Vector speakerProfiles;

    private SpeakerProfile currentSpeaker;

    public BaseSpeakerManager() {
        speakerProfiles = new Vector();
        currentSpeaker = null;
    }

    /**
     * createSpeaker
     *
     * @param speaker SpeakerProfile
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public void createSpeaker(SpeakerProfile speaker) {
        speakerProfiles.addElement(speaker);
    }

    /**
     * deleteSpeaker
     *
     * @param speaker SpeakerProfile
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public void deleteSpeaker(SpeakerProfile speaker) {
        speakerProfiles.removeElement(speaker);
    }

    /**
     * getCurrentSpeaker
     *
     * @return SpeakerProfile
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public SpeakerProfile getCurrentSpeaker() {
        return currentSpeaker;
    }

    /**
     * getSpeakerManagerUI
     *
     * @return SpeakerManagerUI
     */
    public SpeakerManagerUI getSpeakerManagerUI() {
        return null;
    }

    /**
     * listKnownSpeakers
     *
     * @return SpeakerProfile[]
     */
    public SpeakerProfile[] listKnownSpeakers() {
        if (speakerProfiles.size() < 1) return new SpeakerProfile[]{};

        SpeakerProfile[] profiles = new SpeakerProfile[speakerProfiles.size()];
        for (int i = 0; i < speakerProfiles.size(); i++) {
            profiles[i] = (SpeakerProfile)speakerProfiles.elementAt(i);
        }
        return profiles;

    }

    /**
     * renameSpeaker
     *
     * @param oldSpeaker SpeakerProfile
     * @param newSpeaker SpeakerProfile
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public void renameSpeaker(SpeakerProfile oldSpeaker,
                              SpeakerProfile newSpeaker) {
    }

    /**
     * restoreCurrentSpeaker
     *
     * @throws EngineStateException
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public void restoreCurrentSpeaker() throws EngineStateException {
    }

    /**
     * saveCurrentSpeaker
     *
     * @throws EngineStateException
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public void saveCurrentSpeaker() throws EngineStateException {
    }

    /**
     * setCurrentSpeaker
     *
     * @param speaker SpeakerProfile
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public void setCurrentSpeaker(SpeakerProfile speaker) {
        if (speakerProfiles.contains(speaker) == false) {
            createSpeaker(speaker);
        }
        currentSpeaker = speaker;
    }
}
