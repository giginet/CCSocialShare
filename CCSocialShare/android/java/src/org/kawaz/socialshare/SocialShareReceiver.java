package org.kawaz.socialshare;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import com.twitter.sdk.android.tweetcomposer.*;

public class SocialShareReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
        SocialShare.runSuccessCallback();
    } else {
        SocialShare.runFailureCallback();
    }
  }
}
