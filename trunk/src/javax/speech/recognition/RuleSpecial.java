package javax.speech.recognition;

public class RuleSpecial extends RuleComponent {
    public static RuleSpecial GARBAGE = new RuleSpecial("GARBAGE");

    public static RuleSpecial NULL = new RuleSpecial("NULL");

    public static RuleSpecial VOID = new RuleSpecial("VOID");

    private String special;

    private RuleSpecial(String special) {
        this.special = special;
    }

    @Override
    public String toString() {
        return "<ruleref special=\"" + special + "\"/>";
    }
}
