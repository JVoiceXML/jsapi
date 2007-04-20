package javax.speech;

import javax.speech.test.TestEngine;

import junit.framework.TestCase;

public class AudioEventTest extends TestCase {
    /** The test engine. */
    private Engine engine;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        engine = new TestEngine();
    }

    /**
     * Test method for {@link javax.speech.AudioEvent#paramString()}.
     */
    public void testParamString() {
        AudioEvent event = new AudioEvent(engine, 43);
        String str = event.paramString();
        assertTrue(str.indexOf("43") >= 0);

        AudioEvent event2 = new AudioEvent(engine, 44, 45);
        String str2 = event2.paramString();
        assertTrue(str2.indexOf("44") >= 0);
    }

    /**
     * Test method for {@link javax.speech.AudioEvent#toString()}.
     */
    public void testToString() {
        AudioEvent event = new AudioEvent(engine, 46);
        String str = event.toString();
        
        assertTrue(str.indexOf("46") >= 0);

        AudioEvent event2 = new AudioEvent(engine, 47, 48);
        String str2 = event2.paramString();
        assertTrue(str2.indexOf("47") >= 0);
    }

    public void testGetAudioLevel() {
        AudioEvent event = new AudioEvent(engine, 46);
        assertEquals(AudioEvent.AUDIO_LEVEL_MIN, event.getAudioLevel());

        AudioEvent eventMin = new AudioEvent(engine, 46, 
                AudioEvent.AUDIO_LEVEL_MIN);
        assertEquals(AudioEvent.AUDIO_LEVEL_MIN, eventMin.getAudioLevel());

        AudioEvent eventQuiet = new AudioEvent(engine, 46, 
                AudioEvent.AUDIO_LEVEL_QUIET);
        assertEquals(AudioEvent.AUDIO_LEVEL_QUIET, eventQuiet.getAudioLevel());

        AudioEvent eventLoud = new AudioEvent(engine, 46, 
                AudioEvent.AUDIO_LEVEL_LOUD);
        assertEquals(AudioEvent.AUDIO_LEVEL_LOUD, eventLoud.getAudioLevel());

        AudioEvent eventMax = new AudioEvent(engine, 46, 
                AudioEvent.AUDIO_LEVEL_MAX);
        assertEquals(AudioEvent.AUDIO_LEVEL_MAX, eventMax.getAudioLevel());
    }
}
