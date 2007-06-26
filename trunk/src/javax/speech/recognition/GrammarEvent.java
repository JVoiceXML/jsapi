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

import java.util.Collection;

import javax.speech.SpeechEvent;

public class GrammarEvent extends SpeechEvent {

    public static int GRAMMAR_ACTIVATED = 1;

    public static int GRAMMAR_CHANGES_COMMITTED = GRAMMAR_ACTIVATED << 1;

    public static int GRAMMAR_CHANGES_REJECTED = GRAMMAR_CHANGES_COMMITTED << 1;

    public static int GRAMMAR_DEACTIVATED = GRAMMAR_CHANGES_REJECTED << 1;

    public static int DEFAULT_MASK = GRAMMAR_ACTIVATED
	    | GRAMMAR_CHANGES_COMMITTED | GRAMMAR_CHANGES_REJECTED
	    | GRAMMAR_DEACTIVATED;

    private boolean enabledChanged;

    private boolean definitionChanged;

    private GrammarException grammarException;

    public GrammarEvent(Object source, int id) {
    	super(source, id);
    }

    public GrammarEvent(Grammar source, int id, boolean enabledChanged,
	    boolean definitionChanged, GrammarException grammarException) {
    	super(source, id);

    	this.enabledChanged = enabledChanged;
    	this.definitionChanged = definitionChanged;
    	this.grammarException = grammarException;
    }

    public GrammarException getGrammarException() {
    	final int id = getId();
		if (id == GRAMMAR_CHANGES_COMMITTED) {
			return grammarException;
		}

		return null;
    }

    public boolean isDefinitionChanged() {
    	return definitionChanged;
    }

    public boolean isEnabledChanged() {
    	return enabledChanged;
    }

    /**
     * {@inheritDoc}
     */
    protected Collection getParameters() {
    	final Collection parameters = super.getParameters();

    	final Boolean definitionChangedObject = new Boolean(definitionChanged);
    	parameters.add(definitionChangedObject);
    	final Boolean enabledChangedObject = new Boolean(enabledChanged);
    	parameters.add(enabledChangedObject);
    	parameters.add(grammarException);

    	return parameters;
    }
}
