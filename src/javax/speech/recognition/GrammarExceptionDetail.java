package javax.speech.recognition;

public class GrammarExceptionDetail {
    private String grammarReference;

    private String ruleName;

    private int lineNumber;

    private int charNumber;

    private String message;

    public GrammarExceptionDetail(String grammarReference, String ruleName,
            int lineNumber, int charNumber, String message) {
        this.grammarReference = grammarReference;
        this.ruleName = ruleName;
        this.lineNumber = lineNumber;
        this.charNumber = charNumber;
        this.message = message;
    }

    public int getCharNumber() {
        return charNumber;
    }

    public String getGrammarReference() {
        return grammarReference;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getRuleName() {
        return ruleName;
    }
}
