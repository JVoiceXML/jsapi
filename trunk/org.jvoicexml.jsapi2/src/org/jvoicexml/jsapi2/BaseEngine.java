/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
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

import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.Engine;
import javax.speech.EngineEvent;
import javax.speech.EngineException;
import javax.speech.EngineListener;
import javax.speech.EngineMode;
import javax.speech.EngineProperties;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.VocabularyManager;

/**
 * Supports the JSAPI 2.0 {@link Engine} interface.
 * <p>
 * Actual JSAPI implementations might want to extend or modify this
 * implementation.
 * </p>
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 * @version $Revision$
 */
public abstract class BaseEngine implements Engine {

    /**
     * A bitmask holding the current state of this <code>Engine</code>.
     */
    private long engineState;

    /**
     * An <code>Object</code> used for synchronizing access to
     * <code>engineState</code>.
     * @see #engineState
     */
    protected Object engineStateLock;

    /**
     * List of <code>EngineListeners</code> registered for
     * <code>EngineEvents</code> on this <code>Engine</code>.
     * <p>
     * For a synthesizer this will contain only
     * {@link javax.speech.synthesis.SynthesizerListener}s
     * and for a recognizer only
     * {@link javax.speech.recognition.RecognizerListener}s.
     */
    protected final Vector engineListeners;

    /**
     * The <code>AudioManager</code> for this <code>Engine</code>.
     */
    private AudioManager audioManager;

    /**
     * The <code>EngineModeDesc</code> for this <code>Engine</code>.
     */
    private EngineMode engineMode;

    /** The current speech event executor. */
    private SpeechEventExecutor speechEventExecutor;

    /**
     * Utility state for clearing the <code>engineState</code>.
     */
    protected static final long CLEAR_ALL_STATE = ~(0L);

    /** The engine mask. */
    private int engineMask = EngineEvent.DEFAULT_MASK;

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
     * @param mode the operating mode of this <code>Engine</code>
     * @param manager the audio manager
     */
    public BaseEngine(final EngineMode mode, final AudioManager manager) {
        engineMode = mode;
        engineListeners = new Vector();
        engineState = DEALLOCATED;
        engineStateLock = new Object();
        audioManager = manager;
        speechEventExecutor = new ThreadSpeechEventExecutor();
    }

    /**
     * Adds the given {@link EnginePropertyChangeRequestListener} that is
     * retrieved from {@link #getChangeRequestListener()} to the list of
     * known listeners of the {@link BaseEngineProperties}.
     *
     * @param properties
     *            the properties to add the listener
     */
    protected final void addEnginePropertyChangeRequestListener(
            final BaseEngineProperties properties) {
        final EnginePropertyChangeRequestListener listener =
            getChangeRequestListener();
        if (listener != null) {
            properties.addEnginePropertyChangeRequestListener(listener);
        }
    }

    /**
     * Retrieves the {@link EnginePropertyChangeRequestListener} that should
     * receive notifications if there are any change requests to the
     * {@link EngineProperties}. This method is called after the
     * {@link EngineProperties} object is created.
     * @return the listener for change request notifications,
     *         <code>null</code> if the underlying engine does not support
     *         change requests to the {@link EngineProperties}.
     */
    protected abstract EnginePropertyChangeRequestListener
        getChangeRequestListener();

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
    public final long getEngineState() {
        return engineState;
    }

    /**
     * {@inheritDoc}
     */
    public final long waitEngineState(final long state)
        throws InterruptedException {
        return waitEngineState(state, 0);
    }

    /**
     * {@inheritDoc}
     */
    public final long waitEngineState(final long state, final long timeout)
        throws InterruptedException {
        synchronized (engineStateLock) {
            if (!isValid(state)) {
                throw new IllegalArgumentException(
                        "Cannot wait for impossible state: "
                        + stateToString(state));
            }
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
                return getEngineState();
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
     * {@inheritDoc}
     */
    public final boolean testEngineState(final long state) {
        return (getEngineState() & state) == state;
    }

    /**
     * Updates this <code>Engine</code> state by clearing defined bits,
     * then setting other specified bits.
     * @param clear the flags to clear
     * @param set the flags to set
     * @return a length-2 array with old and new state values.
     */
    protected long[] setEngineState(final long clear, final long set) {
        long[] states = new long[2];
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
     * {@inheritDoc}
     */
    public void allocate() throws AudioException, EngineException {
        //Validate current state
        if (testEngineState(ALLOCATED)
                || testEngineState(ALLOCATING_RESOURCES)) {
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
        } finally {
            if (!success) {
                states = setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
                postEngineEvent(states[0], states[1],
                        EngineEvent.ENGINE_DEALLOCATED);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void allocate(final int mode)
        throws AudioException, EngineException {
        if (mode == ASYNCHRONOUS_MODE) {
            final Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        allocate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            (new Thread(runnable)).start();
        } else {
            allocate();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deallocate()
        throws AudioException, EngineException {

        //Validate current state
        if (testEngineState(DEALLOCATED)
                || testEngineState(DEALLOCATING_RESOURCES)) {
            return;
        }

        checkEngineState(ALLOCATING_RESOURCES);

        //Update current state
        long[] states = setEngineState(CLEAR_ALL_STATE,
                                       DEALLOCATING_RESOURCES);
        postEngineEvent(states[0], states[1],
                EngineEvent.ENGINE_DEALLOCATING_RESOURCES);
        baseDeallocate();
    }


    /**
     * {@inheritDoc}
     */
    public void deallocate(final int mode)
        throws AudioException, EngineException {
        if (mode == ASYNCHRONOUS_MODE) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        deallocate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        } else {
            deallocate();
        }
    }


    /**
     * {@inheritDoc}
     */
    public void pause() {
        //Validate current state
        if (testEngineState(PAUSED)) {
            return;
        }

        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
                return;
            }
        }

        //Handle pause
        boolean status = basePause();
        if (status) {
            long[] states = setEngineState(RESUMED, PAUSED);
            postEngineEvent(states[0], states[1], EngineEvent.ENGINE_PAUSED);
        }
    }


    /**
     * {@inheritDoc}
     */
    public boolean resume() {

        //Validate current state
        if (testEngineState(RESUMED)) {
            return true;
        }

        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
                return false;
            }
        }

        //Handle resume
        if (baseResume()) {
            long[] states = setEngineState(PAUSED, RESUMED);
            postEngineEvent(states[0], states[1], EngineEvent.ENGINE_RESUMED);
            return true;
        } else {
            return false;
        }
    }


    /**
     * {@inheritDoc}
     */
    public AudioManager getAudioManager() {
        return audioManager;
    }

    /**
     * {@inheritDoc}
     */
    public VocabularyManager getVocabularyManager() {
        return null;
    }

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
     * @param mode the new operating mode of this <code>Engine</code>
     */
    protected void setEngineMode(final EngineMode mode) {
        engineMode = mode;
    }

    /**
     * {@inheritDoc}
     */
    public SpeechEventExecutor getSpeechEventExecutor() {
        return speechEventExecutor;
    }

    /**
     * {@inheritDoc}
     */
    public void setSpeechEventExecutor(
            final SpeechEventExecutor executor) {
        if (executor == null) {
            speechEventExecutor = new ThreadSpeechEventExecutor();
        } else {
            // Terminate a previously running executor.
            if (speechEventExecutor instanceof ThreadSpeechEventExecutor) {
                final ThreadSpeechEventExecutor baseExecutor =
                    (ThreadSpeechEventExecutor) this.speechEventExecutor;
                baseExecutor.terminate();
            }
            speechEventExecutor = executor;
        }
    }

    /**
     * Requests notification of <code>EngineEvents</code> from this
     * <code>Engine</code>.
     *
     * @param listener the listener to add.
     */
    protected void addEngineListener(final EngineListener listener) {
        synchronized (engineListeners) {
            if (!engineListeners.contains(listener)) {
                engineListeners.addElement(listener);
            }
        }
    }

    /**
     * Removes an <code>EngineListener</code> from the list of
     * <code>EngineListeners</code>.
     *
     * @param listener the listener to remove.
     */
    protected void removeEngineListener(final EngineListener listener) {
        synchronized (engineListeners) {
            engineListeners.removeElement(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setEngineMask(final int mask) {
        engineMask = mask;
    }

    /**
     * {@inheritDoc}
     */
    public int getEngineMask() {
        return engineMask;
    }

    /**
     * Posts the given event using the current {@link SpeechEventExecutor}.
     *
     * @param event the engine event to post.
     */
    protected void postEngineEvent(final EngineEvent event) {
        final int id = event.getId();
        if ((getEngineMask() & id) == id) {
            speechEventExecutor.execute(new Runnable() {
                public void run() {
                    fireEvent(event);
                }
            });
        }
    }


    /**
     * Convenience method that throws an <code>EngineStateException</code>
     * if any of the bits in the passed state are set in the
     * <code>state</code>.
     *
     * @param state the <code>Engine</code> state to check
     * @exception EngineStateException
     *            state is not the expected state
     */
    protected void checkEngineState(final long state)
        throws EngineStateException {
        long currentState = getEngineState();
        if ((currentState & state) != 0) {
            throw new EngineStateException("Invalid EngineState: expected=("
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
    protected String stateToString(final long state) {
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
        return getEngineMode().getEngineName()
                + ":" + getEngineMode().getModeName();
    }

    /**
     * @todo Finish validation
     *
     * @param state long
     * @return boolean
     */
    protected boolean isValid(final long state) {
        if (testEngineState(PAUSED | RESUMED)) {
            return false;
        }

        if (testEngineState(FOCUSED | DEFOCUSED)) {
            return false;
        }

        return true;
    }


    /**
     * @todo Finish conditions
     *
     * @param state long
     * @return boolean
     */
    protected boolean isReachable(final long state) {
        if ((state & ERROR_OCCURRED) == ERROR_OCCURRED) {
            return false;
        }

        if (!testEngineState(ALLOCATED)) {
            if (((state & RESUMED) == RESUMED)
                || ((state & PAUSED) == PAUSED)) {
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
    protected abstract boolean baseAllocate()
        throws EngineStateException, EngineException, AudioException;

    /**
     * Called from the <code>deallocate</code> method.  Override this in
     * subclasses.
     *
     * @throws EngineException if this <code>Engine</code> cannot be
     *   deallocated.
     */
    protected abstract boolean baseDeallocate() throws EngineStateException, EngineException, AudioException;

    /**
     * Called from the <code>pause</code> method.  Override this in subclasses.
     */
    protected abstract boolean basePause();

    /**
     * Called from the <code>resume</code> method.  Override in subclasses.
     */
    protected abstract boolean baseResume();

    /**
     * Notifies all listeners about the given event. This method is
     * being called using the currently configured {@link SpeechEventExecutor}.
     * <p>
     * This is needed since the event listeners for the synthesizer and
     * the recognizer have different notification signatures.
     * </p>
     * @param event the event
     */
    protected abstract void fireEvent(final EngineEvent event);

    protected abstract void postEngineEvent(final long oldState,
            final long newState, final int eventType);

}
