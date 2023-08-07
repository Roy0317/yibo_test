package com.yibo.yiboapp.adapter;

import android.view.View;

import com.yibo.yiboapp.views.VerticalBannerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public abstract class BaseBannerAdapter {
    private List<Object> mDatas;
    private OnDataChangedListener mOnDataChangedListener;

    public BaseBannerAdapter(List<Object> datas) {
        mDatas = datas;
        if (datas == null || datas.isEmpty()) {
            throw new RuntimeException("nothing to show");
        }
    }

    public BaseBannerAdapter(Object[] datas) {
        mDatas = new ArrayList<>(Arrays.asList(datas));
    }

    /**
     * 设置banner填充的数据
     */
    public void setData(List<Object> datas) {
        this.mDatas = datas;
        notifyDataChanged();
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    void notifyDataChanged() {
        mOnDataChangedListener.onChanged();
    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * 设置banner的样式
     */
    public abstract View getView(VerticalBannerView parent);

    /**
     * 设置banner的数据
     */
    public abstract void setItem(View view, Object data);


    public interface OnDataChangedListener {
        void onChanged();
    }
}