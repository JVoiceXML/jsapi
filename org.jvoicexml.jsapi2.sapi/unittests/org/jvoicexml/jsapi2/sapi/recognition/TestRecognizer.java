package org.jvoicexml.jsapi2.sapi.recognition;

import javax.speech.AudioException;
import javax.speech.Engine;
import javax.speech.EngineException;
import javax.speech.EngineManager;
import javax.speech.EngineStateException;
import javax.speech.SpeechLocale;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.RecognizerMode;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvoicexml.jsapi2.sapi.SapiEngineListFactory;
import org.jvoicexml.jsapi2.sapi.recognition.SapiRecognizer;


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
    
    private Recognizer recognizer;
    
    @BeforeClass
    public static void init() throws Exception {
        System.setProperty("javax.speech.supports.audio.management",
                Boolean.TRUE.toString());
        System.setProperty("javax.speech.supports.audio.capture",
                Boolean.TRUE.toString());
        EngineManager.registerEngineListFactory(
                SapiEngineListFactory.class.getCanonicalName());
    }
    
    /**
     * Set up the test .
     * @throws Exception
     *         set up failed
     */
    @Before
    public void setUp() throws Exception {
        recognizer = (Recognizer) EngineManager
            .createEngine(new RecognizerMode( SpeechLocale.GERMAN ));      
        recognizer.allocate();
        recognizer.waitEngineState(Engine.ALLOCATED);
    }
    
    /**
     * Tear down the test .
     * @throws Exception
     *         tear down failed
     */
    @After
    public void tearDown() throws Exception {
        if(recognizer != null){
           recognizer.deallocate();
           recognizer.waitEngineState(Engine.DEALLOCATED);
        }
    }
    
    /**
     * Test case for {@link SapiRecognizer#handlePause()}.
     * @throws Exception
     *         test failed
     */  
    @Test
    public void testPause() throws Exception {
            recognizer.pause();
            Thread.sleep(5000);
    }
    
    /**
     * Test case for {@link SapiRecognizer#handleResume()}.
     * @throws Exception
     *         test failed
     */
    @Test
    public void testResume() throws Exception {
        recognizer.resume();
        Thread.sleep(5000);
}
    
    
    
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
	
    public static void main(String[] args) throws InterruptedException, EngineStateException, AudioException, EngineException 
    { 
    	SapiRecognizer recognizer = null;  //new SapiRecognizer();
    	System.out.println( "new SapiRecognizer:\tokay");
    	
    	Thread.sleep(20);
    	
    	try {
			recognizer.allocate();
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
		
		recognizer.deallocate();
		System.out.println( "Deallocate:\t\tokay");
		
		System.exit(0);
    }

}
