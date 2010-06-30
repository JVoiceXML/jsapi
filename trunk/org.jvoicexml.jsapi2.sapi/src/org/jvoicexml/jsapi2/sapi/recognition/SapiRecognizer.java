package org.jvoicexml.jsapi2.sapi.recognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarManager;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestEvent;
import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.recognition.JseBaseRecognizer;

public final class SapiRecognizer extends JseBaseRecognizer {

    static {
        System.loadLibrary("Jsapi2SapiBridge");
    }

    /** SAPI recognizer Handle. **/
    private long recognizerHandle;

    /**
     * Constructs a new object.
     * @param mode the recognizer mode.
     */
    public SapiRecognizer(final SapiRecognizerMode mode) {
        super(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector<?> getBuiltInGrammars() {
        return sapiGetBuildInGrammars(recognizerHandle);
    }

    private native Vector<?> sapiGetBuildInGrammars(long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleAllocate() throws EngineStateException, EngineException,
            AudioException, SecurityException {
        recognizerHandle = sapiAllocate();
    }

    private native long sapiAllocate();

    @Override
    public void handleDeallocate() {
        sapiDeallocate(recognizerHandle);
    }

    private native void sapiDeallocate(long handle);

    @Override
    protected void handlePause() {
        sapiPause(recognizerHandle);
    }

    private native void sapiPause(long handle);

    @Override
    protected void handlePause(int flags) {
        sapiPause(recognizerHandle, flags);
    }

    private native void sapiPause(long handle, int flags);

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean handleResume() throws EngineStateException {
        final GrammarManager manager = getGrammarManager();
        final Grammar[] grammars = manager.listGrammars();
        final String[] grammarSources = new String[grammars.length];
        int i = 0;
        for (Grammar grammar : grammars) {
            try {
                final File file = File.createTempFile("sapi", "xml");
                file.deleteOnExit();
                final FileOutputStream out = new FileOutputStream(file);
                final String xml = grammar.toString();
                out.write(xml.getBytes());
                out.close();
                grammarSources[i] = file.getCanonicalPath();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ++i;
        }
        return sapiResume(recognizerHandle, grammarSources);
    }

    private native boolean sapiResume(long handle, String[] grammars)
        throws EngineStateException;

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean setGrammars(
            @SuppressWarnings("rawtypes") Vector grammarDefinition) {
        return false;
    }

    public boolean setGrammar(final String grammarPath) {
        return sapiSetGrammar(recognizerHandle, grammarPath);
    }

    private native boolean sapiSetGrammar(long handle, String grammarPath);

    @Override
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        return null;
    }

    void startRecognition() {
        start(recognizerHandle);
    }

    private native void start(long handle);

    class SapiChangeRequestListener
            implements EnginePropertyChangeRequestListener {

        @Override
        public void propertyChangeRequest(EnginePropertyChangeRequestEvent event) {
            // TODO Auto-generated method stub

        }

    }

}
