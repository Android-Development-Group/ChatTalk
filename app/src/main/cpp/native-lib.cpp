#include <jni.h>
#include <string>

extern "C"

/*
 * Class:     com_jusenr_chat_jninative_NativeContent
 * Method:    stringFromJNI
 * Signature: ([BIIZIIII)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_com_jusenr_chat_jninative_NativeContent_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
