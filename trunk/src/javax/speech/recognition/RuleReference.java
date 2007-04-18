package javax.speech.recognition;

public class RuleReference extends RuleComponent {
    private String grammarReference;

    private String ruleName;

    private String mediaType;

    public RuleReference(String ruleName) {
        this.ruleName = ruleName;
    }

    public RuleReference(String grammarReference, String ruleName) {
        this.grammarReference = grammarReference;
        this.ruleName = ruleName;
    }

    public RuleReference(String grammarReference, String ruleName,
            String mediaType) {
        this.grammarReference = grammarReference;
        this.ruleName = ruleName;
        this.mediaType = mediaType;
    }

    
    public String getGrammarReference() {
        return grammarReference;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getRuleName() {
        return ruleName;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
    
    
}
