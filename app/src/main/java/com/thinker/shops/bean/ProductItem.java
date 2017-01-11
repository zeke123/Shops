package com.thinker.shops.bean;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/1/10.
 */

public class ProductItem implements Serializable {


    private static final long serialVersionUID = 1L;

    private long objectId;
    private String productName;
    private String flagStatus;


    public String getFlagStatus() {
        return flagStatus;
    }

    public void setFlagStatus(String flagStatus) {
        this.flagStatus = flagStatus;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ProductItem{" +
                "flagStatus='" + flagStatus + '\'' +
                ", objectId=" + objectId +
                ", productName='" + productName + '\'' +
                '}';
    }
}
