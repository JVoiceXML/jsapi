/*
 * File:    $HeadURL: https://jsapi.svn.sourceforge.net/svnroot/jsapi/trunk/org.jvoicexml.jsapi2.jse/demo/HelloWorld/src/org/jvoicexml/jsapi2/demo/helloworld/HelloWorldDemo.java $
 * Version: $LastChangedRevision: 593 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An base implementation for JSR 113.
 *
 * Copyright (C) 2009 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2.demo.helloworld;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.speech.EngineManager;
import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerEvent;
import javax.speech.synthesis.SynthesizerListener;
import javax.speech.synthesis.SynthesizerMode;

import org.jvoicexml.jsapi2.jse.synthesis.freetts.FreeTTSEngineListFactory;

/**
 * A demo to output a synthesized text to the speaker.
 * @author Dirk Schnelle-Walka
 * @version $Revision: 593 $
 */
public final class HelloWorldDemo implements SpeakableListener, SynthesizerListener {
    /**
     * Do not create from outside.
     */
    private HelloWorldDemo() {
    }

    /**
     * Starts this demo.
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        // Enable logging at all levels.
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        Logger.getLogger("").addHandler(handler);
        Logger.getLogger("").setLevel(Level.ALL);

        try {
            EngineManager
                    .registerEngineListFactory(FreeTTSEngineListFactory.class
                            .getName());
            System.setProperty("javax.speech.supports.audio.management",
                    Boolean.TRUE.toString());
            System.setProperty("javax.speech.supports.audio.capture",
                    Boolean.TRUE.toString());
            // Create a synthesizer for the default Locale
            Synthesizer synth = (Synthesizer) EngineManager
                    .createEngine(SynthesizerMode.DEFAULT);
            HelloWorldDemo demo = new HelloWorldDemo();
            synth.addSynthesizerListener(demo);
            // Get it ready to speak
            synth.allocate();

            // Speak the "hello world" string
            System.out.println("Speaking 'Hello, world!'...");
            synth.speak("Hello, world!", demo);
            synth.speakMarkup("<?xml version=\"1.0\"?>"
                    + "<speak>Goodbye!</speak>", demo);
            synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
            System.out.println("done.");

            // Clean up - includes waiting for the queue to empty
            synth.deallocate();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void speakableUpdate(SpeakableEvent e) {
        System.out.println(e);
    }

    @Override
    public void synthesizerUpdate(SynthesizerEvent e) {
        System.out.println(e);
    }
}
