/**
 * 
 */
package javax.speech;

import javax.speech.mock.MockRecognizerEngineListFactory;
import javax.speech.mock.MockSpeechEventExecutor;
import javax.speech.recognition.RecognizerMode;
import javax.speech.synthesis.SynthesizerMode;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RuleAlternatives}.
 * 
 * @author Dirk Schnelle
 */
public class EngineManagerTest extends TestCase {
    /**
     * Test method for
     * {@link javax.speech.EngineManager#availableEngines(javax.speech.EngineMode)}.
     */
    public void testAvailableEngines() {
        final EngineMode require1 = null;
        final EngineList engines1 = EngineManager.availableEngines(require1);
        assertEquals(2, engines1.size());

        final EngineMode require2 = new SynthesizerMode();
        final EngineList engines2 = EngineManager.availableEngines(require2);
        assertEquals(1, engines2.size());

        final EngineMode require3 = new RecognizerMode();
        final EngineList engines3 = EngineManager.availableEngines(require3);
        assertEquals(1, engines3.size());
    }

    /**
     * Test method for
     * {@link javax.speech.EngineManager#createEngine(javax.speech.EngineMode)}.
     */
    public void testCreateEngine() throws Exception {
        final EngineMode require1 = new SynthesizerMode();
        final Engine engine1 = EngineManager.createEngine(require1);
        assertNotNull(engine1);

        final EngineMode require2 = new RecognizerMode();
        final Engine engine2 = EngineManager.createEngine(require2);
        assertNull(engine2);

        final EngineMode require3 = null;
        Exception failure = null;
        try {
            EngineManager.createEngine(require3);
        } catch (IllegalArgumentException e) {
            failure = e;
        }
        assertNotNull(failure);
    }

    /**
     * Test method for
     * {@link javax.speech.EngineManager#getSpeechEventExecutor()}.
     */
    public void testGetSpeechEventExecutor() {
        assertNull(EngineManager.getSpeechEventExecutor());
    }

    /**
     * Test method for
     * {@link javax.speech.EngineManager#setSpeechEventExecutor(javax.speech.SpeechEventExecutor)}.
     */
    public void testSetSpeechEventExecutor() {
        assertNull(EngineManager.getSpeechEventExecutor());
        final SpeechEventExecutor executor = new MockSpeechEventExecutor();
        EngineManager.setSpeechEventExecutor(executor);
        assertEquals(executor, EngineManager.getSpeechEventExecutor());
    }

    /**
     * Test method for {@link javax.speech.EngineManager#getVersion()}.
     */
    public void testGetVersion() {
        assertEquals("2.0.6.0", EngineManager.getVersion());
    }

    /**
     * Test method for
     * {@link javax.speech.EngineManager#registerEngineListFactory(java.lang.String)}.
     */
    public void testRegisterEngineListFactory() throws Exception {
        final EngineList engines1 = EngineManager.availableEngines(null);
        assertEquals(1, engines1.size());

        EngineManager
                .registerEngineListFactory(MockRecognizerEngineListFactory.class
                        .getName());

        final EngineList engines2 = EngineManager.availableEngines(null);
        assertEquals(2, engines2.size());

        EngineManager
                .registerEngineListFactory(MockRecognizerEngineListFactory.class
                        .getName());

        final EngineList engines3 = EngineManager.availableEngines(null);
        assertEquals(2, engines3.size());
    }
}
