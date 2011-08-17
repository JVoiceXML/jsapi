package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Perform the recognition in an own java process.
 * @author Josua Arndt
 * @author Dirk Schnelle-Walka
 *
 */
final class SapiRecognitionThread extends Thread {
    /** Logger for this class. */
    private static final Logger LOGGER =
        Logger.getLogger(SapiRecognitionThread.class.getName());

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
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Starting recognition process");
        }
        final String tmp[] = recognizer.sapiRecognize(handle);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Recognitionprocess ended");
        }

        //copy the result into a local variable and notify the recognizer
        final String result[];
        if (tmp == null) {
            result = null;
        } else {
            result = Arrays.copyOf(tmp, 2);
        }
        recognizer.reportResult(result);
    }

    /**
     * Stops the recognition process.
     */
    public void stopRecognition() {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Issuing Interrupt to Recognition-Thread...");
        }
        interrupt();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("...issued Interrupt to Recognition-Thread");
        }
        final long handle = recognizer.getRecognizerHandle();
        recognizer.sapiAbortRecognition(handle);
    }
}
