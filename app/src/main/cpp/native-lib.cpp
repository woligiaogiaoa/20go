#include <jni.h>
#include <string> //in the incldue dirs

#include "android/bitmap.h"

extern "C"{

#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"

}

extern "C" JNIEXPORT jstring JNICALL
Java_com_aaaaa_a2k_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_aaaaa_a2k_MainActivity_getVideoFormatName(JNIEnv *env, jobject thiz, jstring path) {

    AVFormatContext *avFormatContext= nullptr;

    const char *nativeString = env->GetStringUTFChars(path, 0);

    //Do something with the nativeString

    if(avformat_open_input( &avFormatContext,nativeString, nullptr, nullptr)){
        env->ReleaseStringUTFChars(path, nativeString);
        std::string empty;
        return env->NewStringUTF(empty.c_str());

    }

    auto name = avFormatContext->iformat->long_name;

    env->ReleaseStringUTFChars(path, nativeString);
    return env->NewStringUTF(name);


}















