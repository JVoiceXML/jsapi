/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package javax.speech.synthesis;

import java.util.Vector;

import javax.speech.EngineEvent;

public class SynthesizerEvent extends EngineEvent {

    public static int QUEUE_EMPTIED = ENGINE_ERROR << 1;

    public static int QUEUE_UPDATED = QUEUE_EMPTIED << 1;

    public static int SYNTHESIZER_BUFFER_READY = QUEUE_UPDATED << 1;

    public static int SYNTHESIZER_BUFFER_UNFILLED = SYNTHESIZER_BUFFER_READY << 1;

    public static int DEFAULT_MASK = EngineEvent.DEFAULT_MASK | QUEUE_EMPTIED
            | SYNTHESIZER_BUFFER_UNFILLED | SYNTHESIZER_BUFFER_READY;

    private boolean topOfQueueChanged;

    public SynthesizerEvent(Synthesizer source, int id, long oldEngineState,
            long newEngineState, Throwable problem, boolean topOfQueueChanged) {
        super(source, id, oldEngineState, newEngineState, problem);
        this.topOfQueueChanged = topOfQueueChanged;
    }

    public boolean isTopOfQueueChanged() {
        final int id = getId();
        if (id == QUEUE_UPDATED) {
            return topOfQueueChanged;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    protected Vector getParameters() {
        final Vector parameters = super.getParameters();

        final Boolean topOfQueueChangedObject = new Boolean(topOfQueueChanged);
        parameters.addElement(topOfQueueChangedObject);

        return parameters;
    }
}
