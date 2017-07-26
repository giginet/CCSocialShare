package org.kawaz.socialshare;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.util.Log;
import org.cocos2dx.lib.Cocos2dxActivity;
import com.twitter.sdk.android.tweetcomposer.*;

public class SocialShareReceiver extends BroadcastReceiver {
  private static final String TAG = "org.kawaz.socialshare.SocialshareReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
        Log.d(TAG, "onReceive SocialShareReceiver SUCCESS");
        SocialShare.runSuccessCallback();
    } else {
        Log.d(TAG, "onReceive SocialShareReceiver FAILURE");
        Cocos2dxActivity activity = (Cocos2dxActivity) Cocos2dxActivity.getContext();
        activity.showDialog("error", "You should retry after some hours.");
        SocialShare.runFailureCallback();
    }
  }
}
