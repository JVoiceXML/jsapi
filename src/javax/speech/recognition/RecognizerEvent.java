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

import java.util.Vector;

import javax.speech.EngineEvent;

public class RecognizerEvent extends EngineEvent {
    public static int CHANGES_COMMITTED = ENGINE_ERROR << 1;

    public static int CHANGES_REJECTED = CHANGES_COMMITTED << 1;

    public static int RECOGNIZER_BUFFERING = CHANGES_REJECTED << 1;

    public static int RECOGNIZER_NOT_BUFFERING = RECOGNIZER_BUFFERING << 1;

    public static int RECOGNIZER_LISTENING = RECOGNIZER_NOT_BUFFERING << 1;

    public static int RECOGNIZER_PROCESSING = RECOGNIZER_NOT_BUFFERING << 1;

    public static int SPEECH_STARTED = RECOGNIZER_PROCESSING << 1;

    public static int SPEECH_STOPPED = SPEECH_STARTED << 1;

    public static int UNKNOWN_AUDIO_POSITION = SPEECH_STOPPED << 1;

    public static int DEFAULT_MASK = EngineEvent.DEFAULT_MASK
            | CHANGES_COMMITTED | CHANGES_REJECTED | RECOGNIZER_LISTENING
            | RECOGNIZER_PROCESSING | SPEECH_STARTED | SPEECH_STOPPED;

    private GrammarException grammarException;

    private long audioPosition;

    public RecognizerEvent(Recognizer source, int id, long oldEngineState,
            long newEngineState, Throwable problem,
            GrammarException grammarException, long audioPosition) {
        super(source, id, oldEngineState, newEngineState, problem);

        this.grammarException = grammarException;
        this.audioPosition = audioPosition;
    }

    public long getAudioPosition() {
        return audioPosition;
    }

    public GrammarException getGrammarException() {
        final int id = getId();
        if (id == CHANGES_REJECTED) {
            return grammarException;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    protected Vector getParameters() {
        final Vector parameters = super.getParameters();

        final Long audioPositionObject = new Long(audioPosition);
        parameters.addElement(audioPositionObject);
        parameters.addElement(grammarException);

        return parameters;
    }
}
