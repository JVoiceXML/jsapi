package javax.speech.recognition;

public interface FinalRuleResult extends FinalResult {
    RuleGrammar getRuleGrammar(int nBest);

    String[] getTags(int nBest);

    RuleParse parse(int nBest);
}
