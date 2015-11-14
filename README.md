# CCSocialShare

`CCSocialShare` provides to the feature to share for your games on cocos2d-x.

## Feature

- Multi platforms (iOS/Android)
- Multi services (Currently Twitter only)
- Easy to integrate
- Easy to share
    - SocialKit (iOS)
    - Intent (Android)
- Compatible with Android Studio

## Usage

```cpp
#include "CCSocialManager.h"

void onShareButtonTapped() {
    auto socialManager = CCSocialShare::SocialManager::createWithService(CCSocialShare::Service::TWITTER);
    if (socialManager->isAvailable()) {
        socialManager->postMessage("I beat this game!", [](CCSocialShare::PostResult result) {
            if (result == CCSocialShare::PostResult::SUCCEED) {
                // When to post is succeed
            } else if (result == CCSocialShare::PostResult::CANCELED) {
                // When to post is canceled
            }
        });
    }
}
```

## Add ScreenShot

```cpp
#include "CCSocialManager.h"

USING_NS_CC;

void onShareButtonTapped() {
    auto socialManager = CCSocialShare::SocialManager::createWithService(CCSocialShare::Service::TWITTER);
    if (socialManager->isAvailable()) {
        Size size = Director::getInstance()->getWinSize();
        RenderTexture* texture = RenderTexture::create((int)size.width, (int)size.height);
        texture->setPosition(Point(size.width / 2, size.height / 2));
        texture->begin();
        Director::getInstance()->getRunningScene()->visit();
        texture->end();

        texture->saveToFile("screenshot.png", Image::Format::PNG, true, [&](RenderTexture* rt, const std::string& path) {
            socialManager->postMessage("I beat this game!", path.c_str(), [](CCSocialShare::PostResult result) {
                if (result == CCSocialShare::PostResult::SUCCEED) {
                    // When to post is succeed
                } else if (result == CCSocialShare::PostResult::CANCELED) {
                    // When to post is canceled
                }
            });
        });
    }
}
```

## Requirements

### iOS

- Latest Xcode (7.0+)
- cocos2d-x 3.x

### Android

- Latest Android Studio (1.5+)
- cocos2d-x 3.7+
    - Because of Android Studio support.

## How to integrate

### General settings

```sh
$ cocos new YourFantasticGame -l cpp
$ cd YourFantasticGame
$ git clone https://github.com/giginet/CCSocialShare.git Classes/external/CCSocialShare
```

### for iOS

1. Add `CCSocialManager.{h,mm}` into your Xcode project.
2. Add `Social.framework`
    - Target 'YourFantasticGame iOS' > General > Linked Frameworks and Libraries
3. Build and Run

### for Android

1: Open `proj.android-studio` with Android Studio

2: Edit `proj.android-studio/settings.gradle` like following

```gradle
include ':libcocos2dx'
project(':libcocos2dx').projectDir = new File(settingsDir, '../cocos2d/cocos/platform/android/libcocos2dx')
include ':YourFantasticGame'
project(':YourFantasticGame').projectDir = new File(settingsDir, 'app')
// Add this setting
include ':socialshare'
project(':socialshare').projectDir = new File(settingsDir, '../Classes/external/CCSocialShare/CCSocialShare/android')
```

3: Edit `proj.android-studio/app/build.gradle`

```gradle
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile project(':libcocos2dx')
    // Add this line
    compile project(':socialshare')
}
```

4: Edit `proj.android-studio/app/jni/Android.mk`

```makefile
LOCAL_SRC_FILES := hellocpp/main.cpp \
                   ../../../Classes/AppDelegate.cpp \
                   ../../../Classes/HelloWorldScene.cpp \
                   ../../../Classes/external/CCSocialShare/CCSocialShare/android/SocialManager.cpp

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../../Classes \
                    $(LOCAL_PATH)/../../../Classes/external/CCSocialShare/CCSocialShare
```

5: Execute following command

```shell
$ cocos compile -p android --android-studio
```

6: Build and Run on Android Studio
