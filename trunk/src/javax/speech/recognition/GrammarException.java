package javax.speech.recognition;

import javax.speech.SpeechException;

public class GrammarException extends SpeechException {

    private GrammarExceptionDetail[] details;

    public GrammarException() {
    }

    public GrammarException(String s) {
        super(s);
    }

    public GrammarException(String message, GrammarExceptionDetail[] details) {
        this.details = details;
    }

    public GrammarExceptionDetail[] getDetails() {
        return details;
    }
}
