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

import javax.speech.recognition.ResultToken;

import org.jvoicexml.jsapi2.jse.recognition.BaseResult;

/**
 * A recognition result from the SAPI engine.
 * @author Dirk Schnelle-Walka
 */
@SuppressWarnings("serial")
public final class SapiResult extends BaseResult  {
    /** The received utterance. */
    private String utterance;

    /** The received utterance. */
    private String sml;

    /** The received utterance. */
    private float confidence;

    /**
     * Constructs a new object.
     */
    public SapiResult() {
        setSml(null);
        utterance = null;
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

    public void setSml(String ssml) {
        this.sml = ssml;
    }

    public String getSml() {
        return sml;
    }
    
    public void setTags(String[] tags) {
        this.tags = tags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append(SapiResult.class.getCanonicalName());
        str.append('[');
        str.append(utterance);
        str.append(',');
        str.append(confidence);
        str.append(',');
        str.append(sml);
        str.append(',');
        str.append(tags);
        str.append(']');
        return str.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultToken[] getUnfinalizedTokens() {
        return null;
    }
}
