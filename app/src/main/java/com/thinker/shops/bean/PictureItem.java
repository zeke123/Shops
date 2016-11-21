package com.thinker.shops.bean;

import java.io.Serializable;

/**
 * Created by zhoujian on 2016/11/18.
 */

public class PictureItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    //商品的id
    private int productId;

    //商品名
    private String productName;

    //原来图片的url地址
    private String oldictureUrl;


    //点击下载后的图片的url地址
    private String newictureUrl;

    //是否被删除
    private String isDelele;

    //是否可见
    private String isWatch;


    public String getIsDelele() {
        return isDelele;
    }

    public void setIsDelele(String isDelele) {
        this.isDelele = isDelele;
    }

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

    public String getOldictureUrl() {
        return oldictureUrl;
    }

    public void setOldictureUrl(String oldictureUrl) {
        this.oldictureUrl = oldictureUrl;
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

    @Override
    public String toString() {
        return "PictureItem{" +
                "isDelele='" + isDelele + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", oldictureUrl='" + oldictureUrl + '\'' +
                ", newictureUrl='" + newictureUrl + '\'' +
                ", isWatch='" + isWatch + '\'' +
                '}';
    }
}
