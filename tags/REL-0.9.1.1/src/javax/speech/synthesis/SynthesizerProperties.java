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

import javax.speech.EngineProperties;

public interface SynthesizerProperties extends EngineProperties {
    int DEFAULT_VOLUME = 0;

    int MAX_VOLUME = 1;

    int MEDIUM_VOLUME = 2;

    int MIN_VOLUME = 3;

    int OBJECT_LEVEL = 4;

    int QUEUE_LEVEL = 5;

    int WORD_LEVEL = 6;

    int getInterruptibility();

    int getPitch();

    int getPitchRange();

    int getSpeakingRate();

    Voice getVoice();

    int getVolume();

    void setInterruptibility(int level);

    void setPitch(int hertz);

    void setPitchRange(int hertz);

    void setPriority(int priority);

    void setSpeakingRate(int wpm);

    void setVoice(Voice voice);

    void setVolume(int volume);
}
