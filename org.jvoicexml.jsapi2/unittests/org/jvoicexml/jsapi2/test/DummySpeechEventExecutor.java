/**
 * 
 */
package org.jvoicexml.jsapi2.test;

import javax.speech.SpeechEventExecutor;

/**
 * Dummy implementation of a speech event executor that executes synchronously.
 * @author Dirk Schnelle-Walka
 *
 */
public class DummySpeechEventExecutor implements SpeechEventExecutor {

    /**
     * {@inheritDoc}
     */
    public void execute(final Runnable command) throws IllegalStateException,
            NullPointerException {
        if (command == null) {
            throw new NullPointerException("command must not be null!");
        }
        command.run();
    }

}
