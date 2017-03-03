/**
 * 
 */
package org.jvoicexml.jsapi2;

import javax.speech.Engine;
import javax.speech.EngineEvent;
import javax.speech.SpeechEventExecutor;

import junit.framework.TestCase;

import org.jvoicexml.jsapi2.mock.EngineEventAccumulator;
import org.jvoicexml.jsapi2.mock.synthesis.MockSynthesizer;

/**
 * State transition test cases for {@link BaseEngine}.
 * @author Dirk Schnelle-Walka
 *
 */
public final class BaseEngineStateTransitionTest extends TestCase {
    /** The engine to test. */
    private Engine engine;

    /** Collector for issued events. */
    private EngineEventAccumulator eventAccu;

    /**
     * Set up the test environment.
     * @exception Exception set up failed
     */
    protected void setUp() throws Exception {
        final MockSynthesizer synthesizer = new MockSynthesizer();
        synthesizer.setEngineMask(EngineEvent.DEFAULT_MASK
                | EngineEvent.ENGINE_ALLOCATING_RESOURCES
                | EngineEvent.ENGINE_DEALLOCATING_RESOURCES);
        eventAccu = new EngineEventAccumulator();
        synthesizer.addSynthesizerListener(eventAccu);
        engine = synthesizer;
        final SpeechEventExecutor executor =
                new SynchronousSpeechEventExecutor();
        engine.setSpeechEventExecutor(executor);
        super.setUp();
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.BaseEngine#allocate(int)}.
     * @throws Exception test failed
     */
    public void testAllocate() throws Exception {
        engine.allocate();
        engine.waitEngineState(Engine.ALLOCATED);
        final EngineEvent[] events = eventAccu.getEvents();
        assertEquals(2, events.length);
        assertEquals(EngineEvent.ENGINE_ALLOCATING_RESOURCES,
                events[0].getId());
        assertEquals(EngineEvent.ENGINE_ALLOCATED, events[1].getId());
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.BaseEngine#allocate(int)}.
     * @throws Exception test failed
     */
    public void testDeallocate() throws Exception {
        engine.allocate();
        engine.waitEngineState(Engine.ALLOCATED);
        engine.deallocate();
        engine.waitEngineState(Engine.DEALLOCATED);
        final EngineEvent[] events = eventAccu.getEvents();
        assertEquals(4, events.length);
        assertEquals(EngineEvent.ENGINE_ALLOCATING_RESOURCES,
                events[0].getId());
        assertEquals(EngineEvent.ENGINE_ALLOCATED, events[1].getId());
        assertEquals(EngineEvent.ENGINE_DEALLOCATING_RESOURCES,
                events[2].getId());
        assertEquals(EngineEvent.ENGINE_DEALLOCATED, events[3].getId());
    }

}
