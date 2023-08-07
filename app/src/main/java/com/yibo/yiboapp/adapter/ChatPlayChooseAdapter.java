package com.yibo.yiboapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simon.base.BaseRecyclerAdapter;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SubPlayItem;
import com.yibo.yiboapp.views.recycleanim.ExpandableViewHoldersUtil;


import java.util.List;

/**
 * Author:Ray
 * Date  :2019年11月01日22:19:20
 * Desc  :com.yibo.yiboapp.adapter
 */
public class ChatPlayChooseAdapter extends BaseRecyclerAdapter<List<PlayItem>> {
    private int pos;

    private int expandPst = -1; //已经展开的item位置
    private int expandChildPst = -1;
    //每一行子view个数
    private static final int ITEMNUM = 4;
    private LinearLayout.LayoutParams params;
    ;
    private int supChosePosition;
    private ExpandableViewHoldersUtil.KeepOneH keepOne;

    public void setPos(int pos) {
        this.pos = pos;
    }

    public ChatPlayChooseAdapter(Context ctx, List<List<PlayItem>> list) {
        super(ctx, list);
        keepOne = new ExpandableViewHoldersUtil.KeepOneH();
        params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_play_choose, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        final ViewHolder holder = (ViewHolder) viewHolder;
        holder.bind(position);
        final List<PlayItem> data = getList().get(position);

        holder.rootLayout.removeAllViews();
        //根据每一行的数据添加itemView
        for (int i = 0; i < data.size(); i++) {
            final PlayItem lotteryData = data.get(i);
            View itemView = getItemView(lotteryData);
            final int pst = i;
            //点击itemView就展示子玩法布局
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (lotteryData.isSelected()) {
//                        lotteryData.setSelected(false);
//                    } else {
//                        lotteryData.setSelected(true);
//                    }
//                    for (int i = 0; i < data.size(); i++) {
//                        if (i != pst) {
//                            if (data.get(i).isSelected()) {
//                                data.get(i).setSelected(false);
//                                notifyItemChanged(position);
//                            }
//                        }
//                    }
                    supChosePosition = position;
//                    for (int i = 0; i < holder.rootLayout.getChildCount(); i++) {
//                        if (i == pst) {
//                            if (holder.rootLayout.getChildAt(i).isSelected()) {
//                                holder.rootLayout.getChildAt(i).setSelected(false);
//                            } else {
//                                holder.rootLayout.getChildAt(i).setSelected(true);
//                            }
//                        } else {
//                            holder.rootLayout.getChildAt(i).setSelected(false);
//                        }
//                    }

                    //只有一项的时候不展开，直接跳转
                    if (lotteryData.getRules().size() == 1) {
                        if (selectRuleListener != null) {
                            selectRuleListener.onSelect(lotteryData, lotteryData.getRules().get(0));
                        }
                        itemView.setSelected(false);
                        setMoreData(holder, lotteryData, position, pst);
                        return;
                    }
                    clickItemView(holder, lotteryData, position, pst);
                }
            });

            holder.rootLayout.addView(itemView, params);


        }


//        vh.tv_play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectRuleListener != null) {
//                    selectRuleListener.onSelect(lotteryPlay, position);
//                }
//                if (timeCallback != null) {
//                    timeCallback.onTimeSync();
//                }
//                pos = position;
//                notifyDataSetChanged();
//            }
//        });

    }


    /**
     * 区分点击官方，信用，真人，电子，体育情况进行不同逻辑处理
     *
     * @param holder
     * @param lotteryData
     * @param position
     * @param pst
     */
    private void clickItemView(ViewHolder holder, PlayItem lotteryData, int position, int pst) {
        setMoreData(holder, lotteryData, position, pst);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements ExpandableViewHoldersUtil.Expandable {
        public LinearLayout rootLayout;
        public LinearLayout llMore;

        public ViewHolder(View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            llMore = itemView.findViewById(R.id.llMore);
        }

        @Override
        public View getExpandView() {
            return llMore;
        }

        @Override
        public ImageView getExpandImageView() {
            return null;
        }

        public void bind(int pos) {
            keepOne.bind(this, pos, true);
        }

    }

    /**
     * 设置子玩法布局
     *
     * @param holder
     * @param playItem
     * @param pst
     * @param childPst
     */
    private void setMoreData(ViewHolder holder, PlayItem playItem, int pst, int childPst) {
        List<SubPlayItem> subData = playItem.getRules();
        LinearLayout layout = null;
        holder.llMore.removeAllViews();
        if (subData != null && !subData.isEmpty() && subData.size() > 1) {
            for (int j = 0; j < subData.size(); j++) {
                if (j == 0 || j % ITEMNUM == 0) {
                    layout = getLinearLayout(LinearLayout.HORIZONTAL);
                    holder.llMore.addView(layout);
                }
                if (layout != null) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                    layout.addView(getItemChildView(playItem, subData.get(j)), params);
                }
            }
        }

        if (pst == expandPst && childPst != expandChildPst && keepOne.get_opened() != -1) {
            expandChildPst = childPst;
            return;
        }

        expandPst = pst;
        expandChildPst = childPst;
        keepOne.toggle(holder, null);
    }


    /**
     * 获取子玩法view
     *
     * @param lotteryData
     * @return
     */
    private View getItemChildView(PlayItem playItem, final SubPlayItem lotteryData) {
        final View itemView = View.inflate(ctx, R.layout.chat_play_choose_sub_item, null);
        TextView tv_play_choose = itemView.findViewById(R.id.tv_play_choose);
        setItemViewData(lotteryData, tv_play_choose);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!YiboPreference.instance(ctx).isLogin()) {
                    UsualMethod.loginWhenSessionInvalid(ctx);
                    return;
                }

                if (selectRuleListener != null) {
                    selectRuleListener.onSelect(playItem, lotteryData);
                }
//                StartActivityUtil.startTouzhuActivity(ctx, lotteryData);
            }
        });
        return itemView;
    }


    private LinearLayout getLinearLayout(int orientation) {
        LinearLayout linearLayout = new LinearLayout(ctx);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(orientation);
        return linearLayout;
    }


    /**
     * 获取每个玩法的View
     *
     * @param lotteryData
     * @return
     */
    private View getItemView(final PlayItem lotteryData) {
        final View itemView = View.inflate(ctx, R.layout.chat_play_choose_item, null);
        TextView tv_play_choose = itemView.findViewById(R.id.tv_play_choose);
        setItemViewData(lotteryData, tv_play_choose);
        return itemView;
    }

    public void resetExpend() {
        expandPst = -1;
        expandChildPst = -1;
        keepOne.resetExpend();
    }


    /**
     * 设置itemView数据
     *
     * @param lotteryData
     * @param name
     */
    private void setItemViewData(final PlayItem lotteryData, TextView name) {
        name.setText(lotteryData.getName());
    }

    /**
     * 设置itemView数据
     *
     * @param lotteryData
     * @param name
     */
    private void setItemViewData(final SubPlayItem lotteryData, TextView name) {
        name.setText(lotteryData.getName());
    }

    public void setSelectRuleListener(OnSelectRuleListener selectRuleListener) {
        this.selectRuleListener = selectRuleListener;
    }

    private OnSelectRuleListener selectRuleListener;

    public interface OnSelectRuleListener {
        void onSelect(PlayItem playItem, SubPlayItem lotteryPlay);
    }

//    public void setTimeCallback(SyncServerTimeCallback timeCallback) {
//        this.timeCallback = timeCallback;
//    }
//
//    private SyncServerTimeCallback timeCallback;
//
//    public interface SyncServerTimeCallback {
//        void onTimeSync();
//    }

}
