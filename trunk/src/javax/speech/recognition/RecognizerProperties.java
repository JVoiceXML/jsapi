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
    public static int ADAPT_PAUSED = 1;

    public static int ADAPT_RESUMED = 2;

    public static int ENDPOINT_AUTOMATIC = 1;

    public static int ENDPOINT_MANUAL = ENDPOINT_AUTOMATIC << 1;

    public static int ENDPOINT_SPEECH_DETECTION = ENDPOINT_MANUAL << 1;

    public static int ENDPOINT_PUSH_TO_TALK = ENDPOINT_SPEECH_DETECTION << 1;

    public static int ENDPOINT_PUSH_TO_START = ENDPOINT_PUSH_TO_TALK << 1;

    public static int MIN_ACCURACY = 0;

    public static int NORM_ACCURACY = 5;

    public static int MAX_ACCURACY = 10;

    public static int MIN_CONFIDENCE = 0;

    public static int NORM_CONFIDENCE = 5;

    public static int MAX_CONFIDENCE = 10;

    public static int UNKNOWN_CONFIDENCE = -1;

    public static int MIN_SENSITIVITY = 0;

    public static int NORM_SENSITIVITY = 5;

    public static int MAX_SENSITIVITY = 10;

    void setAdaptation(int adapt) throws IllegalArgumentException;

    int getAdaptation();

    void setCompleteTimeout(int timeout) throws IllegalArgumentException;

    int getCompleteTimeout();

    void setConfidenceThreshold(int confidenceThreshold) throws IllegalArgumentException;

    int getConfidenceThreshold();

    void setEndpointStyle(int endpointStyle) throws IllegalArgumentException;

    int getEndpointStyle();

    void setIncompleteTimeout(int timeout) throws IllegalArgumentException;

    int getIncompleteTimeout();

    void setNumResultAlternatives(int num) throws IllegalArgumentException;

    int getNumResultAlternatives();

    void setPriority(int priority) throws IllegalArgumentException;

    int getPriority();

    int getSensitivity();

    void setSpeedVsAccuracy(int speedVsAccuracy) throws IllegalArgumentException;

    int getSpeedVsAccuracy();

    void setResultAudioProvided(boolean audioProvided);

    boolean isResultAudioProvided();

    void setTrainingProvided(boolean trainingProvided);

    boolean isTrainingProvided();

    void setSensitivity(int sensitivity) throws IllegalArgumentException;
}
