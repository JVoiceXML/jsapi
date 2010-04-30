package org.jvoicexml.jsapi2.sapi.synthesis;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;

public class SapiSynthesizer extends JseBaseSynthesizer {


	protected native Speakable getSpeakable(String text);

	protected native void Allocate() throws EngineStateException,
			EngineException, AudioException, SecurityException;

	protected native boolean Cancel();

	protected native boolean Cancel(int id);

	protected native boolean CancelAll();

	protected native void Deallocate();

	protected native void Pause();

	protected native boolean Resume();

	protected native void Speak(int id, String item);

	protected native void Speak(int id, Speakable item);

	protected native EnginePropertyChangeRequestListener getChangeRequestListener();

}

