#include "stdafx.h"
#include "org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer.h"
#include "Recognizer.h"
#include "JNIUtils.h"
#include <log4cplus/loggingmacros.h>

// wraps a java InputStream in an IStream
#include "jInputStream.h"

//static initializations
static log4cplus::Logger logger =
    log4cplus::Logger::getInstance(_T("org.jvoicexml.sapi.cpp.SapiRecognizer"));

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
    LOG4CPLUS_DEBUG(logger, "allocated");
    return (jlong) recognizer;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    sapiSetRecognizerInputStream
 * Signature: (JLjava/io/InputStream;FIIZZLjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiSetRecognizerInputStream
  (JNIEnv *env, jobject caller, jlong recognizerHandle, jobject inputStream, 
	jfloat sampleRate, jint bitsPerSample, jint channels, jboolean endian, jboolean signedData, jstring encoding)  {
		
		Recognizer* recognizer = (Recognizer*)recognizerHandle;
		HRESULT hr;

		/* create instance of JInputStream */
		CComPtr<IStream> jStream;
		hr = jStream.CoCreateInstance(CLSID_JInputStream);

		/* query Setter-Interface */
		CComPtr<IJavaInputStream> jStreamSetter;
		if (SUCCEEDED(hr)) {
			hr = jStream->QueryInterface(IID_IJavaInputStream, (void**) &jStreamSetter);
		}
		/* setup our IStream with the given Java InputStream as it's source */
		if (SUCCEEDED(hr)) {
			hr = jStreamSetter->setJavaInputStream(env, inputStream);
		}

		/* create ISpStream for the recognizer */
		CComPtr<ISpStream> cpSpStream;
		if (SUCCEEDED(hr)) {
			hr = cpSpStream.CoCreateInstance(CLSID_SpStream);
		}

		/* set WAVEFORMATEX accordingly */
		// see http://msdn.microsoft.com/en-us/library/ms720517%28v=VS.85%29.aspx
		WAVEFORMATEX *format;
		format = (WAVEFORMATEX*)malloc(sizeof(WAVEFORMATEX));
		format->wFormatTag = WAVE_FORMAT_PCM;	//constant
		format->nChannels = channels;			//variable
		format->nSamplesPerSec = sampleRate;	//variable
		format->wBitsPerSample = bitsPerSample;	//variable
		format->nBlockAlign = (format->wBitsPerSample * format->nChannels) / 8;	//constant
		format->nAvgBytesPerSec = format->nSamplesPerSec * format->nBlockAlign;	//constant
		format->cbSize = 0;	//constant

		/* set the Java IStream and it's format as the source for the SpeechStream */
		if (SUCCEEDED(hr)) {
			hr = cpSpStream->SetBaseStream(jStream, SPDFID_WaveFormatEx, format);
		}

		/* set the constructed SpeechStream as the new recognizerInput */
		/* NOTE:
		 *	The RecognizerContext must be paused before the InputStream-switch.
		 *	This responsibility lies on the Java-side!
		 */
		if (SUCCEEDED(hr)) {
			hr = recognizer->SetRecognizerInputStream(cpSpStream);
		}
		int limitSetInput = 5;
		for (int i = 0; (hr == SPERR_ENGINE_BUSY) && i < limitSetInput; i++) {
            LOG4CPLUS_DEBUG(logger, _T("=> CPP setInputCounter:") << i);
			Sleep(10);
			hr = recognizer->SetRecognizerInputStream(cpSpStream);
		}

		if (SUCCEEDED(hr)) {
			return JNI_TRUE;
		} else {
			//insert ERROR-Logging here
			LOG4CPLUS_ERROR(logger, "CPP: Error setting a new InputStream! ErrorCode: 0x" << std::hex << std::uppercase << hr);
			return JNI_FALSE;
		}
}
 
/*
 * Class:     org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer
 * Method:    nativHandleDeallocate
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_recognition_SapiRecognizer_sapiDeallocate
  (JNIEnv *env, jobject object, jlong recognizerHandle) {

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
	HRESULT hr = recognizer->Pause();
	
	if (FAILED(hr))
    {
		LOG4CPLUS_ERROR(logger, "Could not pause the recognizer!");
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
	//HRESULT hr = recognizer->Pause();
    if ( FAILED(hr) )
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer),
            "Abort Recognition failed",
            hr);
        ThrowJavaException(env, "javax/speech/EngineStateException", buffer);
    }
}
