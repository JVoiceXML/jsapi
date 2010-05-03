#pragma once

#include "targetver.h"

#define WIN32_LEAN_AND_MEAN             // Selten verwendete Teile der Windows-Header nicht einbinden.
// Windows-Headerdateien:

#include <sapi.h>
#include <sphelper.h>
#include "resource.h"

#include <atlbase.h>
#include <atlwin.h>
extern CComModule _Module;
#include <atlcom.h>