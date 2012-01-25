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

package javax.speech.synthesis;

import junit.framework.TestCase;

/**
 * Test case for {@link jjavax.speech.synthesis.PhoneInfo}.
 * 
 * @author Dirk Schnelle
 */
public class PhoneInfoTest extends TestCase {

    /**
     * Test method for {@link javax.speech.synthesis.PhoneInfo#getDuration()}.
     */
    public void testGetDuration() {
        final PhoneInfo info = new PhoneInfo("phoneme", 45);
        assertEquals(45, info.getDuration());

        final PhoneInfo info2 = new PhoneInfo("phoneme2",
                PhoneInfo.UNKNOWN_DURATION);
        assertEquals(PhoneInfo.UNKNOWN_DURATION, info2.getDuration());
    }

    /**
     * Test method for {@link javax.speech.synthesis.PhoneInfo#getPhoneme()}.
     */
    public void testGetPhoneme() {
        final PhoneInfo info = new PhoneInfo("phoneme", 45);
        assertEquals("phoneme", info.getPhoneme());

        final PhoneInfo info2 = new PhoneInfo("phoneme2",
                PhoneInfo.UNKNOWN_DURATION);
        assertEquals("phoneme2", info2.getPhoneme());
    }

}
