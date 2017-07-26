package org.kawaz.socialshare;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import org.cocos2dx.lib.Cocos2dxActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public class SocialShare {
    public enum PostResult {
        SUCCEED,
        FAILURE,
        CANCELLED
    }
    private static native void executeCallback(int code);
    final static String TWITTER_PACKAGE_NAME = "com.twitter.android";
    final static String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    final static int REQUEST_CODE = 100;
    private static TwitterAuthClient authClient = null;

    static private void postToTwitter(final String message, final String imagePath) {
        TwitterConfig config = new TwitterConfig.Builder(Cocos2dxActivity.getContext()).debug(true).build();
        Twitter.initialize(config);
        Context context = Cocos2dxActivity.getContext();
        authClient = new TwitterAuthClient();
        authClient.authorize((Activity) context, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Context context = Cocos2dxActivity.getContext();
                TwitterSession session = result.data;
                ComposerActivity.Builder builder = new ComposerActivity.Builder(context).session(session).text(message);
                if (!imagePath.equals("")) {
                    try {
                        Uri originalImageUri = Uri.parse(imagePath);
                        Uri readableFileUri = saveImageToExternalDirectory(originalImageUri);
                        if (readableFileUri != null) {
                            builder.image(readableFileUri);
                        }
                    } catch (Exception e) {
                    }
                }
                ((Activity) context).startActivity(builder.createIntent());
            }

            @Override
            public void failure(TwitterException e) {
                Cocos2dxActivity activity = (Cocos2dxActivity) Cocos2dxActivity.getContext();
                activity.showDialog("error", "Twitter login failure");
            }
        });
    }

    static public void postToFacebook(String message, String imagePath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TITLE, message);
        if (!imagePath.equals("")) {
            try {
                Uri originalImageUri = Uri.parse(imagePath);
                String ext = getExtension(originalImageUri);
                Uri readableFileUri = saveImageToExternalDirectory(originalImageUri);
                if (readableFileUri != null) {
                    intent.setType("image/" + ext);
                    intent.putExtra(Intent.EXTRA_STREAM, readableFileUri);
                }
                intent.setPackage(FACEBOOK_PACKAGE_NAME);
            } catch (Exception e) {
            }
        }
        Context context = Cocos2dxActivity.getContext();
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
    }

    static private String getExtension(Uri imagePath) {
        String pathString = imagePath.getPath();
        return pathString.substring(pathString.lastIndexOf(".") + 1);
    }

    static private String getFilename(Uri imagePath) {
        return imagePath.getLastPathSegment();
    }

    static private Uri saveImageToExternalDirectory(Uri imageUri) {
        File dst = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getFilename(imageUri));
        File src = new File(imageUri.getPath());
        try {
            copyFile(src, dst);
        } catch (Exception e) {
            return null;
        }
        return Uri.fromFile(dst);
    }

    static public void copyFile(File src, File dst) throws Exception {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    static public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(authClient != null){
            authClient.onActivityResult(TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE, resultCode, intent);
            authClient = null;
            return;
        }

        if (intent == null) {
            runCancelCallback();
            return;
        }
        runSuccessCallback();
    }

    static public boolean isTwitterAvailable() {
        return isAvailable(TWITTER_PACKAGE_NAME);
    }

    static public boolean isFacebookAvailable() {
        return isAvailable(FACEBOOK_PACKAGE_NAME);
    }

    static public void runSuccessCallback() {
        executeCallback(PostResult.SUCCEED.ordinal());
    }

    static public void runFailureCallback() {
        executeCallback(PostResult.FAILURE.ordinal());
    }

    static public void runCancelCallback() {
        executeCallback(PostResult.CANCELLED.ordinal());
    }

    static private boolean isAvailable(String packageName) {
        Context context = Cocos2dxActivity.getContext();
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> appInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo info : appInfoList) {
            if (packageName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
