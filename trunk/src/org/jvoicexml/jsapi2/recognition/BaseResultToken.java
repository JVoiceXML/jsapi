package org.jvoicexml.jsapi2.recognition;

import javax.speech.recognition.Result;
import javax.speech.recognition.ResultToken;
import javax.speech.recognition.RecognizerProperties;

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
public class BaseResultToken implements ResultToken {

    private int confidenceLevel;
    private long startTime;
    private long endTime;
    private final Result result;
    private final String token;

    public BaseResultToken(Result result, String token) {
        this.result = result;
        confidenceLevel = RecognizerProperties.NORM_CONFIDENCE;
        startTime = -1;
        endTime = -1;
        this.token = token;
    }

    /**
     * getConfidenceLevel
     *
     * @return int
     */
    public int getConfidenceLevel() {
        if (result.getResultState() == Result.ACCEPTED)
            return confidenceLevel;
        else
            return RecognizerProperties.UNKNOWN_CONFIDENCE;
    }

    public void setConfidenceLevel(int level) {
        confidenceLevel = level;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * getEndTime
     *
     * @return long
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * getResult
     *
     * @return Result
     */
    public Result getResult() {
        return result;
    }

    /**
     * getStartTime
     *
     * @return long
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * getText
     *
     * @return String
     */
    public String getText() {
        return token;
    }
}
