#pragma once

inline jstring GetPackageName(JNIEnv *env, jobject jActivity)
{
    jmethodID method = env->GetMethodID(env->GetObjectClass(jActivity), OBF("getPackageName"), OBF("()Ljava/lang/String;"));
    return (jstring)env->CallObjectMethod(jActivity, method);
}

inline jobject GetGlobalActivity(JNIEnv *env)
{
    jclass activityThread = env->FindClass(OBF("android/app/ActivityThread"));
    jmethodID currentActivityThread = env->GetStaticMethodID(activityThread, OBF("currentActivityThread"), OBF("()Landroid/app/ActivityThread;"));
    jobject at = env->CallStaticObjectMethod(activityThread, currentActivityThread);
    jmethodID getApplication = env->GetMethodID(activityThread, OBF("getApplication"), OBF("()Landroid/app/Application;"));
    jobject context = env->CallObjectMethod(at, getApplication);
    return context;
}

inline void toasty(const char* txt, int msDuration = 3500)
{
    jclass ToastClass = mEnv->FindClass(OBF("android/widget/Toast"));
    jmethodID makeTextMethodID = mEnv->GetStaticMethodID(ToastClass, OBF("makeText"), OBF("(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;"));
    jmethodID showMethodID = mEnv->GetMethodID(ToastClass, OBF("show"), OBF("()V"));

    jstring message = mEnv->NewStringUTF(txt);
    jint duration = msDuration;

    jobject toast = mEnv->CallStaticObjectMethod(ToastClass, makeTextMethodID, GetGlobalActivity(mEnv), message, duration);
    mEnv->CallVoidMethod(toast, showMethodID);
    
    mEnv->DeleteLocalRef(message);
}