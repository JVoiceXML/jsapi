package org.jvoicexml.jsapi2.jse.test.synthesis;
import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;

/**
 * 
 */

/**
 * @author Dirk Schnelle-Walka
 *
 */
public class DummySynthesizer extends JseBaseSynthesizer {

    @Override
    protected Speakable getSpeakable(String text) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void handleAllocate() throws EngineStateException,
            EngineException, AudioException, SecurityException {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected boolean handleCancel() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleCancel(int id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleCancelAll() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleDeallocate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handlePause() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleResume() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void handleSpeak(int id, String item) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void handleSpeak(int id, Speakable item) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        // TODO Auto-generated method stub
        return null;
    }
}
