package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.interfaces.PlayItemSelectListener;
import com.yibo.yiboapp.ui.BallonView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.TouzhuFuncView;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 彩票球网格列表适配器
 */
public class BallonGridItemAdapter extends LAdapter<BallListItemInfo> {

    public static final String TAG = BallonGridItemAdapter.class.getSimpleName();
    private Context context;
    PlayItemSelectListener playItemSelectListener;
    int poolPos;//投注球列表在主列表（PeilvListAdapter）中的位置

    public BallonGridItemAdapter(Context mContext, List<BallListItemInfo> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        context = mContext;
    }

    @Override
    public void convert(int position, LViewHolder holder, ViewGroup parent, BallListItemInfo item) {
        final TouzhuFuncView ballons = holder.getView(R.id.ballon);
        ballons.setPlayItemSelectListener(playItemSelectListener);
        ballons.setBallonNum(item.getNum());
//        ballons.setSingleBallonInList(true);
//        ballons.setBallonPosWhenInList(position);
//        ballons.setPoolPos(poolPos);
//        ballons.setGridViewPos(position);
//        ballons.setSelected(item.isSelected());
        ballons.updateBallons();
    }

    public void setPlayItemSelectListener(PlayItemSelectListener listener) {
        this.playItemSelectListener = listener;
    }

    public void setPoolPos(int poolPos) {
        this.poolPos = poolPos;
    }
}
