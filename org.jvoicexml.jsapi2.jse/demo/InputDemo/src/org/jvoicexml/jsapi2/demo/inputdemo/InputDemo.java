/**
 * 
 */
package org.jvoicexml.jsapi2.demo.inputdemo;

import java.io.InputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.speech.EngineManager;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.RecognizerMode;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultListener;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerMode;

import org.jvoicexml.jsapi2.jse.recognition.sphinx4.SphinxEngineListFactory;
import org.jvoicexml.jsapi2.jse.synthesis.freetts.FreeTTSEngineListFactory;

/**
 * @author Dirk Schnelle-Walka
 *
 */
public class InputDemo implements ResultListener {

    /**
     * @param args
     */
    public static void main(String[] args) {
        InputDemo demo = new InputDemo();

        // Enable logging at all levels.
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        Logger.getLogger("").addHandler(handler);
        Logger.getLogger("").setLevel(Level.ALL);

        try {
            EngineManager.registerEngineListFactory(
                    FreeTTSEngineListFactory.class.getName());
            System.setProperty("javax.speech.supports.audio.management",
                    Boolean.TRUE.toString());
            System.setProperty("javax.speech.supports.audio.capture",
                    Boolean.TRUE.toString());
            // Create a synthesizer for the default Locale
            Synthesizer synth = (Synthesizer) EngineManager
                    .createEngine(SynthesizerMode.DEFAULT);

            EngineManager.registerEngineListFactory(
                    SphinxEngineListFactory.class.getName());

            // Get it ready to speak
            synth.allocate();

            Recognizer recognizer = (Recognizer) EngineManager.createEngine(
                    RecognizerMode.DEFAULT);

            recognizer.allocate();
            recognizer.addResultListener(demo);

            GrammarManager grammarManager = recognizer.getGrammarManager();
            InputStream in = InputDemo.class.getResourceAsStream("hello.xml");
            grammarManager.loadGrammar("grammar:greeting", null, in, "UTF-8");
            // Speak the "hello world" string
            synth.speak("Please say something", null);
            synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
            System.out.println("Please say something...");

            recognizer.requestFocus();
            recognizer.resume();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void resultUpdate(ResultEvent e) {
        System.out.println(e);
    }

}
