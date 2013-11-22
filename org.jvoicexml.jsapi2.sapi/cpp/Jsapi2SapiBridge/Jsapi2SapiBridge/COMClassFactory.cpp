#include "COMClassFactory.h"
#include "JavaInputStream.h"
#include "ErrorLog.h"


 STDMETHODIMP COMClassFactory::QueryInterface(REFIID riid, void** ppv)
{
    if (ppv == NULL)
	{
        return E_INVALIDARG;
	}

    if (riid == IID_IClassFactory || riid == IID_IUnknown) 
	{
        *ppv = (IClassFactory *) this;
        AddRef();
        return S_OK;
    }
    *ppv = NULL;
    return E_NOINTERFACE;
} 

 STDMETHODIMP COMClassFactory::CreateInstance(LPUNKNOWN punkOuter, REFIID riid, void** ppv)
{
    LPUNKNOWN   punk;
    HRESULT     hr;

    *ppv = NULL;
    if (punkOuter != NULL)
	{
        return CLASS_E_NOAGGREGATION;
	}

	if (riid == CLSID_JavaInputStream)
    {
		punk = new JavaInputStream();
	}
	else if (riid == CLSID_ErrorLog)
	{
		punk = new ErrorLog();
	}
    if (punk == NULL)
    {
        return E_OUTOFMEMORY;
    }

    return punk->QueryInterface(riid, ppv);
}  