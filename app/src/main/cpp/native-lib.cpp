#include <jni.h>
#include <string> //in the incldue dirs

#include "android/bitmap.h"

#include <memory>

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

    const char * nativeString=env->GetStringUTFChars(path, 0);
    {
        jstring formatName;

        if(avformat_open_input( &avFormatContext,nativeString, nullptr, nullptr)){

            std::string empty;
            formatName=env->NewStringUTF(empty.c_str());

        } else{
            auto name = avFormatContext->iformat->long_name;
            formatName=env->NewStringUTF(name);

        }

        env->ReleaseStringUTFChars(path, nativeString);

        return formatName;
    }

}















