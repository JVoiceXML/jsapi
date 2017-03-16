/*
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 200-20177 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2;

import javax.speech.SpeechEventExecutor;

/**
 * A {@link SpeechEventExecutor} that runs synchronously.
 * @author Dirk Schnelle-Walka
 *
 */
public final class SynchronousSpeechEventExecutor
    implements SpeechEventExecutor {
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Runnable command) throws IllegalStateException,
            NullPointerException {
        command.run();
    }

}
