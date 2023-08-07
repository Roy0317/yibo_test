package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by johnson on 2018/3/14.
 */

public class NumbersAdapter extends LAdapter<String> {

    Context context;
    String codeType;
    String cpBianma;
    List<String> mDatas;
    String cpVersion;
    long resultTime;//开奖时间
    String dateTime;

    boolean showShenxiao;

    public NumbersAdapter(Context mContext, List<String> mDatas,
                          int layoutId, String codeType, String cpBianma, long time) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        this.codeType = codeType;
        this.mDatas = mDatas;
        this.cpBianma = cpBianma;
        this.resultTime = time;
        cpVersion = YiboPreference.instance(context).getGameVersion();
        if (codeType.equals("6") || codeType.equals("66")) {
            if (mDatas != null && mDatas.size() != 0) {
                String str = mDatas.get(mDatas.size() - 1);
                mDatas.remove(mDatas.size() - 1);
                mDatas.add("=");
                mDatas.add(str);
            }
        }

    }

    public NumbersAdapter(Context mContext, List<String> mDatas,
                          int layoutId, String codeType, String cpBianma) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        this.codeType = codeType;
        this.mDatas = mDatas;
        this.cpBianma = cpBianma;
        if (codeType.equals("6") || codeType.equals("66")) {
            if (mDatas != null && mDatas.size() != 0) {
                String str = mDatas.get(mDatas.size() - 1);
                mDatas.remove(mDatas.size() - 1);
                mDatas.add("=");
                mDatas.add(str);
            }
        }
        cpVersion = YiboPreference.instance(context).getGameVersion();

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
        UsualMethod.figureImgeByCpCode(codeType, item, cpVersion, ball, position, context);

        if (UsualMethod.isSixMark(context,cpBianma) && showShenxiao) {
            if (summary != null) {
                summary.setVisibility(View.VISIBLE);

                long newDate = 0;
                if (!Utils.isEmptyString(dateTime)) {
                    newDate = Utils.date2TimeStamp(dateTime, Utils.DATE_FORMAT);
                }
                String s;
                if (resultTime != 0) {
                    s = UsualMethod.getNumbersFromShengXiaoName(context, item, resultTime);
                } else {
                    s = UsualMethod.getNumbersFromShengXiaoName(context, item, newDate);
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