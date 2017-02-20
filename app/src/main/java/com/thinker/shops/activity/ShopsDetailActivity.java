package com.thinker.shops.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thinker.shops.R;
import com.thinker.shops.bean.DataItem;
import com.thinker.shops.db.MyDbOpenHelper;
import com.thinker.shops.http.HttpClient;
import com.thinker.shops.utils.DensityUtils;
import com.thinker.shops.utils.SharedPreferencesUtils;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 * Created by zhoujian on 2016/11/16.
 */

public class ShopsDetailActivity extends Activity {

    public static final String TAG = "ShopsDetailActivity";
    @InjectView(R.id.bt_download)
    Button mBtDownload;
    private long exitTime;
    private Map<Integer, String> map = new HashMap<Integer, String>();
    private String pathUrl;
    //用于要播放的图片
    ArrayList<String> pictureList = new ArrayList<String>();
    @InjectView(R.id.im_personal)
    ImageButton mImPersonal;

    @InjectView(R.id.mGridViewImage)
    GridView mMGridViewImage;
    private int mWidth;
    private String commuityOid;
    private long objectId;
    private ArrayList<DataItem> mList;
    //线上
    ArrayList<DataItem> list;
    //本地数据
    ArrayList<DataItem> dataList;
    private String img_path;
    private ProgressDialog progress;
    private MyAdater adater;
    private MyDbOpenHelper mHelper;
    private SQLiteDatabase SQLdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shops_detail);
        ButterKnife.inject(this);
        //创建数据库
        mHelper = new MyDbOpenHelper(ShopsDetailActivity.this, "picturetable.db", null, 1);
        SQLdb = mHelper.getWritableDatabase();
        //获取屏幕的的宽度
        mWidth = DensityUtils.getScreenWidth(ShopsDetailActivity.this);
        commuityOid = getIntent().getStringExtra("mId");
        //服务器返回数据
        mList = (ArrayList<DataItem>) getIntent().getSerializableExtra("mList");
        //把数据插入数据库
        insertDb();
        //查询数据库,并把数据存入集合
        queryDb();
        //线上数据
        Log.e(TAG, "线上数据。。。。list==" + list.toString());
        Log.e(TAG, "数据库数据。。。dataList==" + dataList.toString());
        //初始化数据
        initData();
        //点击事件
        clickEvent();
    }

    private void queryDb() {

        //存放本地数据库数据
        dataList = new ArrayList<DataItem>();
        Cursor c = SQLdb.query("picturetable", null, null, null, null, null, null, null);
        // 读取出数据库所有信息，并封装至对象，存至集合中
        while (c.moveToNext()) {
            int productId = c.getInt(c.getColumnIndex("productId"));
            String productName = c.getString(c.getColumnIndex("productName"));
            String objectId = c.getString(c.getColumnIndex("objectId"));
            String showimg = c.getString(c.getColumnIndex("showimg"));
            String newictureUrl = c.getString(c.getColumnIndex("newictureUrl"));
            String isWatch = c.getString(c.getColumnIndex("isWatch"));
            DataItem item = new DataItem();
            item.setProductId(productId);
            item.setProductName(productName);
            item.setObjectId(Long.parseLong(objectId));
            item.setPadshowimg(showimg);
            item.setNewictureUrl(newictureUrl);
            item.setIsWatch(isWatch);
            dataList.add(item);
        }
    }


    private void insertDb() {
        if (mList != null && mList.size() > 0) {
            list = new ArrayList<DataItem>();
            for (int i = 0; i < mList.size(); i++) {
                Long objectId = mList.get(i).getObjectId();
                int count = 0;
                Cursor cursor = SQLdb.rawQuery("select count(*) from picturetable where objectid=" + Long.toString(objectId), null);
                while (cursor.moveToNext()) {
                    count = cursor.getInt(0);
                }
                if (count == 0) {
                    DataItem item = new DataItem();
                    item.setProductId(i);
                    item.setProductName(mList.get(i).getProductName());
                    item.setObjectId(mList.get(i).getObjectId());
                    item.setPadshowimg(mList.get(i).getPadshowimg());
                    item.setNewictureUrl(mList.get(i).getNewictureUrl());
                    item.setIsWatch(mList.get(i).getIsWatch());
                    list.add(item);
                    SQLdb.beginTransaction();// 开始事务
                    SQLdb.execSQL("insert into picturetable(productId, productName, objectId,showimg,newictureUrl,isWatch)values(?, ?, ?,?, ?, ?);",
                            new Object[]{i, list.get(i).getProductName(), list.get(i).getObjectId(),
                                    list.get(i).getPadshowimg(), list.get(i).getNewictureUrl(), list.get(i).getIsWatch()
                            });
                    SQLdb.setTransactionSuccessful();
                    SQLdb.endTransaction();
                }
            }

        }
    }

    private void clickEvent() {
        mImPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ShopsDetailActivity.this);
                builder.setMessage("是否退出当前绑定的社区店？");
                builder.setTitle("来米汇");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //清空表中所有记录
                        SQLdb.execSQL("DELETE FROM picturetable");
                        SharedPreferencesUtils.clearString(ShopsDetailActivity.this,"SHOPING_NAME");
                        finish();

                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();

            }
        });


        mMGridViewImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private String mUrl;

            @Override
            public void onItemClick(AdapterView<?> view, View view1, int postion, long l) {
                queryDb();
                mUrl = dataList.get(postion).getNewictureUrl();

                String prodectName = dataList.get(postion).getProductName();

                if ("null".equals(mUrl) || TextUtils.isEmpty(mUrl)) {
                    Toast.makeText(ShopsDetailActivity.this, "图片还未下载", Toast.LENGTH_SHORT).show();
                } else {


                   /* Intent intent = new Intent(ShopsDetailActivity.this, SingleImgActivity.class);
                    intent.putExtra("mStringPath", mUrl);
                    intent.putExtra("prodectName", prodectName);
                    startActivity(intent);*/


                    queryDb();

                    if (dataList != null && dataList.size() > 0) {
                        pictureList.clear();
                        for (int i = 0; i < dataList.size(); i++) {
                            if ("1".equals(dataList.get(i).getIsWatch()) && ((!("null".equals(dataList.get(i).
                                    getNewictureUrl()))) || !TextUtils.isEmpty(dataList.get(i).getNewictureUrl()))) {
                                pictureList.add(dataList.get(i).getNewictureUrl());
                            }
                        }

                        if (pictureList != null && pictureList.size() > 0) {
                            Intent intent = new Intent(ShopsDetailActivity.this, KannerActivity.class);
                            intent.putStringArrayListExtra("pictureList", pictureList);
                            intent.putExtra("mStringPath", mUrl);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ShopsDetailActivity.this, "没有要播放的图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }
        });

        //下载的点击按钮
        mBtDownload.setOnClickListener(new View.OnClickListener()
        {

            private DataItem mNext;
            private String mUrl;

            @Override
            public void onClick(View v)
            {
                queryDb();

                if (dataList != null && dataList.size() > 0) {
                    Iterator<DataItem> sListIterator = dataList.iterator();
                    while(sListIterator.hasNext()){
                        mNext = sListIterator.next();
                        mUrl = mNext.getNewictureUrl();
                        if (null!=mUrl && !(mUrl.equals("null"))&&  !mUrl.equals("")){
                            sListIterator.remove();
                        }
                    }

                    if(dataList!=null && dataList.size()>0){
                        Intent intent =  new Intent(ShopsDetailActivity.this,DownLoadActivity.class);
                        intent.putExtra("dataList",(Serializable) dataList);
                        intent.putExtra("commuityOid",commuityOid);
                        startActivity(intent);
                    }else{
                        Toast.makeText(ShopsDetailActivity.this, "全部图片已下载完成", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

    }

    private void initData()
    {
        adater = new MyAdater();
        mMGridViewImage.setAdapter(adater);
        mMGridViewImage.setSelector(R.color.transparent);
        adater.notifyDataSetChanged();
    }

    class MyAdater extends BaseAdapter {
        private String mShowimg;
        private String mUrl;
        private String mWatch;

        private String productName;

        public int getCount() {
            if (dataList != null && dataList.size() > 0) {
                return dataList.size();
            }
            Log.e(TAG, "dataList====" + dataList.toString());
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
                holder.im_share = (ImageView) view.findViewById(R.id.im_share);

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
            if (dataList != null && dataList.size() > 0) {
                //下载的图片是否为null
                mUrl = dataList.get(position).getNewictureUrl();
                mShowimg = dataList.get(position).getPadshowimg();
                String newShowimg = mShowimg.replace("/home/thinker/wwwroot/", "http://");
                Glide.with(ShopsDetailActivity.this).load(newShowimg).into(holder.im_picture);

                 productName =  dataList.get(position).getProductName();
                holder.tv_product_name.setText(productName);
                //holder.tv_product_name.setText("正宗福建平和琯溪红肉蜜柚红心柚子新鲜农家特产纯天然有机水果10斤装 3-4个");
                if ("null".equals(mUrl) || TextUtils.isEmpty(mUrl)) {
                    //下载的按钮可见，浮层可见
                   // holder.im_download.setVisibility(View.VISIBLE);
                    holder.im_botoom.setVisibility(View.VISIBLE);
                    holder.ll_bottom_right.setVisibility(View.INVISIBLE);
                } else {
                   // holder.im_download.setVisibility(View.INVISIBLE);
                    holder.im_botoom.setVisibility(View.INVISIBLE);
                    holder.ll_bottom_right.setVisibility(View.VISIBLE);
                    mWatch = dataList.get(position).getIsWatch();
                    if ("0".equals(mWatch)) {
                        holder.im_watch.setVisibility(View.INVISIBLE);
                        holder.im_not_watch.setVisibility(View.GONE);
                    } else if ("1".equals(mWatch)) {
                        holder.im_watch.setVisibility(View.GONE);
                        holder.im_not_watch.setVisibility(View.GONE);
                    }
                }


                holder.im_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectId = dataList.get(position).getObjectId();
                        pathUrl = "http://laimihui.china1h.cn/task/mall/paddemo/postimg.do?objectId=" + objectId + "&communityOid=" + commuityOid;
                        productName =  dataList.get(position).getProductName();
                        showShare(pathUrl,productName);
                    }
                });

                //眼睛的点击事件
                holder.im_not_watch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //0---->1
                        holder.im_watch.setVisibility(View.GONE);
                        holder.im_not_watch.setVisibility(View.GONE);
                        objectId = mList.get(position).getObjectId();
                        //跟新数据
                        ContentValues values = new ContentValues();
                        values.put("isWatch", "1");
                        SQLdb.update("picturetable", values, "objectId=?", new String[]{Long.toString(objectId)});
                        queryDb();

                    }
                });

                holder.im_watch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  1--->0
                        holder.im_watch.setVisibility(View.GONE);
                        holder.im_not_watch.setVisibility(View.GONE);
                        objectId = mList.get(position).getObjectId();
                        //跟新数据
                        ContentValues values = new ContentValues();
                        values.put("isWatch", "0");
                        SQLdb.update("picturetable", values, "objectId=?", new String[]{Long.toString(objectId)});
                        queryDb();
                    }
                });
                //删除的点击事件
                holder.im_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //把下载到本地的图片删除，删除一个字段
                        objectId = mList.get(position).getObjectId();
                        //跟新数据
                        ContentValues values = new ContentValues();
                        values.put("newictureUrl", "null");
                        values.put("isWatch", "0");
                        SQLdb.update("picturetable", values, "objectId=?", new String[]{Long.toString(objectId)});
                        holder.im_botoom.setVisibility(View.VISIBLE);
                        holder.im_download.setVisibility(View.GONE);
                        holder.ll_bottom_right.setVisibility(View.GONE);
                        queryDb();
                    }
                });
                //下载的点击事件
                holder.im_download.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        objectId = mList.get(position).getObjectId();
                        //Log.e(TAG,"objectId=="+objectId);
                        //"http://dev.wecity.co/task/mall/paddemo/postimg.do?objectId="
                        //192.168.1.57:8080
                        pathUrl = "http://laimihui.china1h.cn/task/mall/paddemo/postimg.do?objectId=" + objectId + "&communityOid=" + commuityOid;
                        SharedPreferencesUtils.saveString(ShopsDetailActivity.this, "pathUrl", pathUrl);
                        Log.e(TAG, "pathUrl==" + pathUrl);
                        //开启线程下载图片
                        downLoadDialog();
                        progress.show();
                        startDownLoaad(pathUrl);
                    }

                    private void startDownLoaad(final String imgUrl) {
                        final Handler hander = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                progress.dismiss();
                                map.put(position, img_path);
                                //跟新数据
                                ContentValues values = new ContentValues();
                                values.put("newictureUrl", img_path);
                                values.put("isWatch", "1");
                                // SQLdb.update("picturetable",values,"objectId=?",new String[]{Long.toString(objectId)});
                                SQLdb.update("picturetable", values, "objectId=?", new String[]{Long.toString(objectId)});
                                holder.im_botoom.setVisibility(View.GONE);
                                holder.ll_bottom_right.setVisibility(View.VISIBLE);
                                holder.im_download.setVisibility(View.GONE);
                                queryDb();
                                Toast.makeText(ShopsDetailActivity.this, "图片下载完成", Toast.LENGTH_SHORT).show();
                            }
                        };
                        Thread thread = new Thread() {
                            public void run() {
                                try {
                                    HttpClient httpClient = new HttpClient();
                                    byte[] byteData = httpClient.getData(imgUrl);
                                    // SD卡的路径
                                    String sdCardPath = getSDCardPath();
                                    if (sdCardIsExit()) {
                                        // 图片的保存路
                                        img_path = sdCardPath + System.currentTimeMillis() + ".jpg";
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
                                } catch (Exception e) {
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

            ImageView im_share;
            ImageView im_download;
            ImageView im_delete;
            ImageView im_watch;
            ImageView im_not_watch;
            LinearLayout ll_bottom_right;
            TextView tv_product_name;
        }
    }

    private void showShare(String pathUrl,String name)
    {


        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if (pathUrl != null && name!=null)
        {
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
            oks.setTitle(name);
            // titleUrl是标题的网络链接，QQ和QQ空间等使用
            oks.setTitleUrl(pathUrl);
            // text是分享文本，所有平台都需要这个字段
            oks.setText("扫描二维码即可下单购买！");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImageUrl(pathUrl);//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            //oks.setUrl(pathUrl);
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment(getString(R.string.app_name));
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(pathUrl);
            // 启动分享GUI
            oks.show(this);
        }

    }

    private void downLoadDialog() {
        progress = new ProgressDialog(ShopsDetailActivity.this);
        progress.setMessage("请等候，图片下载中...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(false);
    }

    /**
     * 判断SD卡是否可用
     */
    private static boolean sdCardIsExit() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
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
        queryDb();
        adater.notifyDataSetChanged();
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

        //清空表中所有记录
        SQLdb.execSQL("DELETE FROM picturetable");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(true);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
