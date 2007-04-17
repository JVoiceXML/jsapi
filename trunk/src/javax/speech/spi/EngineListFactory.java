package javax.speech.spi;

import javax.speech.EngineList;
import javax.speech.EngineMode;

public interface EngineListFactory {
    EngineList createEngineList(EngineMode require) throws SecurityException;
}
