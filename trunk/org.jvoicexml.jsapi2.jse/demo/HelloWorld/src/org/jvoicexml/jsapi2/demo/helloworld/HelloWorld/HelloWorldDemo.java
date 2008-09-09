/**
 * 
 */
package org.jvoicexml.jsapi2.demo.helloworld.HelloWorld;

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
        try {
        	EngineManager.registerEngineListFactory(
        			FreeTTSEngineListFactory.class.getName());
            // Create a synthesizer for the default Locale
            Synthesizer synth = (Synthesizer)
                EngineManager.createEngine(SynthesizerMode.DEFAULT);
            
            // Get it ready to speak
            synth.allocate();

            // Speak the "hello world" string
            synth.speak("Hello, world!", null);

            // Clean up - includes waiting for the queue to empty
            synth.deallocate();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
