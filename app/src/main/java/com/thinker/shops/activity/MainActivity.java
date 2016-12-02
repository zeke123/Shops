package com.thinker.shops.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.thinker.shops.MyApplication;
import com.thinker.shops.R;
import com.thinker.shops.bean.BackData;
import com.thinker.shops.bean.DataItem;
import com.thinker.shops.bean.JsonDatas;
import com.thinker.shops.volley.VolleyInterface;
import com.thinker.shops.volley.VolleyRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity
{

    public static final String TAG = "MainActivity";
    private String mShopName;
    private String mStatus;
    private BackData mData;
    private long mId;
    private ArrayList<DataItem> mList;
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

    @Override
    protected void onStop()
    {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("myTAG");
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

    protected void showDialog()
    {
        final Dialog dialog = new Dialog(MainActivity.this,R.style.customExitDailogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.delete_project_dialog, null);
        Button confirm_btn = (Button) view.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
       final EditText shops_name = (EditText) view.findViewById(R.id.shops_name);

        confirm_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mShopName = shops_name.getText().toString().trim();

                if(!TextUtils.isEmpty(mShopName)){
                    dialog.dismiss();
                    initDatas(mShopName);
                }else {
                    Toast.makeText(MainActivity.this, "请输入社区店域名", Toast.LENGTH_SHORT).show();
                    return;
                }
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

    private void initDatas(String name)
    {

        //"http://dev.wecity.co/task/mall/paddemo/selectCommunityByDomain.do"
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("domain", name);
        VolleyRequest.requestPost(getApplicationContext(),
                "http://dev.wecity.co/task/mall/paddemo/selectCommunityByDomain.do", "myTAG", params, new VolleyInterface(
                        MainActivity.this, VolleyInterface.mListener,
                        VolleyInterface.mErrorListener)
                {

                    private String mData;
                    private String mStatus;
                    @Override
                    public void onMySuccess(String resault)
                    {
                        Log.e(TAG, "resault============"+resault.toString());
                        JSONObject  jsonObject = JSONObject.parseObject(resault);
                        mStatus = jsonObject.getString("status");
                        Log.e(TAG, "mStatus============"+mStatus);
                        if("OK".equals(mStatus))
                        {
                            Gson gson = new Gson();
                            JsonDatas  mDatas = gson.fromJson(resault.toString(), JsonDatas.class);
                            MainActivity.this.mData = mDatas.getData();
                            mId = MainActivity.this.mData.getObjectId();
                            mList = MainActivity.this.mData.getProductList();
                            startShopsDetail();

                        }
                        else if("ERROR".equals(mStatus)){
                            mData = jsonObject.getString("data");
                            Toast.makeText(getApplicationContext(),mData, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startShopsDetail()
    {
        Intent intent =  new Intent(MainActivity.this,ShopsDetailActivity.class);
        intent.putExtra("mId",mId+"");
        intent.putExtra("mList",(Serializable) mList);
        startActivity(intent);
    }
}