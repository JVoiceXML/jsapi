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

import java.io.InputStream;
import java.io.OutputStream;

public interface AudioManager {
    int getAudioMask();

    void setAudioMask(int mask);

    void addAudioListener(AudioListener listener);

    void removeAudioListener(AudioListener listener);

    void audioStart() throws AudioException;

    void audioStop() throws AudioException;;

    void setMediaLocator(String locator) throws AudioException,
            EngineStateException;

    void setMediaLocator(String locator, InputStream stream)
            throws AudioException, EngineStateException;

    void setMediaLocator(String locator, OutputStream stream)
            throws AudioException, EngineStateException;

    String getMediaLocator();

    String[] getSupportedMediaLocators(String mediaLocator);

    boolean isSupportedMediaLocator(String mediaLocator);

    boolean isSameChannel(AudioManager audioManager);
}
