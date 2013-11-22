#include "ErrorLog.h"


ErrorLog::ErrorLog(): m_lastError(NULL) {}

ErrorLog::~ErrorLog()
{
	if (m_lastError != NULL)
	{
		free(m_lastError);
		m_lastError = NULL;
	}
}


BOOL ErrorLog::GetErrorIndex(const unsigned int index, WCHAR** dError)
{
	if ((index >= 0) && (index < m_errors.size()))
	{
		if (m_lastError != NULL)
		{
			free(m_lastError);
			m_lastError = NULL;
		}
		m_lastError = NULL; //ConvertToWide(m_errors[index]);
		if (m_lastError != NULL)
		{
			*dError = m_lastError;
			return TRUE;
		}
	}
	return FALSE;
}


STDMETHODIMP_(ULONG) ErrorLog::AddRef()
{
	return ++m_uRefCount; // Increment this object's reference count.
}

STDMETHODIMP_(ULONG) ErrorLog::Release()
{
	ULONG uRet = --m_uRefCount; // Decrement this object's reference count.
	if ( m_uRefCount == 0 ) // Releasing last reference?
	{
		delete this;
	}

	return uRet;
}

STDMETHODIMP ErrorLog::QueryInterface ( REFIID riid, void** ppv )
{
    if (ppv == NULL)
    {
        return E_INVALIDARG;
    }
	if (riid == IID_IUnknown)
    {
		*ppv = static_cast<IUnknown*> (this);
        AddRef();
        return S_OK;
    }

	if (riid == IID_ISpErrorLog)
    {
		*ppv = static_cast<ISpErrorLog*> (this);
		AddRef();
		return S_OK;
	}

	if (riid == IID_IErrorLog)
    {
		*ppv = static_cast<ErrorLog*> (this);
		AddRef();
		return S_OK;
	}

    *ppv = NULL;
    return E_NOINTERFACE;

}


void STDMETHODCALLTYPE ErrorLog::ClearErrors()
{
	m_errors.clear();
}


HRESULT ErrorLog::AddError(const long lLineNumber,
						   HRESULT hr,
						   const WCHAR *pszDescription,
						   const WCHAR *pszHelpFile,
						   DWORD dwHelpContext)
{
	//std::string errorContent = FormattedString("Error %d at line %d of %s: %s",
	//	hr, lLineNumber, (pszHelpFile !=NULL) ? ConvertFromWide((WCHAR*)pszHelpFile).c_str() : "UNKNOWN",
	//	(pszDescription!=NULL)?ConvertFromWide((WCHAR*)pszDescription).c_str():"EMP­TY");

	//m_errors.push_back(errorContent);
	return S_OK;
}


