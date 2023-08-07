package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SubPlayItem;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;

import java.util.List;
/**
 *
 * Created by johnson on 2017/9/18.
 * sub adpter for the listview of popup window in jianjinTouzhActivity
 */

public class PlayRuleMenuChilddapter extends LAdapter<SubPlayItem>{

    public static final String TAG = PlayRuleMenuChilddapter.class.getSimpleName();
    int selectPos;
    List<SubPlayItem> mDatas;

    public PlayRuleMenuChilddapter(Context mContext, List<SubPlayItem> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        this.mDatas = mDatas;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }

    public void updateDatas(List<SubPlayItem> playRules) {
        if (playRules != null) {
            this.mDatas.clear();
            this.mDatas.addAll(playRules);
            notifyDataSetChanged();
        }
    }

    @Override
    public void convert(int position, LViewHolder holder, ViewGroup parent, SubPlayItem item) {

//        LinearLayout itemLayout = holder.getView(R.id.item);
        TextView text = holder.getView(R.id.child_textView);
        text.setText(item.getName());
        if (selectPos == position) {
            text.setTextColor(mContext.getResources().getColor(
                    R.color.colorPrimary));
        }else{
            text.setTextColor(mContext.getResources().getColor(
                    R.color.grey));
        }
    }
}
