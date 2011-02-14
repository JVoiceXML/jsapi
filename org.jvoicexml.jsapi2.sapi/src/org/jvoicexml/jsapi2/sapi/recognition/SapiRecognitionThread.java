package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Arrays;

/**
 * Perform the recognition in an own java process.
 * @author Josua Arndt
 * @author Dirk Schnelle-Walka
 *
 */
final class SapiRecognitionThread extends Thread {
    /** The calling SapiRecognizer.  **/
    private SapiRecognizer recognizer;
   
    /**
     * Constructs a new object.
     * @param rec the calling recognizer
     */
    public SapiRecognitionThread(final SapiRecognizer rec) {
        recognizer = rec;
        setDaemon(true);
    }
    
    /**
     * {@inheritDoc}
     */
    public void run() {
        final long handle = recognizer.getRecognizerHandle();
        //start recognition and get the recognition result
        System.out.println("--RecognitionThread: Starting recognition process");
        String tmp[] = recognizer.sapiRecognize(handle);
        System.out.println("++RecognitionThread: Recognitionprocess ended");
        
        //copy the result into a local variable and notify the recognizer
        String result[] = (tmp==null ? null : Arrays.copyOf(tmp, 2));
        recognizer.reportResult(result);
    }

    /**
     * Stops the recognition process.
     */
    public void stopRecognition() {
        System.out.println("--Recognizer: Issuing Interrupt to Recognition-Thread");
        interrupt();
        System.out.println("++Recognizer: Issuing Interrupt to Recognition-Thread -- DONE!");
        final long handle = recognizer.getRecognizerHandle();
        recognizer.sapiAbortRecognition(handle);
    }
}
