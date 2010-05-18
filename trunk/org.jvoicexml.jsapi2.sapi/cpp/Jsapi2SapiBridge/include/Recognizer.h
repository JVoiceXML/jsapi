#include <stdafx.h>
#include <iostream>


class Recognizer{

	private:

		HRESULT						hr;
		CComPtr<ISpRecognizer>		cpRecognizer;
		CComPtr<ISpObjectToken>     cpObjectToken;
		CComPtr<ISpAudio>           cpAudio;

		CComPtr<ISpRecoContext>		cpRecoCtxt;
		CComPtr<ISpRecoGrammar>		cpGrammar;

	public:
	
		HRESULT setGrammar( ){

			hr = cpRecoCtxt->CreateGrammar( 0, &cpGrammar);
			std::cout<< "hr:\t\t" << hr <<"\n";

			hr = cpGrammar->SetGrammarState(SPGS_DISABLED);			
			std::cout<< "hr:\t\t" << hr <<"\n";

			hr = cpGrammar->LoadCmdFromFile( L"C:\\Users\\J-ooloo\\Desktop\\Licht2.xml",SPLO_DYNAMIC);
			std::cout<< "hr:\t\t" << hr <<"\n";

			hr = cpGrammar->Commit(NULL);
			std::cout<< "cpGrammar:\t" << cpGrammar <<"\n";

			return hr;
		
		}


		Recognizer(){
		
			hr = ::CoInitialize(NULL);			
			
			if( SUCCEEDED(hr) ){
				// create a new InprocRecognizer.
				hr = cpRecognizer.CoCreateInstance(CLSID_SpInprocRecognizer);

			}
			std::cout<< "hr:\t\t" << hr <<"\n";

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
			std::cout<< "hr:\t\t" << hr <<"\n";

			std::cout<< "hr:\t\t" << hr <<"\n";
			if (SUCCEEDED(hr))
			{
			   // Set the audio input to our object.
			   hr = cpRecognizer->SetInput(cpAudio, TRUE);
			}
			std::cout<< "hr:\t\t" << hr <<"\n";


			if( SUCCEEDED(hr) ){
				// create a new Recognition context.
				hr = cpRecognizer->CreateRecoContext( &cpRecoCtxt);
			}
			std::cout<< "hr:\t\t" << hr <<"\n";

			if( SUCCEEDED(hr) ){
				// Set the Notivy.
				hr = cpRecoCtxt->SetNotifyWin32Event();
			}
			std::cout<< "hr:\t\t" << hr <<"\n";

			if( SUCCEEDED(hr) ){
				hr = cpRecoCtxt->SetInterest(SPFEI(SPEI_RECOGNITION ), SPFEI(SPEI_RECOGNITION ) );
			}
			std::cout<< "hr:\t\t" << hr <<"\n";

			if( SUCCEEDED(hr) ){
				hr = cpRecoCtxt->SetAudioOptions(SPAO_RETAIN_AUDIO, NULL, NULL);
			}	
			std::cout<< "hr:\t\t" << hr <<"\n";

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
			std::cout<< "hr:\t\t" << hr <<"\n";

			std::cout<< "cpRecognizer:\t" << cpRecognizer <<"\n";
			std::cout<< "cpRecoCtxt:\t" << cpRecoCtxt <<"\n";
			

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
			std::cout<< "\n set grammar enable:\t\t" << hr << "\n";

			while (  SUCCEEDED( hr = BlockForResult(cpRecoCtxt, &cpResult) )  )
			{		
					
                    cpGrammar->SetGrammarState(SPGS_DISABLED);
					std::cout<< "\n set grammar disable:\t\t" << hr << "\n";

                    CSpDynamicString dstrText;

                    if (SUCCEEDED(cpResult->GetText(SP_GETWHOLEPHRASE, SP_GETWHOLEPHRASE, 
                                                    TRUE, &dstrText, NULL)))
                    {
						std::cout<< "I heard: " << W2A(dstrText)<<"\n";
						cpResult.Release();
                      
                    }                  
                    hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
            } 
		}

		HRESULT BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult)
		{
			USES_CONVERSION;

			HRESULT hr = S_OK;
			CSpEvent event;

			hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_ACTIVE );
			std::cout<< "\n set rule state active =\t\t" << hr << "\n";

			while ( SUCCEEDED(hr) && SUCCEEDED(hr = event.GetFrom(pRecoCtxt)) && hr == S_FALSE  )
			{
				hr = pRecoCtxt->WaitForNotifyEvent(5000);
				std::cout<< "\n wait for event:\t\t" << hr << "\n";
			}
			
			hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_INACTIVE );
			std::cout<< "\n set rule state inactive =\t\t" << hr << "\n";

			*ppResult = event.RecoResult();

			if (*ppResult)
			{
				(*ppResult)->AddRef();
			}

			return hr;
		}


};		

