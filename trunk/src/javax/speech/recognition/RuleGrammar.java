package javax.speech.recognition;

public interface RuleGrammar extends Grammar {
    void addElement(String element);

    void addRule(Rule rule);

    void addRule(String ruleText);

    void addRules(Rule[] rules);

    String getAttribute(String attribute);

    String getDoctype();

    String[] getElements();

    String getRoot();

    Rule getRule(String ruleName);

    boolean isEnabled();

    boolean isEnabled(String ruleName);

    String[] listRuleNames();

    RuleParse parse(String[] tokens, String ruleName);

    RuleParse parse(String text, String ruleName);

    void removeElement(String element);

    void removeRule(String ruleName);

    RuleReference resolve(RuleReference ruleReference);

    void setAttribute(String attribute, String value);

    void setDoctype(String doctype);

    void setEnabled(boolean enabled);

    void setEnabled(String[] ruleNames, boolean enabled);

    void setEnabled(String ruleName, boolean enabled);

    void setRoot(String rootName);

    String toString();
}
