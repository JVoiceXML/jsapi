package javax.speech.recognition;

public class RuleParse extends RuleComponent {
    private RuleReference ruleReference;

    private RuleComponent parse;

    public RuleParse(RuleReference ruleReference, RuleComponent parse) {
        this.ruleReference = ruleReference;
        this.parse = parse;
    }

    public String[] getTags() {
        return null;
    }

    public RuleComponent getParse() {
        return parse;
    }

    public RuleReference getRuleReference() {
        return ruleReference;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

}
