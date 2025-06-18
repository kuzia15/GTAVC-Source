//
// Created by vadim on 11.06.2025.
//
#include "../main.h"
#include "patches.h"
#include "CHooks.h"
#include "dependencies/armhook/patch.h"

void DisableAutoAim()
{
    CHook::RET(OBF("_ZN10CPlayerPed22FindWeaponLockOnTargetEv")); // CPed::FindWeaponLockOnTarget
    CHook::RET(OBF("_ZN10CPlayerPed26FindNextWeaponLockOnTargetEP7CEntityb")); // CPed::FindNextWeaponLockOnTarget
    CHook::RET("_ZN4CPed21SetWeaponLockOnTargetEP7CEntity"); // CPed::SetWeaponLockOnTarget
}

void ApplyPatches()
{
    LOGI(MAKEOBF("> Inject pacthes..."));

    DisableAutoAim();

    CHook::CallFunction<void>(OBF("_Z17SunnyWeatherCheatv"));

    CHook::RET(OBF("_Z12WeaponCheat1v")); // CCheat::WeaponCheat1
    CHook::RET(OBF("_Z12WeaponCheat2v")); // CCheat::WeaponCheat2
    CHook::RET( OBF("_Z12WeaponCheat3v")); // CCheat::WeaponCheat3

    CHook::RET(OBF("_ZN7CWanted14SetWantedLevelEi"));
    CHook::RET(OBF("_ZN7CWanted13RegisterCrimeE10eCrimeTypeRK7CVectorjb"));
    CHook::RET(OBF("_ZN7CWanted6UpdateEv"));
    CHook::RET(OBF("_ZN7CWanted10InitialiseEv"));

    CHook::RET(OBF("_ZN12CCutsceneMgr16LoadCutsceneDataEPKc")); // LoadCutsceneData
    CHook::RET(OBF("_ZN12CCutsceneMgr10InitialiseEv"));      // CCutsceneMgr::Initialise

    //CHook::RET("_ZN9CMessages7DisplayEv")); // CMessages::Display

    CHook::RET(OBF("_ZN13cMusicManager10InitialiseEv")); // All music and radio

    CHook::RET(OBF("_ZN9CMessages7ProcessEv")); //CMessages::Process
}

