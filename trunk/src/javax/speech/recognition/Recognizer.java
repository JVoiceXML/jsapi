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
