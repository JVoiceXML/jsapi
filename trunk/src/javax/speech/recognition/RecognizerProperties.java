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

import javax.speech.EngineProperties;

public interface RecognizerProperties extends EngineProperties {
    int ADAPT_PAUSED = 0;

    int ADAPT_RESUMED = 1;

    int ENDPOINT_AUTOMATIC = 2;

    int ENDPOINT_MANUAL = 3;

    int ENDPOINT_PUSH_TO_START = 4;

    int ENDPOINT_PUSH_TO_TALK = 5;

    int ENDPOINT_SPEECH_DETECTION = 6;

    int MAX_ACCURACY = 7;

    int MAX_CONFIDENCE = 8;

    int MAX_SENSITIVITY = 9;

    int MIN_ACCURACY = 10;

    int MIN_CONFIDENCE = 11;

    int MIN_SENSITIVITY = 12;

    int NORM_ACCURACY = 13;

    int NORM_CONFIDENCE = 14;

    int NORM_SENSITIVITY = 15;

    int UNKNOWN_CONFIDENCE = 16;

    int getAdaptation();

    int getCompleteTimeout();

    int getConfidenceThreshold();

    int getEndpointStyle();

    int getIncompleteTimeout();

    int getNumResultAlternatives();

    int getPriority();

    int getSensitivity();

    int getSpeedVsAccuracy();

    boolean isResultAudioProvided();

    boolean isTrainingProvided();

    void setAdaptation(int adapt);

    void setCompleteTimeout(int timeout);

    void setConfidenceThreshold(int confidenceThreshold);

    void setEndpointStyle(int endpointStyle);

    void setIncompleteTimeout(int timeout);

    void setNumResultAlternatives(int num);

    void setPriority(int priority);

    void setResultAudioProvided(boolean audioProvided);

    void setSensitivity(int sensitivity);

    void setSpeedVsAccuracy(int speedVsAccuracy);

    void setTrainingProvided(boolean trainingProvided);
}
