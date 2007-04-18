package javax.speech.recognition;

public class RuleTag extends RuleComponent {
    private String tag;
    
    public RuleTag(String tag) {
        this.tag = tag;
    }
    
    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
