/**
 * 
 */
package javax.speech.recognition;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RuleTag}.
 * 
 * @author Dirk Schnelle
 */
public class RuleTagTest extends TestCase {
    /**
     * Test method for
     * {@link javax.speech.recognition.RuleToken#RuleToken(String)}.
     */
    public void testNewRuleToken() {
        final RuleTag tag1 = new RuleTag("test");
        assertEquals("test", tag1.getTag());

        Exception failure = null;
        try {
            final RuleTag tag2 = new RuleTag(null);
        } catch (IllegalArgumentException e) {
            failure = e;
        }
        assertNotNull(failure);

        failure = null;
        try {
            final RuleTag tag3 = new RuleTag("<text>contents<text>");
        } catch (IllegalArgumentException e) {
            failure = e;
        }
        assertNotNull(failure);
    }

    /**
     * Test method for {@link javax.speech.recognition.RuleTag#toString()}.
     */
    public void testToString() {
        final RuleTag tag = new RuleTag("CL");
        assertEquals("<tag>CL</tag>", tag.toString());
    }

    /**
     * Test method for {@link javax.speech.recognition.RuleTag#getTag()}.
     */
    public void testGetTag() {
        final RuleTag tag = new RuleTag("CL");
        assertEquals("CL", tag.getTag());
    }
}
