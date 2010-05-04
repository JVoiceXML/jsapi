#include "org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer.h"
#include <stdafx.h>

	CComPtr<ISpTTSEngine>			iSpTtsEngine;
	CComPtr<IEnumSpObjectTokens>	cpEnum;
	CComPtr<ISpObjectToken>			cpToken;
	HRESULT							hr;
	ISpVoice *						ispVoice = NULL;




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
		   
	  //hr = iSpTtsEngine.CoCreateInstance( reinterpret_cast<const IID>(CLSID_SpVoice), CLSCTX_SERVER, NULL);
    //hr = iSpTTSEngine.CoInitialize(NULL);  //hr = m_cpVoice.CoCreateInstance( CLSID_SpVoice ); 

		::CoInitialize(NULL);
		hr = CoCreateInstance(CLSID_SpVoice, NULL, CLSCTX_ALL, IID_ISpVoice, (void **)&ispVoice);
		
    //if(SUCCEEDED(hr))
    // {
    //      hr = SpEnumTokens(SPCAT_VOICES, L"Name=Microsoft Mary", NULL, &cpEnum);  // MS Mary was not found by my system
    // }    

    // // cpToken is still empty, first load token from cpEnum
    // if(SUCCEEDED(hr))
    // {
    //      hr = cpEnum->Next(1, &cpToken, NULL);
    // }

    // if(SUCCEEDED(hr))
    // {    
    //      hr = ispVoice->SetVoice(cpToken);
    // }		
		
		hr = ispVoice->SetInterest( SPFEI_ALL_TTS_EVENTS, SPFEI_ALL_TTS_EVENTS );
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancel
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancel__
  (JNIEnv *env, jobject object){
	  
	  if( SUCCEEDED(ispVoice->Speak( NULL, SPF_PURGEBEFORESPEAK, 0 ) )){
		  //SUCCEEDED( ispVoice->SetVolume(0)
		  return JNI_TRUE;
	  }		
	  else{
		  return JNI_FALSE;
	  }
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancel
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancel__I
  (JNIEnv *env, jobject onbject, jint id){
	return NULL;
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancelAll
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancelAll
  (JNIEnv *env, jobject object){
	return NULL;
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleDeallocate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleDeallocate
  (JNIEnv *env, jobject object){

	if( SUCCEEDED( hr ) )
    {
        ispVoice->Release();
        ispVoice = NULL;
    }
    ::CoUninitialize();	

}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handlePause
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handlePause
  (JNIEnv *env, jobject object){

	  hr = ispVoice->Pause();
	
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleResume
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleResume
  (JNIEnv *env, jobject object){

	  if( SUCCEEDED( ispVoice->Resume() ) )	{
		  return JNI_TRUE;
	  }		
	  else{
		  return JNI_FALSE;
	  }
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
		hr = ispVoice->Speak( wsz, SPF_ASYNC | SPF_IS_XML , NULL);
    }
	env->ReleaseStringChars(string, raw);	
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleSpeak
 * Signature: (ILjavax/speech/synthesis/Speakable;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleSpeak__ILjavax_speech_synthesis_Speakable_2
  (JNIEnv *env, jobject object, jint id, jobject item){
	
}