// stdafx.h : Includedatei f�r Standardsystem-Includedateien
// oder h�ufig verwendete projektspezifische Includedateien,
// die nur in unregelm��igen Abst�nden ge�ndert werden.
//

#pragma once

#include "targetver.h"
#define WIN32_LEAN_AND_MEAN             // Selten verwendete Teile der Windows-Header nicht einbinden.

// Windows-Headerdateien:
//#include <sapi53.h>
//#include <comip.h>
#include <sapi53.h>
#include <sphelper.h>
#include "resource.h"

//#include <atlwin.h>
//extern CComModule _Module;
//#include <atlcom.h>

// TODO: Hier auf zus�tzliche Header, die das Programm erfordert, verweisen.

#include <iostream>
void GetErrorMessage(char* buffer, size_t size, const char* text, HRESULT hr);
