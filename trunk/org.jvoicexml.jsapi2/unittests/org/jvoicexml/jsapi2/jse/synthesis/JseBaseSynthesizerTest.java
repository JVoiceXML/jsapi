/**
 * 
 */
package org.jvoicexml.jsapi2.jse.synthesis;

import javax.speech.Engine;
import javax.speech.synthesis.Synthesizer;

import org.junit.Assert;
import org.junit.Test;
import org.jvoicexml.jsapi2.mock.synthesis.MockSynthesizer;

/**
 * Test cases for {@link JseBaseSynthesizer}.
 * @author Dirk Schnelle-Walka
 *
 */
public final class JseBaseSynthesizerTest {

    /**
     * Test method for {@link org.jvoicexml.jsapi2.BaseEngine#allocate()}.
     * @throws Exception 
     *         test failed
     */
    @Test
    public void testAllocate() throws Exception {
        final MockSynthesizer synthesizer = new MockSynthesizer();
        final long state1 = synthesizer.getEngineState();
        Assert.assertEquals(Engine.DEALLOCATED, state1);
        synthesizer.allocate();
        synthesizer.waitEngineState(Engine.ALLOCATED);
        final long state2 = synthesizer.getEngineState();
        Assert.assertEquals(Engine.ALLOCATED | Engine.PAUSED,
                Synthesizer.QUEUE_EMPTY | Engine.DEFOCUSED, state2);
    }

}
