#include "stdafx.h"


static HWND hWnd = NULL;

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
	}

	return DefWindowProc(hWnd, message, lParam, wParam);
}

BOOL APIENTRY DllMain(HINSTANCE  hModule, DWORD  ul_reason_for_call, LPVOID lpReserved)
{
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
        ::CoInitialize(NULL);
        break;
    case DLL_THREAD_ATTACH:
        ::CoInitializeEx(NULL, COINIT_MULTITHREADED);
        break;
    case DLL_PROCESS_DETACH:
    case DLL_THREAD_DETACH:
        ::CoUninitialize();
        break;
    }

    if (hWnd != NULL)
    {
        return TRUE;
    }
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
