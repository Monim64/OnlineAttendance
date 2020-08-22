package com.monim.onlineattendance.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreItem implements Serializable {

    @SerializedName("id")
    private int storeId;

    @SerializedName("name")
    private String storeName;

    @SerializedName("address")
    private String storeAddress;

    public int getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }
}
