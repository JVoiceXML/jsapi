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

public interface Grammar {
    int ACTIVATION_FOCUS = 0;

    int ACTIVATION_GLOBAL = 1;

    int ACTIVATION_MODAL = 2;

    void addGrammarListener(GrammarListener listener);

    void removeGrammarListener(GrammarListener listener);

    void addResultListener(ResultListener listener);

    void removeResultListener(ResultListener listener);

    int getActivationMode();

    Recognizer getRecognizer();

    String getReference();

    void setActivationMode(int mode);

    boolean isActive();

    void setEnabled(boolean flag);

    boolean isEnabled();
}
