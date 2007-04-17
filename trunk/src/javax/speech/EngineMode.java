package javax.speech;

public class EngineMode {
    public static final Integer FULL = new Integer(1);
    
    public static final Integer NONE = new Integer(2);
    
    private String engineName;
    
    private String modeName;
    
    private Boolean running;
    
    private Boolean supportsLetterToSound;
    
    private Integer markupSupport;
    
    public EngineMode() {
        
    }
    
    public EngineMode(String engineName, String modeName, Boolean running, 
            Boolean supportsLetterToSound, Integer markupSupport) {
        this.engineName = engineName;
        this.modeName = modeName;
        this.running = running;
        this.supportsLetterToSound = supportsLetterToSound;
        this.markupSupport = markupSupport;
    }
    
    public String getEngineName() {
        return engineName;
    }
    
    public Integer getMarkupSupport() {
        return markupSupport;
    }
    
    public String getModeName() {
        return modeName;
    }
    
    public Boolean getRunning() {
        return running;
    }
    
    public Boolean getSupportsLetterToSound() {
        return supportsLetterToSound;
    }
    
    public boolean match(EngineMode require) {
        // TODO: implement EngineMode.match
        return false;
    }
    
    public boolean equals(Object mode) {
        // TODO: implement EngineMode.equals.
        return super.equals(mode);
    }
    
    public int hashCode() {
        // TODO: implement EngineMode.hashCode
        return super.hashCode();
    }
    
    public String toString() {
        // TODO: implement toString.
        return super.toString();
    }
}
