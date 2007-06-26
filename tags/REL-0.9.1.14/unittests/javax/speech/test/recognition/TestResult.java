/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate: $
 * Author:  $LastChangedBy: $
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

package javax.speech.test.recognition;

import javax.speech.recognition.Grammar;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.ResultToken;

/**
 * Result for test purposes.
 * 
 * @author Dirk Schnelle
 */
public class TestResult implements Result {

    /**
     * {@inheritDoc}
     */
    public void addResultListener(ResultListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public ResultToken getBestToken(int tokNum) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ResultToken[] getBestTokens() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Grammar getGrammar() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int getNumTokens() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public int getResultState() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public ResultToken[] getUnfinalizedTokens() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void removeResultListener(ResultListener listener) {
    }
}
