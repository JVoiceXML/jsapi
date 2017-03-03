/**
 * 
 */
package org.jvoicexml.jsapi2.sapi.recognition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Result;
import javax.speech.recognition.RuleGrammar;

import org.junit.Assert;
import org.junit.Test;
import org.jvoicexml.jsapi2.recognition.BaseGrammarManager;

/**
 * Test methods for {@link SapiResult}.
 * 
 * @author Dirk Schnelle-Walka
 * 
 */
public class SapiResultTest {

    private String readSml(final String resource) throws IOException {
        final InputStream in = SapiResultTest.class
                .getResourceAsStream(resource);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        do {
            int read = in.read(buffer);
            if (read < 0) {
                return out.toString();
            }
            out.write(buffer, 0, read);
            ;
        } while (true);
    }

    @Test
    public void testSetSmlNoTags() throws Exception {
        final GrammarManager manager = new BaseGrammarManager();
        final RuleGrammar grammar = manager.createRuleGrammar("grammar:test",
                "root");
        final SapiResult result = new SapiResult(grammar);
        result.setResultState(Result.ACCEPTED);
        final String sml = readSml("sml-simple.xml");
        result.setSml(sml);
        final Object[] tags = result.getTags(0);
        Assert.assertEquals(0, tags.length);
    }

    @Test
    public void testSetSmlTag() throws Exception {
        final GrammarManager manager = new BaseGrammarManager();
        final RuleGrammar grammar = manager.createRuleGrammar("grammar:test",
                "root");
        final SapiResult result = new SapiResult(grammar);
        result.setResultState(Result.ACCEPTED);
        final String sml = readSml("sml-tag.xml");
        result.setSml(sml);
        final Object[] tags = result.getTags(0);
        Assert.assertEquals(1, tags.length);
        Assert.assertEquals("Projectmanager", tags[0]);
    }

    @Test
    public void testSetSmlMultipleTags() throws Exception {
        final GrammarManager manager = new BaseGrammarManager();
        final RuleGrammar grammar = manager.createRuleGrammar("grammar:test",
                "root");
        final SapiResult result = new SapiResult(grammar);
        result.setResultState(Result.ACCEPTED);
        final String sml = readSml("sml-multiple-tags.xml");
        result.setSml(sml);
        final Object[] tags = result.getTags(0);
        Assert.assertEquals(2, tags.length);
        Assert.assertEquals("out.greet=\"general\";", tags[0]);
        Assert.assertEquals("out.who=\"Projectmanager\";", tags[1]);
    }
    @Test
    public void testSetSmlCompound() throws Exception {
        final GrammarManager manager = new BaseGrammarManager();
        final RuleGrammar grammar = manager.createRuleGrammar("grammar:test",
                "root");
        final SapiResult result = new SapiResult(grammar);
        result.setResultState(Result.ACCEPTED);
        final String sml = readSml("sml-compound.xml");
        result.setSml(sml);
        final Object[] tags = result.getTags(0);
        for (Object o : tags) {
            System.out.println(o);
        }
        Assert.assertEquals(4, tags.length);
        Assert.assertEquals("out = new Object();", tags[0]);
        Assert.assertEquals("out.order = new Object();", tags[1]);
        Assert.assertEquals("out.order.size=\"small\";", tags[2]);
        Assert.assertEquals("out.order.topping=\"salami\";", tags[3]);
    }
}
