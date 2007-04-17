package javax.speech;

import java.util.Enumeration;

public class EngineList {
    private EngineMode[] features;

    public EngineList(EngineMode[] features) {
        this.features = features;
    }

    public boolean anyMatch(EngineMode require) {
        return false;
    }

    public EngineMode elementAt(int index) {
        return null;
    }

    public Enumeration elements() {
        return null;
    }

    public void orderByMatch(EngineMode require) {
        
    }

    public void rejectMatch(EngineMode reject) {

    }

    public void removeElementAt(int index) {

    }

    void requireMatch(EngineMode require) {

    }

    int size() {
        return features.length;
    }
}
