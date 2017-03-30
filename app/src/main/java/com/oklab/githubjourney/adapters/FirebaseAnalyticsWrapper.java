package com.oklab.githubjourney.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by olgakuklina on 2017-03-29.
 */

public class FirebaseAnalyticsWrapper {
    private FirebaseAnalytics firebaseAnalytics;

    public FirebaseAnalyticsWrapper(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean fbanalytics = sharedPref.getBoolean("fbanalytics_switch", true);
        if (fbanalytics) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
    }

    public void logEvent(String name, Bundle bundle) {
        if (firebaseAnalytics != null) {
            firebaseAnalytics.logEvent(name, bundle);
        }
    }

    public void setCurrentScreen(Activity activity, String name) {
        if (firebaseAnalytics != null) {
            firebaseAnalytics.setCurrentScreen(activity, name, null);
        }
    }
}
