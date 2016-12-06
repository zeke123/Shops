package com.thinker.shops.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

public class AbbUtils {

	/**
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 版本号，动态获取应用程序的版本号
	 * 
	 * @return 程序的版本字符串
	 */
	public static String getVersionCode(Context context) {
		// 包管理器
		PackageManager pm = context.getPackageManager();
		try {
			// 得到清单文件
			// 第一个参数返回的是当前应用的唯一包名
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			// 返回清单文件中的版本号
			return pi.versionCode + "";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean checkSDCard()
	{
		final String status = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(status))
		{
			return true;
		}
		return false;
	}

}
