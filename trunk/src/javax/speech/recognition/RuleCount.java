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
