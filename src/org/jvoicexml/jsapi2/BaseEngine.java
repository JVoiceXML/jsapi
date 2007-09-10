/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: $
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
package org.jvoicexml.jsapi2;

import javax.speech.Engine;
import javax.speech.EngineEvent;
import javax.speech.EngineListener;
import javax.speech.EngineProperties;
import javax.speech.EngineException;
import javax.speech.AudioManager;
import javax.speech.AudioException;
import javax.speech.EngineStateException;
import javax.speech.VocabularyManager;
import javax.speech.EngineMode;
import java.util.Vector;
import javax.speech.SpeechEventExecutor;

/**
 * Supports the JSAPI 2.0 <code>Engine</code> interface.
 * Actual JSAPI implementations might want to extend or modify this
 * implementation.
 */
abstract public class BaseEngine implements Engine {
    /**
     * A bitmask holding the current state of this <code>Engine</code>.
     */
    protected long engineState;

    /**
     * An <code>Object</code> used for synchronizing access to
     * <code>engineState</code>.
     * @see #engineState
     */
    protected Object engineStateLock;

    /**
     * List of <code>EngineListeners</code> registered for
     * <code>EngineEvents</code> on this <code>Engine</code>.
     */
    protected Vector engineListeners;

    /**
     * The <code>AudioManager</code> for this <code>Engine</code>.
     */
    protected AudioManager audioManager = null;

    /**
     * The <code>EngineModeDesc</code> for this <code>Engine</code>.
     */
    protected EngineMode engineMode = null;

    /**
     * The <code>EngineProperties</code> for this <code>Engine</code>.
     */
    //protected EngineProperties engineProperties = null;

    /**
     * Event executor
     */
    protected SpeechEventExecutor speechEventExecutor = null;

    /**
     * Utility state for clearing the <code>engineState</code>.
     */
    protected final static long CLEAR_ALL_STATE = ~(0L);

    /**
     * Creates a new <code>Engine</code> in the
     * <code>DEALLOCATED</code> state.
     */
    public BaseEngine() {
        this(null);
    }

    /**
     * Creates a new <code>Engine</code> in the
     * <code>DEALLOCATED</code> state.
     *
     * @param desc the operating mode of this <code>Engine</code>
     */
    public BaseEngine(EngineMode engineMode) {
        this.engineMode = engineMode;
        engineListeners = new Vector();
        engineState = DEALLOCATED;
        engineStateLock = new Object();
        //engineProperties = createEngineProperties();
        setSpeechEventExecutor(null);
    }

    /**
     * Returns a or'ed set of flags indicating the current state of
     * this <code>Engine</code>.
     *
     * <p>An <code>EngineEvent</code> is issued each time this
     * <code>Engine</code> changes state.
     *
     * <p>The <code>getEngineState</code> method can be called successfully
     * in any <code>Engine</code> state.
     *
     * @return the current state of this <code>Engine</code>
     *
     * @see #getEngineState
     * @see #waitEngineState
     */
    public long getEngineState() {
        return engineState;
    }

    /**
     * Blocks the calling thread until this <code>Engine</code>
     * is in a specified state.
     *
     * <p>All state bits specified in the <code>state</code> parameter
     * must be set in order for the method to return, as defined
     * for the <code>testEngineState</code> method.  If the <code>state</code>
     * parameter defines an unreachable state
     * (e.g. <code>PAUSED | RESUMED</code>) an exception is thrown.
     *
     * <p>The <code>waitEngineState</code> method can be called successfully
     * in any <code>Engine</code> state.
     *
     * @param state a bitmask of the state to wait for
     *
     * @see #testEngineState
     * @see #getEngineState
     *
     * @throws InterruptedException
     *   if another thread has interrupted this thread.
     * @throws IllegalArgumentException
     *   if the specified state is unreachable
     */
    public void waitEngineState(long state) {
        synchronized (engineStateLock) {
            while (!testEngineState(state)) {
                try {
                    engineStateLock.wait();
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    /**
     * Returns <code>true</code> if this state of this
     * <code>Engine</code> matches the specified state.
     *
     * <p>The test performed is not an exact match to the current
     * state.  Only the specified states are tested.  For
     * example the following returns true only if the
     * <code>Synthesizer</code> queue is empty, irrespective
     * of the pause/resume and allocation states.
     *
     * <PRE>
     *    if (synth.testEngineState(Synthesizer.QUEUE_EMPTY)) ...
     * </PRE>
     *
     * <p>The <code>testEngineState</code> method is equivalent to:
     *
     * <PRE>
     *      if ((engine.getEngineState() & state) == state)
     * </PRE>
     *
     * <p>The <code>testEngineState</code> method can be called
     * successfully in any <code>Engine</code> state.
     *
     * @param state a bitmask of the states to test for
     *
     * @return <code>true</code> if this <code>Engine</code> matches
     *   <code>state</code>; otherwise <code>false</code>
     * @throws IllegalArgumentException
     *   if the specified state is unreachable
     */
    public boolean testEngineState(long state) throws IllegalArgumentException {
        return ((getEngineState() & state) == state);
    }

    /**
     * Updates this <code>Engine</code> state by clearing defined bits,
     * then setting other specified bits.
     *
     * @return a length-2 array with old and new state values.
     */
    protected long[] setEngineState(long clear, long set) {
        long states[] = new long[2];
        synchronized (engineStateLock) {
            states[0] = engineState;
            engineState = engineState & (~clear);
            engineState = engineState | set;
            states[1] = engineState;
            engineStateLock.notifyAll();
        }
        return states;
    }

    /**
     * Allocates the resources required for this <code>Engine</code> and
     * puts it into the <code>ALLOCATED</code> state.  When this method
     * returns successfully the <code>ALLOCATED</code> bit of this
     * <code>Engine</code> state is set, and the
     * <code>testEngineState(Engine.ALLOCATED)</code> method returns
     * <code>true</code>.
     *
     * <p>During the processing of the method, this <code>Engine</code> is
     * temporarily in the <code>ALLOCATING_RESOURCES</code> state.
     *
     * @see #deallocate
     *
     * @throws EngineException if this <code>Engine</code> cannot be allocated
     * @throws EngineStateError if this <code>Engine</code> is in the
     *   <code>DEALLOCATING_RESOURCES</code> state
     */
    public void allocate() throws AudioException, EngineException,
            EngineStateException {
        if (testEngineState(ALLOCATED)) {
            return;
        }

        long[] states = setEngineState(CLEAR_ALL_STATE, ALLOCATING_RESOURCES);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_ALLOCATING_RESOURCES);

        handleAllocate();
    }

    /**
     *
     * @todo Implement mode
     *
     * @param mode int
     * @throws AudioException
     * @throws EngineException
     * @throws EngineStateException
     */
    public void allocate(int mode) throws AudioException, EngineException,
            EngineStateException {
        if (testEngineState(ALLOCATED)) {
            return;
        }

        long[] states = setEngineState(CLEAR_ALL_STATE, ALLOCATING_RESOURCES);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_ALLOCATING_RESOURCES);

        handleAllocate();
    }


    /**
     * Called from the <code>allocate</code> method.  Override this in
     * subclasses.
     *
     * @see #allocate
     *
     * @throws EngineException if problems are encountered
     */
    abstract protected boolean handleAllocate() throws EngineException;

    /**
     * Frees the resources of this <code>Engine</code> that were
     * acquired during allocation and during operation and return this
     * <code>Engine</code> to the <code>DEALLOCATED</code>.  When this
     * method returns the <code>DEALLOCATED</code> bit of this
     * <code>Engine</code> state is set so the
     * <code>testEngineState(Engine.DEALLOCATED)</code> method returns
     * <code>true</code>.
     *
     * <p>During the processing of the method, this
     * <code>Engine</code> is temporarily in the
     * <code>DEALLOCATING_RESOURCES</code> state.
     *
     * <p>A deallocated engine can be re-started with a subsequent
     * call to <code>allocate</code>.
     *
     * @see #allocate
     *
     * @throws EngineException if this <code>Engine</code> cannot be
     *   deallocated
     * @throws EngineStateError if this <code>Engine</code> is in the
     *   <code>ALLOCATING_RESOURCES</code> state
     */
    public void deallocate() throws EngineException, EngineStateException {
        if (testEngineState(DEALLOCATED)) {
            return;
        }

        long[] states = setEngineState(CLEAR_ALL_STATE,
                                       DEALLOCATING_RESOURCES);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_DEALLOCATING_RESOURCES);

        handleDeallocate();
    }


    /**
     *
     * @todo Implement mode
     *
     * @param mode int
     * @throws AudioException
     * @throws EngineException
     * @throws EngineStateException
     */

    public void deallocate(int mode) throws AudioException, EngineException,
            EngineStateException {

        if (testEngineState(DEALLOCATED)) {
            return;
        }

        long[] states = setEngineState(CLEAR_ALL_STATE,
                                       DEALLOCATING_RESOURCES);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_DEALLOCATING_RESOURCES);

        handleDeallocate();
    }


    /**
     * Called from the <code>deallocate</code> method.  Override this in
     * subclasses.
     *
     * @throws EngineException if this <code>Engine</code> cannot be
     *   deallocated.
     */
    abstract protected boolean handleDeallocate() throws EngineException;

    /**
     * Pauses the audio stream for this <code>Engine</code> and put
     * this <code>Engine</code> into the <code>PAUSED</code> state.
     *
     * @throws EngineStateError if this <code>Engine</code> is in the
     *   <code>DEALLOCATING_RESOURCES</code> or
     *   <code>DEALLOCATED</code> state.
     */
    public void pause() throws EngineStateException {
        synchronized (engineStateLock) {
            checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

            if (testEngineState(PAUSED)) {
                return;
            }

            handlePause();

            long[] states = setEngineState(RESUMED, PAUSED);
            postEngineEvent(states[0], states[1], EngineEvent.ENGINE_PAUSED);
        }
    }

    /**
     * Called from the <code>pause</code> method.  Override this in subclasses.
     */
    abstract protected boolean handlePause();

    /**
     * Resumes the audio stream for this <code>Engine</code> and put
     * this <code>Engine</code> into the <code>RESUMED</code> state.
     *
     * @throws AudioException if unable to gain access to the audio channel
     * @throws EngineStateError if this <code>Engine</code> is in the
     *   <code>DEALLOCATING_RESOURCES</code> or
     *   <code>DEALLOCATED</code> state
     */
    public boolean resume() throws EngineStateException {
        synchronized (engineStateLock) {
            checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

            if (testEngineState(RESUMED))
                return true;

            if (handleResume()) {
                long[] states = setEngineState(PAUSED, RESUMED);
                postEngineEvent(states[0], states[1], EngineEvent.ENGINE_RESUMED);

                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Called from the <code>resume</code> method.  Override in subclasses.
     */
    abstract protected boolean handleResume();

    /**
     * Returns an object that provides management of the audio input
     * or output of this <code>Engine</code>.
     *
     * @return the audio manader for this <code>Engine</code>
     */
    public AudioManager getAudioManager() {
        if (audioManager == null) {
            audioManager = new BaseAudioManager();
        }
        return audioManager;
    }

    /**
     * Returns an object that provides management of the vocabulary for
     * this <code>Engine</code>.  Returns <code>null</code> if this
     * <code>Engine</code> does not support vocabulary management.
     *
     * @return the vocabulary manager of this <code>Engine</code>
     *
     * @throws EngineStateError if this <code>Engine</code> in the
     *   <code>DEALLOCATING_RESOURCES</code> or
     *   <code>DEALLOCATED</code> state
     */
    public VocabularyManager getVocabularyManager() throws EngineStateException {
        return null;
    }

    /**
     * Gets the <code>EngineProperties</code> of this <code>Engine</code>.
     * Must be set in subclasses.
     *
     * @return the <code>EngineProperties</code> of this <code>Engine</code>.
     */
 /*   public EngineProperties getEngineProperties() {
        return engineProperties;
    }*/

    /**
     * Gets the current operating properties and mode of
     * this <code>Engine</code>.
     *
     * @return the operating mode of this <code>Engine</code>
     *
     * @throws SecurityException
     */
    public EngineMode getEngineMode() {
        return engineMode;
    }

    /**
     * Sets the current operating properties and mode of
     * this <code>Engine</code>.
     *
     * @param desc the new operating mode of this <code>Engine</code>
     */
    protected void setEngineMode(EngineMode engineMode) {
        this.engineMode = engineMode;
    }

    /**
     * Sets the SpeechEventExecutor used to fire events for this engine.
     *
     * @return SpeechEventExecutor
     */
    public SpeechEventExecutor getSpeechEventExecutor() {
        return speechEventExecutor;
    }

    public void setSpeechEventExecutor(SpeechEventExecutor speechEventExecutor) {
        if (speechEventExecutor == null) {
            this.speechEventExecutor = new BaseSpeechEventExecutor();
        }
        this.speechEventExecutor = speechEventExecutor;
    }

    /**
     * Requests notification of <code>EngineEvents</code> from this
     * <code>Engine</code>.
     *
     * @param listener the listener to add.
     */
    public void addEngineListener(EngineListener listener) {
        if (!engineListeners.contains(listener)) {
            engineListeners.addElement(listener);
        }
    }

    /**
     * Removes an <code>EngineListener</code> from the list of
     * <code>EngineListeners</code>.
     *
     * @param listener the listener to remove.
     */
    public void removeEngineListener(EngineListener listener) {
        engineListeners.removeElement(listener);
    }


    /**
     * Adds an event to SpeechEventExecutor
     *
     * @param event EngineEvent
     */
    protected void postEngineEvent(final EngineEvent event) {
        try {
            speechEventExecutor.execute(new Runnable() {
                public void run() {
                    fireEvent(event);
                }
            });
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    abstract public void fireEvent(EngineEvent event);

    abstract protected void postEngineEvent(long oldState, long newState, int eventType);





    /**
     * Factory constructor for EngineProperties object.
     *
     * @return a <code>BaseEngineProperties</code> object specific to
     *   a subclass.
     */
   // abstract protected BaseEngineProperties createEngineProperties();

    /**
     * Convenience method that throws an <code>EngineStateError</code>
     * if any of the bits in the passed state are set in the
     * <code>state</code>.
     *
     * @param state the <code>Engine</code> state to check
     *
     * @throws EngineStateError if any of the bits in the passed state
     *   are set in the <code>state</code>
     */
    protected void checkEngineState(long state) throws EngineStateException {
        long currentState = getEngineState();
        if ((currentState & state) != 0) {
            throw new EngineStateException
                    ("Invalid EngineState: expected=("
                     + stateToString(state) + ") current state=("
                     + stateToString(currentState) + ")");
        }
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
        StringBuffer buf = new StringBuffer();
        if ((state & Engine.DEALLOCATED) != 0)
            buf.append(" DEALLOCATED ");
        if ((state & Engine.ALLOCATING_RESOURCES) != 0)
            buf.append(" ALLOCATING_RESOURCES ");
        if ((state & Engine.ALLOCATED) != 0)
            buf.append(" ALLOCATED ");
        if ((state & Engine.DEALLOCATING_RESOURCES) != 0)
            buf.append(" DEALLOCATING_RESOURCES ");
        if ((state & Engine.PAUSED) != 0)
            buf.append(" PAUSED ");
        if ((state & Engine.RESUMED) != 0)
            buf.append(" RESUMED ");
        return buf.toString();
    }



    /**
     * Returns the engine name and mode for debug purposes.
     *
     * @return the engine name and mode.
     */
    public String toString() {
        return getEngineMode().getEngineName() +
                ":" + getEngineMode().getModeName();
    }
}
