/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jvoicexml.jsapi2.recognition.sphinx4;

import javax.speech.SpeechLocale;

import org.junit.Assert;
import org.junit.Test;
import org.jvoicexml.jsapi2.recognition.sphinx4.SphinxRecognizerMode;


/**
 *Test cases for {@link SphinxRecognizerMode}.
 * @author Dirk Schnelle-Walka
 */
public class SphinxRecognizerModeTest {
    /**
     * Test metod to create an engine.
     * @throws Exception
     *         test failed
     */        
    @Test
    public void testCreateEngine() throws Exception {
        final SphinxRecognizerMode mode = new SphinxRecognizerMode(SpeechLocale.ENGLISH);
        Assert.assertEquals(Sphinx4Recognizer.class, mode.createEngine().getClass());
    }
}