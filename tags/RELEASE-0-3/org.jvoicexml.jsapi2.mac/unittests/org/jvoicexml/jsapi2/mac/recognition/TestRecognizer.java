package org.jvoicexml.jsapi2.mac.recognition;

import java.io.InputStream;

import javax.speech.Engine;
import javax.speech.EngineManager;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.RecognizerMode;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.ResultToken;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvoicexml.jsapi2.mac.MacEngineListFactory;


/**
 * Test cases for {@link SapiRecognizer}.
 * <p>
 * Run this unit test with the VM option:
 * <code>-Djava.library.path=cpp/build</code>.
 * </p>
 * @author Dirk Schnelle-Walka
 * @author Josua Arndt
 *
 */
public final class TestRecognizer implements ResultListener {
    /** The test object. */
    private Recognizer recognizer;

    /** Locking mechanism. */
    private final Object lock = new Object();

    /** The recognition result. */
    private Result result;

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
                MacEngineListFactory.class.getCanonicalName());
    }
    
    /**
     * Set up the test .
     * @throws Exception
     *         set up failed
     */
    @Before
    public void setUp() throws Exception {
        recognizer =
            (Recognizer) EngineManager.createEngine(RecognizerMode.DEFAULT);
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
        if (recognizer != null) {
           recognizer.deallocate();
           recognizer.waitEngineState(Engine.DEALLOCATED);
        }
    }

    /**
     * Test case for the recognizer.
     * @throws Exception
     *         test failed.
     */
    @Test
    public void testRecognize() throws Exception {
        recognizer.addResultListener(this);

        final GrammarManager grammarManager = recognizer.getGrammarManager();
        final InputStream in = TestRecognizer.class.getResourceAsStream("Licht.xml");
        grammarManager.loadGrammar("grammar:LIGHT", null, in, "iso-8859-1");

        recognizer.requestFocus();
        recognizer.resume();
        recognizer.waitEngineState(Engine.RESUMED);
        System.out.println("Please say something...");
       
        synchronized (lock) {
            lock.wait();
        }
        
        System.out.print("Recognized: ");
        final ResultToken[] tokens = result.getBestTokens();

        for (int i = 0; i < tokens.length; i++) {
            System.out.print(tokens[i].getText() + " ");
        }
        System.out.println();
        
    }

//    /**
//     * Test case for {@link SapiRecognizer#handlePause()}.
//     * Test case for {@link SapiRecognizer#handleResume()}.
//     * @throws Exception
//     *         test failed
//     */  
//    @Test
//    public void testPause() throws Exception {
//            recognizer.pause();
//            System.out.println("\tPause Recognizer \n");
//            Thread.sleep(5000);
//            recognizer.resume();
//            System.out.println("\tResume Recognizer \n");
//            Thread.sleep(5000);
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resultUpdate(final ResultEvent event) {
        System.out.println(event);
        if (event.getId() == ResultEvent.RESULT_ACCEPTED) {
            result = (Result) (event.getSource());
            synchronized (lock) {
                lock.notifyAll();
            }
        }
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
}
