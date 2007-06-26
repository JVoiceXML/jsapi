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

public class RuleAlternatives extends RuleComponent {
    public static int MAX_WEIGHT = Integer.MAX_VALUE;

    public static int NORM_WEIGHT = MAX_WEIGHT / 2;

    public static int MIN_WEIGHT = 0;

    private RuleComponent[] ruleComponents;

    private int[] weights;

    public RuleAlternatives(RuleComponent[] ruleComponents) {
        this.ruleComponents = ruleComponents;
    }

    public RuleAlternatives(RuleComponent[] ruleComponents, int[] weights) {
        this.ruleComponents = ruleComponents;
        if (weights == null) {
            this.weights = new int[ruleComponents.length];
            for (int i = 0; i < ruleComponents.length; i++) {
                this.weights[i] = NORM_WEIGHT;
            }
        } else {
            if (ruleComponents.length != weights.length) {
                throw new IllegalArgumentException(
                        "Lengths of rule components and weights do not match!");
            }
            this.weights = weights;
        }
    }

    public RuleAlternatives(String[] tokens) {
        if (tokens != null) {
            ruleComponents = new RuleComponent[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                final String token = tokens[i];
                ruleComponents[i] = new RuleToken(token);
            }
        }
    }

    public RuleComponent[] getRuleComponents() {
        return ruleComponents;
    }

    public int[] getWeights() {
        return weights;
    }

    public String toString() {
        if ((ruleComponents == null) || (ruleComponents.length == 0)) {
            return RuleSpecial.VOID.toString();
        }

        final StringBuffer str = new StringBuffer();

        str.append("<one-of>");

        for (int i = 0; i < ruleComponents.length; i++) {
            str.append("<item");
            if (weights != null) {
                str.append(" weight=\"");
                final int weight = weights[i];
                final float realWeight = weight / NORM_WEIGHT;
                str.append(realWeight);
                str.append("\"");
            }
            str.append('>');
            final RuleComponent component = ruleComponents[i];
            if (component == null) {
                str.append(RuleSpecial.NULL.toString());
            } else {
                str.append(component.toString());
            }
            str.append("</item>");
        }
        str.append("</one-of>");

        return str.toString();
    }
}
