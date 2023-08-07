package com.yibo.yiboapp.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.IncommeListAdapter;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.IncomeListData;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.views.RecycleEmptyView;
import com.yibo.yiboapp.views.SwipeRefreshLayoutExtend;
import com.yibo.yiboapp.views.loadmore.LoadMoreRecyclerAdapter;

public class   JijinZhangdanActivity extends BaseActivity {
    IncomeListData data;
    SwipeRefreshLayoutExtend refresh;
    TextView tv_back;
    RecycleEmptyView mRecyclerView;
    IncommeListAdapter mAdapter;
    LoadMoreRecyclerAdapter loadMoreRecyclerAdapter;
    int pageNumber = 1;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_jijinzhangdan);
        initView();
        loadData();
    }


    @Override
    protected void initView() {
        super.initView();
        refresh = findViewById(R.id.refresh);
        tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mAdapter = new IncommeListAdapter(this);
        loadMoreRecyclerAdapter = new LoadMoreRecyclerAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(loadMoreRecyclerAdapter);
        refresh.setmRefreshListener(new SwipeRefreshLayoutExtend.onRefrshListener() {
            @Override
            public void onPullDownRefresh() {
                pageNumber=1;
                loadData();
            }

            @Override
            public void onPullupLoadMore() {
                loadData();
                pageNumber++;
            }
        });

    }


    //加载数据
    protected void loadData()
        {
            ApiParams params = new ApiParams();
            params.put("pageNumber", pageNumber);
            params.put("pageSize", Constant.PAGE_SIZE);
            HttpUtil.postForm(this, Urls.INCOME_ORDER_URL, params, true, getString(R.string.get_recording), new HttpCallBack() {
                @Override
                public void receive(NetworkResult result) {
                    if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                    }
                    if (result.isSuccess()) {
                        Log.e("123123", result.getContent());

                        data = new Gson().fromJson(result.getContent(), IncomeListData.class);

                        if (pageNumber == 1) {
                            mAdapter.getList().clear();
                        }
                        mAdapter.addAll(data.getList());
                        pageNumber++;
                    }
                }
            });

        }
}