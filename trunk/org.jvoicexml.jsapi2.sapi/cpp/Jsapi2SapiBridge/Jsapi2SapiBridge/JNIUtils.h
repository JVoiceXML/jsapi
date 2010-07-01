#pragma once

#include <jni.h>

void ThrowJavaException(JNIEnv* env, char* exceptionClassName, char* message);

BOOL GetMethodId(JNIEnv* env, const char* className, const char* methodName,
                 const char* sig, jclass& clazz, jmethodID& methodId);