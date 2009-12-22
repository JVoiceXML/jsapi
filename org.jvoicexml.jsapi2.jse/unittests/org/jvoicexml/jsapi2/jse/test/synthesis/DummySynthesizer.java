package org.jvoicexml.jsapi2.jse.test.synthesis;
import javax.speech.AudioManager;
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
    public Speakable getSpeakable(String text) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleAllocate()
     */
    @Override
    public boolean handleAllocate() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleCancel()
     */
    @Override
    public boolean handleCancel() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleCancel(int)
     */
    @Override
    public boolean handleCancel(int id) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleCancelAll()
     */
    @Override
    public boolean handleCancelAll() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleDeallocate()
     */
    @Override
    public boolean handleDeallocate() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handlePause()
     */
    @Override
    public boolean handlePause() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleResume()
     */
    @Override
    public boolean handleResume() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleSpeak(int, java.lang.String)
     */
    @Override
    public void handleSpeak(int id, String item) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.synthesis.BaseSynthesizer#handleSpeak(int, javax.speech.synthesis.Speakable)
     */
    @Override
    public void handleSpeak(int id, Speakable item) {
        // TODO Auto-generated method stub

    }

    @Override
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        return null;
    }

    @Override
    protected AudioManager createAudioManager() {
        // TODO Auto-generated method stub
        return null;
    }

}
