/**
 * 
 */
package javax.speech.recognition;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RuleToken}.
 * 
 * @author Dirk Schnelle
 */
public class RuleTokenTest extends TestCase {
    /**
     * Test method for
     * {@link javax.speech.recognition.RuleToken#RuleToken(String)}.
     */
    public void testNewRuleToken() {
        final RuleToken token1 = new RuleToken("test");
        assertEquals("test", token1.getText());

        final RuleToken token2 = new RuleToken("  New    York   ");
        assertEquals("New York", token2.getText());

        Exception failure = null;
        try {
            final RuleToken token3 = new RuleToken(null);
        } catch (IllegalArgumentException e) {
            failure = e;
        }
        assertNotNull(failure);

        failure = null;
        try {
            final RuleToken token4 = new RuleToken("<text>contents<text>");
        } catch (IllegalArgumentException e) {
            failure = e;
        }
        assertNotNull(failure);
    }

    /**
     * Test method for {@link javax.speech.recognition.RuleToken#getText()}.
     */
    public void testGetText() {
        final RuleToken token = new RuleToken("test");
        assertEquals("test", token.getText());
    }

    /**
     * Test method for {@link javax.speech.recognition.RuleToken#toString()}.
     */
    public void testToString() {
        final RuleToken token = new RuleToken("test");
        assertEquals("test", token.getText());
    }

}
