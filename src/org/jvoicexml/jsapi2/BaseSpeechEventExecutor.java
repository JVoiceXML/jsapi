/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */
package org.jvoicexml.jsapi2;

import javax.speech.SpeechEventExecutor;

/**
 * <p>Title: JSAPI 2.0</p>
 *
 * <p>Description: An independent reference implementation of JSR 113</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class BaseSpeechEventExecutor implements SpeechEventExecutor {

    public BaseSpeechEventExecutor() {
    }

    /**
     * Executes the given command.
     *
     * @param command Runnable
     * @throws InterruptedException
     */
    public void execute(Runnable command) throws InterruptedException, NullPointerException {
        if (command == null) throw new NullPointerException("Command is null");
        new Thread(command).start();
    }
}
