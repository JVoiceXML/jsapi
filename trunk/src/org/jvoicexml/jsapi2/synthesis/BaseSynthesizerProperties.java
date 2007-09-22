package org.jvoicexml.jsapi2.synthesis;

import org.jvoicexml.jsapi2.BaseEngineProperties;
import javax.speech.synthesis.SynthesizerProperties;
import java.beans.PropertyChangeListener;
import javax.speech.synthesis.Voice;
import javax.speech.synthesis.Synthesizer;

/**
 * <p>Title: JSAPI 2.0</p>
 *
 * <p>Description: An independent reference implementation of JSR 113</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class BaseSynthesizerProperties extends BaseEngineProperties implements SynthesizerProperties {


    public BaseSynthesizerProperties(Synthesizer synthesizer) {
        super(synthesizer);
        reset();
    }

    /**
     * addPropertyChangeListener
     *
     * @param listener PropertyChangeListener
     * @todo Implement this javax.speech.EngineProperties method
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    /**
     * getBase
     *
     * @return String
     * @todo Implement this javax.speech.EngineProperties method
     */
    public String getBase() {
        return "";
    }

    /**
     * getInterruptibility
     *
     * @return int
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public int getInterruptibility() {
        return 0;
    }

    /**
     * getPitch
     *
     * @return int
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public int getPitch() {
        return 0;
    }

    /**
     * getPitchRange
     *
     * @return int
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public int getPitchRange() {
        return 0;
    }

    /**
     * getPriority
     *
     * @return int
     * @todo Implement this javax.speech.EngineProperties method
     */
    public int getPriority() {
        return 0;
    }

    /**
     * getSpeakingRate
     *
     * @return int
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public int getSpeakingRate() {
        return 0;
    }

    /**
     * getVoice
     *
     * @return Voice
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public Voice getVoice() {
        return null;
    }

    /**
     * getVolume
     *
     * @return int
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public int getVolume() {
        return 0;
    }

    /**
     * removePropertyChangeListener
     *
     * @param listener PropertyChangeListener
     * @todo Implement this javax.speech.EngineProperties method
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    /**
     * reset
     *
     * @todo Implement this javax.speech.EngineProperties method
     */
    public void reset() {

        super.reset();
    }

    /**
     * setBase
     *
     * @param uri String
     * @todo Implement this javax.speech.EngineProperties method
     */
    public void setBase(String uri) {
    }

    /**
     * setInterruptibility
     *
     * @param level int
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public void setInterruptibility(int level) {
    }

    /**
     * setPitch
     *
     * @param hertz int
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public void setPitch(int hertz) {
    }

    /**
     * setPitchRange
     *
     * @param hertz int
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public void setPitchRange(int hertz) {
    }

    /**
     * setPriority
     *
     * @param priority int
     * @todo Implement this javax.speech.EngineProperties method
     */
    public void setPriority(int priority) {
    }

    /**
     * setSpeakingRate
     *
     * @param wpm int
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public void setSpeakingRate(int wpm) {
    }

    /**
     * setVoice
     *
     * @param voice Voice
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public void setVoice(Voice voice) {
    }

    /**
     * setVolume
     *
     * @param volume int
     * @todo Implement this javax.speech.synthesis.SynthesizerProperties
     *   method
     */
    public void setVolume(int volume) {
    }
}
