#include <stdafx.h>
#include "Synthesizer.h"

Synthesizer::Synthesizer(const wchar_t* engineName)
:cpVoice(NULL), hr(0)
{
    CComPtr<ISpObjectToken>			cpToken;
    CComPtr<IEnumSpObjectTokens>	cpEnum;

    HRESULT hr = CoCreateInstance(CLSID_SpVoice, NULL, CLSCTX_ALL, IID_ISpVoice, (void **)&cpVoice);

    // create cpEnum that contains the information about the designated TTSEngine
    if(SUCCEEDED(hr))
    {	
        hr = SpEnumTokens(SPCAT_VOICES, engineName, NULL, &cpEnum);  
    }  

    // load required cpToken from cpEnum 
    // get the closest token if designated Voice is not traceable the system default
    // Voice is selected
    if(SUCCEEDED(hr))
    {
        hr = cpEnum->Next(1, &cpToken, NULL);
    }
    // Set the designated or system default Voice is used
    if(SUCCEEDED(hr))
    {    
        hr = cpVoice->SetVoice(cpToken);
    }

    // Set the notifyEvent
    if(SUCCEEDED(hr))
    {    
        hr = cpVoice->SetInterest(
            SPFEI(SPEI_WORD_BOUNDARY|SPEI_TTS_BOOKMARK),SPFEI(SPEI_WORD_BOUNDARY
                |SPEI_TTS_BOOKMARK));
        hr = cpVoice->SetNotifyWin32Event();
    }

    cpToken.Release();
    cpEnum.Release();		
}

Synthesizer::~Synthesizer()
{
    if (cpVoice != NULL)
    {
        cpVoice->Release();
        cpVoice = NULL;
    }
}


HRESULT Synthesizer::Speak(LPCWSTR text, long& size, byte*& buffer)
{
    CComPtr<ISpeechMemoryStream> stream;
    hr = stream.CoCreateInstance(CLSID_SpMemoryStream);
    if(FAILED(hr))
	{
        return hr;
	}

    hr = cpVoice->SetOutput(stream, TRUE);
    if (FAILED(hr))
    {
        return hr;
    }
    hr = cpVoice->Speak(text, SPF_IS_NOT_XML, NULL);
    if (FAILED(hr))
    {
        return hr;
    }
    VARIANT c;
    VariantInit(&c); 
    hr = stream->GetData(&c);
    if (FAILED(hr))
    {
        return hr;
    }
    SAFEARRAY* sa = c.parray;
    SafeArrayGetUBound(sa, 1, &size);
    buffer = new byte[size];
    memcpy(buffer, sa->pvData, size * sizeof(byte));
    return S_OK;
}

HRESULT Synthesizer::SpeakSSML(LPCWSTR ssml, long& size, byte*& buffer)
{
    CComPtr<ISpeechMemoryStream> stream;
    hr = stream.CoCreateInstance(CLSID_SpMemoryStream);
    if(FAILED(hr))
	{
        return hr;
	}

    hr = cpVoice->SetOutput(stream, TRUE);
    if (FAILED(hr))
    {
        return hr;
    }
    hr = cpVoice->Speak(ssml, SPF_IS_XML, NULL);
    if (FAILED(hr))
    {
        return hr;
    }
    VARIANT c;
    VariantInit(&c); 
    hr = stream->GetData(&c);
    if (FAILED(hr))
    {
        return hr;
    }
    SAFEARRAY* sa = c.parray;
    SafeArrayGetUBound(sa, 1, &size);
    buffer = new byte[size];
    memcpy(buffer, sa->pvData, size * sizeof(byte));
    return S_OK;
}

HRESULT Synthesizer::GetAudioFormat(WAVEFORMATEX& f)
{
    CComPtr<ISpeechMemoryStream> stream;
    hr = stream.CoCreateInstance(CLSID_SpMemoryStream);
    if(FAILED(hr))
	{
        return hr;
	}

    CComPtr<ISpeechAudioFormat> format;
    if(FAILED(hr))
	{
        return hr;
	}
    hr = stream->get_Format(&format);
    if(FAILED(hr))
	{
        return hr;
	}
    CComPtr<ISpeechWaveFormatEx> waveFormat;
    if(FAILED(hr))
	{
        return hr;
	}
    hr = format->GetWaveFormatEx(&waveFormat);
    if(FAILED(hr))
	{
        return hr;
	}
    
    waveFormat->get_AvgBytesPerSec((long*)&f.nAvgBytesPerSec);
    waveFormat->get_BitsPerSample((short*)&f.wBitsPerSample);
    waveFormat->get_Channels((short*)&f.nChannels);
    waveFormat->get_FormatTag((short*)&f.wFormatTag);
    waveFormat->get_SamplesPerSec((long*)&f.nSamplesPerSec);
    return S_OK;
}

