package com.thinker.shops.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.thinker.shops.R;
import com.thinker.shops.utils.SharedPreferencesUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by zhoujian on 2016/11/18.
 */

public class SingleImgActivity extends Activity
{
    @InjectView(R.id.img_bottom)
    ImageView mImgBottom;
    @InjectView(R.id.im_share)
    ImageButton mImShare;
    @InjectView(R.id.im_takecare)
    ImageButton mImTakecare;
    @InjectView(R.id.im_back)
    ImageView mImBack;
    private String mPath;
    private String pathUrl;
    private Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_single);
        ShareSDK.initSDK(this);
        ButterKnife.inject(this);
        mPath = getIntent().getStringExtra("mStringPath");
        pathUrl =SharedPreferencesUtils.getString(SingleImgActivity.this,"pathUrl",null);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        bm = BitmapFactory.decodeFile(mPath, options);
        mImgBottom.setImageBitmap(bm);
        clickEvent();
    }

    private void clickEvent() {

        //保存到相册中

        mImTakecare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageToGallery(SingleImgActivity.this, bm);
            }
        });

        //分享
        mImShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });

        mImBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showShare()
    {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        if (pathUrl != null && mPath != null)
        {
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
            oks.setTitle(getString(R.string.app_name));
            // titleUrl是标题的网络链接，QQ和QQ空间等使用
            oks.setTitleUrl(pathUrl);
            // text是分享文本，所有平台都需要这个字段
            oks.setText(getString(R.string.app_name));
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImagePath(mPath);//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(pathUrl);
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

    public static void saveImageToGallery(Context context, Bitmap bmp)
    {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Pictures");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try
        {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        Toast.makeText(context, "图片保存保存到相册中", Toast.LENGTH_SHORT).show();
    }
}
