package org.jvoicexml.jsapi2.recognition;

import java.util.ArrayList;
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

    private ArrayList speakerProviles;

    public BaseSpeakerManager() {
        speakerProviles = new ArrayList();
    }

    /**
     * createSpeaker
     *
     * @param speaker SpeakerProfile
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public void createSpeaker(SpeakerProfile speaker) {
    }

    /**
     * deleteSpeaker
     *
     * @param speaker SpeakerProfile
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public void deleteSpeaker(SpeakerProfile speaker) {
    }

    /**
     * getCurrentSpeaker
     *
     * @return SpeakerProfile
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public SpeakerProfile getCurrentSpeaker() {
        return null;
    }

    /**
     * getSpeakerManagerUI
     *
     * @return SpeakerManagerUI
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public SpeakerManagerUI getSpeakerManagerUI() {
        return null;
    }

    /**
     * listKnownSpeakers
     *
     * @return SpeakerProfile[]
     * @todo Implement this javax.speech.recognition.SpeakerManager method
     */
    public SpeakerProfile[] listKnownSpeakers() {
        return (SpeakerProfile[])speakerProviles.toArray(new SpeakerProfile[]{});

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
    }
}
