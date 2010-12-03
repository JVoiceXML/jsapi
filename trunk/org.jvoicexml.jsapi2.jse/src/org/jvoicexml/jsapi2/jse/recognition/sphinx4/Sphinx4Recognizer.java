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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.recognition.RecognizerEvent;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.RuleGrammar;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestEvent;
import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.JseBaseAudioManager;
import org.jvoicexml.jsapi2.jse.recognition.GrammarDefinition;
import org.jvoicexml.jsapi2.jse.recognition.JseBaseRecognizer;

import edu.cmu.sphinx.frontend.DataProcessor;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.util.props.ConfigurationManager;

/**
 * JSAPI wrapper for sphinx4.
 *
 * <p>
 * Unfortunately sphinx4 provides no full support for JSAPI, so we try to
 * build our own wrapper. This is going to be a bit troublesome. Hope we
 * can make it ;-)
 * </p>
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
final class Sphinx4Recognizer extends JseBaseRecognizer
    implements EnginePropertyChangeRequestListener {
    /** Logger for this class. */
    private static final Logger LOGGER =
            Logger.getLogger(Sphinx4Recognizer.class.getName());

    /** Msecs to sleep before the status of the recognizer is checked again. */
    private static final long SLEEP_MSEC = 50;

    /** The encapsulated recognizer. */
    private Recognizer recognizer;

    /** The input device. */
    private DataProcessor dataProcessor;

    /** The grammar manager. */
    private SRGSGrammar grammar;

    /** The result listener. */
    private final Sphinx4ResultListener resultListener;

    /**
     * The decoding thread. It points either to the single decoding thread
     * or is <code>null</code> if no recognition thread is started.
     */
    private RecognitionThread recognitionThread;

    /**
     * Construct a new object.
     */
    public Sphinx4Recognizer(SphinxRecognizerMode recognizerMode) {
        super(recognizerMode);

        String configFile = System.getProperty(
                "org.jvoicexml.jsapi2.jse.recognition.sphinx4.configPath",
        		"/sphinx4.config.xml");

        URL url = Sphinx4Recognizer.class.getResource(configFile);
        if (url == null) {
            LOGGER.info("Using default configuration.");
            url = Sphinx4Recognizer.class.getResource("default.config.xml");
        }
        
        try {
            final ConfigurationManager configuration =
                    new ConfigurationManager(url);

            recognizer = (Recognizer) configuration.lookup("recognizer");
            dataProcessor = (DataProcessor) configuration.lookup("sphinxInputDataProcessor");
            grammar = (SRGSGrammar) configuration.lookup("srgsGrammar");

            if (!(dataProcessor instanceof SphinxInputDataProcessor)) {
                throw new EngineException("Unsupported input type");
            }

        } catch (Exception ex) {
            LOGGER.warning("error creating engine properties "
                    + ex.getMessage());
            ex.printStackTrace();
        }

        ((JseBaseAudioManager)getAudioManager()).setEngineAudioFormat(new AudioFormat(16000, 16, 1, true, true));
        resultListener = new Sphinx4ResultListener(this);
    }

    /**
     * Called from the <code>resume</code> method.
     */
    public boolean handleResume() {
        if (recognizer == null) {
            LOGGER.warning("no recognizer: cannot resume!");
            return false;
        }

        if (recognitionThread != null) {
            LOGGER.fine("recognition thread already started.");
            return false;
        }

        recognitionThread = new RecognitionThread(this);
        recognitionThread.start();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("recognition started");
        }

        return true;
    }

    /**
     * Called from the <code>pause</code> method.
     */
    public void handlePause() {
        if (recognitionThread == null) {
            throw new EngineStateException(
                    "Cannot pause, no decoder started");
        }

        stopRecognitionThread();
        if (dataProcessor instanceof Microphone) {
            final Microphone microphone = (Microphone) dataProcessor;
            microphone.stopRecording();
        }
    }

    /**
     * @todo Correctly implement this
     */
     public void handlePause(int flags) {
         handlePause();
     }

    /**
     * Called from the <code>allocate</code> method.
     *
     * @throws EngineException if problems are encountered
     */
    public void handleAllocate() throws AudioException, EngineException,
        EngineStateException, SecurityException {
        if (recognizer == null) {
            throw new EngineException(
                    "cannot allocate: no recognizer configured!");
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("allocating recognizer...");
        }

        //Get and set input
        InputStream inputStream = ((JseBaseAudioManager)getAudioManager()).getInputStream();

        ((SphinxInputDataProcessor)dataProcessor).setInputStream(inputStream);

        recognizer.allocate();
        recognizer.addResultListener(resultListener);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("...allocated");
            LOGGER.fine("state: " + recognizer.getState());
        }

        setEngineState(CLEAR_ALL_STATE, ALLOCATED);
    }

    /**
     * Called from the <code>deallocate</code> method.
     *
     * @throws EngineException if this <code>Engine</code> cannot be
     *   deallocated.
     * @todo Implement this com.sun.speech.engine.BaseEngine method
     */
    public void handleDeallocate() {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("deallocating recognizer...");
        }

        // Stop the decoder thread.
        stopRecognitionThread();

        // Deallocate the recognizer and wait until it stops recognizing.
        recognizer.deallocate();
        while (recognizer.getState() == Recognizer.State.RECOGNIZING) {
            try {
                Thread.sleep(SLEEP_MSEC);
            } catch (InterruptedException ie) {
                LOGGER.warning("error waiting for recognizer to deallocate "
                        + ie.getMessage());
            }
        }
        recognizer.resetMonitors();
   //////////////////////////////     recognizer.removeResultListener(resultListener);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("...deallocated");
        }
    }

    /**
     * Load a RuleGrammar and its imported grammars from a URL containing
     * JSGF text.
     * From javax.speech.recognition.Recognizer.
     * @param url the base URL containing the JSGF grammar file.
     * @param name the name of the JSGF grammar to load.
     * @return Loaded grammar.
     *
     * @exception GrammarException
     *            Error in the grammar.
     * @exception IOException
     *            Error reading the grammar.
     */
   /* @Override
    public RuleGrammar loadJSGF(final URL url, final String name)
            throws GrammarException, IOException {
        grammar.loadJSGF(name);
        return super.loadJSGF(url, name);
    }*/

    /**
     * Selector for the data processor.
     * @return The used data processor.
     */
    DataProcessor getDataProcessor() {
        return dataProcessor;
    }

    /**
     * Selector for the wrapped sphinx4 recognizer.
     * @return Recognizer
     */
    Recognizer getRecognizer() {
        return recognizer;
    }

    /**
     * Stop the recognition thread and wait until it is terminated.
     */
    private void stopRecognitionThread() {
        if (recognitionThread == null) {
            LOGGER.fine("recognition thread already stopped");
            return;
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("stopping recognition thread...");
        }
        recognitionThread.stopRecognition();

        final long maxSleepTime = 5000;
        long sleepTime = 0;

        while (recognitionThread.isAlive() && (sleepTime < maxSleepTime)) {
            try {
                Thread.sleep(SLEEP_MSEC);
                sleepTime += SLEEP_MSEC;
            } catch (InterruptedException ie) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("recognition thread interrupted");
                }
            }
        }

        recognitionThread = null;

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("recognition thread stopped");
        }
    }

    /**
     * Get the current rule grammar.
     * @return Active grammar.
     */
    RuleGrammar getRuleGrammar() {
        return grammar.getRuleGrammar();
    }


    /**
     * @todo: in case of grammarDefinition.size > 1, make <one-of> of
     * all the grammars
     * @param newGrammars String[]
     * @return boolean
     */
    protected boolean setGrammars(Vector grammarDefinition) {
        if (grammarDefinition.size() == 1) {
            try {
                grammar.loadSRGS(((GrammarDefinition) grammarDefinition.get(0)).
							getGrammar());
            } catch (IOException ex) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getBuiltInGrammars() {
        return new Vector();
    }

    public void postStartOfSpeechEvent(){
        postEngineEvent(new RecognizerEvent(this,RecognizerEvent.SPEECH_STARTED,1,1,null,null,0));
    }

    public void postEndOfSpeechEvent(){
        postEngineEvent(new RecognizerEvent(this,RecognizerEvent.SPEECH_STOPPED,1,1,null,null,0));
    }

    public void postProcessingEvent(){
        long states[] = setEngineState(LISTENING, PROCESSING);
        postEngineEvent(states[0], states[1],
                        RecognizerEvent.RECOGNIZER_PROCESSING,(long)0);
    }

    public void postListeningEvent(){
        long states[] = setEngineState(PROCESSING, LISTENING);
        postEngineEvent(states[0], states[1],RecognizerEvent.RECOGNIZER_LISTENING,(long)0);

    }

    /**
     * {@inheritDoc}
     */
    public void postResultEvent(final ResultEvent resultEvent){
        super.postResultEvent(resultEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChangeRequest(EnginePropertyChangeRequestEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void handleRequestFocus() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void handleReleaseFocus() {
        // TODO Auto-generated method stub
        
    }

}
