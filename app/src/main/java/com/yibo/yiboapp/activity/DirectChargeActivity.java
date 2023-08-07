package com.yibo.yiboapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.DirectChargeAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.DirectChargeResponse;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.views.CustomDatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import crazy_wrapper.Crazy.Utils.Utils;

public class DirectChargeActivity extends BaseActivity {

    private RelativeLayout relativeApplyTab;
    private View bottomLineApply;
    private RelativeLayout relativeRecordTab;
    private View bottomLineRecord;
    private LinearLayout linearApply;
    private Button buttonApply;
    private LinearLayout linearRecord;
    private TextView textStartTime;
    private TextView textEndTime;
    private Button buttonConfirm;
    private TextView textEmpty;
    private TextView textRule;

    private XListView xListView;
    private DirectChargeAdapter adapter;
    private List<DirectChargeResponse.DCRecord> mRecords = new ArrayList<>();
    private int currentPage = 1;

    private CustomDatePicker customDatePicker1, customDatePicker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_charge);

        initView();
        init();
    }

    @Override
    protected void initView() {
        super.initView();

        tvMiddleTitle.setText("免提直充");
        relativeApplyTab = findViewById(R.id.relativeApplyTab);
        bottomLineApply = findViewById(R.id.bottomLineApply);
        relativeRecordTab = findViewById(R.id.relativeRecordTab);
        bottomLineRecord = findViewById(R.id.bottomLineRecord);
        linearApply = findViewById(R.id.linearApply);
        buttonApply = findViewById(R.id.buttonApply);
        linearRecord = findViewById(R.id.linearRecord);
        textStartTime = findViewById(R.id.textStartTime);
        textEndTime = findViewById(R.id.textEndTime);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        textEmpty = findViewById(R.id.textEmpty);
        xListView = findViewById(R.id.recyclerView);
        textRule = findViewById(R.id.textRule);

        relativeApplyTab.setOnClickListener(this);
        relativeRecordTab.setOnClickListener(this);
        buttonApply.setOnClickListener(this);
        textStartTime.setOnClickListener(this);
        textEndTime.setOnClickListener(this);
        buttonConfirm.setOnClickListener(this);
    }

    private void init(){
        showRule();
        checkDirectCharge();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        textEndTime.setText(sdf.format(c.getTime()));
        c.add(Calendar.DATE, -6);
        textStartTime.setText(sdf.format(c.getTime()));

        String defaultStartTime = "2010-01-01 00:00";
        String defaultEndTime = "2030-12-31 23:59";
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                textStartTime.setText(time.substring(0, 10));
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动


        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                textEndTime.setText(time.substring(0, 10));
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(false); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动

        adapter = new DirectChargeAdapter(this, mRecords, R.layout.item_direct_charge);
        xListView.setPullLoadEnable(false);
        xListView.setPullRefreshEnable(false);
        xListView.setDivider(getResources().getDrawable(R.color.driver_line_color));
        xListView.setDividerHeight(3);
        xListView.setXListViewListener(new ListViewListener());
        xListView.setAdapter(adapter);
    }

    private void showRule(){
        try{
            SysConfig sc = UsualMethod.getConfigFromJson(this);
            double percent = Double.parseDouble(sc.getDirect_charge_gift_money_percent());
            StringBuilder sb = new StringBuilder();
            sb.append("活动规则：\n");
            sb.append("1.计算金额：会员提出申请时的余额。\n");
            sb.append(String.format("2.计算方式：10000 / 100 = 100，100 * %.2f (赠送比例%.2f%s ) = %.2f。", percent, percent, "%", 100*percent));
            sb.append(String.format("赠送彩金 %.2f + 原有余额 10000.66 = %.2f (执行后余额)。\n", 100*percent, 10000.66+100*percent));
            sb.append("3.为避免文字理解差异，本活动最终解释权归本平台所有。");
            textRule.setText(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
            textRule.setText("活动规则：\n1.计算金额：会员提出申请时的余额。");
        }
    }

    private void checkDirectCharge(){
        HttpUtil.get(this, Urls.DIRECT_CHARGE_CHECK, new ApiParams(), true, "正在获取信息", result -> {
            if(result.isSuccess()){
                try{
                    JSONObject jsonObject = new JSONObject(result.getContent());
                    updateApplyStatus(jsonObject.getBoolean("applyStatus"));
                }catch (JSONException e){
                    e.printStackTrace();
                    updateApplyStatus(false);
                }
            }else {
                showToast(result.getMsg());
                updateApplyStatus(false);
            }
        });
    }

    private void updateApplyStatus(boolean applicable){
        buttonApply.setEnabled(applicable);
        if(applicable){
            buttonApply.setText("可申请");
        }else {
            buttonApply.setText("不可申请");
        }
    }

    private void applyDirectCharge(){
        HttpUtil.get(this, Urls.DIRECT_CHARGE_APPLY, new ApiParams(), true, "正在提交申请", result -> {
            if(result.isSuccess()){
                try{
                    JSONObject jsonObject = new JSONObject(result.getContent());
                    if(jsonObject.getBoolean("success")){
                        showToast("提交申请完成");
                    }else {
                        showToast("提交申请失败");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    showToast("提交申请时发生错误");
                }
            }else {
                showToast("提交申请时发生错误：" + result.getMsg());
            }

            //重新确认可申请状态
            checkDirectCharge();
        });
    }

    private void fetchRecords(){
        String start = textStartTime.getText().toString() + " 00:00:00";
        String end = textEndTime.getText().toString() + " 23:59:59";
        ApiParams params = new ApiParams();
        params.put("startTime", start);
        params.put("endTime", end);
        params.put("useContent", true);
        params.put("page", currentPage);
        params.put("rows", 20);
        HttpUtil.get(this, Urls.DIRECT_CHARGE_RECORDS, params, true, "正在获取纪录", result -> {
            if(result.isSuccess() && result.getContent() != null){
                DirectChargeResponse response = new Gson().fromJson(result.getContent(), DirectChargeResponse.class);
                showRecords(response.getList(), response.isHasNext());
            }else {
                showToast(result.getMsg());
                showRecords(null, false);
            }
        });
    }

    private void showRecords(List<DirectChargeResponse.DCRecord> records, boolean canLoadMore){
        if(xListView.isPullLoading())
            xListView.stopLoadMore();
        xListView.setPullLoadEnable(canLoadMore);

        if(records == null || records.isEmpty()){
            textEmpty.setVisibility(View.VISIBLE);
            xListView.setVisibility(View.GONE);
        }else {
            textEmpty.setVisibility(View.GONE);
            xListView.setVisibility(View.VISIBLE);
            mRecords.addAll(records);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 列表下拉，上拉监听器
     */
    private final class ListViewListener implements XListView.IXListViewListener {

        ListViewListener() {}

        public void onRefresh() {}

        public void onLoadMore() {
            currentPage++;
            Utils.logd(TAG, "onLoadMore, currentPage = " + currentPage);
            fetchRecords();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.relativeApplyTab:
                linearApply.setVisibility(View.VISIBLE);
                bottomLineApply.setVisibility(View.VISIBLE);
                linearRecord.setVisibility(View.GONE);
                bottomLineRecord.setVisibility(View.GONE);
                break;
            case R.id.relativeRecordTab:
                linearApply.setVisibility(View.GONE);
                bottomLineApply.setVisibility(View.GONE);
                linearRecord.setVisibility(View.VISIBLE);
                bottomLineRecord.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonApply:
                applyDirectCharge();
                break;
            case R.id.textStartTime:
                customDatePicker1.show(textStartTime.getText().toString());
                break;
            case R.id.textEndTime:
                customDatePicker2.show(textEndTime.getText().toString());
                break;
            case R.id.buttonConfirm:
                currentPage = 1;
                mRecords.clear();
                fetchRecords();
                break;
        }
    }
}