/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 68 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2012 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package org.jvoicexml.jsapi2.mock;

import javax.speech.AudioException;
import javax.speech.AudioManager;
import javax.speech.EngineEvent;
import javax.speech.EngineException;
import javax.speech.EngineStateException;
import javax.speech.SpeechEventExecutor;
import javax.speech.VocabularyManager;

import org.jvoicexml.jsapi2.BaseEngine;

/**
 * An engine for test purposes.
 * @author Dirk Schnelle-Walka
 */
public class MockEngine extends BaseEngine {
    public MockEngine() {
        super(null);
    }

    /**
     * {@inheritDoc}
     */
    protected void baseAllocate() throws AudioException, EngineException,
            EngineStateException, SecurityException {
    }

    /**
     * {@inheritDoc}
     */
    protected AudioManager createAudioManager() {
        return new MockAudioManager();
    }

    /**
     * {@inheritDoc}
     */
    protected VocabularyManager createVocabularyManager() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    protected SpeechEventExecutor createSpeechEventExecutor() {
        return new MockSpeechEventExecutor();
    }

    /**
     * {@inheritDoc}
     */
    protected void baseDeallocate() throws EngineStateException,
            EngineException, AudioException {
    }

    /**
     * {@inheritDoc}
     */
    protected void basePause() throws EngineStateException {
    }

    /**
     * {@inheritDoc}
     */
    protected boolean baseResume() throws EngineStateException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    protected void fireEvent(EngineEvent event) {
    }

    /**
     * {@inheritDoc}
     */
    protected EngineEvent createStateTransitionEngineEvent(long oldState, long newState,
            int id) {
        return new MockEngineEvent(this, id, oldState, newState, null);
    }
}
