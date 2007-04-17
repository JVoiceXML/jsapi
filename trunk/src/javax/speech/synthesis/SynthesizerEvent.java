package javax.speech.synthesis;

import javax.speech.EngineEvent;

public class SynthesizerEvent extends EngineEvent {
    public static int DEFAULT_MASK = 0;

    public static int QUEUE_EMPTIED = 1;

    public static int QUEUE_UPDATED = 2;

    public static int SYNTHESIZER_BUFFER_READY = 3;

    public static int SYNTHESIZER_BUFFER_UNFILLED = 4;

    private boolean topOfQueueChanged;

    public SynthesizerEvent(Synthesizer source, int id, long oldEngineState,
            long newEngineState, Throwable problem, boolean topOfQueueChanged) {
        super(source, id, oldEngineState, newEngineState, problem);
        this.topOfQueueChanged = topOfQueueChanged;
    }

    public boolean isTopOfQueueChanged() {
        return topOfQueueChanged;
    }
    
    public String paramString() {
        return super.paramString();
    }
}
