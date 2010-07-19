#pragma once

#include <jni.h>
#include <vector>

class Recognizer
{
public:

    Recognizer(HWND hwnd, JNIEnv *env, jobject rec);
    virtual ~Recognizer();

    HRESULT Resume();
    HRESULT Pause();

    HRESULT LoadGrammar(const wchar_t* grammar);
    HRESULT LoadGrammarFile(LPCWSTR grammarPath);

	HRESULT EjectGrammar(LPCSTR ID);


    wchar_t* RecognitionHappened();
	
	wchar_t* StartRecognition();

    void StartDictation();

    HRESULT BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult);

    HRESULT	hr;

private:
    void Recognized(LPWSTR utterance);

    int							grammarCount;	
    CComPtr<ISpRecognizer>		cpRecognizerEngine;
    CComPtr<ISpRecoContext>		cpRecoCtxt;

	std::vector< CComPtr<ISpRecoGrammar> > gramVec;

    JNIEnv* jenv;
    jobject jrec;
};		

