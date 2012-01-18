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

package org.jvoicexml.jsapi2.jse.synthesis;

import javax.sound.sampled.AudioFormat;
import javax.speech.AudioManager;
import javax.speech.SpeechEventExecutor;
import javax.speech.VocabularyManager;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerMode;

import org.jvoicexml.jsapi2.BaseVocabularyManager;
import org.jvoicexml.jsapi2.jse.ThreadSpeechEventExecutor;
import org.jvoicexml.jsapi2.synthesis.BaseSynthesizer;


/**
 * Base implementation of a speech synthesizer for JSE.
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 * @version $Revision: $
 */
public abstract class JseBaseSynthesizer extends BaseSynthesizer
    implements Synthesizer {
    /**
     * Constructs a new object.
     */
    public JseBaseSynthesizer() {
        this(null);
    }

    /**
     * Constructs a new object.
     * @param engineMode the engine mode
     */
    public JseBaseSynthesizer(final SynthesizerMode engineMode) {
        super(engineMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SpeechEventExecutor createSpeechEventExecutor() {
        return new ThreadSpeechEventExecutor();
    }

    /**
     * Retrieves the audio format that is produced by this synthesizer.
     * @return audio format.
     */
    protected abstract AudioFormat getAudioFormat();

    /**
     * {@inheritDoc}
     */
    @Override
    protected AudioManager createAudioManager() {
        final AudioFormat format = getAudioFormat();
        final BaseSynthesizerAudioManager manager =
            new BaseSynthesizerAudioManager(format);
        manager.setEngine(this);
        return manager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected VocabularyManager createVocabularyManager() {
        return new BaseVocabularyManager();
    }
}
