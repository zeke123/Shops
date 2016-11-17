package com.thinker.shops;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application 
{

	public static RequestQueue queues;
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		queues = Volley.newRequestQueue(getApplicationContext());				
	}
	
	public static RequestQueue getHttpQueues()
	{
		return queues;
	}

}
