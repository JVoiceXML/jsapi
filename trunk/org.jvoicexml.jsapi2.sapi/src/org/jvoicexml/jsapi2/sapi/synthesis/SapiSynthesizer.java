package org.jvoicexml.jsapi2.sapi.synthesis;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;
import org.jvoicexml.jsapi2.sapi.recognition.SapiRecognizer;

public class SapiSynthesizer extends JseBaseSynthesizer {
	

		
    public static void main(String[] args) 
    { 
    	String dir = System.getProperty("user.dir");
    	System.out.println(dir);
		
    	System.load(dir+"\\cpp\\Jsapi2SapiBridge\\Debug\\Jsapi2SapiBridge.dll");
   	      
        
        try {
        	Allocate();
		} catch (EngineStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AudioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
    }
    
    
    protected native Speakable getSpeakable(String text);
	
    public static  native void Allocate() throws EngineStateException,
			EngineException, AudioException, SecurityException;

	protected native boolean Cancel();

	protected native boolean Cancel(int id);

	protected native boolean CancelAll();

	protected native void Deallocate();

	protected native void Pause();

	protected native boolean Resume();

	protected native void Speak(int id, String item);

	protected native void Speak(int id, Speakable item);

	protected native EnginePropertyChangeRequestListener getChangeRequestListener();

}

