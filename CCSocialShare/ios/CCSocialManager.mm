#include "CCSocialManager.h"
#import <Social//Social.h>

USING_NS_CC;

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
        return [SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter];
    }
    
    void SocialManager::postMessage(const char *message, SocialManagerCompletionCallback callback)
    {
        this->postMessage(message, "", callback);
    }
    
    void SocialManager::postMessage(const char *message, const char *imagePath, SocialManagerCompletionCallback callback)
    {
        if ([SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter]) {
            NSString *serviceType = SLServiceTypeTwitter;
            SLComposeViewController *composeController = [SLComposeViewController composeViewControllerForServiceType:serviceType];
            NSString *text = [NSString stringWithCString:message encoding:NSUTF8StringEncoding];
            [composeController setInitialText:text];
            
            
            // Convert cocos2d::Image UIImage
            if (imagePath != nullptr) {
                auto filepath = std::string(imagePath);
                //auto fullpath = cocos2d::FileUtils::get 

                
                
            }
            
            UIWindow* window = [[UIApplication sharedApplication] keyWindow];
            UIViewController* controller = window.rootViewController;
            [controller presentViewController:composeController
                                     animated:YES
                                   completion:^{
                                       [composeController setCompletionHandler:^(SLComposeViewControllerResult result) {
                                           if (result == SLComposeViewControllerResultDone) {
                                               [controller dismissViewControllerAnimated:YES completion:nil];
                                               if (callback) {
                                                   callback(PostResult::SUCCEED);
                                               }
                                           } else if (result == SLComposeViewControllerResultCancelled) {
                                               if (callback) {
                                                   callback(PostResult::CANCELED);
                                               }
                                           }
                                       }];
                                   }];
            
        }
    }
}
