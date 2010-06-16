#include "stdafx.h"
#include <org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer.h>
#include "Synthesizer.h"
#include <sperror.h>


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
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_org_jvoicexml_jsapi2_sapi_synthesis_SapiSynthesizer_sapiHandleAllocate
  (JNIEnv *env, jobject obj, jstring string)
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
        return NULL;
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
        jclass exception = env->FindClass("javax/speech/synthesis/SpeakableException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << msg << std::endl;
            return NULL;
        }
        env->ThrowNew(exception, msg);
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
        jclass exception = env->FindClass("javax/speech/synthesis/SpeakableException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << msg << std::endl;
            return NULL;
        }
        env->ThrowNew(exception, msg);
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
        char* msg = "Unable to create javax/sound/sampled/AudioFormat!";
        jclass exception = env->FindClass("java/lang/NullPointerException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << msg << std::endl;
            return NULL;
        }
        env->ThrowNew(exception, msg);
        return NULL;
    }
    jmethodID constructor = env->GetMethodID(clazz, "<init>", "(FIIZZ)V");
    if (constructor == NULL)
    {
        char* msg = "Constructor for javax/sound/sampled/AudioFormat not found!";
        jclass exception = env->FindClass("java/lang/NullPointerException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << msg << std::endl;
            return NULL;
        }
        env->ThrowNew(exception, msg);
        return NULL;
    }
    //return env->NewObject(clazz, method, format.nSamplesPerSec,
    //    format.wBitsPerSample, format.nChannels, JNI_TRUE, JNI_TRUE);
    return env->NewObject(clazz, constructor, 22050.0,
        16, 1, JNI_TRUE, JNI_FALSE);
}
