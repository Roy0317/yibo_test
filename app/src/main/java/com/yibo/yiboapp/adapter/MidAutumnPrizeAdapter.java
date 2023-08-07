package com.yibo.yiboapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.FakeBean;

import java.util.List;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: game
 * @package: com.yibo.yiboapp.adapter
 * @description: ${DESP}
 * @date: 2019/9/12
 * @time: 2:27 PM
 */
public class MidAutumnPrizeAdapter extends BaseRecyclerAdapter<FakeBean> {

    private int layoutRes;

    private List<FakeBean> rows;

    public MidAutumnPrizeAdapter(Context ctx, List<FakeBean> rows, int layoutRes) {
        super(ctx);
        this.layoutRes = layoutRes;
        this.rows = rows;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(layoutRes, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        ViewHolder holder = (ViewHolder) viewHolder;

        if (rows != null && rows.size() != 0) {
            FakeBean rowsEntity = rows.get(position);
            holder.tv_rank.setText(position + 1 + "");
            holder.tvName.setText(rowsEntity.getAccount());
            holder.tvOfficialRank.setText(rowsEntity.getProductName());
            holder.tvTimes.setText(rowsEntity.getWinMoney() + "");
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
