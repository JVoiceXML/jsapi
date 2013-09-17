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

        final RuleTag tag2 = new RuleTag(new Integer(42));
        assertEquals(new Integer(42), tag2.getTag());
    }

    /**
     * Test method for {@link javax.speech.recognition.RuleTag#toString()}.
     */
    public void testToString() {
        final RuleTag tag = new RuleTag("CL");
        assertEquals("<tag>CL</tag>", tag.toString());

        Exception failure = null;
        final RuleTag tag2 = new RuleTag(null);
        try {
            tag2.toString();
        } catch (IllegalArgumentException e) {
            failure = e;
        }
        assertNotNull(failure);
    }

    /**
     * Test method for {@link javax.speech.recognition.RuleTag#getTag()}.
     */
    public void testGetTag() {
        final RuleTag tag = new RuleTag("CL");
        assertEquals("CL", tag.getTag());

        final RuleTag tag2 = new RuleTag(null);
        assertNull(tag2.getTag());
    }
}
