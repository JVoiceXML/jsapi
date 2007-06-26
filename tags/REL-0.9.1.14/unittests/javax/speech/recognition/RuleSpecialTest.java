/**
 * 
 */
package javax.speech.recognition;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RuleSpecial}.
 * 
 * @author Dirk Schnelle
 */
public class RuleSpecialTest extends TestCase {
    /**
     * Test method for {@link javax.speech.recognition.RuleSpecial#toString()}.
     */
    public void testToString() {
        assertEquals("<ruleref special=\"GARBAGE\"/>", RuleSpecial.GARBAGE
                .toString());

        assertEquals("<ruleref special=\"VOID\"/>", RuleSpecial.VOID.toString());

        assertEquals("<ruleref special=\"NULL\"/>", RuleSpecial.NULL.toString());
    }
}
