#pragma once

class Recognizer
{
public:

    Recognizer();
    virtual ~Recognizer();

    void pause();

    void Resume();

    void setGrammar( LPCWSTR grammarPath );

    void startdictation();

    HRESULT BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult);

    HRESULT	hr;

private:

    int							grammarCount;	
    CComPtr<ISpRecognizer>		cpRecognizer;
    CComPtr<ISpRecoContext>		cpRecoCtxt;
    CComPtr<ISpRecoGrammar>		cpGrammar;
};		

