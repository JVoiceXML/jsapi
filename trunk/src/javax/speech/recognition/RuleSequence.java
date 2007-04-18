package javax.speech.recognition;

public class RuleSequence extends RuleComponent {
    private RuleComponent[] ruleComponents;
    
    public RuleSequence(RuleComponent[] ruleComponents) {
        this.ruleComponents = ruleComponents;
    }
    
    public RuleSequence(String[] tokens) {
    }
    
    public RuleComponent[] getRuleComponents() {
        return ruleComponents;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
    
    
}
