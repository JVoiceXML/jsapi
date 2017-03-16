/*
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 200-20177 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2;

import javax.speech.SpeechEventExecutor;

/**
 * A speech event executor that can be terminated.
 * @author Dirk Schnelle-Walka
 *
 */
public interface TerminatableSpeechEventExecutor extends SpeechEventExecutor {
    /**
     * Terminate this speech event executor.
     */
    void terminate();
}
