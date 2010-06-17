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
 *!!!!!!!!!!!!!!!!!!!!!!!Beware!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * each Test allocate the Synthesizer so beware that
 * a test is run completely before the next Test starts.
 * 
 * Disregard this will cause native code to crash
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public final class TestSynthesizer {
    /** The test object. */
    private Synthesizer synthesizer;

    /**
     * Prepare the test environment for all tests.
     * @throws Exception
     *         prepare failed
     */
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
        System.out.println("this is a test output");
        synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
    }

    /**
     * Test case for {@link SapiSynthesizer#handleSpause()}.
     * Test case for {@link SapiSynthesizer#handleResume()}.
     * @throws Exception
     *         test failed
     */
    @Test
    public void testPause() throws Exception {
        synthesizer.speak("this is a test output with a pause and resume test", null);     
        System.out.println("this is a test output with a pause and resume test");
        Thread.sleep(1800);
        synthesizer.pause();
        System.out.println("Pause");
        Thread.sleep(3000);
        synthesizer.resume();
        System.out.println("Resume");
        Thread.sleep(1500);       
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
        System.out.println("This sounds normal <pitch middle = '+10'/> but the pitch drops half way through");
        Thread.sleep(4000);
    }
}
