package com.yibo.yiboapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.DirectChargeResponse;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class DirectChargeAdapter extends LAdapter<DirectChargeResponse.DCRecord> {

    private NumberFormat formatter = new DecimalFormat("#0.00");

    public DirectChargeAdapter(Context c, List<DirectChargeResponse.DCRecord> records, int layoutID){
        super(c, records, layoutID);
    }

    @Override
    public void convert(int position, LViewHolder holder, ViewGroup parent, DirectChargeResponse.DCRecord record) {
        TextView textDatetime = holder.getView(R.id.textDatetime);
        TextView textAccount = holder.getView(R.id.textAccount);
        TextView textAmount = holder.getView(R.id.textAmount);
        TextView textPercentage = holder.getView(R.id.textPercentage);
        TextView textAward = holder.getView(R.id.textAward);
        TextView textStatus = holder.getView(R.id.textStatus);

        textDatetime.setText(record.getCreateDatetime());
        textAccount.setText(record.getAccount());
        textAmount.setText(String.valueOf((int) record.getMoney()));
        textPercentage.setText(String.valueOf((int) record.getGiftMoneyPercent()));
        textAward.setText(formatter.format(record.getGiftMoney()));
        switch (record.getStatus()){
            case 2:
                textStatus.setText("失败");
                break;
            case 3:
                textStatus.setText("等待审批");
                break;
            case 1:
            default:
                textStatus.setText("成功");
                break;
        }
    }
}
