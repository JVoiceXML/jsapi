/*
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2017 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package org.jvoicexml.jsapi2.recognition;

import java.util.Collection;

import javax.speech.EngineStateException;
import javax.speech.recognition.SpeakerManager;
import javax.speech.recognition.SpeakerManagerUI;
import javax.speech.recognition.SpeakerProfile;

/**
 * Basic implementation of a speaker manager.
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 */
public class BaseSpeakerManager implements SpeakerManager {

    /** Known speaker profiles. */
    private Collection<SpeakerProfile> speakerProfiles;

    /** The current speaker profile. */
    private SpeakerProfile currentSpeaker;

    /**
     * Constructs a new object.
     */
    public BaseSpeakerManager() {
        speakerProfiles = new java.util.ArrayList<SpeakerProfile>();
        currentSpeaker = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSpeaker(final SpeakerProfile speaker) {
        speakerProfiles.add(speaker);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSpeaker(final SpeakerProfile speaker) {
        speakerProfiles.remove(speaker);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpeakerProfile getCurrentSpeaker() {
        return currentSpeaker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpeakerManagerUI getSpeakerManagerUI() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpeakerProfile[] listKnownSpeakers() {
        final SpeakerProfile[] profiles =
                new SpeakerProfile[speakerProfiles.size()];
        speakerProfiles.toArray(profiles);
        return profiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renameSpeaker(final SpeakerProfile oldSpeaker,
                              final SpeakerProfile newSpeaker) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreCurrentSpeaker() throws EngineStateException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveCurrentSpeaker() throws EngineStateException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentSpeaker(final SpeakerProfile speaker) {
        if (!speakerProfiles.contains(speaker)) {
            createSpeaker(speaker);
        }
        currentSpeaker = speaker;
    }
}
