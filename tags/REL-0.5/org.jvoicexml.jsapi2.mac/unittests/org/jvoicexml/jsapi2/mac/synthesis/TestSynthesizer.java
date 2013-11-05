package org.jvoicexml.jsapi2.mac.synthesis;

import javax.speech.Engine;
import javax.speech.EngineManager;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.Voice;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvoicexml.jsapi2.mac.MacEngineListFactory;

/**
 * Test cases for {@link SapiSynthesizer}.
 * <p>
 * Run this unit test with the VM argument:
 * <code>-Djava.library.path=cpp/build</code>.
 * </p>
 * 
 * @author Dirk Schnelle-Walka
 * @author Josua Arndt
 * @author Stefan Radomski
 * 
 */
public final class TestSynthesizer {
	/** The test object. */
	private Synthesizer synthesizer;

	/**
	 * Prepare the test environment for all tests.
	 * 
	 * @throws Exception
	 *             prepare failed
	 */
	@BeforeClass
	public static void init() throws Exception {
		/**
		 * You have to set one of these properties to true on Mac OSX 10.6,
		 * otherwise JDK13Services.getProviders() called by the AudioManager
		 * will never return.
		 */
//		System.setProperty("com.apple.javaws.usingSWT", Boolean.TRUE.toString());
		System.setProperty("java.awt.headless", Boolean.TRUE.toString());
		EngineManager.registerEngineListFactory(MacEngineListFactory.class.getCanonicalName());
	}

	/**
	 * Set up the test .
	 * 
	 * @throws Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		Voice alex = new Voice(null, "Alex", Voice.GENDER_DONT_CARE, Voice.AGE_DONT_CARE, Voice.VARIANT_DONT_CARE);
		MacSynthesizerMode msm = new MacSynthesizerMode(null, null, null, null, false, new Voice[] { alex });
		synthesizer = (Synthesizer) EngineManager.createEngine(msm);
		synthesizer.allocate();
		synthesizer.waitEngineState(Engine.ALLOCATED);
	}

	/**
	 * Tear down the test .
	 * 
	 * @throws Exception
	 *             tear down failed
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
	 * 
	 * @throws Exception
	 *             test failed
	 */
	@Test
	public void testSpeak() throws Exception {
		synthesizer.resume();
		synthesizer.speak("I'll be artificial intelligence complete!", null);
//		synthesizer.speak("Half past 8", null);
		// synthesizer.speak("Ups!", null);
		System.out.println("this is a test output");
		synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
	}

	/**
	 * Test case for {@link SapiSynthesizer#handleSpause()}. Test case for
	 * {@link SapiSynthesizer#handleResume()}.
	 * 
	 * @throws Exception
	 *             test failed
	 */
	// @Test
	public void testPause() throws Exception {
		// synthesizer.speak("this is a test output with a pause and resume test",
		// null);
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
	 * Test case for
	 * {@link SapiSynthesizer#handleSpeak(int, javax.speech.synthesis.Speakable)}
	 * .
	 * 
	 * @throws Exception
	 *             test failed
	 */
	// @Test
	public void testSpeakSsml() throws Exception {
		synthesizer
				.speakMarkup("This sounds normal <pitch middle = '+10'/> but the pitch drops half way through", null);
		System.out.println("This sounds normal <pitch middle = '+10'/> but the pitch drops half way through");

		Thread.sleep(4000);
	}
}
