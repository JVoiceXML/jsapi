package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.recognition.JseBaseRecognizer;

public class SapiRecognizer extends JseBaseRecognizer {
	

        public static void main(String[] args) 
    { 
        SapiRecognizer spr = new SapiRecognizer();
        spr.getBuiltInGrammars();
   
    }
        
        public native Vector getBuiltInGrammars();

		protected native void Allocate() throws EngineStateException,
				EngineException, AudioException, SecurityException;

		protected native void Deallocate();

		protected native void Pause();
		
		protected native void Pause(int flags);

		protected native boolean Resume();

		protected native boolean setGrammars(Vector grammarDefinition);

		protected native EnginePropertyChangeRequestListener getChangeRequestListener();

}
