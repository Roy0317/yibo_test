package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.MemberListBean;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author: Ray
 * created on 2018/10/17
 * description :用户列表适配器
 */
public class MemberListAdapter extends LAdapter<MemberListBean.ContentBean.ListBean> {


    public MemberListAdapter(Context mContext,
                             List<MemberListBean.ContentBean.ListBean> mDatas,
                             int layoutId) {
        super(mContext, mDatas, layoutId);

    }

    @Override
    public void convert(int position, LViewHolder holder,
                        ViewGroup parent, MemberListBean.ContentBean.ListBean item) {
        TextView tvUserName = holder.getView(R.id.tv_user_name);
        TextView tvUserType = holder.getView(R.id.tv_user_type);
        TextView tvRegisterTime = holder.getView(R.id.tv_register_time);
        TextView tvBalance = holder.getView(R.id.tv_balance);

        tvUserName.setText(item.getAccount()==null?"":item.getAccount());
        tvUserType.setText(item.getAccountType() == 1 ? "会员" : "非会员");

        String registerTime = DateUtils.longToString(item.getCreateDatetime(),"yyyy-MM-dd");

        tvRegisterTime.setText(registerTime);
        tvBalance.setText(String.valueOf(item.getMoney()));


    }





}
