package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.CouponActivity;
import com.yibo.yiboapp.entify.CouponBean;
import com.yibo.yiboapp.interfaces.OnCouponDeleteListener;
import com.yibo.yiboapp.interfaces.OnCouponUseListener;

public class CouponRecordAdapter extends BaseQuickAdapter<CouponBean.ListBean, BaseViewHolder> {


    private OnCouponUseListener onCouponUseListener;
    private OnCouponDeleteListener onCouponDeleteListener;
    private Context context;
    int type;

    public CouponRecordAdapter(int layoutResId, CouponActivity couponActivity) {
        super(layoutResId);
        this.context = couponActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponBean.ListBean item) {
        TextView textView = helper.getView(R.id.tv_first_state);
        textView.setEnabled(false);

        if (item.getStatus() == 1) {
            //未使用
            textView.setEnabled(true);
            textView.setText("去使用");
            textView.setBackground(context.getResources().getDrawable(R.drawable.drawable_coupon_in_date));
        } else if (item.getStatus() == 2) {
            //已使用
            textView.setText("已使用");
            textView.setBackground(context.getResources().getDrawable(R.drawable.drawable_coupon_out_of_date));
        } else if (item.getStatus() == 3) {
            //已过期
            textView.setText("已过期");
            textView.setBackground(context.getResources().getDrawable(R.drawable.drawable_coupon_out_of_date));
        }

        helper.setText(R.id.tv_money, "金额：" + item.getDenomination());
        helper.setText(R.id.tv_label, item.getCouponsName() + "---" + item.getCouponsTypeStr());
        int minAmount = item.getMinAmount();
        int maxAmount = item.getMaxAmount();
        if (maxAmount > minAmount) {
            helper.setText(R.id.tv_remarks, "备注：今日充值满" + minAmount + "-" + maxAmount + "元可用");
        } else {
            helper.setText(R.id.tv_remarks, "备注：今日充值满" + minAmount + "元可用");
        }

        if (item.getUseStartDate() != 0) {
            SpanUtils.with(helper.getView(R.id.tv_term_of_validity)).append("有效期：").setForegroundColor(context.getResources().getColor(R.color.black))
                    .append(TimeUtils.millis2String(item.getUseStartDate()) + "-" + TimeUtils.millis2String(item.getUseEndDate())
                    ).setForegroundColor(context.getResources().getColor(R.color.green2)).create();
        } else {
            SpanUtils.with(helper.getView(R.id.tv_term_of_validity)).append("有效期：").setForegroundColor(context.getResources().getColor(R.color.black))
                    .append("长期有效").create();
        }


        helper.getView(R.id.tv_second_state).setOnClickListener(v -> {
            if (getOnCouponDeleteListener() != null) {
                onCouponDeleteListener.onCouponDeleteListener(helper, item);
            }
        });

        helper.getView(R.id.tv_first_state).setOnClickListener(v -> {
            if (getOnCouponUseListener() != null) {
                onCouponUseListener.onCouponUseListener(helper, item);
            }
        });


    }


    public OnCouponUseListener getOnCouponUseListener() {
        return onCouponUseListener;
    }

    public void setOnCouponUseListener(OnCouponUseListener onCouponUseListener) {
        this.onCouponUseListener = onCouponUseListener;
    }

    public OnCouponDeleteListener getOnCouponDeleteListener() {
        return onCouponDeleteListener;
    }

    public void setOnCouponDeleteListener(OnCouponDeleteListener onCouponDeleteListener) {
        this.onCouponDeleteListener = onCouponDeleteListener;
    }
}
