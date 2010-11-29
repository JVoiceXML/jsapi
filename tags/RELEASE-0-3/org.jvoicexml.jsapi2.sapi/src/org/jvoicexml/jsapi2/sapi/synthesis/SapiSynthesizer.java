package org.jvoicexml.jsapi2.sapi.synthesis;

import java.io.InputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.AudioSegment;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.Voice;

import org.jvoicexml.jsapi2.EnginePropertyChangeRequestListener;
import org.jvoicexml.jsapi2.jse.BaseAudioSegment;
import org.jvoicexml.jsapi2.jse.JseBaseAudioManager;
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;

/**
 * A SAPI compliant {@link javax.speech.synthesis.Synthesizer}.
 * @author Dirk Schnelle-Walka
 * @author Josua Arndt
 *
 */
public final class SapiSynthesizer extends JseBaseSynthesizer {

    /** SAPI synthesizer handle. */
    private long synthesizerHandle;

    /**
     * Constructs a new synthesizer object.
     * @param mode the synthesizer mode
     */
    SapiSynthesizer(final SapiSynthesizerMode mode) {
        super(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleAllocate() throws EngineStateException,
        EngineException, AudioException, SecurityException {
        final Voice voice;
        final SapiSynthesizerMode mode = (SapiSynthesizerMode) getEngineMode();
        if (mode == null) {
            voice = null;
        } else {
            final Voice[] voices = mode.getVoices();
            if (voices == null) {
                voice = null;
            } else {
                voice = voices[0];
            }
        }
        synthesizerHandle = sapiHandleAllocate(voice);
    }

    /**
     * Allocates a SAPI synthesizer.
     * @param voice the voice to use
     * @return synthesizer handle
     */
    private native long sapiHandleAllocate(final Voice voice);

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
        // Leave some time to let all resources detach
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
           return;
        }
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
        final byte[] bytes = sapiHandleSpeak(synthesizerHandle, id, item);
        final JseBaseAudioManager manager = (JseBaseAudioManager) getAudioManager();
        final String locator = manager.getMediaLocator();
        InputStream in = null;
        try {
            in = manager.getAudioFormatConverter().getConvertedAudio(bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        final AudioSegment segment;
        if (locator == null) {
            segment = new BaseAudioSegment(item, in);
        } else {
            segment = new BaseAudioSegment(locator, item, in);
        }
        setAudioSegment(id, segment);
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
     * @return byte array of the synthesized speech
     */
    private native byte[] sapiHandleSpeak(final long handle, final int id,
            final String item);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleSpeak(final int id, final Speakable item) {
        final String markup = item.getMarkupText();
        final byte[] bytes = sapiHandleSpeakSsml(synthesizerHandle, id, markup);
        final JseBaseAudioManager manager = (JseBaseAudioManager) getAudioManager();
        final String locator = manager.getMediaLocator();
        InputStream in = null;
        try {
            in = manager.getAudioFormatConverter().getConvertedAudio(bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        final AudioSegment segment;
        if (locator == null) {
            segment = new BaseAudioSegment(markup, in);
        } else {
            segment = new BaseAudioSegment(locator, markup, in);
        }
        setAudioSegment(id, segment);
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
     * @return byte array of the synthesized speech
     */
    private native byte[] sapiHandleSpeakSsml(final long handle, final int id,
            final String ssml);

    /**
     * {@inheritDoc}
     */
    @Override
    protected EnginePropertyChangeRequestListener getChangeRequestListener() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AudioFormat getAudioFormat() {
        return sapiGetAudioFormat(synthesizerHandle);
    }

    /**
     * retrieves the default audio format.
     * @param handle synthesizer handle.
     * @return native audio format
     */
    private native AudioFormat sapiGetAudioFormat(final long handle);
}

