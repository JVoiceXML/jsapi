#include <stdafx.h>
#include <iostream>


class Recognizer{

	private:
		int							grammarCount;
		HRESULT						hr;
		CComPtr<ISpRecognizer>		cpRecognizer;
		CComPtr<ISpObjectToken>     cpObjectToken;
		CComPtr<ISpAudio>           cpAudio;

		CComPtr<ISpRecoContext>		cpRecoCtxt;
		CComPtr<ISpRecoGrammar>		cpGrammar;

	public:
	
		HRESULT setGrammar( LPCWSTR grammarPath ){

			hr = cpRecoCtxt->CreateGrammar( grammarCount++, &cpGrammar);

			hr = cpGrammar->SetGrammarState(SPGS_DISABLED);			

			hr = cpGrammar->LoadCmdFromFile( grammarPath,SPLO_STATIC);

			std::cout<< "Grammar loaded. hr:" << hr <<"\n";
			fflush(stdout);

			//hr = cpGrammar->Commit(NULL);
			//std::cout<< "cpGrammar:\t" << cpGrammar <<"\n";

			return hr;		
		}


		Recognizer(){

			grammarCount =0;
		
			hr = ::CoInitialize(NULL);			
			
			if( SUCCEEDED(hr) ){
				// create a new InprocRecognizer.
				hr = cpRecognizer.CoCreateInstance(CLSID_SpInprocRecognizer);

			}

			//if( SUCCEEDED(hr) ){
			//	// Get the default audio input token.
			//	hr = SpGetDefaultTokenFromCategoryId(SPCAT_AUDIOIN, &cpObjectToken);
			//}
			//std::cout<< "hr:\t\t" << hr <<"\n";

			//if (SUCCEEDED(hr))
			//{
			//   // Set the audio input to our token.
			//   hr = cpRecognizer->SetInput(cpObjectToken, TRUE);
			//}
			//std::cout<< "hr:\t\t" << hr <<"\n";

			if (SUCCEEDED(hr))
			{
			   // Set up the inproc recognizer audio input with an audio input object.
			   // Create the default audio input object.
			   hr = SpCreateDefaultObjectFromCategoryId(SPCAT_AUDIOIN, &cpAudio);
			}

			if (SUCCEEDED(hr))
			{
			   // Set the audio input to our object.
			   hr = cpRecognizer->SetInput(cpAudio, TRUE);
			}


			if( SUCCEEDED(hr) ){
				// create a new Recognition context.
				hr = cpRecognizer->CreateRecoContext( &cpRecoCtxt);
			}


			if( SUCCEEDED(hr) ){
				// Set the Notivy.
				hr = cpRecoCtxt->SetNotifyWin32Event();
			}
	

			if( SUCCEEDED(hr) ){
				hr = cpRecoCtxt->SetInterest(SPFEI(SPEI_RECOGNITION ), SPFEI(SPEI_RECOGNITION ) );
			}


			if( SUCCEEDED(hr) ){
				hr = cpRecoCtxt->SetAudioOptions(SPAO_RETAIN_AUDIO, NULL, NULL);
			}	

			//if( SUCCEEDED(hr) ){
			//	hr = cpRecoCtxt->CreateGrammar(0, &cpGrammar);
			//}
			//std::cout<< "hr:\t\t" << hr <<"\n";

			//if( SUCCEEDED(hr) ){
			//	hr = cpGrammar->LoadDictation(NULL, SPLO_STATIC);
			//}
			//std::cout<< "hr:\t\t" << hr <<"\n";

			//if( SUCCEEDED(hr) ){
			//	hr = cpGrammar->SetDictationState(SPRS_ACTIVE);
			//}

			//std::cout<< "hr:\t\t" << hr <<"\n";

			//std::cout<< "cpRecognizer:\t" << cpRecognizer <<"\n";
			//std::cout<< "cpRecoCtxt:\t" << cpRecoCtxt <<"\n";
		}

		~Recognizer(){

			cpGrammar.Release();
			cpRecoCtxt.Release();

			cpRecognizer.Release();
			cpObjectToken.Release();
			cpAudio.Release();
			::CoUninitialize();

		}

		void startdictation(){
			
			USES_CONVERSION;
			CComPtr<ISpRecoResult>		cpResult;

			hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
			std::cout<< "Please speak now \n";
			fflush(stdout);

			if (  SUCCEEDED( hr = BlockForResult(cpRecoCtxt, &cpResult) )  )
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
			else{ 
				std::cout<< "no valid input \n";
			}
		}

		HRESULT BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult)
		{
			USES_CONVERSION;

			HRESULT hr = S_OK;
			CSpEvent event;

			hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_ACTIVE );

			while ( SUCCEEDED(hr) && SUCCEEDED(hr = event.GetFrom(pRecoCtxt)) && hr == S_FALSE  )
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


};		

