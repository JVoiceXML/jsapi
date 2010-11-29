package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.speech.recognition.Grammar;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.ResultListener;
import javax.speech.recognition.ResultToken;

import org.jvoicexml.jsapi2.jse.recognition.BaseResult;
import org.jvoicexml.jsapi2.recognition.BaseResultToken;


@SuppressWarnings("serial")
public class SapiResult extends BaseResult  {
    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public String getUtterance() {
        return utterance;
    }

    /** The semantic interpretation of the utterance. */
    private Hashtable< Integer,SsmlInterpretation> interpretation;

    /** The received utterance. */
    private String utterance;
    
    /** The received utterance. */
    private String ssml;
    
    /** The received utterance. */
    private float confidence;
    
    
    public SapiResult() throws GrammarException{
        setSsml(null);
        utterance = null;
        interpretation = new Hashtable<Integer, SsmlInterpretation>(3);
    }
    
    public void setUtterance( String utterance){
        this.utterance=utterance;
    }
    public void setInterpretation( Integer number, SsmlInterpretation interp){
        interpretation.put( number, interp);
    }
    
    public Hashtable getInterpretation() {
        return interpretation;
    }

    public void setSsml(String ssml) {
        this.ssml = ssml;
    }

    public String getSsml() {
        return ssml;
    }
    
    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String toString() {      
        StringBuffer result = new StringBuffer();
        result.append("SapiResult : text = '"+ utterance +"' ");
        result.append("Confidence = '"+ confidence +"' ");
        
        if( !interpretation.isEmpty()){
            Enumeration<Integer> e = interpretation.keys();
            while (e.hasMoreElements()){
                    Integer i = e.nextElement();
                    SsmlInterpretation interp =interpretation.get(i);
                    result.append("tag"+i+" = '"+interp.getTag()+"="+interp.getValue()+"' ");
                    result.append("tag"+i+"Confidence ='"+interp.getConfidence()+"'");
                }
        }
        
        return result.toString();    
    }

    public boolean createResultTokens() {
        if (null == utterance) {
            //there HAS to be some tokens in a result token
            return false;
        } else {
            String[] tokens = utterance.split(" ");
            ResultToken[] resTokens = new ResultToken[tokens.length];
            int i = 0;
            for(String token : tokens) {
                resTokens[i++] = new BaseResultToken(this, token);
            }
            setTokens(resTokens);
            setNumTokens(resTokens.length);
            return true;
        }
    }
    
    public boolean createResultTokens(String utterance) {
        this.utterance = utterance;
        return createResultTokens();
    }
        
//    @Override
//    public ResultToken[] getBestTokens() {
//        
//        String[] token = utterance.split(" ");
//        ResultToken[] tokens = new ResultToken[token.length];
//        for(int i=0; i <token.length; i++){
//            tokens[i] = new BaseResultToken(this, token[i]) ;
//        }
//        return tokens;
//    }
    
    @Override
    public ResultToken[] getUnfinalizedTokens() {
        // TODO Auto-generated method stub
        return null;
    }
        
}
