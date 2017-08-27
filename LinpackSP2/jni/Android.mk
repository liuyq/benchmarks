LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := linpacklib3


 ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_SRC_FILES = linpack2.c  arm732.c
 endif
 ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    LOCAL_SRC_FILES = linpack2.c  arm864.c
 endif

 ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_SRC_FILES = linpack2.c  intel32.c
 endif
 ifeq ($(TARGET_ARCH_ABI),x86_64) 
    LOCAL_SRC_FILES = linpack2.c  intel64.c
  endif 

 ifeq ($(TARGET_ARCH_ABI),mips)
    LOCAL_SRC_FILES = linpack2.c  mips32.c
endif
 ifeq ($(TARGET_ARCH_ABI),mips64) 
    LOCAL_SRC_FILES = linpack2.c  mips64.c
 endif 

include $(BUILD_SHARED_LIBRARY)
