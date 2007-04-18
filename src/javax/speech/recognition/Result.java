package javax.speech.recognition;

public interface Result {
    int ACCEPTED = 0;

    int REJECTED = 1;

    int UNFINALIZED = 2;

    void addResultListener(ResultListener listener);

    ResultToken getBestToken(int tokNum);

    ResultToken[] getBestTokens();

    Grammar getGrammar();

    int getNumTokens();

    int getResultState();

    ResultToken[] getUnfinalizedTokens();

    void removeResultListener(ResultListener listener);
}
