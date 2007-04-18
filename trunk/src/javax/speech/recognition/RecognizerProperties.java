package javax.speech.recognition;

import javax.speech.EngineProperties;

public interface RecognizerProperties extends EngineProperties {
    int ADAPT_PAUSED = 0;

    int ADAPT_RESUMED = 1;

    int ENDPOINT_AUTOMATIC = 2;

    int ENDPOINT_MANUAL = 3;

    int ENDPOINT_PUSH_TO_START = 4;

    int ENDPOINT_PUSH_TO_TALK = 5;

    int ENDPOINT_SPEECH_DETECTION = 6;

    int MAX_ACCURACY = 7;

    int MAX_CONFIDENCE = 8;

    int MAX_SENSITIVITY = 9;

    int MIN_ACCURACY = 10;

    int MIN_CONFIDENCE = 11;

    int MIN_SENSITIVITY = 12;

    int NORM_ACCURACY = 13;

    int NORM_CONFIDENCE = 14;

    int NORM_SENSITIVITY = 15;

    int UNKNOWN_CONFIDENCE = 16;

    int getAdaptation();

    int getCompleteTimeout();

    int getConfidenceThreshold();

    int getEndpointStyle();

    int getIncompleteTimeout();

    int getNumResultAlternatives();

    int getPriority();

    int getSensitivity();

    int getSpeedVsAccuracy();

    boolean isResultAudioProvided();

    boolean isTrainingProvided();

    void setAdaptation(int adapt);

    void setCompleteTimeout(int timeout);

    void setConfidenceThreshold(int confidenceThreshold);

    void setEndpointStyle(int endpointStyle);

    void setIncompleteTimeout(int timeout);

    void setNumResultAlternatives(int num);

    void setPriority(int priority);

    void setResultAudioProvided(boolean audioProvided);

    void setSensitivity(int sensitivity);

    void setSpeedVsAccuracy(int speedVsAccuracy);

    void setTrainingProvided(boolean trainingProvided);
}
