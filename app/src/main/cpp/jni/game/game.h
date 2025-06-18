#pragma once

#include "camera.h"

class CGame
{
public:
    CGame();
    ~CGame() {};

    void InitGame();
    void Process();
    void InjectHooks();
    void InitialiseOnceBeforeRW();
private:
    CCamera* m_pGameCamera;
};