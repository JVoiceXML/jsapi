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

import java.util.EventObject;

public class SpeechEvent extends EventObject {
    private final int id;
    
    private static final int DISABLE_ALL = 1;
    
    private static final int ENABLE_ALL = 2;
    
    public SpeechEvent(Object source, int id) {
        super(source);
        
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public String paramString() {
        StringBuffer str = new StringBuffer();
        
        str.append(Integer.toString(id));
        str.append(",");
        str.append(getSource());
        
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
