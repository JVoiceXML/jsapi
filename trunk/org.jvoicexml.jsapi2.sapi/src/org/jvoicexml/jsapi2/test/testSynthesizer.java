package org.jvoicexml.jsapi2.test;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.sapi.synthesis.SapiSynthesizer;

public class testSynthesizer {

    public static void main(String[] args) throws InterruptedException 
    {       
    	SapiSynthesizer sps = new SapiSynthesizer("Microsoft Anna");
    	SapiSynthesizer sps2 = new SapiSynthesizer("LH Stefan");   // error p==0 der zeiger des dre Zweiten Instanz kann nicht Instanziert werden
    	
    	System.out.println( "new Synthesizer:\tokay");
        try {
        	sps.handleAllocate();        	
        	sps2.handleAllocate();
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
		sps2.handleSpeak( 3, "was geht ab wir reden zu zweit ");
		System.out.println( "Speak:\t\t\tokay");//<RATE SPEED= \"-5\">
		Thread.sleep(435);

		sps.handlePause();
		System.out.println( "Pause:\t\t\tokay");
		Thread.sleep(2000);
		
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
		Thread.sleep(2000);
			
		sps.handleDeallocate();
		sps2.handleDeallocate();
		System.out.println( "Dellocate:\t\tokay");
		System.exit(0);
		
    }
}
