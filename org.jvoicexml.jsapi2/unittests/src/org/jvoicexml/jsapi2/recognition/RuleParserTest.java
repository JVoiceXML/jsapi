package org.jvoicexml.jsapi2.recognition;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.RuleParse;

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

}
