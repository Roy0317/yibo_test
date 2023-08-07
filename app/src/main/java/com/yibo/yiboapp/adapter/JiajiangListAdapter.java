package com.yibo.yiboapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.MemberDeficitRecord;

public class JiajiangListAdapter extends BaseRecyclerAdapter {
    private int type;

    public JiajiangListAdapter(Context ctx, int type) {
        super(ctx);
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JiajiangListAdapter.ViewHolder(mInflater.inflate(R.layout.item_jiajiang, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder viewHolder = (ViewHolder) holder;
        MemberDeficitRecord data = (MemberDeficitRecord) getList().get(position);
        switch (type) {
            case 1:
                viewHolder.tv_item1.setText("上周亏损：" + data.getDeficitMoney());
                viewHolder.tv_item2.setText("转运金额：" + data.getBalance() );
                viewHolder.tv_time.setText("领取时间：" + data.getCreateTime());
                viewHolder.ll_bili.setVisibility(View.GONE);
                switch (data.getStatus()) {
                    case 5:
                        viewHolder.tv_state.setTextColor(ctx.getResources().getColor(R.color.red));
                        viewHolder.tv_state.setText("不可领取");
                        break;
                    case 4:
                        viewHolder.tv_state.setTextColor(ctx.getResources().getColor(R.color.blue_color));
                        viewHolder.tv_state.setText("可领取");
                        break;
                    case 1:
                        viewHolder.tv_state.setTextColor(ctx.getResources().getColor(R.color.green));
                        viewHolder.tv_state.setText("领取成功");
                        break;
                }
                break;
            case 2:
                viewHolder.tv_item1.setText("昨日打码：" + data.getBetNum());
                viewHolder.tv_item2.setText("加奖金额：" + data.getBalance() );
                viewHolder.tv_time.setText("领取时间：" + data.getStatDate());
                viewHolder.tv_bili.setText("加奖比例：" + data.getScale() + "%");
                switch (data.getStatus()) {
                    case 0:
                        viewHolder.tv_state.setTextColor(ctx.getResources().getColor(R.color.red));
                        viewHolder.tv_state.setText("未领取");
                        break;
                    case 1:
                        viewHolder.tv_state.setTextColor(ctx.getResources().getColor(R.color.green));
                        viewHolder.tv_state.setText("领取成功");
                        break;
                }
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item1, tv_item2, tv_time, tv_state, tv_bili;
        LinearLayout ll_bili;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item1 = itemView.findViewById(R.id.tv_ks);
            tv_item2 = itemView.findViewById(R.id.tv_zyje);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_bili = itemView.findViewById(R.id.tv_bili);
            ll_bili = itemView.findViewById(R.id.ll_bili);


        }
    }


}
