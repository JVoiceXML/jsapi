#pragma once

#include <jni.h>
#include <vector>
#include <map>
#include <string>

class Recognizer
{
public:

    Recognizer(HWND hwnd, JNIEnv *env, jobject rec);
    virtual ~Recognizer();

    HRESULT Resume();

    HRESULT Pause();

    HRESULT LoadGrammar(const wchar_t* grammar,LPCWSTR grammarID);

    HRESULT LoadGrammarFile(LPCWSTR grammarPath, LPCWSTR grammarID);
	
	HRESULT DeleteGrammar(LPCWSTR ID);

    wchar_t* RecognitionHappened();
	
	wchar_t* StartRecognition();

	void StartDictation();

	HRESULT AbortRecognition();


    HRESULT	hr;

private:

	boolean continuing;

    void Recognized(LPWSTR utterance);

	HRESULT BlockForResult(ISpRecoGrammar* cpGrammar, ISpRecoResult ** ppResult);
	
	std::map< std::wstring , CComPtr<ISpRecoGrammar> > gramHash;

    CComPtr<ISpRecognizer>		cpRecognizerEngine;

    CComPtr<ISpRecoContext>		cpRecoCtxt;
	
    JNIEnv* jenv;

    jobject jrec;
};		

