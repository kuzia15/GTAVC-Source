//
// Created by vadim on 11.06.2025.
//
#include "../main.h"
#include "patches.h"
#include "CHooks.h"
#include "dependencies/armhook/patch.h"
#include "hooks.h"
#include "game/RW/RenderWare.h"
#include "game/RW/immedi.h"

void Hooks::InstallCHooks()
{
    CHooks::InitHooksKuzia();
    ((void (*)()) (g_libGTAVC + (VER_x32 ? 0x1D6F2C + 1 : 0x2ACDC4)))(); // CCoronas::DoSunAndMoon()

//#if VER_x32
    //CHook::InlineHook(g_libGTAVC + 0x1DD490, &CDraw__SetFOV_hook, &CDraw__SetFOV);
//#else
    //CHook::InlineHook(g_libGTAVC + 0x109050, &CDraw__SetFOV_hook, &CDraw__SetFOV);
//#endif
}