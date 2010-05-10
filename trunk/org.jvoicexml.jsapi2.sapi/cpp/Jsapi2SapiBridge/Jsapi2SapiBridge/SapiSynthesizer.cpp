#include "org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer.h"
#include "synthesizer.h"
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
				// Achtung überlegen wie das mit java env allocierten speicher ist, eventuell freigeben notwendig
 
	/* create new Synthesizer class */
		Synthesizer* synth = new Synthesizer(engineName);
		std::cout<< synth << "\n";

	/* save pointer in JavaClass member sapiSynthesizerPtr as long value*/
		env->SetLongField( object, env->GetFieldID(jcls, "sapiSynthesizerPtr","J"), (long)synth);
	
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
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleSpeak__ILjava_lang_String_2
  (JNIEnv *env, jobject object, jint id, jstring string){
	  	
	/* get pointer sapiSynthesizerPtr in JavaClass as long value and cast it*/
		Synthesizer* synth = (Synthesizer*)env->GetLongField(object,env->GetFieldID(env->GetObjectClass(object), "sapiSynthesizerPtr","J"));

	/* get string and cast as const wchar_t* */
		synth->Speak( (const wchar_t*)env->GetStringChars(string, NULL) );

}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    handleSpeak
 * Signature: (ILjavax/speech/synthesis/Speakable;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_handleSpeak__ILjavax_speech_synthesis_Speakable_2
  (JNIEnv *env, jobject object, jint id, jobject item){
	
}

