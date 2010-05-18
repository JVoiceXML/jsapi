package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.recognition.JseBaseRecognizer;

public class SapiRecognizer extends JseBaseRecognizer  {
	
	static{
    	System.load("D:\\eclipse - Kopie\\Workspace\\org.jvoicexml.jsapi2.sapi\\cpp\\Jsapi2SapiBridge\\Debug\\JSapi2SapiBridge.dll");				
    	}
	
	long sapiRecognizerPtr;

	@Override
	public native Vector getBuiltInGrammars();

	@Override
	public
	native void handleAllocate() throws EngineStateException,
			EngineException, AudioException, SecurityException;

	@Override
	public
	native void handleDeallocate();

	@Override
	protected native void handlePause();

	@Override
	protected native void handlePause(int flags);

	@Override
	protected native boolean handleResume();

	@Override
	protected native boolean setGrammars(Vector grammarDefinition);
	public native boolean setGrammar(String grammarPath);


	@Override
	protected native EnginePropertyChangeRequestListener getChangeRequestListener();

}
