package com.oklab.githubjourney;

import android.app.Application;

import io.github.kbiakov.codeview.classifier.CodeProcessor;

/**
 * Created by olgakuklina on 5/17/17.
 */

public class GHJApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CodeProcessor.init(this);
    }
}
