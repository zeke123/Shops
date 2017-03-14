package com.thinker.shops;

import java.util.ArrayList;

/**
 * Created by zhoujian on 2017/2/22.
 */

public interface ConstantValue
{

    //正式地址
    String COMMEN = "http://laimihui.china1h.cn/";
    //测试地址
    //String COMMEN = "http://dev.wecity.co/";

    //http://laimihui.china1h.cn/task/mall/paddemo/selectCommunityByDomain.do
    //http://laimihui.china1h.cn/task/mall/paddemo/postimg.do?objectId=" + objectId + "&communityOid=" + commuityOid;

    //登录的API
    String LOGIN_URL =COMMEN+ "task/mall/paddemo/selectCommunityByDomain.do";
    //下载的API
    //String DOWNLOAD_URL =COMMEN+ "task/mall/paddemo/postimg.do?objectId=" + objectId + "&communityOid=" + commuityOid;

    //用于要播放的图片
    ArrayList<String> pictureList = new ArrayList<String>();

}
