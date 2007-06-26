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

package javax.speech;

import java.util.Collection;

public class EngineEvent extends SpeechEvent {
    // Events.
    public static int ENGINE_ALLOCATED = 1;

    public static int ENGINE_DEALLOCATED = ENGINE_ALLOCATED << 1;

    public static int ENGINE_ALLOCATING_RESOURCES = ENGINE_DEALLOCATED << 1;

    public static int ENGINE_DEALLOCATING_RESOURCES = 
        ENGINE_ALLOCATING_RESOURCES << 1;

    public static int ENGINE_DEFOCUSED = ENGINE_DEALLOCATING_RESOURCES << 1;

    public static int ENGINE_FOCUSED = ENGINE_DEFOCUSED << 1;

    public static int ENGINE_PAUSED = ENGINE_FOCUSED << 1;

    public static int ENGINE_RESUMED = ENGINE_PAUSED << 1;

    public static int ENGINE_ERROR = ENGINE_RESUMED << 1;

    public static int DEFAULT_MASK = ENGINE_ALLOCATED | ENGINE_DEALLOCATED 
        | ENGINE_PAUSED | ENGINE_RESUMED | ENGINE_FOCUSED | ENGINE_DEFOCUSED 
        | ENGINE_ERROR;

    
    private long oldEngineState;

    private long newEngineState;

    private Throwable problem;

    public EngineEvent(Engine source, int id, long oldEngineState,
            long newEngineState, Throwable problem) {
        super(source, id);

        this.oldEngineState = oldEngineState;
        this.newEngineState = newEngineState;
        this.problem = problem;
    }

    public long getNewEngineState() {
        return newEngineState;
    }

    public long getOldEngineState() {
        return oldEngineState;
    }

    public Throwable getEngineError() {
        final int id = getId();
        if (id == ENGINE_ERROR) {
            return problem;
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    protected Collection getParameters() {
        final Collection parameters = super.getParameters();

        final Long oldEngineStateObject = new Long(oldEngineState);
        parameters.add(oldEngineStateObject);
        final Long newEngineStateObject = new Long(newEngineState);
        parameters.add(newEngineStateObject);
        parameters.add(problem);

        return parameters;
    }
}
