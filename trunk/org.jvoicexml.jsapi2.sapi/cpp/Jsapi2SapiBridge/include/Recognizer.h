#include <stdafx.h>
#include <iostream>


class Recognizer{

	public:

		Recognizer();

		virtual ~Recognizer();

		HRESULT pause();

		HRESULT resume();

		HRESULT setGrammar( LPCWSTR grammarPath );

		void startdictation();

		HRESULT BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult);

	private:
		int							grammarCount;
		HRESULT						hr;
		CComPtr<ISpRecognizer>		cpRecognizer;
		CComPtr<ISpObjectToken>     cpObjectToken;
		CComPtr<ISpAudio>           cpAudio;

		CComPtr<ISpRecoContext>		cpRecoCtxt;
		CComPtr<ISpRecoGrammar>		cpGrammar;

};		

