package org.jvoicexml.jsapi2.sapi.recognition;

/**
 * Perform the recognition in an own java process.
 * @author Josua Arndt
 * @author Dirk Schnelle-Walka
 *
 */
final class SapiRecognitionThread extends Thread {
    static {
        System.loadLibrary("Jsapi2SapiBridge");
    }

    /** The calling SapiRecognizer.  **/
    private SapiRecognizer recognizer;
   
    /**
     * Constructs a new object.
     * @param rec the calling recognizer
     */
    public SapiRecognitionThread(final SapiRecognizer rec) {
        recognizer = rec;
    }
    
    /**
     * {@inheritDoc}
     */
    public void run() {
        String utterance = sapiRecognize(recognizer.getRecognizerHandle());    
        recognizer.reportResult(utterance);
    }

    /**
     * Start recognition.
     * @param recognizerHandle the recognizer handle
     * @return recognition result
     */
    private native String sapiRecognize(final long recognizerHandle);
}
