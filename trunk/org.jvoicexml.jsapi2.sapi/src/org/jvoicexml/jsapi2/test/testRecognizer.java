package org.jvoicexml.jsapi2.test;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.sapi.recognition.SapiRecognizer;

public class testRecognizer {
	
    public static void main(String[] args) throws InterruptedException 
    { 
    	SapiRecognizer recognizer = new SapiRecognizer();
    	System.out.println( "new SapiRecognizer:\tokay");
    	
    	Thread.sleep(20);
    	
    	try {
			recognizer.handleAllocate();
			System.out.println( "Allocate:\t\tokay");
		} catch (EngineStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AudioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

		
		Thread.sleep(800);
		
		recognizer.handleDeallocate();
		System.out.println( "Deallocate:\t\tokay");
		
		System.exit(0);
    }

}
