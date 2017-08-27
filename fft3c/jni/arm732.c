 #include <stdlib.h>
 #include <stdio.h>
 #include "jni.h"

 char results[1000];


 jstring
 Java_com_fft3c_fft3cActivity_stringFromJNI2( JNIEnv* env, jobject thiz2)
 {
     sprintf(results, "           Compiled for 32 bit ARM v7a\n");
     return (*env)->NewStringUTF(env, results);
 }
