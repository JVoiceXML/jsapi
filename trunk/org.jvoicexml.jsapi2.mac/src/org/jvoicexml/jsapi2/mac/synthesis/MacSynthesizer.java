package org.jvoicexml.jsapi2.mac.synthesis;

import java.io.ByteArrayInputStream;

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
import org.jvoicexml.jsapi2.jse.synthesis.JseBaseSynthesizer;

/**
 * A SAPI compliant {@link javax.speech.synthesis.Synthesizer}.
 * @author Dirk Schnelle-Walka
 * @author Josua Arndt
 *
 */
public final class MacSynthesizer extends JseBaseSynthesizer {

    static {
        System.loadLibrary("Jsapi2MacBridge");
    }

    /** SAPI synthesizer handle. */
    private long synthesizerHandle;

    /**
     * Constructs a new synthesizer object.
     * @param mode the synthesizer mode
     */
    MacSynthesizer(final MacSynthesizerMode mode) {
        super(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleAllocate() throws EngineStateException,
        EngineException, AudioException, SecurityException {
        final Voice voice;
        final MacSynthesizerMode mode = (MacSynthesizerMode) getEngineMode();
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
        synthesizerHandle = macHandleAllocate(voice);
    }

    /**
     * Allocates a Mac synthesizer.
     * @param voice the voice to use
     * @return synthesizer handle
     */
    private native long macHandleAllocate(final Voice voice);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleCancel() {
        return macHandleCancel(synthesizerHandle);
    }

    /**
     * Cancels the current output.
     * @param handle the synthesizer handle
     * @return <code>true</code> if the current output has been canceled
     */
    private native boolean macHandleCancel(final long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean handleCancel(final int id) {
        return macHandleCancel(synthesizerHandle, id);
    }

    /**
     * Cancels the output with the given id.
     * @param handle the synthesizer handle
     * @param id the item to cancel
     * @return <code>true</code> if the output with the given id has been
     * canceled
     */
    private native boolean macHandleCancel(final long handle, final int id);

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean handleCancelAll() {
        return macHandleCancelAll(synthesizerHandle);
    }

    /**
     * Cancels all outputs.
     * 
     * @param handle
     *            the synthesizer handle
     * @return <code>true</code> if at least one output has been canceled
     */
    private native boolean macHandleCancelAll(final long handle);

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
        macHandlDeallocate(synthesizerHandle);
        synthesizerHandle = 0;
    }

    /**
     * Deallocates the SAPI synthesizer.
     * @param handle
     *            the synthesizer handle
     */
    private native void macHandlDeallocate(final long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    public void handlePause() {
        macHandlePause(synthesizerHandle);
    }

    /**
     * Pauses the synthesizer.
     * @param handle the synthesizer handle
     *            the synthesizer handle
     */
    private native void macHandlePause(final long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleResume() {
        return macHandlResume(synthesizerHandle);
    }

    /**
     * Resumes the synthesizer.
     * @param handle the synthesizer handle
     *            the synthesizer handle
     * @return <code>true</code> if the synthesizer is resumed
     */
    private native boolean macHandlResume(final long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleSpeak(final int id, final String item) {
        final byte[] bytes = macHandleSpeak(synthesizerHandle, id, item);
        System.out.println("Returned from JNI with " + bytes.length + " bytes");
        final AudioManager manager = getAudioManager();
        final String locator = manager.getMediaLocator();
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes);
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
    private native byte[] macHandleSpeak(final long handle, final int id,
            final String item);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleSpeak(final int id, final Speakable item) {
        final String markup = item.getMarkupText();
        macHandleSpeakSsml(synthesizerHandle, id, markup);
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
    private native byte[] macHandleSpeakSsml(final long handle, final int id,
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
        return macGetAudioFormat(synthesizerHandle);
    }

    /**
     * retrieves the default audio format.
     * @param handle synthesizer handle.
     * @return native audio format
     */
    private native AudioFormat macGetAudioFormat(final long handle);
}

