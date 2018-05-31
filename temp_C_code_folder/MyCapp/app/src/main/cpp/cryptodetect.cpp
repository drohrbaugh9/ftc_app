#include <jni.h>
#include "smoothkernel.h"

#include "opencv2/objdetect.hpp"
#include "opencv2/videoio.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/imgproc.hpp"

extern "C"
JNIEXPORT jintArray

JNICALL
Java_com_example_pdutoit_mycapp_MainActivity_stringFromJNI(
        JNIEnv *env, jobject thisObj, jlong image) {
    Mat* frame = (Mat*) image;
    jsize len = env->GetArrayLength(image);
    jint *body = env->GetIntArrayElements(image, 0);


    ////////////////////////////////////////////


    // Our code goes here:


    












    /////////////////////////////////////////////

    jint fill[50];
    for (int i = 0; i < 5; i++) {
        fill[i] = 2 * body[i];
    }


    jintArray result;
    result = env->NewIntArray(50);

    env->SetIntArrayRegion(result, 0, 50, fill);

    env->ReleaseIntArrayElements(image, body, 0);
    return result;
}
