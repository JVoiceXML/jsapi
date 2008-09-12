/**
 * 
 */
package org.jvoicexml.jsapi2.jse.test.synthesis;

import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SpeakableListener;

/**
 * @author DS01191
 *
 */
public class DummySpeakableListener implements SpeakableListener {

    /* (non-Javadoc)
     * @see javax.speech.synthesis.SpeakableListener#speakableUpdate(javax.speech.synthesis.SpeakableEvent)
     */
    @Override
    public void speakableUpdate(SpeakableEvent e) {
        System.out.println(e);
    }

}
