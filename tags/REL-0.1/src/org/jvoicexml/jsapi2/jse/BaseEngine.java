/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
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
package org.jvoicexml.jsapi2.jse;

import java.util.ArrayList;
import java.util.List;

import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.Engine;
import javax.speech.EngineEvent;
import javax.speech.EngineException;
import javax.speech.EngineListener;
import javax.speech.EngineMode;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.VocabularyManager;

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
    protected final List<EngineListener> engineListeners;

    /**
     * The <code>AudioManager</code> for this <code>Engine</code>.
     */
    protected AudioManager audioManager;

    /**
     * The <code>EngineModeDesc</code> for this <code>Engine</code>.
     */
    protected EngineMode engineMode;

    /**
     * The <code>EngineProperties</code> for this <code>Engine</code>.
     */
    //protected EngineProperties engineProperties = null;

    /**
     * Event executor
     */
    protected SpeechEventExecutor speechEventExecutor;

    /**
     * Utility state for clearing the <code>engineState</code>.
     */
    protected final static long CLEAR_ALL_STATE = ~(0L);


    protected int engineMask = EngineEvent.DEFAULT_MASK;

    /**
     * Creates a new <code>Engine</code> in the
     * <code>DEALLOCATED</code> state.
     */
    public BaseEngine() {
        this(null, null);
    }

    /**
     * Creates a new <code>Engine</code> in the
     * <code>DEALLOCATED</code> state.
     *
     * @param desc the operating mode of this <code>Engine</code>
     */
    public BaseEngine(EngineMode engineMode, BaseAudioManager manager) {
        this.engineMode = engineMode;
        engineListeners = new ArrayList<EngineListener>();
        engineState = DEALLOCATED;
        engineStateLock = new Object();
        //engineProperties = createEngineProperties();
        setSpeechEventExecutor(null);
        manager.setEngine(this);
        audioManager = manager;
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
     * Blocks the calling Thread until the Engine is in a specified state.
     *
     * @param state long
     * @return long
     * @throws InterruptedException
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public long waitEngineState(long state) throws InterruptedException,
            IllegalArgumentException, IllegalStateException {
        return waitEngineState(state, 0);
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
    public long waitEngineState(long state, long timeout)
        throws InterruptedException, IllegalArgumentException,
            IllegalStateException {
        synchronized (engineStateLock) {
            if (!isValid(state)) {
                throw new IllegalArgumentException(
                        "Cannot wait for impossible state: "
                        + stateToString(state));
            }
            do {
                if (testEngineState(state)) {
                    return state;
                }

                if (!isReachable(state)) {
                    throw new IllegalStateException("State is not reachable: "
                            + stateToString(state));
                }

                //Wait for a state change
                if (timeout > 0) {
                    engineStateLock.wait(timeout);
                    return getEngineState();
                } else {
                    //Will wait forever to reach that state
                    engineStateLock.wait();
                }
            } while (true);
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
     * @throws EngineStateException if this <code>Engine</code> is in the
     *   <code>DEALLOCATING_RESOURCES</code> state
     */
    public void allocate() throws AudioException, EngineException,
            EngineStateException {
        //Validate current state
        if (testEngineState(ALLOCATED) ||
                testEngineState(ALLOCATING_RESOURCES)) {
            return;
        }

        checkEngineState(DEALLOCATING_RESOURCES);

        //Update current state
        long[] states = setEngineState(CLEAR_ALL_STATE, ALLOCATING_RESOURCES);
        postEngineEvent(states[0], states[1],
                EngineEvent.ENGINE_ALLOCATING_RESOURCES);

        //Handle engine allocation
        boolean success = false;
        try {
            //Handle allocate
            success = baseAllocate();
        } catch (AudioException ex) {
            throw ex;
        } catch (EngineException ex) {
            throw ex;
        } finally {
            if (success == false) {
                states = setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
                postEngineEvent(states[0], states[1],
                        EngineEvent.ENGINE_DEALLOCATED);
            }
        }
    }

    /**
     *
     *
     * @param mode int
     * @throws AudioException
     * @throws EngineException
     * @throws EngineStateException
     */
    public void allocate(int mode) throws AudioException, EngineException,
            EngineStateException {

        if (mode == ASYNCHRONOUS_MODE) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        allocate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, "Asynchronous allocate").start();
        }
        else {
            allocate();
        }
    }




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
     * @throws EngineStateException if this <code>Engine</code> is in the
     *   <code>ALLOCATING_RESOURCES</code> state
     */
    public void deallocate() throws AudioException, EngineException, EngineStateException {

        //Validate current state
        if (testEngineState(DEALLOCATED) || testEngineState(DEALLOCATING_RESOURCES))  return;

        checkEngineState(ALLOCATING_RESOURCES);

        //Update current state
        long[] states = setEngineState(CLEAR_ALL_STATE,
                                       DEALLOCATING_RESOURCES);
        postEngineEvent(states[0], states[1], EngineEvent.ENGINE_DEALLOCATING_RESOURCES);
        baseDeallocate();
    }


    /**
     *
     *
     * @param mode int
     * @throws AudioException
     * @throws EngineException
     * @throws EngineStateException
     */

    public void deallocate(int mode) throws AudioException, EngineException,
            EngineStateException {

        if (mode == ASYNCHRONOUS_MODE) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        deallocate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, "Asynchronous deallocate").start();
        }
        else {
            deallocate();
        }
    }


    /**
     * Pauses the audio stream for this <code>Engine</code> and put
     * this <code>Engine</code> into the <code>PAUSED</code> state.
     *
     * @throws EngineStateException if this <code>Engine</code> is in the
     *   <code>DEALLOCATING_RESOURCES</code> or
     *   <code>DEALLOCATED</code> state.
     */
    public void pause() throws EngineStateException {

        //Validate current state
        if (testEngineState(PAUSED)) return;

        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        //Handle pause
        boolean status = basePause();
        if (status == true) {
            long[] states = setEngineState(RESUMED, PAUSED);
            postEngineEvent(states[0], states[1], EngineEvent.ENGINE_PAUSED);
        }
    }


    /**
     * Resumes the audio stream for this <code>Engine</code> and put
     * this <code>Engine</code> into the <code>RESUMED</code> state.
     *
     * @throws EngineStateException if this <code>Engine</code> is in the
     *   <code>DEALLOCATING_RESOURCES</code> or
     *   <code>DEALLOCATED</code> state
     */
    public boolean resume() throws EngineStateException {

        //Validate current state
        if (testEngineState(RESUMED)) return true;

        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        //Handle resume
        if (baseResume()) {
            long[] states = setEngineState(PAUSED, RESUMED);
            postEngineEvent(states[0], states[1], EngineEvent.ENGINE_RESUMED);

            return true;
        }
        else {
            return false;
        }
    }


    /**
     * Returns an object that provides management of the audio input
     * or output of this <code>Engine</code>.
     *
     * @return the audio manader for this <code>Engine</code>
     */
    public AudioManager getAudioManager() {
        return audioManager;
    }

    /**
     * Returns an object that provides management of the vocabulary for
     * this <code>Engine</code>.  Returns <code>null</code> if this
     * <code>Engine</code> does not support vocabulary management.
     *
     * @return the vocabulary manager of this <code>Engine</code>
     *
     * @throws EngineStateException if this <code>Engine</code> in the
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
        else {
            if (this.speechEventExecutor instanceof BaseSpeechEventExecutor) {
                ((BaseSpeechEventExecutor)this.speechEventExecutor).terminate();
            }
            this.speechEventExecutor = speechEventExecutor;
        }
    }

    /**
     * Requests notification of <code>EngineEvents</code> from this
     * <code>Engine</code>.
     *
     * @param listener the listener to add.
     */
    public void addEngineListener(EngineListener listener) {
        synchronized (engineListeners) {
            if (!engineListeners.contains(listener)) {
                engineListeners.add(listener);
            }
        }
    }

    /**
     * Removes an <code>EngineListener</code> from the list of
     * <code>EngineListeners</code>.
     *
     * @param listener the listener to remove.
     */
    public void removeEngineListener(EngineListener listener) {
        synchronized (engineListeners) {
            engineListeners.remove(listener);
        }
    }

    public void setEngineMask(int mask) {
        engineMask = mask;
    }

    public int getEngineMask() {
        return engineMask;
    }

    /**
     * Adds an event to SpeechEventExecutor
     *
     * @param event EngineEvent
     */
    protected void postEngineEvent(final EngineEvent event) {
        if ((getEngineMask() & event.getId()) == event.getId()) {
            try {
                speechEventExecutor.execute(new Runnable() {
                    public void run() {
                        fireEvent(event);
                    }
                });
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Factory constructor for EngineProperties object.
     *
     * @return a <code>BaseEngineProperties</code> object specific to
     *   a subclass.
     */
   // abstract protected BaseEngineProperties createEngineProperties();

    /**
     * Convenience method that throws an <code>EngineStateException</code>
     * if any of the bits in the passed state are set in the
     * <code>state</code>.
     *
     * @param state the <code>Engine</code> state to check
     *
     * @throws EngineStateException if any of the bits in the passed state
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
        if ((state & Engine.DEALLOCATED) != 0) {
            buf.append("DEALLOCATED");
        }
        if ((state & Engine.ALLOCATING_RESOURCES) != 0) {
            if (buf.length() > 0) {
                buf.append(' ');
            }
            buf.append("ALLOCATING_RESOURCES");
        }
        if ((state & Engine.ALLOCATED) != 0) {
            if (buf.length() > 0) {
                buf.append(' ');
            }
            buf.append("ALLOCATED");
        }
        if ((state & Engine.DEALLOCATING_RESOURCES) != 0) {
            if (buf.length() > 0) {
                buf.append(' ');
            }
            buf.append("DEALLOCATING_RESOURCES");
        }
        if ((state & Engine.PAUSED) != 0) {
            if (buf.length() > 0) {
                buf.append(' ');
            }
            buf.append("PAUSED");
        }
        if ((state & Engine.RESUMED) != 0) {
            if (buf.length() > 0) {
                buf.append(' ');
            }
            buf.append("RESUMED");
        }
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

    /**
     * @todo Finish validation
     *
     * @param state long
     * @return boolean
     */
    protected boolean isValid(long state) {
        if (testEngineState(PAUSED | RESUMED)) return false;

        if (testEngineState(FOCUSED | DEFOCUSED)) return false;

        return true;
    }


    /**
     * @todo Finish conditions
     *
     * @param state long
     * @return boolean
     */
    protected boolean isReachable(long state) {
        if ((state & ERROR_OCCURRED) == ERROR_OCCURRED)
            return false;

        if (testEngineState(ALLOCATED) == false) {
            if (((state & RESUMED) == RESUMED) ||
                ((state & PAUSED) == PAUSED)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Called from the {@link #allocate()} method.
     * Override this in subclasses.
     *
     * @see #allocate
     *
     * @throws EngineException if problems are encountered
     */
    abstract protected boolean baseAllocate()
        throws EngineStateException, EngineException, AudioException;

    /**
     * Called from the <code>deallocate</code> method.  Override this in
     * subclasses.
     *
     * @throws EngineException if this <code>Engine</code> cannot be
     *   deallocated.
     */
    abstract protected boolean baseDeallocate() throws EngineStateException, EngineException, AudioException;

    /**
     * Called from the <code>pause</code> method.  Override this in subclasses.
     */
    abstract protected boolean basePause();

    /**
     * Called from the <code>resume</code> method.  Override in subclasses.
     */
    abstract protected boolean baseResume();

    abstract protected void fireEvent(EngineEvent event);

    abstract protected void postEngineEvent(long oldState, long newState, int eventType);

}
