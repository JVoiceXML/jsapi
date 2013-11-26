/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2;

import java.util.Enumeration;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

/**
 * Test cases for {@link ThreadSpeechEventExecutor}.
 * @author Dirk Schnelle-Walka
 *
 */
public final class ThreadSpeechEventExecutorTest extends TestCase {
    /** The test object. */
    private ThreadSpeechEventExecutor executor;

    /**
     * Setup the test environment.
     * @throws java.lang.Exception
     *         setup failed
     */
    public void setUp() throws Exception {
        executor = new ThreadSpeechEventExecutor();
    }

    /**
     * Cleanup of the test environment.
     * @throws java.lang.Exception
     *         cleanup failed
     */
    public void tearDown() throws Exception {
        executor.terminate();
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.ThreadSpeechEventExecutor.BaseSpeechEventExecutor#execute(java.lang.Runnable)}.
     * @exception Exception
     *            test failed
     */
    public void testExecute() throws Exception {
        final List<Integer> list = new java.util.ArrayList<Integer>();
        final Runnable runnable1 = new Runnable() {
            public void run() {
                list.add(new Integer(1));
            }
        };
        final Runnable runnable2 = new Runnable() {
            public void run() {
                list.add(new Integer(2));
                synchronized (list) {
                    list.notifyAll();
                }
            }
        };
        executor.execute(runnable1);
        executor.execute(runnable2);
        synchronized (list) {
            list.wait();
        }
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(new Integer(1), list.get(0));
        Assert.assertEquals(new Integer(2), list.get(1));
    }

}
