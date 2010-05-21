#include "stdafx.h"
#include <org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer.h>
#include "Synthesizer.h"
#include <iostream>

void GetErrorMessage(char* buffer, size_t size, const char* text, HRESULT hr) 
{
    if (FormatMessageA(
        FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_IGNORE_INSERTS |
        FORMAT_MESSAGE_FROM_SYSTEM,
        NULL,
        hr,
        MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
        buffer,
        0,
        NULL) > 0)
    {
        sprintf_s(buffer, size, "%s: %s (0x%x)", text, buffer, hr);
    }
    else
    {
        sprintf_s(buffer, size, "%s: 0x%x", text, buffer, hr);
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
        jclass exception = env->FindClass("java/lang/NullPointerException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << buffer << std::endl;
            return 0;
        }
        env->ThrowNew(exception, buffer);
    }
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
	synth->Pause();
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

	/* get string and cast as const wchar_t* */
    const wchar_t* utterance = (const wchar_t*)env->GetStringChars(item, NULL);
	HRESULT hr = synth->Speak(utterance);
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
