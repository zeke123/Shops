package com.thinker.shops.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtils {

	public static final String SP_NAME = "config";
	private static SharedPreferences sp;

	/**
	 * 存储字符串
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveString(Context context, String key, String value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putString(key, value).commit();
		
	}
	
	
	/**
	 * 获取字符串
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context ,String key , String defValue){
		if(sp == null) 
			sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getString(key, defValue);
	}
	
	
	public static void clearString(Context context,String key){
		sp = context.getSharedPreferences(SP_NAME, 0);
		Editor edit = sp.edit();
		edit.clear();
		edit.commit();
	}

}
