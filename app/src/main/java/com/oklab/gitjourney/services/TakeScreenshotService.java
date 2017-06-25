package com.oklab.gitjourney.services;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by olgakuklina on 2017-03-26.
 */

public class TakeScreenshotService {

    private static final String TAG = TakeScreenshotService.class.getSimpleName();
    private final Activity activity;

    public TakeScreenshotService(Activity activity) {
        this.activity = activity;
    }

    public void takeScreenShot() {
        View v1 = activity.getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap myBitmap = v1.getDrawingCache();
        String filePath = saveBitmap(myBitmap);
        v1.setDrawingCacheEnabled(false);
        if (filePath != null) {
            sendMail(filePath);
        }
    }

    private String saveBitmap(Bitmap bitmap) {
        Utils.verifyStoragePermissions(activity);
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + activity.getString(R.string.local_path);
        File imagePath = new File(filePath);
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            return filePath;
        } catch (IOException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    private void sendMail(String path) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                activity.getString(R.string.screenshot_msg_title));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                activity.getString(R.string.screenshot_msg_extra_text));
        emailIntent.setType(activity.getString(R.string.content_type));
        Uri myUri = Uri.parse(activity.getString(R.string.content_path) + path);
        emailIntent.putExtra(Intent.EXTRA_STREAM, myUri);
        activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.screenshot_msg_desc)));
    }
}
