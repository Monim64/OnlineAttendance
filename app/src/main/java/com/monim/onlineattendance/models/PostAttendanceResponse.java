package com.monim.onlineattendance.models;

import com.google.gson.annotations.SerializedName;

public class PostAttendanceResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("app_message")
    private String appMessage;

    @SerializedName("user_message")
    private String userMessage;

    public int getCode() {
        return code;
    }

    public String getAppMessage() {
        return appMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
