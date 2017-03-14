        package com.thinker.shops.activity;

        import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.thinker.shops.R;
import com.thinker.shops.bean.DataItem;
import com.thinker.shops.bean.ProductItem;
import com.thinker.shops.db.MyDbOpenHelper;
import com.thinker.shops.http.HttpClient;
import com.thinker.shops.view.CustomToast;
import java.io.FileOutputStream;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;



/**
 * Created by zhoujian on 2017/1/10.
 */

public class DownLoadActivity extends Activity
{

    public static final String TAG = "DownLoadActivity";
    public static final int FLAG = 0;
    @InjectView(R.id.im_back)
    ImageView mImBack;
    private ProgressDialog progress;
    @InjectView(R.id.list_pruduct)
    ListView mListPruduct;
    private ArrayList<DataItem> mList;
    private String pathUrl;
    private String img_path;
    private String commuityOid;
    private MyDbOpenHelper mHelper;
    private SQLiteDatabase SQLdb;
    private String objectId;
    private MyThread thread;
    private Handler hander;
    private int i = 0;
    private ArrayList<ProductItem> productList = new ArrayList<ProductItem>();
    private ProductItem mProductItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_download);
        ButterKnife.inject(this);
        //服务器返回数据
        mList = (ArrayList<DataItem>) getIntent().getSerializableExtra("dataList");
        for (int j = 0; j < mList.size(); j++) {
            mProductItem = new ProductItem();
            mProductItem.setObjectId(mList.get(j).getObjectId());
            mProductItem.setProductName(mList.get(j).getProductName());
            mProductItem.setFlagStatus("未下载");
            productList.add(mProductItem);
        }

        Log.i(TAG, "productList====" + productList.toString());
        commuityOid = getIntent().getStringExtra("commuityOid");
        //创建数据库
        mHelper = new MyDbOpenHelper(DownLoadActivity.this, "picturetable.db", null, 1);
        SQLdb = mHelper.getWritableDatabase();
        CustomToast.showToast(DownLoadActivity.this,"开始下载图片");
       // Toast.makeText(this, "开始下载图片", Toast.LENGTH_SHORT).show();
        //开启线程下载图片
        startDownLoaad();
        setData();
        clickEvent();
    }

    private void clickEvent()
    {
        mImBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (i == productList.size()) {
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DownLoadActivity.this);
                    builder.setMessage("是否停止当前下载？");
                    builder.setTitle("来米汇");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            thread.stop();
                            finish();
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.show();
                }
            }
        });
    }

    private void downLoadDialog() {
        progress = new ProgressDialog(DownLoadActivity.this);
        progress.setMessage("请等候，图片下载中...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(false);
    }

    private void startDownLoaad() {
        hander = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                i += 1;
                switch (msg.what) {
                    case FLAG:
                        CustomToast.showToast(DownLoadActivity.this,"第" + i + "张图片下载完成");
                        //Toast.makeText(DownLoadActivity.this, "第" + i + "张图片下载完成", Toast.LENGTH_SHORT).show();
                        productList.get(i - 1).setFlagStatus("下载完成");
                        setData();
                        if (i == productList.size()) {

                            CustomToast.showToast(DownLoadActivity.this,"全部图片下载完成");
                            //Toast.makeText(DownLoadActivity.this, "全部图片下载完成", Toast.LENGTH_SHORT).show();

                            return;
                        }
                        break;
                }
            }
        };
        thread = new MyThread();
        thread.start();
    }

    public class MyThread extends Thread {
        @Override
        public void run() {
            //http://dev.wecity.co/task/mall/paddemo/postimg.do?objectId=" + objectId + "&communityOid=" + commuityOid;
            //http://laimihui.china1h.cn/task/mall/paddemo/postimg.do?objectId=" + objectId + "&communityOid=" + commuityOid;

            try {
                HttpClient httpClient = new HttpClient();
                for (int i = 0; i < productList.size(); i++) {
                    productList.get(i).setFlagStatus("正在下载中");

                    objectId = Long.toString(productList.get(i).getObjectId());
                    pathUrl = "http://laimihui.china1h.cn/task/mall/paddemo/postimg.do?objectId=" + objectId + "&communityOid=" + commuityOid;

                    //pathUrl= "http://dev.wecity.co/task/mall/paddemo/postimg.do?objectId=" + objectId + "&communityOid=" + commuityOid;

                    //http://dev.wecity.co/task/mall/paddemo/postimg.do?objectId=20416022903460960&communityOid=8295683397117721
                    byte[] byteData = httpClient.getData(pathUrl);
                    // SD卡的路径
                    String sdCardPath = getSDCardPath();
                    if (sdCardIsExit())
                    {
                        // 图片的保存路
                        img_path = sdCardPath + System.currentTimeMillis() + i + ".jpg";
                        FileOutputStream fos = new FileOutputStream(img_path, false);
                        // 把图片写到本地
                        fos.write(byteData);
                        // 刷新
                        fos.flush();
                        // 关流
                        fos.close();
                        ContentValues values = new ContentValues();
                        values.put("newictureUrl", img_path);
                        values.put("isWatch", "1");
                        SQLdb.update("picturetable", values, "objectId=?", new String[]{objectId});
                        Message message = Message.obtain();
                        message.what = FLAG;
                        hander.sendMessage(message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setData() {
        ProductAdapter adapter = new ProductAdapter();
        mListPruduct.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class ProductAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            View view;
            final ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(DownLoadActivity.this, R.layout.list_item, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                holder.tv_statas = (TextView) view.findViewById(R.id.tv_statas);
                view.setTag(holder);
            }
            if (productList != null && productList.size() > 0) {
                String status = productList.get(position).getFlagStatus();
                if ("下载完成".equals(status)) {
                    holder.tv_statas.setTextColor(Color.parseColor("#2c9a2d"));
                    holder.tv_statas.setText(productList.get(position).getFlagStatus());
                } else if ("未下载".equals(status)) {
                    holder.tv_statas.setTextColor(Color.parseColor("#e51c23"));
                    holder.tv_statas.setText(productList.get(position).getFlagStatus());
                } else if ("正在下载中".equals(status)) {
                    holder.tv_statas.setTextColor(Color.parseColor("#2175ed"));
                    holder.tv_statas.setText(productList.get(position).getFlagStatus());
                }
                holder.tv_name.setText(productList.get(position).getProductName());
            }

            return view;

        }
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_statas;
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (i == productList.size())
            {
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(DownLoadActivity.this);
                builder.setMessage("是否停止当前下载？");
                builder.setTitle("来米汇");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        thread.stop();
                        finish();
                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
