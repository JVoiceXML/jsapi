/**
 * 
 */
package org.jvoicexml.jsapi2.demo.inputdemo;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.speech.EngineManager;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.RecognizerMode;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerMode;

import org.jvoicexml.jsapi2.jse.recognition.sphinx4.SphinxEngineListFactory;
import org.jvoicexml.jsapi2.jse.synthesis.freetts.FreeTTSEngineListFactory;

/**
 * @author Dirk Schnelle-Walka
 *
 */
public class InputDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
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

            // Speak the "hello world" string
            System.out.println("Please say something...");
            synth.speak("Please say something", null);
            synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
            System.out.println("done.");

            // Clean up - includes waiting for the queue to empty
            synth.deallocate();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
