package com.thinker.shops.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.thinker.shops.MyApplication;
import com.thinker.shops.R;
import com.thinker.shops.bean.DataItem;
import com.thinker.shops.utils.DensityUtils;
import com.thinker.shops.view.MenuGridView;
import com.thinker.shops.volley.VolleyInterface;
import com.thinker.shops.volley.VolleyRequest;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhoujian on 2016/11/16.
 */

public class ShopsDetailActivity extends Activity {

    public static final String TAG = "ShopsDetailActivity";
    @InjectView(R.id.im_personal)
    ImageButton mImPersonal;
    @InjectView(R.id.im_start)
    ImageButton mImStart;
    @InjectView(R.id.im_flush)
    ImageButton mImFlush;
    @InjectView(R.id.mGridViewImage)
    MenuGridView mMGridViewImage;
    private int mWidth;
    private String commuityOid;
    private long objectId;
    private ArrayList<DataItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shops_detail);
        ButterKnife.inject(this);
        //获取屏幕的的宽度
        mWidth = DensityUtils.getScreenWidth(ShopsDetailActivity.this);
        commuityOid = getIntent().getStringExtra("mId");
        mList = (ArrayList<DataItem>) getIntent().getSerializableExtra("mList");
        initData();
    }

    private void initData() {
        MyAdater adater = new MyAdater();
        mMGridViewImage.setAdapter(adater);
        mMGridViewImage.setSelector(R.color.transparent);
    }

    class MyAdater extends BaseAdapter {
        private String mShowimg;

        public int getCount() {
            if (mList != null && mList.size() > 0) {
                return mList.size();
            }
            return 0;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            final ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(ShopsDetailActivity.this, R.layout.shops_detail_item, null);
                holder = new ViewHolder();
                holder.im_picture = (ImageView) view.findViewById(R.id.im_picture);
                holder.im_botoom = (ImageView) view.findViewById(R.id.im_botoom);
                holder.im_delete = (ImageView) view.findViewById(R.id.im_delete);
                holder.im_download = (ImageView) view.findViewById(R.id.im_download);
                holder.im_watch = (ImageView) view.findViewById(R.id.im_watch);
                holder.im_not_watch = (ImageView) view.findViewById(R.id.im_not_watch);
                holder.ll_bottom_right = (LinearLayout) view.findViewById(R.id.ll_bottom_right);
                holder.tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.im_picture.getLayoutParams();
                //图片的宽度
                int pictureWidth = (mWidth - DensityUtils.dp2px(ShopsDetailActivity.this, 30)) / 3;
                params.width = pictureWidth;
                params.height = pictureWidth;
                //浮层的宽度
                FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) holder.im_botoom.getLayoutParams();
                param.width = pictureWidth;
                param.height = pictureWidth;
                view.setTag(holder);
            }
            if (mList != null && mList.size() > 0) {
                mShowimg = mList.get(position).getShowimg();
                String newShowimg = mShowimg.replace("/home/thinker/wwwroot/", "http://");
                Glide.with(ShopsDetailActivity.this).load(newShowimg).into(holder.im_picture);
                holder.tv_product_name.setText(mList.get(position).getProductName());

                holder.im_download.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        objectId = mList.get(position).getObjectId();
                        //点击请求服务器获取新的图片
                        initPictureData(holder.im_botoom, holder.ll_bottom_right, holder.im_download);
                    }
                });
            }
            return view;
        }

        class ViewHolder {
            ImageView im_picture;
            ImageView im_botoom;
            ImageView im_download;
            ImageView im_delete;
            ImageView im_watch;
            ImageView im_not_watch;
            LinearLayout ll_bottom_right;
            TextView tv_product_name;
        }
    }

    private void initPictureData(final ImageView view,final LinearLayout ll,final ImageView downImag) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("objectId", objectId + "");
        params.put("commuityOid", commuityOid);

        VolleyRequest.requestPost(getApplicationContext(), "http://192.168.1.57:8080/task/mall/paddemo/postimg.do", "myTag", params, new VolleyInterface(ShopsDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String resault)
            {
                //Toast.makeText(getApplicationContext(), resault.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "resault============" + resault.toString());
                view.setVisibility(View.GONE);
                ll.setVisibility(View.VISIBLE);
                downImag.setVisibility(View.GONE);
            }

            @Override
            public void onMyError(VolleyError error)
            {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("myTag");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
