package org.jvoicexml.jsapi2.sapi.recognition;

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
        //String utterance = recognizer.sapiRecognize(handle);
        //recognizer.reportResult(utterance);
        String result[] = recognizer.sapiRecognize(handle);
        recognizer.reportResult(result);
    }

    /**
     * Stops the recognition process.
     */
    public void stopRecognition() {
        interrupt();
        final long handle = recognizer.getRecognizerHandle();
        recognizer.sapiAbortRecognition(handle);
    }
}
