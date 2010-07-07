package org.jvoicexml.jsapi2.sapi.recognition;

public class SapiRecognitionThread extends Thread{
      
    /** calling SapiRecognizer  **/
    private SapiRecognizer recognizer;
   
    /** Start recognition **/
    private native String sapiRecognize(long recognizerHandle);
    
    public SapiRecognitionThread(SapiRecognizer recognizer){
        this.recognizer = recognizer;
    }
    
    public void run(){
        String utterance = sapiRecognize(recognizer.getRecognizerHandle());
    
        recognizer.reportResult(utterance);
    
    }
    
}
