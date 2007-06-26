/**
 * 
 */
package javax.speech.recognition;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.Rule}.
 * 
 * @author Dirk Schnelle
 */
public class RuleTest extends TestCase {

	/**
	 * Test method for
	 * {@link javax.speech.recognition.Rule#Rule(java.lang.String, javax.speech.recognition.RuleComponent)}.
	 */
	public void testRuleStringRuleComponent() {
		final RuleComponent component1 = new RuleToken("test1");
		final Rule rule1 = new Rule("rule1", component1);

		final RuleComponent component2 = null;
		final Rule rule2 = new Rule("rule2", component2);

		final RuleComponent component3 = new RuleToken("test3");
		Exception failure = null;
		try {
			final Rule rule3 = new Rule(null, component3);
		} catch (IllegalArgumentException e) {
			failure = e;
		}
		assertNotNull(failure);

		final RuleComponent component4 = new RuleToken("test4");
		failure = null;
		try {
			final Rule rule4 = new Rule("test:test", component4);
		} catch (IllegalArgumentException e) {
			failure = e;
		}
		assertNotNull(failure);
	}

	/**
	 * Test method for
	 * {@link javax.speech.recognition.Rule#Rule(java.lang.String, javax.speech.recognition.RuleComponent, int)}.
	 */
	public void testRuleStringRuleComponentInt() {
		final RuleComponent component1 = new RuleToken("test1");
		final Rule rule1 = new Rule("rule1", component1, Rule.PUBLIC_SCOPE);

		final RuleComponent component2 = null;
		final Rule rule2 = new Rule("rule2", component2, Rule.PUBLIC_SCOPE);

		final RuleComponent component3 = new RuleToken("test3");
		Exception failure = null;
		try {
			final Rule rule3 = new Rule(null, component3, Rule.PUBLIC_SCOPE);
		} catch (IllegalArgumentException e) {
			failure = e;
		}
		assertNotNull(failure);

		final RuleComponent component4 = new RuleToken("test4");
		failure = null;
		try {
			final Rule rule4 = new Rule("test:test", component4,
					Rule.PUBLIC_SCOPE);
		} catch (IllegalArgumentException e) {
			failure = e;
		}
		assertNotNull(failure);

		final RuleComponent component5 = new RuleToken("test5");
		failure = null;
		try {
			final Rule rule5 = new Rule("test", component5, 42);
		} catch (IllegalArgumentException e) {
			failure = e;
		}
		assertNotNull(failure);
	}

	/**
	 * Test method for {@link javax.speech.recognition.Rule#getRuleComponent()}.
	 */
	public void testGetRuleComponent() {
		final RuleComponent component1 = new RuleToken("test1");
		final Rule rule1 = new Rule("rule1", component1);
		assertEquals(component1, rule1.getRuleComponent());

		final RuleComponent component2 = new RuleToken("test2");
		final Rule rule2 = new Rule("rule2", component2, Rule.PUBLIC_SCOPE);
		assertEquals(component2, rule2.getRuleComponent());
	}

	/**
	 * Test method for {@link javax.speech.recognition.Rule#getRuleName()}.
	 */
	public void testGetRuleName() {
		final RuleComponent component1 = new RuleToken("test1");
		final Rule rule1 = new Rule("rule1", component1);
		assertEquals("rule1", rule1.getRuleName());

		final RuleComponent component2 = new RuleToken("test2");
		final Rule rule2 = new Rule("rule2", component2, Rule.PUBLIC_SCOPE);
		assertEquals("rule2", rule2.getRuleName());
	}

	/**
	 * Test method for {@link javax.speech.recognition.Rule#getScope()}.
	 */
	public void testGetScope() {
		final RuleComponent component1 = new RuleToken("test1");
		final Rule rule1 = new Rule("rule1", component1);
		assertEquals(Rule.PRIVATE_SCOPE, rule1.getScope());

		final RuleComponent component2 = new RuleToken("test2");
		final Rule rule2 = new Rule("rule2", component2, Rule.PUBLIC_SCOPE);
		assertEquals(Rule.PUBLIC_SCOPE, rule2.getScope());
	}

	/**
	 * Test method for {@link javax.speech.recognition.Rule#toString()}.
	 */
	public void testToString() {
		final RuleComponent component1 = new RuleToken("test1");
		final Rule rule1 = new Rule("rule1", component1);
		assertEquals(
				"javax.speech.recognition.Rule[rule1,test1,PRIVATE_SCOPE]",
				rule1.toString());

		final RuleComponent component2 = new RuleToken("test2");
		final Rule rule2 = new Rule("rule2", component2, Rule.PUBLIC_SCOPE);
		assertEquals("javax.speech.recognition.Rule[rule2,test2,PUBLIC_SCOPE]",
				rule2.toString());
	}
}
