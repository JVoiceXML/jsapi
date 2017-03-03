package org.jvoicexml.jsapi2.recognition;

import javax.speech.Engine;
import javax.speech.recognition.Recognizer;
import javax.speech.synthesis.Synthesizer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvoicexml.jsapi2.mock.recognition.MockRecognizer;


/**
 * Test cases for state transitions of the
 * {@link javax.speech.synthesis.Synthesizer}.
 * @author Dirk Schnelle-Walka
 */
public final class RecognizerStateTransitionTest {
    /** The recognizer to test. */
    private BaseRecognizer recognizer;

    /**
     * Set up the test environment.
     * @exception Exception
     *            set up failed
     */
    @Before
    public void setUp() throws Exception {
        recognizer = new MockRecognizer();
    }
    
    /**
     * Checks for the given state.
     * @param expected the expected state
     */
    private void checkState(final long expected) {
        Assert.assertTrue("Expected " + recognizer.stateToString(expected)
                + " but was "
                + recognizer.stateToString(recognizer.getEngineState()),
                recognizer.testEngineState(expected));
    }


    /**
     * Test cases for PAUSED/RESUMED state transitions.
     * @throws Exception
     *          test failed
     */
    @Test
    public void testRecognizerPausedResumed() throws Exception {
        recognizer.allocate();
        recognizer.waitEngineState(Engine.ALLOCATED);
        checkState(Recognizer.PAUSED);
        recognizer.resume();
        recognizer.waitEngineState(Synthesizer.RESUMED);
        checkState(Synthesizer.RESUMED);
        recognizer.pause();
        recognizer.waitEngineState(Synthesizer.PAUSED);
        checkState(Synthesizer.PAUSED);
        recognizer.resume();
        recognizer.waitEngineState(Synthesizer.RESUMED);
        checkState(Synthesizer.RESUMED);
        recognizer.deallocate();
        recognizer.waitEngineState(Synthesizer.DEALLOCATED);
        checkState(Synthesizer.DEALLOCATED);
        recognizer.allocate();
        recognizer.waitEngineState(Engine.ALLOCATED);
        checkState(Synthesizer.PAUSED);
        recognizer.deallocate();
        recognizer.waitEngineState(Synthesizer.DEALLOCATED);
        checkState(Synthesizer.DEALLOCATED);
    }

    /**
     * Test cases for FOCUSED/DEFOCUSED state transitions.
     * @throws Exception
     *         test failed
     */
    @Test
    public void testRecognizerFocusedDefocused() throws Exception {
        recognizer.allocate();
        recognizer.waitEngineState(Engine.ALLOCATED);
        checkState(Recognizer.ALLOCATED);
        checkState(Recognizer.DEFOCUSED);
        recognizer.requestFocus();
        recognizer.waitEngineState(Engine.FOCUSED);
        checkState(Recognizer.ALLOCATED);
        recognizer.releaseFocus();
        recognizer.waitEngineState(Engine.DEFOCUSED);
        checkState(Recognizer.ALLOCATED);
        recognizer.deallocate();
        recognizer.waitEngineState(Synthesizer.DEALLOCATED);
        checkState(Synthesizer.DEALLOCATED);
        recognizer.allocate();
        recognizer.waitEngineState(Engine.ALLOCATED);
        checkState(Recognizer.ALLOCATED);
        checkState(Recognizer.DEFOCUSED);
        recognizer.requestFocus();
        recognizer.waitEngineState(Engine.FOCUSED);
        checkState(Recognizer.ALLOCATED);
        recognizer.deallocate();
        recognizer.waitEngineState(Synthesizer.DEALLOCATED);
        checkState(Synthesizer.DEALLOCATED);
        
        // TODO add checks if another recognizer requests the focus
    }
}
