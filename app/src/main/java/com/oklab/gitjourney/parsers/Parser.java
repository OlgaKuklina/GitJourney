package com.oklab.gitjourney.parsers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by olgakuklina on 2017-03-25.
 */

public interface Parser<T> {
    T parse(JSONObject jsonObject) throws JSONException;
}
