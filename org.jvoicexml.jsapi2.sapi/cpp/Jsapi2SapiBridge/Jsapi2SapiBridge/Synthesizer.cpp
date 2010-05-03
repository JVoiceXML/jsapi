#include "org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer.h"
#include "Libraries.h"
#include <string>

		// Declare local identifiers:
	HRESULT						hr;
	CComPtr<ISpEventSource>		cpEventSource;
	ULONGLONG					ullMyEvents = SPFEI(SPEI_HYPOTHESIS);
	ISpVoice*					pVoice = NULL;

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    getSpeakable
 * Signature: (Ljava/lang/String;)Ljavax/speech/synthesis/Speakable;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_getSpeakable
  (JNIEnv *, jobject, jstring){
	return false;
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleAllocate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleAllocate
  (JNIEnv *env, jobject object){

	hr=::CoInitialize(NULL);

	hr = CoCreateInstance(CLSID_SpVoice, NULL, CLSCTX_ALL, IID_ISpVoice, (void **)&pVoice);
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancel
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancel__
  (JNIEnv *, jobject){
	return false;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancel
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancel__I
  (JNIEnv *, jobject, jint){
	return false;
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancelAll
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancelAll
  (JNIEnv *, jobject){
	return false;
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleDeallocate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleDeallocate
(JNIEnv *, jobject){

	 pVoice->Release();
     pVoice = NULL;
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
  (JNIEnv *, jobject);

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleSpeak
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleSpeak__ILjava_lang_String_2
(JNIEnv *env, jobject object, jint id, jstring string){
		  
	if( SUCCEEDED( hr ) ){

			// Converting 	//16-bit UNICODE character
			if (string != NULL){
				int len = env->GetStringLength(string);
				const jchar* raw = env->GetStringChars(string, NULL);

				wchar_t* wsz = new wchar_t[len+1];
				memcpy(wsz, raw, len*2);
				wsz[len] = 0;		

        

		if(wsz!=NULL){
        hr = pVoice->Speak( wsz, SPF_IS_XML, NULL);
		}
				env->ReleaseStringChars(string, raw);
			}
    }
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleSpeak
 * Signature: (ILjavax/speech/synthesis/Speakable;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleSpeak__ILjavax_speech_synthesis_Speakable_2
(JNIEnv *, jobject, jint, jobject){

}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    getChangeRequestListener
 * Signature: ()Lorg/jvoicexml/jsapi2/EnginePropertyChangeRequestListener;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_getChangeRequestListener
  (JNIEnv *env , jobject object){
	  return NULL;
	
	//ULONG*		pulFetched;		
	//SPEVENT*		eventItem ; 
	//// Set type of events the client is interested in.
	//hr = cpEventSource->SetInterest(ullMyEvents, ullMyEvents);

	//if (SUCCEEDED(hr))
	//{
	//	hr = cpEventSource->GetEvents( 1, eventItem, pulFetched);
	//	if ( hr == E_POINTER)
	//	{
	//		return eventItem->eEventId;

	//	}
	//}


}

