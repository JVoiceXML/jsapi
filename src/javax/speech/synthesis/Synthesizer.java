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

package javax.speech.synthesis;

import javax.speech.AudioSegment;
import javax.speech.Engine;

public interface Synthesizer extends Engine {
    long QUEUE_EMPTY = 0;

    long QUEUE_NOT_EMPTY = 1;

    void addSpeakableListener(SpeakableListener listener);

    void addSynthesizerListener(SynthesizerListener listener);

    void cancel();

    void cancel(int id);

    void cancelAll();

    String getPhonemes(String text);

    int getSpeakableMask();

    SynthesizerProperties getSynthesizerProperties();

    void pause();

    void removeSpeakableListener(SpeakableListener listener);

    void removeSynthesizerListener(SynthesizerListener listener);

    boolean resume();

    void setSpeakableMask(int mask);

    int speak(AudioSegment audio, SpeakableListener listener);

    int speak(Speakable speakable, SpeakableListener listener);

    int speak(String text, SpeakableListener listener);

    int speakMarkup(String synthesisMarkup, SpeakableListener listener);
}
