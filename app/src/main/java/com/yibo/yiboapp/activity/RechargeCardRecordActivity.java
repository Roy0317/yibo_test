package com.yibo.yiboapp.activity;


import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simon.widget.ToastUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.RechargeCardRecordAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.RechargeCardRecordBean;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.views.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 充值卡使用记录
 */
public class RechargeCardRecordActivity  extends BaseActivity {


    private TextView tvStartTime;//开始时间
    private TextView tvEndTime;//结束时间


    private LinearLayout llSearchLayout;
    private String startTime;//开始时间
    private String endTime;//结束时间
    private RecyclerView scrollView;


    private List<RechargeCardRecordBean.ListBean> list;
//
    private RechargeCardRecordAdapter adapter;

    private CustomDatePicker customDatePicker1,customDatePicker2;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_recharge_card_record);
        super.initView();
        initView();
        //初始化时间选择器
        initDatePicker();
        //初始化当前时间
        initCurrentTime();
        //默认
        initListener();
        initData();
        getRecordData();
    }

    protected void initView() {

        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("筛选");
        tvRightText.setOnClickListener(v -> {
            llSearchLayout.setVisibility(llSearchLayout.isShown() ? View.GONE : View.VISIBLE);
        });
        //开始时间
        tvStartTime = findViewById(R.id.tv_start_time);
        //结束时间
        tvEndTime = findViewById(R.id.tv_end_time);
        //头部搜索布局
        llSearchLayout = findViewById(R.id.ll_search);

        findViewById(R.id.ll_lottery_type).setVisibility(View.GONE);
        scrollView =findViewById(R.id.scrollView);
        scrollView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    private void initListener() {
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        //取消和确定的按钮
        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }


    protected void initData() {
        list = new ArrayList<>();
        adapter = new RechargeCardRecordAdapter(R.layout.item_record_recharge_card, list, this);
        scrollView.setLayoutManager(new LinearLayoutManager(this));
        scrollView.setAdapter(adapter);

        View emptyView = View.inflate(this,R.layout.listview_empty_view,null);
        TextView clickFresh = emptyView.findViewById(R.id.click_refresh);
        clickFresh.setOnClickListener(v -> getRecordData());
        adapter.setEmptyView(emptyView);
        tvMiddleTitle.setText("充值卡使用记录");
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_confirm:
                //访问接口
                llSearchLayout.setVisibility(View.GONE);
                getRecordData();
            case R.id.btn_cancel:
                llSearchLayout.setVisibility(View.GONE);
                break;
            case R.id.tv_start_time:
                customDatePicker1.show(startTime);
                break;
            case R.id.tv_end_time:
                customDatePicker2.show(endTime);
                break;
        }
    }

    private void getRecordData() {
        ApiParams apiParams = new ApiParams();
        apiParams.put("startTime", startTime);
        apiParams.put("endTime", endTime);
        HttpUtil.get(this, Urls.GET_RECHARGE_RECORD, apiParams, true, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (!TextUtils.isEmpty(result.getContent())) {
                    RechargeCardRecordBean bean = new Gson().fromJson(result.getContent(), RechargeCardRecordBean.class);
                    List<RechargeCardRecordBean.ListBean> rows = bean.getList();
                    adapter.getData().clear();
                    if (rows!=null){
                        adapter.addData(rows);
                    }else{
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtil.showToast(RechargeCardRecordActivity.this, "暂无数据,请重试");
                }
            }
        });

    }



    /**
     * 初始化时间选择器
     */
    private void initDatePicker() {
        String defaultStartTime = "2010-01-01 00:00:00";
        String defaultEndTime = "2050-12-31 23:59:00";
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvStartTime.setText(time);
                startTime = time+":00";
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动
        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvEndTime.setText(time);
                endTime = time+":00";
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }



    /**
     * 初始化当前时间
     */
    private void initCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date date = new Date();
        //开始时间默认为当天的零时零分零秒
        startTime = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date) + " 00:00:00";
        //结束时间默认为当天的23:59:59
        endTime = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date) + " 23:59:59";

        tvStartTime.setText(startTime.substring(0, startTime.length() - 3));
        tvEndTime.setText(endTime.substring(0, endTime.length() - 3));


    }

}
