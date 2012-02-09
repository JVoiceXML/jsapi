package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Hashtable;

public class SMLHandler implements DocHandler {
    private SapiResult result;
    private String tag;
    private float conf;
    private int valueTrue;
    private int count;
    
    public SMLHandler(SapiResult result) {
        this.result= result;
    }

    @Override
    public void endDocument() throws Exception {
    }

    @Override
    public void endElement(String tag) throws Exception {
    }

    @Override
    public void startDocument() throws Exception {
    }

    @Override
    public void startElement(String tag, Hashtable h) throws Exception {
        this.tag = tag;
        if( tag.equals("SML") ){
           result.setUtterance( (String)h.get("text"));
           result.setConfidence(Float.parseFloat((String)h.get("confidence")));
           count = -1;
           this.tag = tag;
        }
        else
        {
            String helper = (String) h.get("confidence");
            String help[] = helper.split("E-");
            
            if(help.length == 1){
                conf = Float.valueOf(help[0]);
            }
            if(help.length == 2){
                float faktor= 0.1f;
                for(int i=0; i<Integer.valueOf(help[1]); i++ ) {
                    faktor*=faktor;
                }
                conf = faktor*Float.valueOf(help[0]);
            }
            valueTrue = 1;
            count++;
        }                
    }

    @Override
    public void text(String str) throws Exception {
        if(valueTrue == 1) { 
            result.setInterpretation( Integer.valueOf(count),
                    new SmlInterpretation(tag, str, conf));
            valueTrue = 0;
        }       
    }
}
