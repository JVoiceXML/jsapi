/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 63 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2013 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package org.jvoicexml.jsapi2.jse.recognition;

import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Rule;
import javax.speech.recognition.RuleComponent;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleSequence;
import javax.speech.recognition.RuleTag;
import javax.speech.recognition.RuleToken;

import org.junit.Test;
import org.jvoicexml.jsapi2.mock.recognition.MockRecognizer;
import org.jvoicexml.jsapi2.recognition.BaseRecognizer;


/**
 * Test cases for {@link BaseResult}.
 * @author Dirk Schnelle-Walka
 *
 */
public final class BaseResultTest {

    /**
     * Test method for {@link org.jvoicexml.jsapi2.jse.recognition.BaseResult#getTags(int)}.
     * @exception Exception test failed
     */
    @Test
    public void testGetTags() throws Exception {
        final BaseRecognizer recognizer = new MockRecognizer();
        final GrammarManager manager = recognizer.getGrammarManager();
        final RuleGrammar grammar =
            manager.createRuleGrammar("grammar:test", "test");
        final RuleComponent[] components = new RuleComponent[]  {
                new RuleToken("test"),
                new RuleTag("T")
        };
        final RuleSequence sequence = new RuleSequence(components);
        final Rule root = new Rule("test", sequence, Rule.PUBLIC);
        grammar.addRule(root);
        recognizer.processGrammars();
        System.out.println(grammar);
        final BaseResult result = new BaseResult(grammar, "test");
        final Object[] tags = result.getTags(1);
        System.out.println(tags);
    }

}
