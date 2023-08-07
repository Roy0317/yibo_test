package com.yibo.yiboapp.views;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.BaseRecyclerAdapter;
import com.yibo.yiboapp.entify.VIPCenterBean;

import java.util.List;


/**
 * Created by Dajavu on 25/10/2017.
 */

public class FloatMenuAdapter extends BaseRecyclerAdapter<VIPCenterBean> {

    private OnItemClickListener onItemClickListener;

    private boolean isLeft;

    public FloatMenuAdapter(Context ctx, List<VIPCenterBean> list, boolean isLeft) {
        super(ctx, list);
        this.isLeft = isLeft;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(isLeft ? R.layout.item_float_menu_left : R.layout.item_float_menu_right, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        ViewHolder holder = (ViewHolder) viewHolder;
        VIPCenterBean bean = mList.get(position);
        holder.txt.setText(bean.getName());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txt;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageIcon);
            txt = itemView.findViewById(R.id.txt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }
}
