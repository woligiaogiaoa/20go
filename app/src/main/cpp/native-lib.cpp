#include <jni.h>
#include <string> //in the incldue dirs

#include "android/bitmap.h"

#include <memory>

#include <android/log.h>

extern "C" {

#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include "libavutil/imgutils.h"
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_aaaaa_a2k_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_aaaaa_a2k_MainActivity_getVideoFormatName(JNIEnv *env, jobject thiz, jstring path) {

    AVFormatContext *avFormatContext = nullptr;

    const char *nativeString = env->GetStringUTFChars(path, 0);
    {
        jstring formatName;

        if (avformat_open_input(&avFormatContext, nativeString, nullptr, nullptr)) {

            std::string empty;
            formatName = env->NewStringUTF(empty.c_str());

        } else {
            auto name = avFormatContext->iformat->name;
            formatName = env->NewStringUTF(name);

        }

        env->ReleaseStringUTFChars(path, nativeString);

        return formatName;
    }

}












const  char* TAG="native_lib";




extern "C"
JNIEXPORT jboolean JNICALL
Java_com_aaaaa_a2k_MainActivity_showVideoPreview(JNIEnv *env, jobject thiz, jstring path,
                                                 jobject jbitmap) {

    //todo what
    AVFormatContext *avFormatContext = nullptr;

    //todo what
    const char *mediaFilePath = env->GetStringUTFChars(path, 0);

    if (avformat_open_input(&avFormatContext, mediaFilePath, nullptr, nullptr)) {
        //error occur
        //fixme :释放
        __android_log_print(ANDROID_LOG_ERROR,TAG,"avformat_open_input fail");
        return static_cast<jboolean>(false);
    }

    if (avformat_find_stream_info(avFormatContext, nullptr)) {
        //fixme:where
        return static_cast<jboolean>(false);
    }

    AVCodec *codec = nullptr;
    AVCodecParameters *codecParameters = nullptr;
    int targetIndex;

    for (int i = 0; i < avFormatContext->nb_streams; i++) {
        codecParameters = avFormatContext->streams[i]->codecpar;

        if (codecParameters->codec_type == AVMEDIA_TYPE_VIDEO) {
            codec = avcodec_find_decoder(codecParameters->codec_id);
            targetIndex = i;
            break;
        }
    }


    AndroidBitmapInfo bitmapInfo;

    AndroidBitmap_getInfo(env, jbitmap, &bitmapInfo);

    AVCodecContext *videoCodecContext = avcodec_alloc_context3(codec);

    avcodec_parameters_to_context(videoCodecContext, codecParameters);

    avcodec_open2(videoCodecContext, codec, nullptr);


    SwsContext *scaleContext(sws_getContext(
                codecParameters->width,
            codecParameters->height,
            videoCodecContext->pix_fmt,
            bitmapInfo.width,
            bitmapInfo.height,
            AV_PIX_FMT_RGBA,
            SWS_BICUBIC, nullptr, nullptr, nullptr
    ));

    AVPacket *packet = av_packet_alloc();
    AVFrame *frame = av_frame_alloc();

    while (av_read_frame(avFormatContext, packet) >= 0) {
        if (packet->stream_index == targetIndex) {
            avcodec_send_packet(videoCodecContext, packet);
            int response = avcodec_receive_frame(videoCodecContext, frame);

            if (response == AVERROR(EAGAIN)) {
                continue;
            }

            if (response > 0) {

                AVFrame *frameForDrawing = av_frame_alloc();
                void *bitmapBuffer;
                AndroidBitmap_lockPixels(env, jbitmap, &bitmapBuffer);
                av_image_fill_arrays(
                        frameForDrawing->data,
                        frameForDrawing->linesize,
                        static_cast<const uint8_t *>(bitmapBuffer),
                        AV_PIX_FMT_RGBA,
                        bitmapInfo.width,
                        bitmapInfo.height,
                        1
                );

                sws_scale(
                        scaleContext,
                        frame->data,
                        frame->linesize,
                        0,
                        codecParameters->height,
                        frameForDrawing->data,
                        frameForDrawing->linesize
                );
                AndroidBitmap_unlockPixels(env, jbitmap);
                break;
            }

        }
    }

    av_packet_unref(packet);
    av_packet_free(&packet);
    av_frame_free(&frame);
    sws_freeContext(scaleContext);
    avcodec_free_context(&videoCodecContext);
    return static_cast<jboolean>(true);


}













