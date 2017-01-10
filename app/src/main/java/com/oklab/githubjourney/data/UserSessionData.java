package com.oklab.githubjourney.data;

/**
 * Created by olgakuklina on 2017-01-09.
 */

public class UserSessionData {

    private final String id;
    private final String credentials;
    private final String token;


    public static UserSessionData createUserSessionDataFromString(String data) {
       String[] array =  data.split(";");
        return new UserSessionData(array[0], array[1], array[2]);
    }
    public UserSessionData(String id, String credentials, String token) {
        this.id = id;
        this.credentials = credentials;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getCredentials() {
        return credentials;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return id + ";" + credentials + ";" + token;
    }
}
