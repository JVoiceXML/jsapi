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

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class EngineList {
    /**
     * The features in this list.
     * 
     * TODO The specification is close to a Vector, e.g. Enumeration should not
     * be used. Better solutions would be a {@link java.util.List}.
     */
    private Vector features;

    public EngineList(EngineMode[] features) {
        this.features = new Vector(features.length);
        for (int i = 0; i < features.length; i++) {
            this.features.add(features[i]);
        }
    }

    public boolean anyMatch(EngineMode require) {
        final Iterator iterator = features.iterator();

        while (iterator.hasNext()) {
            final EngineMode mode = (EngineMode) iterator.next();
            if (mode.match(require)) {
                return true;
            }
        }

        return false;
    }

    public EngineMode elementAt(int index) {
        return (EngineMode) features.get(index);
    }

    public Enumeration elements() {
        return features.elements();
    }

    public void orderByMatch(EngineMode require) {
        final Comparator comparator = new EngineListComparator(require);
        Collections.sort(features, comparator);
    }

    public void rejectMatch(EngineMode reject) {
        final Iterator iterator = features.iterator();
        while (iterator.hasNext()) {
            final EngineMode mode = (EngineMode) iterator.next();
            if (mode.match(reject)) {
                iterator.remove();
            }
        }
    }

    public void removeElementAt(int index) {
        features.remove(index);
    }

    void requireMatch(EngineMode require) {
        final Iterator iterator = features.iterator();
        while (iterator.hasNext()) {
            final EngineMode mode = (EngineMode) iterator.next();
            if (!mode.match(require)) {
                iterator.remove();
            }
        }
    }

    int size() {
        return features.size();
    }

    /**
     * 
     * @author Dirk Schnelle Note: this comparator imposes orderings that are
     *         inconsistent with equals.
     */
    private class EngineListComparator implements Comparator {
        final EngineMode require;

        public EngineListComparator(EngineMode require) {
            this.require = require;
        }

        public int compare(Object object1, Object object2) {
            final EngineMode mode1 = (EngineMode) object1;
            final EngineMode mode2 = (EngineMode) object2;

            final boolean object1Matches = mode1.match(require);
            final boolean object2Matches = mode2.match(require);

            if (object1Matches == object2Matches) {
                return 0;
            }

            if (object1Matches) {
                return -1;
            }

            return 1;
        }

    }

    public String toString() {
        final StringBuffer str = new StringBuffer();

        str.append(getClass());
        str.append("[");

        final Iterator iterator = features.iterator();
        while (iterator.hasNext()) {
            final EngineMode mode = (EngineMode) iterator.next();
            str.append(mode);
            if (iterator.hasNext()) {
                str.append(",");
            }
        }

        str.append("]");

        return str.toString();
    }
}
