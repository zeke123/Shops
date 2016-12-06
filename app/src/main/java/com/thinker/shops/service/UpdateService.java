package com.thinker.shops.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.thinker.shops.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateService extends android.app.Service
{
	
	private final static int	DOWNLOAD_COMPLETE			= 0;
	private final static int	DOWNLOAD_FAIL				= 1;
	// 文件存储
	private File				updateDir					= null;
	private File				updateFile					= null;
	// 通知栏
	private NotificationManager	updateNotificationManager	= null;
	private Notification.Builder 		updateNotification			= null;
	// 通知栏跳转Intent
	//private Intent				updateIntentU				= null;
	int							flags						= 0;
	int							startId						= 0;
	private String				NewAPKUrl					= "";
	private PendingIntent		updatePendingIntent			= null;
	private final String		DOWNLOADPATH				= "app/download/";
	private final String		UPDATEFILENAME				= "android-app.apk";
	String installPath;
	int isSdChche;
	private long downloadSize;
	@Override
	public void onStart(Intent intent, int startid)
	{
		NewAPKUrl = intent.getStringExtra("URL");
		isSdChche = intent.getIntExtra("isSdChche", 0);
		// apk 下载到sd卡
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			updateDir = new File(Environment.getExternalStorageDirectory(), DOWNLOADPATH);
			updateFile = new File(updateDir.getPath(), UPDATEFILENAME);
		}
		//apk 下载到手机内存中
		else 
		{
			installPath = this.getApplication().getFilesDir().getAbsolutePath();
			updateDir = new File(installPath);
			updateFile = new File(updateDir.getPath(), UPDATEFILENAME);
		}

		this.updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		this.updateNotification = new Notification.Builder(this);






		// 设置下载过程中，点击通知栏，回到主界面
		//updateIntentU = new Intent(this, MainActivity.class);
		//updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntentU, 0);
		// 设置通知栏显示内容

		updateNotification.setSmallIcon( R.mipmap.ic_launcher); //设置图标

		updateNotification.setTicker(getResources().getString(R.string.app_name));

		updateNotification.setAutoCancel(true);//打开程序后图标消失

		updateNotification.setContentTitle(getResources().getString(R.string.app_name));

		updateNotification.setContentText("正在下载0%");

		updateNotification.setContentIntent(updatePendingIntent);


		Notification notification1 = updateNotification.getNotification();
		// 发出通知
		updateNotificationManager.notify(0, notification1);




		// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
		new Thread(new updateRunnable()).start();// 这个是下载的重点，是下载的过程
		super.onStart(intent, startid);
	}

	private Handler	updateHandler	= new Handler() {
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case DOWNLOAD_COMPLETE:
				{
					// 自动安装PendingIntent
					Uri uri = Uri.fromFile(updateFile);
					Intent installIntent = new Intent(Intent.ACTION_VIEW);
					installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
					installIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
					UpdateService.this.startActivity(installIntent);
					updatePendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installIntent, 0);


					updateNotification.setDefaults(Notification.DEFAULT_SOUND); // 铃声提醒

					updateNotification.setTicker(getResources().getString(R.string.app_name));
					updateNotification.setContentTitle(getResources().getString(R.string.app_name));
					updateNotification.setContentText("下载完成，请点击安装!");


					updateNotification.setContentIntent(updatePendingIntent);

					Notification notification1 = updateNotification.getNotification();
					// 发出通知
					updateNotificationManager.notify(0, notification1);


					// 停止服务
					//stopService(updateIntentU);
					break;
				}
				case DOWNLOAD_FAIL:
				{
					// 下载失败

					updateNotification.setTicker(getResources().getString(R.string.app_name));
					updateNotification.setContentTitle(getResources().getString(R.string.app_name));
					updateNotification.setContentText("对不起，下载失败!");

					Notification notification1 = updateNotification.getNotification();
					// 发出通知
					updateNotificationManager.notify(0, notification1);

					break;
				}
				default:
				{
					//if (updateIntentU!=null) {
					//	stopService(updateIntentU);
					//}
					break;
				}
			}
		}
	};

	private class updateRunnable implements Runnable
	{
		Message	message	= updateHandler.obtainMessage();

		public void run()
		{
			try
			{
				if (!updateDir.exists())
				{
					updateDir.mkdirs();
				}
				if (!updateFile.exists())
				{
					updateFile.createNewFile();
				}
				// 1 代表有sd卡    进入sd 下载apk文件
				if (isSdChche==1) 
				{
					downloadSize = downloadUpdateFile(NewAPKUrl, updateFile);
				}
				//进入内存apk下载
				else 
				{
					downloadSize = downloadFile(NewAPKUrl, updateFile);
				}
				if (downloadSize > 0)
				{
					message.what = DOWNLOAD_COMPLETE;
					updateHandler.handleMessage(message);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				message.what = DOWNLOAD_FAIL;
				updateHandler.handleMessage(message);
			}
		}

		/**
		 * 下载APK文件到sd卡中
		 * @param downloadUrl
		 * @param saveFile
		 * @return
		 * @throws Exception
		 */
		public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception
		{
			int downloadCount = 0;
			int currentSize = 0;
			long totalSize = 0;
			int updateTotalSize = 0;
			HttpURLConnection httpConnection = null;
			InputStream is = null;
			FileOutputStream fos = null;
			try
			{
				URL url = new URL(downloadUrl);
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
				if (currentSize > 0)
				{
					httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
				}
				httpConnection.setConnectTimeout(50000);
				httpConnection.setReadTimeout(50000);
				updateTotalSize = httpConnection.getContentLength();
				if (httpConnection.getResponseCode() == 404)
				{
					throw new Exception("fail!");
				}
				is = httpConnection.getInputStream();
				fos = new FileOutputStream(saveFile, false);
				byte buffer[] = new byte[1024*4];
				int readsize = 0;
				final String loading = getString(R.string.str_download_loading);
				while ((readsize = is.read(buffer)) > 0) {
					fos.write(buffer, 0, readsize);
					totalSize += readsize;
					// 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
					if ((downloadCount == 0)
							|| (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
						downloadCount += 10;


						updateNotification.setContentTitle(getResources().getString(R.string.app_name));
						updateNotification.setTicker(getResources().getString(R.string.app_name));


						updateNotification.setContentText(loading+":"+(int) totalSize * 100 / updateTotalSize + "%");


						Notification notification1 = updateNotification.getNotification();
						// 发出通知
						updateNotificationManager.notify(0, notification1);


					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (httpConnection != null)
				{
					httpConnection.disconnect();
				}
				if (is != null)
				{
					is.close();
				}
				if (fos != null)
				{
					fos.close();
				}
			}
			return totalSize;
		}
	}

		/**
		 * 下载APK文件到内存中
		 * @param downloadUrl
		 * @param saveFile
		 * @return
		 * @throws Exception
		 */
		public long downloadFile(String downloadUrl, File saveFile) throws Exception
		{
			int downloadCount = 0;
			int currentSize = 0;
			long totalSize = 0;
			int updateTotalSize = 0;
			HttpURLConnection httpConnection = null;
			InputStream is = null;
			FileOutputStream fos = null;
			try
			{
				URL url = new URL(downloadUrl);
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
				if (currentSize > 0)
				{
					httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
				}
				httpConnection.setConnectTimeout(10000);
				httpConnection.setReadTimeout(20000);
				updateTotalSize = httpConnection.getContentLength();
				if (httpConnection.getResponseCode() == 404)
				{
					throw new Exception("fail!");
				}
				is = httpConnection.getInputStream();
	//			fos = openFileOutput(saveFile.getName(), Context.MODE_WORLD_WRITEABLE);
				fos = new FileOutputStream(saveFile, false); // 文件存在则覆盖掉 
				byte buffer[] = new byte[1024*4];
				int readsize = 0;
				final String loading = getString(R.string.str_download_loading);
				while ((readsize = is.read(buffer)) > 0) 
				{
					fos.write(buffer, 0, readsize);
					totalSize += readsize;
					// 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
					if ((downloadCount == 0)
							|| (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
						downloadCount += 10;

						updateNotification.setTicker(getResources().getString(R.string.app_name));
						updateNotification.setContentTitle(getResources().getString(R.string.app_name));

						updateNotification.setContentText(loading+":"+(int) totalSize * 100 / updateTotalSize + "%");

						Notification notification1 = updateNotification.getNotification();
						// 发出通知
						updateNotificationManager.notify(0, notification1);


					}
				}
				//添加访问权限   否则无法正常解析apk
				String permission = "777";
				try
				{
					String command = "chmod " + permission + " " + saveFile;
					Runtime runtime = Runtime.getRuntime();
					runtime.exec(command);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (httpConnection != null)
				{
					httpConnection.disconnect();
				}
				if (is != null)
				{
					is.close();
				}
				if (fos != null)
				{
					fos.close();
				}
			}
			return totalSize;
		}
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}


