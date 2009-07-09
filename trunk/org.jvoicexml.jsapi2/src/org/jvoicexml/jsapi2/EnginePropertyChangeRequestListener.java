package org.jvoicexml.jsapi2;

import javax.speech.EngineProperties;


/**
 * Listener to change requests in the {@link EngineProperties}.
 * @author Dirk Schnelle-Walka
 *
 */
public interface EnginePropertyChangeRequestListener {
    /**
     * A change request has been made.
     * @param event the notification about the change request
     */
    void propertyChangeRequest(final EnginePropertyChangeRequestEvent event);
}
