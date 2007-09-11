package org.jvoicexml.jsapi2.recognition;

import org.jvoicexml.jsapi2.BaseEngineProperties;
import javax.speech.recognition.RecognizerProperties;
import javax.speech.recognition.Recognizer;

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
public class BaseRecognizerProperties extends BaseEngineProperties implements RecognizerProperties {

    public BaseRecognizerProperties(Recognizer recognizer) {
        super(recognizer);
    }

    /**
     * getAdaptation
     *
     * @return int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public int getAdaptation() {
        return 0;
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
     * getCompleteTimeout
     *
     * @return int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public int getCompleteTimeout() {
        return 0;
    }

    /**
     * getConfidenceThreshold
     *
     * @return int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public int getConfidenceThreshold() {
        return 0;
    }

    /**
     * getEndpointStyle
     *
     * @return int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public int getEndpointStyle() {
        return 0;
    }

    /**
     * getIncompleteTimeout
     *
     * @return int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public int getIncompleteTimeout() {
        return 0;
    }

    /**
     * getNumResultAlternatives
     *
     * @return int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public int getNumResultAlternatives() {
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
     * getSensitivity
     *
     * @return int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public int getSensitivity() {
        return 0;
    }

    /**
     * getSpeedVsAccuracy
     *
     * @return int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public int getSpeedVsAccuracy() {
        return 0;
    }

    /**
     * isResultAudioProvided
     *
     * @return boolean
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public boolean isResultAudioProvided() {
        return false;
    }

    /**
     * isTrainingProvided
     *
     * @return boolean
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public boolean isTrainingProvided() {
        return false;
    }

    /**
     * reset
     *
     * @todo Implement this javax.speech.EngineProperties method
     */
    public void reset() {
    }

    /**
     * setAdaptation
     *
     * @param adapt int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public void setAdaptation(int adapt) {
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
     * setCompleteTimeout
     *
     * @param timeout int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public void setCompleteTimeout(int timeout) {
    }

    /**
     * setConfidenceThreshold
     *
     * @param confidenceThreshold int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public void setConfidenceThreshold(int confidenceThreshold) {
    }

    /**
     * setEndpointStyle
     *
     * @param endpointStyle int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public void setEndpointStyle(int endpointStyle) {
    }

    /**
     * setIncompleteTimeout
     *
     * @param timeout int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public void setIncompleteTimeout(int timeout) {
    }

    /**
     * setNumResultAlternatives
     *
     * @param num int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public void setNumResultAlternatives(int num) {
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
     * setResultAudioProvided
     *
     * @param audioProvided boolean
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public void setResultAudioProvided(boolean audioProvided) {
    }

    /**
     * setSensitivity
     *
     * @param sensitivity int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public void setSensitivity(int sensitivity) {
    }

    /**
     * setSpeedVsAccuracy
     *
     * @param speedVsAccuracy int
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public void setSpeedVsAccuracy(int speedVsAccuracy) {
    }

    /**
     * setTrainingProvided
     *
     * @param trainingProvided boolean
     * @todo Implement this javax.speech.recognition.RecognizerProperties
     *   method
     */
    public void setTrainingProvided(boolean trainingProvided) {
    }
}
