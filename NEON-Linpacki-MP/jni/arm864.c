 #include <stdlib.h>
 #include <stdio.h>
 #include "jni.h"

 char results[1000];


 jstring
 Java_com_linpackimp_LinpackMPActivityi_stringFromJNI2( JNIEnv* env, jobject thiz2)
 {
     sprintf(results, "           Compiled for 64 bit ARM v8a\n");
     return (*env)->NewStringUTF(env, results);
 }
