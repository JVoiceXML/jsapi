#include "include/org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer.h"
#include "JNIUtils.h"
#include <wchar.h>
#import <Carbon/Carbon.h>
#import <ApplicationServices/ApplicationServices.h>
#include <iostream>
#include <unistd.h>

/*
 * Class:     org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer
 * Method:    macHandleAllocate
 * Signature: (Ljavax/speech/synthesis/Voice;)J
 */
JNIEXPORT jlong JNICALL Java_org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer_macHandleAllocate
(JNIEnv* env, jobject object, jobject jvoice) {
  std::cout << "macHandleAllocate" << std::endl;
  
  // NULL will cause GetVoiceDescription to return the system default
  VoiceSpec	voiceSpec;
  OSErr theErr = noErr;

  if (jvoice != NULL) {
    // determine name of voice
    jclass voiceClass = env->GetObjectClass(jvoice);
    if (voiceClass == NULL) {
      ThrowJavaException(env, "java/lang/NullPointerException", "Unable to create javax/speech/synthesis/Voice!");
      return 0;
    }
    jmethodID voiceNameMethod = env->GetMethodID(voiceClass, "getName", "()Ljava/lang/String;");
    if (voiceNameMethod == NULL) {
      ThrowJavaException(env, "java/lang/NullPointerException", "Unable to get the getName method of javax/speech/synthesis/Voice!");
      return 0;
    }
    jstring name = (jstring) env->CallObjectMethod(jvoice, voiceNameMethod);
    
    // Carbon specific part
    if (name != NULL) {
      char* voiceName = GetStringNativeChars(env, name);
      
      short numOfVoices;
      theErr = CountVoices(&numOfVoices);
      
      u_int voiceIndex;
      for (voiceIndex = 1; voiceIndex <= numOfVoices; voiceIndex++) {
        VoiceDescription tmpVDesc;
        VoiceSpec	tmpVSpec;
        
        theErr = GetIndVoice(voiceIndex, &tmpVSpec);
        theErr = GetVoiceDescription(&tmpVSpec, &tmpVDesc, sizeof(tmpVDesc));
        
        char* currName = new char[64];
        snprintf(currName, 64, "%s", &(tmpVDesc.name[1]));
        
        if (strcmp(currName, voiceName) == 0) {
          //std::cout << "Found voice " << currName << std::endl;
          voiceSpec = tmpVSpec;
        }
      }
    }
  }
  
  // create a new speech channel
  SpeechChannel* chan = (SpeechChannel*)malloc(sizeof(SpeechChannel));
  theErr = NewSpeechChannel(&voiceSpec, chan);
  return (jlong) chan;
}

/*
 * Class:     org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer
 * Method:    macHandleCancel
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer_macHandleCancel__J
(JNIEnv* env, jobject object, jlong handle) {
  std::cout << "macHandleCancel" << std::endl;
}


/*
 * Class:     org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer
 * Method:    macHandleCancel
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer_macHandleCancel__JI
(JNIEnv* env, jobject object, jlong handle, jint id) {
  std::cout << "macHandleCancel" << std::endl;
}


/*
 * Class:     org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer
 * Method:    macHandleCancelAll
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer_macHandleCancelAll
(JNIEnv* env, jobject object, jlong handle) {
  std::cout << "macHandleCancelAll" << std::endl;
}


/*
 * Class:     org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer
 * Method:    macHandlDeallocate
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer_macHandlDeallocate
(JNIEnv* env, jobject object, jlong handle) {
  std::cout << "macHandlDeallocate" << std::endl;
}


/*
 * Class:     org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer
 * Method:    macHandlePause
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer_macHandlePause
(JNIEnv* env, jobject object, jlong handle) {
  std::cout << "macHandlePause" << std::endl;
}


/*
 * Class:     org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer
 * Method:    macHandlResume
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer_macHandlResume
(JNIEnv* env, jobject object, jlong handle) {
  std::cout << "macHandlResume" << std::endl;
}


/*
 * Class:     org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer
 * Method:    macHandleSpeak
 * Signature: (JILjava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer_macHandleSpeak
(JNIEnv* env, jobject object, jlong handle, jint id, jstring item) {
  std::cout << "macHandleSpeak" << std::endl;
  
  SpeechChannel* chan = (SpeechChannel*) handle;
  OSErr ok;
  long size;
  
  const char* textBuf = GetStringNativeChars(env, item);
  size = strlen(textBuf);
  
  ok = SpeakText((*chan), textBuf, strlen(textBuf));
  
  jbyteArray jb = env->NewByteArray(1000);
  return jb;
}


/*
 * Class:     org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer
 * Method:    macHandleSpeakSsml
 * Signature: (JILjava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer_macHandleSpeakSsml
(JNIEnv* env, jobject object, jlong handle, jint id, jstring ssml) {
  std::cout << "macHandleSpeakSsml" << std::endl;
}


/*
 * Class:     org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer
 * Method:    macGetAudioFormat
 * Signature: (J)Ljavax/sound/sampled/AudioFormat;
 */
JNIEXPORT jobject JNICALL Java_org_jvoicexml_jsapi2_mac_synthesis_MacSynthesizer_macGetAudioFormat
(JNIEnv* env, jobject object, jlong handle) {
  std::cout << "macGetAudioFormat" << std::endl;
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


