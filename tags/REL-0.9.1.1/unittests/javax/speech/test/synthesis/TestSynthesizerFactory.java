/**
 * 
 */
package javax.speech.test.synthesis;

import javax.speech.Engine;
import javax.speech.EngineException;
import javax.speech.spi.EngineFactory;
import javax.speech.synthesis.SynthesizerMode;

/**
 * SynthesizerFactory for test purposes.
 * 
 * @author Dirk Schnelle
 */
public class TestSynthesizerFactory extends SynthesizerMode implements
		EngineFactory {
	/**
	 * {@inheritDoc}
	 */
	public Engine createEngine() throws IllegalArgumentException,
			EngineException, SecurityException {
		return new TestSynthesizer();
	}

}
