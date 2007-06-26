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
public class RuleReferenceTest extends TestCase {
	/**
	 * Test method for
	 * {@link javax.speech.recognition.RuleReference}
	 */
	public void testNewRuleReference() {
		final RuleReference reference1 = new RuleReference("ruleNameOne");
		assertEquals("ruleNameOne", reference1.getRuleName());

		Exception failure = null;
		try {
			final RuleReference reference2 = new RuleReference(null);
		} catch (IllegalArgumentException e) {
			failure = e;
		}
		assertNotNull(failure);

		failure = null;
		try {
			final RuleReference reference3 = new RuleReference("#ruleNameThree");
		} catch (IllegalArgumentException e) {
			failure = e;
		}
		assertNotNull(failure);

		final RuleReference reference4 = new RuleReference(
				"grammarReferenceFour", "ruleNameFour");
		assertEquals("ruleNameFour", reference4.getRuleName());

		failure = null;
		try {
			final RuleReference reference5 = new RuleReference(null,
					"ruleNameFive");
		} catch (IllegalArgumentException e) {
			failure = e;
		}
		assertNotNull(failure);

		failure = null;
		try {
			final RuleReference reference6 = new RuleReference(
					"file://grammarReference6", null);
		} catch (IllegalArgumentException e) {
			failure = e;
		}
		assertNotNull(failure);
	}

	/**
	 * Test method for {@link javax.speech.recognition.RuleReference#toString()}.
	 */
	public void testToString() {
		final RuleReference reference1 = new RuleReference("ruleNameOne");
		assertEquals("<ruleref uri=\"#ruleNameOne\"/>", reference1.toString());

		final RuleReference reference2 = new RuleReference(
				"grammarReferenceTwo", "ruleNameTwo");
		assertEquals("<ruleref uri=\"grammarReferenceTwo#ruleNameTwo\"/>",
				reference2.toString());

		final RuleReference reference3 = new RuleReference(
				"grammarReferenceThree", "ruleNameThree", "mediaTypeThree");
		assertEquals("<ruleref uri=\"grammarReferenceThree#ruleNameThree\" "
				+ "type=\"mediaTypeThree\"/>", reference3.toString());

		final RuleReference reference4 = new RuleReference(
				"grammarReferenceFour", "ruleNameFour", "mediaTypeFour");
		assertEquals(
				"<ruleref uri=\"grammarReferenceFour#ruleNameFour\" type=\"mediaTypeFour\"/>",
				reference4.toString());
	}

	/**
	 * Test method for
	 * {@link javax.speech.recognition.RuleReference#getGrammarReference()}.
	 */
	public void testGetGrammarReference() {
		final RuleReference reference1 = new RuleReference("ruleNameOne");
		assertNull(reference1.getGrammarReference());

		final RuleReference reference2 = new RuleReference(
				"grammarReferenceTwo", "ruleNameTwo");
		assertEquals("grammarReferenceTwo", reference2.getGrammarReference());

		final RuleReference reference3 = new RuleReference(
				"grammarReferenceThree", "ruleNameThree", "mediaTypeThree");
		assertEquals("grammarReferenceThree", reference3.getGrammarReference());
	}

	/**
	 * Test method for
	 * {@link javax.speech.recognition.RuleReference#getMediaType()}.
	 */
	public void testGetMediaType() {
		final RuleReference reference1 = new RuleReference("ruleNameOne");
		assertEquals("application/srgs+xml", reference1.getMediaType());

		final RuleReference reference2 = new RuleReference(
				"grammarReferenceTwo", "ruleNameTwo");
		assertEquals("application/srgs+xml", reference1.getMediaType());

		final RuleReference reference3 = new RuleReference(
				"grammarReferenceThree", "ruleNameThree", "application/x-jsgf");
		assertEquals("application/x-jsgf", reference3.getMediaType());
	}

	/**
	 * Test method for
	 * {@link javax.speech.recognition.RuleReference#getRuleName()}.
	 */
	public void testGetRuleName() {
		final RuleReference reference1 = new RuleReference("ruleNameOne");
		assertEquals("ruleNameOne", reference1.getRuleName());

		final RuleReference reference2 = new RuleReference(
				"grammarReferenceTwo", "ruleNameTwo");
		assertEquals("ruleNameTwo", reference2.getRuleName());

		final RuleReference reference3 = new RuleReference(
				"grammarReferenceThree", "ruleNameThree", "mediaTypeThree");
		assertEquals("ruleNameThree", reference3.getRuleName());
	}

}
