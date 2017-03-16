/*
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2017 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.jsapi2.recognition.sphinx4;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.SpeechLocale;
import javax.speech.recognition.RecognizerEvent;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.RuleGrammar;

import org.jvoicexml.jsapi2.BaseAudioManager;
import org.jvoicexml.jsapi2.BaseEngineProperties;
import org.jvoicexml.jsapi2.recognition.BaseRecognizer;
import org.jvoicexml.jsapi2.recognition.GrammarDefinition;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.Context;
import edu.cmu.sphinx.decoder.search.Token;
import edu.cmu.sphinx.linguist.language.grammar.Grammar;

/**
 * JSAPI wrapper for sphinx4.
 * 
 * <p>
 * Unfortunately sphinx4 provides no full support for JSAPI, so we try to build
 * our own wrapper. This is going to be a bit troublesome. Hope we can make it
 * ;-)
 * </p>
 * 
 * @author Dirk Schnelle
 * @author Stefan Radomski
 * @author Stephan Radeck-Arneth (adaptation for handling of different
 *         languages)
 */
final class Sphinx4Recognizer extends BaseRecognizer {
    /** Logger for this class. */
    private static final Logger LOGGER = Logger
            .getLogger(Sphinx4Recognizer.class.getName());

    /**
     * Msecs to sleep before the status of the recognizer thread is checked
     * again.
     */
    private static final long SLEEP_MSEC = 50;

    /** The encapsulated recognizer. */
    private Jsapi2Recognizer recognizer;

    /** The grammar manager. */
    private Grammar grammar;

    private Configuration configuration;

    /** The result listener. */
    private final Sphinx4ResultListener resultListener;

    /**
     * The decoding thread. It points either to the single decoding thread or is
     * <code>null</code> if no recognition thread is started.
     */
    private RecognitionThread recognitionThread;

    /** Possible error in the constructor. */
    private Exception error;

    /**
     * Construct a new object.
     */
    public Sphinx4Recognizer(SphinxRecognizerMode recognizerMode) {
        super(recognizerMode);
        // First check the system setting that may override any other setting
        String resource = System.getProperty(
                "org.jvoicexml.jsapi2.recognition.sphinx4.configPath");
        // There is no config, call dynamic URL handler
        if (resource == null) {
            resource = getConfiguration(recognizerMode);
        }
        configuration = new Configuration();

        // Set path to acoustic model.
        configuration.setAcousticModelPath(
                "resource:/edu/cmu/sphinx/models/en-us/en-us");
        // Set path to dictionary.
        configuration.setDictionaryPath(
                "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        // Set language model.
        configuration.setLanguageModelPath(
                "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        try {
            Context context = new Context("resource:/default-EN.config.xml",
                    configuration);
            recognizer = new Jsapi2Recognizer(context);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "error creating engine properties {0}",
                    e.getMessage());
            error = e;
        }

        // hard-coded audio format
        final AudioFormat format = getAudioFormat();
        ((BaseAudioManager) getAudioManager()).setEngineAudioFormat(format);
        resultListener = new Sphinx4ResultListener(this);
    }

    /**
     * Determine a configuration file from the
     * {@link javax.speech.recognition.RecognizerMode}.
     * <p>
     * The name of the configuration file is determined by
     * <code>default-&lt;LOCALE LANGUAGE&gt;.config.xml</code>
     * </p>
     * 
     * @param recognizerMode
     *            the recognizer mode
     * @return URL of the configuration file to use.
     */
    private String getConfiguration(final SphinxRecognizerMode recognizerMode) {
        // Determine the speech locale to use
        SpeechLocale[] speechLocales = recognizerMode.getSpeechLocales();
        if (speechLocales == null
                && recognizerMode.getSpeakerProfiles() != null) {
            speechLocales = recognizerMode.getSpeechLocales();
        }

        // None given: use default
        if (speechLocales == null) {
            LOGGER.info("Sphinx4Recognizer using default configuration.");
            return "resource:/default-EN.config.xml";
        }

        // Determine the name from the locale
        final SpeechLocale speechLocale = speechLocales[0];
        final String selectedLanguage = speechLocale.getLanguage();
        return "resource:/default-" + selectedLanguage.toUpperCase()
                + ".config.xml";
    }

    /**
     * Called from the <code>allocate</code> method.
     * 
     * @throws EngineException
     *             if problems are encountered
     */
    public void handleAllocate() throws AudioException, EngineException,
            EngineStateException, SecurityException {
        if (error != null) {
            throw new EngineException(error.getMessage());
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("allocating recognizer...");
        }
        configuration = new Configuration();

        // Set path to acoustic model.
        configuration.setAcousticModelPath(
                "resource:/edu/cmu/sphinx/models/en-us/en-us");
        // Set path to dictionary.
        configuration.setDictionaryPath(
                "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        // Set language model.
        configuration.setLanguageModelPath(
                "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        try {
            final Context context = new Context(
                    "resource:/default-EN.config.xml", configuration);
            recognizer = new Jsapi2Recognizer(context);
            recognizer.allocate();
        } catch (IOException e) {
            throw new EngineException(e.getMessage());
        }

        // Register result listener
        recognizer.addResultListener(resultListener);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("...allocated");
        }

        setEngineState(CLEAR_ALL_STATE, ALLOCATED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleResume(final InputStream in) {
        if (recognizer == null) {
            LOGGER.warning("no recognizer: cannot resume!");
            return false;
        }

        if (recognitionThread != null) {
            LOGGER.warning("recognition thread already started.");
            return false;
        }
        recognizer.startRecognition(in);

        // start the recognizer thread and wait for the recognizer to recognize
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
            throw new EngineStateException("Cannot pause, no decoder started");
        }

        // prevent further calls to recognize()
        recognitionThread.stopRecognition();
        recognizer.stopRecognition();

        // get rid of the recognizer thread
        stopRecognitionThread();
    }

    /**
     * @todo Correctly implement this
     */
    public void handlePause(int flags) {
        handlePause();
    }

    /**
     * Called from the <code>deallocate</code> method.
     * 
     * According to JSAPI2 specs, pause is transitioned before dealloc.
     * 
     * @throws EngineException
     *             if this <code>Engine</code> cannot be deallocated.
     * @todo Implement this com.sun.speech.engine.BaseEngine method
     */
    public void handleDeallocate() {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("deallocating recognizer...");
        }

        // // pause is not called before dealloc obviously
        // if (recognizer.getState() != State.READY)
        // handlePause();
        //
        recognizer.deallocate();
        recognizer.removeResultListener(resultListener);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("...deallocated");
        }

    }

    /**
     * Selector for the wrapped sphinx4 recognizer.
     * 
     * @return Recognizer
     */
    Jsapi2Recognizer getRecognizer() {
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
     * Get the current rule grammar or the one that produced the list of tokens
     * in case of the SRGS container.
     * 
     * @param token
     * 
     * @return Active grammar.
     */
    RuleGrammar getRuleGrammar(final Token token) {
        if (grammar instanceof SRGSGrammar) {
            return ((SRGSGrammar) grammar).getRuleGrammar();
        }
        if (grammar instanceof SRGSGrammarContainer) {
            return ((SRGSGrammarContainer) grammar).getRuleGrammar(token);
        }
        return null;
    }

    /**
     * @todo: in case of grammarDefinition.size > 1, make <one-of> of all the
     *        grammars
     * @param newGrammars
     *            String[]
     * @return boolean
     */
    @Override
    protected boolean setGrammars(
            final Collection<GrammarDefinition> grammarDefinitions) {
        return recognizer.setGrammars(grammarDefinitions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<javax.speech.recognition.Grammar> getBuiltInGrammars() {
        return null;
    }

    public void postStartOfSpeechEvent() {
        postEngineEvent(new RecognizerEvent(this,
                RecognizerEvent.SPEECH_STARTED, 1, 1, null, null, 0));
    }

    public void postEndOfSpeechEvent() {
        // SearchGraph sg = linguist.getSearchGraph();
        // SearchGraphDumper.dumpDot("sg.dot", "foo", sg);

        postEngineEvent(new RecognizerEvent(this,
                RecognizerEvent.SPEECH_STOPPED, 1, 1, null, null, 0));
    }

    public void postProcessingEvent() {
        long states[] = setEngineState(LISTENING, PROCESSING);
        postEngineEvent(states[0], states[1],
                RecognizerEvent.RECOGNIZER_PROCESSING, (long) 0);
    }

    public void postListeningEvent() {
        long states[] = setEngineState(PROCESSING, LISTENING);
        postEngineEvent(states[0], states[1],
                RecognizerEvent.RECOGNIZER_LISTENING, (long) 0);

    }

    /**
     * {@inheritDoc}
     */
    public void postResultEvent(final ResultEvent resultEvent) {
        super.postResultEvent(resultEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleRequestFocus() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleReleaseFocus() {
    }

    @Override
    protected AudioFormat getAudioFormat() {
        return recognizer.getAudioFormat();
        // return new AudioFormat(16000, 16, 1, true, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handlePropertyChangeRequest(
            final BaseEngineProperties properties, final String propName,
            final Object oldValue, final Object newValue) {
        LOGGER.warning("changing property '" + propName + "' to '" + newValue
                + "' ignored");
    }
}
