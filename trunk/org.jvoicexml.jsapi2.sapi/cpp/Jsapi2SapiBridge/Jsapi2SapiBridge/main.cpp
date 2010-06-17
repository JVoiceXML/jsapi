#include "stdafx.h"

static HWND hWnd = NULL;
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
	}

	return DefWindowProcA(hWnd, message, lParam, wParam);
}

BOOL APIENTRY DllMain(HINSTANCE hModule, DWORD  ul_reason_for_call, LPVOID lpReserved)
{	
	HRESULT hr = S_FALSE;

    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
       hr = ::CoInitialize(NULL);
	   
	   std::cout<< "CoInitialize \tThreadID: 0x" <<std::hex<< GetCurrentThreadId();
	   std::cout<< "\tProcess ID: 0x" <<std::hex<< CoGetCurrentProcess() <<"\n";fflush(stdout);
			//switch (hr)
			//{
			//case S_OK: std::cout<< "CoInitialize: S_OK :" << hr <<"\n";fflush(stdout);break;
			//	case S_FALSE: std::cout<< "CoInitialize: S_FALSE :" << hr <<"\n";fflush(stdout);break;
			//	case RPC_E_CHANGED_MODE: std::cout<< "CoInitialize: RPC_E_CHANGED_MODE :" << hr <<"\n";fflush(stdout);break;
			//	default: std::cout<< "CoInitialize: " << hr <<"\n";fflush(stdout);break;
			//}
        break;

    case DLL_THREAD_ATTACH:
       hr = ::CoInitializeEx(NULL, COINIT_MULTITHREADED);
	   
	   std::cout<< "CoInitializeEx \tThreadID: 0x" <<std::hex<< GetCurrentThreadId();
	   std::cout<< "\tProcess ID: 0x" <<std::hex<< CoGetCurrentProcess() <<"\n";fflush(stdout);
			//switch (hr)
			//{
			//	case S_OK: std::cout<< "CoInitializeEx: S_OK :" << hr <<"\n";fflush(stdout);break;
			//	case S_FALSE: std::cout<< "CoInitializeEx: S_FALSE :" << hr <<"\n";fflush(stdout);break;
			//	case RPC_E_CHANGED_MODE: std::cout<< "CoInitializeEx: RPC_E_CHANGED_MODE :" << hr <<"\n";fflush(stdout);break;
			//	default: std::cout<< "CoInitializeEx: " << hr <<"\n";fflush(stdout);break;
			//}
        break;
    case DLL_PROCESS_DETACH:
    case DLL_THREAD_DETACH:
        ::CoUninitialize();
		std::cout<< "CoUninitialize \tThreadID: 0x" <<std::hex<< GetCurrentThreadId();
		std::cout<< "\tProcess ID: 0x" <<std::hex<< CoGetCurrentProcess() <<"\n";fflush(stdout);
        break;
    }

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
