package javax.speech.recognition;

public interface ResultToken {
    int getConfidenceLevel();

    long getEndTime();

    Result getResult();

    long getStartTime();

    String getText();
}
