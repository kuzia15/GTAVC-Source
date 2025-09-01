cmake_minimum_required(VERSION 3.12)
project(VCMP)

set(CMAKE_CXX_STANDARD 14)

if(${ANDROID_ABI} STREQUAL "armeabi-v7a")
    add_definitions(-DARCH32=true)
elseif(${ANDROID_ABI} STREQUAL "arm64-v8a")
    add_definitions(-DARCH32=false)
endif()

file(GLOB_RECURSE SOURCES
    ${CMAKE_CURRENT_LIST_DIR}/*.cpp
    ${CMAKE_CURRENT_LIST_DIR}/game/*.cpp
    ${CMAKE_CURRENT_LIST_DIR}/game/RW/RenderWare.cpp
    ${CMAKE_CURRENT_LIST_DIR}/dependencies/armhook/*.cpp
)

include_directories(
    ${CMAKE_CURRENT_LIST_DIR}
    ${CMAKE_CURRENT_LIST_DIR}/dependencies/armhook
)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -w -s -fvisibility=hidden -pthread -Wall -fpack-struct=1 -O2 -fexceptions")

add_library(VCMP SHARED ${SOURCES})

add_library(Dobby STATIC IMPORTED)
set_target_properties(Dobby PROPERTIES
    IMPORTED_LOCATION "${CMAKE_CURRENT_LIST_DIR}/dependencies/Dobby/${ANDROID_ABI}/libdobby.a"
)

target_link_libraries(VCMP
    Dobby
    shadowhook
    log
    android
    EGL
    GLESv2
)
