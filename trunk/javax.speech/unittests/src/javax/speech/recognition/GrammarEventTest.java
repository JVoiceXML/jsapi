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

import javax.speech.mock.recognition.MockGrammar;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.GrammarEvent}.
 * 
 * @author Dirk Schnelle
 */
public class GrammarEventTest extends TestCase {
    /** The grammar. */
    private Grammar grammar;

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();

        grammar = new MockGrammar();
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.GrammarEvent#getGrammarException()}.
     */
    public void testGetGrammarException() {
        final GrammarEvent event = new GrammarEvent(grammar, 43);
        assertNull(event.getGrammarException());

        final GrammarException grammarException = new GrammarException();
        final GrammarEvent event2 = new GrammarEvent(grammar,
                GrammarEvent.GRAMMAR_CHANGES_REJECTED, true, true,
                grammarException);
        assertEquals(grammarException, event2.getGrammarException());

        final GrammarEvent event3 = new GrammarEvent(grammar,
                GrammarEvent.GRAMMAR_CHANGES_COMMITTED, true, true,
                null);
        assertNull(event3.getGrammarException());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.GrammarEvent#isDefinitionChanged()}.
     */
    public void testIsDefinitionChanged() {
        final GrammarEvent event = new GrammarEvent(grammar, 43);
        assertFalse(event.isDefinitionChanged());

        final GrammarException grammarException = new GrammarException();
        final GrammarEvent event2 = new GrammarEvent(grammar, 
                GrammarEvent.GRAMMAR_CHANGES_REJECTED, true, true,
                grammarException);
        assertTrue(event2.isDefinitionChanged());

        final GrammarEvent event3 = new GrammarEvent(grammar, 
                GrammarEvent.GRAMMAR_CHANGES_COMMITTED, true, false,
                null);
        assertFalse(event3.isDefinitionChanged());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.GrammarEvent#isActivableChanged()}.
     */
    public void testIsActivableChanged() {
        final GrammarEvent event = new GrammarEvent(grammar, 43);
        assertFalse(event.isActivableChanged());

        final GrammarException grammarException = new GrammarException();
        final GrammarEvent event2 = new GrammarEvent(grammar, 
                GrammarEvent.GRAMMAR_CHANGES_REJECTED, true, true,
                grammarException);
        assertTrue(event2.isActivableChanged());

        final GrammarEvent event3 = new GrammarEvent(grammar, 
                GrammarEvent.GRAMMAR_ACTIVATED, false, true,
                null);
        assertFalse(event3.isActivableChanged());
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#paramString()}.
     */
    public void testParamString() {
        final GrammarEvent event = new GrammarEvent(grammar, 43);
        final String str = event.paramString();
        assertTrue("id not found in toString", str.indexOf("43") >= 0);

        final GrammarException grammarException = new GrammarException();
        final GrammarEvent event2 = new GrammarEvent(grammar, 
                GrammarEvent.GRAMMAR_CHANGES_REJECTED, true, true,
                grammarException);
        final String str2 = event2.paramString();
        assertTrue("id not found in toString",
                str2.indexOf("GRAMMAR_CHANGES_REJECTED") >= 0);
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#toString()}.
     */
    public void testToString() {
        final GrammarEvent event = new GrammarEvent(grammar, 43);
        final String str = event.toString();
        assertTrue("id not found in toString", str.indexOf("43") >= 0);

        String paramString = event.paramString();
        assertTrue("toString not longer than paramString",
                str.length() > paramString.length());

        final GrammarException grammarException = new GrammarException();
        final GrammarEvent event2 = new GrammarEvent(grammar, 
                GrammarEvent.GRAMMAR_CHANGES_REJECTED, true, true,
                grammarException);
        final String str2 = event2.toString();
        assertTrue("id not found in toString",
                str2.indexOf("GRAMMAR_CHANGES_REJECTED") >= 0);

        String paramString2 = event.paramString();
        assertTrue("toString not longer than paramString",
                str2.length() > paramString2.length());
    }

}
