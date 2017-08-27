LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := drivespeedlib
LOCAL_SRC_FILES := drivespeed.c

include $(BUILD_SHARED_LIBRARY)
