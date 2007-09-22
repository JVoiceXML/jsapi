package org.jvoicexml.jsapi2.recognition;

import javax.speech.recognition.*;

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
    private long endTime;
    private final Result result;


    public BaseResultToken(Result result) {
        this.result = result;
        confidenceLevel = RecognizerProperties.NORM_CONFIDENCE;
    }

    /**
     * getConfidenceLevel
     *
     * @return int
     */
    public int getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(int level) {
        confidenceLevel = level;
    }

    /**
     * getEndTime
     *
     * @return long
     * @todo Implement this javax.speech.recognition.ResultToken method
     */
    public long getEndTime() {
        return 0L;
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
     * @todo Implement this javax.speech.recognition.ResultToken method
     */
    public long getStartTime() {
        return 0L;
    }

    /**
     * getText
     *
     * @return String
     * @todo Implement this javax.speech.recognition.ResultToken method
     */
    public String getText() {
        return "";
    }
}
