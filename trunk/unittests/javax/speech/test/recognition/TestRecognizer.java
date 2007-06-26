/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package javax.speech.test.recognition;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;

import javax.speech.AudioManager;
import javax.speech.EngineException;
import javax.speech.EngineMode;
import javax.speech.EngineStateException;
import javax.speech.VocabularyManager;
import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.RecognizerListener;
import javax.speech.recognition.RecognizerProperties;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.SpeakerManager;

/**
 * Recognizer for test purposes.
 * 
 * @author Dirk Schnelle
 */
public class TestRecognizer implements Recognizer {

    /**
     * {@inheritDoc}
     */
    public void addRecognizerListener(RecognizerListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public void addResultListener(ResultListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public RuleGrammar createRuleGrammar(String grammarReference,
            String rootName) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void deleteGrammar(Grammar grammar) {
    }

    /**
     * {@inheritDoc}
     */
    public RecognizerProperties getRecognizerProperties() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public RuleGrammar getRuleGrammar(String grammarReference) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public SpeakerManager getSpeakerManager() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Grammar[] listGrammars() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public RuleGrammar loadRuleGrammar(String grammarReference) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public RuleGrammar loadRuleGrammar(String grammarReference,
            boolean loadReferences, boolean reloadGrammars,
            Vector loadedGrammars) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public RuleGrammar loadRuleGrammar(String grammarReference, Reader reader) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public RuleGrammar loadRuleGrammar(String grammarReference,
            String grammarText) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void pause() {
    }

    /**
     * {@inheritDoc}
     */
    public void pause(int flags) {
    }

    /**
     * {@inheritDoc}
     */
    public void processGrammars() {
    }

    /**
     * {@inheritDoc}
     */
    public void releaseFocus() {
    }

    /**
     * {@inheritDoc}
     */
    public void removeRecognizerListener(RecognizerListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public void removeResultListener(ResultListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public void requestFocus() {
    }

    /**
     * {@inheritDoc}
     */
    public boolean resume() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void allocate() {
    }

    /**
     * {@inheritDoc}
     */
    public void allocate(int mode) {
    }

    /**
     * {@inheritDoc}
     */
    public void deallocate() {
    }

    /**
     * {@inheritDoc}
     */
    public void deallocate(int mode) {
    }

    /**
     * {@inheritDoc}
     */
    public AudioManager getAudioManager() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int getEngineMask() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public EngineMode getEngineMode() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public long getEngineState() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public VocabularyManager getVocabularyManager() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setEngineMask(int mask) {
    }

    /**
     * {@inheritDoc}
     */
    public boolean testEngineState(long state) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void waitEngineState(long state) {
    }

    /**
     * {@inheritDoc}
     */
    public int getResultMask() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public RuleGrammar loadRuleGrammar(String grammarReference,
            boolean loadReferences, Vector loadedGrammars)
            throws GrammarException, IOException, EngineStateException,
            EngineException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setResultMask(int mask) {
    }
}
