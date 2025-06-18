LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := Dobby
LOCAL_SRC_FILES := $(LOCAL_PATH)/dependencies/Dobby/$(TARGET_ARCH_ABI)/libdobby.a
LOCAL_SHARED_LIBRARIES += shadowhook
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := VCMP

FILE_LIST := $(wildcard $(LOCAL_PATH)/*.cpp)
FILE_LIST += $(wildcard $(LOCAL_PATH)/game/*.cpp)
FILE_LIST += $(wildcard $(LOCAL_PATH)/game/RW/RenderWare.cpp)
FILE_LIST += $(wildcard $(LOCAL_PATH)/dependencies/armhook/*.cpp)
LOCAL_SRC_FILES := $(FILE_LIST:$(LOCAL_PATH)/%=%)

FILE_LIST_H := $(wildcard $(LOCAL_PATH)/dependencies/armhook/*.h)
LOCAL_C_INCLUDES := $(LOCAL_PATH) $(LOCAL_PATH)/dependencies/armhook

LOCAL_CPPFLAGS := -w -s -fvisibility=hidden -pthread -Wall -fpack-struct=1 -O2 -std=c++14 -fexceptions

ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_CPPFLAGS += -DVER_x32=true
else ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    LOCAL_CPPFLAGS += -DVER_x32=false
endif

LOCAL_LDLIBS := -llog -lEGL -lGLESv2
LOCAL_STATIC_LIBRARIES := Dobby

include $(BUILD_SHARED_LIBRARY)

$(call import-module,prefab/shadowhook)