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

public class Rule {
	public static int PRIVATE_SCOPE = 0;

	public static int PUBLIC_SCOPE = 1;

	private String ruleName;

	private RuleComponent ruleComponent;

	private int scope;

	public Rule(String ruleName, RuleComponent ruleComponent) {
		this(ruleName, ruleComponent, PRIVATE_SCOPE);
	}

	public Rule(String ruleName, RuleComponent ruleComponent, int scope) {
		RuleComponent.checkValidGrammarText(ruleName);

		if ((scope != PRIVATE_SCOPE) && (scope != PUBLIC_SCOPE)) {
			throw new IllegalArgumentException(
					"Scope must be either PRIVATE_SCOPE or PUBLIC_SCOPE!");
		}
		
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

	public String toString() {
        StringBuffer str = new StringBuffer();
        
        str.append(getClass().getName());
        str.append("[");
        str.append(ruleName);
        str.append(",");
        str.append(ruleComponent);
        str.append(",");
        if (scope == PRIVATE_SCOPE) {
        	str.append("PRIVATE_SCOPE");
        } else {
        	str.append("PUBLIC_SCOPE");
        }
        str.append("]");
        
        return str.toString();
	}
}
