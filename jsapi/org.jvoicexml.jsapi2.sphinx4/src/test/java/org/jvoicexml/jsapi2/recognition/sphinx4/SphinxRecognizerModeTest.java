package org.jvoicexml.jsapi2.recognition.sphinx4;

import javax.sound.sampled.AudioFileFormat.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.speech.SpeechLocale;

import org.junit.Assert;
import org.junit.Test;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

/**
 * Test cases for {@link SphinxRecognizerMode}.
 * 
 * @author Dirk Schnelle-Walka
 */
public class SphinxRecognizerModeTest {
    /**
     * Test method to create an engine.
     * 
     * @throws Exception
     *             test failed
     */
    @Test
    public void testCreateEngine() throws Exception {

        Configuration configuration = new Configuration();

        // Set path to acoustic model.
        configuration.setAcousticModelPath(
                "resource:/edu/cmu/sphinx/models/en-us/en-us");
        // Set path to dictionary.
        configuration.setDictionaryPath(
                "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        // Set language model.
        configuration.setLanguageModelPath(
                "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(
                configuration);
        

        AudioFormat format = new AudioFormat(16000, 16, 2, true, false);
        TargetDataLine lineLocalMic = AudioSystem.getTargetDataLine(format);
        AudioInputStream input = new AudioInputStream(lineLocalMic);
        AudioFormat recformat = new AudioFormat(16000, 16, 1, true, false);
        AudioInputStream recinput = AudioSystem.getAudioInputStream(recformat, input);
        lineLocalMic.open();

//        PipedOutputStream pout = new PipedOutputStream();
//        PipedInputStream pin = new PipedInputStream(pout);
//        FileOutputStream fout = new FileOutputStream("test.wav");
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("start speaking..");
//                WaveHeader header = new WaveHeader(recformat);
//                try {
//                    header.write(pout);
//                    header.write(fout);
//                    byte[] BUFFER = new byte[1024];
//                    int read = 0;
//                    do {
//                        read = recinput.read(BUFFER);
//                        if (read > 0) {
//                            pout.write(BUFFER, 0, read);
//                            fout.write(BUFFER, 0, read);
//                            pout.flush();
//                            fout.flush();
//                        }
//                    } while (read > 0);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                
//            }
//        });
        // Start recognition process pruning previously cached data.
        recognizer.startRecognition(recinput);
        Thread micThread = new Thread(new Runnable() {
            @Override
            public void run() {
                lineLocalMic.start();
                System.out.println("say something");
                try {
                    Thread.sleep(5000);
                    System.out.println("done");
//                    recinput.close();
//                    input.close();
//                    lineLocalMic.stop();
//                    lineLocalMic.close();
                } catch (InterruptedException e) {
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
                }
            }
        });
        micThread.start();
//        thread.start();
        SpeechResult result = recognizer.getResult();
        System.out.println(result.getResult().getBestResultNoFiller());
        // Pause recognition process. It can be resumed then with
        // startRecognition(false).
        recognizer.stopRecognition();

//        fout.close();
        final SphinxRecognizerMode mode = new SphinxRecognizerMode(
                SpeechLocale.ENGLISH);
        Assert.assertEquals(Sphinx4Recognizer.class,
                mode.createEngine().getClass());
    }
}