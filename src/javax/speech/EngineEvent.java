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

public class EngineEvent extends SpeechEvent {
    public static int DEFAULT_MASK = 0;

    public static int ENGINE_ALLOCATED = 1;

    public static int ENGINE_ALLOCATING_RESOURCES = 2;

    public static int ENGINE_DEALLOCATED = 3;

    public static int ENGINE_DEALLOCATING_RESOURCES = 4;

    public static int ENGINE_DEFOCUSED = 5;

    public static int ENGINE_ERROR = 6;

    public static int ENGINE_FOCUSED = 7;

    public static int ENGINE_PAUSED = 8;

    public static int ENGINE_RESUMED = 9;

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
        return problem;
    }

    public String paramString() {
        // TODO: implement EngineEvent.paramString
        return super.paramString();
    }
}
