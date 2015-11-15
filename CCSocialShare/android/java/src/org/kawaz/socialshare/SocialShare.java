package org.kawaz.socialshare;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.cocos2dx.lib.Cocos2dxActivity;

import java.io.File;

public class SocialShare {
    static public void postToTwitter(String message, String imagePath) {
        String url = "http://twitter.com/share?text=" + message;
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(url));
        if (!imagePath.equals("")) {
            System.out.println(imagePath);
            System.out.println("image path!!!!!!");
            try {
                File file = new File(imagePath);
                System.out.println("File exist");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            } catch (Exception e) {
            }
        }
        Context context = Cocos2dxActivity.getContext();
        context.startActivity(intent);
    }
}
