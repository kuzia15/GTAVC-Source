#include "../main.h"
#include "game.h"
#include "patch.h"

void InitScripting();
//void InstallHooks();

CGame::CGame()
{
    m_pGameCamera = new CCamera();
}


void CGame::InitialiseOnceBeforeRW() {
    CHook::CallFunction<void>(g_libGTAVC+(VER_x32?0x1624C0+1:0x212DC4));
}


void CGame::Process()
{
    ((void(*)())(g_libGTAVC + (VER_x32 ? 0x00163BBC + 1 : 0x215A18)))(); // CPad::UpdatePads()
}

void CGame::InjectHooks()
{
    //CHook::Redirect("_ZN5CGame22InitialiseOnceBeforeRWEv", &CGame::InitialiseOnceBeforeRW);
    //CHook::Redirect("_ZN5CGame7ProcessEv", &CGame::Process);
}

#ifdef IS_ARM64
#define ADDR_GAMESTATE 0x57AED0
#elif defined(IS_ARM32)
#define ADDR_GAMESTATE 0x7152BC
#endif

void CGame::InitGame()
{
    LOGI(MAKEOBF("libGTAVC.so: Game has been inited!"));
        //ApplyPatches();
        InitScripting();
        ApplyPatches();
        //InstallHooks();
}


