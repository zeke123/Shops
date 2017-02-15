package com.thinker.shops.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.thinker.shops.R;
import com.thinker.shops.bean.BackData;
import com.thinker.shops.bean.DataItem;
import com.thinker.shops.bean.JsonDatas;
import com.thinker.shops.utils.SharedPreferencesUtils;
import com.thinker.shops.volley.VolleyInterface;
import com.thinker.shops.volley.VolleyRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by zhoujian on 2017/1/11.
 */

public class SplashActivity extends Activity
{

    private String name;
    private BackData mData;
    private long mId;
    private ArrayList<DataItem> mList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        name =  SharedPreferencesUtils.getString(SplashActivity.this,"SHOPING_NAME",null);

        new Handler(){
            @Override
            public void handleMessage(Message msg)
            {


                if(TextUtils.isEmpty(name)){

                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }else{


                    initDatas(name);
                }




            }
        }.sendEmptyMessageDelayed(0,2000);
    }



    private void initDatas(final String name)
    {

        //"http://dev.wecity.co/task/mall/paddemo/selectCommunityByDomain.do"
        //http://laimihui.china1h.cn
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("domain", name);

        VolleyRequest.requestPost(getApplicationContext(),
                "http://laimihui.china1h.cn/task/mall/paddemo/selectCommunityByDomain.do", "myTAG", params, new VolleyInterface(
                        SplashActivity.this, VolleyInterface.mListener,
                        VolleyInterface.mErrorListener)
                {

                    private String mData;
                    private String mStatus;
                    @Override
                    public void onMySuccess(String resault)
                    {

                        SharedPreferencesUtils.saveString(SplashActivity.this,"SHOPING_NAME",name);

                        Log.e(TAG, "resault============"+resault.toString());
                        JSONObject jsonObject = JSONObject.parseObject(resault);
                        mStatus = jsonObject.getString("status");
                        Log.e(TAG, "mStatus============"+mStatus);
                        if("OK".equals(mStatus))
                        {
                            Gson gson = new Gson();
                            JsonDatas mDatas = gson.fromJson(resault.toString(), JsonDatas.class);
                            SplashActivity.this.mData = mDatas.getData();
                            mId = SplashActivity.this.mData.getObjectId();
                            mList = SplashActivity.this.mData.getProductList();
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
                        Toast.makeText(getApplicationContext(),"网络连接错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startShopsDetail()
    {
        Intent intent =  new Intent(SplashActivity.this,ShopsDetailActivity.class);
        intent.putExtra("mId",mId+"");
        intent.putExtra("mList",(Serializable) mList);
        startActivity(intent);
        finish();
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
