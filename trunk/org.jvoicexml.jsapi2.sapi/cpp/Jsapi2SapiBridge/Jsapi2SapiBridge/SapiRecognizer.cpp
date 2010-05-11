#include "org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer.h"
#include "Recognizer.h"
#include <iostream>


/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    getBuiltInGrammars
 * Signature: ()Ljava/util/Vector;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_getBuiltInGrammars
(JNIEnv *, jobject){

	return false;

}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    handleAllocate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_handleAllocate
(JNIEnv *env, jobject object){

	/* create new Recognizer class */
		Recognizer* recognizer = new Recognizer();

	/* save pointer in JavaClass member sapiRecognizerPtr as long value*/
		env->SetLongField( object, env->GetFieldID(env->GetObjectClass(object), "sapiRecognizerPtr","J"), (long)recognizer);
		//recognizer->startdictation();

}


/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    handleDeallocate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_handleDeallocate
(JNIEnv *env, jobject object){
	
	/* get pointer sapiRecognizerPtr in JavaClass as long value and cast it*/
		Recognizer* recognizer = (Recognizer*)env->GetLongField(object,env->GetFieldID(env->GetObjectClass(object), "sapiRecognizerPtr","J"));
	
		recognizer->~Recognizer();

}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    handlePause
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_handlePause__
(JNIEnv *, jobject){
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    handlePause
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_handlePause__I
(JNIEnv *, jobject, jint){
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    handleResume
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_handleResume
(JNIEnv *, jobject){
	return false;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    setGrammars
 * Signature: (Ljava/util/Vector;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_setGrammars
(JNIEnv *env, jobject object, jobject grammar){

	/* get pointer sapiRecognizerPtr in JavaClass as long value and cast it*/
		Recognizer* recognizer = (Recognizer*)env->GetLongField(object,env->GetFieldID(env->GetObjectClass(object), "sapiRecognizerPtr","J"));
	
		recognizer->setGrammar();

	return false;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    getChangeRequestListener
 * Signature: ()Lorg/jvoicexml/jsapi2/EnginePropertyChangeRequestListener;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_getChangeRequestListener
  (JNIEnv *, jobject){
	return false;
}