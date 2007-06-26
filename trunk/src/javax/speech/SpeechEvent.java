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
import java.util.EventObject;
import java.util.Iterator;

public class SpeechEvent extends EventObject {
    private final int id;

    private static final int DISABLE_ALL = 0;

    private static final int ENABLE_ALL = Integer.MAX_VALUE;

    public SpeechEvent(Object source, int id) {
        super(source);

        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Creates a collection of all parameters.
     * 
     * @return collection of all parameters.
     */
    protected Collection getParameters() {
        final Collection parameters = new java.util.ArrayList();

        final Object source = getSource();
        parameters.add(source);
        final Integer identifier = new Integer(id);
        parameters.add(identifier);

        return parameters;
    }

    public String paramString() {
        final StringBuffer str = new StringBuffer();

        final Collection parameters = getParameters();
        final Iterator iterator = parameters.iterator();

        while (iterator.hasNext()) {
            final Object parameter = iterator.next();
            str.append(parameter);
            if (iterator.hasNext()) {
                str.append(",");
            }
        }

        return str.toString();
    }

    public String toString() {
        StringBuffer str = new StringBuffer();

        str.append(getClass().getName());
        str.append("[");
        str.append(paramString());
        str.append("]");

        return str.toString();
    }
}
