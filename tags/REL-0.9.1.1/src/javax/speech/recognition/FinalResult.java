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

import javax.speech.AudioSegment;

public interface FinalResult extends Result {
    int DONT_KNOW = 0;

    int MISRECOGNITION = 1;

    int USER_CHANGE = 2;

    ResultToken[] getAlternativeTokens(int nBest);

    AudioSegment getAudio();

    AudioSegment getAudio(ResultToken fromToken, ResultToken toToken);

    int getConfidenceLevel();

    int getConfidenceLevel(int nBest);

    int getNumberAlternatives();

    boolean isAudioAvailable();

    boolean isTrainingInfoAvailable();

    void releaseAudio();

    void releaseTrainingInfo();

    void tokenCorrection(String[] correctTokens, ResultToken fromToken,
            ResultToken toToken, int correctionType);
}