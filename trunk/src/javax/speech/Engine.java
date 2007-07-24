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

package javax.speech;

public interface Engine {
    long ALLOCATED = 1;

    long ALLOCATING_RESOURCES = ALLOCATED << 1;

    long DEALLOCATED = ALLOCATING_RESOURCES << 1;

    long DEALLOCATING_RESOURCES = DEALLOCATED << 1;

    long DEFOCUSED = DEALLOCATING_RESOURCES << 1;

    long ERROR_OCCURRED = DEFOCUSED << 1;

    long FOCUSED = ERROR_OCCURRED << 1;

    long PAUSED = FOCUSED << 1;

    long RESUMED = PAUSED << 1;

    int ASYNCHRONOUS_MODE = 1;

    int IMMEDIATE_MODE = ASYNCHRONOUS_MODE << 1;

    void allocate() throws AudioException, EngineException,
            EngineStateException;

    void allocate(int mode) throws AudioException, EngineException,
            EngineStateException;

    void deallocate() throws AudioException, EngineException,
            EngineStateException;

    void deallocate(int mode) throws AudioException, EngineException,
            EngineStateException;

    void pause() throws EngineStateException;

    boolean resume() throws EngineStateException;

    boolean testEngineState(long state) throws IllegalArgumentException;

    void waitEngineState(long state) throws InterruptedException, IllegalArgumentException;

    AudioManager getAudioManager();

    EngineMode getEngineMode();

    long getEngineState();

    VocabularyManager getVocabularyManager() throws EngineStateException;

    void setEngineMask(int mask);

    int getEngineMask();

    SpeechEventExecutor getSpeechEventExecutor();

    void setSpeechEventExecutor(SpeechEventExecutor speechEventExecutor);
}
