package org.jvoicexml.jsapi2.sapi.recognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultEvent;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestEvent;
import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.recognition.BaseResult;
import org.jvoicexml.jsapi2.jse.recognition.JseBaseRecognizer;

/**
 * A SAPI recognizer.
 * @author Dirk Schnelle-Walka
 *
 */
public final class SapiRecognizer extends JseBaseRecognizer {
    /** Logger for this class. */
    private static final Logger LOGGER =
            Logger.getLogger(SapiRecognizer.class.getName());

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
        GrammarManager manager = getGrammarManager();
        Grammar[] grammars = manager.listGrammars();
        final String[] grammarSources = new String[grammars.length];
        int i = 0;
        for (Grammar grammar : grammars) {
            try {
                final File file = File.createTempFile("sapi", "xml");
                file.deleteOnExit();
                final FileOutputStream out = new FileOutputStream(file);   
                String xml = grammar.toString();           
                out.write(xml.toString().getBytes());
                out.close();
                grammarSources[i] = file.getCanonicalPath();
            } catch (IOException e) {
                throw new EngineStateException(e.getMessage());
            }
            ++i;
        }
        sapiResume(recognizerHandle, grammarSources);
        
        final Thread thread = new Thread() {
            public void run() {
                String utterance = sapiRecognize(recognizerHandle);
                reportResult(utterance);
            }
        };
        thread.start();
        return true;
    }

    /**
     * Start recognition.
     * @param handle the recognizer handle
     * @return recognition result
     */
    private native String sapiRecognize(final long handle);

    public long getRecognizerHandle() {
        return recognizerHandle;
    }

    private native boolean sapiResume(long handle, String[] grammars)
        throws EngineStateException;

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean setGrammars(
            @SuppressWarnings("rawtypes") final Vector grammarDefinition) {
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
    
   public void reportResult(final String utterance) {
        if (utterance == null) {
            return;
        }
        final GrammarManager manager = getGrammarManager();
        final Grammar[] grammars = manager.listGrammars();
       
//        RuleGrammar grammar = currentGrammar;
        
        BaseResult result = null; 
        
        for (Grammar grammar : grammars) {
            try {
                result = new BaseResult(grammar, utterance);
            } catch (GrammarException e) {
                LOGGER.warning(e.getMessage());
                return;
            } 
        }
    
        final ResultEvent created = new ResultEvent(result,
                ResultEvent.RESULT_CREATED, false, false);
        postResultEvent(created);
    
        final ResultEvent grammarFinalized =
            new ResultEvent(result, ResultEvent.GRAMMAR_FINALIZED);
        postResultEvent(grammarFinalized);
    
        if (result.getResultState() == Result.REJECTED) {
            final ResultEvent rejected =
                new ResultEvent(result, ResultEvent.RESULT_REJECTED,
                        false, false);
            postResultEvent(rejected);
        } else {
            final ResultEvent accepted = new ResultEvent(result,
                        ResultEvent.RESULT_ACCEPTED, false, false);
            postResultEvent(accepted);
        }
   }
}
