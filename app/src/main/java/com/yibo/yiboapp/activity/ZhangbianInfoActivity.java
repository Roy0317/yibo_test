package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.AccountChangeResult;
import com.yibo.yiboapp.entify.AccountRecord;
import com.yibo.yiboapp.entify.BcLotteryOrder;
import com.yibo.yiboapp.entify.ChessBetBean;
import com.yibo.yiboapp.entify.NewSportOrderBean;
import com.yibo.yiboapp.entify.RealBetBean;
import com.yibo.yiboapp.entify.SBSportOrder;
import com.yibo.yiboapp.entify.SportOrder;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.SportTableContainer;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.CustomDatePicker;
import com.yibo.yiboapp.views.CustomPopupWindow;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.response.SessionResponse;


/**
 * Author: Ray
 * created on 2018/10/19
 * description : 账变记录
 */
public class ZhangbianInfoActivity extends BaseActivity implements View.OnClickListener, SessionResponse.Listener<CrazyResult> {

    //给所有查看投注记录动作时使用
    public static void createIntent(Context context, String name, int status, String cpBianma) {
        Intent intent = new Intent(context, ZhangbianInfoActivity.class);
        intent.putExtra("cp_name", name);
        intent.putExtra("recordType", status);
        intent.putExtra("cpBianma", cpBianma);
        context.startActivity(intent);
    }

    //针对体育投注页面的跳转接口
    public static void createIntent(Context context, String name, int status, int ballType, boolean fromSportBetPage) {
        Intent intent = new Intent(context, ZhangbianInfoActivity.class);
        intent.putExtra("cp_name", name);
        intent.putExtra("recordType", status);
        intent.putExtra("ballType", ballType);
        intent.putExtra("fromSportBetPage", fromSportBetPage);
        context.startActivity(intent);
    }

    private List<AccountRecord> accountRecords;
    private AccountRecordAdapter accountRecordAdapter;
    private LinearLayout llSearchLayout;
    private XListView recordList;
    private EmptyListView empty;
    private int recordType;
    private int pageIndex = 1;
    private int pageSize = 20;


    //状态 时间 彩种的三个布局
    private LinearLayout llState;

    private TextView tvState;//状态
    private TextView tvType;//彩种

    private TextView tvStartTime;//开始时间
    private TextView tvEndTime;//结束时间

    private String type;
    private String startTime;//开始时间
    private String endTime;//结束时间
    private CustomPopupWindow popupWindowType;//类型的popupWindow

    private boolean cpcdOpen = false;

    private FrameLayout flContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caipiao_record_new);
        //初始化title
        initView();

        //初始化控件
        initFindViewById();

        //子布局
        View mContentView = View.inflate(this, R.layout.touzhu_record_content, null);
        //listView布局
        recordList = (XListView) mContentView.findViewById(R.id.xlistview);
        recordType = getIntent().getIntExtra("recordType", Constant.CAIPIAO_RECORD_STATUS);
        //设置headerView
        if (recordType == Constant.REAL_PERSON_RECORD_STATUS ||
                recordType == Constant.ELECTRIC_GAME_RECORD_STATUS ||
                recordType == Constant.CAIPIAO_RECORD_STATUS ||
                recordType == Constant.LHC_RECORD_STATUS ||
                recordType == Constant.CHESS_GAME_RECORD_STATUS) {

            SysConfig config = UsualMethod.getConfigFromJson(this);
            if (config.getMobile_v3_bet_order_detail_total().equalsIgnoreCase("on")) {
                View view = LayoutInflater.from(this).inflate(R.layout.touzhu_attach_header, null);
                recordList.addHeaderView(view);
            }
        }

        //设置listView的各种属性
        recordList.setPullLoadEnable(false);
        recordList.setPullRefreshEnable(true);
        recordList.setDivider(getResources().getDrawable(R.color.driver_line_color));
        recordList.setDividerHeight(3);
        recordList.setXListViewListener(new ListviewListener());

        empty = (EmptyListView) mContentView.findViewById(R.id.empty_list);
        empty.setListener(emptyListviewListener);

        //初始化时间选择器
        initDatePicker();
        //初始化当前时间
        initCurrentTime();

        //请求记录
        initRecyclerView();
        actionRecords(true);

        SysConfig config = UsualMethod.getConfigFromJson(this);
        if (config != null) {
            if (!Utils.isEmptyString(config.getLottery_order_cancle_switch()) &&
                    config.getLottery_order_cancle_switch().equalsIgnoreCase("on")) {
                cpcdOpen = true;
            }
        }
        flContent.addView(mContentView);
    }

    //初始化控件
    private void initFindViewById() {
        //头部搜索布局
        llSearchLayout = (LinearLayout) findViewById(R.id.ll_search);
        //状态
        llState = (LinearLayout) findViewById(R.id.ll_state);
        //状态
        tvState = (TextView) findViewById(R.id.tv_state);
        //类型
        tvType = (TextView) findViewById(R.id.tv_type);
        //开始时间
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        //结束时间
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        //彩种 球种 平台
        //内容部分
        flContent = (FrameLayout) findViewById(R.id.fl_content);

        TextView tv_type_title = findViewById(R.id.tv_type_title);
        tv_type_title.setText("类型：");
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        llState.setVisibility(View.GONE);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);

        tvState.setOnClickListener(this);
        tvType.setOnClickListener(this);

    }

    private void initRecyclerView() {
        //请求记录
        if (recordType == Constant.ACCOUNT_CHANGE_RECORD_STATUS) {
            accountRecords = new ArrayList<>();
            accountRecordAdapter = new AccountRecordAdapter(this, accountRecords, R.layout.account_record_item);
            recordList.setAdapter(accountRecordAdapter);
        }
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_type:
                tvType.setTextColor(getResources().getColor(R.color.colorPrimary));
                popupWindowType.showAsDropDown(v);
                break;
            case R.id.tv_start_time:
                customDatePicker1.show(startTime);
                break;
            case R.id.tv_end_time:
                customDatePicker2.show(endTime);
                break;
            case R.id.btn_cancel:
                llSearchLayout.setVisibility(View.GONE);
                break;
            case R.id.btn_confirm:
                //访问接口
                if (!Utils.judgeTime(tvStartTime.getText().toString(), tvEndTime.getText().toString())) {
                    showToast("开始时间不能大于结束时间");
                    return;
                }
                if (Utils.timeoffset(startTime, endTime)) {
                    showToast("只能查询31天内的数据");
                    return;
                }
                llSearchLayout.setVisibility(View.GONE);
                pageIndex = 1;
                accountRecords.clear();
                actionRecords(true);
                break;
        }
    }


    private CustomDatePicker customDatePicker1, customDatePicker2;

    /**
     * 初始化时间选择器
     */
    private void initDatePicker() {
        String defaultStartTime = "2010-01-01 00:00";
        String defaultEndTime = "2050-12-31 23:59";
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvStartTime.setText(time);
                startTime = time;
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动
        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvEndTime.setText(time);
                endTime = time;
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }

    private boolean isFirstSet = true;

    @Override
    protected void initView() {
        super.initView();

        //初始化标题
        String name = getIntent().getStringExtra("cp_name");
        if (!Utils.isEmptyString(name)) {
            tvMiddleTitle.setText(name);
        } else {
            tvMiddleTitle.setText(getString(R.string.touzhu_record));
        }
        //右面的筛选文字
        tvRightText.setText("筛选");
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSearchLayout.setVisibility(llSearchLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                //设置显示或隐藏项
                if (isFirstSet && llSearchLayout.getVisibility() == View.VISIBLE) {
                    setLinearLayoutState();
                    isFirstSet = false;
                }
            }
        });
    }

    private void setLinearLayoutState() {
        if (llSearchLayout.isShown()) {
            tvType.post(() -> {
                String[] arrays = getResources().getStringArray(R.array.zhangbian_info);
                popupWindowType = new CustomPopupWindow(this, tvType, arrays);
            });
        }
    }


    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
        @Override
        public void onEmptyListviewClick() {
            pageIndex = 1;
            accountRecords.clear();
            actionRecords(true);
        }
    };


    /**
     * 列表下拉，上拉监听器
     */
    private final class ListviewListener implements XListView.IXListViewListener {

        ListviewListener() {}

        public void onRefresh() {
            pageIndex = 1;
            accountRecords.clear();
            actionRecords(false);
        }

        public void onLoadMore() {
            pageIndex++;
            actionRecords(false);
        }
    }

    private void actionRecords(boolean showDialog) {
        getAccountRecords(startTime, endTime, pageIndex, pageSize, showDialog);
        if (!showDialog) {
            startProgress();
        }
    }


    /**
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param page       页码
     * @param pageSize
     * @param showDialog
     */
    private void getAccountRecords(String startTime, String endTime, int page, int pageSize, boolean showDialog) {
        ApiParams params = new ApiParams();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("pageNumber", page);
        params.put("pageSize", pageSize);

        if (null != popupWindowType) {
            type = popupWindowType.getType();
            params.put("type", type);
        }

        HttpUtil.get(this, Urls.ACCOUNT_CHANGE_RECORD_URL_V2, params, showDialog, getString(R.string.get_recording), new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (recordList.isRefreshing()) {
                    recordList.stopRefresh();
                } else if (recordList.isPullLoading()) {
                    recordList.stopLoadMore();
                }
                empty.setVisibility(View.VISIBLE);
                recordList.setEmptyView(empty);
                if (result.isSuccess()) {
                    YiboPreference.instance(ZhangbianInfoActivity.this).setToken(result.getAccessToken());
                    AccountChangeResult reg = new Gson().fromJson(result.getContent(), AccountChangeResult.class);
                    long totalCountFromWeb = reg.getTotalCount();
                    if (!reg.getResults().isEmpty()) {
                        accountRecords.addAll(reg.getResults());
                        accountRecordAdapter.notifyDataSetChanged();
                    }
                    recordList.setPullLoadEnable(totalCountFromWeb > accountRecords.size());
                } else {
                    showToast(!Utils.isEmptyString(result.getMsg()) ? result.getMsg() :
                            getString(R.string.get_record_fail));
                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (result.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(ZhangbianInfoActivity.this);
                    }
                }
            }
        });
    }

    /**
     * 帐变记录adapter
     */
    public class AccountRecordAdapter extends LAdapter<AccountRecord> {

        Context context;
        DecimalFormat decimalFormat;

        public AccountRecordAdapter(Context mContext, List<AccountRecord> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            decimalFormat = new DecimalFormat("0.00");
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final AccountRecord item) {
            TextView orderName = holder.getView(R.id.orderno);
            TextView moneyBefore = holder.getView(R.id.money_before);
            TextView changeMoney = holder.getView(R.id.change_money);
            TextView moneyAfter = holder.getView(R.id.after_money);
            TextView fee = holder.getView(R.id.fee);
            TextView time = holder.getView(R.id.time);
            LinearLayout linearLayout = holder.getView(R.id.item);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String changeJson = new Gson().toJson(item, AccountRecord.class);
                    AccountChangeDetailActivity.createIntent(context, changeJson);
                }
            });

            orderName.setText(UsualMethod.convertAccountChangeTypeToString(item.getType()));
            moneyBefore.setText(String.format(getString(R.string.change_before_money_format),
                    decimalFormat.format(item.getMoneyBefore())));
            moneyAfter.setText(String.format(getString(R.string.change_after_money_format),
                    decimalFormat.format(item.getMoneyAfter())));

            BigDecimal decimalAfter = BigDecimal.valueOf(item.getMoneyAfter());
            BigDecimal decimalBefore = BigDecimal.valueOf(item.getMoneyBefore());
            changeMoney.setText(String.format(getString(R.string.biandong_money_format),
                    decimalFormat.format(decimalAfter.subtract(decimalBefore).doubleValue())));

            if(item.getType()<=7){
                fee.setVisibility(View.VISIBLE);
                fee.setText(String.format(getString(R.string.fee_money_format), item.getFee()));
            } else {
                fee.setVisibility(View.GONE);
            }
            time.setText(item.getTimeStr());
        }
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult> response) {}
}
