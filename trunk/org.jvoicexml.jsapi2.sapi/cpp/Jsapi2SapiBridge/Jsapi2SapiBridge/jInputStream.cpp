#pragma once

#include "jInputStream.h"

// ---------------------------------------------------------------------------
// %%Function: CJavaInputStream::setJavaInputStream
// ---------------------------------------------------------------------------
 STDMETHODIMP
CJavaInputStream::setJavaInputStream(JNIEnv *env, jobject object) {
	/* the java environment */
	this->env = env;

	/* our InputStream on the javaside */
	inputStream = env->NewWeakGlobalRef(object);

	// prefetch and cache some MethodIDs for better performance
	jclass clazz = env->GetObjectClass(inputStream);

	//method header of "int InputStream.read(byte[] b, int off, int len)"
	// => "([BII)I"
	jReadByteArray = env->GetMethodID(clazz, "read", "([BII)I");
	if (jReadByteArray == NULL) {
		std::cerr << "Method 'int read(byte[], int, int)' not found!" << std::endl;
		return S_FALSE;
	}

	//method header of "int InputStream.read()"
	// => "()I"
	jReadByte = env->GetMethodID(clazz, "read", "()I");
	if (jReadByte == NULL) {
		std::cerr << "Method 'int read()' not found!" << std::endl;
		return S_FALSE;
	}
	
	jAvailable = env->GetMethodID(clazz, "available", "()I");
	if (jAvailable == NULL) {
		std::cerr << "Method 'int available()' not found!" << std::endl;
		return S_FALSE;
	}

	return S_OK;
} // CJavaInputStream::setJavaInputStream


// ---------------------------------------------------------------------------
// %%Function: CJavaInputStream::QueryInterface
// ---------------------------------------------------------------------------
 STDMETHODIMP
CJavaInputStream::QueryInterface(REFIID riid, void** ppv)
{
    if (ppv == NULL)
        return E_INVALIDARG;
	if (riid == IID_IUnknown) {
		std::clog << "QueryInterface: IUnknown" << std::endl;
		*ppv = static_cast<IUnknown*> (this);
        AddRef();
        return S_OK;
    }

	if (riid == IID_IStream) {
		std::clog << "QueryInterface: IStream" << std::endl;
		*ppv = static_cast<IStream*> (this);
		AddRef();
		return S_OK;
	}

	if (riid == IID_IJavaInputStream) {
		std::clog << "QueryInterface: IJavaInputStream" << std::endl;
		*ppv = static_cast<IJavaInputStream*> (this);
		AddRef();
		return S_OK;
	}

    *ppv = NULL;
    return E_NOINTERFACE;
}  // CJavaInputStream::QueryInterface


// ---------------------------------------------------------------------------
// %%Function: CJavaInputStream::Read
// ---------------------------------------------------------------------------
 STDMETHODIMP
CJavaInputStream::Read(void *pv, ULONG cb, ULONG *pcbRead)
{
	//std::cout << TEXT("IStream:Read") << std::endl;

	// if we have no connection to the jvm, return immediately
	if (jvm == NULL) {
		if (pcbRead) {
			*pcbRead = 0;
		}
		return S_FALSE;
	}

	// Attach the current thread to the running JVM
	// - this is needed, because thread(s) started by the recognizer are not connected to the JVM
	//		and the precached "env"-variable is only valid in the thread it was created
	int res = jvm->AttachCurrentThreadAsDaemon((void**)&env, NULL);

	//if the java environment has not been set, return immediately
	if (env == NULL) {
		if (pcbRead) {
			*pcbRead = 0;
		}
		return S_FALSE;
	}
	
	// look if enough bytes are available
	//	if not, wait some time for more data
	ULONG available = env->CallIntMethod(inputStream, jAvailable);
	int readTimeOut = 10; //50ms * 10 = 500ms timeout
	while (available < cb && readTimeOut > 0) {
		//std::cout << "Not enough data! Sleeping...";
		Sleep(50); //sleep 50ms
		//std::cout << "and awake!" << std::endl;
		readTimeOut--;
		available = env->CallIntMethod(inputStream, jAvailable);
	}

	
	// prepare internal buffer to get the requested bytes
	jbyteArray arr = env->NewByteArray(cb);
	jbyte* buf = (jbyte*)malloc(cb * sizeof(jbyte));

	// call InputStream.read(byte[], int offset, int len)
	//jvalue args[3];
	//args[0] = (jvalue)arr;
	//args[1] = (jvalue)0;
	//args[2] = (jvalue)cb;
	//jint bytesRead = env->CallIntMethodA(inputStream, jReadByteArray, args);
	//jint cbJInt = (jint)cb;
	jint bytesRead = env->CallIntMethod(inputStream, jReadByteArray, arr, 0, (jint)cb);

	// check if we have bytes in our puffer
	if (bytesRead == -1) {
		// InputStream's EOF
		//	 ref: http://msdn.microsoft.com/en-us/library/aa380011%28v=VS.85%29.aspx
		std::cout << "discovered EOF!" << std::endl;
		env->ReleaseByteArrayElements(arr, NULL, JNI_ABORT);
		if (pcbRead) {
			*pcbRead = 0;
		}
		return S_FALSE;
	}

	// read the elt's in our cpp-compliant array "buf"
	env->GetByteArrayRegion(arr, 0, bytesRead, buf);

	// give the elt's back to the callee
	memcpy(pv, buf, bytesRead);
	env->ReleaseByteArrayElements(arr, NULL, JNI_ABORT);
	
	if (pcbRead) {
		*pcbRead = bytesRead;
	}
	if (bytesRead == cb) {
		return S_OK;
	} else {
		return S_FALSE;
	}
}  // CJavaInputStream::Read

//// ---------------------------------------------------------------------------
//// %%Function: CJavaInputStream::Write
//// ---------------------------------------------------------------------------
// STDMETHODIMP
//CJavaInputStream::Write(VOID const *pv, ULONG cb, ULONG *pcbWritten)
//{
//	return E_FAIL;
//}  // CJavaInputStream::Write

// ---------------------------------------------------------------------------
// %%Function: CClassFactory::QueryInterface
// ---------------------------------------------------------------------------
 STDMETHODIMP
CClassFactory::QueryInterface(REFIID riid, void** ppv)
{
    if (ppv == NULL)
        return E_INVALIDARG;
    if (riid == IID_IClassFactory || riid == IID_IUnknown) {
        *ppv = (IClassFactory *) this;
        AddRef();
        return S_OK;
    }
    *ppv = NULL;
    return E_NOINTERFACE;
}  // CClassFactory::QueryInterface

// ---------------------------------------------------------------------------
// %%Function: CClassFactory::CreateInstance
// ---------------------------------------------------------------------------
 STDMETHODIMP
CClassFactory::CreateInstance(LPUNKNOWN punkOuter, REFIID riid, void** ppv)
{
    LPUNKNOWN   punk;
    HRESULT     hr;

    *ppv = NULL;

    if (punkOuter != NULL)
        return CLASS_E_NOAGGREGATION;

	std::cout << "IClassFactory:CreateInstance" << std::endl;

    punk = new CJavaInputStream;

    if (punk == NULL)
        return E_OUTOFMEMORY;

    hr = punk->QueryInterface(riid, ppv);
    return hr;
}  // CClassFactory::CreateInstance