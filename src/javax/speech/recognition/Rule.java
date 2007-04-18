package javax.speech.recognition;

public class Rule {
    public static int PRIVATE_SCOPE = 0;

    public static int PUBLIC_SCOPE = 1;

    private String ruleName;

    private RuleComponent ruleComponent;

    private int scope;
    
    public Rule(String ruleName, RuleComponent ruleComponent) {
        this.ruleName = ruleName;
        this.ruleComponent = ruleComponent;
    }
    
    public Rule(String ruleName, RuleComponent ruleComponent, int scope) {
        this.ruleName = ruleName;
        this.ruleComponent = ruleComponent;
        this.scope = scope;
    }

    public RuleComponent getRuleComponent() {
        return ruleComponent;
    }

    public String getRuleName() {
        return ruleName;
    }

    public int getScope() {
        return scope;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
}
