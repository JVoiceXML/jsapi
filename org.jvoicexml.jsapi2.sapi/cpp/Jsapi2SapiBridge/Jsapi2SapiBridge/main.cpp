#include "stdafx.h"
#include "Recognizer.h"

HWND hWnd = NULL;
static HINSTANCE hInstance = NULL;

// alternative look-up error codes returned by sapi in the file sperror.h
void GetErrorMessage(char* buffer, size_t size, const char* text, HRESULT hr) 
{	
	LPSTR pMessage = NULL;
	DWORD length = FormatMessageA(
							FORMAT_MESSAGE_ALLOCATE_BUFFER  |
							FORMAT_MESSAGE_FROM_HMODULE |
							FORMAT_MESSAGE_FROM_SYSTEM	|
							FORMAT_MESSAGE_IGNORE_INSERTS |
							FORMAT_MESSAGE_MAX_WIDTH_MASK,
							hInstance,
							hr,
							MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
							(LPSTR)pMessage,
							0,
							NULL);
    if (length > 0)
    {		
		sprintf_s( buffer, length, "%s. %s: (%#lX)", text, pMessage, hr);	
		LocalFree(pMessage);
	}
    else
    {
		sprintf_s(buffer, size, "%s. ErrorCode: %#lX", text, hr);
    }	
}


LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	switch (message)
	{
	case WM_NCCREATE:
		DefWindowProc(hWnd, message, lParam, wParam);
		return 1;
	case WM_CLOSE:
		DestroyWindow(hWnd);
		return 0;
	case WM_DESTROY:
		PostQuitMessage(0);
		return 0;
    case WM_RECOEVENT:
        Recognizer* recognizer = (Recognizer*) wParam;
        recognizer->RecognitionHappened();
        return 0;
	}

	return DefWindowProcA(hWnd, message, lParam, wParam);
}

BOOL APIENTRY DllMain(HINSTANCE hModule, DWORD ul_reason_for_call, LPVOID lpReserved)
{	
    if (hWnd != NULL)
    {
        return TRUE;
    }
    hInstance = hModule;
	TCHAR *szWindowClass=_T("JSAPI2SapiWindowClass");
	TCHAR *szTitle=_T("JSAPI2 SAPI");

	WNDCLASS wndclass;
    wndclass.style         = CS_HREDRAW | CS_VREDRAW;
    wndclass.lpfnWndProc   = WndProc;
    wndclass.cbClsExtra    = 0;
    wndclass.cbWndExtra    = 0;
    wndclass.hInstance     = hModule;
    wndclass.hIcon         = NULL;
    wndclass.hCursor       = 0;
    wndclass.hbrBackground = (HBRUSH)(COLOR_WINDOW+1);
    wndclass.lpszMenuName  = NULL;
    wndclass.lpszClassName = szWindowClass;
    ATOM atom = RegisterClass(&wndclass);
    if (atom == NULL)
	{
        return FALSE;
	}

	hWnd = CreateWindow(
	  szWindowClass,
	  szTitle,
  	  WS_OVERLAPPEDWINDOW,
  	  CW_USEDEFAULT,
  	  0,
  	  CW_USEDEFAULT,
  	  0,
	  HWND_MESSAGE,
	  NULL,
	  hModule,
	  NULL);
	if (hWnd==0)
	{
        return FALSE;
	}
    return TRUE;
}
