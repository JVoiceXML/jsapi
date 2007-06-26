/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package javax.speech.recognition;

public interface RuleGrammar extends Grammar {
    void addElement(String element);

    void removeElement(String element);
    
    Rule getRule(String ruleName);

    void addRule(Rule rule);

    void addRule(String ruleText) throws GrammarException;

    void addRules(Rule[] rules);

    void removeRule(String ruleName);

    String[] listRuleNames();

    void setAttribute(String attribute, String value);

    String getAttribute(String attribute);

    String getDoctype();

    void setDoctype(String doctype);

    String[] getElements();

    void setEnabled(boolean enabled);

    boolean isEnabled();

    void setEnabled(String[] ruleNames, boolean enabled);

    void setEnabled(String ruleName, boolean enabled);

    boolean isEnabled(String ruleName);

    RuleParse parse(String[] tokens, String ruleName) throws GrammarException;

    RuleParse parse(String text, String ruleName) throws GrammarException;

    RuleReference resolve(RuleReference ruleReference) throws GrammarException;
    
    String getReference();
    
    void setRoot(String rootName);

    String getRoot();

    String toString();
}
