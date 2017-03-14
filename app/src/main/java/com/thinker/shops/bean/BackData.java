package com.thinker.shops.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhoujian on 2016/11/17.
 */

public class BackData implements Serializable
{

    private static final long serialVersionUID = 1L;
    private long  objectId;
    private ArrayList<DataItem> productList;
    public long getObjectId() {
        return objectId;
    }
    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }
    public ArrayList<DataItem> getProductList()
    {
        return productList;
    }
    public void setProductList(ArrayList<DataItem> productList) {
        this.productList = productList;
    }

    @Override
    public String toString()
    {
        return "BackData{" + "objectId=" + objectId +", productList=" + productList +'}';
    }
}
