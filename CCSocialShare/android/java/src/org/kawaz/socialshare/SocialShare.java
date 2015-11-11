package org.kawaz.socialshare;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.cocos2dx.lib.Cocos2dxActivity;

public class SocialShare {
    static public void postToTwitter(String message) {
        String url = "http://twitter.com/share?text=" + message;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Context context = Cocos2dxActivity.getContext();
        context.startActivity(intent);
    }
}
