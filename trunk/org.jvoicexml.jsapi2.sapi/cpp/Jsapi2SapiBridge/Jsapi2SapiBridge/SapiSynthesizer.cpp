#include "org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer.h"
#include <stdafx.h>

	CComPtr<ISpTTSEngine>			iSpTtsEngine;
	CComPtr<IEnumSpObjectTokens>	cpEnum;
	CComPtr<ISpObjectToken>			cpToken;
	CComPtr<ISpVoice>				cpVoice;
	HRESULT							hr;
	
	//ISpVoice *						cpVoice = NULL;

	LPCOLESTR pProgID = L"SAPI.SpVoice";
	CLSID clsid = GUID_NULL;


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
  (JNIEnv *env, jobject object){

	/*  get superclass from commited object */
	  //jclass jcls = env->GetSuperclass( env->GetObjectClass(object) );

	/*  get class from commited object */
	   jclass jcls = env->GetObjectClass(object);

	/*  get jfieldID of classmember engineName2 thats type is jstring */
		jfieldID jfid = env->GetFieldID(jcls, "engineName2","Ljava/lang/String;");

	/*  get the engineName2 Object und cast it to jstring*/
	/*  get the chars contained in engineName2 jsring and cast them to const wchar_t* */
		const wchar_t* engineName= (const wchar_t*)env->GetStringChars((jstring)env->GetObjectField(object, jfid),NULL);

 // Avoid 64bit problems CoCreateInstance from CComPtr<ISpVoice> struct to gain access to 32bit engines
	::CoInitialize(NULL);
	hr = CLSIDFromProgID(pProgID, &clsid);

	if(SUCCEEDED(hr))
	{	
		hr = cpVoice.CoCreateInstance(clsid ,NULL, CLSCTX_ALL);
	}
	// create cpEnum that contains the information about the designated TTSEngine
	if(SUCCEEDED(hr))
	{	
		hr = SpEnumTokens(SPCAT_VOICES, engineName, NULL, &cpEnum);  
	}  

	// load required cpToken from cpEnum
	if(SUCCEEDED(hr))
	{
		hr = cpEnum->Next(1, &cpToken, NULL);
	}
	// Set the designated Voice if not possible standard System is used
	if(SUCCEEDED(hr))
	{    
		hr = cpVoice->SetVoice(cpToken);
	}

}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancel
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancel__
  (JNIEnv *env, jobject object){
	  
	  if( SUCCEEDED(cpVoice->Speak( NULL, SPF_PURGEBEFORESPEAK, 0 ) )){
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
        cpVoice.Release();
        //cpVoice = NULL;
    }
	//::CoUninitialize();	

}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handlePause
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handlePause
  (JNIEnv *env, jobject object){

	  hr = cpVoice->Pause();
	
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleResume
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleResume
  (JNIEnv *env, jobject object){

	  if( SUCCEEDED( cpVoice->Resume() ) )	{
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

	if( SUCCEEDED( hr ) )
    {
		hr = cpVoice->Speak( (const wchar_t*)env->GetStringChars(string, NULL), SPF_ASYNC | SPF_IS_XML , NULL);
    }
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleSpeak
 * Signature: (ILjavax/speech/synthesis/Speakable;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleSpeak__ILjavax_speech_synthesis_Speakable_2
  (JNIEnv *env, jobject object, jint id, jobject item){
	
}


// SOME MISERABLE CONVERSION


//	std::wstring JavaToWSZ(JNIEnv* env, jstring string)
//{
//    std::wstring value;
//    if (string == NULL) {
//        return value; // empty string
//    }
//    const jchar* raw = env->GetStringChars(string, NULL);
//    if (raw != NULL) {
//        jsize len = env->GetStringLength(string);
//        value.assign(raw, len);
//        env->ReleaseStringChars(string, raw);
//    }
//    return value;
//}
//wchar_t * JavaToWSZ(JNIEnv* env, jstring string)
//{
//    if (string == NULL)
//        return NULL;
//    int len = env->GetStringLength(string);
//    const jchar* raw = env->GetStringChars(string, NULL);
//    if (raw == NULL)
//        return NULL;
//
//    wchar_t* wsz = new wchar_t[len+1];
//    memcpy(wsz, raw, len*2);
//    wsz[len] = 0;
//
//    env->ReleaseStringChars(string, raw);
//
//    return wsz;
//}
