package org.jvoicexml.jsapi2.sapi.synthesis;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;

/**
 * A SAPI compliant {@link javax.speech.synthesis.Synthesizer}.
 * @author Dirk Schnelle-Walka
 * @author Josua Arndt
 *
 */
public final class SapiSynthesizer extends JseBaseSynthesizer {

    static {
        System.loadLibrary("Jsapi2SapiBridge");
    }

    /** Name of this engine. */
    private final String engineName;

	public long sapiSynthesizerPtr;
	
    /**
     * Constructs a new synthesizer object.
     * @param name voice name;
     */
    public SapiSynthesizer(final String name) {
        engineName = "name=" + name;
    }
	
	protected native Speakable getSpeakable(String text);


    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleAllocate() throws EngineStateException,
        EngineException, AudioException, SecurityException {
        sapiSynthesizerPtr = handleAllocate(engineName);
    }

    /**
     * Allocates a SAPI synthesizer.
     * @param name name of this synthesizer
     * @return synthesizer handle
     */
    private native long handleAllocate(final String name);

	public native boolean handleCancel();

	
	protected native boolean handleCancel(int id);

	
	protected native boolean handleCancelAll();

	
	public native void handleDeallocate();


	public native void handlePause();

	
	public native boolean handleResume();


    /**
     * {@inheritDoc}
     */
    @Override
    public void handleSpeak(final int id, final String item) {
        handleSpeak(sapiSynthesizerPtr, id, item);
    }

    /**
     * Speaks the given item.
     * @param handle synthesizer handle
     * @param id id of the item
     * @param item the item to speak
     */
    private native void handleSpeak(final long handle, final int id,
            final String item);
	
	protected native void handleSpeak(int id, Speakable item);

	
	protected native EnginePropertyChangeRequestListener getChangeRequestListener();


}

