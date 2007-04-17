package javax.speech;

public class EngineManager {
    public static EngineList availableEngines(EngineMode require) {
        return null;
    }

    public static Engine createEngine(EngineMode require) {
        return null;
    }

    public static SpeechEventExecutor getSpeechEventExecutor() {
        return null;
    }

    public static String getVersion() {
        return "2.0.0.0";
    }

    public static void registerEngineListFactory(String className) {

    }

    public static void setSpeechEventExecutor(
            SpeechEventExecutor speechEventDispatcher) {

    }
}
