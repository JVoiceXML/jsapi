#include "stdafx.h"
#include "org_jvoicexml_jsapi2_sapi_SapiEngineListFactory.h"
#include "Synthesizer.h"
#include "JNIUtils.h"

JNIEXPORT jobjectArray JNICALL Java_org_jvoicexml_jsapi2_sapi_SapiEngineListFactory_sapiGetVoices
  (JNIEnv *env, jobject obj)
{
    HRESULT hr = ::CoInitializeEx(NULL, COINIT_MULTITHREADED);
    if (FAILED(hr))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Initializing COM failed!",
            hr);
        ThrowJavaException(env, "java/lang/NullPointerException", buffer);
        return NULL;
    }
    Voice* voices = NULL;
    ULONG num;
    Synthesizer::ListVoices(voices, num);
    ::CoUninitialize();

    jclass clazz = env->FindClass("javax/speech/synthesis/Voice");
    if (clazz == NULL)
    {
        char* msg = "Unable to create javax/speech/synthesis/Voice!";
        jclass exception = env->FindClass("java/lang/NullPointerException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << msg << std::endl;
            return NULL;
        }
        env->ThrowNew(exception, msg);
        return NULL;
    }
    jmethodID constructor = env->GetMethodID(clazz, "<init>",
        "(Ljavax/speech/SpeechLocale;Ljava/lang/String;III)V");
    if (constructor == NULL)
    {
        char* msg = "Constructor for javax/speech/synthesis/Voice not found!";
        jclass exception = env->FindClass("java/lang/NullPointerException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << msg << std::endl;
            return NULL;
        }
        env->ThrowNew(exception, msg);
        return NULL;
    }

    jobjectArray jvoices = env->NewObjectArray(num, clazz, NULL);
    if (jvoices == NULL)
    {
        char* msg = "Error creating the voices array!";
        jclass exception = env->FindClass("java/lang/NullPointerException");
        if (exception == 0) /* Unable to find the new exception class, give up. */
        {
            std::cerr << msg << std::endl;
            return NULL;
        }
        env->ThrowNew(exception, msg);
        return NULL;
    }
    for (ULONG i=0; i<num; i++)
    {
        WCHAR* voiceName = voices[i].GetName();
        jstring name = env->NewString((jchar*)voiceName, wcslen(voiceName));
        jobject voice = env->NewObject(clazz, constructor, NULL, name, -1, -1, -1);
        env->SetObjectArrayElement(jvoices, i, voice);
    }

    if (voices != NULL)
    {
        delete[] voices;
    }
    return jvoices;
}

