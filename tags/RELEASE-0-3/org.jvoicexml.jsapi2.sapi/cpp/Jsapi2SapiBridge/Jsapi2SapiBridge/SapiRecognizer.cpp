#include "stdafx.h"
#include "org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer.h"
#include "Recognizer.h"
#include "JNIUtils.h"


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
  (JNIEnv *env, jobject object)
{
    /* create new Recognizer class */
    Recognizer* recognizer = new Recognizer(hWnd, env, object);
    /* check if Handle valid */
    if (FAILED(recognizer->hr))
    {      
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Allocation of recognizer failed",
            recognizer->hr);
        ThrowJavaException(env, "javax/speech/EngineException", buffer);
        return 0;
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
	recognizer->Pause();
	
	if (FAILED(recognizer->hr))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Pause recognizer failed",
            recognizer->hr);
        ThrowJavaException(env, "javax/speech/EngineException", buffer);
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
 * Method:    sapiResume
 * Signature: (J[Ljava/lang/String;[Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiResume
(JNIEnv *env, jobject object, jlong handle, jobjectArray grammars, jobjectArray references)
{

	Recognizer* recognizer = (Recognizer*)handle;
    jsize size = env->GetArrayLength(grammars);
    if (size == 0)
    {
        return JNI_TRUE;
    }
    for (jsize i=0; i<size; i++)
    {
        jstring grammar = (jstring) env->GetObjectArrayElement(grammars, i);
		const wchar_t* gram = (const wchar_t*)env->GetStringChars(grammar, NULL);	
        
		jstring reference = (jstring) env->GetObjectArrayElement(references, i);
		const wchar_t* ref = (const wchar_t*)env->GetStringChars(reference, NULL);	

		//HRESULT hr= recognizer->LoadGrammarFile(gram, ref);
		HRESULT hr= recognizer->LoadGrammar(gram, ref);
        if ( FAILED(hr) )
        {
            char buffer[1024];
			GetErrorMessage(buffer, sizeof(buffer),
                "Resume Recognizer: load Grammar failed",
                hr);
            ThrowJavaException(env, "javax/speech/EngineStateException", buffer);
        }
    }
	
	return recognizer->Resume();
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    sapiSetGrammar
 * Signature: (JLjava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiSetGrammar
(JNIEnv *env, jobject object, jlong recognizerHandle, jstring grammarPath, jstring reference){
		
	Recognizer* recognizer = (Recognizer*)recognizerHandle;
    const wchar_t* gram = (const wchar_t*)env->GetStringChars(grammarPath, NULL);
	const wchar_t* ref = (const wchar_t*)env->GetStringChars(reference, NULL);	
	
	HRESULT hr = recognizer->LoadGrammarFile(gram, ref);
	if (SUCCEEDED(hr))
    {
        return JNI_TRUE;
	}
	else
    {
		return JNI_FALSE;
	}
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    sapiSetGrammarContent
 * Signature: (JLjava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiSetGrammarContent
(JNIEnv *env, jobject object, jlong recognizerHandle, jstring grammarContent, jstring reference)
{
		
	Recognizer* recognizer = (Recognizer*)recognizerHandle;
    const wchar_t* grammar = (const wchar_t*)env->GetStringChars(grammarContent, NULL);
	const wchar_t* ref = (const wchar_t*)env->GetStringChars(reference, NULL);	
	
	HRESULT hr = recognizer->LoadGrammar(grammar, ref);

	if (SUCCEEDED(hr))
    {
        return JNI_TRUE;
	}
	else
    {
		return JNI_FALSE;
	}
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    getChangeRequestListener
 * Signature: ()Lorg/jvoicexml/jsapi2/EnginePropertyChangeRequestListener;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_getChangeRequestListener
 (JNIEnv *env, jobject object)
{
	return NULL;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    start
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_start
(JNIEnv *env, jobject object, jlong recognizerHandle)
{
	reinterpret_cast<Recognizer*>(recognizerHandle)->StartDictation();	
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    sapiRecognize
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiRecognize
  (JNIEnv *env, jobject object, jlong handle)
{

    Recognizer* recognizer = (Recognizer*) handle;
	WCHAR* resTmp[2];
	resTmp[0] = NULL; resTmp[1] = NULL;
	HRESULT hres = recognizer->StartRecognition(resTmp);
	if (FAILED(hres) || NULL == resTmp[0]) {
		return NULL;
	}
	//return env->NewString((jchar*)result, wcslen(result));
	jobjectArray result;
	result = (jobjectArray) env->NewObjectArray(2,
									env->FindClass("java/lang/String"), 
									env->NewString((jchar*)&"", 0));
	env->SetObjectArrayElement(result, 0, env->NewString((jchar*)resTmp[0], wcslen(resTmp[0]))); //ruleName
	env->SetObjectArrayElement(result, 1, env->NewString((jchar*)resTmp[1], wcslen(resTmp[1]))); //utterance
    return result;
}


/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    sapiAbortRecognition
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiAbortRecognition
(JNIEnv *env, jobject object, jlong handle)
{

	Recognizer* recognizer = (Recognizer*) handle;
	
	HRESULT hr = recognizer->AbortRecognition();
    if ( FAILED(hr) )
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer),
            "Abort Recognition failed",
            hr);
        ThrowJavaException(env, "javax/speech/EngineStateException", buffer);
    }
}