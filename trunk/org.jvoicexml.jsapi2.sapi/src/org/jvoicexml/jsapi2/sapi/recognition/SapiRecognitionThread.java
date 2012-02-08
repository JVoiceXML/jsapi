package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.speech.EngineException;

/**
 * Perform the recognition in an own java process.
 * @author Josua Arndt
 * @author Dirk Schnelle-Walka
 *
 */
final class SapiRecognitionThread extends Thread {
    final static int RECOGNITION_SUCCESSFULL = 0;       //S_OK
    final static int RECOGNITION_NOMATCH = 43;          //SPEVENTENUM::SPEI_FALSE_RECOGNITION
    final static int RECOGNITION_ABORTED = 1;           //S_FALSE

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
        final String[] tmp = {null, null};
        int returnValue = -1;
        try {
            returnValue = recognizer.sapiRecognize(handle, tmp);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Recognitionprocess ended");
            }
        } catch (EngineException e) {
            LOGGER.log(Level.WARNING, "Error in recognition process: {0}",
                    e.getMessage());
            recognizer.postEngineException(e);
            return;
        }

        switch (returnValue) {
        case RECOGNITION_NOMATCH:
            recognizer.reportResultRejected();
            break;
        case RECOGNITION_SUCCESSFULL:
            final String ruleName = tmp[0];
            final String utterance = tmp[1];
            recognizer.reportResult(ruleName, utterance);
            break;
        case RECOGNITION_ABORTED:
            break;
        default:
            LOGGER.log(Level.FINE, "Unknown returnvalue: {0}",
                    returnValue);
            break;
        }
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
