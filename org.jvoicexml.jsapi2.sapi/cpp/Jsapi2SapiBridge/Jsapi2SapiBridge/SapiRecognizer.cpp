#include "org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer.h"
#include "Recognizer.h"
#include <iostream>

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativGetBuildInGrammars
 * Signature: (J)Ljava/util/Vector;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiGetBuildInGrammars
  (JNIEnv *env, jobject object, jlong recognizerHandle){

	return false;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativeHandleAllocate
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiAllocate
  (JNIEnv *env, jobject object){

	/* create new Recognizer class */
	  Recognizer* recognizer =new Recognizer();
	  
	/* check if Handle valid */
	  if( recognizer == NULL ){
		env->ThrowNew( env->FindClass("java/lang/IllegalArgumentException"),"MS SAPI: no Recognizer allocated" );
	  }		

	/* Set recognizer handle to RecognizerHandle in Class SapiRecognizer */
		env->SetLongField( object, env->GetFieldID( env->GetObjectClass(object), "recognizerHandle","J"), (long) recognizer);
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandleDeallocate
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiDeallocate
  (JNIEnv *env, jobject object, jlong recognizerHandle){

	reinterpret_cast< Recognizer* >(recognizerHandle)->~Recognizer();
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandlePause
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiPause__J
  (JNIEnv *env, jobject object, jlong recognizerHandle){

	HRESULT hr = reinterpret_cast< Recognizer* >(recognizerHandle)->pause();
	
	if( FAILED(hr)){
		env->ThrowNew( env->FindClass("java/lang/IllegalArgumentException"),"MS SAPI: Pause Recognizer failed" );
	}
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandlePause
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiPause__JI
  (JNIEnv *env, jobject object, jlong recognizerHandle, jint flags){

}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandleResume
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiResume
(JNIEnv *env, jobject object, jlong recognizerHandle){

	if( SUCCEEDED(reinterpret_cast<Recognizer*>(recognizerHandle)->resume()) ){
		return true;
	}
	else{
		return false;
	}
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativSetGrammar
 * Signature: (JLjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiSetGrammar
  (JNIEnv *env, jobject object, jlong recognizerHandle, jstring grammarPath){

		if( SUCCEEDED(reinterpret_cast< Recognizer* >(recognizerHandle)->setGrammar( (const wchar_t*)env->GetStringChars( grammarPath, NULL) )) ){
			reinterpret_cast< Recognizer* >(recognizerHandle)->startdictation();	
			return true;
		}
		else{
			return false;
		}

		
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    getChangeRequestListener
 * Signature: ()Lorg/jvoicexml/jsapi2/EnginePropertyChangeRequestListener;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_getChangeRequestListener
 (JNIEnv *env, jobject object){

	return false;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    start
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_start
(JNIEnv *env, jobject object, jlong recognizerHandle){

	reinterpret_cast< Recognizer* >(recognizerHandle)->startdictation();
	
}

