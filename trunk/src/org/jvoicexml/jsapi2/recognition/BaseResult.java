package org.jvoicexml.jsapi2.recognition;

import javax.speech.recognition.Result;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.ResultToken;
import javax.speech.recognition.Grammar;
import java.util.Vector;
import javax.speech.recognition.FinalRuleResult;
import javax.speech.recognition.FinalResult;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.ResultException;
import javax.speech.recognition.RuleParse;
import javax.speech.AudioSegment;


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
public class BaseResult implements Result, FinalResult, FinalRuleResult {

    private Vector resultListeners;
    private final Grammar grammar;
    private int resultState;


    public BaseResult(Grammar grammar) {
        this.grammar = grammar;
        resultState = Result.UNFINALIZED;
        resultListeners = new Vector();
    }

    /**
     * addResultListener
     *
     * @param listener ResultListener
     * @todo Implement this javax.speech.recognition.Result method
     */
    public void addResultListener(ResultListener listener) {
        if (!resultListeners.contains(listener))
            resultListeners.addElement(listener);
    }

    /**
     * getBestToken
     *
     * @param tokNum int
     * @return ResultToken
     * @todo Implement this javax.speech.recognition.Result method
     */
    public ResultToken getBestToken(int tokNum) {
        return null;
    }

    /**
     * getBestTokens
     *
     * @return ResultToken[]
     * @todo Implement this javax.speech.recognition.Result method
     */
    public ResultToken[] getBestTokens() {
        return null;
    }

    /**
     * getGrammar
     *
     * @return Grammar
     * @todo Implement this javax.speech.recognition.Result method
     */
    public Grammar getGrammar() {
        return grammar;
    }

    /**
     * getNumTokens
     *
     * @return int
     * @todo Implement this javax.speech.recognition.Result method
     */
    public int getNumTokens() {
        return 0;
    }

    /**
     * getResultState
     *
     * @return int
     */
    public int getResultState() {
        return resultState;
    }

    /**
     * getUnfinalizedTokens
     *
     * @return ResultToken[]
     * @todo Implement this javax.speech.recognition.Result method
     */
    public ResultToken[] getUnfinalizedTokens() {
        return null;
    }

    /**
     * removeResultListener
     *
     * @param listener ResultListener
     */
    public void removeResultListener(ResultListener listener) {
        resultListeners.removeElement(listener);
    }

    public RuleGrammar getRuleGrammar(int nBest) throws ResultException {
        return null;
    }

    public Object[] getTags(int nBest) throws ResultException {
        return null;
    }

    public RuleParse parse(int nBest) throws ResultException {
        return null;
    }

    public ResultToken[] getAlternativeTokens(int nBest) throws ResultException {
        checkResultState(Result.UNFINALIZED);

        return null;
    }

    public AudioSegment getAudio() throws ResultException {
        checkResultState(Result.UNFINALIZED);

        return null;
    }

    public AudioSegment getAudio(ResultToken fromToken, ResultToken toToken) throws
            ResultException {
        checkResultState(Result.UNFINALIZED);

        return null;
    }

    public int getConfidenceLevel() throws ResultException {
        checkResultState(Result.UNFINALIZED);


        return 0;
    }

    public int getConfidenceLevel(int nBest) throws ResultException {
        checkResultState(Result.UNFINALIZED);

        return 0;
    }

    public int getNumberAlternatives() throws ResultException {
        checkResultState(Result.UNFINALIZED);

        return 0;
    }

    public boolean isAudioAvailable() throws ResultException {
        checkResultState(Result.UNFINALIZED);

        return false;
    }

    public boolean isTrainingInfoAvailable() throws ResultException {
        checkResultState(Result.UNFINALIZED);

        return false;
    }

    public void releaseAudio() throws ResultException {
        checkResultState(Result.UNFINALIZED);
    }

    public void releaseTrainingInfo() throws ResultException {
        checkResultState(Result.UNFINALIZED);
    }

    public void tokenCorrection(String[] correctTokens, ResultToken fromToken,
                                ResultToken toToken, int correctionType) throws
            ResultException {
        checkResultState(Result.UNFINALIZED);
    }

    /**
     * If the result is in the given state, throw a ResultException.
     */
    protected void checkResultState(int state) throws ResultException {
        if (getResultState() == state) {
            throw new ResultException("Invalid ResultState: " + getResultState());
        }
    }


}
