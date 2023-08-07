package com.example.anuo.immodule.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseLVAdapter<T> extends BaseAdapter {

    protected List<T> mDatas ;
    protected Context mContext ;
    protected int layoutId;

    public BaseLVAdapter(Context mContext , List<T> mDatas , int layoutId){
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.layoutId = layoutId;

    }

    @Override
    public int getCount() {
        if(mDatas == null){
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        if(mDatas == null){
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LViewHolder holder = LViewHolder.get(mContext, convertView, layoutId);
        convert(position,holder, parent,getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(int position, LViewHolder holder, ViewGroup parent, T item);
}

