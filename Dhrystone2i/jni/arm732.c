 #include <stdlib.h>
 #include <stdio.h>
 #include "jni.h"

 char results[1000];


 jstring
 Java_com_dhry2i_Dhrystone2iActivity_stringFromJNI2( JNIEnv* env, jobject thiz2)
 {
     sprintf(results, "           Compiled for 32 bit ARM v7a\n\n");
     return (*env)->NewStringUTF(env, results);
 }
