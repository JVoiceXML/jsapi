/**
 * Copyright 1998-2003 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */

package org.jvoicexml.jsapi2.jse.recognition;

import java.util.StringTokenizer;
import java.util.Vector;

import javax.speech.EngineStateException;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.Rule;
import javax.speech.recognition.RuleAlternatives;
import javax.speech.recognition.RuleComponent;
import javax.speech.recognition.RuleCount;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleReference;
import javax.speech.recognition.RuleParse;
import javax.speech.recognition.RuleSequence;
import javax.speech.recognition.RuleTag;
import javax.speech.recognition.RuleToken;

/**
 * Implementation of the parse method(s) on
 * javax.speech.recognition.RuleGrammar.
 *
 * @version 1.5 10/27/99 16:33:49
 */
public class RuleParser {

    private Recognizer theRec;

    private int iPos;

    /*
     * parse a text string against a particular rule from a particluar grammar
     * returning a RuleParse data structure is successful and null otherwise
     */
    public static RuleParse parse(String text, Recognizer recognizer,
                                  RuleGrammar grammar, String ruleName) {
        String inputTokens[] = tokenize(text);
        return parse(inputTokens, recognizer, grammar, ruleName);
    }

    public static RuleParse parse(String inputTokens[], Recognizer recognizer,
                                  RuleGrammar grammar, String ruleName) {
        RuleParse rpa[] = mparse(inputTokens, recognizer, grammar, ruleName);
        if (rpa == null) {
            return null;
        } else {
            return rpa[0];
        }
    }

    public static RuleParse[] mparse(String text, Recognizer recognizer,
                                     RuleGrammar grammar, String ruleName) {
        String inputTokens[] = tokenize(text);
        return mparse(inputTokens, recognizer, grammar, ruleName);
    }

    public static RuleParse[] mparse(String inputTokens[],
                                     Recognizer recognizer, RuleGrammar grammar,
                                     String ruleName) {
        RuleParser rp = new RuleParser();
        rp.iPos = 0;
        rp.theRec = recognizer;
        String rNames[];
        RuleComponent startRule = null;
        if (ruleName != null) {
            rNames = new String[1];
            rNames[0] = ruleName;
        } else {
            rNames = grammar.listRuleNames();
        }
        Vector p = null;
        Vector<RuleParse> t = new Vector<RuleParse>();
        for (int j = 0; j < rNames.length; j++) {
            if ((ruleName == null) && !(grammar.isActivatable(rNames[j]))) {
                continue;
            }
            startRule = grammar.getRule(rNames[j]).getRuleComponent();
            if (startRule == null) {
                System.out.println("BAD RULENAME " + rNames[j]);
                continue;
            }
            p = new Vector();
            RuleComponent ruleComponent = rp.parse(grammar, startRule,
                    inputTokens);
            if (ruleComponent != null) {
                RuleParse ruleParse = new RuleParse(new RuleReference(rNames[j]),
                        ruleComponent);
                p.add(ruleParse);
            }
        }
        if (p.size() == 0) {
            //No parse is available
            return null;
        }
        RuleParse rpa[] = new RuleParse[p.size()];
        p.copyInto(rpa);
        return rpa;
    }

    /*
     * parse routine called recursively while traversing the Rule structure
     * in a depth first manner. Returns a list of valid parses.
     */
    private RuleComponent parse(RuleGrammar G, RuleComponent r, String input[]) {
        if (r instanceof RuleReference) {
            return parse(G, (RuleReference) r, input);
        } else if (r instanceof RuleToken) {
            return parse(G, (RuleToken) r, input);
        } else if (r instanceof RuleAlternatives) {
            return parse(G, (RuleAlternatives) r, input);
        } else if (r instanceof RuleSequence) {
            return parse(G, (RuleSequence) r, input);
        } else if (r instanceof RuleTag) {
            return parse(G, (RuleTag) r, input);
        } else if (r instanceof RuleCount) {
            return parse(G, (RuleCount) r, input);
        } else {
            return null;
        }
    }

    /**
     * RULE REFERENCES
     * @todo void, null and garbage ??
     * @todo not tested with external rules (rules defined in other files)
     */
    private RuleComponent parse(RuleGrammar G, RuleReference r, String input[]) {
        String simpleName = r.getRuleName();
        RuleComponent ruleref;
        if (G.getRule(simpleName) == null) {
            ruleref = null;
        } else {
            ruleref = G.getRule(simpleName).getRuleComponent();
        }
        if (ruleref == null) {
            String gname = r.getGrammarReference();
            if ((gname != null) && (gname.length() > 0)) {
                RuleGrammar RG1 = null;
                try {
                    RG1 = (RuleGrammar) theRec.getGrammarManager().getGrammar(
                            gname);
                } catch (EngineStateException ex) {
                    ex.printStackTrace();
                }
                if (RG1 != null) {
                    ruleref = RG1.getRule(simpleName).getRuleComponent();
                    G = RG1;
                } else {
                    System.out.println("ERROR: UNKNOWN GRAMMAR " + gname);
                }
            }
            if (ruleref == null) {
                System.out.println("ERROR: UNKNOWN RULE NAME " + r.getRuleName() +
                                   " " + r);
                return null;
            }
        }

        RuleComponent rc = parse(G, ruleref, input);
        if (rc == null)
            return null;

        return new RuleParse(new RuleReference(simpleName), rc);
    }


    /**
     * LITERAL TOKENS
     **/
    private RuleToken parse(RuleGrammar G, RuleToken r, String input[]) {
        if (iPos >= input.length) {
            return null;
        }

        // @TODO: what about case sensitivity ??????
        String tText = r.getText().toLowerCase();
        if (tText.equals(input[iPos]) || (input[iPos].equals("%")) ||
            (input[iPos].equals("*"))) {
            iPos++;
            return new RuleToken(tText);
        } else {
            if (tText.indexOf(' ') < 0) {
                return null;
            }
            if (!tText.startsWith(input[iPos])) {
                return null;
            }
            String ta[] = tokenize(tText);
            int j = 0;
            StringBuffer strBuffer = new StringBuffer("");
            while (true) {
                if (j >= ta.length) {
                    break;
                }
                if (iPos >= input.length) {
                    return null;
                }
                if (!ta[j].equals(input[iPos])) {
                    return null;
                }
                strBuffer.append(ta[j]);
                iPos++;
                j++;
            }
            return new RuleToken(strBuffer.toString());
        }
    }

    /**
     * ALTERNATIVES
     **/
    private RuleComponent parse(RuleGrammar G, RuleAlternatives r, String input[]) {
        RuleComponent rar[] = r.getRuleComponents();
        Vector result = new Vector();
        for (int i = 0; i < rar.length; i++) {
            RuleComponent p = parse(G, rar[i], input);
            if (p == null) {
                continue;
            }
            result.add(p);
        }
        RuleComponent rc[] = new RuleComponent[result.size()];
        result.copyInto(rc);
        return new RuleAlternatives(rc);
    }

    /**
     * RULESEQUENCE
     **/
    private RuleComponent parse(RuleGrammar G, RuleSequence r, String input[]) {
        Vector<RuleComponent> result = new Vector<RuleComponent>();
        RuleComponent rarry[] = r.getRuleComponents();
        if ((rarry == null) || (rarry.length == 0)) {
            return null;
        }

        for (int j = 0; j < rarry.length; ++j) {
            final RuleComponent subres = parse(G, rarry[j], input);
            if (subres == null)
                return null;
            result.add(subres);
        }

        RuleComponent[] rc = new RuleComponent[result.size()];
        result.copyInto(rc);
        return new RuleSequence(rc);
    }

    /**
     * TAGS
     **/
    private RuleComponent parse(RuleGrammar G, RuleTag r, String input[]) {
        Object theTag = r.getTag();
        return new RuleTag(theTag);
    }

    /**
     * RULECOUNT
     * @todo have some problems with
     *   ...
     *   <item repeat="0-3">a</item>
     *   <item>a</item>
     *   ...
     *   input: aa
     **/
    private RuleComponent parse(RuleGrammar G, RuleCount r, String input[]) {
        int rcount = r.getRepeatMax() - r.getRepeatMin();
        RuleComponent ruleComponent = null;
        int i = 0;

        for (; i < r.getRepeatMin(); ++i) {
            RuleComponent rc = parse(G, r.getRuleComponent(), input);
            if (rc == null)
                return null;
            if (ruleComponent == null) {
                ruleComponent = rc;
            }
        }

        for (; i < r.getRepeatMax(); ++i) {
            RuleComponent rc = parse(G, r.getRuleComponent(), input);
            if (rc == null)
                break;
            if (ruleComponent == null) {
                ruleComponent = rc;
            }

        }

        if (r.getRepeatProbability() != RuleCount.MAX_PROBABILITY) {
            return new RuleCount(ruleComponent, i, i, r.getRepeatProbability());
        } else {
            return new RuleCount(ruleComponent, i, i);
        }
    }

    /**
     * tokenize a string
     **/
    static String[] tokenize(String text) {
        StringTokenizer st = new StringTokenizer(text);
        int size = st.countTokens();
        String res[] = new String[size];
        int i = 0;
        while (st.hasMoreTokens()) {
            res[i++] = st.nextToken().toLowerCase();
        }
        return res;
    }

}


