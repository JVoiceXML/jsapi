#include "stdafx.h"
#include <org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer.h>
#include "Synthesizer.h"
#include <sperror.h>

// alternative look-up error codes returned by sapi in the file sperror.h
void GetErrorMessage(char* buffer, size_t size, const char* text, HRESULT hr) 
{	
	LPSTR pMassage = NULL;
	DWORD length = -1;
	
	length = FormatMessageA(
							FORMAT_MESSAGE_ALLOCATE_BUFFER  |
							FORMAT_MESSAGE_FROM_HMODULE |
							FORMAT_MESSAGE_FROM_SYSTEM	|
							FORMAT_MESSAGE_IGNORE_INSERTS |
							FORMAT_MESSAGE_MAX_WIDTH_MASK,
							GetModuleHandle(_T("SAPI.dll")),
							hr,
							MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
							(LPSTR)pMassage,
							0,
							NULL);
    if ( length > 0 )
    {		
		sprintf_s( buffer, length, "%s. %s: (%#lX)", text, pMassage, hr);	
		LocalFree(pMassage);
	}
    else
    {
		MAKE_SAPI_ERROR(hr);

		sprintf_s(buffer, size, "%s. MS Sapi5 ErrorCode: %#lX", text, hr);
    }	
}


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
 * Method:    sapiHandleAllocate
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandleAllocate
  (JNIEnv * env, jobject obj, jstring string)
{
    const wchar_t* engineName = (const wchar_t*)env->GetStringChars(string, NULL);
 
	/* create new Synthesizer class */
	Synthesizer* synth = new Synthesizer(engineName);		
    if (FAILED(synth->hr))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Allocation of synthesizer failed",
            synth->hr);
        jclass exception = env->FindClass("javax/speech/EngineException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << buffer << std::endl;
            return 0;
        }
        env->ThrowNew(exception, buffer);
    }

	//std::cout << "Allocate :: synthesizerHandle to Java:" << synth << std::endl;
	//fflush(stdout);
    return (jlong) synth;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandleCancel
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandleCancel__J
  (JNIEnv *env, jobject obj, jlong handle)
{
	Synthesizer* synth = (Synthesizer*) handle;
	return synth->Cancel();
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandleCancel
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandleCancel__JI
  (JNIEnv * env, jobject obj, jlong handle, jint id)
{
	return NULL;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandleCancelAll
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandleCancelAll
  (JNIEnv * env, jobject obj, jlong handle)
{
	return NULL;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandlDeallocate
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandlDeallocate
  (JNIEnv * env, jobject obj, jlong handle)
{
	Synthesizer* synthesizer = (Synthesizer*) handle;
    
	//std::cout << "Deallocate :: synthesizerHandle from Java:" << synthesizer << std::endl;
	//fflush(stdout);

	delete synthesizer;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandlPause
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandlPause
  (JNIEnv *env, jobject obj, jlong handle)
{
	Synthesizer* synth = (Synthesizer*) handle;	
	
	//std::cout << "Pause :: synthesizerHandle from Java:" << synth << std::endl;
	//fflush(stdout);

	synth->Pause();

	if (FAILED(synth->hr))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Pause recognizer failed",
            synth->hr);
        jclass exception = env->FindClass("javax/speech/EngineException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << buffer << std::endl;
        }
        env->ThrowNew(exception, buffer);
    }

}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandlResume
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandlResume
  (JNIEnv *env, jobject obj, jlong handle)
{	
	Synthesizer* synth = (Synthesizer*) handle;	

	//std::cout << "Resume:: synthesizerHandle from Java:" << synth << std::endl;
	//fflush(stdout);

	return synth->Resume();
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandleSpeak
 * Signature: (JILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandleSpeak
  (JNIEnv *env, jobject obj, jlong handle, jint id, jstring item)
{
	Synthesizer* synth = (Synthesizer*) handle;

	//std::cout << "Speak :: synthesizerHandle from Java:" << synth << std::endl;
	//fflush(stdout);

	/* get string and cast as const wchar_t* */
    const wchar_t* utterance = (const wchar_t*)env->GetStringChars(item, NULL);
		
	HRESULT hr = synth->Speak(utterance);

	//std::cout << "speak okay" << utterance << std::endl;
	//fflush(stdout);

	//env->ReleaseStringChars(item, (const jchar*)utterance);
	//
	//std::cout << "release okay" << utterance << std::endl;
	//fflush(stdout);
   
	if (FAILED(hr))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Speak failed", hr);
        jclass exception = env->FindClass("javax/speech/synthesis/SpeakableException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << buffer << std::endl;
            return;
        }
        env->ThrowNew(exception, buffer);
    }
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandleSpeakSsml
 * Signature: (JILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandleSpeakSsml
  (JNIEnv *env, jobject obj, jlong handle, jint id, jstring markup)
{
	Synthesizer* synth = (Synthesizer*) handle;
	
	/* get string and cast as const wchar_t* */
    const wchar_t* utterance = (const wchar_t*)env->GetStringChars(markup, NULL);
	HRESULT hr = synth->SpeakSSML(utterance);
    if (FAILED(hr))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Speak SSML failed", hr);
        jclass exception = env->FindClass("javax/speech/synthesis/SpeakableException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << buffer << std::endl;
            return;
        }
        env->ThrowNew(exception, buffer);
    }
}
