package com.thinker.shops.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.thinker.shops.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity {

    @InjectView(R.id.tv_binder)
    TextView mTvBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initEvent();
    }

    private void initEvent()
    {
        mTvBinder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //弹出对话框
                showDialog();
            }
        });
    }

    protected void showDialog() {
        final Dialog dialog = new Dialog(MainActivity.this,R.style.customExitDailogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.delete_project_dialog, null);
        Button confirm_btn = (Button) view.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                startShopsDetail();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.customExitDailogStyle);
        window.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    private void startShopsDetail() {
         startActivity(new Intent(MainActivity.this,ShopsDetailActivity.class));
    }
}
