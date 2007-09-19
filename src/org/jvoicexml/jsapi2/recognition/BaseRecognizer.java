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
package org.jvoicexml.jsapi2.recognition;


import org.jvoicexml.jsapi2.BaseEngine;
import javax.speech.recognition.Recognizer;
import java.util.Vector;
import java.util.Hashtable;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.SpeakerManager;
import javax.speech.recognition.RecognizerMode;
import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.EngineEvent;
import javax.speech.Engine;
import javax.speech.recognition.Grammar;
import java.util.Enumeration;
import javax.speech.recognition.RecognizerEvent;
import javax.speech.recognition.RecognizerListener;
import javax.speech.recognition.ResultListener;
import java.io.IOException;
import javax.speech.recognition.GrammarException;
import java.io.Reader;
import javax.speech.EngineListener;
import javax.speech.recognition.RecognizerProperties;


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
public class BaseRecognizer extends BaseEngine implements Recognizer {
    protected Vector resultListeners;
    protected Hashtable grammarList;
    protected boolean caseSensitiveGrammarNames = true;
    protected boolean hasModalGrammars = false;

    protected boolean supportsNULL = true;
    protected boolean supportsVOID = true;

    // used when printing grammars
    public RuleGrammar currentGrammar = null;

    // Set to true if recognizer cannot handle partial
    // grammar loading.
    protected boolean reloadAll = false;

    private SpeakerManager speakerManager;
    private BaseRecognizerProperties recognizerProperties;
    private int resultMask;


    /**
     * Create a new Recognizer in the DEALLOCATED state.
     */
    public BaseRecognizer() {
        this(false, null);
    }

    /**
     * Create a new Recognizer in the DEALLOCATED state.
     */
    public BaseRecognizer(RecognizerMode mode) {
        this(false, mode);
    }

    /**
     * Create a new Recognizer in the DEALLOCATED state.
     * @param reloadAll set to true if recognizer cannot handle
     * partial grammar loading.  Default = false.
     */
    public BaseRecognizer(boolean reloadAll, RecognizerMode mode) {
        super(mode);
        this.reloadAll = reloadAll;
        resultListeners = new Vector();
        grammarList = new Hashtable();
        speakerManager = new BaseSpeakerManager();
        recognizerProperties = new BaseRecognizerProperties(this);
        resultMask = 0;
    }


    /**
     * Allocate the resources for the Engine and put it in the ALLOCATED,
     * RESUMED, QUEUE_EMPTY state.
     */
    public void allocate() throws AudioException, EngineException,
            EngineStateException {

        // We don't need the following steps to be atomic
        // so there's no need to synchronize on engineStateLock
        if (testEngineState(ALLOCATED)) {
            return;
        }

        // Temporarily go in to the ALLOCATING_RESOURCES state.
        long[] states = setEngineState(CLEAR_ALL_STATE, ALLOCATING_RESOURCES);
        postEngineEvent(states[0], states[1],
                        EngineEvent.ENGINE_ALLOCATING_RESOURCES);

        // Go in to the ALLOCATED, RESUMED, LISTENING, and FOCUS_ON states.
        // Subclasses with shared engines should check all states before
        // changing them here.
        synchronized (engineStateLock) {
            long newState = ALLOCATED | RESUMED | LISTENING | FOCUSED;
            states = setEngineState(CLEAR_ALL_STATE, newState);
        }
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_ALLOCATED);
        handleAllocate();
    }


    /**
     * Deallocate the resources for the Engine and put it in the
     * DEALLOCATED state.
     */

    public void deallocate() throws EngineException, EngineStateException {
        // We don't need the following steps to be atomic
        // so there's no need to synchronize on engineStateLock

        if (testEngineState(DEALLOCATED))
            return;

        // Clean up the focus state
        releaseFocus();

        // Temporarily go in to the DEALLOCATING_RESOURCES state.
        // Make sure we kill the PAUSE/RESUME, LISTENING, FOCUS states etc.
        long[] states = setEngineState(CLEAR_ALL_STATE, DEALLOCATING_RESOURCES);
        postEngineEvent(states[0], states[1],
                        EngineEvent.ENGINE_DEALLOCATING_RESOURCES);

        // Go in to the DEALLOCATED state.
        states = setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_DEALLOCATED);
        handleDeallocate();
    }


    /**
     * Request speech focus for this Recognizer from the underlying speech
     * recognition system.
     */
    public void requestFocus() throws EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        if (testEngineState(Engine.ALLOCATING_RESOURCES) == true) {
            waitEngineState(Engine.ALLOCATED);
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

        if (testEngineState(Engine.ALLOCATING_RESOURCES) == true) {
            waitEngineState(Engine.ALLOCATED);
        }

        if (testEngineState(DEFOCUSED))
            return;

        long[] states = setEngineState(FOCUSED, DEFOCUSED);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_DEFOCUSED);

        notifyGrammarActivation();
    }


    /**
     * Retrieve a grammar from the grammar list.
     */
    protected RuleGrammar retrieveGrammar(String name) {
        if (caseSensitiveGrammarNames) {
            return (RuleGrammar) grammarList.get(name);
        } else {
            return (RuleGrammar) grammarList.get(name.toLowerCase());
        }
    }


    /**
     * Determine if the given Grammar is active.  This is a combination
     * of the enabled state and activation modes of the Grammar as well
     * as the current focus state of the recognizer.  NOT JSAPI.
     */
    protected boolean isActive(Grammar grammar) {
        //[[[WDW - check engineState?]]]
        if (!grammar.isEnabled()) {
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
        if (grammarList == null) {
            return;
        }
        Enumeration e = grammarList.elements();
        while (e.hasMoreElements()) {
            RuleGrammar rg = (RuleGrammar) e.nextElement();
            boolean active = isActive(rg);
            /*     if (active != rg.isActive()) {
                     rg.grammarActive = active;
                     if (active) {
                         rg.postGrammarActivated();
                     } else {
                         rg.postGrammarDeactivated();
                     }
                 }*/
        }
    }


    /**
     * Called from the <code>resume</code> method.  Override in subclasses.
     */
    protected boolean handleResume() {
        return false;
    }

    /**
     * Called from the <code>pause</code> method.  Override this in subclasses.
     */
    protected boolean handlePause() {
        return false;
    }

    /**
     * Called from the <code>allocate</code> method.  Override this in
     * subclasses.
     *
     * @see #allocate
     *
     * @throws EngineException if problems are encountered
     */
    protected boolean handleAllocate() throws EngineException {
        return false;
    }

    /**
     * Called from the <code>deallocate</code> method.  Override this in
     * subclasses.
     *
     * @throws EngineException if this <code>Engine</code> cannot be
     *   deallocated.
     */
    protected boolean handleDeallocate() throws EngineException {
        return false;
    }

    /**
     * @todo Implement
     *
     * @param flags int
     * @throws EngineStateException
     */
    public void pause(int flags) throws EngineStateException {
    }


    public void fireEvent(EngineEvent event) {
        if (super.engineListeners == null) {
            return;
        }

        Enumeration E = engineListeners.elements();
        while (E.hasMoreElements()) {
            EngineListener el = (EngineListener) E.nextElement();
            ((RecognizerListener) el).recognizerUpdate((RecognizerEvent) event);
        }
    }


    protected void postEngineEvent(long oldState, long newState, int eventType) {
        final RecognizerEvent event = new RecognizerEvent(this,
                eventType,
                oldState,
                newState,
                null,
                null,
                0);

        postEngineEvent(event);
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


    public RuleGrammar createRuleGrammar(String grammarReference,
                                         String rootName) throws
            IllegalArgumentException, EngineStateException, EngineException {

        return null;
    }


    public void deleteGrammar(Grammar grammar) throws IllegalArgumentException,
            EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
        String name = grammar.getReference();
        grammarList.remove(name);
    }

    /**
     * Get the RecognizerProperties of this Recognizer.
     * From javax.speech.recognition.Recognizer.
     */
    public RecognizerProperties getRecognizerProperties() {
        return recognizerProperties;
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
     * @todo Implement
     *
     * @throws EngineStateException
     */
    public void processGrammars() throws EngineStateException {

    }

    /**
     * @todo Implement
     *
     * @return Grammar[]
     * @throws EngineStateException
     */
    public Grammar[] listGrammars() throws EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
        if (grammarList == null) {
            return new RuleGrammar[0];
        }
        RuleGrammar rl[] = new RuleGrammar[grammarList.size()];
        int i = 0;
        Enumeration e = grammarList.elements();
        while (e.hasMoreElements()) {
            rl[i++] = (RuleGrammar) e.nextElement();
        }
        return rl;
    }

    /**
     * @todo Implement
     *
     * @param grammarReference String
     * @return RuleGrammar
     * @throws EngineStateException
     */
    public RuleGrammar getRuleGrammar(String grammarReference) throws
            EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
        return retrieveGrammar(grammarReference);
    }

    /**
     * @todo Implement
     *
     * @param grammarReference String
     * @return RuleGrammar
     * @throws GrammarException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws EngineStateException
     * @throws EngineException
     */
    public RuleGrammar loadRuleGrammar(String grammarReference) throws
            GrammarException, IllegalArgumentException, IOException,
            EngineStateException, EngineException {
        return null;
    }

    /**
     * @todo Implement
     *
     * @param grammarReference String
     * @param loadReferences boolean
     * @param reloadGrammars boolean
     * @param loadedGrammars Vector
     * @return RuleGrammar
     * @throws GrammarException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws EngineStateException
     * @throws EngineException
     */
    public RuleGrammar loadRuleGrammar(String grammarReference,
                                       boolean loadReferences,
                                       boolean reloadGrammars,
                                       Vector loadedGrammars) throws
            GrammarException, IllegalArgumentException,
            IOException, EngineStateException, EngineException {
        return null;
    }

    /**
     * @todo Implement
     *
     * @param grammarReference String
     * @param reader Reader
     * @return RuleGrammar
     * @throws GrammarException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws EngineStateException
     * @throws EngineException
     */
    public RuleGrammar loadRuleGrammar(String grammarReference, Reader reader) throws
            GrammarException, IllegalArgumentException, IOException,
            EngineStateException, EngineException {
        return null;
    }

    /**
     * @todo Implement
     *
     * @param grammarReference String
     * @param grammarText String
     * @return RuleGrammar
     * @throws GrammarException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws EngineStateException
     * @throws EngineException
     */
    public RuleGrammar loadRuleGrammar(String grammarReference,
                                       String grammarText) throws
            GrammarException, IllegalArgumentException, IOException,
            EngineStateException, EngineException {
        return null;
    }

}
