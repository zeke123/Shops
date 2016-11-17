package com.thinker.shops.bean;

import java.io.Serializable;

/**
 * Created by zhoujian on 2016/11/17.
 */

public class DataItem implements Serializable{

    private static final long serialVersionUID = 1L;


    private long objectId;
    private String productName;

    private String showimg;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getShowimg() {
        return showimg;
    }

    public void setShowimg(String showimg) {
        this.showimg = showimg;
    }

    @Override
    public String toString() {
        return "DataItem{" +
                "objectId=" + objectId +
                ", productName='" + productName + '\'' +
                ", showimg='" + showimg + '\'' +
                '}';
    }
}
