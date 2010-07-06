#pragma once

#include <jni.h>

class Recognizer
{
public:

    Recognizer(HWND hwnd, JNIEnv *env, jobject rec);
    virtual ~Recognizer();

    void pause();

    LPWSTR Resume();

    HRESULT LoadGrammar(const wchar_t* grammar);
    HRESULT LoadGrammarFile(LPCWSTR grammarPath);
    LPWSTR RecognitionHappened();

    void startdictation();

    HRESULT BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult);

    HRESULT	hr;

private:
    void Recognized(LPWSTR utterance);

    int							grammarCount;	
    CComPtr<ISpRecognizer>		cpRecognizerEngine;
    CComPtr<ISpRecoContext>		cpRecoCtxt;
    CComPtr<ISpRecoGrammar>		cpGrammar;
    JNIEnv* jenv;
    jobject jrec;
};		

