package com.thinker.shops.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhoujian on 2016/11/17.
 */

public class MyDbOpenHelper extends SQLiteOpenHelper {
    public MyDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


       /* //商品的id
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
        private String isWatch;*/


        //String sql = "create table user(id int,name varchar(20))";


        db.execSQL("create table picturetable(productId int," +
                " productName varchar(20)," +
                " oldictureUrl varchar(20)," +
                " newictureUrl varchar(20)," +
                "isDelele varchar(20)," +
                "isWatch varchar(20));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {

    }
}
