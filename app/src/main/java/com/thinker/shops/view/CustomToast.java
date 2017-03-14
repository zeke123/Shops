package com.thinker.shops.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.thinker.shops.R;

/**
 * Created by zhoujian on 2017/3/13.
 */

public class CustomToast {

    private static TextView mTextView;
    private static TextView title;

    public static void showToast(Context context, String message) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast, null);
        //初始化布局控件
        mTextView = (TextView) toastRoot.findViewById(R.id.message);
        title = (TextView) toastRoot.findViewById(R.id.title);
        //为控件设置属性
        mTextView.setText(message);
        title.setText("提示信息");
        //Toast的初始化
        Toast toastStart = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 2);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }
}
