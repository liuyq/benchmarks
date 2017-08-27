 #include <stdlib.h>
 #include <stdio.h>
 #include "jni.h"

 char results[1000];


 jstring
 Java_com_dhry2i_Dhrystone2iActivity_stringFromJNI2( JNIEnv* env, jobject thiz2)
 {
     sprintf(results, "           Compiled for 64 bit ARM v8a\n\n");
     return (*env)->NewStringUTF(env, results);
 }
