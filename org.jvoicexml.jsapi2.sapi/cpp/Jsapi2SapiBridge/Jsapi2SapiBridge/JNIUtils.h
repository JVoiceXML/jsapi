#pragma once

#include <jni.h>

void ThrowJavaException(JNIEnv* env, char* exceptionClassName, char* message);
