/**
 * 
 */
package javax.speech.test;

import javax.speech.EngineList;
import javax.speech.EngineMode;
import javax.speech.recognition.RecognizerMode;
import javax.speech.spi.EngineListFactory;
import javax.speech.test.recognition.TestRecognizerFactory;

/**
 * EngineListFactory for test purposes.
 * 
 * @author Dirk Schnelle
 */
public class TestRecognizerEngineListFactory implements EngineListFactory {
	/**
	 * {@inheritDoc}
	 */
	public EngineList createEngineList(EngineMode require)
			throws SecurityException {
		if (require instanceof RecognizerMode) {
			final EngineMode[] modes = new EngineMode[] { new TestRecognizerFactory() };
			
			return new EngineList(modes);
		}
		
		return null;
	}
}
