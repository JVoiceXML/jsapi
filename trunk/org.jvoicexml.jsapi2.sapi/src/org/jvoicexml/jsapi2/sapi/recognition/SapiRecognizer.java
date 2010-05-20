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
	
	public long RecognizerHandle;

	@Override
	public Vector<?> getBuiltInGrammars(){
	    return sapiGetBuildInGrammars(RecognizerHandle);
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
	    sapiDeallocate(RecognizerHandle);
	}
	private native void sapiDeallocate(long handle);
	
	
	@Override
	protected void handlePause(){
	     sapiPause(RecognizerHandle);
	}
	private native void sapiPause(long handle);

        @Override
	protected void handlePause(int flags){
            sapiPause( RecognizerHandle, flags);
        }

	private native void sapiPause(long handle, int flags);

	
        @Override
	protected boolean handleResume(){
            return sapiResume(RecognizerHandle);
        }
	private native boolean sapiResume(long Handle);
	
	
	@Override
	protected boolean setGrammars(Vector grammarDefinition){
	    return false;
	}
	
	
	public boolean setGrammar(String grammarPath ){
	    return sapiSetGrammar( RecognizerHandle, grammarPath);
	}
	private native boolean sapiSetGrammar(long handle, String grammarPath);

        @Override
	protected EnginePropertyChangeRequestListener getChangeRequestListener(){return null;}
        
        void startRecognition(){
            start(RecognizerHandle);           
        }
        private native void start(long handle);
        
        
        class SapiChangeRequestListener implements EnginePropertyChangeRequestListener{

            @Override
            public void propertyChangeRequest(EnginePropertyChangeRequestEvent event) {
                // TODO Auto-generated method stub
                
            }
            
        }
        
}
