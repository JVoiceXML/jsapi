/**
 * Copyright 1998-2003 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */
package org.jvoicexml.jsapi2.jse.recognition;

import javax.speech.recognition.Grammar;
import java.util.Vector;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.GrammarListener;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.GrammarEvent;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultEvent;
import javax.speech.SpeechEventExecutor;
import java.util.Enumeration;
import javax.speech.recognition.GrammarManager;


/**
 * Implementation of javax.speech.recognition.Grammar.
 *
 */
public class BaseGrammar implements Grammar {
    /** Reference to the recognizer. */
    public    transient BaseRecognizer recognizer;

    /** Registered listeners for grammar changes. */
    protected transient Vector grammarListeners;

    /** Registered listeners for result events. */
    protected transient Vector resultListeners;

    /** Name of this grammar. */
    protected String    name;

    /**
     * <code>true</code> if this grammar is active.
     * only changed by commit and rec focus.
     */
    protected boolean grammarActive;

    /** The activation mode of this grammar. */
    protected int     activationMode;

    /** <code>true</code> if this grammar can be activated. */
    protected boolean activatable;

    /**
     * Creates a new BaseGrammar.
     * @param rec the BaseRecognizer for this Grammar.
     * @param grammarName the name of this Grammar.
     */
    public BaseGrammar(final BaseRecognizer rec, final String grammarName) {
        grammarListeners = new Vector();
        resultListeners = new Vector();
        recognizer = rec;
        name = grammarName;
        grammarActive = false;
        activatable = false;
        activationMode = ACTIVATION_FOCUS;
    }

    /**
     * {@inheritDoc}
     */
    public Recognizer getRecognizer() {
        return recognizer;
    }

    /**
     * {@inheritDoc}
     */
    public String getReference() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public void setActivatable(final boolean value) {
        activatable = value;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isActivatable() {
        return activatable;
    }

    /**
     * {@inheritDoc}
     */
    public void setActivationMode(final int mode)
        throws IllegalArgumentException {
        if ((mode != ACTIVATION_GLOBAL)
            && (mode != ACTIVATION_MODAL)
            && (mode != ACTIVATION_FOCUS)
            && (mode != ACTIVATION_INDIRECT)) {
            throw new IllegalArgumentException("Invalid ActivationMode");
        } else if (mode != activationMode) {
            activationMode = mode;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getActivationMode() {
        return activationMode;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isActive() {
        return recognizer.isActive(this);
    }

    /**
     * {@inheritDoc}
     */
    public GrammarManager getGrammarManager() {
        return recognizer.getGrammarManager();
    }

    /**
     * {@inheritDoc}
     */
    public void addGrammarListener(final GrammarListener listener) {
        if (!grammarListeners.contains(listener)) {
            grammarListeners.addElement(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeGrammarListener(final GrammarListener listener) {
        grammarListeners.removeElement(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void addResultListener(final ResultListener listener) {
        if (!resultListeners.contains(listener)) {
            resultListeners.addElement(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeResultListener(final ResultListener listener) {
        resultListeners.removeElement(listener);
    }
    /**
     * Utility function to generate grammar event and post it to the event queue.
     * @param speechEventExecutor SpeechEventExecutor
     * @param event ResultEvent
     */
    public void postGrammarEvent(SpeechEventExecutor speechEventExecutor, final GrammarEvent event){
        try {
            speechEventExecutor.execute(new Runnable() {
                public void run() {
                    fireGrammarEvent(event);
                }
            });
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Utility function to send a grammar event to all grammar
     * listeners.
     */
    public void fireGrammarEvent(GrammarEvent event) {
        Enumeration E;
        if (resultListeners != null) {
            E = resultListeners.elements();
            while (E.hasMoreElements()) {
                GrammarListener rl = (GrammarListener) E.nextElement();
                rl.grammarUpdate(event);
            }
        }
    }



    /**
     * Utility function to generate GRAMMAR_ACTIVATED event and post it
     * to the event queue.  Eventually fireGrammarActivated will be called
     * by dispatchSpeechEvent as a result of this action.
     */
    public void postGrammarActivated() {
        /* SpeechEventUtilities.postSpeechEvent(
            this,
            new GrammarEvent(this, GrammarEvent.GRAMMAR_ACTIVATED));*/
    }

    /**
     * Utility function to send a GRAMMAR_ACTIVATED event to all result
     * listeners.
     */
    protected void fireGrammarActivated(GrammarEvent event) {
        /*if (grammarListeners == null) {
	    return;
        }
        Enumeration E = grammarListeners.elements();
        while (E.hasMoreElements()) {
            GrammarListener gl = (GrammarListener) E.nextElement();
            gl.grammarActivated(event);
        }*/
    }

    /**
     * Utility function to generate GRAMMAR_CHANGES_COMMITTED event and post it
     * to the event queue.  Eventually fireGrammarChangesCommitted will be
     * called by dispatchSpeechEvent as a result of this action.
     */
    public void postGrammarChangesCommitted() {
        /*   SpeechEventUtilities.postSpeechEvent(
            this,
            new GrammarEvent(this, GrammarEvent.GRAMMAR_CHANGES_COMMITTED));*/
    }

    /**
     * Utility function to send a GRAMMAR_CHANGES_COMMITTED event to all result
     * listeners.
     */
    protected void fireGrammarChangesCommitted(GrammarEvent event) {
        /*if (grammarListeners == null) {
	    return;
        }
        Enumeration E = grammarListeners.elements();
        while (E.hasMoreElements()) {
            GrammarListener gl = (GrammarListener) E.nextElement();
            gl.grammarChangesCommitted(event);
        }*/
    }

    /**
     * Utility function to generate GRAMMAR_DEACTIVATED event and post it
     * to the event queue.  Eventually fireGrammarDeactivated will be called
     * by dispatchSpeechEvent as a result of this action.
     */
    public void postGrammarDeactivated() {
        /*  SpeechEventUtilities.postSpeechEvent(
            this,
            new GrammarEvent(this, GrammarEvent.GRAMMAR_DEACTIVATED));*/
    }

    /**
     * Utility function to send a GRAMMAR_DEACTIVATED event to all result
     * listeners.
     */
    protected void fireGrammarDeactivated(GrammarEvent event) {
        /*if (grammarListeners == null) {
	    return;
        }
        Enumeration E = grammarListeners.elements();
        while (E.hasMoreElements()) {
            GrammarListener gl = (GrammarListener) E.nextElement();
            gl.grammarDeactivated(event);
        }*/
    }
    //////////////////////
    // End utility methods for sending GrammarEvents
    //////////////////////

    //////////////////////
    // Begin utility methods for sending ResultEvents
    //////////////////////
    /**
     * Utility function to generate AUDIO_RELEASED event and post it
     * to the event queue.  Eventually fireAudioReleased will be called
     * by dispatchSpeechEvent as a result of this action.
     */
    public void postAudioReleased(Result result) {
        /* SpeechEventUtilities.postSpeechEvent(
            this,
            new ResultEvent(result, ResultEvent.AUDIO_RELEASED));*/
    }

    /**
     * Utility function to send a AUDIO_RELEASED event to all result
     * listeners.
     */
    public void fireAudioReleased(ResultEvent event) {
        /*   Enumeration E;
	if (resultListeners != null) {
            E = resultListeners.elements();
            while (E.hasMoreElements()) {
                ResultListener rl = (ResultListener) E.nextElement();
                rl.audioReleased(event);
            }
        }*/
    }

    /**
     * Utility function to generate GRAMMAR_FINALIZED event and post it
     * to the event queue.  Eventually fireGrammarFinalized will be called
     * by dispatchSpeechEvent as a result of this action.
     */
    public void postGrammarFinalized(Result result) {
        /*  SpeechEventUtilities.postSpeechEvent(
            this,
            new ResultEvent(result, ResultEvent.GRAMMAR_FINALIZED));*/
    }

    /**
     * Utility function to send a GRAMMAR_FINALIZED event to all result
     * listeners.
     */
    public void fireGrammarFinalized(ResultEvent event) {
        /*   Enumeration E;
	if (resultListeners != null) {
            E = resultListeners.elements();
            while (E.hasMoreElements()) {
                ResultListener rl = (ResultListener) E.nextElement();
                rl.grammarFinalized(event);
            }
        }*/
    }

    /**
     * Utility function to generate RESULT_ACCEPTED event and post it
     * to the event queue.  Eventually fireResultAccepted will be called
     * by dispatchSpeechEvent as a result of this action.
     */
    public void postResultAccepted(Result result) {
        /* SpeechEventUtilities.postSpeechEvent(
            this,
            new ResultEvent(result, ResultEvent.RESULT_ACCEPTED));*/
    }

    /**
     * Utility function to send a RESULT_ACCEPTED event to all result
     * listeners.
     */
    public void fireResultAccepted(ResultEvent event) {
        /*  Enumeration E;
	if (resultListeners != null) {
            E = resultListeners.elements();
            while (E.hasMoreElements()) {
                ResultListener rl = (ResultListener) E.nextElement();
                rl.resultAccepted(event);
            }
        }*/
    }

    /**
     * Utility function to generate RESULT_CREATED event and post it
     * to the event queue.  Eventually fireResultCreated will be called
     * by dispatchSpeechEvent as a result of this action.
     */
    public void postResultCreated(Result result) {
        /*    SpeechEventUtilities.postSpeechEvent(
            this,
            new ResultEvent(result, ResultEvent.RESULT_CREATED));*/
    }

    /**
     * Utility function to send a RESULT_CREATED event to all result
     * listeners.
     */
    public void fireResultCreated(ResultEvent event) {
        /*    Enumeration E;
	if (resultListeners != null) {
            E = resultListeners.elements();
            while (E.hasMoreElements()) {
                ResultListener rl = (ResultListener) E.nextElement();
                rl.resultCreated(event);
            }
        }*/
    }

    /**
     * Utility function to generate RESULT_REJECTED event and post it
     * to the event queue.  Eventually fireResultRejected will be called
     * by dispatchSpeechEvent as a result of this action.
     */
    public void postResultRejected(Result result) {
        /*   SpeechEventUtilities.postSpeechEvent(
            this,
            new ResultEvent(result, ResultEvent.RESULT_REJECTED));*/
    }

    /**
     * Utility function to send a RESULT_REJECTED event to all result
     * listeners.
     */
    public void fireResultRejected(ResultEvent event) {
        /*   Enumeration E;
	if (resultListeners != null) {
            E = resultListeners.elements();
            while (E.hasMoreElements()) {
                ResultListener rl = (ResultListener) E.nextElement();
                rl.resultRejected(event);
            }
        }*/
    }

    /**
     * Utility function to generate RESULT_UPDATED event and post it
     * to the event queue.  Eventually fireResultUpdated will be called
     * by dispatchSpeechEvent as a result of this action.
     */
    public void postResultUpdated(Result result) {
        /*   SpeechEventUtilities.postSpeechEvent(
            this,
            new ResultEvent(result, ResultEvent.RESULT_UPDATED));*/
    }

    /**
     * Utility function to send a RESULT_UPDATED event to all result
     * listeners.
     */
    public void fireResultUpdated(ResultEvent event) {
        /*  Enumeration E;
	if (resultListeners != null) {
            E = resultListeners.elements();
            while (E.hasMoreElements()) {
                ResultListener rl = (ResultListener) E.nextElement();
                rl.resultUpdated(event);
            }
        }*/
    }

    /**
     * Utility function to generate TRAINING_INFO_RELEASED event and post it
     * to the event queue.  Eventually fireTrainingInfoReleased will be called
     * by dispatchSpeechEvent as a result of this action.
     */
    public void postTrainingInfoReleased(Result result) {
        /* SpeechEventUtilities.postSpeechEvent(
            this,
            new ResultEvent(result, ResultEvent.TRAINING_INFO_RELEASED));*/
    }

    /**
     * Utility function to send a TRAINING_INFO_RELEASED event to all result
     * listeners.
     */
    public void fireTrainingInfoReleased(ResultEvent event) {
        /*   Enumeration E;
	if (resultListeners != null) {
            E = resultListeners.elements();
            while (E.hasMoreElements()) {
                ResultListener rl = (ResultListener) E.nextElement();
                rl.trainingInfoReleased(event);
            }
        }*/
    }
}

