/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 249 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
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

import javax.speech.Engine;
import javax.speech.EngineEvent;

public class RecognizerEvent extends EngineEvent {
    public static int CHANGES_COMMITTED = 0;

    public static int CHANGES_REJECTED = 1;

    public static int DEFAULT_MASK = 2;

    public static int RECOGNIZER_BUFFERING = 3;

    public static int RECOGNIZER_LISTENING = 4;

    public static int RECOGNIZER_NOT_BUFFERING = 5;

    public static int RECOGNIZER_PROCESSING = 6;

    public static int SPEECH_STARTED = 7;

    public static int SPEECH_STOPPED = 8;

    static int UNKNOWN_AUDIO_POSITION = 9;

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
        return grammarException;
    }

    public String paramString() {
        return super.paramString();
    }
}
