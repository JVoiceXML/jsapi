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

import javax.speech.EngineProperties;

public interface RecognizerProperties extends EngineProperties {
    int ADAPT_PAUSED = 0;

    int ADAPT_RESUMED = 1;

    int ENDPOINT_AUTOMATIC = 2;

    int ENDPOINT_MANUAL = 3;

    int ENDPOINT_PUSH_TO_START = 4;

    int ENDPOINT_PUSH_TO_TALK = 5;

    int ENDPOINT_SPEECH_DETECTION = 6;

    int MIN_ACCURACY = 7;

    int NORM_ACCURACY = 8;

    int MAX_ACCURACY = 8;

    int MIN_CONFIDENCE = 10;

    int NORM_CONFIDENCE = 11;

    int MAX_CONFIDENCE = 12;

    int UNKNOWN_CONFIDENCE = 13;

    int MIN_SENSITIVITY = 14;

    int NORM_SENSITIVITY = 15;

    int MAX_SENSITIVITY = 16;

    void setAdaptation(int adapt);

    int getAdaptation();

    void setCompleteTimeout(int timeout);

    int getCompleteTimeout();

    void setConfidenceThreshold(int confidenceThreshold);

    int getConfidenceThreshold();

    void setEndpointStyle(int endpointStyle);

    int getEndpointStyle();

    void setIncompleteTimeout(int timeout);

    int getIncompleteTimeout();

    void setNumResultAlternatives(int num);

    int getNumResultAlternatives();

    void setPriority(int priority);

    int getPriority();

    int getSensitivity();

    void setSpeedVsAccuracy(int speedVsAccuracy);
    
    int getSpeedVsAccuracy();

    void setResultAudioProvided(boolean audioProvided);

    boolean isResultAudioProvided();

    void setTrainingProvided(boolean trainingProvided);

    boolean isTrainingProvided();

    void setSensitivity(int sensitivity);
}
