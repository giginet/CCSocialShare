#include "CCSocialManager.h"
#import <Social//Social.h>

USING_NS_CC;

namespace CCSocialShare {
    SocialManager::SocialManager()
    {
    }
    
    SocialManager::~SocialManager()
    {
    }
    
    bool SocialManager::isAvailable(Service service)
    {
        if (service == Service::TWITTER) {
            return [SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter];
        } else {
            return [SLComposeViewController isAvailableForServiceType:SLServiceTypeFacebook];
        }
        return false;
    }
    
    void SocialManager::postMessage(Service service, const char *message, SocialManagerCompletionCallback callback)
    {
        postMessage(service, message, "", callback);
    }
    
    void SocialManager::postMessage(Service service, const char *message, const char *imagePath, SocialManagerCompletionCallback callback)
    {
        NSString *serviceType = nil;
        if (service == Service::TWITTER) {
            serviceType = SLServiceTypeTwitter;
        } else if (service == Service::FACEBOOK) {
            serviceType = SLServiceTypeFacebook;
        }
        if ([SLComposeViewController isAvailableForServiceType:serviceType]) {
            SLComposeViewController *composeController = [SLComposeViewController composeViewControllerForServiceType:serviceType];
            NSString *text = [NSString stringWithCString:message encoding:NSUTF8StringEncoding];
            [composeController setInitialText:text];
            
            if (imagePath != nullptr) {
                NSString *path = [NSString stringWithCString:imagePath encoding:NSUTF8StringEncoding];
                UIImage *image = [UIImage imageWithContentsOfFile:path];
                if (image != nil) {
                    [composeController addImage:image];
                }
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
