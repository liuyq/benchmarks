LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := drivespeed2lib
LOCAL_SRC_FILES := drivespeed2.c

include $(BUILD_SHARED_LIBRARY)
