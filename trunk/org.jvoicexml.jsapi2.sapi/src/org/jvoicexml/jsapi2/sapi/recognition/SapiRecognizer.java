package org.jvoicexml.jsapi2.sapi.recognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import javax.speech.recognition.RuleGrammar;

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
//               System.out.println(xml);
//                System.out.println(grammarSources[i]);
                          
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ++i;
        }
        
        Recognition reco = new Recognition();
        reco.setGrammarSource(grammarSources);
        reco.start();
        
        return true;        
    }

    private native String sapiResume(long handle, String[] grammars)
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
    
    
    private class Recognition extends Thread{
        
        String[] grammarSource;
        
        public void setGrammarSource( String[] source ){
            grammarSource = source;
        }
        
        public void run(){            
                 String result = sapiResume(recognizerHandle, grammarSource);
                 reportResult(result);
        }
        
        /**
         * Notification from the SAPI recognizer about a recognition result.
         * @param utterance the detected utterance
         */
        private void reportResult(final String utterance) {
      
            GrammarManager manager = getGrammarManager();
            Grammar[] grammars = manager.listGrammars();
            
            BaseResult result = null; 
            
            for (Grammar grammar : grammars) {               
        
                try {
                    result = new BaseResult( grammar , utterance);
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

}
