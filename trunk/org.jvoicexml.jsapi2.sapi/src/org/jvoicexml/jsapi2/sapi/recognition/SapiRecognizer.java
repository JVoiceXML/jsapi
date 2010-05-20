package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestEvent;
import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.recognition.JseBaseRecognizer;

public class SapiRecognizer extends JseBaseRecognizer  {
		
	public SapiRecognizer(){
		String dir = System.getProperty("user.dir");
    	System.out.println(dir);
    	System.load(dir+"\\cpp\\Jsapi2SapiBridge\\Debug\\JSapi2SapiBridge.dll");				
	}
	
	public long recognizerHandle;

	@Override
	public Vector<?> getBuiltInGrammars(){
	    return sapiGetBuildInGrammars(recognizerHandle);
	}
	
	private native Vector<?> sapiGetBuildInGrammars(long handle);

	
        @Override
	public void handleAllocate()throws EngineStateException,
			EngineException, AudioException, SecurityException
			{
             sapiAllocate();
        } 
        private native void sapiAllocate();

	@Override
	public void handleDeallocate(){
	    sapiDeallocate(recognizerHandle);
	}
	private native void sapiDeallocate(long handle);
	
	
	@Override
	protected void handlePause(){
	     sapiPause(recognizerHandle);
	}
	private native void sapiPause(long handle);

        @Override
	protected void handlePause(int flags){
            sapiPause( recognizerHandle, flags);
        }

	private native void sapiPause(long handle, int flags);

	
        @Override
	protected boolean handleResume(){
            return sapiResume(recognizerHandle);
        }
	private native boolean sapiResume(long Handle);
	
	
	@SuppressWarnings("unchecked")
        @Override
	protected boolean setGrammars(Vector grammarDefinition){
	    
	    return false;
	}
	
	
	public boolean setGrammar(String grammarPath ){
	    return sapiSetGrammar( recognizerHandle, grammarPath);
	}
	private native boolean sapiSetGrammar(long handle, String grammarPath);

        @Override
	protected EnginePropertyChangeRequestListener getChangeRequestListener(){return null;}
        
        void startRecognition(){
            start(recognizerHandle);           
        }
        private native void start(long handle);
        
        
        class SapiChangeRequestListener implements EnginePropertyChangeRequestListener{

            @Override
            public void propertyChangeRequest(EnginePropertyChangeRequestEvent event) {
                // TODO Auto-generated method stub
                
            }
            
        }
        
}
