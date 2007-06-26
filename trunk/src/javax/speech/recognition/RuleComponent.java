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

public class RuleComponent {
    // TODO Is this really a full fledged class or should it be defined
    // abstract.

    /**
     * Checks if the given text is a valid grammar text.
     * 
     * @param text
     *            the text to check.
     */
    protected static void checkValidGrammarText(String text) {
        if ((text == null) || (text.length() == 0)) {
            throw new IllegalArgumentException("'" + text + "'"
                    + " is not a valid grammar text");
        }

        final char[] chars = text.toCharArray();
        // The first symbol must be a character.
        final char first = chars[0];
        if (!Character.isLetter(first) && (first != '_')) {
            throw new IllegalArgumentException("'" + text
                    + "' is not a valid grammar text");
        }
        // Following symbols must be a character or a digit.
        for (int i = 1; i < chars.length; i++) {
            final char ch = chars[i];
            if (!Character.isLetter(ch) && !Character.isDigit(ch)
                    && (ch != '_')) {
                throw new IllegalArgumentException("'" + text
                        + "' is not a valid grammar text");
            }
        }
    }

    public String toString() {
        return null;
    }
}
