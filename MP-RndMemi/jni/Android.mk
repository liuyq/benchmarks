LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := mprandilib

# LOCAL_CFLAGS := -save-temps

 ifeq ($(TARGET_ARCH_ABI),armeabi-v7a) 
   LOCAL_CFLAGS    += -DHAVE_NEON=1 
   LOCAL_SRC_FILES := mprandmemi.c arm732.c  
 endif 
 ifeq ($(TARGET_ARCH_ABI),arm64-v8a) 
   L OCAL_CFLAGS    += -DHAVE_NEON64=1 
    LOCAL_SRC_FILES = mprandmemi.c  arm864.c 
 endif 

 ifeq ($(TARGET_ARCH_ABI),x86) 
    LOCAL_CFLAGS += -ffast-math -mtune=atom -mssse3 -mfpmath=sse 
    LOCAL_SRC_FILES = mprandmemi.c  intel32.c 
 endif 
 ifeq ($(TARGET_ARCH_ABI),x86_64) 
    LOCAL_CFLAGS += -ffast-math -mtune=slm -msse4.2 
    LOCAL_SRC_FILES = mprandmemi.c  intel64.c 
  endif 

 ifeq ($(TARGET_ARCH_ABI),mips) 
    LOCAL_SRC_FILES = mprandmemi.c  mips32.c 
endif 
 ifeq ($(TARGET_ARCH_ABI),mips64) 
    LOCAL_SRC_FILES = mprandmemi.c  mips64.c 
 endif 

include $(BUILD_SHARED_LIBRARY)
