package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.ScroolTouZhuModle;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.widget.WheelView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by johnson on 2018/3/14.
 */

public class NumbersScroolAdapter extends LAdapter<String> {

    Context context;
    String codeType;
    String cpBianma;
    List<String> mDatas;
    String cpVersion;
    private int lunarYear;

    public List<WheelView> getList() {
        return list;
    }

    public void setList(List<WheelView> list) {
        this.list = list;
    }

    List<WheelView> list;


    boolean showShenxiao;

    public NumbersScroolAdapter(Context mContext, List<String> mDatas,
                                int layoutId, String codeType, String cpBianma, int lunarYear) {
        super(mContext, mDatas, layoutId);
        this.mDatas = mDatas;
        this.codeType = codeType;
        this.cpBianma = cpBianma;
        this.context = mContext;
        this.lunarYear = lunarYear;
        cpVersion = YiboPreference.instance(context).getGameVersion();
        list = new ArrayList<WheelView>();

    }

    public void setShowShenxiao(boolean showShenxiao) {
        this.showShenxiao = showShenxiao;
    }

    @Override
    public void convert(final int position, LViewHolder holder, ViewGroup parent, final String item) {
        final WheelView scr = holder.getView(R.id.scollball);
        final List<ScroolTouZhuModle> mds = UsualMethod.figureImgeByCpCodescrool(codeType, item, cpVersion, position);
        scr.setViewAdapter(new ScroolBallAdapter(context, mds,cpBianma, lunarYear));
        scr.setVisibleItems(1);
        scr.setCyclic(true);
        scr.setEnabled(false);
        scr.setDrawShadows(false);

    }

}