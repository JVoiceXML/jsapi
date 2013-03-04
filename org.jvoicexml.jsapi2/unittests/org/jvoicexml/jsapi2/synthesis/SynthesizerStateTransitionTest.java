package org.jvoicexml.jsapi2.synthesis;

import javax.speech.Engine;
import javax.speech.synthesis.Synthesizer;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jvoicexml.jsapi2.mock.synthesis.MockSynthesizer;

/**
 * Test cases for state transitions of the
 * {@link javax.speech.synthesis.Synthesizer}.
 * @author Stefan Radomski
 * @author Dirk Schnelle-Walka
 */
public class SynthesizerStateTransitionTest extends TestCase {
    /** The synthesizer to test. */
    private BaseSynthesizer synthesizer;

    /**
     * Set up the test environment.
     */
    protected void setUp() throws Exception {
        synthesizer = new MockSynthesizer();
        super.setUp();
    }

    /**
     * Checks for the given state.
     * @param expected the expected state
     */
    private void checkState(long expected) {
        Assert.assertTrue("Expected " + synthesizer.stateToString(expected)
                + " but was "
                + synthesizer.stateToString(synthesizer.getEngineState()),
                synthesizer.testEngineState(expected));
    }

    /**
     * Test cases for the PAUSED-RESUMED transitions
     */
    public void testSynthesizerPauseResume() throws Exception {
        synthesizer.allocate();
        synthesizer.waitEngineState(Engine.ALLOCATED);
        checkState(Synthesizer.RESUMED);
        synthesizer.pause();
        synthesizer.waitEngineState(Synthesizer.PAUSED);
        checkState(Synthesizer.PAUSED);
        synthesizer.resume();
        synthesizer.waitEngineState(Synthesizer.RESUMED);
        checkState(Synthesizer.RESUMED);
        synthesizer.deallocate();
        synthesizer.waitEngineState(Synthesizer.DEALLOCATED);
        checkState(Synthesizer.DEALLOCATED);
        synthesizer.allocate();
        synthesizer.waitEngineState(Engine.ALLOCATED);
        checkState(Synthesizer.RESUMED);
        synthesizer.deallocate();
        synthesizer.waitEngineState(Synthesizer.DEALLOCATED);
        checkState(Synthesizer.DEALLOCATED);
    }

    /**
     * Test cases for nested PAUSED-RESUMED transitions
     */    
    public void testSynthesizerNestedPauseResume() throws Exception {
        synthesizer.allocate();
        synthesizer.waitEngineState(Engine.ALLOCATED);
        checkState(Synthesizer.RESUMED);
        synthesizer.pause();
        synthesizer.waitEngineState(Synthesizer.PAUSED);
        checkState(Synthesizer.PAUSED);
        synthesizer.pause();
        synthesizer.waitEngineState(Synthesizer.PAUSED);
        checkState(Synthesizer.PAUSED);
        synthesizer.resume();
        checkState(Synthesizer.PAUSED);
        synthesizer.pause();
        checkState(Synthesizer.PAUSED);
        synthesizer.resume();
        checkState(Synthesizer.PAUSED);
        synthesizer.resume();
        checkState(Synthesizer.RESUMED);
        synthesizer.deallocate();
        synthesizer.waitEngineState(Synthesizer.DEALLOCATED);
        checkState(Synthesizer.DEALLOCATED);
    }
}
