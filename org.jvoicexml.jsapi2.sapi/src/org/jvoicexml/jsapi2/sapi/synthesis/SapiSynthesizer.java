package org.jvoicexml.jsapi2.sapi.synthesis;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;

/**
 * A SAPI compliant {@link javax.speech.synthesis.Synthesizer}.
 * @author Dirk Schnelle-Walka
 * @author Josua Arndt
 *
 */
public final class SapiSynthesizer extends JseBaseSynthesizer {

    static {
        System.loadLibrary("Jsapi2SapiBridge");
    }

	String engineName2 ;
	public long sapiSynthesizerPtr;
	
	public SapiSynthesizer(String name){
		
		engineName2 = "name="+name;
    }		

	
	protected native Speakable getSpeakable(String text);

	
	public native void handleAllocate() throws EngineStateException,
			EngineException, AudioException, SecurityException;

	
	public native boolean handleCancel();

	
	protected native boolean handleCancel(int id);

	
	protected native boolean handleCancelAll();

	
	public native void handleDeallocate();


	public native void handlePause();

	
	public native boolean handleResume();

	
	public native void handleSpeak(int id, String item);

	
	protected native void handleSpeak(int id, Speakable item);

	
	protected native EnginePropertyChangeRequestListener getChangeRequestListener();


}

