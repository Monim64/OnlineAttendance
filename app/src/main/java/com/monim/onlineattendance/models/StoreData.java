package com.monim.onlineattendance.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreData {

    @SerializedName("data")
    private List<StoreItem> storeList;

    @SerializedName("links")
    private Links footerLinks;

    @SerializedName("meta")
    private MetaData metaData;

    public List<StoreItem> getStoreList() {
        return storeList;
    }

    public Links getFooterLinks() {
        return footerLinks;
    }

    public MetaData getMetaData() {
        return metaData;
    }
}
