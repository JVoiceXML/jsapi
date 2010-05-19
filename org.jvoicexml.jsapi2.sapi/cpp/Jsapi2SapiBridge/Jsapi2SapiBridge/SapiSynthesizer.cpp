#include "stdafx.h"
#include <org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer.h>
#include "Synthesizer.h"
#include <iostream>

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
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleAllocate
  (JNIEnv* env, jobject obj, jstring string)
{
    const wchar_t* engineName = (const wchar_t*)env->GetStringChars(string, NULL);
 
	/* create new Synthesizer class */
	Synthesizer* synth = new Synthesizer(engineName);		
    if (synth == NULL)
    {
        jclass Exception = env->FindClass("java/lang/NullPointerException");
        if (Exception == 0) /* Unable to find the new exception class, give up. */
            return 0;
        env->ThrowNew(Exception, "MS SAPI ERRORCODE: " + synth->hr);
    }
    if ( !SUCCEEDED( synth->hr ) )
    {
        jclass Exception = env->FindClass("java/lang/EngineException" );
        if (Exception == 0) /* Unable to find the new exception class, give up. */
            return 0;
        env->ThrowNew(Exception, "MS SAPI ERRORCODE: " + synth->hr );
    }
    return (jlong) synth;
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleCancel
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleCancel__
  (JNIEnv *env, jobject object){

	/* get pointer sapiSynthesizerPtr in JavaClass as long value and cast it*/
		Synthesizer* synth = (Synthesizer*)env->GetLongField(object,env->GetFieldID(env->GetObjectClass(object), "sapiSynthesizerPtr","J"));
	
		return synth->Cancel();
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

	/* get pointer sapiSynthesizerPtr in JavaClass as long value and cast it*/
		Synthesizer* synth = (Synthesizer*)env->GetLongField(object,env->GetFieldID(env->GetObjectClass(object), "sapiSynthesizerPtr","J"));
		synth->~Synthesizer();

}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handlePause
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handlePause
  (JNIEnv *env, jobject object){
	  
	  /* get pointer sapiSynthesizerPtr in JavaClass as long value and cast it*/
		Synthesizer* synth = (Synthesizer*)env->GetLongField(object,env->GetFieldID(env->GetObjectClass(object), "sapiSynthesizerPtr","J"));
	
		synth->Pause();
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleResume
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleResume
  (JNIEnv *env, jobject object){

	  /* get pointer sapiSynthesizerPtr in JavaClass as long value and cast it*/
		Synthesizer* synth = (Synthesizer*)env->GetLongField(object,env->GetFieldID(env->GetObjectClass(object), "sapiSynthesizerPtr","J"));
	
		return synth->Resume();

}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleSpeak
 * Signature: (JILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleSpeak__JILjava_lang_String_2
  (JNIEnv * env, jobject obj, jlong handle, jint id, jstring item)
{
	Synthesizer* synth = (Synthesizer*) handle;

	/* get string and cast as const wchar_t* */
    const wchar_t* utterance = (const wchar_t*)env->GetStringChars(item, NULL);
	synth->Speak(utterance);
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleSpeak
 * Signature: (ILjavax/speech/synthesis/Speakable;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleSpeak__ILjavax_speech_synthesis_Speakable_2
  (JNIEnv *env, jobject object, jint id, jobject item){
	
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    getChangeRequestListener
 * Signature: ()Lorg/jvoicexml/jsapi2/EnginePropertyChangeRequestListener;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_getChangeRequestListener
(JNIEnv *env, jobject object){

		  /* get pointer sapiSynthesizerPtr in JavaClass as long value and cast it*/
		Synthesizer* synth = (Synthesizer*)env->GetLongField(object,env->GetFieldID(env->GetObjectClass(object), "sapiSynthesizerPtr","J"));
	    return NULL;
		//return env->NewObject(NULL, synth->getEventHandler());
		

}
