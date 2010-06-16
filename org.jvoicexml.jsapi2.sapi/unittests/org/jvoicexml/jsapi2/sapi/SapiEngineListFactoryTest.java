/**
 * 
 */
package org.jvoicexml.jsapi2.sapi;

import java.util.Enumeration;

import javax.speech.EngineList;
import javax.speech.synthesis.SynthesizerMode;

import org.junit.Test;

/**
 * @author Dirk Schnelle-Walka
 *
 */
public class SapiEngineListFactoryTest {

    /**
     * Test method for {@link org.jvoicexml.jsapi2.sapi.SapiEngineListFactory#createEngineList(javax.speech.EngineMode)}.
     */
    @Test
    public void testCreateEngineList() {
        final SapiEngineListFactory factory = new SapiEngineListFactory();
        final EngineList list =
            factory.createEngineList(SynthesizerMode.DEFAULT);
        Enumeration e = list.elements();
        while (e.hasMoreElements()) {
            SynthesizerMode mode = (SynthesizerMode) e.nextElement();
            System.out.println(mode);
        }
    }

}
