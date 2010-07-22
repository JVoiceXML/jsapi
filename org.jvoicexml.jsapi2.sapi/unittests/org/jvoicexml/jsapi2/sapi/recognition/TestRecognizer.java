package org.jvoicexml.jsapi2.sapi.recognition;

import java.io.InputStream;

import javax.speech.Engine;
import javax.speech.EngineManager;
import javax.speech.recognition.Grammar;
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
import org.jvoicexml.jsapi2.sapi.SapiEngineListFactory;


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
                SapiEngineListFactory.class.getCanonicalName());
        
        
    }
    
    /**
     * Set up the test .
     * @throws Exception
     *         set up failed
     */
    @Before
    public void setUp() throws Exception {
        System.out.println("Allocating ASR Engine");
        recognizer =
            (Recognizer) EngineManager.createEngine(RecognizerMode.DEFAULT);
        recognizer.allocate();
        recognizer.waitEngineState(Engine.ALLOCATED);
        System.out.println("ASR Engine allocated");
    }

    /**
     * Tear down the test .
     * @throws Exception
     *         tear down failed
     */
    @After
    public void tearDown() throws Exception {
        System.out.println("Deallocatinge ASR Engine");
        if (recognizer != null) {
           recognizer.deallocate();
           recognizer.waitEngineState(Engine.DEALLOCATED);
        }
        System.out.println("ASR Engine deallocated");
    }

    /**
     * Simple Test case for the recognizer.
     * Use only one Grammar
     * 
     * @throws Exception
     *         test failed.
     */
//    @Test
//    public void testRecognize() throws Exception {
//        recognizer.addResultListener(this);
//
//        final GrammarManager grammarManager = recognizer.getGrammarManager();
//        final InputStream in = TestRecognizer.class.getResourceAsStream("global_WZ.xml");
//        grammarManager.loadGrammar("grammar:global_WZ", null, in, "UTF-8");//iso-8859-1
//        recognizer.requestFocus();
//        recognizer.resume();
//        recognizer.waitEngineState(Engine.RESUMED);
//        System.out.println("Test1 Please say something...");      
//        
//        synchronized (lock) {
//            lock.wait();
//        }
//        
//        System.out.print("Recognized: ");
//        final ResultToken[] tokens = result.getBestTokens();
//
//        for (int i = 0; i < tokens.length; i++) {
//            System.out.print(tokens[i].getText() + " ");
//        }
//        System.out.println();
//        
//    }
    /**
     * Test case  for the recognizer.
     * 
     * Use 2 grammars for the first recognition 
     * gretting contains:
     *          ( Hallo| Guten Morgen) [ Dirk| David| Renato| Josua] 
     * LIGHT contains:
     *          (Licht| Lampe) (ein| aus)
     * 
     * only use greeting for the second recognition
     * 
     * @throws Exception
     *         test failed.
     */
    @Test
    public void testRecognize2() throws Exception {
        recognizer.addResultListener(this);

        final GrammarManager grammarManager = recognizer.getGrammarManager();
        InputStream in = TestRecognizer.class.getResourceAsStream("Licht.xml");
        grammarManager.loadGrammar("grammar:LIGHT", null, in, "UTF-8");//iso-8859-1
        in = TestRecognizer.class.getResourceAsStream("hello.xml");
        grammarManager.loadGrammar("grammar:greeting", null, in, "UTF-8");//iso-8859-1
        
        
        recognizer.requestFocus();
        recognizer.resume();
        recognizer.waitEngineState(Engine.RESUMED);
        System.out.println("Test2.1 Please say something...");      
        
        synchronized (lock) {
            lock.wait();
        }
        
        System.out.print("Recognized: ");
        ResultToken[] tokens = result.getBestTokens();

        for (int i = 0; i < tokens.length; i++) {
            System.out.print(tokens[i].getText() + " ");
        }
        System.out.println();        
        
        Grammar gram = grammarManager.getGrammar("grammar:LIGHT");
        grammarManager.deleteGrammar(gram);
        
        recognizer.pause();                      
        recognizer.requestFocus();
        recognizer.resume();
        recognizer.waitEngineState(Engine.RESUMED);
        System.out.println("Test2.2 Please say something...");             
        
        Thread.sleep(3000);
        
        ((SapiRecognizer)recognizer).handlePause();

        Thread.sleep(2000);
      
      if(result != null){              
        System.out.print("Recognized: ");
        
        tokens = result.getBestTokens();

        for (int i = 0; i < tokens.length; i++) {
            System.out.print(tokens[i].getText() + " ");
        }
        System.out.println(); 
      }
        
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void resultUpdate(final ResultEvent event) {       
        System.out.println( event);
        if (event.getId() == ResultEvent.RESULT_ACCEPTED) {
            result = (Result) (event.getSource());
            synchronized (lock) {
                lock.notifyAll();
            }
        }
        
    }
    
}
