package com.oklab.gitjourney.data;

/**
 * Created by olgakuklina on 2017-01-09.
 */

public class UserSessionData {

    private final String id;
    private final String credentials;
    private final String token;
    private final String login;


    public UserSessionData(String id, String credentials, String token, String login) {
        this.id = id;
        this.credentials = credentials;
        this.token = token;
        this.login = login;
    }

    public static UserSessionData createUserSessionDataFromString(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        String[] array = data.split(";");
        return new UserSessionData(array[0], array[1], array[2], array[3]);
    }

    public String getLogin() {
        return login;
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
        return id + ";" + credentials + ";" + token + ";" + login;
    }
}
