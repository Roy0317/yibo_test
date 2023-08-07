package com.example.anuo.immodule.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.ChatPlanNewsBean;

import java.util.List;

public class ChatPlanNewsLotteryPopAdapter extends BaseQuickAdapter<ChatPlanNewsBean.SourceBean.LotteryListBean, BaseViewHolder> {

    public ChatPlanNewsLotteryPopAdapter(int layoutResId, @Nullable List<ChatPlanNewsBean.SourceBean.LotteryListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatPlanNewsBean.SourceBean.LotteryListBean item) {
        helper.getView(R.id.tv_lottery).setActivated(item.isClick());
        helper.setText(R.id.tv_lottery, item.getLotteryName());
    }
}
