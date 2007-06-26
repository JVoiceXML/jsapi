/**
 * 
 */
package javax.speech.recognition;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RuleComponent}.
 * 
 * @author Dirk Schnelle
 */
public class RuleComponentTest extends TestCase {

    /**
     * Test method for {@link javax.speech.recognition.RuleComponent#toString()}.
     */
    public void testToString() {
        final RuleComponent component = new RuleComponent();
        assertNull(component.toString());
    }

}
