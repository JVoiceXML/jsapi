package org.jvoicexml.jsapi2.sapi.synthesis;

import javax.speech.AudioException;
import javax.speech.Engine;
import javax.speech.EngineException;
import javax.speech.EngineManager;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerMode;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvoicexml.jsapi2.sapi.SapiEngineListFactory;

/**
 * Test cases for {@link SapiSynthesizer}.
 * <p>
 * Run this unit test with the VM argument:
 * <code>-Djava.library.path=cpp/Jsapi2SapiBridge/Debug</code>.
 * </p>
 * @author Dirk Schnelle-Walka
 * @author Josua Arndt
 *
 */
public final class TestSynthesizer {
    /** The test object. */
    private Synthesizer synthesizer;

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
        synthesizer =  (Synthesizer) EngineManager
            .createEngine(SynthesizerMode.DEFAULT);
        synthesizer.allocate();
        synthesizer.waitEngineState(Engine.ALLOCATED);
    }

    /**
     * Tear down the test .
     * @throws Exception
     *         tear down failed
     */
    @After
    public void tearDown() throws Exception {
       if (synthesizer != null) {
           synthesizer.deallocate();
           synthesizer.waitEngineState(Engine.DEALLOCATED);
       }
    }

    /**
     * Test case for {@link SapiSynthesizer#handleSpeak(int, String)}.
     * @throws Exception
     *         test failed
     */
    @Test
    public void testSpeak() throws Exception {
        synthesizer.speak("this is a test output", null);
        Thread.sleep(5000);
    }

    /**
     * Test case for {@link SapiSynthesizer#handleSpeak(int, javax.speech.synthesis.Speakable)}.
     * @throws Exception
     *         test failed
     */
    @Test
    public void testSpeakSsml() throws Exception {
        synthesizer.speakMarkup(
                "This sounds normal <pitch middle = '+10'/> but the pitch drops half way through", null);
        Thread.sleep(4000);
    }

	public static void main(String[] args) throws InterruptedException, IllegalArgumentException, EngineException, EngineStateException, AudioException, SecurityException 
    {       
//        EngineManager
//        .registerEngineListFactory("org.jvoicexml.jsapi2.sapi.synthesis.SapiSynthesizer");
//        System.setProperty("javax.speech.supports.audio.management",
//        Boolean.TRUE.toString());
//        System.setProperty("javax.speech.supports.audio.capture",
//        Boolean.TRUE.toString());
//        // Create a synthesizer for the default Locale
//        SapiSynthesizer synth = (SapiSynthesizer) EngineManager
//        .createEngine(SynthesizerMode.DEFAULT);
//
//        System.out.println( "SynthesizerMode: " +  synth.getEngineMode().toString());
        
    	SapiSynthesizer sps = null;//new SapiSynthesizer("Microsoft Anna");
    	SapiSynthesizer sps2 = null;//new SapiSynthesizer("LH Stefan");  

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
		System.out.println( "Deallocate:\t\tokay");	
		
		//System.exit(0);
		
    }
}
