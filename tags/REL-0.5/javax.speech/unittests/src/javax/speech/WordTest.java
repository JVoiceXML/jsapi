/**
 * 
 */
package javax.speech;

import junit.framework.TestCase;

/**
 * @author Dirk Schnelle
 * 
 */
public class WordTest extends TestCase {
    public void testConstructor() {
        final String[] pronounciations = new String[] { "one", "two", "three" };
        final String locator = "file:///user/smith/hello.wav";
        final String markup = "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment = new AudioSegment(locator, markup);
        final Word word1 = new Word("text", pronounciations, "spoken form",
                segment, Word.NOUN | Word.CARDINAL);
        assertNotNull(word1);

        Exception failure = null;
        try {
            final Word word2 = new Word(null, pronounciations, "spoken form",
                    segment, Word.NOUN | Word.CARDINAL);
            assertNotNull(word2);
        } catch (IllegalArgumentException e) {
            failure = e;
        }
        assertNotNull(failure);
    }

    /**
     * Test method for {@link javax.speech.Word#getAudioSegment()}.
     */
    public void testGetAudioSegment() {
        final String[] pronounciations = new String[] { "one", "two", "three" };
        final String locator = "file:///user/smith/hello.wav";
        final String markup = "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment = new AudioSegment(locator, markup);
        final Word word = new Word("text", pronounciations, "spoken form",
                segment, Word.NOUN | Word.CARDINAL);
        assertEquals(segment, word.getAudioSegment());
    }

    /**
     * Test method for {@link javax.speech.Word#getCategories()}.
     */
    public void testGetCategories() {
        final String[] pronounciations = new String[] { "one", "two", "three" };
        final String locator = "file:///user/smith/hello.wav";
        final String markup = "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment = new AudioSegment(locator, markup);
        final Word word = new Word("text", pronounciations, "spoken form",
                segment, Word.NOUN | Word.CARDINAL);
        assertEquals(Word.NOUN | Word.CARDINAL, word.getCategories());
    }

    /**
     * Test method for {@link javax.speech.Word#getPronunciations()}.
     */
    public void testGetPronunciations() {
        final String[] pronounciations = new String[] { "one", "two", "three" };
        final String locator = "file:///user/smith/hello.wav";
        final String markup = "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment = new AudioSegment(locator, markup);
        final Word word = new Word("text", pronounciations, "spoken form",
                segment, Word.NOUN | Word.CARDINAL);
        assertEquals(pronounciations, word.getPronunciations());
    }

    /**
     * Test method for {@link javax.speech.Word#getSpokenForm()}.
     */
    public void testGetSpokenForm() {
        final String[] pronounciations = new String[] { "one", "two", "three" };
        final String locator = "file:///user/smith/hello.wav";
        final String markup = "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment = new AudioSegment(locator, markup);
        final Word word = new Word("text", pronounciations, "spoken form",
                segment, Word.NOUN | Word.CARDINAL);
        assertEquals("spoken form", word.getSpokenForm());
    }

    /**
     * Test method for {@link javax.speech.Word#getText()}.
     */
    public void testGetText() {
        final String[] pronounciations = new String[] { "one", "two", "three" };
        final String locator = "file:///user/smith/hello.wav";
        final String markup = "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment = new AudioSegment(locator, markup);
        final Word word = new Word("text", pronounciations, "spoken form",
                segment, Word.NOUN | Word.CARDINAL);
        assertEquals("text", word.getText());
    }
}
