 #include <stdlib.h>
 #include <stdio.h>
 #include "jni.h"

 char results[1000];


 jstring
 Java_com_bus2i_BusSpdActivity2i_stringFromJNI2( JNIEnv* env, jobject thiz2)
 {
     sprintf(results, "           Compiled for 32 bit Intel x86\n");
     return (*env)->NewStringUTF(env, results);
 }
