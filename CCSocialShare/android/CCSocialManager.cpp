#include "SocialManager.h"
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
        this->postMessage(message, nullptr, callback);
    }

    void SocialManager::postMessage(const char *message, cocos2d::Image *image, SocialManagerCompletionCallback callback)
    {
        auto env = cocos2d::JniHelper::getEnv();
        cocos2d::JniMethodInfo methodInfo;
        if (cocos2d::JniHelper::getStaticMethodInfo(methodInfo, SOCIAL_SHARE_CLASS_NAME, "postToTwitter", "(Ljava/lang/String;)V") ) {
            jstring jstr = env->NewStringUTF(message);
            methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID, jstr);
        }
    }
};
