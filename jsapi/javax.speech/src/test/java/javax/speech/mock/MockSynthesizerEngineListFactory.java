/**
 * 
 */
package javax.speech.mock;

import javax.speech.EngineList;
import javax.speech.EngineMode;
import javax.speech.mock.synthesis.MockSynthesizerFactory;
import javax.speech.spi.EngineListFactory;
import javax.speech.synthesis.SynthesizerMode;

/**
 * EngineListFactory for test purposes.
 * 
 * @author Dirk Schnelle
 */
public class MockSynthesizerEngineListFactory implements EngineListFactory {
    /**
     * {@inheritDoc}
     */
    public EngineList createEngineList(EngineMode require)
            throws SecurityException {
        if ((require == null) || (require instanceof SynthesizerMode)) {
            final EngineMode[] modes = new EngineMode[] { new MockSynthesizerFactory() };

            return new EngineList(modes);
        }

        return null;
    }

}
