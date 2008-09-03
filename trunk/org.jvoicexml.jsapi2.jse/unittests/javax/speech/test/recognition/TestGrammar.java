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
import javax.speech.recognition.GrammarListener;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.GrammarManager;

/**
 * Grammar for test purposes.
 *
 * @author Dirk Schnelle
 */
public class TestGrammar implements Grammar {
    /**
     * {@inheritDoc}
     */
    public void addGrammarListener(GrammarListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public void addResultListener(ResultListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public int getActivationMode() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public Recognizer getRecognizer() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getReference() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isActive() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void removeGrammarListener(GrammarListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public void removeResultListener(ResultListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public void setActivationMode(int mode) {
    }

    /**
     * {@inheritDoc}
     */
    public void setEnabled(boolean flag) {
    }

    public GrammarManager getGrammarManager() {
        return null;
    }

    public boolean isActivatable() {
        return false;
    }

    public void setActivatable(boolean _boolean) {
    }
}
