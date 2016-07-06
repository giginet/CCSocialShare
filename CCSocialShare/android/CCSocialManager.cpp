#include "CCSocialManager.h"
#include "platform/android/jni/JniHelper.h"

namespace CCSocialShare {
    const char* SOCIAL_SHARE_CLASS_NAME = "org/kawaz/socialshare/SocialShare";
    SocialManager::SocialManager()
    {
    }

    SocialManager::~SocialManager()
    {
    }

    bool SocialManager::isAvailable(Service service)
    {
        return true;
    }

    void SocialManager::postMessage(Service service, 
            const char *message, 
            SocialManagerCompletionCallback callback)
    {
        postMessage(service, message, "", callback);
    }

    SocialManagerCompletionCallback SocialManager::_callback = [](PostResult){};

    void SocialManager::postMessage(Service service,
            const char *message, const char *imagePath,
            SocialManagerCompletionCallback callback)
    {
        _callback = callback;
        auto env = cocos2d::JniHelper::getEnv();
        cocos2d::JniMethodInfo methodInfo;
        std::string methodName;
        if (service == Service::TWITTER) {
            methodName = "postToTwitter";
        } else if (service == Service::FACEBOOK) {
            methodName = "postToFacebook";
        }
        if (cocos2d::JniHelper::getStaticMethodInfo(methodInfo, SOCIAL_SHARE_CLASS_NAME, methodName.c_str(), "(Ljava/lang/String;Ljava/lang/String;)V") ) {
            jstring messageStr = env->NewStringUTF(message);
            jstring imagePathStr = env->NewStringUTF(imagePath);
            methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID, messageStr, imagePathStr);
        }
    }
};

extern "C"
{
	void Java_org_kawaz_socialshare_SocialShare_callCallback(JNIEnv *env, jobject thiz, jint code)
	{
    CCSocialShare::SocialManager::callCallback(code);
	}
}
