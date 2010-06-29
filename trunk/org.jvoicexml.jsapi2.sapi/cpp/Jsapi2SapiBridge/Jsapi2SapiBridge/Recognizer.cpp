#include "stdafx.h"
#include "Recognizer.h"

Recognizer::Recognizer(HWND hwnd)
: cpRecognizerEngine(NULL), cpRecoCtxt(NULL), cpGrammar(NULL), hr(S_OK), grammarCount(0)
{
    // create a new InprocRecognizer.
    hr = cpRecognizerEngine.CoCreateInstance(CLSID_SpInprocRecognizer);	
    if (SUCCEEDED(hr))
    {
        hr = cpRecognizerEngine->CreateRecoContext(&cpRecoCtxt);
    }
    if (SUCCEEDED(hr))
    {
        // Set recognition notification for dictation
        hr = cpRecoCtxt->SetNotifyWindowMessage(hwnd, WM_RECOEVENT, 0, 0);
    }

    if (SUCCEEDED(hr))
    {
        // This specifies which of the recognition events are going to trigger
        // notifications.
        // Here, all we are interested in is the beginning and ends of sounds, as well as
        // when the engine has recognized something
        hr = cpRecoCtxt->SetInterest(SPFEI(SPEI_RECOGNITION), SPFEI(SPEI_RECOGNITION));
    }

    CComPtr<ISpAudio> cpAudio;
	if (SUCCEEDED(hr))
	{
	   // Set up the inproc recognizer audio input with an audio input object.
	   // Create the default audio input object.
	   hr = SpCreateDefaultObjectFromCategoryId(SPCAT_AUDIOIN, &cpAudio);
	}	
	if (SUCCEEDED(hr))
	{
	   // Set the audio input to our object.
	   hr = cpRecognizerEngine->SetInput(cpAudio, TRUE);
	}
	if( SUCCEEDED(hr) )
    {
		hr = cpRecoCtxt->SetAudioOptions(SPAO_RETAIN_AUDIO, NULL, NULL);
	}	
	cpAudio.Release();
}

Recognizer::~Recognizer()
{
	cpGrammar.Release();
	cpRecoCtxt.Release();
	cpRecognizerEngine.Release();
}


HRESULT Recognizer::setGrammar( LPCWSTR grammarPath )
{
    hr = cpRecoCtxt->CreateGrammar( grammarCount++, &cpGrammar);
    if (FAILED(hr))
    {
        return hr;
    }
	hr = cpGrammar->LoadCmdFromFile( grammarPath,SPLO_STATIC);
    if (FAILED(hr))
    {
        return hr;
    }

	hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
    return hr;
}

void Recognizer::pause()
{

		hr = cpRecoCtxt->Pause( NULL);				
}

void Recognizer::Resume()
{
	hr = cpRecoCtxt->Resume(NULL);
}

void Recognizer::startdictation()
{	
	USES_CONVERSION;
	CComPtr<ISpRecoResult>		cpResult;

	std::cout<< "Please speak now \n";
	fflush(stdout);

	if (  SUCCEEDED( hr = BlockForResult(cpRecoCtxt, &cpResult) ) )
	{		
            cpGrammar->SetGrammarState(SPGS_DISABLED);

            CSpDynamicString dstrText;

            if (SUCCEEDED(cpResult->GetText(SP_GETWHOLEPHRASE, SP_GETWHOLEPHRASE, 
                                            TRUE, &dstrText, NULL)))
            {
				std::cout<< "I heard: " << W2A(dstrText)<<"\n";
				fflush(stdout);
				cpResult.Release();   						
            }                  
            hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
    }
}

HRESULT Recognizer::BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult)
{	
	USES_CONVERSION;

	HRESULT hr = S_OK;
	CSpEvent event;

	hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_ACTIVE );

	if ( SUCCEEDED(hr)&& SUCCEEDED(hr = event.GetFrom(pRecoCtxt)) && hr == S_FALSE ) //
	{
		hr = pRecoCtxt->WaitForNotifyEvent(INFINITE);
	}
	
	hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_INACTIVE );

	*ppResult = event.RecoResult();

	if (*ppResult)
	{
		(*ppResult)->AddRef();
	}

	return hr;
}
