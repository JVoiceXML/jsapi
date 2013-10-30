/*
 * File:    $HeadURL: https://svn.code.sf.net/p/jsapi/code/trunk/org.jvoicexml.jsapi2/unittests/org/jvoicexml/jsapi2/ThreadSpeechEventExecutorTest.java $
 * Version: $LastChangedRevision: 782 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2013 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2.jse.recognition;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.Rule;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleToken;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvoicexml.jsapi2.mock.recognition.MockRecognizer;

/**
 * Test cases for the {@link GrammarManager}.
 * @author Dirk Schnelle-Walka
 *
 */
public final class BaseGrammarManagerTest {
    /** The related recognizer. */
    private Recognizer recognizer;

    /** The object to test. */
    private GrammarManager manager;

    /**
     * {@inheritDoc}
     */
    @Before
    public void setUp() throws Exception {
        recognizer = new MockRecognizer();
        manager = recognizer.getGrammarManager();
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.recognition.BaseGrammarManager#createRuleGrammar(java.lang.String, java.lang.String)}.
     * @exception Exception test failed
     */
    @Test
    public void testCreateRuleGrammarStringString() throws Exception {
        final String name = "test";
        final RuleGrammar grammar =
            manager.createRuleGrammar(name, name);
        final RuleToken token = new RuleToken("hello world");
        final Rule rule = new Rule("test", token, Rule.PUBLIC);
        grammar.addRule(rule);
        recognizer.processGrammars();
        final Grammar retrievedGrammar = manager.getGrammar(name);
        Assert.assertNotNull(retrievedGrammar);
        Assert.assertEquals(grammar.toString(), retrievedGrammar.toString());
    }

    /**
     * Test method for {@link GrammarManager#loadGrammar(String, String, Reader)}.
     * @throws Exception test failed
     */
    @Test
    public void testCreateRuleGrammarReader() throws Exception {
        final InputStream in =
                BaseGrammarManagerTest.class.getResourceAsStream(
                        "pizza-de.xml");
        final Reader reader = new InputStreamReader(in);
        final Grammar grammar = manager.loadGrammar("test",
                "application/srgs+xml", reader);
        Assert.assertTrue(grammar instanceof RuleGrammar);
    }
}
