package com.thinker.shops.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.thinker.shops.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhoujian on 2016/11/18.
 */

public class SingleImgActivity extends Activity
{

    @InjectView(R.id.img_bottom)
    ImageView mImgBottom;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_single);
        ButterKnife.inject(this);
        mPath = getIntent().getStringExtra("mStringPath");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap bm = BitmapFactory.decodeFile(mPath, options);
        mImgBottom.setImageBitmap(bm);

    }
}
