#include "stdafx.h"
#include "JNIUtils.h"

void ThrowJavaException(JNIEnv* env, char* exceptionClassName, char* message)
{
    jclass exception = env->FindClass(exceptionClassName);
    if (exception == 0) /* Unable to find the new exception class, give up. */
    {
        std::cerr << message << std::endl;
        return;
    }
    env->ThrowNew(exception, message);
}
