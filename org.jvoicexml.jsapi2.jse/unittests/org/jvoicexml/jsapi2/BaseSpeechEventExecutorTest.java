/*
 * File:    $HeadURL: https://jsapi.svn.sourceforge.net/svnroot/jsapi/trunk/org.jvoicexml.jsapi2.jse/unittests/org/jvoicexml/jsapi2/jse/BaseSpeechEventExecutorTest.java $
 * Version: $LastChangedRevision: 266 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BaseSpeechEventExecutor}.
 * @author Dirk Schnelle-Walka
 *
 */
public class BaseSpeechEventExecutorTest {
    /** The test object. */
    private BaseSpeechEventExecutor executor;

    /**
     * Setup the test environment.
     * @throws java.lang.Exception
     *         setup failed
     */
    @Before
    public void setUp() throws Exception {
        executor = new BaseSpeechEventExecutor();
    }

    /**
     * Cleanup of the test environment.
     * @throws java.lang.Exception
     *         cleanup failed
     */
    @After
    public void tearDown() throws Exception {
        executor.terminate();
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.jse.BaseSpeechEventExecutor#execute(java.lang.Runnable)}.
     * @exception Exception
     *            test failed
     */
    @Test
    public void testExecute() throws Exception {
        final Collection<Integer> list = new java.util.ArrayList<Integer>();
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
        final Iterator<Integer> iterator = list.iterator();
        Assert.assertEquals(new Integer(1), iterator.next());
        Assert.assertEquals(new Integer(2), iterator.next());
    }

}
