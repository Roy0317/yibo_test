package com.yibo.yiboapp.data;

import android.content.Context;
import android.nfc.Tag;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.SportBean;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.SportTableContainer;
import com.yibo.yiboapp.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by johnson on 2017/10/30.
 * 联赛列表项扩展时的比赛场次数据
 */

public class SportExpandListAdapter extends LAdapter<List<SportBean>> {

    Context context;
    int gameType;//球类，足球或篮球
    String playType;//大玩法
    String categoryType;//球赛分类，滚球，今日赛事，早盘
    SportResultItemClick sportResultItemClick;
    List<Map> mainMaps;//体育每场赛事的header数据

    public SportExpandListAdapter(Context mContext, List<List<SportBean>> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        context = mContext;
    }

    public void bindMap(List<Map> maps) {
        mainMaps = maps;
    }

    /**
     * 传入比赛的球类，球赛分类，大玩法
     *
     * @param gameType 球类
     * @param category 球赛分类
     * @param playType 大玩法
     */
    public void updateType(int gameType, String category, String playType) {
        this.gameType = gameType;
        this.categoryType = category;
        this.playType = playType;
    }

    @Override
    public void convert(int position, LViewHolder holder, ViewGroup parent, final List<SportBean> item) {

        TextView currentTime = holder.getView(R.id.current_time);
        TextView scoresTV = holder.getView(R.id.scores);
        TextView isGunqiu = holder.getView(R.id.is_gunqiu);
        TextView mainName = holder.getView(R.id.main_name);
        TextView guestName = holder.getView(R.id.guest_name);
        SportTableContainer tableContainer = holder.getView(R.id.tables);

        if (mainMaps == null) {
            throw new IllegalStateException("havent bind sport main map");
        }

        Map map = mainMaps.get(position);
        if (map == null) {
            return;
        }

        if (categoryType.equals(Constant.RB_TYPE)) {
            if (gameType == SportTableContainer.FOOTBALL_PAGE) {
                String retimeset = map.containsKey("retimeset") ? (String) map.get("retimeset") : "";
                String scoreH = map.containsKey("scoreH") ? (String) map.get("scoreH") : "";
                String scoreC = map.containsKey("scoreC") ? (String) map.get("scoreC") : "";
                if (!Utils.isEmptyString(retimeset) && retimeset.contains("^")) {
                    String halfValue = retimeset.substring(0, retimeset.indexOf("^"));
                    String timeValue = retimeset.substring(retimeset.indexOf("^") + 1, retimeset.length());
//                    currentTime.setText();
                    try {
                        String html = "<html><head></head><body>" + (halfValue.equals("1H") ? "上半场" : "下半场") + " " + timeValue + "</body></html>";
                        currentTime.setText(Html.fromHtml(html, null, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (!Utils.isEmptyString(retimeset)) {
                    currentTime.setText("半场");
                }
                scoresTV.setVisibility(View.VISIBLE);
                scoresTV.setText(scoreH + "-" + scoreC);
            } else {
                String nowSession = map.containsKey("nowSession") ? (String) map.get("nowSession") : "";
                String scoreH = map.containsKey("scoreH") ? (String) map.get("scoreH") : "";
                String scoreC = map.containsKey("scoreC") ? (String) map.get("scoreC") : "";
                if (nowSession.equalsIgnoreCase("OT")) {
                    currentTime.setText("加时");
                } else if (nowSession.equalsIgnoreCase("1Q")) {
                    currentTime.setText("第一节");
                } else if (nowSession.equalsIgnoreCase("2Q")) {
                    currentTime.setText("第二节");
                } else if (nowSession.equalsIgnoreCase("3Q")) {
                    currentTime.setText("第三节");
                } else if (nowSession.equalsIgnoreCase("4Q")) {
                    currentTime.setText("第四节");
                }
                scoresTV.setVisibility(View.VISIBLE);
                scoresTV.setText(scoreH + "-" + scoreC);
            }
        } else {
            currentTime.setText(Utils.formatTime(map.containsKey("openTime") ?
                    Long.parseLong((String) map.get("openTime")) : 0, "HH:mm"));
        }
        mainName.setText(map.containsKey("home") ? (String) map.get("home") : "暂无队名");
        guestName.setText(map.containsKey("guest") ? (String) map.get("guest") : "暂无队名");
        boolean isLive = map.containsKey("live") ? Boolean.parseBoolean((String) map.get("live")) : false;
        isGunqiu.setVisibility(isLive ? View.VISIBLE : View.GONE);

        //设置表格头部
        int columns = tableContainer.fillTables(map, gameType, playType);
        //将赛果通过gridview展现出来
        GridView content = holder.getView(R.id.content);
        content.setNumColumns(columns);
        content.setAdapter(new GridViewAdapter(context, item, R.layout.sport_item_tv));
        Utils.setListViewHeightBasedOnChildren(content, columns);
    }


    public interface SportResultItemClick {
        void onSportItemClick(int ballType, String categoryType, String playType, SportBean sportBean);
    }

    public void setSportResultItemClick(SportResultItemClick sportResultItemClick) {
        this.sportResultItemClick = sportResultItemClick;
    }

    public class GridViewAdapter extends LAdapter<SportBean> {

        Context context;
        DecimalFormat pldecimalFormat;

        public GridViewAdapter(Context mContext, List<SportBean> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            pldecimalFormat = new DecimalFormat("0.00");
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final SportBean item) {
//            final TextView tvResult = holder.getView(R.id.result);
            final TextView tvPeilv = holder.getView(R.id.peilv);
            String showText = "";
            String peilv = "";
            if (!Utils.isEmptyString(item.getPeilv())) {
                peilv = pldecimalFormat.format(Float.parseFloat(item.getPeilv()));
            }
            if (!Utils.isEmptyString(item.getTxt()) && !Utils.isEmptyString(peilv)) {
                showText = item.getTxt() + "\n" + peilv;
            } else if (Utils.isEmptyString(item.getTxt()) && !Utils.isEmptyString(peilv)) {
                showText = peilv;
            } else if (!Utils.isEmptyString(item.getTxt()) && Utils.isEmptyString(peilv)) {
                showText = item.getTxt();
            } else if (Utils.isEmptyString(item.getTxt()) && Utils.isEmptyString(peilv)) {
                showText = "--";
            }
            tvPeilv.setText(showText);
            if (item.isHeader()) {
                tvPeilv.setTextColor(context.getResources().getColor(R.color.grey));
                tvPeilv.setBackgroundResource(R.drawable.middle_table_item_press);
            }
            tvPeilv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isFakeItem() || item.isHeader()) {
                        return;
                    }
                    sportResultItemClick.onSportItemClick(gameType, categoryType, playType, item);
                }
            });
        }
    }

}
