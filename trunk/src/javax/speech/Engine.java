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

package javax.speech;

public interface Engine {
    long ALLOCATED = 1;

    long ALLOCATING_RESOURCES = 2;

    int ASYNCHRONOUS_MODE = 3;

    long DEALLOCATED = 4;

    long DEALLOCATING_RESOURCES = 5;

    long DEFOCUSED = 6;

    long ERROR_OCCURRED = 7;

    long FOCUSED = 8;

    int IMMEDIATE_MODE = 9;

    long PAUSED = 10;

    long RESUMED = 11;

    void allocate();

    void allocate(int mode);

    void deallocate();

    void deallocate(int mode);

    AudioManager getAudioManager();

    int getEngineMask();

    EngineMode getEngineMode();

    long getEngineState();

    VocabularyManager getVocabularyManager();

    void pause();

    boolean resume();

    void setEngineMask(int mask);

    boolean testEngineState(long state);

    void waitEngineState(long state);
}
