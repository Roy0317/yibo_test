package com.yibo.yiboapp.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.JiajiangListAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.MemberDeficitRecord;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.views.RecycleEmptyView;
import com.yibo.yiboapp.views.SwipeRefreshLayoutExtend;
import com.yibo.yiboapp.views.loadmore.LoadMoreRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 每日加奖/周周转运 记录页面
 * type：1周周转运 2.每日加奖
 */

public class NativeRecordActivity extends BaseActivity {
    List<MemberDeficitRecord> data;
    SwipeRefreshLayoutExtend refresh;
    RecycleEmptyView mRecyclerView;
    JiajiangListAdapter mAdapter;
    LoadMoreRecyclerAdapter loadMoreRecyclerAdapter;
    int type = 0;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_native_record);
        type = getIntent().getExtras().getInt("type", 0);
        initView();
        loadData();
    }


    @Override
    protected void initView() {
        super.initView();
        refresh = findViewById(R.id.refresh);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        if (type == 1) {
            tvMiddleTitle.setText("周周转运记录");
        }
        if (type == 2) {
            tvMiddleTitle.setText("每日加奖记录");
        }
        mAdapter = new JiajiangListAdapter(this, type);
        loadMoreRecyclerAdapter = new LoadMoreRecyclerAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(loadMoreRecyclerAdapter);
    }


    //加载数据
    protected void loadData() {
        switch (type) {
            case 1://周周转运
                HttpUtil.get(this, Urls.RECORD_ZHUANYUN, null, true, getString(R.string.get_recording), new HttpCallBack() {
                    @Override
                    public void receive(NetworkResult result) {

                        if (result.isSuccess()) {
                            data = new Gson().fromJson(result.getContent(), new TypeToken<ArrayList<MemberDeficitRecord>>() {}.getType());
                            mAdapter.setList(data);
                        }
                    }
                });
                break;
            case 2://每日加奖
                HttpUtil.get(this, Urls.RECORD_JIAJIANG, null, true, getString(R.string.get_recording), new HttpCallBack() {
                    @Override
                    public void receive(NetworkResult result) {
                        if (refresh.isRefreshing()) {
                            refresh.setRefreshing(false);
                        }
                        if (result.isSuccess()) {
                            data = new Gson().fromJson(result.getContent(), new TypeToken<ArrayList<MemberDeficitRecord>>() {
                            }.getType());
                            mAdapter.setList(data);
                        }
                    }
                });
                break;

        }


    }

}
