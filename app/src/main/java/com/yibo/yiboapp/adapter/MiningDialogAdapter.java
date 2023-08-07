package com.yibo.yiboapp.adapter;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.WakuangListBean;


public class MiningDialogAdapter extends BaseQuickAdapter<WakuangListBean, BaseViewHolder> {


    public MiningDialogAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, WakuangListBean item) {

        int awardType = item.getAwardType();

        String miningType = "";
        if (awardType==1){
            miningType = "钻石";
        } else if (awardType ==2){
            miningType = "黄金";
        }else if (awardType ==3 ){
            miningType = "白银";
        }else if (awardType==4){
            miningType ="青铜";
        }

        helper.setText(R.id.tv_mining_type,miningType);
        helper.setText(R.id.tv_mining_reward,item.getAwardValue()+"");
        helper.setText(R.id.tv_date_time, TimeUtils.millis2String(item.getCreateDatetime()));
        helper.setText(R.id.tv_status,item.getStatus()==2?"兑换成功":"兑换中");
    }
}
