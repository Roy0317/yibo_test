package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.DonateActivity;
import com.yibo.yiboapp.entify.DonateRecordResponse;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;

import java.util.List;

import crazy_wrapper.Crazy.Utils.Utils;

public class DonationAdapter extends LAdapter<DonateRecordResponse.DonateRecord> {

    public DonationAdapter(Context c, List<DonateRecordResponse.DonateRecord> donations, int layoutID){
        super(c, donations, layoutID);
    }

    @Override
    public void convert(int position, LViewHolder holder, ViewGroup parent, DonateRecordResponse.DonateRecord record) {
        TextView textDatetime = holder.getView(R.id.textDatetime);
        TextView textAccount = holder.getView(R.id.textAccount);
        TextView textAmount = holder.getView(R.id.textAmount);
        TextView textStatus = holder.getView(R.id.textStatus);

        textDatetime.setText(record.getCreateDatetime());
        textAccount.setText(record.getAccount());
        textAmount.setText(record.getDonateMoney() + "元");
        switch (record.getStatus()){
            case 1:
                textStatus.setText("成功");
                break;
            case 2:
                textStatus.setText("失败");
                break;
            default:
                textStatus.setText("等待审批");
                break;
        }
    }
}
