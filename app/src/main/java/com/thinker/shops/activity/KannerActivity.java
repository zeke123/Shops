package com.thinker.shops.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.thinker.shops.R;

import java.util.ArrayList;
import java.util.Iterator;

import krelve.view.Kanner;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by zhoujian on 2016/11/18.
 */

public class KannerActivity extends Activity
{

    private Kanner kanner;
    private ArrayList<String> mList;
    private String[] mStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_kanner);

        String  mPath = getIntent().getStringExtra("mStringPath");

        kanner = (Kanner) findViewById(R.id.kanner);

        mList = getIntent().getStringArrayListExtra("pictureList");

        mList.add(mPath);

        for (Iterator iter = mList.iterator(); iter.hasNext(); )
        {
            String str = (String) iter.next();
            if (mPath!=null && mPath.equals(str))
            {
                iter.remove();
            }
        }
        mList.add(0,mPath);

        if (mList != null && mList.size() > 0)
        {
            mStrings = mList.toArray(new String[mList.size()]);
            kanner.setImagesUrl(mStrings);
        }
    }

    @Override
    protected void onDestroy()
    {
        kanner.removeCallbacksAndMessages();
        super.onDestroy();
    }
}