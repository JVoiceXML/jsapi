package org.jvoicexml.jsapi2.sapi.recognition;

public class SsmlInterpretation {

    private String tag;
    private String value;
    private float confidence;

    
    SsmlInterpretation(String tag, String value, float conf){
        this.tag = tag;
        this.value = value ;
        confidence = conf;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }

    public float getConfidence() {
        return confidence;
    }
}
