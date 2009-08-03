package org.jvoicexml.jsapi2.jse.test.synthesis;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer;

/**
 * 
 */

/**
 * @author DS01191
 *
 */
public class DummySynthesizer extends BaseSynthesizer {

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#getSpeakable(java.lang.String)
     */
    @Override
    protected Speakable getSpeakable(String text) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleAllocate()
     */
    @Override
    protected boolean handleAllocate() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleCancel()
     */
    @Override
    protected boolean handleCancel() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleCancel(int)
     */
    @Override
    protected boolean handleCancel(int id) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleCancelAll()
     */
    @Override
    protected boolean handleCancelAll() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleDeallocate()
     */
    @Override
    protected boolean handleDeallocate() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handlePause()
     */
    @Override
    protected boolean handlePause() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleResume()
     */
    @Override
    protected boolean handleResume() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleSpeak(int, java.lang.String)
     */
    @Override
    protected void handleSpeak(int id, String item) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleSpeak(int, javax.speech.synthesis.Speakable)
     */
    @Override
    protected void handleSpeak(int id, Speakable item) {
        // TODO Auto-generated method stub

    }

    @Override
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        return null;
    }

}
