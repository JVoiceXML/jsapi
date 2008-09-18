/**
 * 
 */
package org.jvoicexml.jsapi2.demo.helloworld.HelloWorld;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.speech.EngineManager;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerMode;

import org.jvoicexml.jsapi2.jse.synthesis.freetts.FreeTTSEngineListFactory;

/**
 * @author DS01191
 * 
 */
public class HelloWorldDemo {
    public static void main(String args[]) {
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        Logger.getLogger("com.sun").addHandler(handler);
        Logger.getLogger("com.sun").setLevel(Level.ALL);
        try {
            EngineManager
                    .registerEngineListFactory(FreeTTSEngineListFactory.class
                            .getName());
            System.setProperty("javax.speech.supports.audio.management",
                    Boolean.TRUE.toString());
            // Create a synthesizer for the default Locale
            Synthesizer synth = (Synthesizer) EngineManager
                    .createEngine(SynthesizerMode.DEFAULT);

            // Get it ready to speak
            synth.allocate();

            // Speak the "hello world" string
            System.out.println("Speaking 'Hello, world!'...");
            synth.speak("Hello, world!", null);
            synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
//            Thread.sleep(3000);
            System.out.println("done.");

            // Clean up - includes waiting for the queue to empty
            synth.deallocate();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}