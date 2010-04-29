package org.jvoicexml.jsapi2.sapi.recognition;

import java.io.ObjectInputStream.GetField;
import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.recognition.JseBaseRecognizer;

public class SapiRecognizer extends JseBaseRecognizer {

        
    public native Vector getBuiltInGrammars();

    
        public static void main(String[] args) 
    { 
        SapiRecognizer spr = new SapiRecognizer();
        spr.getBuiltInGrammars();
   
    }


		@Override
		protected native void handleAllocate() throws EngineStateException,
				EngineException, AudioException, SecurityException;


		@Override
		protected native void handleDeallocate();


		@Override
		protected native void handlePause();


		@Override
		protected native void handlePause(int flags);


		@Override
		protected native boolean handleResume();


		@Override
		protected native boolean setGrammars(Vector grammarDefinition);


		@Override
		protected EnginePropertyChangeRequestListener getChangeRequestListener() {
			// TODO Auto-generated method stub
			return null;
		}

	
}
