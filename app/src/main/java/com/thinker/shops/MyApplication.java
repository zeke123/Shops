package com.thinker.shops;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application 
{

	public static RequestQueue queues;
	
	@Override
	public void onCreate() 
	{
		super.onCreate();

		/*DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.mipmap.ic_launcher)
				.showImageOnFail(R.mipmap.ic_launcher)
				.cacheInMemory(false)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.cacheOnDisk(true)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);*/



		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.cacheOnDisk(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize((int) (2 * 1024 * 1024))
				.memoryCacheSizePercentage(13)
				// default
				//.diskCache(new UnlimitedDiscCache(cacheDir))
				// default
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.defaultDisplayImageOptions(defaultOptions).writeDebugLogs() // Remove
				.build();
				// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);






		queues = Volley.newRequestQueue(getApplicationContext());				
	}
	
	public static RequestQueue getHttpQueues()
	{
		return queues;
	}

}
