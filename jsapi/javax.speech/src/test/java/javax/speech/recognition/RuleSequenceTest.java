/**
 * 
 */
package javax.speech.recognition;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RuleSequence}.
 * 
 * @author Dirk Schnelle-Walka
 */
public class RuleSequenceTest extends TestCase {

    /**
     * Test method for {@link javax.speech.recognition.RuleSequence#toString()}.
     */
    public void testToString() {
        final RuleComponent[] components1 = new RuleComponent[0];
        final RuleSequence sequence1 = new RuleSequence(components1);
        assertEquals("", sequence1.toString());

        final RuleComponent[] components2 = new RuleComponent[0];
        final RuleSequence sequence2 = new RuleSequence(components2);
        assertEquals("", sequence2.toString());

        final RuleComponent[] components3 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag") };
        final RuleSequence sequence3 = new RuleSequence(components3);
        StringBuffer str3 = new StringBuffer();
        for (int i = 0; i < components3.length; i++) {
            RuleComponent component = components3[i];
            str3.append(component.toString());
        }
        assertEquals(str3.toString(), sequence3.toString());

        final String[] tokens4 = new String[0];
        final RuleSequence sequence4 = new RuleSequence(tokens4);
        assertEquals("", sequence4.toString());

        final String[] tokens5 = new String[0];
        final RuleSequence sequence5 = new RuleSequence(tokens5);
        assertEquals("", sequence5.toString());

        final String[] tokens6 = new String[] { "tokenOne", "tokenTwo",
                "tokenThree" };
        final RuleSequence sequence6 = new RuleSequence(tokens6);
        StringBuffer str6 = new StringBuffer();
        for (int i = 0; i < tokens6.length; i++) {
            String token = tokens6[i];
            RuleComponent component = new RuleToken(token);
            str6.append(component.toString());
        }
        assertEquals(str6.toString(), sequence6.toString());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RuleSequence#getRuleComponents()}.
     */
    public void testGetRuleComponents() {
        final RuleComponent[] components1 = new RuleComponent[0];
        final RuleSequence sequence1 = new RuleSequence(components1);
        assertEquals(components1, sequence1.getRuleComponents());

        final RuleComponent[] components2 = new RuleComponent[0];
        final RuleSequence sequence2 = new RuleSequence(components2);
        final RuleComponent[] actcomponents2 = sequence2.getRuleComponents();
        assertEquals(0, actcomponents2.length);

        final RuleComponent[] components3 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag") };
        final RuleSequence sequence3 = new RuleSequence(components3);
        assertEquals(components3, sequence3.getRuleComponents());

        final String[] tokens4 = new String[0];
        final RuleSequence sequence4 = new RuleSequence(tokens4);
        assertEquals(0, sequence4.getRuleComponents().length);

        final String[] tokens5 = new String[0];
        final RuleSequence sequence5 = new RuleSequence(tokens5);
        final RuleComponent[] components5 = sequence5.getRuleComponents();
        assertEquals(0, components5.length);

        final String[] tokens6 = new String[] { "tokenOne", "tokenTwo",
                "tokenThree" };
        final RuleSequence sequence6 = new RuleSequence(tokens6);
        final RuleComponent[] components6 = sequence6.getRuleComponents();
        assertEquals(tokens6.length, components6.length);
        for (int i = 0; i < tokens6.length; i++) {
            final String token = tokens6[i];
            final RuleComponent component = components6[i];
            assertEquals(new RuleToken(token), component);
        }
    }
}
