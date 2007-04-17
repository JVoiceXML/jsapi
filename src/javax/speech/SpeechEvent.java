package javax.speech;

import java.util.EventObject;

@SuppressWarnings("serial")
public class SpeechEvent extends EventObject {
    private final int id;
    
    private static final int DISABLE_ALL = 1;
    
    private static final int ENABLE_ALL = 2;
    
    public SpeechEvent(Object source, int id) {
        super(source);
        
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public String paramString() {
        // TODO: check creation of the param string.
        return Integer.toString(id);
    }
    
    public String toString() {
        // TODO: check creation of toString.
        return super.toString();
    }
}
