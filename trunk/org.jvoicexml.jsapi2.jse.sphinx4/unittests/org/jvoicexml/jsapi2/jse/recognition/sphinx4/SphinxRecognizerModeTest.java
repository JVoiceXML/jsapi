/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jvoicexml.jsapi2.jse.recognition.sphinx4;

import org.junit.Assert;
import org.junit.Test;


/**
 *Test cases for {@link SphinxRecognizerMode}.
 * @author Dirk Schnelle-Walka
 */
public class SphinxRecognizerModeTest {
    @Test
    public void testCreateEngine() throws Exception {
        final SphinxRecognizerMode mode = new SphinxRecognizerMode();
        Assert.assertEquals(Sphinx4Recognizer.class, mode.createEngine().getClass());
    }
}