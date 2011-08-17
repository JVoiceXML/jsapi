/**
 * 
 */
package org.jvoicexml.jsapi2.test;

import javax.speech.Engine;
import javax.speech.EngineEvent;

/**
 * Dummy implementation of an engine event for engine-type independent tests.
 * @author Dirk Schnelle-Walka
 */
public class DummyEngineEvent extends EngineEvent {

    /**
     * Constructs a new object.
     * @param source
     * @param id
     * @param oldEngineState
     * @param newEngineState
     * @param problem
     * @throws IllegalArgumentException
     */
    public DummyEngineEvent(Engine source, int id, long oldEngineState,
            long newEngineState, Throwable problem)
            throws IllegalArgumentException {
        super(source, id, oldEngineState, newEngineState, problem);
    }

}
