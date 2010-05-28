#include "stdafx.h"
#include "org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer.h"
#include "Recognizer.h"


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
JNIEXPORT jlong JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiAllocate
  (JNIEnv *env, jobject object){

	/* create new Recognizer class */
	  Recognizer* recognizer =new Recognizer();
	  
	/* check if Handle valid */
	if (FAILED(recognizer->hr))
    {      
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Allocation of recognizer failed",
            recognizer->hr);
        jclass exception = env->FindClass("javax/speech/EngineException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << buffer << std::endl;
			fflush(stdout);
            return 0;
        }
        env->ThrowNew(exception, buffer);
    }

    return (jlong) recognizer;	
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandleDeallocate
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiDeallocate
  (JNIEnv *env, jobject object, jlong recognizerHandle){

	Recognizer* recognizer = (Recognizer*)recognizerHandle;
	delete recognizer;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandlePause
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiPause__J
  (JNIEnv *env, jobject object, jlong recognizerHandle){
		
	Recognizer* recognizer = (Recognizer*)recognizerHandle;
	recognizer->pause();
	
	if (FAILED(recognizer->hr))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Pause recognizer failed",
            recognizer->hr);
        jclass exception = env->FindClass("javax/speech/EngineException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << buffer << std::endl;
        }
        env->ThrowNew(exception, buffer);
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

	Recognizer* recognizer = (Recognizer*)recognizerHandle;
	recognizer->resume();	
			
	if( SUCCEEDED(recognizer->hr) ){
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
		
	Recognizer* recognizer = (Recognizer*)recognizerHandle;
	recognizer->setGrammar( (const wchar_t*)env->GetStringChars( grammarPath, NULL) );

	if( SUCCEEDED(recognizer->hr) ){
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

