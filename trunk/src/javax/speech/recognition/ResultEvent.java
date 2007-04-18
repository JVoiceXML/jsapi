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

import javax.speech.SpeechEvent;

public class ResultEvent extends SpeechEvent {
    public static int AUDIO_RELEASED = 0;

    public static int DEFAULT_MASK = 1;

    public static int GRAMMAR_FINALIZED = 2;

    public static int RESULT_ACCEPTED = 3;

    public static int RESULT_CREATED = 4;

    public static int RESULT_REJECTED = 5;

    public static int RESULT_UPDATED = 6;

    public static int TRAINING_INFO_RELEASED = 7;

    private boolean tokensFinalized;

    private boolean unfinalizedTokensChanged;

    public ResultEvent(Result source, int id) {
        super(source, id);
    }

    public ResultEvent(Result source, int id, boolean tokensFinalized,
            boolean unfinalizedTokensChanged) {
        super(source, id);

        this.tokensFinalized = tokensFinalized;
        this.unfinalizedTokensChanged = unfinalizedTokensChanged;
    }

    public boolean isFinalizedChanged() {
        return tokensFinalized;
    }

    public boolean isUnfinalizedChanged() {
        return unfinalizedTokensChanged;
    }

    public String paramString() {
        return super.paramString();
    }
}
