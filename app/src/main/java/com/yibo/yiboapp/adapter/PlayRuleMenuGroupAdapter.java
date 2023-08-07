package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;

import java.util.List;

/**
 * Created by johnson on 2017/9/18.
 * group adpter for the listview of popup window in jianjinTouzhActivity
 */

public final class PlayRuleMenuGroupAdapter extends LAdapter<PlayItem>{

    public static final String TAG = PlayRuleMenuGroupAdapter.class.getSimpleName();
    int selectPos;
    List<PlayItem> playRules;

    public PlayRuleMenuGroupAdapter(Context mContext, List<PlayItem> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        playRules = mDatas;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }

    public void updateDatas(List<PlayItem> playRules) {
        if (playRules != null) {
            this.playRules.clear();
            this.playRules.addAll(playRules);
            notifyDataSetChanged();
        }
    }

    @Override
    public void convert(int position, LViewHolder holder, ViewGroup parent, PlayItem item) {
        LinearLayout itemLayout = holder.getView(R.id.item);
        TextView text = holder.getView(R.id.group_textView);
        text.setText(item.getName());
        if (selectPos == position) {
            text.setTextColor(mContext.getResources().getColor(
                    R.color.colorPrimary));
            itemLayout.setBackgroundColor(mContext.getResources().getColor(
                    R.color.driver_line_color2));
        }else{
            text.setTextColor(mContext.getResources().getColor(
                    R.color.grey));
            itemLayout.setBackgroundColor(mContext.getResources().getColor(
                    R.color.colorWhite));
        }
    }
}
