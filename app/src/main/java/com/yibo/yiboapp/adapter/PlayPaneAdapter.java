package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.BallonItem;
import com.yibo.yiboapp.entify.BallonRules;
import com.yibo.yiboapp.interfaces.PlayItemSelectListener;
import com.yibo.yiboapp.ui.BallonView;
import com.yibo.yiboapp.ui.JianjinBallPanAdapter;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.TouzhuFuncView;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PlayPaneAdapter extends LAdapter<BallonRules> {

    public static final String TAG = PlayPaneAdapter.class.getSimpleName();
    Context context;
    PlayItemSelectListener playItemSelectListener;
    List<BallonRules> mDatas;
    int screenWidth;
    boolean isExpanded = true;//玩法栏是否处于展现状态
    public static final float FIXED_BALL_WIDTH = 50.0f;
    public static final float FIXED_PLAYBAR_WIDTH = 25f;

    public PlayPaneAdapter(Context mContext, List<BallonRules> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        this.mDatas = mDatas;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setPlayBarStatus(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    @Override
    public void convert(final int position, LViewHolder holder, ViewGroup parent, final BallonRules item) {

        final TouzhuFuncView weishu = holder.getView(R.id.weishu_view);
        final TextView playRule = holder.getView(R.id.play_rule);
        TouzhuFuncView funcView = holder.getView(R.id.func_view);
        GridView ballViews = holder.getView(R.id.ball_view);

        weishu.setVisibility(item.isShowWeiShuView() ? View.VISIBLE : View.GONE);
        weishu.setPlayItemSelectListener(playItemSelectListener);
        playRule.setText(item.getRuleTxt());
        if (item.isShowWeiShuView()) {
            weishu.updateBallons(item.getWeishuInfo());
        }
        //位数所有子项的监听事件
        weishu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BallonView ballonView = (BallonView) v;
                List<BallListItemInfo> weishuInfo = item.getWeishuInfo();
                int index = ballonView.getViewIndex();
                List<BallListItemInfo> data = new ArrayList<BallListItemInfo>();
                for (int i = 0; i < weishuInfo.size(); i++) {
                    BallListItemInfo info = weishuInfo.get(i);
                    if (i == index) {
                        info.setSelected(!info.isSelected());
                    }
                    data.add(info);
                }
                weishuInfo.clear();
                item.setWeishuInfo(data);
                mDatas.set(position, item);
                notifyDataSetChanged();
                //点击音效及动画
                if (playItemSelectListener != null) {
                    playItemSelectListener.onViewClick(ballonView);
                }
            }
        });

        funcView.setVisibility(item.isShowFuncView() ? View.VISIBLE : View.GONE);
        funcView.setPlayItemSelectListener(playItemSelectListener);
        //功能键所有子项的监听事件
        funcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BallListItemInfo> ballListItemInfos = onFuncViewClick(v, item.getBallonsInfo());
                item.setBallonsInfo(ballListItemInfos);
                mDatas.set(position, item);
                notifyDataSetChanged();
                if (playItemSelectListener != null) {
                    playItemSelectListener.onViewClick(v);
                }
            }
        });
        int column = (int) (((isExpanded ? screenWidth * 0.75 : screenWidth) -
                Utils.dip2px(context, FIXED_PLAYBAR_WIDTH)) / Utils.dip2px(context, FIXED_BALL_WIDTH));
        Utils.LOG(TAG, "the figure out column == " + column);

        ballViews.setNumColumns(column);
        JianjinBallPanAdapter ballPanAdapter = new JianjinBallPanAdapter(context, item.getBallonsInfo(), R.layout.jianjin_pan_ball_item);
        ballViews.setAdapter(ballPanAdapter);
        ballPanAdapter.setPlayItemSelectListener(playItemSelectListener);
        Utils.setListViewHeightBasedOnChildren(ballViews, column);
    }

    //功能视图点击时的列表数据变化逻辑
    private List<BallListItemInfo> onFuncViewClick(View ballon, List<BallListItemInfo> infos) {
        if (ballon == null) {
            return infos;
        }
        if (!(ballon instanceof BallonView)) {
            return infos;
        }
        BallonView bv = (BallonView) ballon;
        if (bv.getBallonType() != Constant.LETTER_VIEW) {
            return infos;
        }
        if (infos == null || infos.isEmpty()) {
            return infos;
        }
        int size = infos.size();
        String ballonNumber = bv.getNumber();
        List<BallListItemInfo> results = new ArrayList<BallListItemInfo>();
        for (int i = 0; i < size; i++) {
            BallListItemInfo binfo = infos.get(i);

            if (ballonNumber.equals("全")) {
                binfo.setSelected(true);
            } else if (ballonNumber.equals("大")) {
                if (i >= size / 2) {
                    binfo.setSelected(true);
                } else {
                    binfo.setSelected(false);
                }
            } else if (ballonNumber.equals("小")) {
                if (i >= size / 2) {
                    binfo.setSelected(false);
                } else {
                    binfo.setSelected(true);
                }
            } else if (ballonNumber.equals("单")) {
                String num = binfo.getNum();
                if (!Utils.isEmptyString(num) && Utils.isNumeric(num)) {
                    int numValue = 0;
                    if (num.length() > 1) {
                        num = num.substring(num.length() - 1, num.length());
                    }
                    numValue = Integer.parseInt(num);
                    if (numValue % 2 == 0) {
                        binfo.setSelected(false);
                    } else {
                        binfo.setSelected(true);
                    }
                } else {
                    binfo.setSelected(false);
                }
            } else if (ballonNumber.equals("双")) {
                String num = binfo.getNum();
                if (!Utils.isEmptyString(num) && Utils.isNumeric(num)) {
                    int numValue = 0;
                    if (num.length() > 1) {
                        num = num.substring(num.length() - 1, num.length());
                    }
                    numValue = Integer.parseInt(num);
                    if (numValue % 2 == 0) {
                        binfo.setSelected(true);
                    } else {
                        binfo.setSelected(false);
                    }
                } else {
                    binfo.setSelected(false);
                }
            } else if (ballonNumber.equals("清")) {
                binfo.setSelected(false);
            }
            results.add(binfo);
        }
        infos.clear();
        infos.addAll(results);
        return infos;
    }


    public void setPlayItemSelectListener(PlayItemSelectListener listener) {
        this.playItemSelectListener = listener;
    }
}
