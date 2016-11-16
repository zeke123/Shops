package com.thinker.shops.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import com.thinker.shops.R;
import com.thinker.shops.view.MenuGridView;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhoujian on 2016/11/16.
 */

public class ShopsDetailActivity extends Activity {

    @InjectView(R.id.im_personal)
    ImageButton mImPersonal;
    @InjectView(R.id.im_start)
    ImageButton mImStart;
    @InjectView(R.id.im_flush)
    ImageButton mImFlush;
    @InjectView(R.id.mGridViewImage)
    MenuGridView mMGridViewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_detail);
        ButterKnife.inject(this);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
