#include "stdafx.h"
#include "Recognizer.h"
#include "JNIUtils.h"

#include <iostream>
#include <fstream>
#include <string>

Recognizer::Recognizer(HWND hwnd, JNIEnv *env, jobject rec)
: cpRecognizerEngine(NULL), cpRecoCtxt(NULL), cpGrammar(NULL), hr(S_OK), grammarCount(0),
  jenv(env), jrec(rec)
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
        //hr = cpRecoCtxt->SetNotifyWindowMessage(hwnd, WM_RECOEVENT, (WPARAM) this, 0);
		//cpRecoCtxt->SetNotifyWin32Event();
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
	cpGrammar.Release();
	cpRecoCtxt.Release();
	cpRecognizerEngine.Release();
}

HRESULT Recognizer::LoadGrammar(const wchar_t* grammar)
{
    hr = cpRecoCtxt->CreateGrammar(grammarCount++, &cpGrammar);
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
    stream->Write(grammar, wcslen(grammar), &written);
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
    hr = compiler->CompileStream(stream, compiledStream, NULL, NULL, NULL, 0);
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

HRESULT Recognizer::LoadGrammarFile(LPCWSTR grammarPath)
{

    hr = cpRecoCtxt->CreateGrammar(grammarCount++, &cpGrammar);
    if (FAILED(hr))
    {
        return GetLastError();;
    }
	hr = cpGrammar->LoadCmdFromFile( grammarPath , SPLO_STATIC);
	
    if (FAILED(hr))
    {
        return hr;
    }

	hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
	hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_ACTIVE );
	
	std::cout<< "c-Code PLEASE SPEAK Licht ein"<<std::endl; fflush(stdout);

	cpRecoCtxt->SetNotifyWin32Event();					// Achtung bei allocate ->SetNotifyWindowMessage() auskommentiert
	hr = cpRecoCtxt->WaitForNotifyEvent(INFINITE);

	RecognitionHappened();

	return hr;
}

HRESULT Recognizer::RecognitionHappened()
{
	hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_INACTIVE );

    CSpEvent event;
    // Process all of the recognition events
    while ( SUCCEEDED( event.GetFrom(cpRecoCtxt) ) )//== S_OK
    {
        switch (event.eEventId)
        {
        case SPEI_RECOGNITION:
            ISpRecoResult* result = event.RecoResult();
            wchar_t* utterance;
            hr = result->GetText(SP_GETWHOLEPHRASE, SP_GETWHOLEPHRASE, TRUE,
                &utterance, NULL);
			
			std::cout<< "c-Code Utterance: "<< CW2A(utterance) <<std::endl; fflush(stdout);

            Recognized(utterance);
            CoTaskMemFree(utterance);
			return hr;
        }
    }
    return S_FALSE;
} 

void Recognizer::Recognized(wchar_t* utterance)
{
	jclass clazz = jenv->GetObjectClass(jrec);
    jmethodID methodId = jenv->GetMethodID(clazz, "reportResult","(Ljava/lang/String;)V");
  
	//  if (!GetMethodId(jenv, "org/jvoicexml/jsapi2/sapi/recognition/SapiRecognizer",
  //      "reportResult", "(JLjava/lang/String;)V", clazz, methodId))
  //  {
		//std::cout<< clazz<<std::endl; 
		//std::cout<< methodId<<std::endl; 
		//std::cout<< "Recocnizer::Recognized(utterance);  cant get Method ID"<<std::endl; fflush(stdout);
		//return;
  //  }

	  if ( methodId == NULL )
    {
		std::cout<< "RecognizerClassHandle: "<<clazz<<std::endl; 
		std::cout<< "reportResultMethodeId: " <<methodId<<std::endl; 
		std::cout<< "Recocnizer::Recognized(utterance);  cant get Method ID"<<std::endl; fflush(stdout);
		return;
    }
    jstring jstr = jenv->NewString((jchar*)utterance, wcslen(utterance));
    jenv->CallObjectMethod(jrec, methodId, jstr);
}

void Recognizer::pause()
{

		hr = cpRecoCtxt->Pause( NULL);				
}

HRESULT Recognizer::Resume()
{
    return cpRecoCtxt->Resume(NULL);
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
