package com.thinker.shops.bean;

import java.io.Serializable;

/**
 * Created by zhoujian on 2016/11/17.
 */

public class DataItem implements Serializable
{



    private static final long serialVersionUID = 1L;
    private long objectId;
    private String productName;
    private String padshowimg;
    private int productId;
    private String newictureUrl;
    private String isWatch;


    public String getIsWatch() {
        return isWatch;
    }

    public void setIsWatch(String isWatch) {
        this.isWatch = isWatch;
    }

    public String getNewictureUrl() {
        return newictureUrl;
    }

    public void setNewictureUrl(String newictureUrl) {
        this.newictureUrl = newictureUrl;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getPadshowimg() {
        return padshowimg;
    }

    public void setPadshowimg(String padshowimg) {
        this.padshowimg = padshowimg;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    @Override
    public String toString() {
        return "DataItem{" +
                "isWatch='" + isWatch + '\'' +
                ", objectId=" + objectId +
                ", productName='" + productName + '\'' +
                ", padshowimg='" + padshowimg + '\'' +
                ", productId=" + productId +
                ", newictureUrl='" + newictureUrl + '\'' +
                '}';
    }
}
