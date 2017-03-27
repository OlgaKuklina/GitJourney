package com.oklab.githubjourney.services;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.oklab.githubjourney.utils.Utils;

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
                + File.separator + "Pictures/screenshot.png";
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
                "User contributions activity screenshot");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "This is a contributions activity screenshot mail from GitHubJourney app");
        emailIntent.setType("image/png");
        Uri myUri = Uri.parse("file://" + path);
        emailIntent.putExtra(Intent.EXTRA_STREAM, myUri);
        activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}
