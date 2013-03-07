/**
 * 
 */
package javax.speech.recognition;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RuleAlternatives}.
 * 
 * @author Dirk Schnelle
 */
public class RuleAlternativesTest extends TestCase {

    /**
     * Test method for
     * {@link javax.speech.recognition.RuleAlternatives#toString()}.
     */
    public void testToString() {
        final RuleComponent[] components1 =
                new RuleComponent[] {};
        final RuleAlternatives alternatives1 = new RuleAlternatives(components1);
        final String str1 = alternatives1.toString();
        assertEquals(RuleSpecial.VOID.toString(), str1);

        final RuleComponent[] components2 = new RuleComponent[0];
        final RuleAlternatives alternatives2 = new RuleAlternatives(components2);
        final String str2 = alternatives2.toString();
        assertEquals(RuleSpecial.VOID.toString(), str2);

        final RuleComponent[] components3 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag") };
        final RuleAlternatives alternatives3 = new RuleAlternatives(components3);
        final String str3 = alternatives3.toString();
        assertEquals("<one-of><item>token</item><item><tag>tag</tag></item>"
                + "</one-of>", str3);

        final String[] tokens4 = null;
        final RuleAlternatives alternatives4 = new RuleAlternatives(tokens4);
        final String str4 = alternatives4.toString();
        assertEquals(RuleSpecial.VOID.toString(), str4);

        final String[] tokens5 = new String[0];
        final RuleAlternatives alternatives5 = new RuleAlternatives(tokens5);
        final String str5 = alternatives5.toString();
        assertEquals(RuleSpecial.VOID.toString(), str5);

        final String[] tokens6 = new String[] { "tokenOne", "tokenTwo",
                "tokenThree" };
        final RuleAlternatives alternatives6 = new RuleAlternatives(tokens6);
        final String str6 = alternatives6.toString();
        assertEquals("<one-of><item>tokenOne</item><item>tokenTwo</item>"
                + "<item>tokenThree</item></one-of>", str6);

        final RuleComponent[] components7 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag"),
                new RuleToken("otherToken") };
        int weights7[] = new int[] { RuleAlternatives.MIN_WEIGHT,
                RuleAlternatives.NORM_WEIGHT, RuleAlternatives.MAX_WEIGHT };
        final RuleAlternatives alternatives7 = new RuleAlternatives(
                components7, weights7);
        final String str7 = alternatives7.toString();
        assertEquals("<one-of><item weight=\"0.0\">token</item>"
                + "<item weight=\"1.0\"><tag>tag</tag></item>"
                + "<item weight=\""
                + (float) RuleAlternatives.MAX_WEIGHT 
                    / RuleAlternatives.NORM_WEIGHT
                + "\">otherToken</item></one-of>", str7);

        final RuleComponent[] components8 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag"),
                new RuleToken("otherToken") };
        int weights8[] = null;
        final RuleAlternatives alternatives8 = new RuleAlternatives(
                components8, weights8);
        final String str8 = alternatives8.toString();
        assertEquals("<one-of><item weight=\"1.0\">token</item>"
                + "<item weight=\"1.0\"><tag>tag</tag></item>"
                + "<item weight=\"1.0\">otherToken</item></one-of>", str8);
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RuleAlternatives#RuleAlternatives(javax.speech.recognition.RuleComponent[], int[])}.
     */
    public void testRuleAlternativesRuleComponentArrayIntArray() {
        final RuleComponent[] components1 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag"),
                new RuleToken("otherToken") };
        final int weights1[] = new int[] { RuleAlternatives.MIN_WEIGHT,
                RuleAlternatives.NORM_WEIGHT, RuleAlternatives.MAX_WEIGHT };
        final RuleAlternatives alternatives1 = new RuleAlternatives(
                components1, weights1);
        assertNotNull(alternatives1);

        Exception failure = null;

        final RuleComponent[] components2 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag"),
                new RuleToken("otherToken") };
        final int weights2[] = new int[] {};
        try {
            final RuleAlternatives alternatives2 = new RuleAlternatives(
                    components2, weights2);
        } catch (IllegalArgumentException e) {
            failure = e;
        }
        assertNotNull(failure);

        final RuleComponent[] components3 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag"),
                new RuleToken("otherToken") };
        final int weights3[] = null;
        final RuleAlternatives alternatives3 = new RuleAlternatives(
                components3, weights3);
        assertNotNull(alternatives3);

        final RuleComponent[] components4 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag"),
                new RuleToken("otherToken") };
        final int weights4[] = new int[] { RuleAlternatives.NORM_WEIGHT };

        try {
            final RuleAlternatives alternatives4 = new RuleAlternatives(
                    components4, weights4);
        } catch (IllegalArgumentException e) {
            failure = e;
        }
        assertNotNull(failure);
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RuleAlternatives#getRuleComponents()}.
     */
    public void testGetRuleComponents() {
        final RuleComponent[] components1 = new RuleComponent[] {};
        final RuleAlternatives alternatives1 = new RuleAlternatives(components1);
        assertEquals(components1,
                alternatives1.getRuleComponents());

        final RuleComponent[] components2 = new RuleComponent[0];
        final RuleAlternatives alternatives2 = new RuleAlternatives(components2);
        final RuleComponent[] actcomponents2 = alternatives2
                .getRuleComponents();
        assertEquals(0, actcomponents2.length);

        final RuleComponent[] components3 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag") };
        final RuleAlternatives alternatives3 = new RuleAlternatives(components3);
        assertEquals(components3, alternatives3.getRuleComponents());

        final String[] tokens4 = null;
        final RuleAlternatives alternatives4 = new RuleAlternatives(tokens4);
        assertNull(alternatives4.getRuleComponents());

        final String[] tokens5 = new String[0];
        final RuleAlternatives alternatives5 = new RuleAlternatives(tokens5);
        final RuleComponent[] components5 = alternatives5.getRuleComponents();
        assertEquals(0, components5.length);

        final String[] tokens6 = new String[] { "tokenOne", "tokenTwo",
                "tokenThree" };
        final RuleAlternatives alternatives6 = new RuleAlternatives(tokens6);
        final RuleComponent[] components6 = alternatives6.getRuleComponents();
        assertEquals(tokens6.length, components6.length);
        for (int i = 0; i < tokens6.length; i++) {
            final String token = tokens6[i];
            final RuleComponent component = components6[i];
            assertEquals(new RuleToken(token), component);
        }

        final RuleComponent[] components7 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag"),
                new RuleToken("otherToken") };
        int weights7[] = new int[] { RuleAlternatives.MIN_WEIGHT,
                RuleAlternatives.NORM_WEIGHT, RuleAlternatives.MAX_WEIGHT };
        final RuleAlternatives alternatives7 = new RuleAlternatives(
                components7, weights7);
        assertEquals(components7, alternatives7.getRuleComponents());

        final RuleComponent[] components8 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag"),
                new RuleToken("otherToken") };
        int weights8[] = null;
        final RuleAlternatives alternatives8 = new RuleAlternatives(
                components8, weights8);
        assertEquals(components8, alternatives8.getRuleComponents());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RuleAlternatives#getWeights()}.
     */
    public void testGetWeights() {
        final RuleComponent[] components1 = new RuleComponent[] {};
        final RuleAlternatives alternatives1 = new RuleAlternatives(components1);
        assertNull(alternatives1.getWeights());

        final RuleComponent[] components2 = new RuleComponent[0];
        final RuleAlternatives alternatives2 = new RuleAlternatives(components2);
        assertNull(alternatives2.getWeights());

        final RuleComponent[] components3 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag") };
        final RuleAlternatives alternatives3 = new RuleAlternatives(components3);
        assertNull(alternatives3.getWeights());

        final String[] tokens4 = null;
        final RuleAlternatives alternatives4 = new RuleAlternatives(tokens4);
        assertNull(alternatives4.getWeights());

        final String[] tokens5 = new String[0];
        final RuleAlternatives alternatives5 = new RuleAlternatives(tokens5);
        assertNull(alternatives5.getWeights());

        final String[] tokens6 = new String[] { "tokenOne", "tokenTwo",
                "tokenThree" };
        final RuleAlternatives alternatives6 = new RuleAlternatives(tokens6);
        assertNull(alternatives6.getWeights());

        final RuleComponent[] components7 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag"),
                new RuleToken("otherToken") };
        final int weights7[] = new int[] { RuleAlternatives.MIN_WEIGHT,
                RuleAlternatives.NORM_WEIGHT, RuleAlternatives.MAX_WEIGHT };
        final RuleAlternatives alternatives7 = new RuleAlternatives(
                components7, weights7);
        assertEquals(weights7, alternatives7.getWeights());

        final RuleComponent[] components8 = new RuleComponent[] {
                new RuleToken("token"), new RuleTag("tag"),
                new RuleToken("otherToken") };
        final int weights8[] = null;
        final RuleAlternatives alternatives8 = new RuleAlternatives(
                components8, weights8);
        final int[] actweights8 = alternatives8.getWeights();
        assertEquals(components8.length, actweights8.length);
        final int weight8 = actweights8[0];
        for (int i = 0; i < actweights8.length; i++) {
            final int weight = actweights8[i];
            assertEquals(weight8, weight);
        }
    }
}
