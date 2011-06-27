package org.jvoicexml.jsapi2.synthesis;

import javax.speech.Engine;
import javax.speech.synthesis.Synthesizer;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jvoicexml.jsapi2.test.synthesis.DummySynthesizer;

/**
 * Test cases for state transitions of the {@link Engine}.
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
        synthesizer = new DummySynthesizer();
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
    public void testSynthesizerPausResume() throws Exception {
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
}
