#include "main.h"
#include "jniutil.h"
#include "CHooks.h"
#define LOGI(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
uintptr_t g_libGTAVC = 0; // libGTAVC
const char* g_pAPKPackage;
char const* g_pStorage = nullptr;
bool bNetworkInited = false;
#include "dependencies/Dobby/include/dobby.h"
#include "game/game.h"
#include "game/hooks.h"
#include "patch.h"

void InitRenderWareFunctions();

jobject appContext;
JavaVM *mVm;
JNIEnv *mEnv;

CGame *pGame = 0;

void (*TouchEvent)(int, int, int posX, int posY);

void TouchEvent_hook(int type, int num, int posX, int posY) {
    return TouchEvent(type, num, posX, posY);
}

void WNGTA() 
{
#ifdef IS_ARM64

    DobbyHook((void *)GTA(0x45D4E8), (void *)TouchEvent_hook, (void **)&TouchEvent);
#elif defined(IS_ARM32)
    DobbyHook((void *)GTA(0x3199EC), (void *)TouchEvent_hook, (void **)&TouchEvent);

#endif
}

extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    LOGI(OBF("VC:MP initializing..."));


    WATERMARK("████████████████████████████████████████"
              "████████████▓▒▒▒░░░░░░░░▒▒▒▓████████████"
              "███████░────────────────────────▒███████"
              "████▒──────────────────────────────▒████"
              "███▒─────────────────────────────────███"
              "███──────────────────────────────────▓██"
              "██░───────────────────────────────────██"
              "██───░███████▒────────────▒███████░───██"
              "██──▓█▓░████████░──────░████████░▓█▓──██"
              "█▓─░░─────░▓█████▒────▓█████▓░─────░──▓█"
              "█▒───────────▓██▓─────░▒██▓───────────▓█"
              "█░─────────────██──────██─────────────▒█"
              "█░───▒███████▒──██────░▓──▒███████▒───░█"
              "█░─▒███████████─██──────░███████████▒░░█"
              "█░─████▓▓▓▓▓▓▒░─██░──────▒▒░░░░░▒░░░░─▒█"
              "█░──────────────██░───────────────────░█"
              "██─────────────░██░───────────────────░█"
              "███────────────▓██────────────────────██"
              "██▓█──────────▒███─────░▒───────────░███"
              "██─████▒▒▓▒░░█████─────▒██──▒▓▒░░▒▒█▒███"
              "███─█▓██────▒█▒─██───────▓░─░▒░▒████─███"
              "███▒─█▒██───────█▓─────────────██─█─▒███"
              "████░─█▓███▓░───██▒▒▒▓█░────░███─█▒─████"
              "█████──█▓▒█████████▓███████████░█▓─█████"
              "██████──██──▒█████░──███████▒──█▓─██████"
              "███████──██▓──────░░░░░░──────█▒─███████"
              "████████──██▓░▒▒░░───────────█▒─████████"
              "█████████──█▒──░░▒████▒────░█░─█████████"
              "██████████─░█─────███▒─────▒░─██████████"
              "███████████░─────▒████───────███████████"
              "█████████████────█████─────░████████████"
              "██████████████───▓████────▓█████████████"
              "███████████████───███░──░███████████████"
              "█████████████████▒███▒▒█████████████████"
              "████████████████████████████████████████");
    
	g_libGTAVC = ARMHook::getLibraryAddress(MAKEOBF("libGTAVC.so"));
	if(g_libGTAVC)
	{
        CHook::InitHookStuff();
        WNGTA();
        Hooks::InstallCHooks();
        InitRenderWareFunctions();
        pGame->InitGame();
	}
    else
    {
        LOGE(OBF("ERROR: libGTAVC.so not found!"));
        return JNI_ERR;
    }

    g_pStorage = MAKEOBF("/storage/emulated/0/Android/data/com.rockstargames.gtavc/files/");

	return JNI_VERSION_1_6;
}

uint32_t GetTickCount()
{
	struct timeval tv;
	gettimeofday(&tv, nullptr);

	return (tv.tv_sec*1000 + tv.tv_usec/1000);
}