/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An base implementation for JSR 113.
 *
 * Copyright (C) 2009 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 */

package org.jvoicexml.jsapi2.jse.synthesis.freetts;

import javax.speech.synthesis.Speakable;

/**
 * A {@link Speakable} as it is used in FreeTTS.
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class FreeTTSSpeakable implements Speakable {
    /** The markup. */
    private String markup;


    /**
     * Constructs a new object.
     * @param text the markup.
     */
    public FreeTTSSpeakable(final String text) {
        markup = text;
    }

    /**
     * {@inheritDoc}
     */
    public String getMarkupText() {
        return markup;
    }
}
