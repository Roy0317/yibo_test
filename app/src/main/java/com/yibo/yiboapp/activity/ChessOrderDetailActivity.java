package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.ChessBetBean;
import com.yibo.yiboapp.entify.SportListItem;

import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 棋牌投注详情页面
 * */
public class ChessOrderDetailActivity extends BaseActivity  {

    XListView listView;
    ListAdapter recordAdapter;
    List<SportListItem> listDatas;
    DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_order_detail);
        initView();

        decimalFormat = new DecimalFormat("0.00");
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listDatas = new ArrayList<>();
        recordAdapter = new ListAdapter(this, listDatas, R.layout.item_chess_order_detail);
        listView.setAdapter(recordAdapter);
        String content = getIntent().getStringExtra("content");
        fillContents(content);
    }

    private void fillContents(String content) {

        if (Utils.isEmptyString(content)) {
            return;
        }

        ChessBetBean record = new Gson().fromJson(content, ChessBetBean.class);
        listDatas.clear();

        SportListItem item1 = new SportListItem();
        item1.setName("平台:");
        item1.setValue(record.getPlatformType());
        listDatas.add(item1);

        SportListItem item2 = new SportListItem();
        item2.setName("游戏名称:");
        item2.setValue(record.getGameName());   //item2.setValue(Utils.formatTime(record.getCreateDatetime()));
        listDatas.add(item2);

        SportListItem item3 = new SportListItem();
        item3.setName("注单号:");
        item3.setValue(record.getOrderId());
        listDatas.add(item3);

        SportListItem item4 = new SportListItem();
        item4.setName("局号:");
        item4.setValue(record.getSceneId());
        listDatas.add(item4);

        SportListItem item8 = new SportListItem();
        item8.setName("下注内容:");
        item8.setValue(record.getBettingContent());
        listDatas.add(item8);

        SportListItem item5 = new SportListItem();
        item5.setName("投注金额:");
        item5.setValue(decimalFormat.format(record.getBettingMoney()));
        listDatas.add(item5);

        SportListItem item6 = new SportListItem();
        item6.setName("有效投注:");
        item6.setValue(decimalFormat.format(record.getRealBettingMoney()));
        listDatas.add(item6);

        SportListItem item7 = new SportListItem();
        item7.setName("盈亏:");
        item7.setValue( (record.getWinMoney() - record.getBettingMoney()) + "" );
        listDatas.add(item7);

        SportListItem item11 = new SportListItem();
        item11.setName("投注时间:");
        item11.setValue(Utils.formatTime(record.getBettingTime()));
        listDatas.add(item11);

        recordAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("棋牌投注详情");
        listView = (XListView) findViewById(R.id.xlistview);
    }


    public class ListAdapter extends LAdapter<SportListItem> {

        Context context;

        public ListAdapter(Context mContext, List<SportListItem> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override public void convert(int position, LViewHolder holder, ViewGroup parent, final SportListItem item) {
            TextView sportName = holder.getView(R.id.item_chess_order_detail_txt);
            sportName.setText(item.getName() + " " + item.getValue());

        }
    }

    public static void createIntent(Context context,String content) {
        Intent intent = new Intent(context, ChessOrderDetailActivity.class);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listDatas.clear();
    }


}
