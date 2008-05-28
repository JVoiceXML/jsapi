package org.jvoicexml.jsapi2.jse;

import javax.speech.AudioSegment;
import java.io.InputStream;
import java.io.IOException;

/**
 * <p>Title: JSAPI 2.0</p>
 *
 * <p>Description: An independent reference implementation of JSR 113</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class BaseAudioSegment extends AudioSegment {

    private InputStream is;

    public BaseAudioSegment(String locator, String markupText) {
        super(locator, markupText);
        this.is = null;
    }

    public BaseAudioSegment(String locator, String markupText, InputStream is) {
        super(locator, markupText);
        this.is = is;
    }

    public InputStream openInputStream()  throws IOException, SecurityException{
        if (is==null)
            return super.openInputStream();
        else
            return is;
    }

}
