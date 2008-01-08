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
import java.util.HashMap;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.SpeakerManager;
import javax.speech.recognition.RecognizerMode;
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
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.GrammarEvent;
import javax.speech.AudioException;
import java.util.List;
import java.util.Arrays;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathConstants;
import org.xml.sax.InputSource;
import java.net.URL;
import java.io.InputStream;
import javax.xml.xpath.*;
import java.io.StringReader;
import javax.speech.recognition.Rule;
import java.util.Iterator;


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
    protected HashMap<String, Grammar> grammars;
    protected boolean hasModalGrammars = false;

    protected boolean supportsNULL = true;
    protected boolean supportsVOID = true;

    // used when printing grammars
    public RuleGrammar currentGrammar = null;

    // Set to true if recognizer cannot handle partial
    // grammar loading.
    protected boolean reloadAll = false;

    private SpeakerManager speakerManager;
    protected RecognizerProperties recognizerProperties;
    private int resultMask;
    private int grammarMask;

    protected Vector<String> uncommitedDeletedGrammars = new Vector<String>();


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
        super(mode);
        resultListeners = new Vector();
        grammars = new HashMap<String, Grammar>();
        speakerManager = new BaseSpeakerManager();
        recognizerProperties = new BaseRecognizerProperties(this);
        resultMask = ResultEvent.DEFAULT_MASK;
        grammarMask = GrammarEvent.DEFAULT_MASK;
        setEngineMask(getEngineMask() | RecognizerEvent.DEFAULT_MASK);
    }


    /**
     * Allocate the resources for the Engine
     */
    /*  public void allocate() throws AudioException, EngineException,
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

          //Proccess allocation
          boolean result = handleAllocate();
          if (result == false) {
              states = setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
              postEngineEvent(states[0], states[1],
                              EngineEvent.ENGINE_DEALLOCATED);

              return;
          }

          // Subclasses with shared engines should check all states before
          // changing them here.
          synchronized (engineStateLock) {
              long newState = ALLOCATED | PAUSED | DEFOCUSED;
              states = setEngineState(CLEAR_ALL_STATE, newState);
          }
          postEngineEvent(states[0], states[1], EngineEvent.ENGINE_ALLOCATED);

      }*/


    /**
     * Deallocate the resources for the Engine and put it in the
     * DEALLOCATED state.
     */
    /*
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
        }*/


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
        if (grammars == null) {
            return;
        }
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
        Enumeration listeners = engineListeners.elements();
        while (listeners.hasMoreElements()) {
            EngineListener el = (EngineListener) listeners.nextElement();
            ((RecognizerListener) el).recognizerUpdate((RecognizerEvent) event);
        }
    }


    public void postEngineEvent(long oldState, long newState, int eventType) {
        postEngineEvent(oldState, newState, eventType, 0);
    }

    public void postEngineEvent(long oldState, long newState, int eventType,
                                long audioPosition) {
        final RecognizerEvent event = new RecognizerEvent(this,
                eventType,
                oldState,
                newState,
                null,
                null,
                audioPosition);
        postEngineEvent(event);
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
     *
     * @param grammarReference String
     * @param rootName String
     * @return RuleGrammar
     * @throws IllegalArgumentException
     * @throws EngineStateException
     * @throws EngineException
     */
    public RuleGrammar createRuleGrammar(String grammarReference,
                                         String rootName) throws
            IllegalArgumentException, EngineStateException, EngineException {

        //Validate current state
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        if (grammars.containsValue(grammarReference)) {
            throw new IllegalArgumentException("Duplicate grammar name: " +
                                               grammarReference);
        }

        //Create grammar
        BaseRuleGrammar brg = new BaseRuleGrammar(this, grammarReference);
        brg.setRoot(rootName);

        //Register it
        grammars.put(grammarReference, brg);

        return brg;
    }

    /**
     * Deletes a Grammar from this Recognizer.
     *
     * @param grammar Grammar
     * @throws IllegalArgumentException
     * @throws EngineStateException
     */
    public void deleteGrammar(Grammar grammar) throws IllegalArgumentException,
            EngineStateException {

        //Validate current state
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        if (!grammars.containsKey(grammar.getReference()))
            throw new IllegalArgumentException("The Grammar is unknown");

        uncommitedDeletedGrammars.add(grammar.getReference());
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
     * @todo Implement
     *
     * @throws EngineStateException
     */
    public void processGrammars() throws EngineStateException {

    }

    /**
     * Lists the Grammars known to this Recognizer
     *
     * @return Grammar[]
     * @throws EngineStateException
     */
    public Grammar[] listGrammars() throws EngineStateException {

        //Validate current state
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        //Get engine built-in grammrs
        List<Grammar> builInGrammars = getBuilInGrammars();

        if (grammars.size() < 0) {
            return new Grammar[0];
        }

        //Return an array of currently known grammars
        return (Grammar[])grammars.values().toArray(new Grammar[grammars.values().size()]);
    }

    /**
     * Gets the RuleGrammar with the specified grammarReference.
     *
     * @param grammarReference String
     * @return RuleGrammar
     * @throws EngineStateException
     */
    public RuleGrammar getRuleGrammar(String grammarReference) throws
            EngineStateException {

        //Validate current state
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        return (RuleGrammar) grammars.get(grammarReference);
    }

    /**
     * Loads a RuleGrammar from a URI or named resource.
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
        return loadRuleGrammar(grammarReference, true, false, null);
    }

    /**
     * Loads a RuleGrammar from a URI or named resource
     * and optionally loads any referenced Grammars.
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

        //Validate current state
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        //Make sure that recognizer supports markup
        if (getEngineMode().getMarkupSupport() == false) {
            throw new EngineException("Engine doesn't support markup");
        }

        //Proccess grammar
        URL url = new URL(grammarReference);
        InputStream grammarStream = url.openStream();
        SrgsRuleGrammarParser srgsParser = new SrgsRuleGrammarParser();
        Rule[] rules = srgsParser.load(grammarStream);
        if (rules != null) {
            //Initialize rule grammar
            BaseRuleGrammar brg = new BaseRuleGrammar(this, grammarReference);
            brg.addRules(rules);

            //Register grammar
            grammars.put(grammarReference, brg);

            return brg;
        }

        return null;
    }

    /**
     * Creates a RuleGrammar from grammar text provided by a Reader.
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

        //Validate current state
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        //Make sure that recognizer supports markup
        if (getEngineMode().getMarkupSupport() == false) {
            throw new EngineException("Engine doesn't support markup");
        }

        //Proccess grammar
        SrgsRuleGrammarParser srgsParser = new SrgsRuleGrammarParser();
        Rule[] rules = srgsParser.load(reader);
        if (rules != null) {
            //Initialize rule grammar
            BaseRuleGrammar brg = new BaseRuleGrammar(this, grammarReference);
            brg.addRules(rules);

            //Register grammar
            grammars.put(grammarReference, brg);

            return brg;
        }

        return null;
    }

    /**
     * Creates a RuleGrammar from grammar text provided as a String.
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
        return loadRuleGrammar(grammarReference, new StringReader(grammarText));
    }

    public void setGrammarMask(int mask) {
        grammarMask = mask;
    }

    public int getGrammarMask() {
        return grammarMask;
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
            long states[] = setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
            postEngineEvent(states[0], states[1],
                            EngineEvent.ENGINE_DEALLOCATED);
        }

        //Stops AudioManager
        audioManager.audioStop();
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
        boolean existChanges;

        existChanges = uncommitedDeletedGrammars.size() > 0;

        while (uncommitedDeletedGrammars.size() > 0) {
            existChanges = true;
            String grammarReference = uncommitedDeletedGrammars.
                                      remove(0);
            grammars.remove(grammarReference);
       }

        String[] newGrammars = new String[grammars.keySet().size()];

        Iterator it=grammars.keySet().iterator();

        for (int i=0; it.hasNext(); ++i){
            BaseRuleGrammar baseRuleGrammar = ((BaseRuleGrammar)grammars.get(it.next()));
            if (baseRuleGrammar.uncommitedChanges.size()>0){
                baseRuleGrammar.commitChanges();
            }
            newGrammars[i] = baseRuleGrammar.toString(false);
        }

        existChanges = existChanges || newGrammars.length > 0;

        if (existChanges){
            if (setGrammars(newGrammars)) {
                postEngineEvent(PAUSED, RESUMED, RecognizerEvent.CHANGES_COMMITTED);
                for (Grammar g : grammars.values()){
                    ((BaseGrammar) g).postGrammarEvent(speechEventExecutor,
                            new
                            GrammarEvent(g, GrammarEvent.GRAMMAR_CHANGES_COMMITTED));
                }
            }else{
                for (Grammar g : grammars.values()){
                    ((BaseGrammar) g).postGrammarEvent(speechEventExecutor,
                            new
                            GrammarEvent(g, GrammarEvent.GRAMMAR_CHANGES_REJECTED));
                }
            }
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

    abstract protected boolean setGrammars(String[] newGrammars);


    /**
     * @todo This is only not abstract not to break compatibilty.... for now
     *
     * Returns a list of engine built-in grammars
     * @return List
     */
    protected List<Grammar> getBuilInGrammars() {
        return new Vector<Grammar>();
    }


}
