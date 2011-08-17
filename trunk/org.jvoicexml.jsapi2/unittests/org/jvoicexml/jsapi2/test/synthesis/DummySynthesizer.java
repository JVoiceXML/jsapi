package org.jvoicexml.jsapi2.test.synthesis;
import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.VocabularyManager;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.synthesis.BaseSynthesizer;
import org.jvoicexml.jsapi2.test.DummyAudioManager;
import org.jvoicexml.jsapi2.test.DummySpeechEventExecutor;

/**
 * 
 */

/**
 * @author Dirk Schnelle-Walka
 *
 */
public class DummySynthesizer extends BaseSynthesizer {

    protected Speakable getSpeakable(String text) {
        return null;
    }

    protected void handleAllocate() throws EngineStateException,
            EngineException, AudioException, SecurityException {
    }

    protected boolean handleCancel() {
        return true;
    }

    protected boolean handleCancel(int id) {
        return true;
    }

    protected boolean handleCancelAll() {
        return true;
    }

    protected void handleDeallocate() {
    }

    protected void handlePause() {
    }

    protected boolean handleResume() {
        return true;
    }

    protected void handleSpeak(int id, String item) {
    }

    protected void handleSpeak(int id, Speakable item) {
    }

    protected AudioManager createAudioManager() {
        return new DummyAudioManager();
    }

    protected SpeechEventExecutor createSpeechEventExecutor() {
        return new DummySpeechEventExecutor();
    }

    protected VocabularyManager createVocabularyManager() {
        return null;
    }

    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        return null;
    }
}
