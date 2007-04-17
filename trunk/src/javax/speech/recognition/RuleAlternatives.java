package javax.speech.recognition;

public class RuleAlternatives extends RuleComponent {
    public static int MAX_WEIGHT = Integer.MAX_VALUE;

    public static int MIN_WEIGHT = 0;

    public static int NORM_WEIGHT = MAX_WEIGHT / 2;

    private RuleComponent[] ruleComponents;

    private int[] weights;

    public RuleAlternatives(RuleComponent[] ruleComponents) {
        this.ruleComponents = ruleComponents;
    }

    public RuleAlternatives(RuleComponent[] ruleComponents, int[] weights) {
        this.ruleComponents = ruleComponents;
        this.weights = weights;
    }

    public RuleAlternatives(String[] tokens) {

    }

    public RuleComponent[] getRuleComponents() {
        return ruleComponents;
    }

    public int[] getWeights() {
        return weights;
    }

    public String toString() {
        return super.toString();
    }
}
