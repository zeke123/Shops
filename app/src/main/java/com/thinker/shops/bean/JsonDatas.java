package com.thinker.shops.bean;

import java.io.Serializable;

/**
 * Created by zhoujian on 2016/11/17.
 */

public class JsonDatas implements Serializable {


    private static final long serialVersionUID = -3911585324063896233L;
    private String status;
    private BackData  data;


    public BackData getData() {
        return data;
    }

    public void setData(BackData data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "JsonDatas{" + "data=" + data + ", status='" + status + '\'' + '}';
    }
}
