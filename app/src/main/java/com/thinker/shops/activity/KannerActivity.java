package com.thinker.shops.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.thinker.shops.ConstantValue;
import com.thinker.shops.R;

import java.util.ArrayList;

import krelve.view.Kanner;

/**
 * Created by zhoujian on 2016/11/18.
 */

public class KannerActivity extends Activity
{

    private Kanner kanner;
    private ArrayList<String> mList;
    private String[] mStrings;
    private int postion;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_kanner);

        kanner = (Kanner) findViewById(R.id.kanner);
        postion= getIntent().getIntExtra("postion",0);
        //mList = getIntent().getStringArrayListExtra("pictureList");
        mList =  ConstantValue.pictureList;
        if (mList != null && mList.size() > 0)
        {
            mStrings = mList.toArray(new String[mList.size()]);
            kanner.setImagesUrl(mStrings,postion);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}