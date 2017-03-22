package com.oklab.githubjourney.data;

import org.json.JSONObject;

/**
 * Created by olgakuklina on 2017-03-22.
 */

public class HTTPConnectionResult {
    private final String result;
    private final int responceCode;

    public HTTPConnectionResult(String result, int responceCode) {
        this.result = result;
        this.responceCode = responceCode;
    }

    public String getResult() {
        return result;
    }

    public int getResponceCode() {
        return responceCode;
    }
}
