#include "org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer.h"
#include <sapi.h>

	//CComPtr<ISpTTSEngine> iSpTTSEngine;

	ISpVoice *			pVoice = NULL;
	HRESULT				hr;

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    getSpeakable
 * Signature: (Ljava/lang/String;)Ljavax/speech/synthesis/Speakable;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_getSpeakable
(JNIEnv *, jobject, jstring){
	return NULL;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleAllocate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleAllocate
  (JNIEnv *, jobject){
		     
    //hr = iSpTTSEngine.  
    ::CoInitialize(NULL);    
    hr = CoCreateInstance(CLSID_SpVoice, NULL, CLSCTX_ALL, IID_ISpVoice, (void **)&pVoice);
	hr = pVoice->SetInterest( SPFEI_ALL_TTS_EVENTS, SPFEI_ALL_TTS_EVENTS );
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancel
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancel__
  (JNIEnv *, jobject){
	return NULL;
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancel
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancel__I
  (JNIEnv *, jobject, jint){
	return NULL;
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancelAll
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancelAll
  (JNIEnv *, jobject){
	return NULL;
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleDeallocate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleDeallocate
  (JNIEnv *, jobject){

	if( SUCCEEDED( hr ) )
    {
        pVoice->Release();
        pVoice = NULL;
    }
    ::CoUninitialize();	

}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handlePause
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handlePause
  (JNIEnv *, jobject){
	
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleResume
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleResume
  (JNIEnv *, jobject){
	return NULL;
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleSpeak
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleSpeak__ILjava_lang_String_2
  (JNIEnv *env, jobject object, jint id, jstring string){


    int len = env->GetStringLength(string);
    const jchar* raw = env->GetStringChars(string, NULL);

    wchar_t* wsz = new wchar_t[len+1];
    memcpy(wsz, raw, len*2);
    wsz[len] = 0;

	if( SUCCEEDED( hr ) )
    {
        hr = pVoice->Speak( wsz, SPF_IS_XML, NULL);
    }
	env->ReleaseStringChars(string, raw);	
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleSpeak
 * Signature: (ILjavax/speech/synthesis/Speakable;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleSpeak__ILjavax_speech_synthesis_Speakable_2
  (JNIEnv *, jobject, jint, jobject){
	
}