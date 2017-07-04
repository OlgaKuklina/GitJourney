package com.oklab.gitjourney.utils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by olgakuklina on 2017-04-02.
 */

public class GithubLanguageColorsMatcher {
    private static final String TAG = GithubLanguageColorsMatcher.class.getSimpleName();
    private static final HashMap<Character, String> MAP = new HashMap<>();

    static {
        MAP.put(' ', "");
        MAP.put('\'', "Diez");
        MAP.put('+', "Plus");
        MAP.put('-', "");
        MAP.put('_', "");
    }

    public static int findMatchedColor(Context context, String language) {
        int id = context.getResources().getIdentifier(language, "color", "com.oklab.gitjourney");
        if (id != 0) {
            return id;
        }
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < language.length(); i++) {
            char a = language.charAt(i);
            if (MAP.containsKey(a)) {
                builder.append(MAP.get(a));
            } else {
                builder.append(a);
            }
        }
        Log.v(TAG, "language = " + language);
        Log.v(TAG, "builder = " + builder.toString());

        Log.v(TAG, "id = " + id);
        return id;
    }
}
