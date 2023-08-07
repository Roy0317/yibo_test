package com.yibo.yiboapp.ui;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

public class LViewHolder {
	
	private SparseArray<View> mViews ;
	private View mConvertView ;
	private int position;
	
	public LViewHolder(Context mContext , int layoutId){
		
		this.mConvertView = LayoutInflater.from(mContext).inflate(layoutId,null,false);
		mViews = new SparseArray<View>();
		this.mConvertView.setTag(this);
	}
	
	public static LViewHolder get(Context mContext , View convertView , int layoutId ){
		if(convertView == null){
			return new LViewHolder(mContext, layoutId);
		}else{
			return (LViewHolder) convertView.getTag();
		}
	}


	public <T extends View> T getView(int  viewId){
		
		View view =  mViews.get(viewId);
		
		if(view == null){
			view = this.mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		
		return (T) view;
			
	}

	public View getConvertView() {
		return mConvertView;
	}




	public  void setDataPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}
}
