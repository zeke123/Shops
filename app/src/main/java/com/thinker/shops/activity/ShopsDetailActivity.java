package com.thinker.shops.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.bumptech.glide.Glide;
import com.thinker.shops.R;
import com.thinker.shops.bean.DataItem;
import com.thinker.shops.http.HttpClient;
import com.thinker.shops.utils.DensityUtils;
import com.thinker.shops.view.MenuGridView;
import java.io.FileOutputStream;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhoujian on 2016/11/16.
 */

public class ShopsDetailActivity extends Activity
{

    public static final String TAG = "ShopsDetailActivity";

    private ArrayList<String>  pictureList = new ArrayList<String>();
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
    private String img_path;


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
        clickEvent();
    }

    private void clickEvent()
    {
        mImPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopsDetailActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

        mImStart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(pictureList!=null && pictureList.size()>0){
                Intent intent = new Intent(ShopsDetailActivity.this,KannerActivity.class);
                intent.putStringArrayListExtra("pictureList",pictureList);
                startActivity(intent);
            }else{
                Toast.makeText(ShopsDetailActivity.this, "没有要播放的图片", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    });
    }

    private void initData()
    {
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
                        Log.e(TAG,"objectId=="+objectId);
                        String  pathUrl= "http://192.168.1.57:8080/task/mall/paddemo/postimg.do?objectId=" +objectId+"&communityOid="+commuityOid;
                        Log.e(TAG,"pathUrl=="+pathUrl);
                        pictureList.add(pathUrl);
                        //开启线程下载图片
                        startDownLoaad(pathUrl);
                    }
                    private void startDownLoaad(final String imgUrl)
                    {
                        final  Handler hander = new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                Bitmap bm = BitmapFactory.decodeFile(img_path);
                                Log.e(TAG,"bm==="+bm);
                                Log.e(TAG,"img_path==="+img_path);
                                holder.im_picture.setImageBitmap(bm);
                                bm.recycle();
                                holder.im_botoom.setVisibility(View.GONE);
                                holder.ll_bottom_right.setVisibility(View.VISIBLE);
                                holder.im_download.setVisibility(View.GONE);
                            }
                        };
                        Thread thread = new Thread()
                        {
                            public void run()
                            {
                                try
                                {
                                    HttpClient httpClient = new HttpClient();
                                    byte[] byteData = httpClient.getData(imgUrl);
                                    // SD卡的路径
                                    String sdCardPath = getSDCardPath();
                                        if (sdCardIsExit())
                                        {
                                            // 图片的保存路
                                            img_path = sdCardPath + java.lang.System.currentTimeMillis() + ".jpg";
                                            FileOutputStream fos = new FileOutputStream(img_path, false);
                                            // 把图片写到本地
                                            fos.write(byteData);
                                            // 刷新
                                            fos.flush();
                                            // 关流
                                            fos.close();
                                            Message msg = hander.obtainMessage();
                                            msg.what = 1;
                                            hander.sendMessage(msg);
                                        }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
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

    /**
     * 判断SD卡是否可用
     */
    private static boolean sdCardIsExit() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        if (sdCardIsExit()) {
            return Environment.getExternalStorageDirectory().toString() + "/";
        }
        return null;
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
