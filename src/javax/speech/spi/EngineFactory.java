package javax.speech.spi;

import javax.speech.Engine;
import javax.speech.EngineException;

public interface EngineFactory {
    Engine createEngine() throws IllegalArgumentException, EngineException,
            SecurityException;
}
