//
// Created by Administrator on 2020/11/5.
//


#include <jni.h>
#include <android/log.h>
#include "client/linux/handler/minidump_descriptor.h"
#include "client/linux/handler/exception_handler.h"

bool DumpCallback(const google_breakpad::MinidumpDescriptor &descriptor,
                  void *context,
                  bool succeeded) {
    __android_log_print(ANDROID_LOG_ERROR, "native", "native crash:%s", descriptor.path());
    return false;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_gacrnd_gcs_crash_CrashReport_initNativeCrash(JNIEnv *env, jclass type, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);

    __android_log_print(ANDROID_LOG_INFO, "native", "===> %s", path);
    google_breakpad::MinidumpDescriptor descriptor(path);
    // 必须使用static延长生命周期，不然出了方法，就会调用析构方法释放掉handler
    static google_breakpad::ExceptionHandler eh(descriptor, NULL, DumpCallback,
                                                NULL, true, -1);

    env->ReleaseStringUTFChars(path_, path);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_gacrnd_gcs_crash_CrashReport_testNativeCrash(JNIEnv *env, jclass type) {

    __android_log_print(ANDROID_LOG_INFO, "native", "xxxxxxxxxx");

    int *p = NULL;
    *p = 10;
}