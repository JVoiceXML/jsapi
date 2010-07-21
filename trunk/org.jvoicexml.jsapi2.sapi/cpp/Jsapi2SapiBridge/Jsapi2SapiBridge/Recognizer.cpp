#include "stdafx.h"
#include "Recognizer.h"
#include "JNIUtils.h"

#include <iostream>
#include <fstream>
#include <string>

//using namespace system;

Recognizer::Recognizer(HWND hwnd, JNIEnv *env, jobject rec)
: cpRecognizerEngine(NULL), cpRecoCtxt(NULL), hr(S_OK), jenv(env), jrec(rec)
{
    // create a new InprocRecognizer.
    hr = cpRecognizerEngine.CoCreateInstance(CLSID_SpInprocRecognizer);	
    if (SUCCEEDED(hr))
    {
        hr = cpRecognizerEngine->CreateRecoContext(&cpRecoCtxt);
		//hr = cpRecoCtxt->Pause(NULL);
    }

    if (SUCCEEDED(hr))
    {
        // This specifies which of the recognition events are going to trigger
        // notifications.
        // Here, all we are interested in is when the engine has recognized something
		// no false recognition
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
    Pause();
	std::map< std::wstring ,  CComPtr<ISpRecoGrammar> >::iterator it = gramHash.begin();

	for( it ; it != gramHash.end(); it++){		

		hr = it->second->SetRuleState(NULL, NULL, SPRS_INACTIVE );

		hr = it->second->SetGrammarState(SPGS_DISABLED);
		
		it->second.Release();
	}
	
	cpRecoCtxt.Release();
	cpRecognizerEngine.Release();
}

HRESULT Recognizer::LoadGrammar(const wchar_t* grammar)
{
	CComPtr<ISpRecoGrammar>		cpGrammar;

    hr = cpRecoCtxt->CreateGrammar( NULL , &cpGrammar);
    if (FAILED(hr))
    {
        return hr;
    }
    CComPtr<IStream> stream;
    hr = ::CreateStreamOnHGlobal(NULL, true, &stream);
    if (FAILED(hr))
    {
        return hr;
    }
    ULONG written;
    hr = stream->Write(grammar, wcslen(grammar), &written);
    if (FAILED(hr))
    {
        return hr;
    }
    CComPtr<ISpGrammarCompiler> compiler;
    compiler.CoCreateInstance(CLSID_SpGrammarCompiler);
    CComPtr<IStream> compiledStream;
    hr = ::CreateStreamOnHGlobal(NULL, true, &compiledStream);
    if (FAILED(hr))
    {
        return hr;
    }
	CComPtr<IStream> headerStream;
	CComPtr<ISpErrorLog> errorLog;
    hr = compiler->CompileStream(stream, compiledStream, headerStream, NULL, errorLog, 0);
    if (FAILED(hr))
    {
		return hr;
    }
    HGLOBAL hGrammar;
    ::GetHGlobalFromStream(compiledStream, &hGrammar);
    cpGrammar->LoadCmdFromMemory((SPBINARYGRAMMAR *)::GlobalLock(hGrammar), SPLO_DYNAMIC);
    compiler.Release();
	hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
    return hr;
}

HRESULT Recognizer::LoadGrammarFile(LPCWSTR grammarPath,LPCWSTR grammarID )
{
	CComPtr<ISpRecoGrammar>		cpGrammar;
	
    hr = cpRecoCtxt->CreateGrammar( NULL, &cpGrammar);
    if (FAILED(hr))
    {
        return hr;
    }

	hr = cpGrammar->LoadCmdFromFile( grammarPath , SPLO_STATIC);	
    if (FAILED(hr))
    {
        return hr;
    }

	hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
    if (FAILED(hr))
    {
        return hr;
    }

	gramHash.insert( std::make_pair( grammarID , cpGrammar ) );
	
	return hr;	
}

HRESULT Recognizer::DeleteGrammar(LPCWSTR ID){

	std::map< std::wstring , CComPtr<ISpRecoGrammar> >::iterator it = gramHash.find(ID);
	
	CComPtr<ISpRecoGrammar>		pGrammar = it->second;

	pGrammar->SetRuleState(NULL, NULL, SPRS_INACTIVE );

	pGrammar->SetGrammarState(SPGS_DISABLED);

	pGrammar.Detach();

	pGrammar.Release();

	gramHash.erase(it);

	return hr; 
}

wchar_t* Recognizer::RecognitionHappened()
{
	std::map< std::wstring ,  CComPtr<ISpRecoGrammar> >::iterator it = gramHash.begin();

	for( it ; it != gramHash.end(); it++){		

		hr = it->second->SetRuleState(NULL, NULL, SPRS_INACTIVE );	
	}	

    CSpEvent event;
    // Process all of the recognition events
    while ( SUCCEEDED( event.GetFrom(cpRecoCtxt) ) )//== S_OK
    {
        switch (event.eEventId)
        {
        case SPEI_RECOGNITION:
            ISpRecoResult* result = event.RecoResult();
            LPWSTR utterance;
            hr = result->GetText(SP_GETWHOLEPHRASE, SP_GETWHOLEPHRASE, TRUE,
                &utterance, NULL);			

			std::map< std::wstring ,  CComPtr<ISpRecoGrammar> >::iterator it = gramHash.begin();

			for( it ; it != gramHash.end(); it++){	

					CComPtr<ISpRecoGrammar>		pGrammar = it->second ;

					pGrammar->SetGrammarState(SPGS_DISABLED);
					pGrammar.Detach();
					pGrammar.Release();
			}

			gramHash.clear();

			return utterance;
        }
    }
    return NULL;
} 


HRESULT Recognizer::Pause()
{
    return cpRecoCtxt->Pause(NULL);				
}

HRESULT Recognizer::Resume()
{   
	return cpRecoCtxt->Resume(NULL); 
}

wchar_t* Recognizer::StartRecognition()
{	
    if (gramHash.empty())
    {
        return NULL;
    }
	std::map< std::wstring ,  CComPtr<ISpRecoGrammar> >::iterator it = gramHash.begin();
	for( it ; it != gramHash.end(); it++){		

		hr = it->second->SetRuleState(NULL, NULL, SPRS_ACTIVE );	
        if (FAILED(hr))
        {
            return NULL;
        }
	}

	hr = cpRecoCtxt->SetNotifyWin32Event();	
	if (FAILED(hr))
    {
        return NULL;
    }		

	hr = cpRecoCtxt->WaitForNotifyEvent(INFINITE);
    if (FAILED(hr))
    {
        return NULL;
    }
    return RecognitionHappened();
}

void Recognizer::StartDictation()
{	
	CComPtr<ISpRecoResult>		cpResult;

	CComPtr<ISpRecoGrammar>		cpGrammar;

	
    hr = cpRecoCtxt->CreateGrammar( NULL, &cpGrammar);

	hr = cpGrammar->LoadDictation(NULL, SPLO_STATIC);

	hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
    
	if ( SUCCEEDED(hr = BlockForResult( cpGrammar, &cpResult)))
	{
		CSpDynamicString dstrText;

        if (SUCCEEDED(cpResult->GetText(SP_GETWHOLEPHRASE, SP_GETWHOLEPHRASE, 
                                        TRUE, &dstrText, NULL)))
        {

			// ToDo Give back the Dictation Result To Java Code
			// and create a terminate Method
			USES_CONVERSION;
			std::cout<< "I heard: " << W2A(dstrText)<<"\n";fflush(stdout);
			
			cpResult.Release();   						
        }
							
		hr = cpGrammar->SetGrammarState(SPGS_ENABLED);			
	}
}


HRESULT Recognizer::BlockForResult( ISpRecoGrammar* cpGrammar, ISpRecoResult ** ppResult)
{	
	USES_CONVERSION;

	HRESULT hr = S_OK;
	CSpEvent event;

	hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_ACTIVE );	
	
	hr = cpRecoCtxt->SetNotifyWin32Event();

	if ( SUCCEEDED(hr)&& SUCCEEDED(hr = event.GetFrom( cpRecoCtxt )) && hr == S_FALSE ) //
	{
		hr = cpRecoCtxt->WaitForNotifyEvent(INFINITE);
	}
			
	hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_INACTIVE );	

	*ppResult = event.RecoResult();

	if (*ppResult)
	{
		(*ppResult)->AddRef();
	}

	return hr;
}


