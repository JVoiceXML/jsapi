/**
 * Copyright 1998-2003 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */

/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 68 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2014 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.jsapi2.recognition;

import java.io.Serializable;
import java.security.Permission;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.speech.AudioSegment;
import javax.speech.SpeechEventExecutor;
import javax.speech.SpeechPermission;
import javax.speech.recognition.FinalResult;
import javax.speech.recognition.FinalRuleResult;
import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.RecognizerProperties;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.ResultStateException;
import javax.speech.recognition.ResultToken;
import javax.speech.recognition.RuleAlternatives;
import javax.speech.recognition.RuleComponent;
import javax.speech.recognition.RuleCount;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleParse;
import javax.speech.recognition.RuleReference;
import javax.speech.recognition.RuleSequence;
import javax.speech.recognition.RuleTag;
import javax.speech.recognition.RuleToken;

/**
 * Very simple implementation of JSAPI Result, FinalResult, FinalRuleResult, and
 * FinalDictationResult.
 * 
 * Ignores many things like N-Best, partial-results, etc.
 */
public class BaseResult
        implements Result, FinalResult, FinalRuleResult, Serializable,
        Cloneable {
    /** The serial version UID. */
    private static final long serialVersionUID = -7742652622067884474L;

    /** Logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(BaseResult.class
            .getName());

    /** Registered result listeners. */
    private Collection<ResultListener> resultListeners;
    private ResultToken tokens[];
    private int nTokens;
    /** The rule grammar that matches this result. */
    private transient Grammar grammar;
    /** Name of the rule within the grammar that matches this result. */
    private String ruleName;
    /** The result state. */
    private int state;
    /** The confidence level. */
    private int confidenceLevel;

    protected Object[] tags;

    /**
     * Create an empty result.
     */
    public BaseResult() {
        this(null);
    }

    /**
     * Create an empty result.
     * 
     * @param gram
     *            the grammar
     */
    public BaseResult(final Grammar gram) {
        resultListeners = new java.util.ArrayList<ResultListener>();
        grammar = gram;
        confidenceLevel = RecognizerProperties.UNKNOWN_CONFIDENCE;
        state = Result.UNFINALIZED;
    }

    /**
     * Create a result with a result string.
     * 
     * @param gram
     *            the grammar
     * @param result
     *            the result string
     * @exception GrammarException
     *                error evaluating the grammar
     */
    public BaseResult(final Grammar gram, final String result)
            throws GrammarException {
        resultListeners = new java.util.ArrayList<ResultListener>();
        grammar = gram;
        confidenceLevel = RecognizerProperties.UNKNOWN_CONFIDENCE;
        setResult(result);
    }

    /**
     * Simple method to set the result as a string.
     * 
     * @param result
     *            the result
     * @throws GrammarException
     *             error parsing the result against the grammar
     */
    public void setResult(final String result) throws GrammarException {
        if (result == null) {
            state = Result.UNFINALIZED;
        } else {
            state = Result.REJECTED;
            final boolean success = tryTokens(grammar, result);
            if (success) {
                state = Result.ACCEPTED;
            }
        }
    }

    /**
     * Copy a result. If the result to be copied is a BaseResult then clone it
     * otherwise create a BaseResult and copy the tokens onto it.
     * 
     * @param result
     *            the result to copy to
     * @return copied result
     * @exception GrammarException
     *                if the related grammar could not be evaluated
     */
    static BaseResult copyResult(final Result result) throws GrammarException {
        BaseResult copy = null;
        if (result instanceof BaseResult) {
            try {
                copy = (BaseResult) ((BaseResult) result).clone();
            } catch (CloneNotSupportedException e) {
                LOGGER.warning("ERROR: " + e);
            }
            return copy;
        } else {
            copy = new BaseResult(result.getGrammar());
            copy.nTokens = result.getNumTokens();
            copy.tokens = new ResultToken[copy.nTokens];
            for (int i = 0; i < result.getNumTokens(); i++) {
                ResultToken sourceToken = result.getBestToken(i);
                BaseResultToken destinationToken = new BaseResultToken(copy,
                        sourceToken.getText());

                destinationToken.setConfidenceLevel(sourceToken
                        .getConfidenceLevel());
                destinationToken.setStartTime(sourceToken.getStartTime());
                destinationToken.setEndTime(sourceToken.getEndTime());

                copy.tokens[i] = destinationToken;
            }
            return copy;
        }
    }

    // ////////////////////
    // Begin Result Methods
    // ////////////////////
    /**
     * Return the current state of the Result object. From
     * javax.speech.recognition.Result.
     */
    public int getResultState() {
        return state;
    }

    /**
     * Return the grammar that goes with this Result. From
     * javax.speech.recognition.Result.
     */
    public Grammar getGrammar() {
        return grammar;
    }

    /**
     * Return the number of finalized tokens in the Result. From
     * javax.speech.recognition.Result.
     */
    public int getNumTokens() {
        return nTokens;
    }

    /**
     * Return the best guess for the nth token. From
     * javax.speech.recognition.Result.
     */
    public ResultToken getBestToken(int nth) throws IllegalArgumentException {
        if ((nth < 0) || (nth > (nTokens - 1))) {
            throw new IllegalArgumentException("Token index out of range.");
        }
        return tokens[nth];
    }

    /**
     * Return the best guess tokens for the Result. From
     * javax.speech.recognition.Result.
     */
    public ResultToken[] getBestTokens() {
        return tokens;
    }

    /**
     * Return the current guess of the tokens following the unfinalized tokens.
     * From javax.speech.recognition.Result.
     */
    public ResultToken[] getUnfinalizedTokens() {
        if (getResultState() == Result.ACCEPTED
                || getResultState() == Result.REJECTED)
            return new ResultToken[0];

        int numUnfinalizedTokens = getBestTokens().length - getNumTokens();

        ResultToken[] unfinalizedTokens = new ResultToken[numUnfinalizedTokens];

        for (int i = 0; i < numUnfinalizedTokens; ++i)
            unfinalizedTokens[i] = getBestTokens()[i + getNumTokens()];

        return unfinalizedTokens;
    }

    /**
     * Add a ResultListener to this Result. From
     * javax.speech.recognition.Result.
     */
    public void addResultListener(ResultListener listener) {
        if (!resultListeners.contains(listener)) {
            resultListeners.add(listener);
        }
    }

    /**
     * Remove a ResultListener from this Result. From
     * javax.speech.recognition.Result.
     */
    public void removeResultListener(ResultListener listener) {
        resultListeners.remove(listener);
    }

    // ////////////////////
    // End Result Methods
    // ////////////////////

    // ////////////////////
    // Begin FinalResult Methods
    // ////////////////////
    /**
     * Returns true if the Recognizer has training information available for
     * this result. From javax.speech.recognition.FinalResult.
     */
    public boolean isTrainingInfoAvailable() throws ResultStateException {
        checkResultState(UNFINALIZED);
        return false;
    }

    /**
     * Release training info for this FinalResult. From
     * javax.speech.recognition.FinalResult.
     */
    public void releaseTrainingInfo() throws ResultStateException {
        checkResultState(UNFINALIZED);
    }

    /**
     * Inform the recognizer of a correction to one or more tokens in a
     * FinalResult to the recognizer can re-train itself. From
     * javax.speech.recognition.FinalResult.
     */
    public void tokenCorrection(String correctTokens[], ResultToken fromToken,
            ResultToken toToken, int correctionType)
            throws ResultStateException, IllegalArgumentException {
        final SecurityManager security = System.getSecurityManager();
        if (security != null) {
            final Permission permission = new SpeechPermission(
                    "javax.speech.recognition.FinalResult.tokenCorrection");
            security.checkPermission(permission);
        }
        checkResultState(UNFINALIZED);
    }

    /**
     * Determine if audio is available for this FinalResult. From
     * javax.speech.recognition.FinalResult.
     */
    public boolean isAudioAvailable() throws ResultStateException {
        checkResultState(UNFINALIZED);
        return false;
    }

    /**
     * Release the audio for this FinalResult. From
     * javax.speech.recognition.FinalResult.
     */
    public void releaseAudio() throws ResultStateException {
        checkResultState(UNFINALIZED);
    }

    /**
     * Get the audio for this FinalResult. From
     * javax.speech.recognition.FinalResult.
     */
    public AudioSegment getAudio() throws ResultStateException {
        checkResultState(UNFINALIZED);
        return null;
    }

    /**
     * Get the audio for this FinalResult. From
     * javax.speech.recognition.FinalResult.
     */
    public AudioSegment getAudio(ResultToken from, ResultToken to)
            throws ResultStateException {
        checkResultState(UNFINALIZED);
        return null;
    }

    // ////////////////////
    // End FinalResult Methods
    // ////////////////////

    // ////////////////////
    // Begin FinalRuleResult Methods
    // ////////////////////
    /**
     * Return the number of guesses for this FinalRuleResult. From
     * javax.speech.recognition.FinalRuleResult.
     */
    public int getNumberAlternatives() throws ResultStateException {
        checkResultState(UNFINALIZED);
        return 1;
    }

    /**
     * Get the nBest token sequence for this FinalRuleResult. From
     * javax.speech.recognition.FinalRuleResult.
     */
    public ResultToken[] getAlternativeTokens(int nBest)
            throws ResultStateException {
        checkResultState(UNFINALIZED);
        if (!(grammar instanceof RuleGrammar)) {
            throw new ResultStateException("Result is not a FinalRuleResult");
        }
        if (nBest == 0) {
            return getBestTokens();
        }
        // [[[WDW - throw InvalidArgumentException?]]]
        return null;
    }

    /**
     * Get the RuleGrammar matched by the nBest guess for this FinalRuleResult.
     * From javax.speech.recognition.FinalRuleResult.
     */
    public Grammar getGrammar(int nBest) throws ResultStateException {
        checkResultState(UNFINALIZED);
        if (!(grammar instanceof RuleGrammar)) {
            throw new ResultStateException("Result is not a FinalRuleResult");
        }
        if (nBest == 0) {
            return (RuleGrammar) grammar;
        }
        // [[[WDW - throw InvalidArgumentException?]]]
        return null;
    }

    /**
     * Get the RuleGrammar matched by the nBest guess for this FinalRuleResult.
     * From javax.speech.recognition.FinalRuleResult.
     */
    public String getRuleName(int nBest) throws ResultStateException {
        checkResultState(UNFINALIZED);
        if (!(grammar instanceof RuleGrammar)) {
            throw new ResultStateException("Result is not a FinalRuleResult");
        }
        if (nBest == 0) {
            return ruleName;
        }
        // [[[WDW - throw InvalidArgumentException?]]]
        return null;
    }

    /**
     * Return the list of tags matched by the best-guess token sequence. From
     * javax.speech.recognition.FinalRuleResult.
     */
    public Object[] getTags(int nBest) throws IllegalArgumentException,
            ResultStateException {
        checkResultState(UNFINALIZED);
        if (!(grammar instanceof RuleGrammar)) {
            throw new ResultStateException("Result is not a FinalRuleResult");
        }
        return tags;
    }

    public RuleReference getRuleReference(int nBest)
            throws ResultStateException, IllegalArgumentException,
            IllegalStateException {
        throw new RuntimeException(
                "BaseResult.getRuleReference NOT IMPLEMENTED");
        // return null;
    }

    // ////////////////////
    // End FinalRuleResult Methods
    // ////////////////////

    /**
     * Utility function to generate result event and post it to the event queue.
     * Eventually fireAudioReleased will be called by dispatchSpeechEvent as a
     * result of this action.
     * 
     * @param speechEventExecutor
     *            SpeechEventExecutor
     * @param event
     *            ResultEvent
     */
    public void postResultEvent(final SpeechEventExecutor speechEventExecutor,
            final ResultEvent event) {
        try {
            speechEventExecutor.execute(new Runnable() {
                public void run() {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("notifying event " + event);
                    }
                    fireResultEvent(event);
                }
            });
        } catch (RuntimeException ex) {
            LOGGER.warning(ex.getLocalizedMessage());
        }
    }

    /**
     * Utility function to send a result event to all result listeners.
     */
    public void fireResultEvent(final ResultEvent event) {
        if (resultListeners != null) {
            for (ResultListener listener : resultListeners) {
                listener.resultUpdate(event);
            }
        }
    }

    /**
     * Utility function to set the number of finalized tokens in the Result.
     * 
     * @param n
     *            int
     */
    public void setNumTokens(int n) {
        nTokens = n;
    }

    /**
     * Simple implementation of tags.
     * 
     * ATTENTION: This method changes the ResultToken content.
     * <p>
     * This implementation replaces each token text by tag information, if it
     * exists.
     * </p>
     * 
     * @todo the tag information can not be only text! It can be anything.
     * 
     * @param rt
     *            the result tokens
     * @param rc
     *            the rule component that will be analyzed
     * @param iPos
     *            the position in rt of next token that will be appear in the
     *            graph
     * @return int
     */
    private int applyTags(ResultToken rt[], final RuleComponent rc, int iPos) {
        if (rc instanceof RuleReference) {
            return iPos;
        } else if (rc instanceof RuleToken) {
            return iPos + 1;
        } else if (rc instanceof RuleAlternatives) {
            return applyTags(rt,
                    ((RuleAlternatives) rc).getRuleComponents()[0], iPos);
        } else if (rc instanceof RuleSequence) {
            for (RuleComponent r : ((RuleSequence) rc).getRuleComponents()) {
                iPos = applyTags(rt, r, iPos);
            }
            return iPos;
        } else if (rc instanceof RuleTag) {
            String tag = (String) ((RuleTag) rc).getTag();

            // assumes that ruleTag component appears after RuleToken component
            rt[iPos - 1] = new BaseResultToken(rt[iPos - 1].getResult(), tag);
            return iPos;
        } else if (rc instanceof RuleCount) {
            return applyTags(rt, ((RuleCount) rc).getRuleComponent(), iPos);
        }

        return iPos;
    }

    /**
     * Utility function to set the resultTokens.
     * 
     * @param rt
     *            the tokens
     */
    public void setTokens(final ResultToken rt[]) {
        setTokens(rt, false);
    }

    /**
     * Utility function to set the resultTokens. Does nothing if no tokens are 
     * provided.
     * 
     * @param rt
     *            the tokens
     * @param replaceTags
     *            if true, tokens must be replaced by tags content.
     */
    public void setTokens(final ResultToken rt[], final boolean replaceTags) {
        if (rt == null) {
            return;
        }
        System.arraycopy(rt, 0, tokens, 0, rt.length);
        if (replaceTags) {
            final RuleParse ruleParse = parse(0);
            if (ruleParse != null) {
                applyTags(tokens, ruleParse.getParse(), 0);
            }
        }
    }

    /**
     * Concatenate the best tokens in the Result.
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        if (nTokens == 0) {
            /** @todo change this. Is it possible a result has 0 tokens?? */
            return super.toString();
        }

        final StringBuilder sb = new StringBuilder(getBestToken(0).getText());
        for (int i = 1; i < getNumTokens(); i++) {
            sb.append(" " + getBestToken(i).getText());
        }
        return sb.toString();
    }

    /**
     * Utility function to set the result state.
     */
    public void setResultState(int state) {
        this.state = state;
    }

    /**
     * If the result is in the given state, throw a ResultStateError.
     */
    protected void checkResultState(int state) throws ResultStateException {
        if (getResultState() == state) {
            throw new ResultStateException("Invalid ResultState: "
                    + getResultState());
        }
    }

    /**
     * Set the grammar that goes with this Result. NOT JSAPI.
     * 
     * @param gram
     *            the grammar
     */
    public void setGrammar(final Grammar gram) {
        grammar = gram;
    }

    /**
     * Try to set the Grammar and tokens of this result.
     * 
     * @param gram
     *            the related grammar
     * @param result
     *            the retrieved recognition result
     * @return {@code true} if the grammar matches the tokens.
     */
    public boolean tryTokens(final Grammar gram, final String result)
            throws GrammarException {
        if ((result == null) || (gram == null)) {
            return false;
        }

        if (gram instanceof RuleGrammar) {
            final RuleGrammar rule = (RuleGrammar) gram;
            return tryTokens(rule, result);
        }
        return false;
    }

    /**
     * Try to set the Grammar and tokens of this result.
     * 
     * @param gram
     *            the related grammar
     * @param result
     *            the retrieved recognition result
     * @return {@code true} if the grammar matches the tokens.
     */
    private boolean tryTokens(final RuleGrammar ruleGrammar, final String result)
            throws GrammarException {
        final RuleParse parse = ruleGrammar.parse(result, null);
        if (parse == null) {
            return false;
        }

        // Adapt tags and rule names
        tags = parse.getTags();
        ruleName = parse.getRuleReference().getRuleName();

        // Copy the result tokens
        final StringTokenizer tokenizer = new StringTokenizer(result);
        nTokens = tokenizer.countTokens();
        int i = 0;
        tokens = new ResultToken[nTokens];
        while (tokenizer.hasMoreTokens()) {
            // TODO information about startTime, endTime and
            // confidenceLevel
            tokens[i] = new BaseResultToken(this, tokenizer.nextToken());
            ++i;
        }
        return true;
    }

    public RuleParse parse(int nBest) throws IllegalArgumentException,
            ResultStateException {
        final ResultToken[] rt = getAlternativeTokens(0);
        final String tokens[] = new String[rt.length];
        for (int i = 0; i < rt.length; ++i) {
            tokens[i] = rt[i].getText();
        }

        try {
            return ((RuleGrammar) getGrammar()).parse(tokens,
                    ((BaseRuleGrammar) getGrammar()).getRoot());
        } catch (GrammarException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setConfidenceLevel(int confidenceLevel) {
        if (confidenceLevel < RecognizerProperties.MIN_CONFIDENCE
                || confidenceLevel > RecognizerProperties.MAX_CONFIDENCE)
            throw new IllegalArgumentException("Invalid confidenceThreshold: "
                    + confidenceLevel);
        this.confidenceLevel = confidenceLevel;
    }

    public int getConfidenceLevel() throws ResultStateException {
        return getConfidenceLevel(0);
    }

    public int getConfidenceLevel(int nBest) throws IllegalArgumentException,
            ResultStateException {
        // uncommented - see JSAPI2/FinalResult#getConfidenceLevel
        // quote: "For a REJECTED result, a useful confidence level
        // MAY be returned, but this is application
        // and platform dependent."
        if (state != FinalResult.ACCEPTED && state != FinalResult.REJECTED
                && state != FinalResult.DONT_KNOW
                && state != FinalResult.MISRECOGNITION
                && state != FinalResult.USER_CHANGE)
            // above code could simply check for FinalResult.UNFINALIZED
            // but this would be more error prone
            // (e.g. if the ResultState was not correctly set to UNFINALIZED)
            throw new ResultStateException();

        if (nBest < 0 || nBest >= getNumberAlternatives()) {
            throw new IllegalArgumentException("nBest out of valid range!");
        }

        return confidenceLevel;
    }
}
