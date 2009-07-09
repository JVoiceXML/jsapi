package org.jvoicexml.jsapi2;

import javax.speech.EngineProperties;

/**
 * Notification that a property in the {@link EngineProperties} should be
 * changed.
 * @author Dirk Schnelle-Walka
 *
 */
public class EnginePropertyChangeRequestEvent {
    private final EngineProperties source;
    private final String propertyName;
    private final Object oldValue;
    private final Object newValue;

    public EnginePropertyChangeRequestEvent(EngineProperties source,
            String propertyName,
            Object oldValue,
            Object newValue) {
        if (source == null) {
            throw new IllegalArgumentException("Source must not be null!");
        }
        this.source = source;
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        
    }

    public EngineProperties getSource() {
        return source;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}
