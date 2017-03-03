package org.jvoicexml.jsapi2.recognition;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Rule;
import javax.speech.recognition.RuleComponent;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleParse;
import javax.speech.recognition.RuleSequence;
import javax.speech.recognition.RuleTag;
import javax.speech.recognition.RuleToken;

import org.junit.Assert;
import org.junit.Test;
import org.jvoicexml.jsapi2.mock.recognition.MockRecognizer;

public class RuleParserTest {

    @Test
    public void testParseStringGrammarManagerStringString() throws Exception {
        final MockRecognizer recognizer = new MockRecognizer();
        final GrammarManager manager = new BaseGrammarManager(recognizer);
        final InputStream in = RuleParserTest.class
                .getResourceAsStream("pizza-de.xml");
        final Reader reader = new InputStreamReader(in);
        final Grammar grammar = manager.loadGrammar("test",
                "application/srgs+xml", reader);
        final RuleParse parse = RuleParser.parse("eine kleine pizza mit salami",
                manager, "test", "order");
        System.out.println(parse);
    }

    /**
     * Test method for {@link org.jvoicexml.jsapi2.recognition.BaseResult#getTags(int)}.
     * @exception Exception test failed
     */
    @Test
    public void testTags() throws Exception {
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
        final RuleParse parse = RuleParser.parse("test",
                manager, "grammar:test", "test");
        final Object[] tags = parse.getTags();
        Assert.assertNotNull(tags);
        Assert.assertEquals(1, tags.length);
        Assert.assertEquals("T", tags[0]);
    }

}
