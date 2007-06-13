/**
 * 
 */
package javax.speech.test;

import javax.speech.EngineList;
import javax.speech.EngineMode;
import javax.speech.spi.EngineListFactory;
import javax.speech.synthesis.SynthesizerMode;
import javax.speech.test.synthesis.TestSynthesizerFactory;

/**
 * EngineListFactory for test purposes.
 * 
 * @author Dirk Schnelle
 */
public class TestSynthesizerEngineListFactory implements EngineListFactory {
	/**
	 * {@inheritDoc}
	 */
	public EngineList createEngineList(EngineMode require)
			throws SecurityException {
		if (require instanceof SynthesizerMode) {
			final EngineMode[] modes = new EngineMode[] { new TestSynthesizerFactory() };
			
			return new EngineList(modes);
		}
		
		return null;
	}

}
