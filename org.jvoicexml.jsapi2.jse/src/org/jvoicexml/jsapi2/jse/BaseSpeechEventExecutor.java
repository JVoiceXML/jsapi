/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: lyncher $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */
package org.jvoicexml.jsapi2.jse;

import javax.speech.SpeechEventExecutor;
import java.util.Vector;

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
public class BaseSpeechEventExecutor implements SpeechEventExecutor, Runnable {

    private Thread thread;

    private Vector commands;

    private boolean shouldRun;

    public BaseSpeechEventExecutor() {
        commands = new Vector();
        thread = new Thread(this, "BaseSpeechEventExecutor");
        shouldRun = true;
        thread.start();
    }

    protected void finalize() {
        shouldRun = false;
    }

    /**
     * Executes the given command.
     *
     * @param command Runnable
     * @throws InterruptedException
     */
    public void execute(Runnable command) throws
            IllegalStateException, NullPointerException {
        if (command == null)
            throw new NullPointerException("Command is null");
        commands.addElement(command);
        synchronized (commands) {
            commands.notify();
        }
    }

    public void run() {
        while (shouldRun) {
            while ((commands.size() < 1) && (shouldRun == true)) {
                synchronized (commands) {
                    try {
                        commands.wait(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }

            //Use this thread to run the command
            Runnable command = (Runnable) commands.firstElement();
            commands.removeElementAt(0);
            command.run();
        }
    }
}
