package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.entify.LongLonngBean;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;

import java.util.List;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.adapter
 * @description: ${DESP}
 * @date: 2019/9/18
 * @time: 5:15 PM
 */
public class LongLoongAdapter extends LAdapter<LongLonngBean.OmmitQueueBean> {


    public LongLoongAdapter(Context mContext, List<LongLonngBean.OmmitQueueBean> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
    }

    @Override
    public void convert(int position, LViewHolder holder, ViewGroup parent, LongLonngBean.OmmitQueueBean item) {

        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvSortNum = holder.getView(R.id.tv_sort_num);

        tvName.setText(item.getName()+"  --  " + item.getNames());
        tvSortNum.setText(String.valueOf(item.getSortNum()) +"期");

    }






}
