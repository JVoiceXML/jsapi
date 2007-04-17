package javax.speech;

public class EngineEvent extends SpeechEvent {
    public static int DEFAULT_MASK = 0;

    public static int ENGINE_ALLOCATED = 1;

    public static int ENGINE_ALLOCATING_RESOURCES = 2;

    public static int ENGINE_DEALLOCATED = 3;

    public static int ENGINE_DEALLOCATING_RESOURCES = 4;

    public static int ENGINE_DEFOCUSED = 5;

    public static int ENGINE_ERROR = 6;

    public static int ENGINE_FOCUSED = 7;

    public static int ENGINE_PAUSED = 8;

    public static int ENGINE_RESUMED = 9;

    private long oldEngineState;

    private long newEngineState;

    private Throwable problem;

    public EngineEvent(Engine source, int id, long oldEngineState,
            long newEngineState, Throwable problem) {
        super(source, id);

        this.oldEngineState = oldEngineState;
        this.newEngineState = newEngineState;
        this.problem = problem;
    }

    public long getNewEngineState() {
        return newEngineState;
    }

    public long getOldEngineState() {
        return oldEngineState;
    }

    public Throwable getEngineError() {
        return problem;
    }

    public String paramString() {
        // TODO: implement EngineEvent.paramString
        return super.paramString();
    }
}
