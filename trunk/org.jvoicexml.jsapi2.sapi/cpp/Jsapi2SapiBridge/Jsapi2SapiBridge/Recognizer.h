#pragma once

class Recognizer
{
public:

    Recognizer(HWND hwnd);
    virtual ~Recognizer();

    void pause();

    HRESULT Resume();

    HRESULT LoadGrammar(const wchar_t* grammar);
    HRESULT LoadGrammarFile(LPCWSTR grammarPath);

    void startdictation();

    HRESULT BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult);

    HRESULT	hr;

private:

    int							grammarCount;	
    CComPtr<ISpRecognizer>		cpRecognizerEngine;
    CComPtr<ISpRecoContext>		cpRecoCtxt;
    CComPtr<ISpRecoGrammar>		cpGrammar;
};		

