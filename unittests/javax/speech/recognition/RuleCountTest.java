/**
 * 
 */
package javax.speech.recognition;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RuleReference}.
 * 
 * @author Dirk Schnelle
 */
public class RuleCountTest extends TestCase {
	/**
	 * Test method for {@link javax.speech.recognition.RuleCount#toString()}.
	 */
	public void testToString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link javax.speech.recognition.RuleCount#RuleCount(javax.speech.recognition.RuleComponent, int)}.
	 */
	public void testRuleCountRuleComponentInt() {
		final RuleComponent component = new RuleToken("token");
		final RuleCount count1 = new RuleCount(component, 42);
		
		Exception failure = null;
		try {
			final RuleCount count2 = new RuleCount(component, -34);
		} catch (IllegalArgumentException e) {
			failure = e;
		}
		assertNotNull(failure);
	}

	/**
	 * Test method for {@link javax.speech.recognition.RuleCount#RuleCount(javax.speech.recognition.RuleComponent, int, int)}.
	 */
	public void testRuleCountRuleComponentIntInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link javax.speech.recognition.RuleCount#RuleCount(javax.speech.recognition.RuleComponent, int, int, int)}.
	 */
	public void testRuleCountRuleComponentIntIntInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link javax.speech.recognition.RuleCount#getRepeatMax()}.
	 */
	public void testGetRepeatMax() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link javax.speech.recognition.RuleCount#getRepeatMin()}.
	 */
	public void testGetRepeatMin() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link javax.speech.recognition.RuleCount#getRepeatProbability()}.
	 */
	public void testGetRepeatProbability() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link javax.speech.recognition.RuleCount#getRuleComponent()}.
	 */
	public void testGetRuleComponent() {
		fail("Not yet implemented");
	}

}
