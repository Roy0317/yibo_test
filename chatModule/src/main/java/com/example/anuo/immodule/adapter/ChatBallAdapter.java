package com.example.anuo.immodule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.base.LViewHolder;
import com.example.anuo.immodule.bean.ScroolTouZhuModle;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.YiboPreferenceUtils;

import java.util.List;

import crazy_wrapper.Crazy.Utils.Utils;


public class ChatBallAdapter extends LAdapter<String> {

    Context context;
    String codeType;
    String cpBianma;
    List<String> mDatas;
    String cpVersion;
    long resultTime;//开奖时间
    String dateTime;

    boolean showShenxiao;

    public ChatBallAdapter(Context mContext, List<String> mDatas,
                          int layoutId, String codeType, String cpBianma, long time) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        this.codeType = codeType;
        this.mDatas = mDatas;
        this.cpBianma = cpBianma;
        this.resultTime = time;
        cpVersion = YiboPreferenceUtils.instance(context).getGameVersion();
        if (codeType.equals("6") || codeType.equals("66")) {
            if (mDatas != null && mDatas.size() != 0) {
                String str = mDatas.get(mDatas.size() - 1);
                mDatas.remove(mDatas.size() - 1);
                mDatas.add("=");
                mDatas.add(str);
            }
        }

    }

    public ChatBallAdapter(Context mContext, List<String> mDatas,
                          int layoutId, String codeType, String cpBianma) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        this.codeType = codeType;
        this.mDatas = mDatas;
        this.cpBianma = cpBianma;
        cpVersion = YiboPreferenceUtils.instance(context).getGameVersion();
        if (codeType.equals("6") || codeType.equals("66")) {
            if (mDatas != null && mDatas.size() != 0) {
                String str = mDatas.get(mDatas.size() - 1);
                mDatas.remove(mDatas.size() - 1);
                mDatas.add("=");
                mDatas.add(str);
            }
        }

    }

    public void setShowShenxiao(boolean showShenxiao) {
        this.showShenxiao = showShenxiao;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public void convert(final int position, LViewHolder holder, ViewGroup parent, final String item) {
        TextView ball = holder.getView(R.id.ball);
        TextView summary = holder.getView(R.id.summary);
        if (item.equals("=")) {
            ball.setText(item);
            ball.setBackground(null);
            return;
        }
        CommonUtils.figureImgeByCpCode(codeType, item, cpVersion, ball, position);

        if (CommonUtils.isSixMark(cpBianma)) {
            if (summary != null) {
                summary.setVisibility(View.VISIBLE);

                long newDate = 0;
                if (!Utils.isEmptyString(dateTime)) {
                    newDate = CommonUtils.date2TimeStamp(dateTime, CommonUtils.DATE_FORMAT);
                }
                String s;
                if (resultTime != 0) {
                    s = CommonUtils.getNumbersFromShengXiaoName(context, item, resultTime);
                } else {
                    s = CommonUtils.getNumbersFromShengXiaoName(context, item, newDate);
                }
                summary.setText(!Utils.isEmptyString(s) ? s : "无");
            }
        } else {
            if (summary != null) {
                summary.setVisibility(View.GONE);
            }
        }
    }
}