package com.thinker.shops.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.thinker.shops.R;
import com.thinker.shops.bean.ImageItemBean;

import java.util.List;

public class ShopsDetailAdapter extends BaseAdapter {

	private List<ImageItemBean> mData = null;
	private Context mContext = null;
	private int mWidth = 0;
	
	public ShopsDetailAdapter(Context mContext, List<ImageItemBean> mData, int mWidth){
		this.mContext = mContext;
		this.mData = mData;
		this.mWidth = mWidth;
	}
	
	public int getCount() {
		if(mData == null){
			return 0;
		}
		return 10;
	}

	public Object getItem(int position) {
		return mData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView == null){
			vh = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.shops_detail_item, null);
			/*vh.mImageView = (ImageView) convertView.findViewById(R.id.mImageView);
			convertView.findViewById(R.id.deleteBtn).setVisibility(View.GONE);		
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) vh.mImageView.getLayoutParams();
			int ivWidth = (mWidth - dp2px(mContext,42))/3;			
			lp.width = ivWidth;
			lp.height = ivWidth;			
			convertView.setTag(vh);*/
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		//ImageLoaderUtils.getinstance(mContext).getImage(vh.mImageView, mData.get(position).getUrl());
		

		return convertView;
	}
	
	class ViewHolder{
		ImageView mImageView;
		FrameLayout deleteBtn;
	}
	
	public int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
				.getDisplayMetrics());
	}

}
