/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision:  $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: lyncher $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This class is based on work by SUN Microsystems and
 * Carnegie Mellon University
 *
 * Copyright 1998-2003 Sun Microsystems, Inc.
 *
 * Portions Copyright 2001-2004 Sun Microsystems, Inc.
 * Portions Copyright 1999-2001 Language Technologies Institute,
 * Carnegie Mellon University.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * Permission is hereby granted, free of charge, to use and distribute
 * this software and its documentation without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of this work, and to
 * permit persons to whom this work is furnished to do so, subject to
 * the following conditions:
 *
 * 1. The code must retain the above copyright notice, this list of
 *    conditions and the following disclaimer.
 * 2. Any modifications must be clearly marked as such.
 * 3. Original authors' names are not deleted.
 * 4. The authors' names are not used to endorse or promote products
 *    derived from this software without specific prior written
 *   permission.
 *
 * SUN MICROSYSTEMS, INC., CARNEGIE MELLON UNIVERSITY AND THE
 * CONTRIBUTORS TO THIS WORK DISCLAIM ALL WARRANTIES WITH REGARD TO THIS
 * SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS, IN NO EVENT SHALL SUN MICROSYSTEMS, INC., CARNEGIE MELLON
 * UNIVERSITY NOR THE CONTRIBUTORS BE LIABLE FOR ANY SPECIAL, INDIRECT OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF
 * USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */
package org.jvoicexml.jsapi2.jse.recognition;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.EngineEvent;
import javax.speech.EngineException;
import javax.speech.EngineListener;
import javax.speech.EngineStateException;
import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarEvent;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.RecognizerEvent;
import javax.speech.recognition.RecognizerListener;
import javax.speech.recognition.RecognizerMode;
import javax.speech.recognition.RecognizerProperties;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.SpeakerManager;

import org.jvoicexml.jsapi2.jse.BaseEngine;


/**
 * Skeletal Implementation of the JSAPI Recognizer interface.
 *
 * This class is useful by itself for debugging, e.g. you
 * can load grammars and simulate a recognizer recognizing
 * some text, etc.
 * <P>
 *
 * Actual JSAPI recognizer implementations might want to extend or
 * modify this implementation.
 *
 */
abstract public class BaseRecognizer extends BaseEngine implements Recognizer {

    protected Vector resultListeners;

    protected boolean hasModalGrammars = false;

    protected boolean supportsNULL = true;
    protected boolean supportsVOID = true;

    // used when printing grammars
    public RuleGrammar currentGrammar = null;

    // Set to true if recognizer cannot handle partial grammar loading.
    protected boolean reloadAll = false;

    private final SpeakerManager speakerManager;
    protected RecognizerProperties recognizerProperties;
    private int resultMask;

    private final BaseGrammarManager grammarManager;

    protected final Vector<String> uncommitedDeletedGrammars = new Vector<String>();


    /**
     * Create a new Recognizer in the DEALLOCATED state.
     */
    public BaseRecognizer() {
        this(null);
    }

    /**
     * Create a new Recognizer in the DEALLOCATED state.
     */
    public BaseRecognizer(RecognizerMode mode) {
        super(mode, new BaseRecognizerAudioManager());
        resultListeners = new Vector();
        speakerManager = new BaseSpeakerManager();
        recognizerProperties = new BaseRecognizerProperties(this);
        grammarManager = new BaseGrammarManager(this);
        resultMask = ResultEvent.DEFAULT_MASK;
        setEngineMask(getEngineMask() | RecognizerEvent.DEFAULT_MASK);
    }

    public GrammarManager getGrammarManager() {
        return grammarManager;
    }

    /**
     * Request speech focus for this Recognizer from the underlying speech
     * recognition system.
     */
    public void requestFocus() throws EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        // Do nothing if the state is already OK
        if (testEngineState(FOCUSED))
            return;

        long[] states = setEngineState(DEFOCUSED, FOCUSED);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_FOCUSED);

        notifyGrammarActivation();
    }

    /**
     * Release speech focus for this Recognizer from the underlying speech
     * recognition system.
     */
    public void releaseFocus() throws EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        if (testEngineState(DEFOCUSED))
            return;

        long[] states = setEngineState(FOCUSED, DEFOCUSED);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_DEFOCUSED);

        notifyGrammarActivation();
    }

    /**
     * Pauses the audio stream for this <code>Engine</code> and put
     * this <code>Engine</code> into the <code>PAUSED</code> state.
     *
     * @param flags int (Recognizer.BUFFER_MODE, Recognizer.IMMEDIATE_MODE)
     * @throws EngineStateException if this <code>Engine</code> is in the
     *   <code>DEALLOCATING_RESOURCES</code> or
     *   <code>DEALLOCATED</code> state.
     */
    public void pause(int flags) throws EngineStateException {
        //Validate current state
        if (testEngineState(PAUSED))
            return;

        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        //Handle pause
        boolean status = basePause(flags);
        if (status == true) {
            long[] states = setEngineState(RESUMED, PAUSED);
            postEngineEvent(states[0], states[1], EngineEvent.ENGINE_PAUSED);
        }

    }

    /**
     * Determine if the given Grammar is active.  This is a combination
     * of the enabled state and activation modes of the Grammar as well
     * as the current focus state of the recognizer.  NOT JSAPI.
     */
    protected boolean isActive(Grammar grammar) {
        //[[[WDW - check engineState?]]]
        if (!grammar.isActivatable()) {
            return false;
        } else if (grammar.getActivationMode() == Grammar.ACTIVATION_GLOBAL) {
            return true;
        } else if (testEngineState(FOCUSED)) {
            if (grammar.getActivationMode() == Grammar.ACTIVATION_MODAL) {
                return true;
            } else if (!hasModalGrammars) {
                return true;
            }
        }
        return false;
    }

    /**
     * Notify any grammars if their activation state has been changed.
     */
    protected void notifyGrammarActivation() {
        /*if (grammars == null) {
            return;
        }*/
        /*  Enumeration e = grammars.elements();
          while (e.hasMoreElements()) {
              RuleGrammar rg = (RuleGrammar) e.nextElement();
              boolean active = isActive(rg);*/
        /*     if (active != rg.isActive()) {
                 rg.grammarActive = active;
                 if (active) {
                     rg.postGrammarActivated();
                 } else {
                     rg.postGrammarDeactivated();
                 }
             }*/
        // }
    }


    public void fireEvent(EngineEvent event) {
        synchronized (engineListeners) {
            for (EngineListener el : engineListeners) {
                ((RecognizerListener) el).recognizerUpdate((RecognizerEvent)
                        event);
            }
        }
    }


    public void postEngineEvent(long oldState, long newState, int eventType) {
        try {
            switch (eventType) {
            case RecognizerEvent.SPEECH_STARTED:
            case RecognizerEvent.SPEECH_STOPPED:
            case RecognizerEvent.RECOGNIZER_BUFFERING:
            case RecognizerEvent.RECOGNIZER_NOT_BUFFERING:
                postEngineEvent(oldState, newState, eventType, 0); /** @todo DGMR rever este 0; nao faltara o audioposition nos parametros da funcao? o speechstart, o stop o buffering e not buferring passam por aqui? */
                break;
            default:
                postEngineEvent(oldState, newState, eventType,
                                RecognizerEvent.UNKNOWN_AUDIO_POSITION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postEngineEvent(long oldState, long newState, int eventType,
                                long audioPosition) {
        try{
            switch (eventType) {
            case RecognizerEvent.SPEECH_STARTED:
            case RecognizerEvent.SPEECH_STOPPED:
            case RecognizerEvent.RECOGNIZER_BUFFERING:
            case RecognizerEvent.RECOGNIZER_NOT_BUFFERING:
                break;
            default:
                audioPosition = RecognizerEvent.UNKNOWN_AUDIO_POSITION;
            }

            final RecognizerEvent event = new RecognizerEvent(this,
                    eventType,
                    oldState,
                    newState,
                    null,
                    null,
                    audioPosition);
            postEngineEvent(event);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void postResultEvent(final ResultEvent event) {
        if ((getResultMask() & event.getId()) == event.getId()) {
            try {
                speechEventExecutor.execute(new Runnable() {
                    public void run() {
                        fireResultEvent(event);
                    }
                });
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
        ((BaseResult)event.getSource()).postResultEvent(speechEventExecutor, event);
    }

    public void fireResultEvent(ResultEvent event) {
        Enumeration listeners = resultListeners.elements();
        while (listeners.hasMoreElements()) {
            ResultListener el = (ResultListener) listeners.nextElement();
            ((ResultListener) el).resultUpdate((ResultEvent) event);
        }
    }


    public void addRecognizerListener(RecognizerListener listener) {
        addEngineListener(listener);
    }

    public void removeRecognizerListener(RecognizerListener listener) {
        removeEngineListener(listener);
    }


    /**
     * Request notification of Result events from the Recognizer.
     * From javax.speech.recognition.Recognizer.
     * @param listener the listener to add.
     */
    public void addResultListener(ResultListener listener) {
        if (!resultListeners.contains(listener)) {
            resultListeners.addElement(listener);
        }
    }

    /**
     * Remove a ResultListener from the list of ResultListeners.
     * From javax.speech.recognition.Recognizer.
     * @param listener the listener to remove.
     */
    public void removeResultListener(ResultListener listener) {
        resultListeners.removeElement(listener);
    }

    /**
     * Get the RecognizerProperties of this Recognizer.
     * From javax.speech.recognition.Recognizer.
     */
    public RecognizerProperties getRecognizerProperties() {
        return recognizerProperties;
    }

    public void setRecognizerProperties(RecognizerProperties
                                        recognizerProperties) {
        this.recognizerProperties = recognizerProperties;
    }

    /**
     * Get the object that manages the speakers of the Recognizer.
     * From javax.speech.recognition.Recognizer.
     */
    public SpeakerManager getSpeakerManager() {
        return speakerManager;
    }

    public void setResultMask(int mask) {
        resultMask = mask;
    }

    public int getResultMask() {
        return resultMask;
    }


    /**
     *
     * @throws EngineStateException
     */
    public void processGrammars() throws EngineStateException {

        //Flag that indicates if grammars were changed
        boolean existChanges = false;

        //Build a new grammar set, with all enabled grammars
        List<GrammarDefinition> newGrammars = new ArrayList<GrammarDefinition>();

        //Commit all grammars pending changes
        Grammar[] grammars = grammarManager.listGrammars();
        for (int i = 0; i < grammars.length; i++) {
            if (grammars[i] instanceof BaseRuleGrammar) {
                BaseRuleGrammar baseRuleGrammar = ((BaseRuleGrammar) grammars[i]);
                //Flag that indicates if this baserulegrammar were changed
                boolean grammarUpdated = baseRuleGrammar.commitChanges();
                //Update "modified-flag"
                existChanges = existChanges || grammarUpdated;
                if (baseRuleGrammar.isActivatable())
                    newGrammars.add(new GrammarDefinition(baseRuleGrammar.
                            toString(false),
                            baseRuleGrammar.getReference()));
            }
        }

        //Set grammars
        boolean setGrammarsResult = setGrammars(newGrammars);

        //Raise proper events
        if (existChanges) {
            if (setGrammarsResult) {
                postEngineEvent(PAUSED, RESUMED, RecognizerEvent.CHANGES_COMMITTED);
                for (int i = 0; i < grammars.length; i++) {
                    ((BaseGrammar) grammars[i]).postGrammarEvent(speechEventExecutor,
                            new
                            GrammarEvent(grammars[i], GrammarEvent.GRAMMAR_CHANGES_COMMITTED, false, false, null));
                }
            } else {
                for (int i = 0; i < grammars.length; i++) {
                    ((BaseGrammar) grammars[i]).postGrammarEvent(speechEventExecutor,
                            new
                            GrammarEvent(grammars[i], GrammarEvent.GRAMMAR_CHANGES_REJECTED, false, false, null));
                }
            }
        }
    }

    protected boolean isValid(long state) {
        if (testEngineState(LISTENING | PROCESSING))
            return false;

        return super.isValid(state);
    }


    /**
     * Returns a <code>String</code> of the names of all the
     * <code>Engine</code> states in the given <code>Engine</code>
     * state.
     *
     * @param state the bitmask of states
     *
     * @return a <code>String</code> containing the names of all the
     *   states set in <code>state</code>
     */
    protected String stateToString(long state) {
        StringBuffer buf = new StringBuffer(super.stateToString(state));
        if ((state & Recognizer.LISTENING) != 0)
            buf.append(" LISTENING ");
        if ((state & Recognizer.PROCESSING) != 0)
            buf.append(" PROCESSING ");
        return buf.toString();
    }


    /**
     * Called from the <code>allocate</code> method.  Override this in
     * subclasses.
     *
     * @see #allocate
     *
     * @throws EngineException if problems are encountered
     */
    protected boolean baseAllocate() throws EngineStateException,
            EngineException, AudioException {

        audioManager.audioStart();

        //Procceed to real engine allocation
        boolean status = handleAllocate();
        if (status == true) {
            long states[] = setEngineState(CLEAR_ALL_STATE,
                                           ALLOCATED | PAUSED | DEFOCUSED |
                                           NOT_BUFFERING);
            postEngineEvent(states[0], states[1], EngineEvent.ENGINE_ALLOCATED);
        }

        return status;
    }

    /**
     * Called from the <code>deallocate</code> method.  Override this in
     * subclasses.
     *
     * @throws EngineException if this <code>Engine</code> cannot be
     *   deallocated.
     */
    protected boolean baseDeallocate() throws EngineStateException,
            EngineException, AudioException {

        //Procceed to real engine deallocation
        boolean status = handleDeallocate();
        if (status == true) {
          //Stops AudioManager
          audioManager.audioStop();

          long states[] = setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
          postEngineEvent(states[0], states[1],
                            EngineEvent.ENGINE_DEALLOCATED);
        }
        else {
          //Stops AudioManager
          audioManager.audioStop();
        }


        return status;
    }

    protected boolean basePause() {

        boolean status = handlePause();
        if (status == true) {
            setEngineState(LISTENING | PROCESSING,
                           getEngineState() & ~LISTENING & ~PROCESSING);
            setEngineState(BUFFERING, NOT_BUFFERING);
        }

        return status;
    }

    protected boolean basePause(int flags) {

        boolean status = handlePause(flags);
        if (status == true) {
            setEngineState(LISTENING | PROCESSING,
                           getEngineState() & ~LISTENING & ~PROCESSING);
        }

        return status;
    }


    /**
     * Called from the <code>resume</code> method.  Override in subclasses.
     *
     * @todo Handle grammar updates
     */
    protected boolean baseResume() {

        //Process grammars
        try {
            processGrammars();
        } catch (EngineStateException ex) {
            ex.printStackTrace();
            return false;
        }

        boolean status = handleResume();
        if (status == true) {
            setEngineState(0, LISTENING);
            postEngineEvent(0, LISTENING, RecognizerEvent.RECOGNIZER_LISTENING);
            setEngineState(NOT_BUFFERING, BUFFERING);
        }

        return status;
    }


    abstract protected boolean handleAllocate();

    abstract protected boolean handleDeallocate();

    abstract protected boolean handlePause();

    abstract protected boolean handlePause(int flags);

    abstract protected boolean handleResume();

    abstract protected boolean setGrammars(List grammarDefinition);


    /**
     *
     * Returns a list of engine built-in grammars
     * @return List
     */
    abstract protected List<Grammar> getBuiltInGrammars();

}
