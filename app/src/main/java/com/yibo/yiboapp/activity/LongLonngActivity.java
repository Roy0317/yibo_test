package com.yibo.yiboapp.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.LongLoongAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.LongLonngBean;
import com.yibo.yiboapp.fragment.KaijianFragment;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.activity
 * @description: ${DESP}
 * @date: 2019/9/18
 * @time: 4:42 PM
 */
public class LongLonngActivity extends BaseActivity {

    private LongLoongAdapter longLoongAdapter;
    private List<LongLonngBean.OmmitQueueBean> list;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_long_lonng);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("两面长龙排行");
        XListView listView = findViewById(R.id.xlistview);
        listView.setPullRefreshEnable(false);
        listView.setPullLoadEnable(false);
        listView.setDivider(getResources().getDrawable(R.color.driver_line_color));
        listView.setDividerHeight(3);
        list = new ArrayList<>();
        String code = getIntent().getStringExtra("code");
        longLoongAdapter = new LongLoongAdapter(this,list,R.layout.item_long_lonng_view);
        listView.setAdapter(longLoongAdapter);
        if (TextUtils.isEmpty(code)) {
            return;
        }


        //请求长龙接口数据
        requestLongLonngData(code);
    }

    private void requestLongLonngData(String code) {

        ApiParams apiParams = new ApiParams();
        apiParams.put("code", code);

        HttpUtil.postForm(this, Urls.GET_LONG_LOONG_URL, apiParams, true, "获取中", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.getContent()!=null &&result.getContent().length()!=0) {
                    LongLonngBean longLonngBean = new Gson().fromJson(result.getContent(),LongLonngBean.class);

                    if (longLonngBean.getOmmitQueue()!=null){
                        list.addAll(longLonngBean.getOmmitQueue());
                        longLoongAdapter.notifyDataSetChanged();
                    }else{
                        ToastUtils.showShort("暂无数据,请重试");
                    }

                } else {
                    ToastUtils.showShort("暂无数据,请重试");

                }
            }
        });
    }
}
