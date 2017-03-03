/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 296 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2012 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.jsapi2.mock.synthesis;

import java.util.Vector;

import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SpeakableListener;

/**
 * An implementation of a {@link SpeakableListener} for test purposes.
 * @author Dirk Schnelle-Walka
 *
 */
public class MockSpeakableListener implements SpeakableListener {
    /** Received speakable events. */
    private final Vector events;

    /** Snchronization lock. */
    private final Object lock;

    /**
     * Constructs a new object.
     */
    public MockSpeakableListener() {
        events = new Vector();
        lock = new Object();
    }

    /**
     * Waits until the number of events matches the given size.
     * @param size the number of expected events
     * @throws InterruptedException
     *         if waiting was interrupted
     */
    public void waitForSize(final int size) throws InterruptedException {
        while (events.size() != size) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    /**
     * Returns the {@link SpeakableEvent} at the given position.
     * @param pos position of the event to return
     * @return the event at the given position
     */
    public SpeakableEvent getEvent(final int pos) {
        return (SpeakableEvent) events.elementAt(pos);
    }

    /**
     * {@inheritDoc}
     */
    public void speakableUpdate(SpeakableEvent e) {
        events.add(e);
        System.out.println(e);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

}
