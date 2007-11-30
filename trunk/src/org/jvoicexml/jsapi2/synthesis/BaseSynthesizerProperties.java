package org.jvoicexml.jsapi2.synthesis;

import org.jvoicexml.jsapi2.BaseEngineProperties;
import javax.speech.synthesis.SynthesizerProperties;
import java.beans.PropertyChangeListener;
import javax.speech.synthesis.Voice;
import javax.speech.synthesis.Synthesizer;
import java.util.Vector;
import javax.speech.synthesis.SynthesizerMode;
import javax.speech.EngineMode;

/**
 * <p>Title: JSAPI 2.0</p>
 *
 * <p>Description: An independent reference implementation of JSR 113</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
 *
 * @author lyncher
 * @version 1.0
 */
public class BaseSynthesizerProperties extends BaseEngineProperties implements SynthesizerProperties {

    private Vector propertyChangeListeners;
    private int interruptibility;
    private int pitch;
    private int pitchRange;
    private int speakingRate;
    private Voice voice;
    private int volume;


    public BaseSynthesizerProperties(Synthesizer synthesizer) {
        super(synthesizer);
        propertyChangeListeners = new Vector();
        reset();
    }

    /**
     * addPropertyChangeListener
     *
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (!propertyChangeListeners.contains(listener)) {
            propertyChangeListeners.addElement(listener);
        }
    }

    /**
     * getInterruptibility
     *
     * @return int
     */
    public int getInterruptibility() {
        return interruptibility;
    }

    /**
     * getPitch
     *
     * @return int
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * getPitchRange
     *
     * @return int
     */
    public int getPitchRange() {
        return pitchRange;
    }

    /**
     * getSpeakingRate
     *
     * @return int
     */
    public int getSpeakingRate() {
        return speakingRate;
    }

    /**
     * getVoice
     *
     * @return Voice
     */
    public Voice getVoice() {
        return voice;
    }

    /**
     * getVolume
     *
     * @return int
     */
    public int getVolume() {
        return volume;
    }

    /**
     * removePropertyChangeListener
     *
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeListeners.removeElement(listener);
    }

    /**
     * reset
     *
     */
    public void reset() {
        setInterruptibility(OBJECT_LEVEL);
        setPitch(160);
        setPitchRange((int)(160 * 0.60));
        setSpeakingRate(DEFAULT_RATE);
        setVolume(MEDIUM_VOLUME);

        //Set default voice
        Voice[] voices = ((SynthesizerMode)((Synthesizer)engine).getEngineMode()).getVoices();
        if ((voices != null) && (voices.length > 0)) {
            setVoice(voices[0]);
        }
        else {
            setVoice(null);
        }

        super.reset();
    }

    /**
     * setInterruptibility
     *
     * @param level int
     *
     * @todo Default values are in System.getProperty:
     *   Synthesizer.defaultTrustedInterruptibility
     *   Synthesizer.defaultUntrustedInterruptibility
     *   Synthesizer.maximumUntrustedInterruptibility
     *
     */
    public void setInterruptibility(int level) {
        postPropertyChangeEvent("interruptibility", new Integer(interruptibility),
                                new Integer(level));
        interruptibility = level;
    }

    /**
     * setPitch
     *
     * @param hertz int
     */
    public void setPitch(int hertz) {
        postPropertyChangeEvent("pitch", new Integer(pitch), new Integer(hertz));
        pitch = hertz;
    }

    /**
     * setPitchRange
     *
     * @param hertz int
     */
    public void setPitchRange(int hertz) {
        postPropertyChangeEvent("pitchRange", new Integer(pitchRange), new Integer(hertz));
        pitchRange = hertz;
    }

    /**
     * setSpeakingRate
     *
     * @param wpm int
     */
    public void setSpeakingRate(int wpm) {
        postPropertyChangeEvent("speakingRate", new Integer(speakingRate), new Integer(wpm));
        speakingRate = wpm;
    }

    /**
     * setVoice
     *
     * @param voice Voice
     */
    public void setVoice(Voice voice) {
        postPropertyChangeEvent("voice", this.voice, voice);
        this.voice = voice;
    }

    /**
     * setVolume
     *
     * @param volume int
     */
    public void setVolume(int volume) {
        postPropertyChangeEvent("volume", new Integer(this.volume), new Integer(volume));
        this.volume = volume;
    }
}
