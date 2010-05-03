package org.jvoicexml.jsapi2.sapi.synthesis;

import java.util.logging.Logger;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;


public class SapiSynthesizer extends JseBaseSynthesizer {
	
	private static final Logger LOGGER =
        Logger.getLogger(SapiSynthesizer.class.getName());
	
	
	SapiSynthesizer(){
		
		String dir = System.getProperty("user.dir");
    	System.out.println(dir);		
    	System.load(dir+"\\cpp\\Jsapi2SapiBridge\\Debug\\JSapi2SapiBridge.dll");				
	}
		
    public static void main(String[] args) 
    {       
    	SapiSynthesizer sps = new SapiSynthesizer();
    	
    	System.out.println( "new SapiSynthesizer: okay");
        try {
        	sps.handleAllocate();
        	System.out.println( "handleAllocate: okay");
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
		
		
		System.out.println( "handleSpeak: okay");
		sps.handleSpeak( 5, "<?xml version=\"1.0\"?>Hallo <break time=\"1s\"/> ich liebe Java");
		sps.handleDeallocate();
		System.out.println( "handleDellocate: okay");
		System.exit(0);
    }

	@Override
	protected native Speakable getSpeakable(String text);

	@Override
	protected native void handleAllocate() throws EngineStateException,
			EngineException, AudioException, SecurityException;

	@Override
	protected native boolean handleCancel();

	@Override
	protected native boolean handleCancel(int id);

	@Override
	protected native boolean handleCancelAll();

	@Override
	protected native void handleDeallocate();

	@Override
	protected native void handlePause();

	@Override
	protected native boolean handleResume();

	@Override
	protected native void handleSpeak(int id, String item);

	@Override
	protected native void handleSpeak(int id, Speakable item);

	@Override
	protected  EnginePropertyChangeRequestListener getChangeRequestListener(){
		return null;
	}
    


}

