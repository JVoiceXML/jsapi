package javax.speech;

import java.beans.PropertyChangeListener;

public interface EngineProperties {
    int MAX_PRIORITY = 0;

    int MAX_UNTRUSTED_PRIORITY = 1;

    int MIN_PRIORITY = 2;

    int NORM_TRUSTED_PRIORITY = 3;

    int NORM_UNTRUSTED_PRIORITY = 4;

    void addPropertyChangeListener(PropertyChangeListener listener);

    String getBase();

    int getPriority();

    void removePropertyChangeListener(PropertyChangeListener listener);

    void reset();

    void setBase(String uri);

    void setPriority(int priority);
}
