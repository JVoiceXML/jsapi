/**
 * 
 */
package org.jvoicexml.jsapi2.sapi;

import java.util.Enumeration;

import javax.speech.EngineList;
import javax.speech.synthesis.SynthesizerMode;
import javax.speech.synthesis.Voice;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test cases for {@link SapiEngineListFactory}.
 * @author Dirk Schnelle-Walka
 *
 */
public final class SapiEngineListFactoryTest {

    /**
     * Test method for {@link org.jvoicexml.jsapi2.sapi.SapiEngineListFactory#createEngineList(javax.speech.EngineMode)}.
     */
    @Test
    public void testCreateEngineList() {
        final SapiEngineListFactory factory = new SapiEngineListFactory();
        final EngineList list =
            factory.createEngineList(SynthesizerMode.DEFAULT);
        final Enumeration<?> e = list.elements();
        Assert.assertTrue(e.hasMoreElements());
        final SynthesizerMode mode = (SynthesizerMode) e.nextElement();
        final Voice[] voices = mode.getVoices();
        Assert.assertTrue(voices.length > 0);
        final Voice voice = voices[0];
        Assert.assertEquals("Microsoft Anna", voice.getName());
    }

}
