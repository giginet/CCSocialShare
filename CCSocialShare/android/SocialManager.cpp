#include "platform/android/jni/JniHelper.h"

namespace CCSocialShare {
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
    
    bool SocialManager::init(CCSocialShare::Service service)
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
    }
};
