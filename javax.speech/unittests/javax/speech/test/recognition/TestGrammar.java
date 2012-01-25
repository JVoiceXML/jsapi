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
import javax.speech.recognition.GrammarManager;
import javax.speech.recognition.ResultListener;

/**
 * Grammar for test purposes.
 * 
 * @author Dirk Schnelle-Walka
 */
public class TestGrammar implements Grammar {

    public void addGrammarListener(GrammarListener listener) {
        // TODO Auto-generated method stub
        
    }

    public void removeGrammarListener(GrammarListener listener) {
        // TODO Auto-generated method stub
        
    }

    public void addResultListener(ResultListener listener) {
        // TODO Auto-generated method stub
        
    }

    public void removeResultListener(ResultListener listener) {
        // TODO Auto-generated method stub
        
    }

    public int getActivationMode() {
        // TODO Auto-generated method stub
        return 0;
    }

    public GrammarManager getGrammarManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getReference() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setActivationMode(int mode) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    public boolean isActive() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isActivatable() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setActivatable(boolean activatable) {
        // TODO Auto-generated method stub
        
    }
    }
