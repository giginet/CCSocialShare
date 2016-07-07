#ifndef CCSocialManager_h
#define CCSocialManager_h

#include "cocos2d.h"

namespace CCSocialShare {
    enum class Service {
        TWITTER,
        FACEBOOK
    };
    
    enum class PostResult {
        SUCCEED,
        FAILURE,
        CANCELED
    };
    
    using SocialManagerCompletionCallback = std::function<void (PostResult)>;
    class SocialManager :public cocos2d::Ref {
    public:
        SocialManager();
        virtual ~SocialManager();
        static bool isAvailable(Service service);
        static void postMessage(Service service, const char* message, SocialManagerCompletionCallback callback);
        static void postMessage(Service service, const char* message, const char* imagePath, SocialManagerCompletionCallback callback);
#ifdef __ANDROID__
        static SocialManagerCompletionCallback _callback;
        static void executeCallback(int result) {
                _callback(static_cast<PostResult>(result));
        }
#endif
    };
}

#endif /* CCSocialManager_h */
