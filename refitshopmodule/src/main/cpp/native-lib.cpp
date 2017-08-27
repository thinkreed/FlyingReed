//
// Created by thinkreed on 2017/8/27.
//

#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_think_reed_refitshopmodule_controller_activity_WrapperActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}