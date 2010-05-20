#include <stdafx.h>
#include "Synthesizer.h"

Synthesizer::Synthesizer(const wchar_t* engineName)
{
    CComPtr<ISpObjectToken>			cpToken;
    CComPtr<IEnumSpObjectTokens>	cpEnum;

    hr = cpVoice.CoCreateInstance(CLSID_SpVoice);

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
}

void Synthesizer::Speak( LPCWSTR text )
{
    hr = cpVoice->Speak( text, SPF_ASYNC, NULL);
    if (FAILED(hr))
    {
        std::cout << "error: " << hr << std::endl;
  LPSTR messageBuffer;
  if (FormatMessageA(
        FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_IGNORE_INSERTS |
		FORMAT_MESSAGE_FROM_SYSTEM,
        NULL,
        hr,
        MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
        (LPSTR)&messageBuffer,
        0,
        NULL) > 0)
	fprintf(stdout, "%s: %s (0x%x)\n", text, messageBuffer, hr);
    LocalFree(messageBuffer);
    }
}

void Synthesizer::SpeakSSML( LPCWSTR ssml )
{
    cpVoice->Speak( ssml, SPF_ASYNC | SPF_IS_XML, NULL);
}
