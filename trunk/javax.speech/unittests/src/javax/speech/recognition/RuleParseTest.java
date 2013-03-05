/**
 * 
 */
package javax.speech.recognition;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RuleParse}.
 * 
 * @author Dirk Schnelle
 */
public class RuleParseTest extends TestCase {
    private RuleReference reference;

    private RuleComponent parse;

    private RuleParse ruleParse;

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();

        final RuleComponent component1 = new RuleParse(new RuleReference(
                "action"), new RuleAlternatives(
                new RuleComponent[] { new RuleSequence(new RuleComponent[] {
                        new RuleToken("close"), new RuleTag("CL") }) }));
        final RuleComponent component2 = new RuleParse(new RuleReference(
                "object"), new RuleSequence(new RuleComponent[] {
                new RuleCount(
                        new RuleSequence(new RuleComponent[] { new RuleParse(
                                new RuleReference("determiner"),
                                new RuleAlternatives(
                                        new RuleComponent[] { new RuleToken(
                                                "the") })) }), 1, 1),
                new RuleAlternatives(
                        new RuleComponent[] { new RuleToken("door") }) }));
        final RuleComponent component3 = new RuleCount(
                new RuleSequence(
                        new RuleComponent[] { new RuleParse(new RuleReference(
                                "polite"),
                                new RuleAlternatives(
                                        new RuleComponent[] { new RuleToken(
                                                "please") })) }), 1, 1);

        reference = new RuleReference("command");
        parse = new RuleSequence(new RuleComponent[] { component1, component2,
                component3 });
        ruleParse = new RuleParse(reference, parse);
    }

    /**
     * Test method for {@link javax.speech.recognition.RuleParse#toString()}.
     */
    public void testToString() {
        final String str = ruleParse.toString();
        assertEquals("<ruleref uri=\"#command\"><ruleref uri=\"#action\">"
                + "<one-of><item>close<tag>CL</tag></item></one-of></ruleref>"
                + "<ruleref uri=\"#object\"><item repeat=\"1\">"
                + "<ruleref uri=\"#determiner\"><one-of><item>the</item>"
                + "</one-of></ruleref></item><one-of><item>door</item>"
                + "</one-of></ruleref><item repeat=\"1\">"
                + "<ruleref uri=\"#polite\"><one-of><item>please</item>"
                + "</one-of></ruleref></item></ruleref>", str);
    }

    /**
     * Test method for {@link javax.speech.recognition.RuleParse#getTags()}.
     */
    public void testGetTags() {
        final Object[] tags = ruleParse.getTags();
        final Object[] expected = new String[] { "CL" };
        assertEquals(expected.length, tags.length);
        for (int i = 0; i < tags.length; i++) {
            final Object tag = tags[i];
            final Object exp = expected[i];
            assertEquals(exp, tag);
        }
    }

    /**
     * Test method for {@link javax.speech.recognition.RuleParse#getParse()}.
     */
    public void testGetParse() {
        assertEquals(parse, ruleParse.getParse());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RuleParse#getRuleReference()}.
     */
    public void testGetRuleReference() {
        assertEquals(reference, ruleParse.getRuleReference());
    }

}
