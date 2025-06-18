#pragma once 

#include <jni.h>
#include <android/log.h>
#include <cstdlib>
#include <string>
#include <vector>
#include <list>
#include <unistd.h>
#include <algorithm>
#include <cmath>
#include <iostream>

#include "shadowhook.h"
#include "dependencies/obfuscator/obfusheader.h"
#include "ARMHook.h"
#include "game/patches.h"
#include "str_obfuscate.hpp"

extern uintptr_t g_libGTAVC;
#define GTA(a) (g_libGTAVC + MAKEOBF((a)))

extern char const* g_pStorage;
extern JNIEnv *mEnv;

uint32_t GetTickCount();

#define LOG_TAG MAKEOBF("VCMP")
#define LOGI(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)