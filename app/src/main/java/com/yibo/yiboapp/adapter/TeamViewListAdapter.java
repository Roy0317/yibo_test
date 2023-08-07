package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.TeamViewBean;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.utils.Utils;

import java.util.List;

/**
 * Author: Ray
 * created on 2018/10/17
 * description :用户列表适配器
 */
public class TeamViewListAdapter extends LAdapter<TeamViewBean> {


    public TeamViewListAdapter(Context mContext,
                             List<TeamViewBean> mDatas,
                             int layoutId) {
        super(mContext, mDatas, layoutId);
    }

    @Override
    public void convert(int position, LViewHolder holder,
                        ViewGroup parent, TeamViewBean item) {


        TextView account = holder.getView(R.id.account);
        TextView date = holder.getView(R.id.date);
        TextView balanced = holder.getView(R.id.balanced);
        TextView notbalance = holder.getView(R.id.not_balance);
        TextView deposit = holder.getView(R.id.deposit_money);

        account.setText(!Utils.isEmptyString(item.getAccount()) ? item.getAccount() : "暂无帐号");
        date.setText(!Utils.isEmptyString(item.getStatdate()) ? item.getStatdate() : "暂无日期");
        balanced.setText(String.valueOf(item.getBetamount()));
        notbalance.setText(String.valueOf(item.getNobetamount()));
        deposit.setText(String.valueOf(item.getDepositamount()));


    }

}
