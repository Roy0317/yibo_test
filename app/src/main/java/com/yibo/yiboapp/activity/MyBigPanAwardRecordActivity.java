//package com.yibo.yiboapp;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.yibo.yiboapp.data.Constant;
//import com.yibo.yiboapp.data.LotteryData;
//import com.yibo.yiboapp.data.TouzhuThreadPool;
//import com.yibo.yiboapp.data.Urls;
//import com.yibo.yiboapp.data.UsualMethod;
//import com.yibo.yiboapp.data.YiboPreference;
//import com.yibo.yiboapp.entify.AccountRecord;
//import com.yibo.yiboapp.entify.AccountRecordWraper;
//import com.yibo.yiboapp.entify.BcLotteryOrder;
//import com.yibo.yiboapp.entify.CancelOrderWraper;
//import com.yibo.yiboapp.entify.LotteryRecordWraper;
//import com.yibo.yiboapp.entify.RealBetBean;
//import com.yibo.yiboapp.entify.RealBetResultWraper;
//import com.yibo.yiboapp.entify.SBSportOrder;
//import com.yibo.yiboapp.entify.SBSportOrderWraper;
//import com.yibo.yiboapp.entify.SportOrder;
//import com.yibo.yiboapp.entify.SportOrderWraper;
//import com.yibo.yiboapp.entify.SysConfig;
//import com.yibo.yiboapp.ui.EmptyListView;
//import com.yibo.yiboapp.ui.LAdapter;
//import com.yibo.yiboapp.ui.LViewHolder;
//import com.yibo.yiboapp.ui.SportTableContainer;
//import com.yibo.yiboapp.ui.XListView;
//import com.yibo.yiboapp.utils.Utils;
//import com.yyydjk.library.DropDownMenu;
//
//import java.lang.ref.WeakReference;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import crazy_wrapper.Crazy.CrazyResponse;
//import crazy_wrapper.Crazy.CrazyResult;
//import crazy_wrapper.Crazy.GsonConverterFactory;
//import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
//import crazy_wrapper.Crazy.request.CrazyRequest;
//import crazy_wrapper.Crazy.response.SessionResponse;
//import crazy_wrapper.RequestManager;
//
///**
// * 我的转盘中奖记录
// * @author johnson
// *
// */
//public class MyBigPanAwardRecordActivity extends BaseActivity implements
//        SessionResponse.Listener
//        <CrazyResult<Object>>{
//
//    XListView recordList;
//    EmptyListView empty;
//    RecordAdapter recordAdapter;
//    List<AccountRecord> accountRecords;
//    long totalCountFromWeb;
//    int pageIndex = 1;
//    int pageSize = 20;
//    public static final int ACCOUNT_RECORD = 0x03;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_bigpan_record);
//        initView();
//        recordList = (XListView) findViewById(R.id.xlistview);
//        recordList.setPullLoadEnable(true);
//        recordList.setPullRefreshEnable(true);
//        recordList.setDivider(getResources().getDrawable(R.color.driver_line_color));
//        recordList.setDividerHeight(3);
//        recordList.setXListViewListener(new ListviewListener());
//        empty = (EmptyListView) findViewById(R.id.empty);
//        empty.setListener(emptyListviewListener);
//
//        accountRecords = new ArrayList<AccountRecord>();
//        recordAdapter = new AccountRecordAdapter(this, accountRecords, R.layout.account_record_item);
//        recordList.setAdapter(accountRecordAdapter);
//
//        actionRecords(true);
//
//        SysConfig config = UsualMethod.getConfigFromJson(this);
//        if (config != null) {
//            if (!Utils.isEmptyString(config.getLottery_order_cancle_switch()) &&
//                    config.getLottery_order_cancle_switch().equalsIgnoreCase("on")) {
//                cpcdOpen = true;
//            }
//        }
//
//    }
//
//    /**
//     * 准备记录过滤菜单项数据
//     */
//    private final class FigureTabMenuContent implements Runnable{
//
//        int recordType;
//        WeakReference<Context> weakReference;
//        FigureTabMenuContent(Context context,int recordType) {
//            this.recordType = recordType;
//            weakReference = new WeakReference<Context>(context);
//        }
//        @Override
//        public void run() {
//            LinkedHashMap<String, Object> tabMap = new LinkedHashMap<String, Object>();
//            //根据投注记录类型来计算各种游戏下的投注记录菜单
//            if (recordType == Constant.CAIPIAO_RECORD_STATUS) {
//                tabMap.put("状态", getResources().getStringArray(R.array.caipiao_touzhu_categories));
//                tabMap.put("时间", getResources().getStringArray(R.array.touzhu_date));
////            if (!Utils.isEmptyString(cpBianma)) {
//                //获取存储在本地preference中的彩种信息
//                if (weakReference.get() != null) {
//                    Type listType = new TypeToken<ArrayList<LotteryData>>() {}.getType();
//                    List<LotteryData> lotteryDatas = new Gson().fromJson(
//                            YiboPreference.instance(weakReference.get()).getLotterys(),listType);
//                    if (lotteryDatas != null) {
//                        List<String> cpDatas = new ArrayList<>();
//                        cpDatas.add("全部,all");
//                        if (lotteryDatas != null && !lotteryDatas.isEmpty()) {
//                            for (int i=0;i<lotteryDatas.size();i++) {
//                                if (lotteryDatas.get(i).getModuleCode() != LotteryData.CAIPIAO_MODULE) {
//                                    continue;
//                                }
//                                LotteryData lotteryData = lotteryDatas.get(i);
//                                cpDatas.add(lotteryData.getName()+","+lotteryData.getCode());
//                            }
//                        }
//                        String[] datas = new String[cpDatas.size()];
//                        for (int i=0;i<cpDatas.size();i++) {
//                            datas[i] = cpDatas.get(i);
//                        }
//                        tabMap.put("彩种", datas);
//                    }
//                }
////            }
//            } else if (recordType == Constant.LHC_RECORD_STATUS) {
////                tabMap.put("状态", getResources().getStringArray(R.array.lhc_touzhu_categories));
//                tabMap.put("时间", getResources().getStringArray(R.array.touzhu_date));
//            } else if (recordType == Constant.SPORTS_RECORD_STATUS || recordType == Constant.SBSPORTS_RECORD_STATUS) {
//                tabMap.put("时间", getResources().getStringArray(R.array.sport_record_date));
//                tabMap.put("球种", getResources().getStringArray(R.array.qiuzhong_categories));
//            } else if (recordType == Constant.REAL_PERSON_RECORD_STATUS) {
//                tabMap.put("时间", getResources().getStringArray(R.array.real_game_touzhu_date));
//                tabMap.put("平台", getResources().getStringArray(R.array.realren_platform_categories));
//            } else if (recordType == Constant.ELECTRIC_GAME_RECORD_STATUS) {
//                tabMap.put("时间", getResources().getStringArray(R.array.real_game_touzhu_date));
//                tabMap.put("平台", getResources().getStringArray(R.array.game_platform_categories));
//            }
//            else if (recordType == Constant.ACCOUNT_CHANGE_RECORD_STATUS) {
//                tabMap.put("时间", getResources().getStringArray(R.array.touzhu_date));
//            }
//            Message msg = menuHandler.obtainMessage(UPDATE_MENU_TABS, tabMap);
//            menuHandler.sendMessage(msg);
//        }
//    }
//
//    //线程异步handler
//    private static class MenuHandler extends Handler {
//        private WeakReference<RecordsActivity> mReference;
//
//        public MenuHandler(RecordsActivity context) {
//            mReference = new WeakReference<>(context);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (mReference.get() == null) {
//                return;
//            }
//            switch (msg.what) {
//                case UPDATE_MENU_TABS:
//                    LinkedHashMap<String,Object> menuDatas = (LinkedHashMap<String, Object>) msg.obj;
//                    if (menuDatas != null && !menuDatas.isEmpty()) {
//                        mReference.get().initPlayDropMenu(menuDatas);
//                    }
//                    break;
//            }
//        }
//    }
//
//    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
//        @Override public void onEmptyListviewClick() {
//            pageIndex = 1;
//            getRecords(true);
//        }
//    };
//
//    /**
//     * 列表下拉，上拉监听器
//     *
//     * @author zhangy
//     */
//    private final class ListviewListener implements XListView.IXListViewListener {
//
//        ListviewListener() {
//        }
//        public void onRefresh() {
//            pageIndex = 1;
//            getRecords(false);
//        }
//
//        public void onLoadMore() {
//            actionRecords(false);
//        }
//    }
//
//    /**
//     * 获取帐变记录
//     * @param page 页码
//     * @param pageSize 每页条数
//     */
//    private void getRecords(int page,int pageSize,boolean showDialog) {
//
//        StringBuilder configUrl = new StringBuilder();
//        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.MY_BIGPAN_RECORD_URL);
//        configUrl.append("?startTime=").append("00:00:00").append("&");
//        configUrl.append("endTime=").append("23:59:59").append("&");
//        configUrl.append("page=").append(page).append("&");
//        configUrl.append("rows=").append(pageSize);
//
//        CrazyRequest<CrazyResult<AccountRecordWraper>> request = new AbstractCrazyRequest.Builder().
//                url(configUrl.toString())
//                .seqnumber(ACCOUNT_RECORD)
//                .headers(Urls.getHeader(this))
//                .cachePeroid(5*60*1000)
//                .refreshAfterCacheHit(false)
//                .shouldCache(false).placeholderText(getString(R.string.get_recording))
//                .priority(CrazyRequest.Priority.HIGH.ordinal())
//                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<AccountRecordWraper>(){}.getType()))
//                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
//                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(this,request);
//    }
//
//    //给所有查看投注记录动作时使用
//    public static void createIntent(Context context,String name,int status,String cpBianma) {
//        Intent intent = new Intent(context, RecordsActivity.class);
//        intent.putExtra("cp_name", name);
//        intent.putExtra("recordType", status);
//        intent.putExtra("cpBianma", cpBianma);
//        context.startActivity(intent);
//    }
//
//    //针对体育投注页面的跳转接口
//    public static void createIntent(Context context,String name,int status,int ballType,boolean fromSportBetPage) {
//        Intent intent = new Intent(context, RecordsActivity.class);
//        intent.putExtra("cp_name", name);
//        intent.putExtra("recordType", status);
//        intent.putExtra("ballType", ballType);
//        intent.putExtra("fromSportBetPage", fromSportBetPage);
//        context.startActivity(intent);
//    }
//
//
//
//    @Override
//    protected void initView() {
//        super.initView();
//        String name = getIntent().getStringExtra("cp_name");
//        if (!Utils.isEmptyString(name)) {
//            tvMiddleTitle.setText(name);
//        }else{
//            tvMiddleTitle.setText(getString(R.string.touzhu_record));
//        }
//    }
//
//    @Override
//    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
//        RequestManager.getInstance().afterRequest(response);
//        stopProgress();
//        if (isFinishing() || response == null) {
//            return;
//        }
//        int action = response.action;
//        if (action == ACCOUNT_RECORD) {
//            if (recordList.isRefreshing()) {
//                recordList.stopRefresh();
//            } else if (recordList.isPullLoading()) {
//                recordList.stopLoadMore();
//            }
//            empty.setVisibility(View.VISIBLE);
//            recordList.setEmptyView(empty);
//            CrazyResult<Object> result = response.result;
//            if (result == null) {
//                showToast(R.string.get_record_fail);
//                return;
//            }
//            if (!result.crazySuccess) {
//                showToast(R.string.get_record_fail);
//                return;
//            }
//            Object regResult =  result.result;
//            AccountRecordWraper reg = (AccountRecordWraper) regResult;
//            if (!reg.isSuccess()) {
//                showToast(!Utils.isEmptyString(reg.getMsg())?reg.getMsg():
//                        getString(R.string.get_record_fail));
//                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
//                //所以此接口当code == 0时表示帐号被踢，或登录超时
//                if (reg.getCode() == 0) {
//                    UsualMethod.loginWhenSessionInvalid(this);
//                }
//                return;
//            }
//            YiboPreference.instance(this).setToken(reg.getAccessToken());
//            if (pageIndex == 1) {
//                accountRecords.clear();
//            }
//            totalCountFromWeb = reg.getContent().getTotalCount();
//            Utils.LOG(TAG,"pick type when get account record = "+response.pickType);
//            if (response.pickType != CrazyResponse.CACHE_REQUEST){
//                pageIndex++;
//            }
//            if (!reg.getContent().getResults().isEmpty()) {
//                accountRecords.addAll(reg.getContent().getResults());
//            }
//            if (totalCountFromWeb <= accountRecords.size()) {
//                recordList.setPullLoadEnable(false);
//            }else{
//                recordList.setPullLoadEnable(true);
//            }
//            accountRecordAdapter.notifyDataSetChanged();
//        }
//    }
//
//    public class RecordAdapter extends LAdapter<AccountRecord> {
//
//        Context context;
//        DecimalFormat decimalFormat;
//        public AccountRecordAdapter(Context mContext, List<AccountRecord> mDatas, int layoutId) {
//            super(mContext, mDatas, layoutId);
//            context = mContext;
//            decimalFormat =new DecimalFormat("0.00");
//        }
//
//        @Override public void convert(int position, LViewHolder holder, ViewGroup parent, final AccountRecord item) {
//
//            TextView orderName = holder.getView(R.id.orderno);
//            TextView moneyBefore = holder.getView(R.id.money_before);
//            TextView changeMoney = holder.getView(R.id.change_money);
//            TextView moneyAfter = holder.getView(R.id.after_money);
//            TextView time = holder.getView(R.id.time);
//            LinearLayout linearLayout = holder.getView(R.id.item);
//            linearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String changeJson = new Gson().toJson(item, AccountRecord.class);
//                    AccountChangeDetailActivity.createIntent(context,changeJson);
//                }
//            });
//
//            orderName.setText(item.getOrderno());
//            moneyBefore.setText(String.format(getString(R.string.change_before_money_format),
//                    decimalFormat.format(item.getMoneyBefore())));
//            changeMoney.setText(String.format(getString(R.string.biandong_money_format),
//                    decimalFormat.format(item.getMoneyAfter() - item.getMoneyBefore())));
//            moneyAfter.setText(String.format(getString(R.string.change_after_money_format),
//                    decimalFormat.format(item.getMoneyAfter())));
//            time.setText(item.getTimeStr());
//
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (accountRecords != null) {
//            accountRecords.clear();
//        }
//    }
//}
//
