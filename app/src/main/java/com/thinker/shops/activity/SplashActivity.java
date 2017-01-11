package com.thinker.shops.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.thinker.shops.R;

/**
 * Created by zhoujian on 2017/1/11.
 */

public class SplashActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        }.sendEmptyMessageDelayed(0,2000);
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
