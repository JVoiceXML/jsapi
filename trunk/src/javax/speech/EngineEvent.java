/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 249 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
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
    // Flags.
    private static int ALLOCATED_FLAG = 0;

    private static int DEALLOCATED_FLAG = 1;

    private static int ALLOCATING_RESOURCES_FLAG = 1;

    private static int DEALLOCATING_RESOURCES_FLAG = 3;

    private static int PAUSED_FLAG = 4;

    private static int RESUMED_FLAG = 5;

    private static int FOCUSED_FLAG = 6;

    private static int DEFOCUSED_FLAG = 7;

    public static int ERROR_OCCURED_FLAG = 8;
    
    // Events.
    public static int ENGINE_ALLOCATED = 2 ^ ALLOCATED_FLAG;

    public static int ENGINE_DEALLOCATED = 2 ^ DEALLOCATED_FLAG;

    public static int ENGINE_ALLOCATING_RESOURCES = 
        2 ^ ALLOCATING_RESOURCES_FLAG;

    public static int ENGINE_DEALLOCATING_RESOURCES = 
        2 ^ DEALLOCATING_RESOURCES_FLAG;

    public static int ENGINE_DEFOCUSED = 2 ^ DEFOCUSED_FLAG;

    public static int ENGINE_FOCUSED = 2 ^ FOCUSED_FLAG;

    public static int ENGINE_PAUSED = 2 ^ PAUSED_FLAG;

    public static int ENGINE_RESUMED = 2 ^ RESUMED_FLAG;

    public static int ENGINE_ERROR = 2 ^ ERROR_OCCURED_FLAG;

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
