#include "stdafx.h"
#include <org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer.h>
#include "Synthesizer.h"
#include <sperror.h>
#include "JNIUtils.h"

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    getSpeakable
 * Signature: (Ljava/lang/String;)Ljavax/speech/synthesis/Speakable;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_getSpeakable
(JNIEnv *, jobject, jstring)
{
	return NULL;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandleAllocate
 * Signature: (Ljavax/speech/synthesis/Voice;)J
 */
JNIEXPORT jlong JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandleAllocate
  (JNIEnv *env, jobject obj, jobject jvoice)
{
    const wchar_t* engineName;
    if (jvoice == NULL)
    {
        engineName = NULL;
    }
    else 
    {
        jclass voiceClass = env->GetObjectClass(jvoice);
        if (voiceClass == NULL)
        {
            ThrowJavaException(env, "java/lang/NullPointerException",
                "Unable to create javax/speech/synthesis/Voice!");
            return 0;
        }
        jmethodID voiceNameMethod = env->GetMethodID(voiceClass, "getName",
            "()Ljava/lang/String;");
        if (voiceNameMethod == NULL)
        {
            ThrowJavaException(env, "java/lang/NullPointerException",
                "Unable to get the getName method of javax/speech/synthesis/Voice!");
            return 0;
        }
        jstring name = (jstring) env->CallObjectMethod(jvoice, voiceNameMethod);
        if (name == NULL)
        {
            engineName = NULL;
        }
        else
        {
            engineName = (const wchar_t*)env->GetStringChars(name, NULL);
        }
    }

    HRESULT hr = ::CoInitializeEx(NULL, COINIT_MULTITHREADED);
    if (FAILED(hr))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Initializing COM failed!",
            hr);
        ThrowJavaException(env, "javax/speech/EngineException", buffer);
        return 0;
    }
	/* create new Synthesizer class */
	Synthesizer* synth = new Synthesizer(engineName);
    if (FAILED(synth->GetLastHResult()))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Allocation of synthesizer failed",
            synth->GetLastHResult());
        ThrowJavaException(env, "javax/speech/EngineException", buffer);
        return 0;
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
	boolean success = synth->Cancel();
    return success ? JNI_TRUE : JNI_FALSE;
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
    ::CoUninitialize();
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
    if (FAILED(synth->GetLastHResult()))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Pause recognizer failed",
            synth->GetLastHResult());
        ThrowJavaException(env, "javax/speech/EngineException", buffer);
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

	boolean success = synth->Resume();
    return success ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandleSpeak
 * Signature: (JILjava/lang/String;)V
 */
JNIEXPORT jbyteArray JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandleSpeak
  (JNIEnv *env, jobject obj, jlong handle, jint id, jstring item)
{
	Synthesizer* synth = (Synthesizer*) handle;

	/* get string and cast as const wchar_t* */
    const wchar_t* utterance = (const wchar_t*)env->GetStringChars(item, NULL);
		
    long size;
    byte* buffer = NULL;

	HRESULT hr = synth->Speak(utterance, size, buffer);
	if (FAILED(hr))
    {
        if (buffer != NULL)
        {
            delete[] buffer;
        }
        char msg[1024];
        GetErrorMessage(msg, sizeof(msg), "Speak failed", hr);
        ThrowJavaException(env, "javax/speech/synthesis/SpeakableException", msg);
        return NULL;
    }
    jbyteArray jb = env->NewByteArray(size);
    env->SetByteArrayRegion(jb, 0, size, (jbyte *)buffer);
    delete[] buffer;

    return jb;
}


/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiHandleSpeakSsml
 * Signature: (JILjava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandleSpeakSsml
  (JNIEnv *env, jobject onj, jlong handle, jint id, jstring markup)
{
	Synthesizer* synth = (Synthesizer*) handle;
	
	/* get string and cast as const wchar_t* */
    const wchar_t* utterance = (const wchar_t*)env->GetStringChars(markup, NULL);
    long size;
    byte* buffer = NULL;
	HRESULT hr = synth->SpeakSSML(utterance, size, buffer);
    if (FAILED(hr))
    {
        if (buffer != NULL)
        {
            delete[] buffer;
        }
        char msg[1024];
        GetErrorMessage(msg, sizeof(msg), "Speak SSML failed", hr);
        ThrowJavaException(env, "javax/speech/synthesis/SpeakableException", msg);
        return NULL;
    }
    jbyteArray jb = env->NewByteArray(size);
    env->SetByteArrayRegion(jb, 0, size, (jbyte *)buffer);
    delete[] buffer;

    return jb;
}

/*
 * Class:     org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer
 * Method:    sapiGetAudioFormat
 * Signature: (J)Ljavax/sound/sampled/AudioFormat;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiGetAudioFormat
  (JNIEnv *env, jobject object, jlong handle)
{
	Synthesizer* synth = (Synthesizer*) handle;
    //WAVEFORMATEX format;
    //HRESULT hr = synth->GetAudioFormat(format);
    //if (FAILED(hr))
    //{
    //    char buffer[1024];
    //    GetErrorMessage(buffer, sizeof(buffer), "GetAudioFormat failed", hr);
    //    jclass exception = env->FindClass("javax/speech/synthesis/SpeakableException");
    //    if (exception == 0) /* Unable to find the new exception class, give up. */
    //    {
    //        std::cerr << buffer << std::endl;
    //        return NULL;
    //    }
    //    env->ThrowNew(exception, buffer);
    //}
    jclass clazz = env->FindClass("javax/sound/sampled/AudioFormat");
    if (clazz == NULL)
    {
        ThrowJavaException(env, "javax/sound/sampled/AudioFormat",
            "Unable to create javax/sound/sampled/AudioFormat!");
        return NULL;
    }
    jmethodID constructor = env->GetMethodID(clazz, "<init>", "(FIIZZ)V");
    if (constructor == NULL)
    {
        ThrowJavaException(env, "java/lang/NullPointerException",
            "Constructor for javax/sound/sampled/AudioFormat not found!");
        return NULL;
    }
    //return env->NewObject(clazz, method, format.nSamplesPerSec,
    //    format.wBitsPerSample, format.nChannels, JNI_TRUE, JNI_TRUE);
    return env->NewObject(clazz, constructor, 22050.0,
        16, 1, JNI_TRUE, JNI_FALSE);
}
