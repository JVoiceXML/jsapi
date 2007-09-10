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

    public BaseSpeechEventExecutor() {
        commands = new Vector();
        thread = new Thread(this, "BaseSpeechEventExecutor");
        thread.start();
    }

    /**
     * Executes the given command.
     *
     * @param command Runnable
     * @throws InterruptedException
     */
    public void execute(Runnable command) throws InterruptedException {
        if (command == null)
            throw new NullPointerException("Command is null");
        commands.add(command);
        synchronized (commands) {
            commands.notify();
        }
    }

    public void run() {
        while (true) {
            while (commands.size() < 1) {
                synchronized (commands) {
                    try {
                        commands.wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }

            //Use this thread to run the command
            Runnable r = (Runnable)commands.remove(0);
            r.run();
        }
    }
}
