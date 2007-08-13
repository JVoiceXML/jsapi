/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate: $
 * Author:  $LastChangedBy: $
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

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.SpeakerProfile}.
 * 
 * @author Dirk Schnelle
 */
public class SpeakerProfileTest extends TestCase {

    /**
     * Test method for
     * {@link javax.speech.recognition.SpeakerProfile#hashCode()}.
     */
    public void testHashCode() {
        final SpeakerProfile profile1 = new SpeakerProfile();
        final SpeakerProfile profile2 = new SpeakerProfile();

        assertTrue(profile1.hashCode() != profile2.hashCode());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.SpeakerProfile#getIdentifier()}.
     */
    public void testGetIdentifier() {
        final SpeakerProfile profile1 = new SpeakerProfile();
        assertNull(profile1.getIdentifier());

        final String id2 = "id2";
        final String name2 = "name2";
        final String variant2 = "variant2";
        final SpeakerProfile profile2 = new SpeakerProfile(id2, name2, variant2);
        assertEquals(id2, profile2.getIdentifier());
    }

    /**
     * Test method for {@link javax.speech.recognition.SpeakerProfile#getName()}.
     */
    public void testGetName() {
        final SpeakerProfile profile1 = new SpeakerProfile();
        assertNull(profile1.getName());

        final String id2 = "id2";
        final String name2 = "name2";
        final String variant2 = "variant2";
        final SpeakerProfile profile2 = new SpeakerProfile(id2, name2, variant2);
        assertEquals(name2, profile2.getName());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.SpeakerProfile#getVariant()}.
     */
    public void testGetVariant() {
        final SpeakerProfile profile1 = new SpeakerProfile();
        assertNull(profile1.getVariant());

        final String id2 = "id2";
        final String name2 = "name2";
        final String variant2 = "variant2";
        final SpeakerProfile profile2 = new SpeakerProfile(id2, name2, variant2);
        assertEquals(variant2, profile2.getVariant());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.SpeakerProfile#equals(Object)}.
     */
    public void testEqualsObject() {
        final SpeakerProfile profile1 = new SpeakerProfile();
        assertFalse(profile1.equals("test"));
        assertFalse(profile1.equals((SpeakerProfile) null));

        final SpeakerProfile profile2 = new SpeakerProfile();
        assertTrue(profile1.equals(profile2));

        final String id3 = "id3";
        final String name3 = "name3";
        final String variant3 = "variant3";
        final SpeakerProfile profile3 = new SpeakerProfile(id3, name3, variant3);
        assertFalse(profile1.equals(profile3));
        assertTrue(profile3.equals(profile3));

        final String id4 = "id3";
        final String name4 = "name3";
        final String variant4 = "variant3";
        final SpeakerProfile profile4 = new SpeakerProfile(id4, name4, variant4);
        assertTrue(profile3.equals(profile4));
        assertTrue(profile4.equals(profile3));

        final String id5 = "id5";
        final String name5 = "name5";
        final String variant5 = "variant5";
        final SpeakerProfile profile5 = new SpeakerProfile(id5, name5, variant5);
        assertFalse(profile3.equals(profile5));
        assertFalse(profile5.equals(profile3));
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.SpeakerProfile#toString()}.
     */
    public void testToString() {
        final SpeakerProfile profile1 = new SpeakerProfile();
        final String str1 = profile1.toString();
        assertNotNull(str1);

        final String id2 = "id2";
        final String name2 = "name2";
        final String variant2 = "variant2";
        final SpeakerProfile profile2 = new SpeakerProfile(id2, name2, variant2);
        final String str2 = profile2.toString();
        assertNotNull(str2);
        assertTrue(str2.indexOf(id2) > 0);
        assertTrue(str2.indexOf(name2) > 0);
        assertTrue(str2.indexOf(variant2) > 0);
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.SpeakerProfile#match(SpeakerProfile)}.
     */
    public void testMatch() {
        final SpeakerProfile profile1 = new SpeakerProfile();
        assertTrue(profile1.match(null));

        final SpeakerProfile profile2 = new SpeakerProfile();
        assertTrue(profile1.match(profile2));

        final String id3 = "id3";
        final String name3 = "name3";
        final String variant3 = "variant3";
        final SpeakerProfile profile3 = new SpeakerProfile(id3, name3, variant3);
        assertFalse(profile1.match(profile3));
        assertTrue(profile3.match(profile3));
        assertTrue(profile3.match(profile1));

        final String id4 = "id3";
        final String name4 = "name3";
        final String variant4 = "variant3";
        final SpeakerProfile profile4 = new SpeakerProfile(id4, name4, variant4);
        assertTrue(profile3.match(profile4));
        assertTrue(profile4.match(profile3));

        final String id5 = "id5";
        final String name5 = "name5";
        final String variant5 = "variant5";
        final SpeakerProfile profile5 = new SpeakerProfile(id5, name5, variant5);
        assertFalse(profile3.match(profile5));
        assertFalse(profile5.match(profile3));
    }
}
