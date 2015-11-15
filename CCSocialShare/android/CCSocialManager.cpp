#include "CCSocialManager.h"
#include "platform/android/jni/JniHelper.h"

namespace CCSocialShare {
    const char* SOCIAL_SHARE_CLASS_NAME = "org/kawaz/socialshare/SocialShare";
    SocialManager* SocialManager::createWithService(Service service)
    {
        SocialManager *manager = new SocialManager();
        if (manager && manager->init(service)) {
            manager->autorelease();
            return manager;
        }
        CC_SAFE_DELETE(manager);
        return nullptr;
    }
    
    bool SocialManager::init(Service service)
    {
        _service = service;
        return true;
    }
    
    bool SocialManager::isAvailable()
    {
        return true;
    }

    void SocialManager::postMessage(const char *message, SocialManagerCompletionCallback callback)
    {
        this->postMessage(message, "", callback);
    }

    void SocialManager::postMessage(const char *message, const char *imagePath, SocialManagerCompletionCallback callback)
    {
        auto env = cocos2d::JniHelper::getEnv();
        cocos2d::JniMethodInfo methodInfo;
        if (cocos2d::JniHelper::getStaticMethodInfo(methodInfo, SOCIAL_SHARE_CLASS_NAME, "postToTwitter", "(Ljava/lang/String;Ljava/lang/String;)V") ) {
            jstring messageStr = env->NewStringUTF(message);
            jstring imagePathStr = env->NewStringUTF(imagePath);
            methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID, messageStr, imagePathStr);
        }
    }
};
