package com.monim.onlineattendance.models;

import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("first")
    private String firstPageLink;

    @SerializedName("last")
    private String lastPageLink;

    @SerializedName("prev")
    private String previousPageLink;

    @SerializedName("next")
    private String nextPageLink;

    public String getFirstPageLink() {
        return firstPageLink;
    }

    public String getLastPageLink() {
        return lastPageLink;
    }

    public String getPreviousPageLink() {
        return previousPageLink;
    }

    public String getNextPageLink() {
        return nextPageLink;
    }
}
