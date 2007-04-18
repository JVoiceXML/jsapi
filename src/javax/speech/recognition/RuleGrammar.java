/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 249 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
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
