package org.jvoicexml.jsapi2.sapi.recognition;

import javax.speech.AudioEvent;
import javax.speech.AudioListener;
import javax.speech.Engine;

/**
 * This AudioEventListener keeps track of the various audioevents happening during runtime.
 * If changes to the inputStream are made, the listener adds a pause() to the Recognizer's 
 * pause-counter. This ensures, that the associated recognizer can only be resumed AFTER 
 * all changes to the inputStream are fully committed.
 * 
 * @author Markus Baumgart <info@CIBEK.de>
 *
 */
public class SapiRecognizerAudioEventListener implements AudioListener {

    /**
     * The associated recognizer
     */
    private SapiRecognizer recognizer;
    
    /**
     * Keeps track of the audioinput's changes.<br>
     * Between an <code>AudioManager.audioStop()</code> and <code>AudioManager.audioStart()</code> 
     * the mediaLocator could be changed  many times.
     * This AudioEventLister must not hold more than one pause on the stack.
     */
    private boolean audioChanged;
    
    public SapiRecognizerAudioEventListener(SapiRecognizer recognizer) {
        this.recognizer = recognizer;
        audioChanged = false;
    }
    
    @Override
    public void audioUpdate(AudioEvent e) {
        switch(e.getId()) {
            case AudioEvent.AUDIO_CHANGED:
                System.out.println("AudioEvent: Audio Changed!");
                if (!audioChanged) {
                    recognizer.pause();
                    audioChanged = true;
                }
                break;
            case AudioEvent.AUDIO_STARTED:
                System.out.println("AudioEvent: Audio Started!");
                
                if (recognizer.testEngineState(Engine.ALLOCATING_RESOURCES)) {
                    try {
                        recognizer.waitEngineState(Engine.ALLOCATED);
                        recognizer.waitEngineState(Engine.PAUSED);
                        recognizer.pause();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                
                // tell the recognizer to get the new InputStream and set it as it's new source
                boolean inputStreamSet = recognizer.setRecognizerInputStream();
                System.out.println("New InputStream set: " + inputStreamSet);
                if (!(recognizer.testEngineState(Engine.DEALLOCATED) ||
                        recognizer.testEngineState(Engine.DEALLOCATING_RESOURCES))) {
                    recognizer.resume();
                }
                break;
            default:
                //not interested
        }
    }
}
