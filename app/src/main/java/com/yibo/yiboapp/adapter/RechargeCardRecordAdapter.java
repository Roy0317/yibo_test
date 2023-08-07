package com.yibo.yiboapp.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.RechargeCardRecordActivity;
import com.yibo.yiboapp.entify.RechargeCardRecordBean;

import java.util.List;

public class RechargeCardRecordAdapter extends BaseQuickAdapter<RechargeCardRecordBean.ListBean, BaseViewHolder> {


    private Context context;

    public RechargeCardRecordAdapter(int layoutResId, @Nullable List<RechargeCardRecordBean.ListBean> data, RechargeCardRecordActivity rechargeCardRecordActivity) {
        super(layoutResId, data);
        context = rechargeCardRecordActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeCardRecordBean.ListBean item) {

        TextView tvSendCard = helper.getView(R.id.tv_send_card_time);
        TextView tv_use_card_time = helper.getView(R.id.tv_use_card_time);
        TextView tv_account_num = helper.getView(R.id.tv_account_num);
        TextView tv_card_number = helper.getView(R.id.tv_card_number);
        TextView tv_card_state = helper.getView(R.id.tv_card_state);


        int color1 = context.getResources().getColor(R.color.black);
        int color2 = context.getResources().getColor(R.color.grey);
        int color3 = context.getResources().getColor(R.color.red);
        int color4 = context.getResources().getColor(R.color.green2);

        SpanUtils.with(tvSendCard).append("发卡时间：").setForegroundColor(color1).append(TimeUtils.millis2String(item.getCreateDatetime())).setForegroundColor(color2).create();
        SpanUtils spanUseTime = SpanUtils.with(tv_use_card_time).append("使用时间：").setForegroundColor(color1);
        if (item.getUseDatetime() == 0) {
            spanUseTime.append("").create();
        } else {
            spanUseTime.append(TimeUtils.millis2String(item.getUseDatetime())).setForegroundColor(color2).create();
        }
        SpanUtils.with(tv_account_num).append("面额：").setForegroundColor(color1).append(item.getDenomination() + "元").setForegroundColor(color2).create();
        SpanUtils.with(tv_card_number).append("充值卡卡号：").setForegroundColor(color1).append(item.getCardNo()).setForegroundColor(color2).create();

        SpanUtils spanStatus = SpanUtils.with(tv_card_state).append("充值状态：").setForegroundColor(color1);
        if (item.getStatus() == 1) {
            spanStatus.append("未使用").setForegroundColor(color4).create();
        } else if (item.getStatus() == 2) {
            spanStatus.append("已充值").setForegroundColor(color3).create();
        }


    }
}
