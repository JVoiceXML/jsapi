/*
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2017 JVoiceXML group - http://jvoicexml.sourceforge.net
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

import org.jvoicexml.jsapi2.recognition.GrammarDefinition;

import edu.cmu.sphinx.api.AbstractSpeechRecognizer;
import edu.cmu.sphinx.api.Context;
import edu.cmu.sphinx.decoder.ResultListener;
import edu.cmu.sphinx.recognizer.Recognizer.State;
import edu.cmu.sphinx.recognizer.StateListener;
import edu.cmu.sphinx.util.props.PropertyException;
import edu.cmu.sphinx.util.props.PropertySheet;

/**
 * Recognizer implementation of Sphinx to be used within the JSAPI2 context.
 * 
 * @author Dirk Schnelle-Walka
 * @since 0.6
 */
public class Jsapi2Recognizer extends AbstractSpeechRecognizer
        implements StateListener {
    /** Logger for this class. */
    private static final Logger LOGGER = Logger
            .getLogger(Jsapi2Recognizer.class.getName());

    private final Object stateMonitor;

    protected Jsapi2Recognizer(Context context) throws IOException {
        super(context);
        stateMonitor = new Object();
        recognizer.addStateListener(this);
    }

    public AudioFormat getAudioFormat() {
        final SphinxInputDataProcessor processor = context
                .getInstance(SphinxInputDataProcessor.class);
        return processor.getAudioFormat();
    }

    protected boolean setGrammars(
            final Collection<GrammarDefinition> grammarDefinitions) {
        SRGSGrammarContainer grammar = context
                .getInstance(SRGSGrammarContainer.class);
        // if (grammar instanceof SRGSGrammar) {
        // // old behavior with only a single active grammar
        // if (grammarDefinitions.size() == 1) {
        // try {
        // final org.jvoicexml.jsapi2.recognition.GrammarDefinition definition =
        // grammarDefinitions
        // .iterator().next();
        // ((SRGSGrammar) grammar).loadSRGS(definition.getGrammar());
        // } catch (IOException ex) {
        // return false;
        // }
        // return true;
        // } else {
        // return false;
        // }
        // } else if (grammar instanceof SRGSGrammarContainer) {
        // the big one-of dispatcher
        try {
            ((SRGSGrammarContainer) grammar).loadGrammars(grammarDefinitions);
        } catch (IOException ex) {
            System.err.println(ex);
            return false;
        }
        return true;
    }

    public void allocate() {
        recognizer.allocate();
        waitForRecognizerState(State.READY);
    }

    public void addResultListener(final ResultListener listener) {
        recognizer.addResultListener(listener);
    }

    public void removeResultListener(final ResultListener listener) {
        recognizer.removeResultListener(listener);
    }

    public void startRecognition(final InputStream in) {
        final SphinxInputDataProcessor processor = context
                .getInstance(SphinxInputDataProcessor.class);
        processor.setInputStream(in);
        processor.isRunning(true);
    }

    public void stopRecognition() {
        final SphinxInputDataProcessor processor = context
                .getInstance(SphinxInputDataProcessor.class);
        processor.isRunning(false);
        waitForRecognizerState(State.READY);
    }

    public void deallocate() {
        recognizer.deallocate();
        recognizer.resetMonitors();
    }

    /**
     * {@inheritDoc} Not used but for compatibility.
     */
    @Override
    public void newProperties(PropertySheet ps) throws PropertyException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void statusChanged(State status) {
        synchronized (stateMonitor) {
            stateMonitor.notifyAll();
        }
    }

    /**
     * Wait for the recognizer to enter the given state.
     * 
     * @param status
     *            The state of the recognizer to wait for.
     */
    synchronized void waitForRecognizerState(final State status) {
        while (recognizer.getState() != status) {
            try {
                synchronized (stateMonitor) {
                    stateMonitor.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOGGER.log(Level.INFO, "Sphinx4Recognizer in state: {0}", status);
    }
}
