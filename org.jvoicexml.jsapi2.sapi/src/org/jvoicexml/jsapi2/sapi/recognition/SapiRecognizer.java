package org.jvoicexml.jsapi2.sapi.recognition;

import java.io.StringReader;
import java.util.Vector;
import java.util.logging.Level;
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

    /** Asynchronous recognition. */
    private SapiRecognitionThread recognitionThread;

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handlePause() {
        sapiPause(recognizerHandle);
        if (recognitionThread != null) {
            recognitionThread.stopRecognition();
            recognitionThread = null;
        }
    }

    private native void sapiPause(long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handlePause(final int flags) {
        sapiPause(recognizerHandle, flags);
        if (recognitionThread != null) {
            recognitionThread.stopRecognition();
            recognitionThread = null;
        }
    }

    private native void sapiPause(long handle, int flags);

    /**
     * Start recognition.
     * @param handle the recognizer handle
     * @return recognition result
     */
    native String sapiRecognize(final long handle);

    public long getRecognizerHandle() {
        return recognizerHandle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean handleResume() throws EngineStateException {
        GrammarManager manager = getGrammarManager();
        Grammar[] grammars = manager.listGrammars();
        final String[] grammarSources = new String[grammars.length];
        final String[] grammarReferences = new String[grammars.length];
        int i = 0;
        
        if(LOGGER.isLoggable(Level.FINE)){
            for (Grammar grammar : grammars) {
            LOGGER.fine("Activate Grammar : "+ grammar.getReference() );
            }
        }
        
        for (Grammar grammar : grammars) {
            grammarSources[i] = grammar.toString();
            grammarReferences[i] = grammar.getReference();
            i++;
        }
        
        sapiResume(recognizerHandle, grammarSources, grammarReferences);
        recognitionThread = new SapiRecognitionThread(this);
        recognitionThread.start();
        return true;
    }

    private native boolean sapiResume(long handle, String[] grammars, String[] references)
    throws EngineStateException;


    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean setGrammars(
            @SuppressWarnings("rawtypes") final Vector grammarDefinition) {
        return false;
    }

    public boolean setGrammar(final String grammarPath, String reference) {
        return sapiSetGrammar(recognizerHandle, grammarPath, reference);
    }

    private native boolean sapiSetGrammar(long handle, String grammarPath, String reference);
    
    public boolean setGrammarContent(final String grammarContent, String reference) {
        return sapiSetGrammarContent(recognizerHandle, grammarContent, reference);
    }

    private native boolean sapiSetGrammarContent(long handle, String grammarPath, String reference);
        
    
    
    
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
    

    public void reportResult( String utterance) {
       
       SapiResult result = null;     
       
       try {
            result = new SapiResult();
        } catch (GrammarException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    
       if (utterance == null ) { 
           if(LOGGER.isLoggable(Level.FINE)){
               LOGGER.fine("No Match Recognized ...");
           }          
           final ResultEvent rejected =
               new ResultEvent(result, ResultEvent.RESULT_REJECTED,
                       false, false);
           postResultEvent(rejected);
           
           return;          
        }
       
       if(LOGGER.isLoggable(Level.FINE)){
           LOGGER.fine("SSML Result String : "+ utterance);
       } 
       System.out.println(utterance);
       result.setSsml(utterance);      
       
       SMLHandler handler= new SMLHandler(result);
       StringReader reader = new StringReader(utterance);
       
       QDParser parser = new QDParser();
       try {
        parser.parse(handler, reader);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if ( utterance.equals("...") ) { 
            if(LOGGER.isLoggable(Level.FINE)){
                LOGGER.fine("Single Garbage Recognized ...");
            }          
            final ResultEvent rejected =
                new ResultEvent(result, ResultEvent.RESULT_REJECTED,
                        false, false);
            postResultEvent(rejected);
            
            return;          
         }
              
//        final GrammarManager manager = getGrammarManager();
//        final Grammar[] grammars = manager.listGrammars();
//
//       BaseResult result = null;
//               
//        for (Grammar grammar : grammars) {
//            try {               
//
//                result = new BaseResult(grammar, utterance );
//                           
//            } catch (GrammarException e) {
//                LOGGER.error(e.getMessage());
//                return;
//            } 
//            
//            if (result.getNumTokens() != 0) {                 
//                break;
//            }                             
//        }
//        
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Recognized utterance : '"+ utterance
                    + "' Confidence : '"+ result.getConfidence() +"'");
        }
        
        final ResultEvent created = new ResultEvent(result,
                ResultEvent.RESULT_CREATED, false, false);
        postResultEvent(created);
    
        final ResultEvent grammarFinalized =
            new ResultEvent(result, ResultEvent.GRAMMAR_FINALIZED);
        postResultEvent(grammarFinalized);
        
        if( result.getConfidence()< 0.4 ){
            result.setResultState(Result.REJECTED);
            if(LOGGER.isLoggable(Level.FINE)){
                LOGGER.fine("Result confidence to low, new ResultState: '"+ result.getResultState() +"'");
            }
        }
            
        if (result.getResultState() == Result.REJECTED ) {
            final ResultEvent rejected =
                new ResultEvent(result, ResultEvent.RESULT_REJECTED,
                        false, false);
            postResultEvent(rejected);
        } else {
            result.setResultState(Result.ACCEPTED);
            final ResultEvent accepted = new ResultEvent(result,
                        ResultEvent.RESULT_ACCEPTED, false, false);
            postResultEvent(accepted);
        }
   }

    @Override
    protected void handleRequestFocus() {
        
//        if(LOGGER.isDebugEnabled()){
//            LOGGER.debug("handleRequestFocus : I do nothing ");
//        }       
    }
    
    @Override
    protected void handleReleaseFocus() {
//        if(LOGGER.isDebugEnabled()){
//            LOGGER.debug("handleReleaseFocus : I do nothing ");
//        }   
        
    }
    
    native void sapiAbortRecognition(long handle);
}
