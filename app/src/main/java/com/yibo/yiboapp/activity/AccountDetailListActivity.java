package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simon.utils.DisplayUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.MoneyChargeDrawRecordWraper;
import com.yibo.yiboapp.entify.MoneyRecordResult;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.ProgressWheel;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.DateUtils;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.CustomDatePicker;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * @author johnson
 * 帐户明细记录
 */
public class AccountDetailListActivity extends FragmentActivity implements
        View.OnClickListener, SessionResponse.Listener<CrazyResult<MoneyChargeDrawRecordWraper>> {

    PagerSlidingTabStrip tabbar = null;
    //ViewPager pager;

    protected RelativeLayout mLayout = null;
    protected TextView tvBackText;
    protected TextView tvMiddleTitle;
    protected TextView tvRightText;
    protected TextView tvSecondTitle;
    protected LinearLayout middle_layout;
    ProgressWheel progressWheel;
    protected RelativeLayout rightLayout;

    //FirstListFragment chargeFragment;
    //SecondListFragment pickFragment;

    List<MoneyRecordResult> chargeDatas;
    List<MoneyRecordResult> drawDatas;

    public static final int GET_RECORD = 0x01;
    boolean fromPickMoney = false;

    private LinearLayout llSearch; //时间选择布局

    private TextView tvStartTime;
    private TextView tvEndTime;

    private String startTime;//开始时间
    private String endTime;//结束时间

    //private static String sData = "2018-01-01 00:00:00"; //默认开始时间

    private TextView tvType;
    private PopupWindow popupWindowState;//状态的popupWindow
    private String[] typeArr;

    private int itemPos = 0; //0：充值    1：提款


    XListView listView;
    EmptyListView empty;
    MListAdapter mListAdapter;

    List<MoneyRecordResult> mDatas = new ArrayList<>();

    public int rPage = 1; //充值页面page
    public int dPage = 1; //提款页面page

    View chargeButtomView; //充值底部红色显示
    View drawButtomView;  //提款红色显示

    private TextView textHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.useTransformBar(this);
        setContentView(R.layout.activity_touzhu_record);
        initView();

        chargeDatas = new ArrayList<>();
        drawDatas = new ArrayList<>();
        fromPickMoney = getIntent().getBooleanExtra("fromPickMoney", false);
        if (fromPickMoney) {
            itemPos = 1;
            chargeButtomView.setVisibility(View.GONE);
            drawButtomView.setVisibility(View.VISIBLE);
        }

        Type listType = new TypeToken<ArrayList<MoneyRecordResult>>() {
        }.getType();
        String chargeJson = new Gson().toJson(chargeDatas, listType);
        String drawwithJson = new Gson().toJson(drawDatas, listType);


//        chargeFragment = new FirstListFragment(this, chargeJson,0);
//        pickFragment = new SecondListFragment(this, drawwithJson,1);

//        chargeFragment = FirstListFragment.getInstance(chargeJson, 0);
//        pickFragment = SecondListFragment.getInstance(drawwithJson, 1);

        listView = (XListView) findViewById(R.id.xlistview);
        empty = (EmptyListView) findViewById(R.id.empty);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new newListviewListener());
        mListAdapter = new MListAdapter(this, mDatas, R.layout.charge_listitem);
        listView.setAdapter(mListAdapter);

        textHeader = new TextView(this);
        textHeader.setGravity(Gravity.CENTER_VERTICAL);
        textHeader.setTextColor(Color.RED);
        textHeader.setPadding(DisplayUtil.dip2px(this, 10), DisplayUtil.dip2px(this, 10), 0, DisplayUtil.dip2px(this, 10));
        listView.addHeaderView(textHeader);

        getDataType(false, 1);
    }

    private void getDataType(boolean isSelect, int page) { //筛选条件
        StringBuilder params = new StringBuilder();

        String sTime = DateUtils.encodeTime(startTime + ":00");
        String eTime = DateUtils.encodeTime(endTime.substring(0, 10) + " 23:59:59");

        params.append("&startTime=").append(sTime);//没有过滤条件，暂时先定死开始时间
        params.append("&endTime=").append(eTime);
        int type = getType();
        if (isSelect) {
            if (type != 0) {
                params.append("&status=").append(type);
            }
            rPage = 1;
            dPage = 1;
            mDatas.clear();
        }
        getRecords(this, itemPos, true, params.toString(), page);
    }

    private int pageSize = 20;

    private void getRecords(Context context, int position, boolean showDialog, String params, int page) {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.CHARGE_DRAW_RECORD_URL);
        configUrl.append("?queryType=").append(position == 0 ? "recharge" : "withdraw");
        configUrl.append("&pageSize=" + pageSize + "&pageNumber=" + page);
        if (!TextUtils.isEmpty(params)) {
            configUrl.append(params);
        }
        CrazyRequest<CrazyResult<MoneyChargeDrawRecordWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(GET_RECORD)
                .headers(Urls.getHeader(context))
                .shouldCache(false)
                .placeholderText(context.getString(R.string.get_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MoneyChargeDrawRecordWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }

    private int getType() { //获取用户选择的处理状态
        String text = tvType.getText().toString();
        int res = 0;
        for (int i = 1; i < typeArr.length; i++) { //排除全部
            if (typeArr[i].equals(text)) {
                res = i;
                break;
            }
        }
        return res;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_text:
                finish();
                break;
            case R.id.tv_start_time:
                customDatePicker1.show(startTime);
                break;
            case R.id.tv_end_time:
                customDatePicker2.show(endTime);
                break;
            case R.id.status_type_tv:
                tvType.setTextColor(getResources().getColor(R.color.colorPrimary));
                if (isFirstSet) {
                    initPopupWindowContent(popupWindowState, typeArr, tvType);
                    isFirstSet = false;
                }
                popupWindowState.showAsDropDown(v);
                break;
            case R.id.btn_cancel:
                llSearch.setVisibility(View.GONE);
                break;
            case R.id.btn_confirm: //点击确认

                if (tvStartTime.getText().toString().equals("请选择开始时间")) {
                    showToast("请选择开始时间");
                    return;
                }

                if (tvEndTime.getText().toString().equals("请选择结束时间")) {
                    showToast("请选择结束时间");
                    return;
                }

                if (!Utils.judgeDate(tvStartTime.getText().toString(), tvEndTime.getText().toString())) {
                    showToast("开始时间不能大于结束时间");
                    return;
                }

                llSearch.setVisibility(View.GONE);

                getDataType(true, 1);

                break;
            case R.id.act_account_detail_list_charge_view:
                if (itemPos == 1) {
                    itemPos = 0;
                    rPage = 1;
                    chargeButtomView.setVisibility(View.VISIBLE);
                    drawButtomView.setVisibility(View.GONE);
                    mDatas.clear();
                    getDataType(false, 1);
                }
                break;
            case R.id.act_account_detail_list_draw_view:
                if (itemPos == 0) {
                    itemPos = 1;
                    dPage = 1;
                    chargeButtomView.setVisibility(View.GONE);
                    drawButtomView.setVisibility(View.VISIBLE);
                    mDatas.clear();
                    getDataType(false, 1);
                }
                break;
            default:
                break;
        }
    }


    protected void showToast(String showText) {
        if (Utils.isEmptyString(showText)) {
            return;
        }
        Toast.makeText(this, showText, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int showText) {
        Toast.makeText(this, showText, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chargeDatas.clear();
        drawDatas.clear();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<MoneyChargeDrawRecordWraper>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == GET_RECORD) {
            stopLoading();

            CrazyResult<MoneyChargeDrawRecordWraper> result = response.result;
            if (result == null) {
                showToast(getString(R.string.acquire_fail));
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.acquire_fail));
                return;
            }
            Object regResult = result.result;
            MoneyChargeDrawRecordWraper reg = (MoneyChargeDrawRecordWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent().size() > 0) {
                if (itemPos == 0) { //充值
                    if (rPage == 1) {
                        mDatas.clear();
                    }
                    mDatas.addAll(reg.getContent());
                    rPage = rPage + 1;
                } else { //提款
                    if (dPage == 1) {
                        mDatas.clear();
                    }
                    mDatas.addAll(reg.getContent());
                    dPage = dPage + 1;
                }
                updateHeader();
                mListAdapter.notifyDataSetChanged();
                if (reg.getContent().size() < pageSize) {
                    listView.setPullLoadEnable(false);
                } else {
                    listView.setPullLoadEnable(true);
                }
            } else {
                listView.setPullLoadEnable(false);
            }
        }
    }

    private void updateHeader() {
        if (mDatas.size() == 0) {
            textHeader.setVisibility(View.GONE);
            return;
        }

        String rechargeName;
        String rechargeValueName;

        if (itemPos == 0) {
            rechargeName = "充值总计：";
            rechargeValueName = "有效充值额：";
        } else {
            rechargeName = "提款总计：";
            rechargeValueName = "有效提款额：";
        }

        float money = 0f;
        float valueMoney = 0f;
        for (MoneyRecordResult mData : mDatas) {
            money += mData.getMoney();
            if (mData.getStatus() == 2l) {
                valueMoney += mData.getMoney();
            }
        }

        BigDecimal moneyStr = new BigDecimal(money).setScale(2, RoundingMode.UP);
        BigDecimal valueStr = new BigDecimal(valueMoney).setScale(2, RoundingMode.UP);

        textHeader.setText(rechargeName + moneyStr + "元\n\n" + rechargeValueName + valueStr + "元");
    }


    public static void createIntent(Context context) {
        Intent intent = new Intent(context, AccountDetailListActivity.class);
        context.startActivity(intent);
    }

    public static void createIntent(Context context, boolean fromPickMoney) {
        Intent intent = new Intent(context, AccountDetailListActivity.class);
        intent.putExtra("fromPickMoney", fromPickMoney);
        context.startActivity(intent);
    }

    boolean isFirstSet = true;

    protected void initView() {
        mLayout = (RelativeLayout) findViewById(R.id.title);

        if (mLayout != null) {
            tvBackText = (TextView) mLayout.findViewById(R.id.back_text);
            middle_layout = (LinearLayout) mLayout.findViewById(R.id.middle_layout);
            tvMiddleTitle = (TextView) middle_layout.findViewById(R.id.middle_title);
            tvRightText = (TextView) mLayout.findViewById(R.id.right_text);
            tvSecondTitle = (TextView) findViewById(R.id.second_title);
            progressWheel = (ProgressWheel) mLayout.findViewById(R.id.progress_wheel);
            tvBackText.setOnClickListener(this);
            tvMiddleTitle.setOnClickListener(this);
            tvRightText.setOnClickListener(this);
            tvBackText.setVisibility(View.VISIBLE);
            tvRightText.setVisibility(View.GONE);
            rightLayout = (RelativeLayout) findViewById(R.id.right_layout);
        }
        tvMiddleTitle.setText(getString(R.string.account_money_detail_record_string));
        //tabbar = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // pager = (ViewPager) findViewById(R.id.pager);

        llSearch = (LinearLayout) findViewById(R.id.ll_search);
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        tvType = (TextView) findViewById(R.id.status_type_tv);

        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvType.setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);

        popupWindowState = new PopupWindow(this);
        typeArr = new String[4];
        typeArr[0] = "全部";
        typeArr[1] = "处理中";
        typeArr[2] = "处理成功";
        typeArr[3] = "处理失败";


        tvRightText.setText("筛选");
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSearch.setVisibility(llSearch.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        chargeButtomView = findViewById(R.id.act_account_detail_list_charge_buttom_view);
        drawButtomView = findViewById(R.id.act_account_detail_list_draw_buttom_view);
        findViewById(R.id.act_account_detail_list_charge_view).setOnClickListener(this);
        findViewById(R.id.act_account_detail_list_draw_view).setOnClickListener(this);

        //初始化时间选择器
        initDatePicker();
        //初始化当前时间
        initCurrentTime();

        tvStartTime.setText(startTime.substring(0, 10));
        tvEndTime.setText(endTime.substring(0, 10));


    }

    /**
     * 列表下拉，上拉监听器
     *
     * @author zhangy
     */
    private final class newListviewListener implements XListView.IXListViewListener {

        public void onRefresh() {
            // AccountDetailListActivity.this.stopLoading();
            listView.stopRefresh();
        }

        public void onLoadMore() {
            //getRecords(0,false, "", rPage);
            if (itemPos == 0) {
                getDataType(false, rPage);
            } else {
                getDataType(false, dPage);
            }
        }
    }

    public class MListAdapter extends LAdapter<MoneyRecordResult> { //主页adapter

        Context context;

        public MListAdapter(Context mContext, List<MoneyRecordResult> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final MoneyRecordResult item) {
            TextView chargeName = holder.getView(R.id.charge_name);
            TextView fee = holder.getView(R.id.fee);
            TextView state = holder.getView(R.id.state);
            TextView timeTxt = holder.getView(R.id.time);
//            TextView remark = holder.getView(R.id.remark);

            if (itemPos == 0) {
                if (item.getType() != 0) {
                    if (item.getType() == 5) {
                        chargeName.setText("充值方式: 在线支付");
                    } else if (item.getType() == 6) {
                        chargeName.setText("充值方式 : 快速入款");
                    } else if (item.getType() == 7) {
                        chargeName.setText("充值方式: 一般入款");
                    } else {
                        chargeName.setText("充值方式: " + (TextUtils.isEmpty(item.getRemark()) ? "无" : item.getRemark()));
                    }
                } else {
                    chargeName.setText("充值方式: " + item.getTitle());
                }
                //chargeName.setText("在线支付");
                String feeString = !TextUtils.isEmpty(item.getFee())? item.getFee() : "0";
                fee.setText("充值金额: " + item.getMoney() + "元" + "（" + "手续费：" + feeString + "元）");
                if (item.getLockFlag() == 1) { //未鎖定
                    state.setText("待处理");
                } else {
                    state.setText(UsualMethod.convertMoneyRecordStatus(item.getStatus()));
                }
                String timeStr = "";
                if (!Utils.isEmptyString(item.getBetdate())) {
                    timeStr += item.getBetdate() + " ";
                }
                if (!Utils.isEmptyString(item.getBettime())) {
                    timeStr += item.getBettime();
                }
                timeTxt.setText(timeStr);
//                if (!Utils.isEmptyString(item.getOpDesc()) && item.getStatus() != Constant.STATUS_UNTREATED) {
//                    remark.setVisibility(View.VISIBLE);
//                    remark.setText(String.format("备注:%s", item.getOpDesc()));
//                } else
//                if (!Utils.isEmptyString(item.getRemark()) && item.getStatus() != Constant.STATUS_UNTREATED) {
//                    remark.setVisibility(View.VISIBLE);
//                    remark.setText(String.format("备注:%s", item.getRemark()));
//                } else {
//                    remark.setVisibility(View.GONE);
//                }
            } else {
                chargeName.setText(item.getTitle());
                String feeString = !TextUtils.isEmpty(item.getFee())? item.getFee() : "0";
                fee.setText("提款金额: " + item.getMoney() + "元" + "（" + "手续费：" + feeString + "元）");
                if (item.getLockFlag() == 1) { //未鎖定
                    state.setText("待处理");
                } else {
                    state.setText(UsualMethod.convertMoneyRecordStatus(item.getStatus()));
                }

                String timeStr = "";
                if (!Utils.isEmptyString(item.getBetdate())) {
                    timeStr += item.getBetdate() + " ";
                }
                if (!Utils.isEmptyString(item.getBettime())) {
                    timeStr += " " + item.getBettime();
                }
                timeTxt.setText(timeStr);
//                if (!Utils.isEmptyString(item.getOpDesc()) && item.getStatus() != Constant.STATUS_SUCCESS) {
//                    remark.setVisibility(View.VISIBLE);
//                    remark.setText(String.format("备注:%s", item.getOpDesc()));
//                } else {
//                    remark.setVisibility(View.GONE);
//                }
            }
        }
    }

    public void stopLoading() {
//        if (listView.isRefreshing()) {
//            listView.stopRefresh();
//        } else if (listView.isPullLoading()) {
//            listView.stopLoadMore();
//        }
        empty.setVisibility(View.VISIBLE);
        listView.setEmptyView(empty);
    }


    public List<MoneyRecordResult> fDatas = new ArrayList<>();

    /**
     * 充值fragment
     */
    public static class FirstListFragment extends Fragment {

        List<MoneyRecordResult> datas = new ArrayList<>();
        FirstListAdapter firstListAdapter;
        XListView listView;
        int position;
        EmptyListView empty;

        AccountDetailListActivity mAct;

//        public FirstListFragment(Context context,List<MoneyRecordResult> datas,int position){
//            this.datas = datas;
//            this.context = context;
//            this.position = position;
//            firstListAdapter = new FirstListAdapter(context, datas, R.layout.charge_listitem);
//        }

        public static FirstListFragment getInstance(String datas, int position) {

            FirstListFragment instance = new FirstListFragment();
            Bundle args = new Bundle();
            args.putString("datas", datas);
            args.putInt("position", position);
            instance.setArguments(args);

            return instance;
        }

        public FirstListFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Type listType = new TypeToken<ArrayList<MoneyRecordResult>>() {
            }.getType();
            datas = new Gson().fromJson(getArguments().getString("datas"), listType);
            position = getArguments().getInt("position");
            firstListAdapter = new FirstListAdapter(getActivity(), datas, R.layout.charge_listitem);
        }

        public void stopLoading() {
            if (listView.isRefreshing()) {
                listView.stopRefresh();
            } else if (listView.isPullLoading()) {
                listView.stopLoadMore();
            }
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
        }

        /**
         * 列表下拉，上拉监听器
         *
         * @author zhangy
         */
        private final class ListviewListener implements XListView.IXListViewListener {

            public void onRefresh() {
                //getRecords(getActivity(),position,false, "");
                FirstListFragment.this.stopLoading();
            }

            public void onLoadMore() {
                //getRecords(this.get,position,false, "", 1);
                FirstListFragment.this.stopLoading();
            }
        }

        public void updateData(List<MoneyRecordResult> data) {
            // if (data != null && !data.isEmpty()) {
            this.datas.clear();
            this.datas.addAll(data);
            firstListAdapter.notifyDataSetChanged();
            // }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View view = inflater.inflate(R.layout.charge_pick_money_list_fragment, container, false);
            listView = (XListView) view.findViewById(R.id.xlistview);
            empty = (EmptyListView) view.findViewById(R.id.empty);
            listView.setPullLoadEnable(false);
            listView.setPullRefreshEnable(true);
            listView.setXListViewListener(new ListviewListener());
            listView.setAdapter(firstListAdapter);
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        public class FirstListAdapter extends LAdapter<MoneyRecordResult> {

            Context context;

            public FirstListAdapter(Context mContext, List<MoneyRecordResult> mDatas, int layoutId) {
                super(mContext, mDatas, layoutId);
                context = mContext;
            }

            @Override
            public void convert(int position, LViewHolder holder, ViewGroup parent, final MoneyRecordResult item) {
                TextView chargeName = holder.getView(R.id.charge_name);
                TextView fee = holder.getView(R.id.fee);
                TextView state = holder.getView(R.id.state);
                TextView timeTxt = holder.getView(R.id.time);
                TextView remark = holder.getView(R.id.remark);

                if (item.getType() != 0) {
                    if (item.getType() == 5) {
                        chargeName.setText("充值方式: 在线支付");
                    } else if (item.getType() == 6) {
                        chargeName.setText("充值方式 : 快速入款");
                    } else if (item.getType() == 7) {
                        chargeName.setText("充值方式: 一般入款");
                    } else {
                        chargeName.setText("充值方式: " + (TextUtils.isEmpty(item.getRemark()) ? "无" : item.getRemark()));
                    }
                } else {
                    chargeName.setText("充值方式: " + item.getTitle());
                }
                //chargeName.setText("在线支付");
                String feeString = !TextUtils.isEmpty(item.getFee())? item.getFee() : "0";
                fee.setText("充值金额: " + item.getMoney() + "元" + "（" + "手续费：" + feeString + "元）");
                if (item.getLockFlag() == 1) { //未鎖定
                    state.setText("待处理");
                } else {
                    state.setText(UsualMethod.convertMoneyRecordStatus(item.getStatus()));
                }
                String timeStr = "";
                if (!Utils.isEmptyString(item.getBetdate())) {
                    timeStr += item.getBetdate() + " ";
                }
                if (!Utils.isEmptyString(item.getBettime())) {
                    timeStr += item.getBettime();
                }
                timeTxt.setText(timeStr);
//                if (!Utils.isEmptyString(item.getOpDesc())) {
//                    remark.setVisibility(View.VISIBLE);
//                    remark.setText(String.format("备注:%s", item.getOpDesc()));
//                } else
                if (!Utils.isEmptyString(item.getRemark())) {
                    remark.setVisibility(View.VISIBLE);
                    remark.setText(String.format("备注:%s", item.getRemark()));
                } else {
                    remark.setVisibility(View.GONE);
                }
            }
        }
    }


    public static class SecondListFragment extends Fragment {

        List<MoneyRecordResult> datas;
        SecondListAdapter pickAdapter;
        XListView listView;
        EmptyListView empty;
        int position;

        public static SecondListFragment getInstance(String datas, int position) {

            SecondListFragment instance = new SecondListFragment();
            Bundle args = new Bundle();
            args.putString("datas", datas);
            args.putInt("position", position);
            instance.setArguments(args);
            return instance;
        }

        public SecondListFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Type listType = new TypeToken<ArrayList<MoneyRecordResult>>() {
            }.getType();
            datas = new Gson().fromJson(getArguments().getString("datas"), listType);
            position = getArguments().getInt("position");
            pickAdapter = new SecondListAdapter(getActivity(), datas, R.layout.charge_listitem);
        }

        public void stopLoading() {
            if (listView.isRefreshing()) {
                listView.stopRefresh();
            } else if (listView.isPullLoading()) {
                listView.stopLoadMore();
            }
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
        }

        public void updateData(List<MoneyRecordResult> data) {
            if (data != null && !data.isEmpty()) {
                this.datas.clear();
                this.datas.addAll(data);
                pickAdapter.notifyDataSetChanged();
            }
        }

        /**
         * 列表下拉，上拉监听器
         *
         * @author zhangy
         */
        private final class ListviewListener implements XListView.IXListViewListener {

            public void onRefresh() {
                //getRecords(getActivity(),position,true, "");
                SecondListFragment.this.stopLoading();
            }

            public void onLoadMore() {
                SecondListFragment.this.stopLoading();
                //getRecords(getActivity(),position,true, "");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View view = inflater.inflate(R.layout.charge_pick_money_list_fragment, container, false);
            listView = (XListView) view.findViewById(R.id.xlistview);
            empty = (EmptyListView) view.findViewById(R.id.empty);
            listView.setPullLoadEnable(false);
            listView.setPullRefreshEnable(true);
            listView.setXListViewListener(new ListviewListener());
            listView.setAdapter(pickAdapter);
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        public final class SecondListAdapter extends LAdapter<MoneyRecordResult> {

            Context context;

            public SecondListAdapter(Context mContext, List<MoneyRecordResult> mDatas, int layoutId) {
                super(mContext, mDatas, layoutId);
                context = mContext;
            }

            @Override
            public void convert(int position, LViewHolder holder, ViewGroup parent, final MoneyRecordResult item) {
                TextView chargeName = holder.getView(R.id.charge_name);
                TextView fee = holder.getView(R.id.fee);
                TextView state = holder.getView(R.id.state);
                TextView timeTxt = holder.getView(R.id.time);
                TextView remark = holder.getView(R.id.remark);

                chargeName.setText(item.getTitle());
                String feeString = !TextUtils.isEmpty(item.getFee())? item.getFee() : "0";
                fee.setText("提款金额: " + item.getMoney() + "元" + "（" + "手续费：" + feeString + "元）");
                if (item.getLockFlag() == 1) { //未鎖定
                    state.setText("待处理");
                } else {
                    state.setText(UsualMethod.convertMoneyRecordStatus(item.getStatus()));
                }

                String timeStr = "";
                if (!Utils.isEmptyString(item.getBetdate())) {
                    timeStr += item.getBetdate() + " ";
                }
                if (!Utils.isEmptyString(item.getBettime())) {
                    timeStr += " " + item.getBettime();
                }
                timeTxt.setText(timeStr);
                if (!Utils.isEmptyString(item.getOpDesc())) {
                    remark.setVisibility(View.VISIBLE);
                    remark.setText(String.format("备注:%s", item.getOpDesc()));
                } else {
                    remark.setVisibility(View.GONE);
                }
            }
        }
    }


    private CustomDatePicker customDatePicker1, customDatePicker2;

    /**
     * 初始化时间选择器
     */
    private void initDatePicker() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//        String now = sdf.format(new Date());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = new Date(System.currentTimeMillis() - 86400000);
        Date endDate = new Date(System.currentTimeMillis());
        String defaultStartTime = "2010-01-01 00:00";
        String defaultEndTime = "2050-12-31 23:59";
//        defaultEndTime = formatter.format(endDate);
//        defaultStartTime = formatter.format(startDate);
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvStartTime.setText(time.substring(0, 10));
                startTime = time;
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动


        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvEndTime.setText(time.substring(0, 10));
                endTime = time;
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(false); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动

    }

    /**
     * 初始化当前时间
     */
    private void initCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = new Date();
        //当前时间

        //开始时间默认为当天的零时零分零秒
        startTime = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date) + " 00:00:00";
        //结束时间默认为当天的23:59:59
        endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date) + "";

    }


    /**
     * 初始化popupWindow内容
     *
     * @param popupWindow
     * @param arrays
     * @param tv
     */
    private void initPopupWindowContent(final PopupWindow popupWindow, final String[] arrays, TextView tv) {

        popupWindow.setWidth(tv.getWidth());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);//必须写,不写后果自负
        ListView listView = new ListView(this);
        listView.setDividerHeight(2);
        listView.setBackgroundColor(Color.WHITE);
        final MyBaseAdapter adapter = new MyBaseAdapter(arrays);
        listView.setAdapter(adapter);
        adapter.setSelectPosition(0);

        //获取listView的每一个item的高度
        View listItem = listView.getAdapter().getView(0, null, listView);
        listItem.measure(0, 0);
        int listItemHeight = listItem.getMeasuredHeight();

//        showToast("listItemHeight：" + listItemHeight + ",width:" + tv.getWidth() + ",arrays的长度:" + arrays.length);

        //判断listView的总高度如果大于tv.getWidth(),则设置高度为tv.getWidth()
        if (arrays.length * listItemHeight >= tv.getWidth()) {
            popupWindow.setHeight(tv.getWidth());
        }

        //设置内容
        popupWindow.setContentView(listView);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                tvType.setTextColor(getResources().getColor(R.color.system_default_color));

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = arrays[position];
                tvType.setText(item);
                adapter.setSelectPosition(position);
                popupWindow.dismiss();

            }
        });


    }


    class MyBaseAdapter extends BaseAdapter {

        private String[] arrays;

        public MyBaseAdapter(String[] arrays) {
            this.arrays = arrays;
        }

        private int selectPosition = 0;

        public void setSelectPosition(int selectPosition) {
            this.selectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return arrays.length;
        }

        @Override
        public Object getItem(int position) {
            return arrays[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyBaseAdapter.Holder holder;
            if (convertView == null) {
                holder = new MyBaseAdapter.Holder();
                convertView = View.inflate(AccountDetailListActivity.this, R.layout.adapter_item_popwindow, null);
                holder.textView = (TextView) convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (MyBaseAdapter.Holder) convertView.getTag();
            }
            String newText = arrays[position];
            if (newText.contains(",")) {
                newText = newText.split(",")[0];
            }

            holder.textView.setText(newText);

            if (selectPosition == position) {
                holder.textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                holder.textView.setBackgroundResource(R.color.check_bg);
            } else {
                holder.textView.setTextColor(getResources().getColor(R.color.grey));
                holder.textView.setBackgroundResource(R.color.white);
            }

            return convertView;
        }

        class Holder {
            TextView textView;
        }
    }


}
