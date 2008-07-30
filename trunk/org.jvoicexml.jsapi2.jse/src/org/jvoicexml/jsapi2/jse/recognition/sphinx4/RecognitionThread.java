/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $Date$
 * Author:  $LastChangedBy$
 *
 * JVoiceXML - A free VoiceXML implementation.
 *
 * Copyright (C) 2005-2008 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.jsapi2.jse.recognition.sphinx4;


import java.util.logging.Level;
import java.util.logging.Logger;

import edu.cmu.sphinx.frontend.DataProcessor;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;

/**
 * Recognition thread to run the recognizer in parallel.
 *
 * @author Dirk Schnelle
 * @version $Revision$
 *
 * <p>
 * Copyright &copy; 2005-2008 JVoiceXML group -
 * <a href="http://jvoicexml.sourceforge.net">
 * http://jvoicexml.sourceforge.net/</a>
 * </p>
 */
final class RecognitionThread
        extends Thread {
    /** Logger for this class. */
    private static final Logger LOGGER =
            Logger.getLogger(RecognitionThread.class.getName());

    /** The wrapper for the sphinx4 recognizer. */
    private Sphinx4Recognizer recognizer;

    /**
     * Creates a new object.
     * @param rec The wrapper for the sphinx4 recognizer.
     */
    public RecognitionThread(final Sphinx4Recognizer rec) {
        super("RecognitionThread");
        recognizer = rec;
        setDaemon(true);
    }

    /**
     * Runs this thread.
     */
    public void run() {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("recognition thread started");
        }

        final Recognizer rec = recognizer.getRecognizer();
        final SphinxInputDataProcessor inputData = getInputData();
        final boolean started;

        /*if (microphone != null) {
            microphone.clear();
            started = microphone.startRecording();
        } else {
            started = true;
        }*/
        started = true;

        // send start of speach and processing event
        // @todo change this;
        recognizer.postStartOfSpeechEvent();
        recognizer.postProcessingEvent();
        if (started) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("start recognizing ..");
            }

            recognize(rec, inputData);
        }
        // send end of speach and listening event
        // @todo change this;
        recognizer.postEndOfSpeechEvent();
        recognizer.postListeningEvent();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("stopping recognition thread...");
        }

       /* if (microphone != null) {
            // Stop recording from the microphone.
            while (microphone.isRecording()) {
                microphone.stopRecording();
            }
        }*/
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("recognition thread terminated");
        }
    }

    /**
     * Recognition loop. Continue recognizing until this thread is
     * requested to stop.
     * @param rec The recognizer to use.
     * @param mic The microphone to use.
     */
    private void recognize(final Recognizer rec, final Microphone mic) {
        while (hasMoreData(mic) && !isInterrupted()) {
            try {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("recognizing...");
                    String [] grammars =
                        recognizer.getRuleGrammar().listRuleNames();
                    LOGGER.fine("RuleGrammars that will be used:");
                    for (int i = 0; i < grammars.length; i++) {
                        LOGGER.fine("grammar: '" + grammars[i].toString()
                                + "'");
                    }
                }

                rec.recognize();
            } catch (IllegalArgumentException iae) {
                LOGGER.fine("unmatched utterance " + iae.getMessage());
            }
        }
    }

    private void recognize(final Recognizer rec,
                           final SphinxInputDataProcessor inputData) {
        rec.recognize();
    }

    /**
     * Checks, if the emicrophone has more data to deliver.
     * @param mic The microphone or <code>null</code> if the data processor
     * is not a microphone.
     * @return <code>true</code> if there is more data.
     */
    private boolean hasMoreData(final Microphone mic) {
        if (mic == null) {
            return true;
        }

        return mic.hasMoreData();
    }

    /**
     * Stop this recognition thread.
     */
    public void stopRecognition() {
        final Microphone microphone = getMicrophone();
        if (microphone != null) {
            microphone.stopRecording();
        }

        Thread thread = currentThread();
        thread.interrupt();
    }

    /**
     * Retrieves the microphone.
     * @return The microphone, <code>null</code> if the data processor is
     * not a microphone.
     * @since 0.5.5
     */
    private Microphone getMicrophone() {
        final DataProcessor dataProcessor = recognizer.getDataProcessor();
        if (dataProcessor instanceof Microphone) {
            return (Microphone) dataProcessor;
        }

        return null;
    }

    private SphinxInputDataProcessor getInputData() {
        final DataProcessor dataProcessor = recognizer.getDataProcessor();
         if (dataProcessor instanceof SphinxInputDataProcessor) {
             return (SphinxInputDataProcessor) dataProcessor;
         }

         return null;
    }
}
