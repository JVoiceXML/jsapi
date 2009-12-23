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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.EngineEvent;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.VocabularyManager;
import javax.speech.recognition.Grammar;
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

import org.jvoicexml.jsapi2.BaseAudioManager;
import org.jvoicexml.jsapi2.BaseEngine;
import org.jvoicexml.jsapi2.BaseEngineProperties;
import org.jvoicexml.jsapi2.BaseVocabularyManager;
import org.jvoicexml.jsapi2.ThreadSpeechEventExecutor;
import org.jvoicexml.jsapi2.recognition.BaseGrammar;
import org.jvoicexml.jsapi2.recognition.BaseRecognizerProperties;


/**
 * Skeletal Implementation of the JSAPI Recognizer interface.
 *
 * This class is useful by itself for debugging, e.g. you
 * can load grammars and simulate a recognizer recognizing
 * some text, etc.
 *
 * <P>
 *
 * Actual JSAPI recognizer implementations might want to extend or
 * modify this implementation.
 *
 */
public abstract class BaseRecognizer extends BaseEngine implements Recognizer {
    /** Logger for this class. */
    private static final Logger LOGGER =
            Logger.getLogger(BaseRecognizer.class.getName());

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
     * @param mode the recognizer mode
     */
    public BaseRecognizer(final RecognizerMode mode) {
        super(mode);
        final BaseAudioManager audioManager =
            (BaseAudioManager) getAudioManager();
        audioManager.setEngine(this);
        resultListeners = new Vector();
        speakerManager = new BaseSpeakerManager();
        final RecognizerProperties props =
            new BaseRecognizerProperties(this);
        setRecognizerProperties(props);
        grammarManager = new BaseGrammarManager(this);
        resultMask = ResultEvent.DEFAULT_MASK;
        setEngineMask(getEngineMask() | RecognizerEvent.DEFAULT_MASK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SpeechEventExecutor createSpeechEventExecutor() {
        return new ThreadSpeechEventExecutor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AudioManager createAudioManager() {
        return new BaseRecognizerAudioManager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected VocabularyManager createVocabularyManager() {
        return new BaseVocabularyManager();
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
        if (testEngineState(FOCUSED)) {
            return;
        }

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

        if (testEngineState(DEFOCUSED)) {
            return;
        }

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
                LOGGER.warning(ex.getLocalizedMessage());
                return;
            }
        }

        //Handle pause
        boolean status = basePause(flags);
        if (status) {
            long[] states = setEngineState(RESUMED, PAUSED);
            postEngineEvent(states[0], states[1], EngineEvent.ENGINE_PAUSED);
        }

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


    /**
     * {@inheritDoc}
     */
    @Override
    public void fireEvent(final EngineEvent event) {
        synchronized (engineListeners) {
            final RecognizerEvent recognizerEvent =
                (RecognizerEvent) event;
            Enumeration enumeration = engineListeners.elements();
            while (enumeration.hasMoreElements()) {
                final RecognizerListener listener =
                    (RecognizerListener) enumeration.nextElement();
                listener.recognizerUpdate(recognizerEvent);
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
            LOGGER.warning(e.getLocalizedMessage());
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
        } catch (Exception e) {
            LOGGER.warning(e.getLocalizedMessage());
        }
    }

    protected void postResultEvent(final ResultEvent event) {
        final SpeechEventExecutor executor = getSpeechEventExecutor();
        try {
            executor.execute(new Runnable() {
                public void run() {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("notifying event " + event);
                    }
                    fireResultEvent(event);
                }
            });
        } catch (RuntimeException e) {
            LOGGER.warning(e.getLocalizedMessage());
        }
        final BaseResult base = (BaseResult) event.getSource();
        base.postResultEvent(executor, event);
    }

    public void fireResultEvent(final ResultEvent event) {
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
        if (recognizerProperties instanceof BaseEngineProperties) {
            final BaseEngineProperties props =
                (BaseEngineProperties) recognizerProperties;
            addEnginePropertyChangeRequestListener(props);
        }
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
                postEngineEvent(PAUSED, RESUMED,
                        RecognizerEvent.CHANGES_COMMITTED);
                for (int i = 0; i < grammars.length; i++) {
                    final BaseGrammar baseGrammar = (BaseGrammar) grammars[i];
                    baseGrammar.postGrammarChangesCommitted();
                }
            } else {
                for (int i = 0; i < grammars.length; i++) {
                    final BaseGrammar baseGrammar = (BaseGrammar) grammars[i];
                    baseGrammar.postGrammarChangesRejected();
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
     * Called from the <code>allocate</code> method.
     *
     * @see #allocate
     *
     * @throws AudioException
     *          if any audio request fails 
     * @throws EngineException
     *          if an allocation error occurred or the Engine is not
     *          operational. 
     * @throws EngineStateException
     *          if called for an Engine in the DEALLOCATING_RESOURCES state 
     * @throws SecurityException
     *          if the application does not have permission for this Engine
     */
    protected final void baseAllocate() throws EngineStateException,
            EngineException, AudioException, SecurityException {
        final AudioManager audioManager = getAudioManager();
        audioManager.audioStart();

        //Procceed to real engine allocation
        handleAllocate();
        long[] states = setEngineState(CLEAR_ALL_STATE,
                ALLOCATED | PAUSED | DEFOCUSED |
                NOT_BUFFERING);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_ALLOCATED);
    }

    /**
     * Perform the real allocation of the recognizer.
     * @throws AudioException
     *          if any audio request fails 
     * @throws EngineException
     *          if an allocation error occurred or the Engine is not
     *          operational. 
     * @throws EngineStateException
     *          if called for an Engine in the DEALLOCATING_RESOURCES state 
     * @throws SecurityException
     *          if the application does not have permission for this Engine
     */
    abstract protected void handleAllocate() throws EngineStateException,
        EngineException, AudioException, SecurityException;


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
        if (status) {
            //Stops AudioManager
            final AudioManager audioManager = getAudioManager();
            audioManager.audioStop();

            long[] states = setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
            postEngineEvent(states[0], states[1],
                    EngineEvent.ENGINE_DEALLOCATED);
        } else {
            //Stops AudioManager
            final AudioManager audioManager = getAudioManager();
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
        } catch (EngineStateException e) {
            LOGGER.warning(e.getLocalizedMessage());
            return false;
        }

        boolean status = handleResume();
        if (status) {
            setEngineState(0, LISTENING);
            postEngineEvent(0, LISTENING, RecognizerEvent.RECOGNIZER_LISTENING);
            setEngineState(NOT_BUFFERING, BUFFERING);
        }

        return status;
    }


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
