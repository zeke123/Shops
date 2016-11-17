package com.thinker.shops.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.thinker.shops.R;
import com.thinker.shops.bean.DataItem;
import com.thinker.shops.utils.DensityUtils;
import com.thinker.shops.view.MenuGridView;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhoujian on 2016/11/16.
 */

public class ShopsDetailActivity extends Activity
{
    @InjectView(R.id.im_personal)
    ImageButton mImPersonal;
    @InjectView(R.id.im_start)
    ImageButton mImStart;
    @InjectView(R.id.im_flush)
    ImageButton mImFlush;
    @InjectView(R.id.mGridViewImage)
    MenuGridView mMGridViewImage;
    private int mWidth;
    private String mId;
    private ArrayList<DataItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shops_detail);
        ButterKnife.inject(this);
        //获取屏幕的的宽度
        mWidth = DensityUtils.getScreenWidth(ShopsDetailActivity.this);
        mId = getIntent().getStringExtra("mId");
        mList = (ArrayList<DataItem>)getIntent().getSerializableExtra("mList");
        initData();
    }

    private void initData() {
        MyAdater adater = new MyAdater();
        mMGridViewImage.setAdapter(adater);
        mMGridViewImage.setSelector(R.color.transparent);
    }
    class MyAdater extends BaseAdapter
    {
        private String mShowimg;
        public int getCount()
        {
            if(mList!=null && mList.size()>0){
                return mList.size();
            }
            return 0;
        }

        public Object getItem(int position)
        {
            return null;
        }

        public long getItemId(int position)
        {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view;
            final ViewHolder holder;
            if (convertView != null)
            {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            else
            {
                view = View.inflate(ShopsDetailActivity.this, R.layout.shops_detail_item, null);
                holder = new ViewHolder();
                holder.im_picture = (ImageView)view.findViewById(R.id.im_picture);
                holder.im_botoom = (ImageView)view.findViewById(R.id.im_botoom);
                holder.im_delete = (ImageView)view.findViewById(R.id.im_delete);
                holder.im_download = (ImageView)view.findViewById(R.id.im_download);
                holder.im_watch = (ImageView)view.findViewById(R.id.im_watch);
                holder.im_not_watch = (ImageView)view.findViewById(R.id.im_not_watch);
                holder.ll_bottom_right = (LinearLayout)view.findViewById(R.id.ll_bottom_right);
                FrameLayout.LayoutParams  params  = (FrameLayout.LayoutParams )holder.im_picture.getLayoutParams();
                //图片的宽度
                int pictureWidth = (mWidth-DensityUtils.dp2px(ShopsDetailActivity.this,30))/3;
                params.width=pictureWidth;
                params.height=pictureWidth;
                //浮层的宽度
                FrameLayout.LayoutParams  param = (FrameLayout.LayoutParams )holder.im_botoom.getLayoutParams();
                param.width=pictureWidth;
                param.height=pictureWidth;
                view.setTag(holder);
            }
            if(mList!=null && mList.size()>0){
                mShowimg = mList.get(position).getShowimg();
                Glide.with(ShopsDetailActivity.this).load(mShowimg).into(holder.im_picture );
            }
            return view;
        }

         class ViewHolder
         {
            ImageView im_picture;
            ImageView im_botoom;
            ImageView im_download;
            ImageView im_delete;
            ImageView im_watch;
            ImageView im_not_watch;
            LinearLayout ll_bottom_right;
        }
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
