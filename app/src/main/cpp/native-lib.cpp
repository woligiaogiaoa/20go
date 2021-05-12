#include <jni.h>
#include <string> //in the incldue dirs

extern "C"{

#include "libavcodec/avcodec.h"

}

extern "C" JNIEXPORT jstring JNICALL
Java_com_jsn_a20k_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}