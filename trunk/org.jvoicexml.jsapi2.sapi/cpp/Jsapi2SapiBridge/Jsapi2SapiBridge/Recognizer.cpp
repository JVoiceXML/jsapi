#include "stdafx.h"
#include "Recognizer.h"
#include "JNIUtils.h"

#include <iostream>
#include <fstream>
#include <string>

#include <stdio.h>
#import <msxml4.dll>


log4cplus::Logger Recognizer::logger =
    log4cplus::Logger::getInstance(_T("org.jvoicexml.sapi.cpp.Recognizer"));


/* Constructor */
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
		hr = cpRecoCtxt->SetInterest(SPFEI(SPEI_RECOGNITION)|SPFEI(SPEI_FALSE_RECOGNITION), SPFEI(SPEI_RECOGNITION)|SPFEI(SPEI_FALSE_RECOGNITION) );//|SPFEI(SPEI_FALSE_RECOGNITION)
    }

	/**** Disable the standard AudioIn */
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
	/**** END -- Disable Standard AudioIn */

	if( SUCCEEDED(hr) )
    {
		hr = cpRecoCtxt->SetAudioOptions(SPAO_RETAIN_AUDIO, NULL, NULL);
	}

	cpAudio.Release();

}

Recognizer::~Recognizer()
{
    Pause();

	/* Inactivate and delete all Grammars contained in the gramHash */
	std::map< std::wstring ,  CComPtr<ISpRecoGrammar> >::iterator it = gramHash.begin();

	for( it ; it != gramHash.end(); it++){		

		CComPtr<ISpRecoGrammar>		grammar = it->second ;

		hr = grammar->SetRuleState(NULL, NULL, SPRS_INACTIVE );

		hr = grammar->SetGrammarState(SPGS_DISABLED);
		
		grammar.Release();
	}
	
	/* Release the CComPtr */
	cpRecoCtxt.Release();
	cpRecognizerEngine.Release();
}


HRESULT Recognizer::LoadGrammar(const wchar_t* grammar, LPCWSTR grammarID )
{
	/* container for the new grammar */
	CComPtr<ISpRecoGrammar> cpGrammar;
    hr = cpRecoCtxt->CreateGrammar( NULL , &cpGrammar);
    if (FAILED(hr))
    {
        return hr;
    }
	
	/* stream our grammar from java into this buffer */
    CComPtr<IStream> stream;
    hr = ::CreateStreamOnHGlobal(NULL, true, &stream);
    if (FAILED(hr))
    {
        return hr;
    }

	// first, we need to convert from WCHAR to CHAR for the GrammarCompiler
	//	(else we get a 0x80045003 - "unsupported format")
	const size_t sizeGrammar = wcslen(grammar)+1;
	char* grammar_ascii = new char[sizeGrammar];
	wcstombs_s(NULL, grammar_ascii, sizeGrammar, grammar, sizeGrammar);
	
    ULONG written;
    hr = stream->Write(grammar_ascii, sizeGrammar - 1, &written);
	//USES_CONVERSION;
	//hr = stream->Write(W2A(grammar), wcslen(grammar), &written);
	
    if (FAILED(hr))
    {
        return hr;
    }

	/* GrammarCompiler for conversion in a binary format */
    CComPtr<ISpGrammarCompiler> compiler;
    //hr = compiler.CoCreateInstance(CLSID_SpGrammarCompiler); //SAPI-Compiler
	hr = compiler.CoCreateInstance(CLSID_SpW3CGrammarCompiler); //SRGS-Compiler
    if (FAILED(hr))
    {
        std::cerr << "CoCreateInstance CLSID_SpW3CGramamrCompiler failed : 0x" << std::hex <<std::uppercase << hr << std::endl;
		return hr;
    }

    CComPtr<IStream> compiledStream;
    hr = ::CreateStreamOnHGlobal(NULL, true, &compiledStream);
    if (FAILED(hr))
    {
        return hr;
    }

	CComPtr<IStream> headerStream;
	CComPtr<ISpErrorLog> errorLog;
	
	/* seek the beginning of the stream */
	LARGE_INTEGER pos;
	pos.QuadPart = 0;
	stream->Seek(pos, STREAM_SEEK_SET, NULL);

	/* compile the stream, and get the errorlog */
	/* @TODO: use the errorlog */
    hr = compiler->CompileStream(stream, compiledStream, NULL, NULL, errorLog, 0);
    if (FAILED(hr))
    {
		std::cerr << "Compile Stream failed : 0x" << std::hex <<std::uppercase << hr << std::endl;
		return hr;
    }

	/* load the binaryFormat into our GrammarObject */
    HGLOBAL hGrammar;
    ::GetHGlobalFromStream(compiledStream, &hGrammar);//compiledStream
    hr = cpGrammar->LoadCmdFromMemory((SPBINARYGRAMMAR *)::GlobalLock(hGrammar), SPLO_DYNAMIC);
	if (FAILED(hr)) {
		std::cerr << "Grammar: \"LoadFromMemory\" failed : 0x" << std::hex <<std::uppercase << hr << std::endl;
		return hr;
	}

    compiler.Release();

	/* enable the grammar */
	hr = cpGrammar->SetGrammarState(SPGS_ENABLED);

	/* pair the grammarID and the grammar in gramHash */
	gramHash.insert( std::make_pair( grammarID , cpGrammar ) );

    return hr;
}

//NOTE: Functioning, but not used anymore. Grammars are now directly loaded from memory.
//		Might be useful someday so better keep it! =)
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

//wchar_t** Recognizer::RecognitionHappened()
HRESULT Recognizer::RecognitionHappened(WCHAR* recoResult[])
{
	/* Inactivate all Grammars contained in the gramHash */
	std::map< std::wstring ,  CComPtr<ISpRecoGrammar> >::iterator it = gramHash.begin();
	for( it ; it != gramHash.end(); it++){		

		hr = it->second->SetRuleState(NULL, NULL, SPRS_INACTIVE );	
	}

	LPWSTR utterance = NULL;
	BSTR SML = NULL;
	LPCWSTR ruleName = NULL;
    CSpEvent event;
	ISpRecoResult* result = NULL;

    /* Process all of the recognition events */
	while ( SUCCEEDED( hr = event.GetFrom(cpRecoCtxt)) && hr!=S_FALSE )//== S_OK
    {	
		switch (event.eEventId)
        {
			case SPEI_RECOGNITION:

				result = event.RecoResult();

				hr = result->GetText(SP_GETWHOLEPHRASE, SP_GETWHOLEPHRASE, TRUE,
					&utterance, NULL);

				/*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*/
				//WCHAR* recoResult[2]; //tst[0]=>ruleName; tst[1] => utterance as SML; 
				
				SPPHRASE *pPhrase;
				hr = result->GetPhrase(&pPhrase);
				if (FAILED(hr)) {
					return hr; //could not retrieve the ruleName
				}
				ruleName = (pPhrase->Rule.pszName);
				recoResult[0] = (WCHAR*) ruleName; //retrieve the rootrulename which activated the rule
				/*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*/


				/* receive an XMLRecoResult from the RecoResult */
				ISpeechXMLRecoResult* XMLResult;
				result->QueryInterface( IID_ISpeechXMLRecoResult , (void**)&XMLResult);

				/* receive an SML String from the XMLRecoResult */				
				hr = XMLResult->GetXMLResult( SPXRO_SML ,&SML);
				if (FAILED(hr)) {
					return hr; // could not retrieve the SML-Resultstring
				}
				recoResult[1] = (WCHAR*) SML; //retrieve the utterance


				/* at the moment not knowing what to do so put it out */
				//std::wcout<< SSML <<std::endl;flush(std::wcout);

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

				//return SML;//utterance;
				//return &recoResult[0]; //ruleName + utterance(SML)
				return S_OK;
				
			case SPEI_FALSE_RECOGNITION:

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

				return NULL;
				//break;
        }

    }
	return NULL;
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

//wchar_t** Recognizer::StartRecognition()
HRESULT Recognizer::StartRecognition(WCHAR* result[])
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
	
	/* Set a Win32 Window Event to receive the recognition Result */
	hr = cpRecoCtxt->SetNotifyWin32Event();	
	if (FAILED(hr))
    {
        return NULL;
    }	

	/* set continuing to true, its a variable to abort the recognition */
	continuing = true;
	hr = S_FALSE;

	/* wait for an event and try to look if it occured every 300ms*/
	while( continuing && hr==S_FALSE  )
    {
		hr = cpRecoCtxt->WaitForNotifyEvent(300);
        if(hr == S_OK)
        {
            //return RecognitionHappened();
			return RecognitionHappened(result);
        }
	}

	/* Delete all Grammars contained in the gramHash */
	/* should be a temporary solution*/
	for( it ; it != gramHash.end(); it++){	

	CComPtr<ISpRecoGrammar>		cpGrammar = it->second ;

	cpGrammar->SetRuleState(NULL, NULL, SPRS_ACTIVE );
	cpGrammar->SetGrammarState(SPGS_DISABLED);
	cpGrammar.Detach();
	cpGrammar.Release();
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
            LOG4CPLUS_DEBUG(logger, _T("I heard: ") << W2A(dstrText));
			
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
