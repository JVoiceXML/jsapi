/**
 * 
 */
package javax.speech.test.recognition;

import javax.speech.Engine;
import javax.speech.EngineException;
import javax.speech.recognition.RecognizerMode;
import javax.speech.spi.EngineFactory;

/**
 * RecognizerFactory for test purposes.
 * 
 * @author Dirk Schnelle
 */
public class TestRecognizerFactory extends RecognizerMode implements
		EngineFactory {
	/**
	 * {@inheritDoc}
	 */
	public Engine createEngine() throws IllegalArgumentException,
			EngineException, SecurityException {
		return new TestRecognizer();
	}

}
