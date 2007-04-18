package javax.speech.recognition;

import javax.speech.AudioSegment;

public interface FinalResult extends Result {
    int DONT_KNOW = 0;

    int MISRECOGNITION = 1;

    int USER_CHANGE = 2;

    ResultToken[] getAlternativeTokens(int nBest);

    AudioSegment getAudio();

    AudioSegment getAudio(ResultToken fromToken, ResultToken toToken);

    int getConfidenceLevel();

    int getConfidenceLevel(int nBest);

    int getNumberAlternatives();

    boolean isAudioAvailable();

    boolean isTrainingInfoAvailable();

    void releaseAudio();

    void releaseTrainingInfo();

    void tokenCorrection(String[] correctTokens, ResultToken fromToken,
            ResultToken toToken, int correctionType);
}
