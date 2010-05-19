package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;

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
	    return nativGetBuildInGrammars(RecognizerHandle);
	}
	
	private native Vector<?> nativGetBuildInGrammars(long handle);

	
        @Override
	public void handleAllocate()throws EngineStateException,
			EngineException, AudioException, SecurityException
			{
             nativeHandleAllocate();
        } 
        private native void nativeHandleAllocate();

	@Override
	public void handleDeallocate(){
	    nativHandleDeallocate(RecognizerHandle);
	}
	private native void nativHandleDeallocate(long handle);
	
	
	@Override
	protected void handlePause(){
	     nativHandlePause(RecognizerHandle);
	}
	private native void nativHandlePause(long handle);

        @Override
	protected void handlePause(int flags){
            nativHandlePause( RecognizerHandle, flags);
        }

	private native void nativHandlePause(long handle, int flags);

	
        @Override
	protected boolean handleResume(){
            return nativHandleResume(RecognizerHandle);
        }
	private native boolean nativHandleResume(long Handle);
	
	
	@Override
	protected boolean setGrammars(Vector grammarDefinition){
	    return false;
	}
	
	
	public boolean setGrammar(String grammarPath ){
	    return nativSetGrammar( RecognizerHandle, grammarPath);
	}
	private native boolean nativSetGrammar(long handle, String grammarPath);

        @Override
	protected EnginePropertyChangeRequestListener getChangeRequestListener(){return null;}

        void startRecognition(){
            start(RecognizerHandle);           
        }
        private native void start(long handle);
        
        
}
