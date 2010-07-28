#include "stdafx.h"
#include "Recognizer.h"
#include "JNIUtils.h"

#include <iostream>
#include <fstream>
#include <string>


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

	/* Inactivate and Delete all Grammars contained in the gramHash */
	std::map< std::wstring ,  CComPtr<ISpRecoGrammar> >::iterator it = gramHash.begin();

	for( it ; it != gramHash.end(); it++){		

		CComPtr<ISpRecoGrammar>		Grammar = it->second ;

		hr = grammar->SetRuleState(NULL, NULL, SPRS_INACTIVE );

		hr = grammar->second->SetGrammarState(SPGS_DISABLED);
		
		grammar->second.Release();
	}
	
	/*Release the CComPtr */
	cpRecoCtxt.Release();
	cpRecognizerEngine.Release();
}

HRESULT Recognizer::LoadGrammar(const wchar_t* grammar)
{
	CComPtr<ISpRecoGrammar>		cpGrammar;

	/* Create a Grammar Instance */
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
	
	/* Create a Grammar Instance */
    hr = cpRecoCtxt->CreateGrammar( NULL, &cpGrammar);
    if (FAILED(hr))
    {
        return hr;
    }

	/* Load content for the Grammar from a file, content will not change */
	hr = cpGrammar->LoadCmdFromFile( grammarPath , SPLO_STATIC);	
    if (FAILED(hr))
    {
        return hr;
    }

	/* Enable the Grammar so the Recognizer will try to match the content*/
	hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
    if (FAILED(hr))
    {
        return hr;
    }

	/* pair the grammarId and the gramamr and insert it in gramHash*/
	gramHash.insert( std::make_pair( grammarID , cpGrammar ) );
	

	return hr;	
}

HRESULT Recognizer::DeleteGrammar(LPCWSTR ID){

	/* find specified Grammar in gramHash*/
	std::map< std::wstring , CComPtr<ISpRecoGrammar> >::iterator it = gramHash.find(ID);
	
	CComPtr<ISpRecoGrammar>		cpGrammar = it->second;
    /* Inactivate the Grammar so the recognizer won´t try to match the content any more */
	cpGrammar->SetRuleState(NULL, NULL, SPRS_INACTIVE );

	/* Disable the Grammar */
	cpGrammar->SetGrammarState(SPGS_DISABLED);

	/* Detach and Release the CComPtr */
	cpGrammar.Detach();
	cpGrammar.Release();

	/* Erase the Grammar from gramHash*/
	gramHash.erase(it);

	return hr; 
}

wchar_t* Recognizer::RecognitionHappened()
{
	/* Inactivate all Grammars contained in the gramHash */
	std::map< std::wstring ,  CComPtr<ISpRecoGrammar> >::iterator it = gramHash.begin();
	for( it ; it != gramHash.end(); it++){		

		hr = it->second->SetRuleState(NULL, NULL, SPRS_INACTIVE );	
	}

	LPWSTR utterance = NULL;
    CSpEvent event;

    /* Process all of the recognition events */
	while ( SUCCEEDED( hr = event.GetFrom(cpRecoCtxt)) && hr!=S_FALSE )//== S_OK
    {	
		switch (event.eEventId)
        {
			case SPEI_RECOGNITION:

				ISpRecoResult* result = event.RecoResult();

				hr = result->GetText(SP_GETWHOLEPHRASE, SP_GETWHOLEPHRASE, TRUE,
					&utterance, NULL);

				/* recieve an XMLRecoResult from the RecoResult */
				ISpeechXMLRecoResult* XMLResult;
				result->QueryInterface( IID_ISpeechXMLRecoResult , (void**)&XMLResult);

				/* recieve an SML String from the XMLRecoResult */
				BSTR SSML = NULL;
				XMLResult->GetXMLResult( SPXRO_SML ,&SSML);

				/* at the moment not knowing what to do so put it out */
				std::wcout<< SSML <<std::endl;flush(std::wcout);

				/* Delete all Grammars contained in the gramHash */
				/* should be a temporary solution*/
				it = gramHash.begin();
				for( it ; it != gramHash.end(); it++){	

					CComPtr<ISpRecoGrammar>		cpGrammar = it->second ;

					cpGrammar->SetGrammarState(SPGS_DISABLED);
					cpGrammar.Detach();
					cpGrammar.Release();
				}

				gramHash.clear();				
				break;
        }
    }

    return utterance;
} 


HRESULT Recognizer::Pause()
{
	continuing = false;

    //return cpRecoCtxt->Pause(NULL);				
    return S_OK;
}

HRESULT Recognizer::Resume()
{   
	//return cpRecoCtxt->Resume(NULL); 
    return S_OK;
}

wchar_t* Recognizer::StartRecognition()
{	
	if (gramHash.empty())
    {
        return NULL;
    }

	/* Activate the Grammars contained in gramHash */
	std::map< std::wstring ,  CComPtr<ISpRecoGrammar> >::iterator it = gramHash.begin();
	for( it ; it != gramHash.end(); it++)
    {		
		hr = it->second->SetRuleState(NULL, NULL, SPRS_ACTIVE );
        if (FAILED(hr))
        {
            return NULL;
        }
	}
	
	/* Set and Win32 Window Event to recieve the recognition Result */
	hr = cpRecoCtxt->SetNotifyWin32Event();	
	if (FAILED(hr))
    {
        return NULL;
    }	

	/* set continuing to true, its a variable to abort the recognition */
	continuing = true;
	hr = S_FALSE;

	/* wait for an event an try to look if it occured every 300ms*/
	while( continuing && hr==S_FALSE  )
    {
		hr = cpRecoCtxt->WaitForNotifyEvent(300);
        if(hr == S_OK)
        {
            return RecognitionHappened();
        }
	}

    return NULL;
}

HRESULT Recognizer::AbortRecognition()
{
    continuing = false;
    return S_OK;
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
