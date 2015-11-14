#ifndef CCSocialManager_h
#define CCSocialManager_h

#include "cocos2d.h"

namespace CCSocialShare {
    enum class Service {
        TWITTER
    };
    
    enum class PostResult {
        SUCCEED,
        FAILURE,
        CANCELED
    };
    
    using SocialManagerCompletionCallback = std::function<void (PostResult)>;
    
    class SocialManager :public cocos2d::Ref {
    protected:
        bool init(Service service);
    public:
        static SocialManager *createWithService(Service service);
        bool isAvailable();
        void postMessage(const char* message, SocialManagerCompletionCallback callback);
        void postMessage(const char* message, const char* imagePath, SocialManagerCompletionCallback callback);
        CC_SYNTHESIZE_READONLY(Service, _service, Service);
    };
}

#endif /* CCSocialManager_h */
