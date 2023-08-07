package com.example.anuo.immodule.adapter;

import android.graphics.Color;
import androidx.annotation.Nullable;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.ChatPlanNewsBean;

import java.util.List;

/**
 * @author soxin
 * 2019年12月04日19:21:48
 */
public class ChatPlanNewsAdapter extends BaseQuickAdapter<ChatPlanNewsBean.SourceBean.ResultListBean, BaseViewHolder> {


    public ChatPlanNewsAdapter(int layoutResId, @Nullable List<ChatPlanNewsBean.SourceBean.ResultListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatPlanNewsBean.SourceBean.ResultListBean item) {

        if (item==null){
            return;
        }
        TextView tvQihao = helper.getView(R.id.tv_qihao);
        TextView tvResult = helper.getView(R.id.tv_result);
        TextView tvPlan = helper.getView(R.id.tv_buy_plan);
        TextView tvTime = helper.getView(R.id.tv_time);

        if (item.getLotteryNum()!=null){
            tvQihao.setText(item.getLotteryNum());
        }else{
            tvQihao.setText("暂无期号");
        }

        if (item.getLotteryResult() == null) {
            tvResult.setTextColor(Color.RED);
            tvResult.setText("待开");
            setBackgroundNull(tvPlan, tvResult);
        } else {
            tvResult.setTextColor(Color.parseColor("#8a000000"));
            tvResult.setText(item.getLotteryResult());
            if (item.getLotteryResult().equals(item.getForecast())) {
                setBackgroundRes(tvPlan, tvResult, R.drawable.point);
            } else {
                setBackgroundNull(tvPlan, tvResult);
            }
        }

        tvPlan.setText(item.getForecast()==null?"":item.getForecast());
        tvTime.setText(item.getResultDate()==null?"":item.getResultDate());
    }

    //去掉背景
    private void setBackgroundNull(TextView textView1, TextView textView2) {
        textView1.setBackground(null);
        textView2.setBackground(null);
    }

    //设置背景圆圈
    private void setBackgroundRes(TextView textView1, TextView textView2, int res) {
        textView1.setBackgroundResource(res);
        textView2.setBackgroundResource(res);
    }


}
