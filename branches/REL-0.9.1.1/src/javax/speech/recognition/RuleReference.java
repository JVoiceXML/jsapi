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

public class RuleReference extends RuleComponent {
	private static final String DEFAULT_MEDIA_TYPE = "application/srgs+xml";
	
	private String grammarReference;

    private String ruleName;

    private String mediaType;

    public RuleReference(String ruleName) {
    	checkValidGrammarText(ruleName);

        this.ruleName = ruleName;
    }

    public RuleReference(String grammarReference, String ruleName) {
    	checkValidGrammarText(grammarReference);
    	checkValidGrammarText(ruleName);

    	this.grammarReference = grammarReference;
        this.ruleName = ruleName;
    }

    public RuleReference(String grammarReference, String ruleName,
            String mediaType) {
    	checkValidGrammarText(grammarReference);
    	checkValidGrammarText(ruleName);

    	// TODO According to the specification, we must check the media type
    	// to be valid grammar text. This forbids strings like 
    	// 'application/x-jsgf'.
    	if (mediaType != null) {
            this.mediaType = mediaType;
    	}

    	this.grammarReference = grammarReference;
        this.ruleName = ruleName;
    }

    
    public String getGrammarReference() {
        return grammarReference;
    }

    public String getMediaType() {
    	if (mediaType == null) {
    		return DEFAULT_MEDIA_TYPE;
    	}
    	
        return mediaType;
    }

    public String getRuleName() {
        return ruleName;
    }

    void appendStartTag(StringBuffer str) {
    	str.append("<ruleref uri=\"");
    	
    	if (grammarReference != null) {
    		str.append(grammarReference);
    	}
    	str.append("#");
    	str.append(ruleName);
    	str.append("\"");
    	
    	if (mediaType != null) {
    		str.append(" type=\"");
    		str.append(mediaType);
    		str.append("\"");
    	}
    }
    
    public String toString() {
    	StringBuffer str = new StringBuffer();
    	appendStartTag(str);
    	str.append("/>");
    	
    	return str.toString();
    }
    
    
}
