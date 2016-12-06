package com.thinker.shops.bean;

/**
 * Created by zhoujian on 2016/12/5.
 */

public class VersionData {

    private String status;

    private Version data;

    public Version getData() {
        return data;
    }

    public void setData(Version data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VersionData{" +
                "data=" + data +
                ", status='" + status + '\'' +
                '}';
    }
}
