package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.recognition.JseBaseRecognizer;

public class SapiRecognizer extends JseBaseRecognizer {

    @Override
    public Vector getBuiltInGrammars() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void handleAllocate() throws EngineStateException,
            EngineException, AudioException, SecurityException {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void handleDeallocate() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void handlePause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void handlePause(int flags) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected boolean handleResume() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean setGrammars(Vector grammarDefinition) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        // TODO Auto-generated method stub
        return null;
    }
	
}
