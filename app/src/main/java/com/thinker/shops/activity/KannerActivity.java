package com.thinker.shops.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import com.thinker.shops.R;
import java.util.ArrayList;
import krelve.view.Kanner;

/**
 * Created by zhoujian on 2016/11/18.
 */

public class KannerActivity extends Activity {

    private Kanner kanner;
    private ArrayList<String> mList;
    private String[] mStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_kanner);

        kanner = (Kanner) findViewById(R.id.kanner);

        mList = getIntent().getStringArrayListExtra("pictureList");
        if (mList != null && mList.size() > 0) {
            mStrings = mList.toArray(new String[mList.size()]);
            kanner.setImagesUrl(mStrings);
        }

        // kanner.setImagesUrl(new String[] {
        // "http://img04.muzhiwan.com/2015/06/16/upload_557fd293326f5.jpg",
        // "http://img03.muzhiwan.com/2015/06/05/upload_557165f4850cf.png",
        // "http://img02.muzhiwan.com/2015/06/11/upload_557903dc0f165.jpg",
        // "http://img04.muzhiwan.com/2015/06/05/upload_5571659957d90.png",
        // "http://img03.muzhiwan.com/2015/06/16/upload_557fd2a8da7a3.jpg" });
    }

    @Override
    protected void onDestroy()
    {
        kanner.removeCallbacksAndMessages();
        super.onDestroy();
    }
}
