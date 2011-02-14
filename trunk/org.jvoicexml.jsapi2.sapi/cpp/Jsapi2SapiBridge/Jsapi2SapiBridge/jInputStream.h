#pragma once

#include <initguid.h>
#include <windows.h>
#include <stdio.h>
#include <iostream>
#include <jni.h>

#include <log4cplus/logger.h>

extern JavaVM *jvm;

// %%GUIDs: ------------------------------------------------------------------
DEFINE_GUID(CLSID_JInputStream, 0x5e9ddec7, 0x5767, 0x11cf, 0xbe, 0xab, 0x0, 0xaa, 0x0, 0x6c, 0x36, 0x6);

// {F1D3E19D-8D32-4ef6-A656-99E242E31098}
DEFINE_GUID(IID_IJavaInputStream,0xf1d3e19d, 0x8d32, 0x4ef6, 0xa6, 0x56, 0x99, 0xe2, 0x42, 0xe3, 0x10, 0x98);


// %%Classes: ----------------------------------------------------------------
// simple class-factory: only knows how to create CJavaInputStream instances
class CClassFactory : public IClassFactory {
  public:
    // IUnknown
    STDMETHODIMP    QueryInterface (REFIID riid, void** ppv);
    STDMETHODIMP_(ULONG) AddRef(void)  { return 1; };
    STDMETHODIMP_(ULONG) Release(void) { return 1; };

    // IClassFactory
    STDMETHODIMP    CreateInstance (LPUNKNOWN punkOuter, REFIID iid, void **ppv);
    STDMETHODIMP    LockServer (BOOL fLock) { return E_FAIL; };
    };

class IJavaInputStream : public IStream {
  public:
	// IUnknown
	virtual STDMETHODIMP    QueryInterface (REFIID riid, void** ppv) {
		std::cout << "Call JavaInputStream->QueryInterface()" << std::endl;
		return 0;};
    virtual STDMETHODIMP_(ULONG) AddRef(void) {
		std::cout << "Call JavaInputStream->AddRef()" << std::endl;
		return 1;};
    virtual STDMETHODIMP_(ULONG) Release(void) {
		std::cout << "Call JavaInputStream->Release()" << std::endl;
		return 1;};

	// IJavaInputStream
	virtual STDMETHODIMP setJavaInputStream(JNIEnv *env, jobject object) {
	  std::cout << "Call JavaInputStream->Set()" << std::endl;
	  return S_OK;
	};
  public:
    JNIEnv*		env;		// jvm-env holding our InputStream
	jobject		inputStream;	// the Java InputStream
	jmethodID	jReadByteArray;	// methodID of "int read(byte[], int, int)"
	jmethodID	jReadByte;		// methodID of "int read()"
	jmethodID	jAvailable;		// methodID of "int available()"
};

// Java streaming class supporting a dummy IStream
class CJavaInputStream : public IJavaInputStream {
  public:
    // IUnknown
    STDMETHODIMP    QueryInterface (REFIID iid, void **ppv);
	STDMETHODIMP_(ULONG) AddRef(void)  { std::cout << std::endl << "References: " << (m_cRef+1) << std::endl; return InterlockedIncrement(&m_cRef); };
	STDMETHODIMP_(ULONG) Release(void) { std::cout << std::endl << "References: " << (m_cRef-1) << std::endl; if (InterlockedDecrement(&m_cRef) == 0) { delete this; return 0; } return 1; }

    // IStream
    STDMETHODIMP    Read(void *pv, ULONG cb, ULONG *pcbRead);
    STDMETHODIMP    Write(VOID const *pv, ULONG cb, ULONG *pcbWritten)
		{ std::cout << "Call Stream->Write" << std::endl;
		return E_FAIL; };
    STDMETHODIMP    Seek(LARGE_INTEGER dbMove, DWORD dwOrigin, ULARGE_INTEGER *pbNewPosition)
		{ std::cout << "Call Stream->Seek" << std::endl;
		if (dbMove.HighPart == 0 && dbMove.LowPart == 0)
			return S_OK;
		else
			return E_FAIL; };
    STDMETHODIMP    SetSize(ULARGE_INTEGER cbNewSize)
        { std::cout << "Call Stream->SetSize" << std::endl;
		return E_FAIL; };
    STDMETHODIMP    CopyTo(IStream *pstm, ULARGE_INTEGER cb, ULARGE_INTEGER *pcbRead, ULARGE_INTEGER *pcbWritten)
        { std::cout << "Call Stream->CopyTo" << std::endl;
		return E_FAIL; };
    STDMETHODIMP    Commit(DWORD grfCommitFlags)
        { std::cout << "Call Stream->Commit" << std::endl;
		return E_FAIL; };
    STDMETHODIMP    Revert(void)
        { std::cout << "Call Stream->Revert" << std::endl;
		return E_FAIL; };
    STDMETHODIMP    LockRegion(ULARGE_INTEGER bOffset, ULARGE_INTEGER cb, DWORD dwLockType)
        { std::cout << "Call Stream->LockRegion" << std::endl;
		return E_FAIL; };
    STDMETHODIMP    UnlockRegion(ULARGE_INTEGER bOffset, ULARGE_INTEGER cb, DWORD dwLockType)
        { std::cout << "Call Stream->UnlockRegion" << std::endl;
		return E_FAIL; };
    STDMETHODIMP    Stat(STATSTG *pstatstg, DWORD grfStatFlag)
        { std::cout << "Call Stream->Stat" << std::endl;
		return E_FAIL; };
    STDMETHODIMP    Clone(IStream **ppstm)
        { std::cout << "Call Stream->Clone" << std::endl;
		return E_FAIL; };

	// JavaInputStream
	STDMETHODIMP setJavaInputStream(JNIEnv *env, jobject object);
    // constructors/destructors
    CJavaInputStream()     { m_cRef = 0; env = NULL;}
    //virtual ~CJavaInputStream()    { ; }
	virtual ~CJavaInputStream();

  private:
    LONG        m_cRef;		//reference counter
};