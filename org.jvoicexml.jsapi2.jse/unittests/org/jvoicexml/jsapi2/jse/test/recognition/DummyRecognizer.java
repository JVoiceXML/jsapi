/**
 * 
 */
package org.jvoicexml.jsapi2.jse.test.recognition;

import java.util.List;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.recognition.Grammar;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer;

/**
 * Dummy implementation of a recognizer for test purposes.
 * @author Dirk Schnelle-Walka
 *
 */
public class DummyRecognizer extends BaseRecognizer {

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#getBuiltInGrammars()
     */
    @Override
    protected List<Grammar> getBuiltInGrammars() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#handleAllocate()
     */
    @Override
    protected void handleAllocate() throws EngineStateException,
            EngineException, AudioException, SecurityException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#handleDeallocate()
     */
    @Override
    protected void handleDeallocate() {
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#handlePause()
     */
    @Override
    protected void handlePause() {
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#handlePause(int)
     */
    @Override
    protected void handlePause(int flags) {
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#handleResume()
     */
    @Override
    protected boolean handleResume() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.jse.recognition.BaseRecognizer#setGrammars(java.util.List)
     */
    @Override
    protected boolean setGrammars(List grammarDefinition) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.jvoicexml.jsapi2.BaseEngine#getChangeRequestListener()
     */
    @Override
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        // TODO Auto-generated method stub
        return null;
    }

}
