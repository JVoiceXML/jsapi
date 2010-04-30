#include "stdafx.h"
#include "SapiRecognizer.h"

	HRESULT hr = S_OK;							// Variable to sheck if process worked well
	
	CComPtr<ISpRecognizer>			g_cpEngine;		// Pointer to our recognition engine instance
	CComPtr<ISpRecoGrammar>			g_cpCmdGrammar; // Pointer to our grammar object
	CComPtr<ISpRecoContext>         g_cpRecoCtxt;   // Pointer to our recognition context
	
	CComPtr<ISpAudio>				cpAudio;		// Pointer to our public ISpStream instance

	/*
	 * Class:     SapiRecognizer
	 * Method:    getBuiltInGrammars
	 * Signature: ()Ljava/util/Vector;
	 */
	JNIEXPORT jobject JNICALL Java_SapiRecognizer_getBuiltInGrammars
	(JNIEnv *, jobject){

		return NULL;
	}

	/*
	 * Class:     SapiRecognizer
	 * Method:    Allocate
	 * Signature: ()V
	 */
	JNIEXPORT void JNICALL Java_SapiRecognizer_Allocate
	(JNIEnv *, jobject){

			// Start COM objects
			 hr = CoInitialize(NULL);

			// create a recognition engine
			hr = g_cpEngine.CoCreateInstance(CLSID_SpSharedRecognizer);
		    
			// Let SR know that window we want it to send event information to, and using
			// what message
			//hr = g_cpRecoCtxt->SetNotifyWindowMessage( hWnd, WM_RECOEVENT, 0, 0 );
			//if ( FAILED( hr ) )
			//{
			//	break;
			//}

			// Tell SR what types of events interest us.  Here we only care about command
			// recognition.
			hr = g_cpRecoCtxt->SetInterest( SPFEI(SPEI_RECOGNITION), SPFEI(SPEI_RECOGNITION) );

	}
			
	/*
	 * Class:     SapiRecognizer
	 * Method:    Deallocate
	 * Signature: ()V
	 */
	JNIEXPORT void JNICALL Java_SapiRecognizer_Deallocate
		(JNIEnv *, jobject){
			
			// Release grammar, if loaded
			if ( g_cpCmdGrammar )
			{
				g_cpCmdGrammar.Release();
			}
			// Release recognition context, if created
			if ( g_cpRecoCtxt)
			{
				g_cpRecoCtxt->SetNotifySink(NULL);
				g_cpRecoCtxt.Release();
			}
			// Release recognition engine instance, if created
			if ( g_cpEngine )
			{
				g_cpEngine.Release();
			}
	}

	/*
	 * Class:     SapiRecognizer
	 * Method:    Pause
	 * Signature: ()V
	 */
	JNIEXPORT void JNICALL Java_SapiRecognizer_Pause__
		(JNIEnv *, jobject){

	}

	/*
	 * Class:     SapiRecognizer
	 * Method:    Pause
	 * Signature: (I)V
	 */
	JNIEXPORT void JNICALL Java_SapiRecognizer_Pause__I
		(JNIEnv *, jobject, jint){

	}

	/*
	 * Class:     SapiRecognizer
	 * Method:    Resume
	 * Signature: ()Z
	 */
	JNIEXPORT jboolean JNICALL Java_SapiRecognizer_Resume
		(JNIEnv *, jobject){
			
			return false;
	}

	/*
	 * Class:     SapiRecognizer
	 * Method:    setGrammars
	 * Signature: (Ljava/util/Vector;)Z
	 */
	JNIEXPORT jboolean JNICALL Java_SapiRecognizer_setGrammars
		(JNIEnv *, jobject, jobject){
		
			// Load our grammar, which is the compiled form of simple.xml bound into this executable as a
			// user defined ("SRGRAMMAR") resource type.
			hr = g_cpRecoCtxt->CreateGrammar( NULL , &g_cpCmdGrammar);

			hr = g_cpCmdGrammar->LoadCmdFromResource(NULL, MAKEINTRESOURCEW(IDR_CMD_CFG),
													 L"SRGRAMMAR", MAKELANGID(LANG_NEUTRAL, SUBLANG_NEUTRAL),
													 SPLO_DYNAMIC);
			//hr = g_cpCmdGrammar->LoadCmdFromFile("D:\\Windows SDK\\Microsoft Speech SDK 5.1\\Samples\\CPP\\Tutorials\\CoffeeS0\\coffee.xml",SPLO_DYNAMIC);



			return false;
	}
