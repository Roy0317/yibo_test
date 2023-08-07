package com.yibo.yiboapp.data;

import android.content.Context;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.GameItemResult;
import com.yibo.yiboapp.entify.SportBean;
import com.yibo.yiboapp.entify.SportCalcResult;
import com.yibo.yiboapp.ui.SportTableContainer;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import crazy_wrapper.Crazy.dialog.CustomLoadingDialog;



/**
 * 体育赛事联赛主列表
 * Created by johnson on 2017/10/30.
 */

public final class SportMainListAdapter extends BaseAdapter {

    Context context;
    int gameType;//球类，足球或篮球
    String playType;//大玩法
    String categoryType;//球赛分类，滚球，今日赛事，早盘
    List<List<Map>> datas;
    MyHandler myHandler;
    LayoutInflater mLayoutInflater;
    SportExpandListAdapter.SportResultItemClick click;
    UpdateSubListListener updateSubListListener;

    public SportMainListAdapter(Context mContext, List<List<Map>> mDatas, int layoutId) {
        context = mContext;
        datas = mDatas;
        myHandler = new MyHandler(context);
        mLayoutInflater = LayoutInflater.from(context);
    }


    /**
     * 传入比赛的球类，球赛分类，大玩法
     * @param gameType 球类
     * @param category 球赛分类
     * @param playType 大玩法
     */
    public void updateType(int gameType, String category, String playType) {
        this.gameType = gameType;
        this.categoryType = category;
        this.playType = playType;
    }

    public void setResultItemListener(SportExpandListAdapter.SportResultItemClick click) {
        this.click = click;
    }

    public void setUpdateSubListListener(UpdateSubListListener updateSubListListener) {
        this.updateSubListListener = updateSubListListener;
    }

    public interface UpdateSubListListener{
        void onUpdate(int position,SportExpandListAdapter adapter);
        void beforeUpdate(int position);
        void afterUpdate(int position);
    }

    public void updateView(View view,int itemIndex,boolean isExpand) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        setDatas(viewHolder,itemIndex,isExpand);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.sport_list_item,null);
            viewHolder.header = (LinearLayout) convertView.findViewById(R.id.league);
            viewHolder.tableLayout = (LinearLayout) convertView.findViewById(R.id.item);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.league = (TextView) convertView.findViewById(R.id.name);
            viewHolder.listView = (XListView) convertView.findViewById(R.id.xlistview);
            viewHolder.indictor = (ImageView) convertView.findViewById(R.id.indictor);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        boolean isExpand = false;
        if (datas.get(position) != null) {
            if (datas.get(position).get(0) != null) {
                if (datas.get(position).get(0).containsKey("expand")&&(Boolean)
                        datas.get(position).get(0).get("expand")){
                    isExpand = true;
                }
            }
        }
        setDatas(viewHolder,position, isExpand);
        return convertView;
    }

    private void setDatas(ViewHolder viewHolder,final int position,boolean isExpand) {
        viewHolder.listView.setPullLoadEnable(false);
        viewHolder.listView.setPullRefreshEnable(false);
        viewHolder.listView.setDivider(null);
        final List<Map> item = datas.get(position);
        if (item == null) {
            return;
        }
        Map map = item.get(0);
        if (map == null) {
            return;
        }
        viewHolder.indictor.setBackgroundResource(isExpand?R.drawable.indicator_list_expand:
                R.drawable.pull_down_bottom_right);
        viewHolder.time.setText(Utils.formatTime(Long.parseLong((String)map.get("openTime")),"HH:mm"));
//        viewHolder.league.setText(map.get("league")+"("+item.size()+"场)");
        viewHolder.league.setText(map.get("league")+"");
        if (isExpand) {
            viewHolder.listView.setVisibility(View.VISIBLE);
            Utils.setListViewHeight(viewHolder.listView);

        }else {
            viewHolder.listView.setVisibility(View.GONE);
        }
        viewHolder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateSubListListener != null) {
                    updateSubListListener.beforeUpdate(position);
                }
                new LoadDataThread(context,item,position).start();
            }
        });
    }

    private final class ViewHolder{
        LinearLayout header;
        LinearLayout tableLayout;
        TextView time;
        TextView league;
        XListView listView;
        ImageView indictor;
    }

    private final class MyHandler extends Handler {

        private WeakReference<Context> mReference;
        private Context fragment;

        public MyHandler(Context fragment) {
            mReference = new WeakReference<>(fragment);
            if (mReference != null) {
                this.fragment = mReference.get();
            }
        }

        public void handleMessage(Message message) {
            if (fragment == null) {
                return;
            }
            SportCalcResult sportCalcResult = (SportCalcResult) message.obj;
            if (sportCalcResult == null) {
                return;
            }
            List<Map> maps = sportCalcResult.getSubSports();
            SportExpandListAdapter adapter = new SportExpandListAdapter(context,
                    sportCalcResult.getLists(),R.layout.football_play_item);
            adapter.bindMap(maps);
            adapter.updateType(gameType,categoryType,playType);
            adapter.setSportResultItemClick(click);
            if (updateSubListListener != null) {
                updateSubListListener.onUpdate(sportCalcResult.getSelectPos(),adapter);
            }
        }
    }


    private final class LoadDataThread extends Thread {

        Context context;
        List<Map> data;
        int position;

        LoadDataThread(Context context,List<Map> data,int position) {
            this.context = context;
            this.data = data;
            this.position = position;
        }
        @Override
        public void run() {
            super.run();
            SportCalcResult result = new SportCalcResult();
            List<List<SportBean>> lists = loadSportData(data);
            result.setLists(lists);
            result.setSubSports(data);
            result.setSelectPos(position);

            Message message = myHandler.obtainMessage(0, result);
            myHandler.sendMessage(message);
        }

        private List<List<SportBean>> loadSportData(List<Map> item) {
            int columnSize = SportTableContainer.tableColumnSize(gameType, playType);
            List<List<SportBean>> datas = new ArrayList<>();
            for (Map bean : item) {
                List<SportBean> sportBeanList = SportTableContainer.fillSportResultData(bean,
                        gameType, playType, columnSize, categoryType);
                datas.add(sportBeanList);
            }
            return datas;
        }
    }
}