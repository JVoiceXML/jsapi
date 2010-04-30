#include "stdafx.h"
#include "SapiSynthesizer.h"

	ISpVoice * pVoice = NULL;

JNIEXPORT jobject JNICALL Java_SapiSynthesizer_getSpeakable
(JNIEnv *, jobject, jstring){

	return NULL;
}

/*
 * Class:     SapiSynthesizer
 * Method:    Allocate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_SapiSynthesizer_Allocate
(JNIEnv *, jobject){
		
	 HRESULT hr=::CoInitialize(NULL);

	 hr = CoCreateInstance(CLSID_SpVoice, NULL, CLSCTX_ALL, IID_ISpVoice, (void **)&pVoice);
    if( SUCCEEDED( hr ) )
    {
        hr = pVoice->Speak(L"Hello world", 0, NULL);
        pVoice->Release();
        pVoice = NULL;
    }

    ::CoUninitialize();
    

}

/*
 * Class:     SapiSynthesizer
 * Method:    Cancel
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_SapiSynthesizer_Cancel__
  (JNIEnv *, jobject);

/*
 * Class:     SapiSynthesizer
 * Method:    Cancel
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_SapiSynthesizer_Cancel__I
(JNIEnv *, jobject, jint){
	return NULL;
}

/*
 * Class:     SapiSynthesizer
 * Method:    CancelAll
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_SapiSynthesizer_CancelAll
(JNIEnv *, jobject){
	return NULL;
}

/*
 * Class:     SapiSynthesizer
 * Method:    Deallocate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_SapiSynthesizer_Deallocate
(JNIEnv *, jobject){

}

/*
 * Class:     SapiSynthesizer
 * Method:    Pause
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_SapiSynthesizer_Pause
(JNIEnv *, jobject){

}

/*
 * Class:     SapiSynthesizer
 * Method:    Resume
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_SapiSynthesizer_Resume
(JNIEnv *, jobject){
	return NULL;
}

/*
 * Class:     SapiSynthesizer
 * Method:    Speak
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_SapiSynthesizer_Speak__ILjava_lang_String_2
(JNIEnv *, jobject, jint, jstring){

}

/*
 * Class:     SapiSynthesizer
 * Method:    Speak
 * Signature: (ILjavax/speech/synthesis/Speakable;)V
 */
JNIEXPORT void JNICALL Java_SapiSynthesizer_Speak__ILjavax_speech_synthesis_Speakable_2
(JNIEnv *, jobject, jint, jobject){

}

/*
 * Class:     SapiSynthesizer
 * Method:    getChangeRequestListener
 * Signature: ()Lorg/jvoicexml/jsapi2/EnginePropertyChangeRequestListener;
 */
JNIEXPORT jobject JNICALL Java_SapiSynthesizer_getChangeRequestListener
(JNIEnv *, jobject){
	return NULL;
}