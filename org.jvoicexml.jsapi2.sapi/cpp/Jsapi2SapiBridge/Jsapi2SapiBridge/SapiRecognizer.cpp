#include "org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer.h"
#include "Recognizer.h"
#include <iostream>

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativGetBuildInGrammars
 * Signature: (J)Ljava/util/Vector;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_nativGetBuildInGrammars
  (JNIEnv *env, jobject object, jlong recognizerHandle){

	return false;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativeHandleAllocate
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_nativeHandleAllocate
  (JNIEnv *env, jobject object){

	/* create new Recognizer class */
		env->SetLongField( object, env->GetFieldID( env->GetObjectClass(object), "RecognizerHandle","J"), (long)new Recognizer() );
}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandleDeallocate
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_nativHandleDeallocate
  (JNIEnv *env, jobject object, jlong recognizerHandle){

	reinterpret_cast< Recognizer* >(recognizerHandle)->~Recognizer();
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandlePause
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_nativHandlePause__J
  (JNIEnv *env, jobject object, jlong recognizerHandle){

	reinterpret_cast< Recognizer* >(recognizerHandle)->pause();

}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandlePause
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_nativHandlePause__JI
  (JNIEnv *env, jobject object, jlong recognizerHandle, jint flags){

}
/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandleResume
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_nativHandleResume
(JNIEnv *env, jobject object, jlong recognizerHandle){

	return reinterpret_cast< Recognizer* >(recognizerHandle)->resume();
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativSetGrammar
 * Signature: (JLjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_nativSetGrammar
  (JNIEnv *env, jobject object, jlong recognizerHandle, jstring grammarPath){

	  	HRESULT hr = reinterpret_cast< Recognizer* >(recognizerHandle)->setGrammar( (const wchar_t*)env->GetStringChars( grammarPath, NULL) );

		reinterpret_cast< Recognizer* >(recognizerHandle)->startdictation();	

		if(SUCCEEDED(hr)){
			return true; 
		}
		else{
			return false;
		}

	return false;
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

	return reinterpret_cast< Recognizer* >(recognizerHandle)->startdictation();
}

