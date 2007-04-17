package javax.speech.recognition;

public class RuleCount extends RuleComponent {
    public static int MAX_PROBABILITY = Integer.MAX_VALUE;

    public static int REPEAT_INDEFINITELY = Integer.MAX_VALUE;

    private RuleComponent ruleComponent;

    private int repeatMin;

    private int repeatMax;

    private int repeatProbability;

    public RuleCount(RuleComponent ruleComponent, int repeatMin) {
        this.ruleComponent = ruleComponent;

        this.repeatMin = repeatMin;
    }

    public RuleCount(RuleComponent ruleComponent, int repeatMin, int repeatMax) {
        this(ruleComponent, repeatMin);

        this.repeatMax = repeatMax;
    }

    public RuleCount(RuleComponent ruleComponent, int repeatMin, int repeatMax,
            int repeatProbability) {
        this(ruleComponent, repeatMin, repeatMax);

        this.repeatProbability = repeatProbability;
    }

    public int getRepeatMax() {
        return repeatMax;
    }

    public int getRepeatMin() {
        return repeatMin;
    }

    public int getRepeatProbability() {
        return repeatProbability;
    }

    public RuleComponent getRuleComponent() {
        return ruleComponent;
    }

    public String toString() {
        return super.toString();
    }
}
