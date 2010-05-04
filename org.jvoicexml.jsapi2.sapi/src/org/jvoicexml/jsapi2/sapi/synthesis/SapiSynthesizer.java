package org.jvoicexml.jsapi2.sapi.synthesis;

import java.util.logging.Logger;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineMode;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;


public class SapiSynthesizer extends JseBaseSynthesizer {
	    
	static{
    	System.load("D:\\eclipse - Kopie\\Workspace\\org.jvoicexml.jsapi2.sapi\\cpp\\Jsapi2SapiBridge\\Debug\\JSapi2SapiBridge.dll");				
    	}
	private static final Logger LOGGER =
        Logger.getLogger(SapiSynthesizer.class.getName());
	
	String engineName2 ;
	
	SapiSynthesizer(String name){
		engineName2 = "name="+name;
		String dir = System.getProperty("user.dir");
    	System.out.println(dir);
//    	static{
//    	System.load(dir+"\\cpp\\Jsapi2SapiBridge\\Debug\\JSapi2SapiBridge.dll");				
//    	}
    }
		
    public static void main(String[] args) throws InterruptedException 
    {       
    	SapiSynthesizer sps = new SapiSynthesizer("LH fgg Anna");
//    	SapiSynthesizer sps2 = new SapiSynthesizer("LH Stefan");   // error p==0 der zeiger des dre Zweiten Instanz kann nicht Instanziert werden
    	
    	System.out.println( "new Synthesizer:\tokay");
        try {
        	sps.handleAllocate();        	
//        	sps2.handleAllocate();
        	System.out.println( "Allocate:\t\tokay");
        	Thread.sleep(200);
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
	 
		sps.handleSpeak( 5, "Hello i love Java and i was paused and resumed now i will be cancelled please wait a moment the rest of this sentence well be Purged");//
//		sps.handleSpeak( 3, "was geht ab wir reden zu zweit ");
		System.out.println( "Speak:\t\t\tokay");//<RATE SPEED= \"-5\">
		Thread.sleep(435);

		sps.handlePause();
		System.out.println( "Pause:\t\t\tokay");
		Thread.sleep(5000);
		
		if(sps.handleResume()){
			System.out.println( "Resume:\t\t\tokay");
			Thread.sleep(3200);			
		}else{
			System.out.println( "Resume:\t\t\tnot okay");
			System.exit(0);
		}		
		if(sps.handleCancel()){
			System.out.println( "Cancel:\t\t\tokay");			
		}else{
			System.out.println( "Cancel:\t\t\tnot okay");
			System.exit(0);
		}
		Thread.sleep(2000);
		sps.handleSpeak( 5, "So now I spaek again");//
		System.out.println( "Speak:\t\t\tokay");//<RATE SPEED= \"-5\">
		Thread.sleep(2500);
			
		sps.handleDeallocate();
		System.out.println( "Dellocate:\t\tokay");
		System.exit(0);
		
    }

	
	protected native Speakable getSpeakable(String text);

	
	protected native void handleAllocate() throws EngineStateException,
			EngineException, AudioException, SecurityException;

	
	protected native boolean handleCancel();

	
	protected native boolean handleCancel(int id);

	
	protected native boolean handleCancelAll();

	
	protected native void handleDeallocate();


	protected native void handlePause();

	
	protected native boolean handleResume();

	
	protected native void handleSpeak(int id, String item);

	
	protected native void handleSpeak(int id, Speakable item);

	
	protected  EnginePropertyChangeRequestListener getChangeRequestListener(){
		return null;
	}
    


}

