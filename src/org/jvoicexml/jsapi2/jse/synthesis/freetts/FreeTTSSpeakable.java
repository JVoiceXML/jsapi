package org.jvoicexml.jsapi2.jse.synthesis.freetts;

import javax.speech.synthesis.Speakable;

/**
 * <p>Title: JSAPI2Engines</p>
 *
 * <p>Description: JSAPI 2.0 Engines implementations</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: INESC-ID L2F</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class FreeTTSSpeakable implements Speakable {

    private String markup;


    public FreeTTSSpeakable(String text) {
        markup = text;
    }

    /**
     * getMarkupText
     *
     * @return String
     * @todo Implement this javax.speech.synthesis.Speakable method
     */
    public String getMarkupText() {
        return markup;
    }
}
