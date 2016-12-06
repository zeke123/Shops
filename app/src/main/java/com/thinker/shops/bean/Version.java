package com.thinker.shops.bean;

import java.io.Serializable;

/**
 * Created by zhoujian on 2016/12/5.
 */

public class Version implements Serializable
{


    private static final long serialVersionUID = 1L;

    private String apk;
    private int appid;
    private String appstore;
    private String downloadurl;
    private String icon;
    private String introduce;
    private String iosversion;
    private int iosversioncode;
    private String qrcode;
    private String sponsor;
    private String support;
    private String supportmobilesite;
    private String supportwebsite;
    private String title;
    private String version;
    private int versioncode;
    private String wechat;

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getAppstore() {
        return appstore;
    }

    public void setAppstore(String appstore) {
        this.appstore = appstore;
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getIosversion() {
        return iosversion;
    }

    public void setIosversion(String iosversion) {
        this.iosversion = iosversion;
    }

    public int getIosversioncode() {
        return iosversioncode;
    }

    public void setIosversioncode(int iosversioncode) {
        this.iosversioncode = iosversioncode;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getSupportmobilesite() {
        return supportmobilesite;
    }

    public void setSupportmobilesite(String supportmobilesite) {
        this.supportmobilesite = supportmobilesite;
    }

    public String getSupportwebsite() {
        return supportwebsite;
    }

    public void setSupportwebsite(String supportwebsite) {
        this.supportwebsite = supportwebsite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }


    @Override
    public String toString() {
        return "Version{" +
                "apk='" + apk + '\'' +
                ", appid=" + appid +
                ", appstore='" + appstore + '\'' +
                ", downloadurl='" + downloadurl + '\'' +
                ", icon='" + icon + '\'' +
                ", introduce='" + introduce + '\'' +
                ", iosversion='" + iosversion + '\'' +
                ", iosversioncode=" + iosversioncode +
                ", qrcode='" + qrcode + '\'' +
                ", sponsor='" + sponsor + '\'' +
                ", support='" + support + '\'' +
                ", supportmobilesite='" + supportmobilesite + '\'' +
                ", supportwebsite='" + supportwebsite + '\'' +
                ", title='" + title + '\'' +
                ", version='" + version + '\'' +
                ", versioncode=" + versioncode +
                ", wechat='" + wechat + '\'' +
                '}';
    }
}
