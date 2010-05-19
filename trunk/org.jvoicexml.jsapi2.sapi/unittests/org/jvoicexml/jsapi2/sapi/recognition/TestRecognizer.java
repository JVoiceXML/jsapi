package org.jvoicexml.jsapi2.sapi.recognition;

import javax.speech.AudioException;
import javax.speech.Engine;
import javax.speech.EngineException;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.sapi.recognition.SapiRecognizer;
import org.jvoicexml.jsapi2.sapi.synthesis.SapiSynthesizer;

/**
 * Test cases for {@link SapiRecognizer}.
 * <p>
 * Run this unit test with the VM option:
 * <code>-Djava.library.path=cpp/Jsapi2SapiBridge/Debug</code>.
 * </p>
 * @author Dirk Schnelle-Walka
 * @author Josua Arndt
 *
 */
public class TestRecognizer {
    
//    public void testRecognition() throws Exception {
//        
//        SapiRecognizer recognizer = new SapiRecognizer();
//        recognizer.allocate();
//        recognizer.waitEngineState(Engine.ALLOCATED);
//        
//        recognizer.setGrammar("Licht.xml");       
//        Thread.sleep(6000);
//        recognizer.deallocate();
//    }
	
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
				 	
		recognizer.setGrammar("Licht.xml");		
		System.out.println( "Load grammar:\t\tokay");
		Thread.sleep(20);
		
		recognizer.startRecognition();		
		System.out.println( "Start:\t\tokay");
		Thread.sleep(2000);
		
		recognizer.pause();
		System.out.println( "Pause:\t\tokay");
		Thread.sleep(200);
		
		recognizer.resume();	
		System.out.println( "resume:\t\tokay");
		Thread.sleep(400);
		
		recognizer.handleDeallocate();
		System.out.println( "Deallocate:\t\tokay");
		
		System.exit(0);
    }

}
