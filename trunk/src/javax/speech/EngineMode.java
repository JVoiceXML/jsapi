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
import java.util.Iterator;

public class EngineMode {
    public static final Integer FULL = new Integer(Integer.MAX_VALUE);

    public static final Integer NONE = new Integer(0);

    private String engineName;

    private String modeName;

    private Boolean running;

    private Boolean supportsLetterToSound;

    private Boolean markupSupport;

    public EngineMode() {

    }

    public EngineMode(String engineName, String modeName, Boolean running,
            Boolean supportsLetterToSound, Boolean markupSupport) {
        this.engineName = engineName;
        this.modeName = modeName;
        this.running = running;
        this.supportsLetterToSound = supportsLetterToSound;
        this.markupSupport = markupSupport;
    }

    public String getEngineName() {
        return engineName;
    }

    public Boolean getMarkupSupport() {
        return markupSupport;
    }

    public String getModeName() {
        return modeName;
    }

    public Boolean getRunning() {
        return running;
    }

    public Boolean getSupportsLetterToSound() {
        return supportsLetterToSound;
    }

    public boolean match(EngineMode require) {
    	if (require == null) {
    		return true;
    	}
    	
        final String otherEngineName = require.getEngineName();
        final boolean namesMatch;
        if (otherEngineName == null) {
            namesMatch = true;
        } else {
            namesMatch = otherEngineName.equals(engineName);
        }

        final String otherModeName = require.getModeName();
        final boolean modesMatch;
        if (otherModeName == null) {
            modesMatch = true;
        } else {
            modesMatch = otherModeName.equals(modeName);
        }

        final Boolean otherModeRunning = require.getRunning();
        final boolean runningsMatch;
        if (otherModeRunning == null) {
            runningsMatch = true;
        } else {
            runningsMatch = otherModeRunning.equals(running);
        }

        final Boolean otherSupportsLetterToSound = require
                .getSupportsLetterToSound();
        final boolean supportsLetterToSoundMatch;
        if (otherSupportsLetterToSound == null) {
            supportsLetterToSoundMatch = true;
        } else {
            supportsLetterToSoundMatch = otherSupportsLetterToSound
                    .equals(supportsLetterToSound);
        }

        final Boolean otherMarkupSupport = require.getMarkupSupport();
        final boolean markupSupportMatch;
        if (otherMarkupSupport == null) {
            markupSupportMatch = true;
        } else {
            markupSupportMatch = otherMarkupSupport.equals(markupSupport);
        }

        return namesMatch && modesMatch && runningsMatch
                && supportsLetterToSoundMatch && markupSupportMatch;
    }

    public boolean equals(Object object) {
        if (!(object instanceof EngineMode)) {
            return false;
        }

        final EngineMode mode = (EngineMode) object;

        final String otherEngineName = mode.getEngineName();
        final boolean namesMatch;
        if (engineName == null) {
            namesMatch = (otherEngineName == null);
        } else {
            namesMatch = engineName.equals(otherEngineName);
        }

        final String otherModeName = mode.getModeName();
        final boolean modesMatch;
        if (modeName == null) {
            modesMatch = (otherModeName == null);
        } else {
            modesMatch = modeName.equals(otherModeName);
        }

        final Boolean otherModeRunning = mode.getRunning();
        final boolean runningsMatch;
        if (running == null) {
            runningsMatch = (otherModeRunning == null);
        } else {
            runningsMatch = running.equals(otherModeRunning);
        }

        final Boolean otherSupportsLetterToSound = mode
                .getSupportsLetterToSound();
        final boolean supportsLetterToSoundMatch;
        if (supportsLetterToSound == null) {
            supportsLetterToSoundMatch = (otherSupportsLetterToSound == null);
        } else {
            supportsLetterToSoundMatch = supportsLetterToSound
                    .equals(otherSupportsLetterToSound);
        }

        final Boolean otherMarkupSupport = mode.getMarkupSupport();
        final boolean markupSupportMatch;
        if (markupSupport == null) {
            markupSupportMatch = (otherMarkupSupport == null);
        } else {
            markupSupportMatch = markupSupport.equals(otherMarkupSupport);
        }

        return namesMatch && modesMatch && runningsMatch
                && supportsLetterToSoundMatch && markupSupportMatch;
    }

    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Creates a collection of all parameters.
     * 
     * @return collection of all parameters.
     */
    protected Collection getParameters() {
        final Collection parameters = new java.util.ArrayList();

        parameters.add(engineName);
        parameters.add(modeName);
        parameters.add(running);
        parameters.add(supportsLetterToSound);
        parameters.add(markupSupport);

        return parameters;
    }

    private void appendCollection(StringBuffer str, Collection col) {
        str.append("[");
        final Iterator iterator = col.iterator();

        while (iterator.hasNext()) {
            final Object parameter = iterator.next();
            if (parameter instanceof Collection) {
                final Collection subcol = (Collection) parameter;
                appendCollection(str, subcol);
            } else {
                str.append(parameter);
            }
            if (iterator.hasNext()) {
                str.append(",");
            }
        }
        str.append("]");
    }
    
    public String toString() {
        StringBuffer str = new StringBuffer();

        str.append(getClass().getName());
        final Collection parameters = getParameters();
        appendCollection(str, parameters);

        return str.toString();
    }
}
