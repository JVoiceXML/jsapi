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

package javax.speech.recognition;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;

import javax.speech.Engine;
import javax.speech.EngineException;
import javax.speech.EngineStateException;

public interface Recognizer extends Engine {
    int BUFFER_MODE = IMMEDIATE_MODE << 1;

    long BUFFERING = RESUMED << 1;

    long NOT_BUFFERING = BUFFERING << 1;

    long LISTENING = NOT_BUFFERING << 1;

    long PROCESSING = LISTENING << 1;

    void addRecognizerListener(RecognizerListener listener);

    void removeRecognizerListener(RecognizerListener listener);

    void addResultListener(ResultListener listener);

    void removeResultListener(ResultListener listener);

    SpeakerManager getSpeakerManager();

    RuleGrammar createRuleGrammar(String grammarReference, String rootName)
            throws EngineStateException, EngineException;

    void deleteGrammar(Grammar grammar) throws EngineStateException;

    RecognizerProperties getRecognizerProperties();

    RuleGrammar getRuleGrammar(String grammarReference)
            throws EngineStateException;

    Grammar[] listGrammars() throws EngineStateException;

    RuleGrammar loadRuleGrammar(String grammarReference)
            throws GrammarException, IOException, EngineStateException,
            EngineException;

    RuleGrammar loadRuleGrammar(String grammarReference,
            boolean loadReferences, boolean reloadGrammars,
            Vector loadedGrammars);

    RuleGrammar loadRuleGrammar(String grammarReference, Reader reader)
            throws GrammarException, IOException, EngineStateException,
            EngineException;

    RuleGrammar loadRuleGrammar(String grammarReference, String grammarText)
            throws GrammarException, IOException, EngineStateException,
            EngineException;

    // TODO: Why use a vector and not a collection?
    RuleGrammar loadRuleGrammar(String grammarReference,
            boolean loadReferences, Vector loadedGrammars)
            throws GrammarException, IOException, EngineStateException,
            EngineException;

    void processGrammars() throws EngineStateException;

    void pause() throws EngineStateException;

    void pause(int flags) throws EngineStateException;

    void releaseFocus() throws EngineStateException;

    void requestFocus() throws EngineStateException;

    boolean resume() throws EngineStateException;

    void setResultMask(int mask);

    int getResultMask();
}
