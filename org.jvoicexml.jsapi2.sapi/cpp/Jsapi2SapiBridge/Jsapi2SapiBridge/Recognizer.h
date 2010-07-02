#pragma once

#include <jni.h>

class Recognizer
{
public:

    Recognizer(HWND hwnd, JNIEnv *env, jobject rec);
    virtual ~Recognizer();

    void pause();

    HRESULT Resume();

    HRESULT LoadGrammar(const wchar_t* grammar);
    HRESULT LoadGrammarFile(LPCWSTR grammarPath);
	//HRESULT LoadGrammarFile(std::wstring grammarPath);
    HRESULT RecognitionHappened();

    void startdictation();

    HRESULT BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult);

    HRESULT	hr;

private:
    void Recognized(wchar_t* utterance);

    int							grammarCount;	
    CComPtr<ISpRecognizer>		cpRecognizerEngine;
    CComPtr<ISpRecoContext>		cpRecoCtxt;
    CComPtr<ISpRecoGrammar>		cpGrammar;
    JNIEnv* jenv;
    jobject jrec;
};		

