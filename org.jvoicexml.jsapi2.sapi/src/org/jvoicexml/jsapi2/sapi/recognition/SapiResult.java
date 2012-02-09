/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 68 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2010-2012 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.speech.recognition.ResultToken;

import org.jvoicexml.jsapi2.jse.recognition.BaseResult;
import org.jvoicexml.jsapi2.recognition.BaseResultToken;

/**
 * A recognition result from the SAPI engine.
 * @author Dirk Schnelle-Walka
 */
@SuppressWarnings("serial")
public final class SapiResult extends BaseResult  {
    /** The semantic interpretation of the utterance. */
    private Hashtable<Integer, SmlInterpretation> interpretation;

    /** The received utterance. */
    private String utterance;
    
    /** The received utterance. */
    private String ssml;
    
    /** The received utterance. */
    private float confidence;
    
    /**
     * Constructs a new object.
     */
    public SapiResult() {
        setSsml(null);
        utterance = null;
        interpretation = new Hashtable<Integer, SmlInterpretation>(3);
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(final float confidence) {
        this.confidence = confidence;
    }

    public String getUtterance() {
        return utterance;
    }

    public void setUtterance(final String utterance){
        this.utterance=utterance;
    }

    public void setInterpretation(Integer number, SmlInterpretation interp) {
        interpretation.put(number, interp);
    }
    
    public Hashtable getInterpretation() {
        return interpretation;
    }

    public void setSsml(String ssml) {
        this.ssml = ssml;
    }

    public String getSsml() {
        return ssml;
    }
    
    public void setTags(String[] tags) {
        this.tags = tags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("SapiResult : text = '" + utterance + "' ");
        result.append("Confidence = '" + confidence + "' ");
        
        if (!interpretation.isEmpty()) {
            Enumeration<Integer> e = interpretation.keys();
            while (e.hasMoreElements()) {
                    Integer i = e.nextElement();
                    SmlInterpretation interp = interpretation.get(i);
                    result.append("tag"+i+" = '"+interp.getTag()+"="+interp.getValue()+"' ");
                    result.append("tag"+i+"Confidence ='"+interp.getConfidence()+"'");
                }
        }
        
        return result.toString();
    }

    public boolean createResultTokens() {
        if (null == utterance) {
            //there HAS to be some tokens in a result token
            return false;
        } else {
            String[] tokens = utterance.split(" ");
            ResultToken[] resTokens = new ResultToken[tokens.length];
            int i = 0;
            for(String token : tokens) {
                resTokens[i++] = new BaseResultToken(this, token);
            }
            setTokens(resTokens);
            setNumTokens(resTokens.length);
            return true;
        }
    }
    
    public boolean createResultTokens(String utterance) {
        this.utterance = utterance;
        return createResultTokens();
    }
        
    @Override
    public ResultToken[] getUnfinalizedTokens() {
        // TODO Auto-generated method stub
        return null;
    }
}
