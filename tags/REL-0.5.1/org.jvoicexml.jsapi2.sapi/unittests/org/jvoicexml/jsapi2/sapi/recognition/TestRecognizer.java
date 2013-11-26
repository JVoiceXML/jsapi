package org.jvoicexml.jsapi2.sapi.recognition;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.speech.recognition.Rule;
import javax.speech.recognition.RuleComponent;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleTag;
import javax.speech.recognition.RuleToken;

import org.junit.After;
import org.junit.Assert;
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
        EngineManager.registerEngineListFactory(
                SapiEngineListFactory.class.getCanonicalName());
        Locale.setDefault(new Locale("en"));
        // Enable logging at all levels.
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        Logger.getLogger("").addHandler(handler);
        Logger.getLogger("").setLevel(Level.ALL);
    }

    /**
     * Set up the test.
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
     * Tear down the test.
     * @throws Exception
     *         tear down failed
     */
    @After
    public void tearDown() throws Exception {
        System.out.println("Deallocating ASR Engine");
        if (recognizer != null) {
           recognizer.pause();
           recognizer.waitEngineState(Engine.PAUSED);
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
    @Test
    public void testRecognize() throws Exception {
        recognizer.addResultListener(this);

        final GrammarManager grammarManager = recognizer.getGrammarManager();
        final String name = getLocaleGrammarName("hello");
        System.out.println(name);
        final InputStream in = TestRecognizer.class.getResourceAsStream(name);
        grammarManager.loadGrammar("grammar:greeting", null, in, "UTF-8");
        recognizer.requestFocus();
        recognizer.resume();
        recognizer.waitEngineState(Engine.RESUMED);
        System.out.println("Test1 Please say something...");
        
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

    /**
     * Retrieves the local grammar name.
     * @param base base grammar name.
     * @return localized grammar name.
     */
    private String getLocaleGrammarName(final String base) {
        final Locale locale = Locale.getDefault();
        final String country = "de"; //locale.getLanguage();
        return base + "-" + country + ".xml";
    }

    /**
     * Pause after the recognizer is ready.
     * 
     * @throws Exception
     *         test failed.
     */
    @Test
    public void testRecognizePause() throws Exception {
        /* timeout for enginestate change requests */
        int timeOut = 3000;
        
        // load testGrammar
        recognizer.addResultListener(this);
        final GrammarManager grammarManager = recognizer.getGrammarManager();
        final String name = getLocaleGrammarName("hello");
        final InputStream in = TestRecognizer.class.getResourceAsStream(name);
        grammarManager.loadGrammar("grammar:greeting", null, in, "UTF-8");
        
        // test resume
        recognizer.requestFocus();
        recognizer.resume();
        recognizer.waitEngineState(Engine.RESUMED, timeOut);
        assertTrue("Enginestate should be RESUMED.",
                    recognizer.testEngineState(Engine.RESUMED)
                );
        
        System.out.println("delaying...");
        Thread.sleep(500);
        System.out.println("...delayed");
        
        // test pause
        recognizer.pause();
        recognizer.waitEngineState(Engine.PAUSED, timeOut);
        assertTrue("Enginestate should be PAUSED.",
                recognizer.testEngineState(Engine.PAUSED)
            );
        
        // test resume after pause
        recognizer.resume();
        recognizer.waitEngineState(Engine.RESUMED, timeOut);
        assertTrue("Enginestate should be RESUMED.",
                recognizer.testEngineState(Engine.RESUMED)
            );
//        
//        System.out.println("Say something");
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
    }

    /**
     * Tests pause after recognition and resume with a different grammar.
     * 
     * @throws Exception
     *         test failed.
     */
    @Test
    public void testRecognizeResume() throws Exception {
        recognizer.addResultListener(this);

        final GrammarManager grammarManager = recognizer.getGrammarManager();
        final String name = getLocaleGrammarName("hello");
        final InputStream in = TestRecognizer.class.getResourceAsStream(name);
        System.out.println("Try to load Grammar");
        final Grammar hello =
            grammarManager.loadGrammar("grammar:greeting", null, in, "UTF-8");
        recognizer.requestFocus();
        recognizer.resume();
        recognizer.waitEngineState(Engine.RESUMED);
        System.out.println("Please say a greeting...");      
        
        synchronized (lock) {
            lock.wait();
        }
        
        System.out.print("Recognized: ");
        final ResultToken[] tokens = result.getBestTokens();

        for (int i = 0; i < tokens.length; i++) {
            System.out.print(tokens[i].getText() + " ");
        }
         System.out.println();
         
//        Object obj = ((SapiResult)result).getSemanticInterpretation();

        System.out.println("Recognizer wait for Enginestate PAUSED");
        recognizer.pause();
        recognizer.waitEngineState(Engine.PAUSED);
        grammarManager.deleteGrammar(hello);
        
        System.out.println("List grammars after delete... ");
        
        Grammar[] grams = grammarManager.listGrammars();
        System.out.println("count: "+ grams.length);
        
        for (int i=0; i<grams.length; i++){
            System.out.println(grams[i].getReference());
        }
        System.out.println("Load new grammar... ");
        final InputStream in2 =
            TestRecognizer.class.getResourceAsStream("Licht.xml");
        grammarManager.loadGrammar("grammar:LIGHT", null, in2, "UTF-8");
        System.out.println("Resume... ");
        recognizer.resume();
        recognizer.waitEngineState(Engine.RESUMED);
        System.out.println("Please say a command...");      
        
        synchronized (lock) {
            lock.wait();
        }
        
        System.out.print("Recognized: ");
        final ResultToken[] tokens2 = result.getBestTokens();

        for (int i = 0; i < tokens2.length; i++) {
            System.out.print(tokens2[i].getText() + " ");
        }
        System.out.println();
    }

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
    public void testRecognize2Grammars() throws Exception {
        recognizer.addResultListener(this);

        final GrammarManager grammarManager = recognizer.getGrammarManager();
        final InputStream in = TestRecognizer.class.getResourceAsStream("Licht.xml");
        grammarManager.loadGrammar("grammar:LIGHT", null, in, "UTF-8");
        final String name = getLocaleGrammarName("hello");
        final InputStream in2 = TestRecognizer.class.getResourceAsStream(name);
        grammarManager.loadGrammar("grammar:greeting", null, in2, "UTF-8");

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

        System.out.print("Recognized: ");
        tokens = result.getBestTokens();

        for (int i = 0; i < tokens.length; i++) {
            System.out.print(tokens[i].getText() + " ");
        }
        System.out.println();
    }
    
    /**
     * Simple Test case for setGrammarContent Methode.
     * which uses the Sapi5 Compiler
     * Use only one Grammar
     * 
     * @throws Exception
     *         test failed.
     */
    @Test
    public void testSetGrammarContent() throws Exception {
                
        recognizer.addResultListener(this);
        
        byte[] buffer = new byte[(int) new File("./unittests/org/jvoicexml/jsapi2/sapi/recognition/hello.xml").length()];
        BufferedInputStream f = new BufferedInputStream(new FileInputStream("./unittests/org/jvoicexml/jsapi2/sapi/recognition/hello.xml"));
        f.read(buffer);
        f.close();
        
        String grammar = new String(buffer);
        
        //System.out.println( grammar );

        if( ((SapiRecognizer)recognizer).setGrammarContent(grammar, "foobar")  ){
            
            SapiRecognitionThread recognitionThread = new SapiRecognitionThread( ( ( SapiRecognizer )recognizer) );
            recognitionThread.start();
            
            System.out.println("Test1 Please say something...");      
            
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
        else
        {
         System.err.println( "Activating Grammar failed" );
        }
        
    }


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
        if (event.getId() == ResultEvent.RESULT_REJECTED) {
            
            recognizer.pause();
            recognizer.requestFocus();
            recognizer.resume();
            System.out.println("Recognition confidence was too low. Please try again ...");            
        }    
    }

    /**
     * Test method for {@link SapiRecognizer#reportResult(String, String)}.
     * @exception Exception
     *            test failed
     */
    @Test
    public void testReportResult() throws Exception {
        recognizer.addResultListener(this);
        final GrammarManager manager = recognizer.getGrammarManager();
        final String ruleName = "test";
        final RuleGrammar grammar =  manager.createRuleGrammar("grammar:test",
                ruleName);
        final RuleComponent hello = new RuleToken("Hello Dirk");
        final Rule rule = new Rule("test", hello);
        grammar.addRule(rule);
        final String utterance = load("sml-simple.xml");
        final SapiRecognizer sapiRecognizer = (SapiRecognizer) recognizer;
        recognizer.addResultListener(this);
        sapiRecognizer.reportResult(ruleName, utterance);
        synchronized (lock) {
            lock.wait();
        }
        String words = "";
        final ResultToken[] tokens = result.getBestTokens();
        for (int i = 0; i < tokens.length; i++) {
            words += tokens[i].getText() + " ";
        }
        Assert.assertEquals("Hello Dirk ", words);
    }

    /**
     * Test method for {@link SapiRecognizer#reportResult(String, String)}.
     * @exception Exception
     *            test failed
     */
    @Test
    public void testReportResultMultipleTags() throws Exception {
        recognizer.addResultListener(this);
        final GrammarManager manager = recognizer.getGrammarManager();
        final String ruleName = "test";
        final RuleGrammar grammar =  manager.createRuleGrammar("grammar:test",
                ruleName);
        final RuleComponent hello = new RuleToken("Hello");
        final RuleTag helloTag = new RuleTag("out=\"general\";");
        final Rule rule = new Rule("test", hello);
        grammar.addRule(rule);
        final String utterance = load("sml-multiple-tags.xml");
        final SapiRecognizer sapiRecognizer = (SapiRecognizer) recognizer;
        recognizer.addResultListener(this);
        sapiRecognizer.reportResult(ruleName, utterance);
        synchronized (lock) {
            lock.wait();
        }
        String words = "";
        final ResultToken[] tokens = result.getBestTokens();
        for (int i = 0; i < tokens.length; i++) {
            words += tokens[i].getText() + " ";
        }
        Assert.assertEquals("Hello Dirk ", words);
}
    
    /**
     * Loads the given resource as a string.
     * @param resource the name of the resource to load
     * @return contents of the resource
     * @throws IOException
     *         error loading the resource
     */
    private String load(final String resource) throws IOException {
        final InputStream in = 
                TestRecognizer.class.getResourceAsStream("sml-simple.xml");
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int num;
        byte[] buffer = new byte[1024];
        do {
            num = in.read(buffer);
            if (num > 0) {
                out.write(buffer, 0, num);
            }
        } while (num >= 0);
        return out.toString();
    }
}
