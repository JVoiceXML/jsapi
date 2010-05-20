package org.jvoicexml.jsapi2.sapi.synthesis;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Speakable;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;
import org.jvoicexml.jsapi2.synthesis.BaseSpeakable;

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

    /** SAPI synthesizer handle. */
    private long synthesizerHandle;

    /**
     * Constructs a new synthesizer object.
     * @param name voice name;
     */
    public SapiSynthesizer(final String name) {
        engineName = "name=" + name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Speakable getSpeakable(final String text) {
        return new BaseSpeakable(text);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleAllocate() throws EngineStateException,
        EngineException, AudioException, SecurityException {
        synthesizerHandle = sapiHandleAllocate(engineName);
    }

    /**
     * Allocates a SAPI synthesizer.
     * @param name name of this synthesizer
     * @return synthesizer handle
     */
    private native long sapiHandleAllocate(final String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleCancel() {
        return sapiHandleCancel(synthesizerHandle);
    }

    /**
     * Cancels the current output.
     * @param handle the synthesizer handle
     * @return <code>true</code> if the current output has been canceled
     */
    private native boolean sapiHandleCancel(final long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean handleCancel(final int id) {
        return sapiHandleCancel(synthesizerHandle, id);
    }

    /**
     * Cancels the output with the given id.
     * @param handle the synthesizer handle
     * @param id the item to cancel
     * @return <code>true</code> if the output with the given id has been
     * canceled
     */
    private native boolean sapiHandleCancel(final long handle, final int id);

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean handleCancelAll() {
        return sapiHandleCancelAll(synthesizerHandle);
    }

    /**
     * Cancels all outputs.
     * 
     * @param handle
     *            the synthesizer handle
     * @return <code>true</code> if at least one output has been canceled
     */
    private native boolean sapiHandleCancelAll(final long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleDeallocate() {
        sapiHandlDeallocate(synthesizerHandle);
        synthesizerHandle = 0;
    }

    /**
     * Deallocates the SAPI synthesizer.
     * @param handle
     *            the synthesizer handle
     */
    private native void sapiHandlDeallocate(final long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    public void handlePause() {
        sapiHandlPause(synthesizerHandle);
    }

    /**
     * Pauses the synthesizer.
     * @param handle the synthesizer handle
     *            the synthesizer handle
     */
    private native void sapiHandlPause(final long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleResume() {
        return sapiHandlResume(synthesizerHandle);
    }

    /**
     * Resumes the synthesizer.
     * @param handle the synthesizer handle
     *            the synthesizer handle
     * @return <code>true</code> if the synthesizer is resumed
     */
    private native boolean sapiHandlResume(final long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleSpeak(final int id, final String item) {
        sapiHandleSpeak(synthesizerHandle, id, item);
    }

    /**
     * Speaks the given item.
     * 
     * @param handle
     *            synthesizer handle
     * @param id
     *            id of the item
     * @param item
     *            the item to speak
     */
    private native void sapiHandleSpeak(final long handle, final int id,
            final String item);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleSpeak(final int id, final Speakable item) {
        final String markup = item.getMarkupText();
        sapiHandleSpeakSsml(synthesizerHandle, id, markup);
    }

    /**
     * Speaks the given item.
     * 
     * @param handle
     *            synthesizer handle
     * @param id
     *            id of the item
     * @param ssml
     *            the SSML markup to speak
     */
    private native void sapiHandleSpeakSsml(final long handle, final int id,
            final String ssml);

    /**
     * {@inheritDoc}
     */
    @Override
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        return null;
    }
}

