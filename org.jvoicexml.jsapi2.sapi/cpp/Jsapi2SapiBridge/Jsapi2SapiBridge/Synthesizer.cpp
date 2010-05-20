#include <stdafx.h>
#include "Synthesizer.h"

Synthesizer::Synthesizer(const wchar_t* engineName)
{
    CComPtr<ISpObjectToken>			cpToken;
    CComPtr<IEnumSpObjectTokens>	cpEnum;

    hr = ::CoInitialize(NULL);
    if (SUCCEEDED(hr))
    {
        hr = cpVoice.CoCreateInstance(CLSID_SpVoice);
    }
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
        hr = cpVoice->SetInterest( SPFEI(SPEI_WORD_BOUNDARY|SPEI_TTS_BOOKMARK),SPFEI(SPEI_WORD_BOUNDARY|SPEI_TTS_BOOKMARK));
        hr = cpVoice->SetNotifyWin32Event();
    }
    std::cout<< "hr:\t\t" << hr << std::endl;

    cpToken.Release();
    cpEnum.Release();		
}

Synthesizer::~Synthesizer()
{
    cpVoice.Release();		
    ::CoUninitialize();	
}

void Synthesizer::Speak( LPCWSTR text )
{
    cpVoice->Speak( text, SPF_DEFAULT, NULL);
}

void Synthesizer::SpeakSSML( LPCWSTR ssml )
{
    cpVoice->Speak( ssml, SPF_ASYNC | SPF_IS_XML, NULL);
}
