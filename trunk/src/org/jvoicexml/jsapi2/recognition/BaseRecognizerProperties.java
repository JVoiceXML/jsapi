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
public class BaseRecognizerProperties extends BaseEngineProperties implements
        RecognizerProperties {

    protected int adaptation;
    protected int completeTimeout;
    protected int confidenceThreshold;
    protected int endpointStyle;
    protected int incompleteTimeout;
    protected int numResultAlternatives;
    protected int sensitivity;
    protected int speedVsAccuracy;
    protected boolean resultAudioProvided;
    protected boolean trainingProvided;

    public BaseRecognizerProperties(Recognizer recognizer) {
        super(recognizer);
        reset();
    }

    /**
     * getAdaptation
     *
     * @return int
     */
    public int getAdaptation() {
        return adaptation;
    }

    /**
     * getCompleteTimeout
     *
     * @return int
     */
    public int getCompleteTimeout() {
        return completeTimeout;
    }

    /**
     * getConfidenceThreshold
     *
     * @return int
     */
    public int getConfidenceThreshold() {
        return confidenceThreshold;
    }

    /**
     * getEndpointStyle
     *
     * @return int
     */
    public int getEndpointStyle() {
        return endpointStyle;
    }

    /**
     * getIncompleteTimeout
     *
     * @return int
     */
    public int getIncompleteTimeout() {
        return incompleteTimeout;
    }

    /**
     * getNumResultAlternatives
     *
     * @return int
     */
    public int getNumResultAlternatives() {
        return numResultAlternatives;
    }

    /**
     * getSensitivity
     *
     * @return int
     */
    public int getSensitivity() {
        return sensitivity;
    }

    /**
     * getSpeedVsAccuracy
     *
     * @return int
     */
    public int getSpeedVsAccuracy() {
        return speedVsAccuracy;
    }

    /**
     * isResultAudioProvided
     *
     * @return boolean
     */
    public boolean isResultAudioProvided() {
        return resultAudioProvided;
    }

    /**
     * isTrainingProvided
     *
     * @return boolean
     */
    public boolean isTrainingProvided() {
        return trainingProvided;
    }

    /**
     * reset
     *
     */
    public void reset() {
        setAdaptation(ADAPT_PAUSED | ADAPT_RESUMED);
        setCompleteTimeout(500);
        setConfidenceThreshold((MAX_CONFIDENCE - MIN_CONFIDENCE) / 4);
        setEndpointStyle(ENDPOINT_SPEECH_DETECTION);
        setIncompleteTimeout(1000);
        setNumResultAlternatives(1);
        setSensitivity(NORM_ACCURACY);
        setSpeedVsAccuracy(NORM_ACCURACY);
        setResultAudioProvided(false);
        setTrainingProvided(false);

        super.reset();
    }

    /**
     * setAdaptation
     *
     * @param adapt int
     * @throws IllegalArgumentException
     */
    public void setAdaptation(int adapt) throws IllegalArgumentException {
        postPropertyChangeEvent("adaptation", new Integer(this.adaptation),
                                new Integer(adapt));
        this.adaptation = adapt;
    }

    /**
     * setCompleteTimeout
     *
     * @param timeout int
     * @throws IllegalArgumentException
     */
    public void setCompleteTimeout(int timeout) throws IllegalArgumentException {
        if (timeout < 0)
            throw new IllegalArgumentException(
                    "Invalid completeTimeout: " + timeout);

        postPropertyChangeEvent("completeTimeout",
                                new Integer(this.completeTimeout),
                                new Integer(timeout));
        this.completeTimeout = timeout;
    }

    /**
     * setConfidenceThreshold
     *
     * @param confidenceThreshold int
     * @throws IllegalArgumentException
     */
    public void setConfidenceThreshold(int threshold) throws
            IllegalArgumentException {
        if ((confidenceThreshold > MAX_CONFIDENCE) ||
            (confidenceThreshold < MIN_CONFIDENCE))
            throw new IllegalArgumentException("Invalid confidenceThreshold: " +
                                               threshold);

        postPropertyChangeEvent("confidenceThreshold",
                                new Integer(this.confidenceThreshold),
                                new Integer(threshold));
        this.confidenceThreshold = threshold;
    }

    /**
     * setEndpointStyle
     *
     * @param endpointStyle int
     * @throws IllegalArgumentException
     */
    public void setEndpointStyle(int style) throws
            IllegalArgumentException {
        if ((style != ENDPOINT_SPEECH_DETECTION) &&
            (style != ENDPOINT_PUSH_TO_START) &&
            (style != ENDPOINT_PUSH_TO_TALK)) {
            throw new IllegalArgumentException("Invalid endpointStyle: " +
                                               style);
        }
        postPropertyChangeEvent("confidenceThreshold",
                                new Integer(this.endpointStyle),
                                new Integer(style));
        this.endpointStyle = style;
    }

    /**
     * setIncompleteTimeout
     *
     * @param timeout int
     * @throws IllegalArgumentException
     */
    public void setIncompleteTimeout(int timeout) throws
            IllegalArgumentException {
        if (timeout < 0)
            throw new IllegalArgumentException("Invalid incompleteTimeout: " +
                                               timeout);
        postPropertyChangeEvent("incompleteTimeout",
                                new Integer(this.incompleteTimeout),
                                new Integer(timeout));
        this.incompleteTimeout = timeout;
    }

    /**
     * setNumResultAlternatives
     *
     * @param num int
     * @throws IllegalArgumentException
     */
    public void setNumResultAlternatives(int num) throws
            IllegalArgumentException {
        postPropertyChangeEvent("numResultAlternatives",
                                new Integer(this.numResultAlternatives),
                                new Integer(num));
        this.numResultAlternatives = num;
    }

    /**
     * setSensitivity
     *
     * @param sensitivity int
     * @throws IllegalArgumentException
     */
    public void setSensitivity(int sensitivity) throws IllegalArgumentException {
        if ((sensitivity > MAX_CONFIDENCE) ||
            (sensitivity < MIN_CONFIDENCE))
            throw new IllegalArgumentException("Invalid sensitivity: " +
                                               sensitivity);

        postPropertyChangeEvent("sensitivity",
                                new Integer(this.sensitivity),
                                new Integer(sensitivity));
        this.sensitivity = sensitivity;
    }


    /**
     * setSpeedVsAccuracy
     *
     * @param speedVsAccuracy int
     * @throws IllegalArgumentException
     */
    public void setSpeedVsAccuracy(int speedVsAccuracy) throws
            IllegalArgumentException {
        if ((speedVsAccuracy != MAX_ACCURACY) &&
            (speedVsAccuracy != NORM_ACCURACY) &&
            (speedVsAccuracy != MAX_ACCURACY)) {
            throw new IllegalArgumentException("Invalid speedVsAccuracy: " +
                                               speedVsAccuracy);
        }
        postPropertyChangeEvent("speedVsAccuracy",
                                new Integer(this.speedVsAccuracy),
                                new Integer(speedVsAccuracy));
        this.speedVsAccuracy = speedVsAccuracy;
    }

    /**
     * setResultAudioProvided
     *
     * @param audioProvided boolean
     */
    public void setResultAudioProvided(boolean audioProvided) {
        postPropertyChangeEvent("resultAudioProvided",
                                new Boolean(this.resultAudioProvided),
                                new Boolean(audioProvided));
        this.resultAudioProvided = audioProvided;
    }

    /**
     * setTrainingProvided
     *
     * @param trainingProvided boolean
     */
    public void setTrainingProvided(boolean trainingProvided) {
        postPropertyChangeEvent("trainingProvided",
                                new Boolean(this.trainingProvided),
                                new Boolean(trainingProvided));
        this.trainingProvided = trainingProvided;
    }


}
