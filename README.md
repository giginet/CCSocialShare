# CCSocialShare

`CCSocialShare` provides the feature to share for your games on cocos2d-x.

## Feature

- Multi platforms (iOS/Android)
- Multi services (Twitter/Facebook)
- Easy to integrate
- Easy to share
    - SocialKit (iOS)
    - Intent (Android)
- Image attachment
- Compatible with Android Studio
- Callback function

![](https://raw.githubusercontent.com/giginet/CCSocialShare/master/images/demo_ios.gif)

## Usage

```cpp
#include "CCSocialManager.h"

void onShareButtonTapped() {
    if (CCSocialShare::SocialManager::isAvailable(CCSocialShare::Service::TWITTER)) {
        CCSocialShare::SocialManager::postMessage(CCSocialShare::Service::TWITTER,
                                                  "I beat this game!",
                                                  [](CCSocialShare::PostResult result) {
            if (result == CCSocialShare::PostResult::SUCCEED) {
                // When to post is succeed
                log("Done");
            } else if (result == CCSocialShare::PostResult::CANCELED) {
                // When to post is canceled
                log("Canceled");
            }
        });
    }
}
```

## Add ScreenShot

```cpp
#include "CCSocialManager.h"

void onShareButtonTapped() {
    if (CCSocialShare::SocialManager::isAvailable(CCSocialShare::Service::TWITTER)) {
        Size size = Director::getInstance()->getWinSize();
        RenderTexture* texture = RenderTexture::create((int)size.width, (int)size.height);
        texture->setPosition(Point(size.width / 2, size.height / 2));
        texture->begin();
        Director::getInstance()->getRunningScene()->visit();
        texture->end();

        texture->saveToFile("screenshot.png",
                            Image::Format::PNG,
                            true,
                            [&](RenderTexture* rt, const std::string& path) {
            CCSocialShare::SocialManager::postMessage(CCSocialShare::Service::TWITTER,
                                                      "I beat this game!",
                                                      path.c_str(), [](CCSocialShare::PostResult result) {
                if (result == CCSocialShare::PostResult::SUCCEED) {
                    // When to post is succeed
                    log("Done");
                } else if (result == CCSocialShare::PostResult::CANCELED) {
                    // When to post is canceled
                    log("Canceled");
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
                   ../../../Classes/external/CCSocialShare/CCSocialShare/android/CCSocialManager.cpp

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../../Classes \
                    $(LOCAL_PATH)/../../../Classes/external/CCSocialShare/CCSocialShare
```

5: Edit `proj.android-studio/app/src/org/cocos2dx/cpp/AppActivity.java` to execute callback function.

```java
import org.kawaz.socialshare.SocialShare;
...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        SocialShare.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
```

6: If you use twitter share then you have to get new Twitter API consumer and secret keys from https://apps.twitter.com/ and put your key and secret to `proj.android-studio/app/res/values/twitter.xml` like this.

```xml
<resources>
  <string name="com.twitter.sdk.android.CONSUMER_KEY">YOUR_CONSUMER_KEY</string>
  <string name="com.twitter.sdk.android.CONSUMER_SECRET">YOUR_CONSUMER_SECRET</string>
</resources>
```

note. The twitter app require `Read and Write` permission.

7: Execute following command

```shell
$ cocos compile -p android --android-studio
```

8: Build and Run on Android Studio
