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

import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.EngineException;
import javax.speech.EngineMode;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.VocabularyManager;
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.RecognizerListener;
import javax.speech.recognition.RecognizerProperties;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.SpeakerManager;

/**
 * Recognizer for test purposes.
 *
 * @author Dirk Schnelle-Walka
 */
public class TestRecognizer implements Recognizer {

    public void allocate() throws AudioException, EngineException,
            EngineStateException, SecurityException {
        // TODO Auto-generated method stub
        
    }

    public void allocate(int mode) throws IllegalArgumentException,
            AudioException, EngineException, EngineStateException,
            SecurityException {
        // TODO Auto-generated method stub
        
    }

    public void deallocate() throws AudioException, EngineException,
            EngineStateException {
        // TODO Auto-generated method stub
        
    }

    public void deallocate(int mode) throws IllegalArgumentException,
            AudioException, EngineException, EngineStateException {
        // TODO Auto-generated method stub
        
    }

    public void pause() throws EngineStateException {
        // TODO Auto-generated method stub
        
    }

    public boolean testEngineState(long state) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return false;
    }

    public long waitEngineState(long state) throws InterruptedException,
            IllegalArgumentException, IllegalStateException {
        // TODO Auto-generated method stub
        return 0;
    }

    public long waitEngineState(long state, long timeout)
            throws InterruptedException, IllegalArgumentException,
            IllegalStateException {
        // TODO Auto-generated method stub
        return 0;
    }

    public AudioManager getAudioManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public EngineMode getEngineMode() {
        // TODO Auto-generated method stub
        return null;
    }

    public long getEngineState() {
        // TODO Auto-generated method stub
        return 0;
    }

    public VocabularyManager getVocabularyManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setEngineMask(int mask) {
        // TODO Auto-generated method stub
        
    }

    public int getEngineMask() {
        // TODO Auto-generated method stub
        return 0;
    }

    public SpeechEventExecutor getSpeechEventExecutor() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setSpeechEventExecutor(SpeechEventExecutor speechEventExecutor) {
        // TODO Auto-generated method stub
        
    }

    public void addRecognizerListener(RecognizerListener listener) {
        // TODO Auto-generated method stub
        
    }

    public void removeRecognizerListener(RecognizerListener listener) {
        // TODO Auto-generated method stub
        
    }

    public void addResultListener(ResultListener listener) {
        // TODO Auto-generated method stub
        
    }

    public void removeResultListener(ResultListener listener) {
        // TODO Auto-generated method stub
        
    }

    public SpeakerManager getSpeakerManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public RecognizerProperties getRecognizerProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    public GrammarManager getGrammarManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public void processGrammars() throws EngineStateException {
        // TODO Auto-generated method stub
        
    }

    public void pause(int flags) throws IllegalArgumentException,
            EngineStateException {
        // TODO Auto-generated method stub
        
    }

    public void releaseFocus() throws EngineStateException {
        // TODO Auto-generated method stub
        
    }

    public void requestFocus() throws EngineStateException {
        // TODO Auto-generated method stub
        
    }

    public boolean resume() throws EngineStateException {
        // TODO Auto-generated method stub
        return false;
    }

    public void setResultMask(int mask) {
        // TODO Auto-generated method stub
        
    }

    public int getResultMask() {
        // TODO Auto-generated method stub
        return 0;
    }
}
