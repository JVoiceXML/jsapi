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

package javax.speech.recognition;

public class SpeakerProfile {
    public static SpeakerProfile DEFAULT = new SpeakerProfile();

    private String id;

    private String name;

    private String variant;

    public SpeakerProfile() {
    }

    public SpeakerProfile(String id, String name, String variant) {
        this.id = id;
        this.name = name;
        this.variant = variant;
    }

    public String getIdentifier() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVariant() {
        return variant;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SpeakerProfile)) {
            return false;
        }

        final SpeakerProfile profile = (SpeakerProfile) obj;
        final String otherId = profile.getIdentifier();
        final boolean idMatch;
        if (id == null) {
            idMatch = (otherId == null);
        } else {
            idMatch = id.equals(otherId);
        }

        final String otherName = profile.getName();
        final boolean nameMatch;
        if (name == null) {
            nameMatch = (otherName == null);
        } else {
            nameMatch = name.equals(otherName);
        }

        final String otherVariant = profile.getVariant();
        final boolean variantMatch;
        if (variant == null) {
            variantMatch = (otherVariant == null);
        } else {
            variantMatch = variant.equals(otherVariant);
        }

        return idMatch && nameMatch && variantMatch;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();

        str.append(getClass().getName());
        str.append("[");
        str.append(id);
        str.append(",");
        str.append(name);
        str.append(",");
        str.append(variant);
        str.append("]");

        return str.toString();
    }

    public boolean match(SpeakerProfile require) {
        if (require == null) {
            return true;
        }

        final String otherId = require.getIdentifier();
        final boolean idMatch;
        if (otherId == null) {
            idMatch = true;
        } else {
            idMatch = otherId.equals(id);
        }

        final String otherName = require.getName();
        final boolean nameMatch;
        if (otherName == null) {
            nameMatch = true;
        } else {
            nameMatch = otherName.equals(name);
        }

        final String otherVariant = require.getVariant();
        final boolean variantMatch;
        if (otherVariant == null) {
            variantMatch = true;
        } else {
            variantMatch = otherVariant.equals(variant);
        }

        return idMatch && nameMatch && variantMatch;
    }
}
