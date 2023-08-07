package com.yibo.yiboapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.MyPrizeDataBean;

import java.util.List;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: game
 * @package: com.yibo.yiboapp.adapter
 * @description: ${DESP}
 * @date: 2019/9/12
 * @time: 4:15 PM
 */
public class MidAutumnMyPrizeAdapter extends BaseRecyclerAdapter<MyPrizeDataBean> {
    private int layoutRes;

    private List<MyPrizeDataBean> rows;

    public MidAutumnMyPrizeAdapter(Context ctx, List<MyPrizeDataBean> rows, int layoutRes) {
        super(ctx);
        this.layoutRes = layoutRes;
        this.rows = rows;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MidAutumnMyPrizeAdapter.ViewHolder(mInflater.inflate(layoutRes, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        MidAutumnMyPrizeAdapter.ViewHolder holder = (ViewHolder) viewHolder;

        if (rows != null && rows.size() != 0) {
            MyPrizeDataBean rowsEntity = rows.get(position);
            holder.tv_rank.setText(rowsEntity.getHaoMa());
            holder.tvName.setText(rowsEntity.getProductName());
            holder.tvOfficialRank.setText(rowsEntity.getCreateDatetime());
            String str = "";
            switch (rowsEntity.getAwardType()) {
                case 1://不中奖
                    str = "不中奖";
                    if (!TextUtils.isEmpty(rowsEntity.getProductName())) {
                        holder.tvName.setText(rowsEntity.getProductName());
                    }
                    if (rowsEntity.getAwardValue() != 0) {
                        holder.tvName.setText(str + rowsEntity.getAwardValue());
                    }

                    break;
                case 2://现金
                    str = "现金";
                    if (rowsEntity.getAwardValue() != 0) {
                        holder.tvName.setText(str + rowsEntity.getAwardValue());
                    }
                    break;
                case 3://商品
                    if (!TextUtils.isEmpty(rowsEntity.getProductName())) {
                        holder.tvName.setText(rowsEntity.getProductName());
                    }
                    break;
                case 4://积分
                    str = "积分";
                    if (rowsEntity.getAwardValue() != 0) {
                        holder.tvName.setText(str + rowsEntity.getAwardValue());
                    }
                    break;

            }


            String state;
            if (rowsEntity.getStatus() == 1) {
                state = "未兑换";
            } else if (rowsEntity.getStatus() == 2) {
                state = "兑换成功";
            } else if (rowsEntity.getStatus() == 3) {
                state = "兑换失败";
            } else if (rowsEntity.getStatus() == 4) {
                state = "系统取消";
            } else {
                state = "";
            }
            holder.tvTimes.setText(state);
        }


    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rank;
        TextView tvName;
        TextView tvOfficialRank;
        TextView tvTimes;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tvName = itemView.findViewById(R.id.tv_name);
            tvOfficialRank = itemView.findViewById(R.id.tv_official_rank);
            tvTimes = itemView.findViewById(R.id.tv_times);

        }
    }
}
