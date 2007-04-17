package javax.speech;

import java.io.ByteArrayInputStream;

public class AudioSegment {
    private ByteArrayInputStream stream;
    
    private String locator;
    
    private String markupText;
    
    public AudioSegment(ByteArrayInputStream stream, String locator, 
            String markupText) {
       this.stream = stream;
       this.locator = locator;
       this.markupText = markupText;
    }
    
    public AudioSegment(String locator, String markupText) {
        this.locator = locator;
        this.markupText = markupText;
    }
    
    public String getLocator() {
        return locator;
    }

    public String getMarkupText() {
        return markupText;
    }

    public ByteArrayInputStream getInputStream() {
        // TODO: check system property javax.speech.supports.audio.capture
        return stream;
    }

    public boolean isGettable() {
        // TODO: check system property javax.speech.supports.audio.capture
        return stream != null;
    }
}
