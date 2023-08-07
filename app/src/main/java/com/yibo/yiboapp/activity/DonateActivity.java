package com.yibo.yiboapp.activity;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.DonationAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.DonateApplyResponse;
import com.yibo.yiboapp.entify.DonateBalanceResponse;
import com.yibo.yiboapp.entify.DonateRecordResponse;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.views.CustomDatePicker;
import com.yibo.yiboapp.views.DonationWayDialog;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

public class DonateActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>>{

    private RelativeLayout relativeApplyTab;
    private View bottomLineApply;
    private RelativeLayout relativeRecordTab;
    private View bottomLineRecord;

    private ScrollView scrollApply;
    private LinearLayout linearApply;
    private TextView textBalance;
    private LinearLayout linearDonationWay;
    private TextView textDonationWay;
    private LinearLayout linearDonationAmount;
    private EditText editMoney;
    private EditText editPercent;
    private Button buttonApply;
    private TextView textRule;

    private LinearLayout linearRecord;
    private TextView textStartTime;
    private TextView textEndTime;
    private Button buttonConfirm;
    private TextView textEmpty;
    private XListView xListView;

    private static final int FETCH_RECORDS = 11;
    private double accountBalance = 1000.0;//帐户余额
    private int currentWay = 0;
    private int donateAmount = 0;
    private TextWatcher moneyTextWatcher;
    private TextWatcher percentTextWatcher;

    private int currentPage = 1;
    private DonationAdapter adapter;
    private List<DonateRecordResponse.DonateRecord> records = new ArrayList<>();
    private CustomDatePicker customDatePicker1, customDatePicker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        initView();
        init();
        fetchAccountBalance();
    }

    @Override
    protected void initView() {
        super.initView();

        tvMiddleTitle.setText("会员乐捐");
        relativeApplyTab = findViewById(R.id.relativeApplyTab);
        bottomLineApply = findViewById(R.id.bottomLineApply);
        relativeRecordTab = findViewById(R.id.relativeRecordTab);
        bottomLineRecord = findViewById(R.id.bottomLineRecord);

        scrollApply = findViewById(R.id.scrollApply);
        linearApply = findViewById(R.id.linearApply);
        textBalance = findViewById(R.id.textBalance);
        linearDonationWay = findViewById(R.id.linearDonationWay);
        textDonationWay = findViewById(R.id.textDonationWay);
        linearDonationAmount = findViewById(R.id.linearDonationAmount);
        editMoney = findViewById(R.id.editMoney);
        editPercent = findViewById(R.id.editPercent);
        buttonApply = findViewById(R.id.buttonApply);
        textRule = findViewById(R.id.textRule);

        linearRecord = findViewById(R.id.linearRecord);
        textStartTime = findViewById(R.id.textStartTime);
        textEndTime = findViewById(R.id.textEndTime);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        textEmpty = findViewById(R.id.textEmpty);
        xListView = findViewById(R.id.recyclerView);


        relativeApplyTab.setOnClickListener(this);
        relativeRecordTab.setOnClickListener(this);
        linearDonationWay.setOnClickListener(this);
        buttonApply.setOnClickListener(this);

        textStartTime.setOnClickListener(this);
        textEndTime.setOnClickListener(this);
        buttonConfirm.setOnClickListener(this);

        moneyTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                int num = 0;
                try{
                    num = Integer.parseInt(s.toString());
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                editPercent.setText(String.valueOf(num*100 / ((int) accountBalance)));
                afterAmountChanged(num);
            }
        };

        percentTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                int percent = 0;
                try{
                    percent = Integer.parseInt(s.toString());
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                int num = (int) (accountBalance * percent/100);
                editMoney.setText(String.valueOf(num));
                afterAmountChanged(num);
            }
        };
    }

    private void init(){
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

        adapter = new DonationAdapter(this, records, R.layout.item_donation);
        xListView.setPullLoadEnable(false);
        xListView.setPullRefreshEnable(false);
        xListView.setDivider(getResources().getDrawable(R.color.driver_line_color));
        xListView.setDividerHeight(3);
        xListView.setXListViewListener(new ListViewListener());
        xListView.setAdapter(adapter);
    }

    private void afterAmountChanged(int amount){
        donateAmount = amount;
        buttonApply.setEnabled(amount > 0);
    }

    private void onChooseDonationWay(int way){
        currentWay = way;
        linearDonationAmount.setVisibility(View.VISIBLE);
        textDonationWay.setTextColor(ContextCompat.getColor(this, R.color.black_text_color));
        if(way == DonationWayDialog.DONATE_MONEY){
            textDonationWay.setText("定额乐捐");
            editMoney.setEnabled(true);
            editMoney.addTextChangedListener(moneyTextWatcher);
            editPercent.setEnabled(false);
            editPercent.removeTextChangedListener(percentTextWatcher);
        }else {
            textDonationWay.setText("百分比乐捐");
            editMoney.setEnabled(false);
            editMoney.removeTextChangedListener(moneyTextWatcher);
            editPercent.setEnabled(true);
            editPercent.addTextChangedListener(percentTextWatcher);
        }
    }

    private void fetchAccountBalance(){
        HttpUtil.get(this, Urls.MEMBER_DONATE_BALANCE, new ApiParams(), true, "正在获取信息", result -> {
            if(result.isSuccess()){
                DonateBalanceResponse response = new Gson().fromJson(result.getContent(), DonateBalanceResponse.class);
                if(response.isSuccess()){
                    accountBalance = response.getBalance();
                    textBalance.setText(String.valueOf(accountBalance));
                }else {
                    showToast("获取账户余额时发生错误");
                }
            }else {
                showToast(result.getMsg());
            }
        });
    }

    private void applyDonation(int money){
        ApiParams params = new ApiParams();
        params.put("money", String.valueOf(money));
        HttpUtil.get(this, Urls.MEMBER_DONATE_APPLY, params, true, "正在提交请求", result -> {
            if(result.isSuccess()){
                DonateApplyResponse response = new Gson().fromJson(result.getContent(), DonateApplyResponse.class);
                if(response.isSuccess()){
                    showToast("申请成功");
                }else {
                    showToast(response.getMsg());
                }
            }else {
                showToast(result.getMsg());
            }
        });
    }

    private void fetchRecords(){
        String start = textStartTime.getText().toString();
        String end = textEndTime.getText().toString();

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.MEMBER_DONATE_RECORD);
        configUrl.append("?startTime=").append(start)
                .append("&endTime=").append(end)
                .append("&useContent=").append(true)
                .append("&pageNumber=").append(currentPage)
                .append("&pageSize=").append(20);
        CrazyRequest<CrazyResult<DonateRecordResponse>> configRequest = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(FETCH_RECORDS)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<DonateRecordResponse>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, configRequest);
    }

    private void showRecords(List<DonateRecordResponse.DonateRecord> records, boolean canLoadMore){
        if(xListView.isPullLoading())
            xListView.stopLoadMore();
        xListView.setPullLoadEnable(canLoadMore);

        if(records == null || records.isEmpty()){
            textEmpty.setVisibility(View.VISIBLE);
            xListView.setVisibility(View.GONE);
        }else {
            textEmpty.setVisibility(View.GONE);
            xListView.setVisibility(View.VISIBLE);
            this.records.addAll(records);
            adapter.notifyDataSetChanged();
        }
    }


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
                scrollApply.setVisibility(View.VISIBLE);
                bottomLineApply.setVisibility(View.VISIBLE);
                linearRecord.setVisibility(View.GONE);
                bottomLineRecord.setVisibility(View.GONE);
                break;
            case R.id.relativeRecordTab:
                scrollApply.setVisibility(View.GONE);
                bottomLineApply.setVisibility(View.GONE);
                linearRecord.setVisibility(View.VISIBLE);
                bottomLineRecord.setVisibility(View.VISIBLE);
                records.clear();
                currentPage = 1;
                fetchRecords();
                break;
            case R.id.linearDonationWay:
                DonationWayDialog dialog = new DonationWayDialog(this, this::onChooseDonationWay);
                dialog.setCurrentWay(currentWay);
                dialog.show();
                break;
            case R.id.buttonApply:
                if(donateAmount > accountBalance){
                    showToast("乐捐金额必须小於账户余额");
                    return;
                }

                applyDonation(donateAmount);
                break;
            case R.id.textStartTime:
                customDatePicker1.show(textStartTime.getText().toString());
                break;
            case R.id.textEndTime:
                customDatePicker2.show(textEndTime.getText().toString());
                break;
            case R.id.buttonConfirm:
                records.clear();
                currentPage = 1;
                fetchRecords();
                break;
        }
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;

        if (action == FETCH_RECORDS) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("获取纪录失败");
                showRecords(null, false);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(TextUtils.isEmpty(errorString) ? "获取纪录失败" : errorString);
                showRecords(null, false);
                return;
            }

            DonateRecordResponse res = (DonateRecordResponse) result.result;
            if (!res.isSuccess() || res.getContent() == null) {
                showToast(TextUtils.isEmpty(res.getMsg()) ? "获取纪录失败" : res.getMsg());
                showRecords(null, false);
                return;
            }

            showRecords(res.getContent().getList(), res.getContent().isHasNext());
        }
    }
}