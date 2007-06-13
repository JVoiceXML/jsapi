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

import java.io.Reader;
import java.util.Vector;

import javax.speech.Engine;

public interface Recognizer extends Engine {
    int BUFFER_MODE = 0;

    long BUFFERING = 1;

    long LISTENING = 2;

    long NOT_BUFFERING = 3;

    long PROCESSING = 4;

    void addRecognizerListener(RecognizerListener listener);

    void addResultListener(ResultListener listener);

    RuleGrammar createRuleGrammar(String grammarReference, String rootName);

    void deleteGrammar(Grammar grammar);

    RecognizerProperties getRecognizerProperties();

    RuleGrammar getRuleGrammar(String grammarReference);

    SpeakerManager getSpeakerManager();

    Grammar[] listGrammars();

    RuleGrammar loadRuleGrammar(String grammarReference);

    RuleGrammar loadRuleGrammar(String grammarReference,
            boolean loadReferences, boolean reloadGrammars,
            Vector loadedGrammars);

    RuleGrammar loadRuleGrammar(String grammarReference, Reader reader);

    RuleGrammar loadRuleGrammar(String grammarReference, String grammarText);

    void pause();

    void pause(int flags);

    void processGrammars();

    void releaseFocus();

    void removeRecognizerListener(RecognizerListener listener);

    void removeResultListener(ResultListener listener);

    void requestFocus();

    boolean resume();
}
