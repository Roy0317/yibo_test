package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.ScroolTouZhuModle;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.widget.adapters.AbstractWheelAdapter;


import java.util.List;

public class ScroolBallAdapter extends AbstractWheelAdapter {
    private Context context;
    private List<ScroolTouZhuModle> modleList;
    private LayoutInflater mInflater;
    private String cpBianma;
    private int lunarYear;

    public ScroolBallAdapter(Context context, List<ScroolTouZhuModle> modleList, String cpBianma, int lunarYear) {
        this.context = context;
        this.modleList = modleList;
        this.mInflater = LayoutInflater.from(context);
        this.cpBianma = cpBianma;
        this.lunarYear = lunarYear;
    }

    @Override
    public int getItemsCount() {
        if (modleList == null) {
            return 0;
        } else {
            return modleList.size();
        }
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {

        ViewHolder vh = null;
        if (cachedView == null) {
            vh = new ViewHolder();
            cachedView = mInflater.inflate(R.layout.touzhu_number_scrool_item, null);

            vh.img = (TextView) cachedView.findViewById(R.id.ball);
            vh.tvSummary = cachedView.findViewById(R.id.summary);
            cachedView.setTag(vh);
        } else {
            vh = (ViewHolder) cachedView.getTag();
        }

        if ("大".equals(modleList.get(index).getNumber()) ||
                        "小".equals(modleList.get(index).getNumber()) ||
                        "单".equals(modleList.get(index).getNumber()) ||
                        "双".equals(modleList.get(index).getNumber())){
            vh.img.setTextColor(context.getResources().getColor(R.color.white));
        }
        vh.img.setText(modleList.get(index).getNumber());
        vh.img.setBackgroundResource(modleList.get(index).getBackground());
        if (UsualMethod.isSixMark(context, cpBianma)) {
            if (vh.tvSummary != null) {
                vh.tvSummary.setVisibility(View.VISIBLE);
//                long newDate = 0;
//                if (!Utils.isEmptyString(date)) {
//                    newDate = Utils.date2TimeStamp(date, Utils.DATE_FORMAT);
//                }
//                String s = UsualMethod.getNumbersFromShengXiaoName(context, modleList.get(index).getNumber(), newDate);
//                vh.tvSummary.setText(!Utils.isEmptyString(sx) ? sx : "无");

                String sx = UsualMethod.getShengXiaoForNumber(lunarYear, modleList.get(index).getNumber());
                vh.tvSummary.setText(sx);
            }
        } else {
            if (vh.tvSummary != null) {
                vh.tvSummary.setVisibility(View.GONE);
            }
        }

        return cachedView;
    }


    private class ViewHolder {
        TextView img;
        TextView tvSummary;
    }
}