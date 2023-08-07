package com.yibo.yiboapp.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.PlayPaneAdapter;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.interfaces.PlayItemSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 2018/4/8.
 * 奖金版下注页面中玩法球列表号码列表adapter
 */

public class JianjinBallPanAdapter extends LAdapter<BallListItemInfo> {

    public static final String TAG = JianjinBallPanAdapter.class.getSimpleName();
    Context context;
    List<BallListItemInfo> mDatas;
    PlayItemSelectListener playItemSelectListener;

    public JianjinBallPanAdapter(Context mContext, List<BallListItemInfo> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        this.mDatas = mDatas;
    }

    public void setPlayItemSelectListener(PlayItemSelectListener playItemSelectListener) {
        this.playItemSelectListener = playItemSelectListener;
    }

    @Override
    public void convert(final int position, LViewHolder holder, ViewGroup parent, final BallListItemInfo item) {
        Button numBtn = holder.getView(R.id.num_btn);
        if (item.isSelected()) {
            numBtn.setBackgroundResource(R.mipmap.bet_red_ball);
            numBtn.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }else{
            numBtn.setBackgroundResource(R.mipmap.bet_gray_ball);
            numBtn.setTextColor(context.getResources().getColor(R.color.blue_color));
        }
        numBtn.setText(item.getNum());
        numBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setSelected(!item.isSelected());
                mDatas.set(position, item);
                notifyDataSetChanged();
                if (playItemSelectListener != null) {
                    playItemSelectListener.onViewClick(v);
                }
            }
        });

    }
}
