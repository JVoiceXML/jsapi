#pragma once

#include <initguid.h>
#include <windows.h>
#include <stdio.h>
#include <iostream>
#include <jni.h>

#include <log4cplus/loggingmacros.h>
#include <log4cplus/logger.h>

extern JavaVM *jvm;

// %%GUIDs: ------------------------------------------------------------------
DEFINE_GUID(CLSID_JInputStream, 0x5e9ddec7, 0x5767, 0x11cf, 0xbe, 0xab, 0x0, 0xaa, 0x0, 0x6c, 0x36, 0x6);

// {F1D3E19D-8D32-4ef6-A656-99E242E31098}
DEFINE_GUID(IID_IJavaInputStream,0xf1d3e19d, 0x8d32, 0x4ef6, 0xa6, 0x56, 0x99, 0xe2, 0x42, 0xe3, 0x10, 0x98);


// %%Classes: ----------------------------------------------------------------
// simple class-factory: only knows how to create CJavaInputStream instances
class CClassFactory : public IClassFactory
{
public:
    // IUnknown
    STDMETHODIMP QueryInterface (REFIID riid, void** ppv);
    STDMETHODIMP_(ULONG) AddRef(void)  { return 1; };
    STDMETHODIMP_(ULONG) Release(void) { return 1; };

    // IClassFactory
    STDMETHODIMP    CreateInstance (LPUNKNOWN punkOuter, REFIID iid, void **ppv);
    STDMETHODIMP    LockServer (BOOL fLock) { return E_FAIL; };
private:
    /** Logger instance. */
    static log4cplus::Logger logger;
};

class IJavaInputStream : public IStream
{
  public:
	// IUnknown
	virtual STDMETHODIMP    QueryInterface (REFIID riid, void** ppv) 
    {
		std::cout << "Call JavaInputStream->QueryInterface()" << std::endl;
		return 0;
    };
    virtual STDMETHODIMP_(ULONG) AddRef(void) 
    {
		std::cout << "Call JavaInputStream->AddRef()" << std::endl;
		return 1;
    };
    virtual STDMETHODIMP_(ULONG) Release(void) 
    {
		std::cout << "Call JavaInputStream->Release()" << std::endl;
		return 1;
    };

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
class CJavaInputStream : public IJavaInputStream 
{
  public:
    // IUnknown
    STDMETHODIMP    QueryInterface (REFIID iid, void **ppv);
	STDMETHODIMP_(ULONG) AddRef(void) 
    {
        LOG4CPLUS_DEBUG(logger, "References: " << (m_cRef+1));
        return InterlockedIncrement(&m_cRef); 
    };
	STDMETHODIMP_(ULONG) Release(void)
    {
        LOG4CPLUS_DEBUG(logger, "References: " << (m_cRef-1));
        if (InterlockedDecrement(&m_cRef) == 0)
        {
            // Ouch!
            // TODO Need a better solution
            delete this;
            return 0;
        }
        return 1;
    }

    // IStream
    STDMETHODIMP    Read(void *pv, ULONG cb, ULONG *pcbRead);
    STDMETHODIMP    Write(VOID const *pv, ULONG cb, ULONG *pcbWritten)
    {
        LOG4CPLUS_DEBUG(logger, "Call Stream->Write");
		return E_FAIL;
    };
    STDMETHODIMP    Seek(LARGE_INTEGER dbMove, DWORD dwOrigin, ULARGE_INTEGER *pbNewPosition)
	{
        LOG4CPLUS_DEBUG(logger, "Call Stream->Seek");
		if (dbMove.HighPart == 0 && dbMove.LowPart == 0)
        {
			return S_OK;
        }
		else
        {
			return E_FAIL;
        }
    };
    STDMETHODIMP    SetSize(ULARGE_INTEGER cbNewSize)
    {
        LOG4CPLUS_DEBUG(logger, "Call Stream->SetSize");
		return E_FAIL;
    };
    STDMETHODIMP    CopyTo(IStream *pstm, ULARGE_INTEGER cb, ULARGE_INTEGER *pcbRead, ULARGE_INTEGER *pcbWritten)
    {
        LOG4CPLUS_DEBUG(logger, "Call Stream->CopyTo");
		return E_FAIL;
    };
    STDMETHODIMP    Commit(DWORD grfCommitFlags)
    {
        LOG4CPLUS_DEBUG(logger, "Call Stream->Commit");
		return E_FAIL;
    };
    STDMETHODIMP    Revert(void)
    {
        LOG4CPLUS_DEBUG(logger, "Call Stream->Revert");
		return E_FAIL;
    };
    STDMETHODIMP    LockRegion(ULARGE_INTEGER bOffset, ULARGE_INTEGER cb, DWORD dwLockType)
    {
        LOG4CPLUS_DEBUG(logger, "Call Stream->LockRegion");
		return E_FAIL;
    };
    STDMETHODIMP    UnlockRegion(ULARGE_INTEGER bOffset, ULARGE_INTEGER cb, DWORD dwLockType)
    {
        LOG4CPLUS_DEBUG(logger, "Call Stream->UnlockRegion");
		return E_FAIL;
    };
    STDMETHODIMP    Stat(STATSTG *pstatstg, DWORD grfStatFlag)
    {
        LOG4CPLUS_DEBUG(logger, "Call Stream->Stat");
		return E_FAIL;
    };
    STDMETHODIMP    Clone(IStream **ppstm)
    {
        LOG4CPLUS_DEBUG(logger, "Call Stream->Clone");
		return E_FAIL;
    };

	// JavaInputStream
	STDMETHODIMP setJavaInputStream(JNIEnv *env, jobject object);
    // constructors/destructors
    CJavaInputStream()     { m_cRef = 0; env = NULL;}
    //virtual ~CJavaInputStream()    { ; }
	virtual ~CJavaInputStream();

  private:
    /** Reference xounter */
    LONG        m_cRef;

    /** Logger instance. */
    static log4cplus::Logger logger;
};