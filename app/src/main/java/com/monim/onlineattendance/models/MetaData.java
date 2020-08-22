package com.monim.onlineattendance.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MetaData implements Serializable {

    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("from")
    private int fromItem;

    @SerializedName("last_page")
    private int lastPage;

    @SerializedName("path")
    private String paiPath;

    @SerializedName("per_page")
    private int itemPerPage;

    @SerializedName("to")
    private int toItem;

    @SerializedName("total")
    private int totalItem;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getFromItem() {
        return fromItem;
    }

    public int getLastPage() {
        return lastPage;
    }

    public String getPaiPath() {
        return paiPath;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public int getToItem() {
        return toItem;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setFromItem(int fromItem) {
        this.fromItem = fromItem;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public void setPaiPath(String paiPath) {
        this.paiPath = paiPath;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

    public void setToItem(int toItem) {
        this.toItem = toItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public String footerText() {
        return "Page: " + currentPage + " (" + fromItem + "-" + toItem + " of " + totalItem + ")";
    }

    public boolean previousEnabled() {
        return currentPage > 1;
    }

    public boolean startEnabled() {
        return currentPage > 1;
    }

    public boolean nextEnabled() {
        return currentPage < lastPage;
    }

    public boolean endEnabled() {
        return currentPage < lastPage;
    }
}
