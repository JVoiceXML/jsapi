package org.jvoicexml.jsapi2.recognition;

import javax.speech.Engine;
import javax.speech.recognition.Recognizer;
import javax.speech.synthesis.Synthesizer;

import junit.framework.TestCase;

import org.junit.Assert;
import org.jvoicexml.jsapi2.mock.recognition.MockRecognizer;


/**
 * Test cases for state transitions of the
 * {@link javax.speech.synthesis.Synthesizer}.
 * @author Dirk Schnelle-Walka
 */
public class RecognizerStateTransitionTest extends TestCase {
    /** The recognizer to test. */
    private BaseRecognizer recognizer;

    /**
     * Set up the test environment.
     */
    protected void setUp() throws Exception {
        recognizer = new MockRecognizer();
        super.setUp();
    }
    
    /**
     * Checks for the given state.
     * @param expected the expected state
     */
    private void checkState(long expected) {
        Assert.assertTrue("Expected " + recognizer.stateToString(expected)
                + " but was "
                + recognizer.stateToString(recognizer.getEngineState()),
                recognizer.testEngineState(expected));
    }


    /**
     * Test cases for PAUSED/RESUMED state transitions.
     * @throws Exception
     */
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
     */
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
