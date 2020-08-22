package com.monim.onlineattendance.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class PostAttendance {

    @SerializedName("name")
    private String userName;

    @SerializedName("uid")
    private String userId;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("request_id")
    private String uniqueRequestId;

    public PostAttendance() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUniqueRequestId() {
        return uniqueRequestId;
    }

    public void setUniqueRequestId(String uniqueRequestId) {
        this.uniqueRequestId = uniqueRequestId;
    }

    public boolean isUserNameEmpty() {
        if (TextUtils.isEmpty(getUserName()))
            return true;

        return getUserName().trim().length() == 0;
    }

    public boolean isUserIdEmpty() {
        if (TextUtils.isEmpty(getUserId()))
            return true;

        return getUserId().trim().length() == 0;
    }
}
