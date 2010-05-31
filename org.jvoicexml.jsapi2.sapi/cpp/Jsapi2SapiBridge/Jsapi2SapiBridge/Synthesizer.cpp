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

HRESULT Synthesizer::Speak( LPCWSTR text )
{
    return cpVoice->Speak( text,  SPF_IS_NOT_XML, NULL);//SPF_ASYNC | without async programm crash, with it pause is not possible SPF_ASYNC |
}

HRESULT Synthesizer::SpeakSSML( LPCWSTR ssml )
{
    return cpVoice->Speak(ssml,  SPF_IS_XML, NULL);
}
