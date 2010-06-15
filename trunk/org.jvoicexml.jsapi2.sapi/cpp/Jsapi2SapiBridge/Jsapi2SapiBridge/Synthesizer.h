#pragma once

#include <stdafx.h>

class Synthesizer
{
public:
	Synthesizer(const wchar_t* engineName);
    virtual ~Synthesizer();

	HRESULT Speak(LPCWSTR text, long& size, byte*& buffer);
    HRESULT SpeakSSML(LPCWSTR ssml, long& size, byte*& buffer);
    HRESULT GetAudioFormat(WAVEFORMATEX& format);

    boolean Cancel(){
			if(  SUCCEEDED( cpVoice->Speak(NULL, SPF_PURGEBEFORESPEAK, NULL) )  ){
				return true;
			}
			else {
				return false;
			}			
		}

		boolean Pause(){
			if(  SUCCEEDED( hr = cpVoice->Pause() )  ){
				return true;
			}
			else {
				return false;
			}	
		}

		boolean Resume(){
						if(  SUCCEEDED( cpVoice->Resume() )  ){
				return true;
			}
			else {
				return false;
			}	
		}

		HANDLE getEventHandler(){
			
			return cpVoice->GetNotifyEventHandle();
		}
    HRESULT	hr;

private:					

    ISpVoice* cpVoice;

};
