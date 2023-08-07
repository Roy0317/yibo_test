package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.TouzhuThreadPool;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.AccountRecord;
import com.yibo.yiboapp.entify.AccountRecordWraper;
import com.yibo.yiboapp.entify.BcLotteryOrder;
import com.yibo.yiboapp.entify.CancelOrderWraper;
import com.yibo.yiboapp.entify.LotteryRecordWraper;
import com.yibo.yiboapp.entify.NewSportOrderBean;
import com.yibo.yiboapp.entify.NewSportOrderWrapper;
import com.yibo.yiboapp.entify.RealRecordResponse;
import com.yibo.yiboapp.entify.SBSportOrder;
import com.yibo.yiboapp.entify.SBSportOrderWraper;
import com.yibo.yiboapp.entify.SportOrder;
import com.yibo.yiboapp.entify.SportOrderWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.SportTableContainer;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;
import com.yyydjk.library.DropDownMenu;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * @author johnson
 */
public class RecordsActivity extends BaseActivity implements SessionResponse.Listener
        <CrazyResult<Object>> {

    XListView recordList;
    EmptyListView empty;
    RecordAdapter recordAdapter;
    List<BcLotteryOrder> listDatas;
    List<AccountRecord> accountRecords;
    List<SportOrder> sportOrders;
    List<NewSportOrderBean> newsportOrders;
    List<SBSportOrder> sbsportOrders;
    List<RealRecordResponse.RealRecordDetail> realGameResults;
    long totalCountFromWeb;
    int pageIndex = 1;
    int pageSize = 40;
    DropDownMenu dropDownMenu;
    View contentView;
    MenuHandler menuHandler;

    public static final int TOUZHU_RECORD = 0x01;
    public static final int CANCEL_ORDER = 0x02;
    public static final int ACCOUNT_RECORD = 0x03;
    public static final int SPORT_RECORD = 0x04;
    public static final int REAL_RECORD = 0x05;
    public static final int GAME_RECORD = 0x06;
    public static final int SBSPORTGAME_RECORD = 0x07;
    public static final int MYBIGPAN_RECORD = 0x08;
    public static final int NEW_SPORT_RECORD = 0x09;


    String statusCategory = "all";
    String dateTime = "today";
    String cpBianma = "";//查询的彩票编码

    //体育记录过滤变量
    String dateTimeValue = "1";//时间
    int ballCategory;//球种 0-全部 1-足球 2-篮球
    public static final int UPDATE_MENU_TABS = 0x01;
    AccountRecordAdapter accountRecordAdapter;
    SportRecordAdapter sportRecordAdapter;
    NewSportRecordAdapter newsportRecordAdapter;
    SBSportRecordAdapter sbsportRecordAdapter;
    RealGameRecordAdapter realGameRecordAdapter;
    int recordType;

    //真人投注过滤条件
    String realDate = "";
    String realPlatform = "";

    //电子投注过滤条件
    String gameDate = "";
    String gamePlatform = "";

    boolean cpcdOpen = false;
    TextView sumBetMoneyUI;
    TextView sumWinMoneyUI;
    TextView sumAwardMoneyUI; //中奖总额
    LinearLayout llSumRealBetMoney;
    TextView sumRealBetMoneyUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caipiao_record);
        initView();

        dropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);
        contentView = LayoutInflater.from(this).inflate(R.layout.touzhu_record_content, null);
        recordList = (XListView) contentView.findViewById(R.id.xlistview);

        recordType = getIntent().getIntExtra("recordType", Constant.CAIPIAO_RECORD_STATUS);
        if (recordType == Constant.REAL_PERSON_RECORD_STATUS || recordType == Constant.ELECTRIC_GAME_RECORD_STATUS ||
                recordType == Constant.CAIPIAO_RECORD_STATUS || recordType == Constant.LHC_RECORD_STATUS) {
            SysConfig config = UsualMethod.getConfigFromJson(this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.touzhu_attach_header, null);
            sumBetMoneyUI = (TextView) view.findViewById(R.id.total_betmoney);
            sumWinMoneyUI = (TextView) view.findViewById(R.id.total_winmoney);
            sumAwardMoneyUI = view.findViewById(R.id.total_zhongjiang);
            llSumRealBetMoney= (LinearLayout) view.findViewById(R.id.ll_value_bet);
            sumRealBetMoneyUI = view.findViewById(R.id.value_betmoney);
            if (config.getMobile_v3_bet_order_detail_total().equalsIgnoreCase("on")) {
                recordList.addHeaderView(view);
            }
        }

        recordList.setPullLoadEnable(false);
        recordList.setPullRefreshEnable(true);
        recordList.setDivider(getResources().getDrawable(R.color.driver_line_color));
        recordList.setDividerHeight(3);
        recordList.setXListViewListener(new ListviewListener());
        empty = (EmptyListView) contentView.findViewById(R.id.empty_list);
        empty.setListener(emptyListviewListener);


        cpBianma = getIntent().getStringExtra("cpBianma");
        int type = getIntent().getIntExtra("ballType", SportTableContainer.FOOTBALL_PAGE);
        boolean fromSportBetPage = getIntent().getBooleanExtra("fromSportBetPage", false);
        if (fromSportBetPage) {
            ballCategory = type == 0 ? 1 : 2;
        }
        menuHandler = new MenuHandler(this);

        //计算出投注记录头部的菜单项及菜单列表内容
        figureMenuTabsByCpBianmaOrRecordType();

        //请求记录
        if (recordType == Constant.ACCOUNT_CHANGE_RECORD_STATUS) {
            accountRecords = new ArrayList<AccountRecord>();
            accountRecordAdapter = new AccountRecordAdapter(this, accountRecords, R.layout.account_record_item);
            recordList.setAdapter(accountRecordAdapter);
        } else if (recordType == Constant.OLD_SPORTS_RECORD_STATUS) {
            sportOrders = new ArrayList<>();
            sportRecordAdapter = new SportRecordAdapter(this, sportOrders, R.layout.sport_record_item);
            recordList.setAdapter(sportRecordAdapter);
        } else if (recordType == Constant.SPORTS_RECORD_STATUS) {
            newsportOrders = new ArrayList<>();
            newsportRecordAdapter = new NewSportRecordAdapter(this, newsportOrders, R.layout.new_sport_item);
            recordList.setAdapter(newsportRecordAdapter);
        } else if (recordType == Constant.SBSPORTS_RECORD_STATUS) {
            sbsportOrders = new ArrayList<>();
            sbsportRecordAdapter = new SBSportRecordAdapter(this, sbsportOrders, R.layout.sbsport_list_item);
            recordList.setAdapter(sbsportRecordAdapter);
        } else if (recordType == Constant.REAL_PERSON_RECORD_STATUS || recordType == Constant.ELECTRIC_GAME_RECORD_STATUS) {
            realGameResults = new ArrayList<>();
            realGameRecordAdapter = new RealGameRecordAdapter(this, realGameResults, R.layout.real_game_item);
            recordList.setAdapter(realGameRecordAdapter);
        } else if (recordType == Constant.MYBIGPAN_RECORD_STATUS) {
            sportOrders = new ArrayList<>();
            sportRecordAdapter = new SportRecordAdapter(this, sportOrders, R.layout.sport_record_item);
            recordList.setAdapter(sportRecordAdapter);
        } else {
            listDatas = new ArrayList<>();
            recordAdapter = new RecordAdapter(this, listDatas, R.layout.touzhu_record_item);
            recordList.setAdapter(recordAdapter);
        }
        actionRecords(true);

        SysConfig config = UsualMethod.getConfigFromJson(this);
        if (config != null) {
            if (!Utils.isEmptyString(config.getLottery_order_cancle_switch()) &&
                    config.getLottery_order_cancle_switch().equalsIgnoreCase("on")) {
                cpcdOpen = true;
            }
        }

    }

    /**
     * 准备记录过滤菜单项数据
     */
    private final class FigureTabMenuContent implements Runnable {

        int recordType;
        WeakReference<Context> weakReference;

        FigureTabMenuContent(Context context, int recordType) {
            this.recordType = recordType;
            weakReference = new WeakReference<>(context);
        }

        @Override
        public void run() {
            LinkedHashMap<String, Object> tabMap = new LinkedHashMap<>();
            //根据投注记录类型来计算各种游戏下的投注记录菜单
            if (recordType == Constant.CAIPIAO_RECORD_STATUS) {
                tabMap.put("状态", getResources().getStringArray(R.array.caipiao_touzhu_categories));
                tabMap.put("时间", getResources().getStringArray(R.array.touzhu_date));
//            if (!Utils.isEmptyString(cpBianma)) {
                //获取存储在本地preference中的彩种信息
                if (weakReference.get() != null) {
                    Type listType = new TypeToken<ArrayList<LotteryData>>() {
                    }.getType();
                    List<LotteryData> lotteryDatas = new Gson().fromJson(
                            YiboPreference.instance(weakReference.get()).getLotterys(), listType);
                    if (lotteryDatas != null) {
                        List<String> cpDatas = new ArrayList<>();
                        cpDatas.add("全部,all");
                        if (lotteryDatas != null && !lotteryDatas.isEmpty()) {
                            for (int i = 0; i < lotteryDatas.size(); i++) {
                                if (lotteryDatas.get(i).getModuleCode() != LotteryData.CAIPIAO_MODULE) {
                                    continue;
                                }
                                LotteryData lotteryData = lotteryDatas.get(i);
                                cpDatas.add(lotteryData.getName() + "," + lotteryData.getCode());
                            }
                        }
                        String[] datas = new String[cpDatas.size()];
                        for (int i = 0; i < cpDatas.size(); i++) {
                            datas[i] = cpDatas.get(i);
                        }
                        tabMap.put("彩种", datas);
                    }
                }
//            }
            } else if (recordType == Constant.LHC_RECORD_STATUS) {
//                tabMap.put("状态", getResources().getStringArray(R.array.lhc_touzhu_categories));
                tabMap.put("时间", getResources().getStringArray(R.array.touzhu_date));
            } else if (recordType == Constant.SPORTS_RECORD_STATUS || recordType == Constant.SBSPORTS_RECORD_STATUS ||
                    recordType == Constant.OLD_SPORTS_RECORD_STATUS) {
                tabMap.put("时间", getResources().getStringArray(R.array.sport_record_date));
                tabMap.put("球种", getResources().getStringArray(R.array.qiuzhong_categories));
            } else if (recordType == Constant.REAL_PERSON_RECORD_STATUS) {
                tabMap.put("时间", getResources().getStringArray(R.array.real_game_touzhu_date));
                tabMap.put("平台", getResources().getStringArray(R.array.realren_platform_categories));
            } else if (recordType == Constant.ELECTRIC_GAME_RECORD_STATUS) {
                tabMap.put("时间", getResources().getStringArray(R.array.real_game_touzhu_date));
                tabMap.put("平台", getResources().getStringArray(R.array.game_platform_categories));
            } else if (recordType == Constant.ACCOUNT_CHANGE_RECORD_STATUS) {
                tabMap.put("时间", getResources().getStringArray(R.array.touzhu_date));
            }
            Message msg = menuHandler.obtainMessage(UPDATE_MENU_TABS, tabMap);
            menuHandler.sendMessage(msg);
        }
    }

    //线程异步handler
    private static class MenuHandler extends Handler {
        private WeakReference<RecordsActivity> mReference;

        public MenuHandler(RecordsActivity context) {
            mReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mReference.get() == null) {
                return;
            }
            switch (msg.what) {
                case UPDATE_MENU_TABS:
                    LinkedHashMap<String, Object> menuDatas = (LinkedHashMap<String, Object>) msg.obj;
                    if (menuDatas != null && !menuDatas.isEmpty()) {
                        mReference.get().initPlayDropMenu(menuDatas);
                    }
                    break;
            }
        }
    }


    //根据彩票编码显示头部过滤tab栏
    private void figureMenuTabsByCpBianmaOrRecordType() {
        TouzhuThreadPool.getInstance().addTask(new FigureTabMenuContent(this, recordType));
    }

    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
        @Override
        public void onEmptyListviewClick() {
            pageIndex = 1;
            actionRecords(true);
        }
    };

    /**
     * 列表下拉，上拉监听器
     *
     * @author zhangy
     */
    private final class ListviewListener implements XListView.IXListViewListener {

        ListviewListener() {
        }

        public void onRefresh() {
            pageIndex = 1;
            actionRecords(false);
        }

        public void onLoadMore() {
            actionRecords(false);
        }
    }

    private void actionRecords(boolean showDialog) {
        if (recordType == Constant.ACCOUNT_CHANGE_RECORD_STATUS) {
            getAccountRecords(dateTime, pageIndex, pageSize, showDialog);
        } else if (recordType == Constant.SPORTS_RECORD_STATUS) {
            getSportRecords(dateTimeValue, ballCategory, Constant.RECORD_TYPE_ALL, showDialog, false);
        } else if (recordType == Constant.OLD_SPORTS_RECORD_STATUS) {
            getSportRecords(dateTimeValue, ballCategory, Constant.RECORD_TYPE_ALL, showDialog, true);
        } else if (recordType == Constant.SBSPORTS_RECORD_STATUS) {
//            String time = Utils.formatTime(System.currentTimeMillis(),"yyyy-MM-dd");
            getsbSportRecords(dateTimeValue, pageIndex, pageSize, ballCategory, "", true);
        } else if (recordType == Constant.REAL_PERSON_RECORD_STATUS) {
            getRealPersonRecords(realDate, realPlatform, pageIndex, pageSize, showDialog);
        } else if (recordType == Constant.ELECTRIC_GAME_RECORD_STATUS) {
            getGameRecords(gameDate, gamePlatform, showDialog);
        } else if (recordType == Constant.CHESS_GAME_RECORD_STATUS) {
//            getChessRecords(gameDate, gamePlatform, showDialog);
        } else {
            getRecords(statusCategory, dateTime, cpBianma, pageIndex, pageSize, showDialog);
        }
        if (!showDialog) {
            startProgress();
        }
    }

    /**
     * 获取帐变记录
     *
     * @param dateTime 时间类型
     * @param page     页码
     * @param pageSize 每页条数
     */
    private void getAccountRecords(String dateTime, int page, int pageSize, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ACCOUNT_CHANGE_RECORD_URL);
        configUrl.append("?qtime=").append(dateTime).append("&");
        configUrl.append("pageNumber=").append(page).append("&");
        configUrl.append("pageSize=").append(pageSize);

        CrazyRequest<CrazyResult<AccountRecordWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(ACCOUNT_RECORD)
                .headers(Urls.getHeader(this))
                .cachePeroid(5 * 60 * 1000)
                .refreshAfterCacheHit(true)
                .shouldCache(true).placeholderText(getString(R.string.get_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<AccountRecordWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    /**
     * 获取体育投注记录
     *
     * @param dateTime   时间类型 1-今天 2-昨天 3-一周 4-近30天
     * @param sportType  球类 0-全部 1-足球 2-篮球
     * @param recordType 记录类型 1-全部 2-会员赢钱 3-未开奖 4-未成功
     * @param isOldSport 是否旧体育记录
     */
    private void getSportRecords(String dateTime, int sportType, int recordType, boolean showDialog,
                                 boolean isOldSport) {

        StringBuilder configUrl = new StringBuilder();
        if (isOldSport) {
            configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.OLD_SPORT_RECORDS);
            configUrl.append("?sportType=").append(sportType).append("&");
            configUrl.append("date=").append(dateTime).append("&");
            configUrl.append("pageNumber=").append(pageIndex).append("&");
            configUrl.append("pageSize=").append(pageSize).append("&");
            configUrl.append("recordType=").append(recordType);


            CrazyRequest<CrazyResult<SportOrderWraper>> request = new AbstractCrazyRequest.Builder().
                    url(configUrl.toString())
                    .seqnumber(SPORT_RECORD)
                    .headers(Urls.getHeader(this))
                    .cachePeroid(60 * 1000)
//                .refreshAfterCacheHit(true)
                    .shouldCache(false).placeholderText(getString(R.string.get_recording))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<SportOrderWraper>() {
                    }.getType()))
                    .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                            CrazyRequest.LOAD_METHOD.NONE.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(this, request);
        } else {
            configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SPORT_RECORDS);
            configUrl.append("?sportType=").append(sportType).append("&");
            configUrl.append("date=").append(dateTime).append("&");
            configUrl.append("pageNumber=").append(pageIndex).append("&");
            configUrl.append("pageSize=").append(pageSize).append("&");
            configUrl.append("recordType=").append(recordType);

            CrazyRequest<CrazyResult<NewSportOrderWrapper>> request = new AbstractCrazyRequest.Builder().
                    url(configUrl.toString())
                    .seqnumber(NEW_SPORT_RECORD)
                    .headers(Urls.getHeader(this))
                    .cachePeroid(60 * 1000)
//                .refreshAfterCacheHit(true)
                    .shouldCache(false).placeholderText(getString(R.string.get_recording))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<NewSportOrderWrapper>() {
                    }.getType()))
                    .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                            CrazyRequest.LOAD_METHOD.NONE.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(this, request);
        }

    }

    private void getsbSportRecords(String dateTimeValue, int pageNumber, int pageSize,
                                   int type, String orderno, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SBSPORT_RECORDS);
//        configUrl.append("?startTime=").append(startTime).append("&");
//        configUrl.append("endTime=").append(endTime).append("&");
        configUrl.append("?dateType=").append(dateTimeValue).append("&");
        configUrl.append("transId=").append(orderno).append("&");
        configUrl.append("type=").append(type).append("&");
        configUrl.append("pageNumber=").append(pageNumber).append("&");
        configUrl.append("pageSize=").append(pageSize);

        CrazyRequest<CrazyResult<SBSportOrderWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(SBSPORTGAME_RECORD)
                .headers(Urls.getHeader(this))
                .cachePeroid(60 * 1000)
                .refreshAfterCacheHit(true)
                .shouldCache(true).placeholderText(getString(R.string.get_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<SBSportOrderWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    /**
     * 获取真人投注记录
     *
     * @param dateTime     时间类型 today-今天 yesterday-昨天 week-一周 month-近30天
     * @param platformType 平台类型 ag,og,bbin,....
     */
    private void getRealPersonRecords(String dateTime, String platformType, int pageIndex, int pageSize, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.REAL_BET_RECORD_URL);
//        configUrl.append("?pageNumber=").append(pageIndex).append("&");
//        configUrl.append("pageSize=").append(pageSize).append("&");
        configUrl.append("?dateType=").append(dateTime).append("&");
        int type = UsualMethod.convertRealMainPlatformValue(platformType);
        configUrl.append("liveType=").append(type == 0 ? "" : type);


        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(REAL_RECORD)
                .headers(Urls.getHeader(this))
                .cachePeroid(60 * 1000)
                .refreshAfterCacheHit(true)
                .shouldCache(true).placeholderText(getString(R.string.get_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(null)
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    /**
     * 获取電子投注记录
     *
     * @param dateTime     时间类型 1-今天 2-昨天 3-一周 4-近30天
     * @param gamePlatform 记录类型 1-全部 2-会员赢钱 3-未开奖 4-未成功
     */
    private void getGameRecords(String dateTime, String gamePlatform, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GAME_BET_RECORD_URL);
        configUrl.append("?dateType=").append(dateTime).append("&");
        int gameType = UsualMethod.convertEgameValue(gamePlatform);
        configUrl.append("egameType=").append(gameType == 0 ? "" : gameType);

        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(GAME_RECORD)
                .headers(Urls.getHeader(this))
                .cachePeroid(60 * 1000)
                .refreshAfterCacheHit(true)
                .shouldCache(true).placeholderText(getString(R.string.get_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(null)
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    /**
     * 获取投注记录
     *
     * @param queryType 查询类型
     * @param dateTime  时间类型
     * @param lotcode   彩种编码
     * @param page      页码
     * @param pageSize  每页条数
     */
    private void getRecords(String queryType, String dateTime, String lotcode, int page, int pageSize, boolean showDialog) {
        String newLotCode = lotcode;
//        if (recordType == Constant.LHC_RECORD_STATUS) {
//            try {
//                newLotCode = URLEncoder.encode(lotcode + ",SFLHC", "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_RECORD_URL);
        configUrl.append("?queryType=").append(queryType).append("&");
        configUrl.append("dateTime=").append(dateTime).append("&");
        configUrl.append("lotCode=").append(newLotCode).append("&");
        configUrl.append("page=").append(page).append("&");
        configUrl.append("rows=").append(pageSize);

        CrazyRequest<CrazyResult<LotteryRecordWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(TOUZHU_RECORD)
                .headers(Urls.getHeader(this))
                .cachePeroid(5 * 60 * 1000)
                .refreshAfterCacheHit(true)
                .shouldCache(true).placeholderText(getString(R.string.get_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LotteryRecordWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    private void initPlayDropMenu(Map<String, Object> tabMap) {
        List<View> popView = new ArrayList<View>();
        List<String> keyList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : tabMap.entrySet()) {
            ListView listview = new ListView(this);
            listview.setDividerHeight(1);
            String[] contents = (String[]) entry.getValue();
            MenuListAdapter adapter = new MenuListAdapter(this, Arrays.asList(contents),
                    R.layout.item_default_drop_down);
            adapter.setCheckItem(0);
            //若是彩种菜单项，初始下拉菜单选中的彩种为 查看投注记录的彩票编码
            if (entry.getKey().equals("彩种") && !Utils.isEmptyString(cpBianma)) {
                String[] values = ((String[]) entry.getValue());
                for (int i = 0; i < values.length; i++) {
                    String strs = values[i];
                    if (strs.contains(cpBianma)) {
                        adapter.setCheckItem(i);
                        break;
                    }
                }
            }

            if (entry.getKey().equals("球种")) {
                String[] values = ((String[]) entry.getValue());
                for (int i = 0; i < values.length; i++) {
                    if (i == ballCategory) {
                        adapter.setCheckItem(i);
                        break;
                    }
                }
            }
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new MenuListItemListener(entry.getKey(), contents, adapter));
            popView.add(listview);
            keyList.add(entry.getKey());
        }
        dropDownMenu.setDropDownMenu(keyList, popView, contentView);
    }

    //给所有查看投注记录动作时使用
    public static void createIntent(Context context, String name, int status, String cpBianma) {
        Intent intent = new Intent(context, RecordsActivity.class);
        intent.putExtra("cp_name", name);
        intent.putExtra("recordType", status);
        intent.putExtra("cpBianma", cpBianma);
        context.startActivity(intent);
    }

    //针对体育投注页面的跳转接口
    public static void createIntent(Context context, String name, int status, int ballType, boolean fromSportBetPage) {
        Intent intent = new Intent(context, RecordsActivity.class);
        intent.putExtra("cp_name", name);
        intent.putExtra("recordType", status);
        intent.putExtra("ballType", ballType);
        intent.putExtra("fromSportBetPage", fromSportBetPage);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        super.initView();
        String name = getIntent().getStringExtra("cp_name");
        if (!Utils.isEmptyString(name)) {
            tvMiddleTitle.setText(name);
        } else {
            tvMiddleTitle.setText(getString(R.string.touzhu_record));
        }
    }

    private void updateSumMoney(double betMoney, double winMoney) {
        sumBetMoneyUI.setText(String.format("%.2f元", betMoney));
        sumWinMoneyUI.setText(String.format("%.2f元", winMoney - betMoney));
        sumAwardMoneyUI.setText(String.format("%.2f元", winMoney));
    }
    private void updateRealBettingMoneyCount(double moneyCount){
        sumRealBetMoneyUI.setText(String.format("%.2f元", moneyCount));
    }


    //过滤菜单列表点击事件
    private class MenuListItemListener implements AdapterView.OnItemClickListener {

        String tabString;
        String[] listData;
        MenuListAdapter adapter;

        MenuListItemListener(String tabString, String[] listData, MenuListAdapter adapter) {
            this.tabString = tabString;
            this.listData = listData;
            this.adapter = adapter;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //更新tab过滤变量
            String item = listData[position];
            String itemValue = "";
            if (!Utils.isEmptyString(item) && item.contains(",")) {
                String[] split = item.split(",");
                if (split.length != 2) {
                    return;
                }
                item = split[0];
                itemValue = split[1];
            }
            pageIndex = 1;//筛选条件选择点重置当前页码
            adapter.setCheckItem(position);
            dropDownMenu.setTabText(item);
            dropDownMenu.closeMenu();

            if (tabString.equals("状态")) {
                statusCategory = itemValue;
            } else if (tabString.equals("时间")) {
                dateTime = itemValue;
                dateTimeValue = itemValue;
                realDate = itemValue;
                gameDate = itemValue;
            } else if (tabString.equals("彩种")) {
                cpBianma = !itemValue.equals("all") ? itemValue : "";
                tvMiddleTitle.setText(getString(R.string.touzhu_record) + "(" + item + ")");
            } else if (tabString.equals("球种")) {
                ballCategory = position;
            } else if (tabString.equals("平台")) {
                if (position == 0) {
                    gamePlatform = "";
                    realPlatform = "";
                } else {
                    gamePlatform = item;
                    realPlatform = item;
                }
            }
            actionRecords(false);
        }
    }

    /**
     * 菜单下拉列表数据适配器
     */
    private final class MenuListAdapter extends LAdapter<String> {

        private int checkItemPosition = 0;
        Context context;

        public void setCheckItem(int position) {
            checkItemPosition = position;
            notifyDataSetChanged();
        }

        public MenuListAdapter(Context mContext, List<String> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, String item) {

            TextView txt = holder.getView(R.id.text);
            //更新tab过滤变量
            if (!Utils.isEmptyString(item) && item.contains(",")) {
                String[] split = item.split(",");
                if (split == null || split.length != 2) {
                    return;
                }
                txt.setText(split[0]);
            } else {
                txt.setText(item);
            }
            if (checkItemPosition != -1) {
                if (checkItemPosition == position) {
                    txt.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    txt.setBackgroundResource(R.color.check_bg);
                } else {
                    txt.setTextColor(context.getResources().getColor(R.color.grey));
                    txt.setBackgroundResource(R.color.white);
                }
            }

        }
    }


    public class RecordAdapter extends LAdapter<BcLotteryOrder> {

        Context context;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        public RecordAdapter(Context mContext, List<BcLotteryOrder> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        private String getStatusStr(int status) {
            if (status == Constant.WAIT_KAIJIAN_STATUS) {
                return "未开奖";
            } else if (status == Constant.ALREADY_WIN_STATUS) {
                return "已中奖";
            } else if (status == Constant.NOT_WIN_STATUS) {
                return "未中奖";
            } else if (status == Constant.CANCEL_ORDER_STATUS) {
                return "已撤单";
            } else if (status == Constant.ROLLBACK_SUCCESS_STATUS) {
                return "派奖回滚成功";
            } else if (status == Constant.ROLLBACK_FAIL_STATUS) {
                return "回滚异常";
            } else if (status == Constant.EXCEPTION_KAIJIAN_STATUS) {
                return "开奖异常";
            } else if (status == Constant.ACCOUNT_CHANGE_RECORD_STATUS) {
                return "和局";
            }
            return "";
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final BcLotteryOrder item) {

            TextView cpname = holder.getView(R.id.name);
            TextView tzFee = holder.getView(R.id.tz_fee);
            TextView pjFee = holder.getView(R.id.pj_fee);
            TextView state = holder.getView(R.id.state);
            Button viewBtn = holder.getView(R.id.view);
            Button cancelOrderBtn = holder.getView(R.id.cancel);
            TextView tzTime = holder.getView(R.id.time);
            TextView haoma = holder.getView(R.id.haoma);
            TextView qihao = holder.getView(R.id.qihao);

            RelativeLayout itemLayout = holder.getView(R.id.item);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TouzhuRecordDetailActivity.createIntent(context, item.getOrderId(), item.getLotCode());
                }
            });

            cpname.setText(item.getLotName());
            String touzhuFee = decimalFormat.format(item.getBuyMoney());
            String winFee = decimalFormat.format(item.getWinMoney());
            tzFee.setText(String.format(getString(R.string.touzhu_money_format), touzhuFee));
            pjFee.setText(String.format(getString(R.string.paijian_money_format), winFee));
            state.setText(getStatusStr(item.getStatus()));
            tzTime.setText(Utils.formatBeijingTime(item.getCreateTime()));
            haoma.setText(String.format("投注号码:%s", item.getHaoMa()));
            qihao.setText(String.format("投注期号:%s", !Utils.isEmptyString(item.getQiHao()) ? item.getQiHao() : ""));

            if (item.getStatus() == Constant.WAIT_KAIJIAN_STATUS) {
                state.setTextColor(getResources().getColor(R.color.gray));
            } else if (item.getStatus() == Constant.ALREADY_WIN_STATUS) {
                state.setTextColor(getResources().getColor(R.color.red));
                state.setTypeface(Typeface.DEFAULT_BOLD);
            } else if (item.getStatus() == Constant.NOT_WIN_STATUS) {
                state.setTextColor(getResources().getColor(R.color.gray));
            } else if (item.getStatus() == Constant.CANCEL_ORDER_STATUS) {
                state.setTextColor(getResources().getColor(R.color.gray));
            } else if (item.getStatus() == Constant.ROLLBACK_SUCCESS_STATUS) {
                state.setTextColor(getResources().getColor(R.color.gray));
            } else if (item.getStatus() == Constant.ROLLBACK_FAIL_STATUS) {
                state.setTextColor(getResources().getColor(R.color.red));
            } else if (item.getStatus() == Constant.EXCEPTION_KAIJIAN_STATUS) {
                state.setTextColor(getResources().getColor(R.color.red));
            } else if (item.getStatus() == Constant.ACCOUNT_CHANGE_RECORD_STATUS) {
                state.setTextColor(getResources().getColor(R.color.blue_color));
            }
            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TouzhuRecordDetailActivity.createIntent(context, item.getOrderId(), item.getLotCode());
                }
            });

            if (cpcdOpen) {
                if (item.getStatus() == Constant.WAIT_KAIJIAN_STATUS) {
                    cancelOrderBtn.setVisibility(View.VISIBLE);
                } else {
                    cancelOrderBtn.setVisibility(View.GONE);
                }
            } else {
                cancelOrderBtn.setVisibility(View.GONE);
            }
            cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionCancelOrder(item.getOrderId(), item.getLotCode());
                }
            });

        }
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
            TextView time = holder.getView(R.id.time);
            LinearLayout linearLayout = holder.getView(R.id.item);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String changeJson = new Gson().toJson(item, AccountRecord.class);
                    AccountChangeDetailActivity.createIntent(context, changeJson);
                }
            });

            orderName.setText(item.getOrderno());
            moneyBefore.setText(String.format(getString(R.string.change_before_money_format),
                    decimalFormat.format(item.getMoneyBefore())));
            changeMoney.setText(String.format(getString(R.string.biandong_money_format),
                    decimalFormat.format(item.getMoneyAfter() - item.getMoneyBefore())));
            moneyAfter.setText(String.format(getString(R.string.change_after_money_format),
                    decimalFormat.format(item.getMoneyAfter())));
            time.setText(item.getTimeStr());

        }
    }

    /**
     * 新体育记录adapter
     */
    public class NewSportRecordAdapter extends LAdapter<NewSportOrderBean> {

        Context context;
        DecimalFormat decimalFormat;

        public NewSportRecordAdapter(Context mContext, List<NewSportOrderBean> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            decimalFormat = new DecimalFormat("0.00");
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final NewSportOrderBean item) {

            TextView sportName = holder.getView(R.id.sport_name);
            TextView statusName = holder.getView(R.id.state);
            TextView moneyName = holder.getView(R.id.tz_fee);
            Button viewBtn = holder.getView(R.id.view);
            TextView timeTV = holder.getView(R.id.time);

            TextView teams = holder.getView(R.id.teamstv);
            TextView league = holder.getView(R.id.league);
            TextView panPeilv = holder.getView(R.id.pan_peilv_tv);
            TextView ballCategory = holder.getView(R.id.ball_category);

            league.setText(item.getLeague());
            if (item.getMix() == 2) {
                teams.setVisibility(View.GONE);
            } else {
                teams.setVisibility(View.VISIBLE);
                teams.setText(String.format("%s vs %s", Utils.cleanHtml(item.getHomeTeam()), Utils.cleanHtml(item.getGuestTeam())));
            }
            panPeilv.setText(String.format("%s--赔率(%.2f)", UsualMethod.convertSportPan(item.getPlate()),
                    item.getOdds()));
            ballCategory.setText(String.format("%s(%s)", UsualMethod.convertSportBallon(item.getSportType()),
                    item.getTypeNames()));

            RelativeLayout linearLayout = holder.getView(R.id.item);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String content = new Gson().toJson(item, NewSportOrderBean.class);
                    NewSportOrderDetailActivity.createIntent(context, content);
                }
            });
            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content = new Gson().toJson(item, NewSportOrderBean.class);
                    NewSportOrderDetailActivity.createIntent(context, content);
                }
            });

            sportName.setText(String.format(getString(R.string.ball_type_format),
                    UsualMethod.convertSportBallon(item.getSportType())));
            statusName.setText(UsualMethod.convertResultStatusAccordingBalance(item.getResultStatus(),
                    item.getBalance(), item.getRemark(), item.getWinMoney(), item.getStatusRemark()));

//            if (item.getBalance() == Constant.BALANCE_UNDO) {
//                statusName.setTextColor(getResources().getColor(R.color.grey));
//            } else if (item.getBalance() == Constant.BALANCE_CUT_GAME) {
//                statusName.setTextColor(getResources().getColor(R.color.colorPrimary));
//            }else if(item.getBalance() == Constant.BALANCE_DONE ||
//                    item.getBalance() == Constant.BALANCE_AGENT_HAND_DONE ||
//                    item.getBalance() == Constant.BALANCE_BFW_DONE){
////                if(item.getBettingResult() > 0){
////                    statusName.setTextColor(getResources().getColor(R.color.blue_color));
////                }else{
//                    statusName.setTextColor(getResources().getColor(R.color.colorPrimary));
////                }
//            }
            moneyName.setText(String.format(getString(R.string.touzhu_money_format),
                    decimalFormat.format(item.getBettingMoney())));
            timeTV.setText(Utils.formatTime(item.getCreateDatetime()));

        }

    }


    /**
     * 体育记录adapter
     */
    public class SportRecordAdapter extends LAdapter<SportOrder> {

        Context context;
        DecimalFormat decimalFormat;

        public SportRecordAdapter(Context mContext, List<SportOrder> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            decimalFormat = new DecimalFormat("0.00");
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final SportOrder item) {

            TextView sportName = holder.getView(R.id.sport_name);
            TextView statusName = holder.getView(R.id.state);
            TextView moneyName = holder.getView(R.id.tz_fee);
            Button viewBtn = holder.getView(R.id.view);
            TextView timeTV = holder.getView(R.id.time);
            RelativeLayout linearLayout = holder.getView(R.id.item);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SportOderDetailActivity.createIntent(context, item.getId());
                }
            });
            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SportOderDetailActivity.createIntent(context, item.getId());
                }
            });

            sportName.setText(String.format(getString(R.string.ball_type_format),
                    UsualMethod.convertSportBallon(item.getSportType())));
            statusName.setText(UsualMethod.convertSportRecordStatus(item.getBalance(), item.getBettingResult()));
            if (item.getBalance() == Constant.BALANCE_UNDO) {
                statusName.setTextColor(getResources().getColor(R.color.grey));
            } else if (item.getBalance() == Constant.BALANCE_CUT_GAME) {
                statusName.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else if (item.getBalance() == Constant.BALANCE_DONE ||
                    item.getBalance() == Constant.BALANCE_AGENT_HAND_DONE ||
                    item.getBalance() == Constant.BALANCE_BFW_DONE) {
                if (item.getBettingResult() > 0) {
                    statusName.setTextColor(getResources().getColor(R.color.blue_color));
                } else {
                    statusName.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
            moneyName.setText(String.format(getString(R.string.touzhu_money_format),
                    decimalFormat.format(item.getBettingMoney())));
            timeTV.setText(Utils.formatTime(item.getBettingDate()));

        }

    }

    /**
     * 沙巴体育记录adapter
     */
    public class SBSportRecordAdapter extends LAdapter<SBSportOrder> {

        Context context;

        public SBSportRecordAdapter(Context mContext, List<SBSportOrder> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final SBSportOrder item) {

            TextView sportName = holder.getView(R.id.sport_name);
            TextView bet_money = holder.getView(R.id.bet_money);
            TextView teams = holder.getView(R.id.teams);
            TextView project_name = holder.getView(R.id.project_name);
            TextView ball_category = holder.getView(R.id.ball_category);
            TextView confirm_status = holder.getView(R.id.confirm_status);
            TextView time_str = holder.getView(R.id.time_str);
            TextView confirm = holder.getView(R.id.confirm);

            sportName.setText(!Utils.isEmptyString(item.getLeagueName()) ? item.getLeagueName() : "暂无联赛名");
            bet_money.setText(String.format("投注金额:%.2f元", item.getAccountBetMoney()));
            teams.setText(String.format("%s vs %s",
                    !Utils.isEmptyString(item.getHomeName()) ? item.getHomeName() : "暂无球队"
                    , !Utils.isEmptyString(item.getAwayName()) ? item.getAwayName() : "暂无球队"));

            if (item.getMix() != 2) {

                StringBuilder sb = new StringBuilder();
                sb.append(item.getBetOddsTypeName()).append("--");
                if (!Utils.isEmptyString(item.getBetTeamName())) {
                    if (item.getBetTeamName().equalsIgnoreCase("小")) {
                        sb.append("小");
                    } else if (item.getBetTeamName().equalsIgnoreCase("大")) {
                        sb.append("大");
                    } else if (item.getBetTeamName().equalsIgnoreCase("客队")) {
                        sb.append((!Utils.isEmptyString(item.getAwayName()) ? item.getAwayName() : "")).append(" ");
                    } else {
                        sb.append((!Utils.isEmptyString(item.getHomeName()) ? item.getHomeName() : "")).append(" ");
                    }
                } else {
                    sb.append((!Utils.isEmptyString(item.getHomeName()) ? item.getHomeName() : "")).append(" ");
                }

                sb.append(((item.getHdp() != null && item.getHdp() != 0) ? item.getHdp() : "") + "@" + item.getBetOdds());
                project_name.setText(sb.toString());

                ball_category.setText((!Utils.isEmptyString(item.getSportTypeName()) ? item.getSportTypeName() : "")
                        + "(" + item.getBetTypeName() + ")");
            } else {
                project_name.setText(!Utils.isEmptyString(item.getBetOddsTypeName()) ? item.getBetOddsTypeName() : "暂无盘口");
                String ballName = "";
                if (item.getChildrens() != null && !item.getChildrens().isEmpty()) {
                    List<SBSportOrder> sub = item.getChildrens();
                    ballName = sub.get(0).getSportTypeName();
                }
                ball_category.setText((!Utils.isEmptyString(ballName) ? ballName : "") + "(" + item.getBetTypeName() + ")");
            }

            confirm_status.setText(UsualMethod.getOrderStatus(item.getTicketStatus()));
            time_str.setText(String.format("时间:%s", Utils.formatTime(item.getTransactionTime())));
            confirm.setText(UsualMethod.getBetStatus(item.getTicketStatus()));

            if (item.getMix() == 2) {
                sportName.setVisibility(View.INVISIBLE);
                teams.setVisibility(View.GONE);
            } else {
                sportName.setVisibility(View.VISIBLE);
                teams.setVisibility(View.VISIBLE);
            }
            LinearLayout linearLayout = holder.getView(R.id.item);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String orderStr = new Gson().toJson(item, SBSportOrder.class);
                    SbsportOrderDetailActivity.createIntent(context, orderStr);
                }
            });
        }

    }

    /**
     * 真人或电子记录adapter
     */
    public class RealGameRecordAdapter extends LAdapter<RealRecordResponse.RealRecordDetail> {

        Context context;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        public RealGameRecordAdapter(Context mContext, List<RealRecordResponse.RealRecordDetail> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final RealRecordResponse.RealRecordDetail item) {

            TextView platformName = holder.getView(R.id.platform_type);
            TextView winState = holder.getView(R.id.winState);
            TextView touzhuFee = holder.getView(R.id.tz_fee);
            TextView timeTV = holder.getView(R.id.time);


            platformName.setText(String.format(getString(R.string.lottery_type_format), item.getGameType(), item.getPlatformType()));
            winState.setText("中奖金额: " + item.getWinMoney() + "元");
            touzhuFee.setText(String.format(getString(R.string.bet_money), item.getBettingMoney()));
            timeTV.setText(sdf.format(new Date(item.getBettingTime())));
            holder.getView(R.id.item).setOnClickListener(v -> {
                String json = new Gson().toJson(item);
                RealElectricRecordDetailActivity.createIntent(RecordsActivity.this, json);
            });
        }

    }

    /**
     * 撤销订单操作
     *
     * @param orderId
     * @param lotCode
     */
    private void actionCancelOrder(String orderId, String lotCode) {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_CANCEL_ORDER_URL);
        configUrl.append("?orderId=").append(orderId).append("&");
        configUrl.append("lotCode=").append(lotCode);
        CrazyRequest<CrazyResult<CancelOrderWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(CANCEL_ORDER)
                .headers(Urls.getHeader(this))
                .shouldCache(true).placeholderText(getString(R.string.cancel_ongogin))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<CancelOrderWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listDatas != null) {
            listDatas.clear();
            listDatas = null;
        }
        if (accountRecords != null) {
            accountRecords.clear();
        }
        if (sportOrders != null) {
            sportOrders.clear();
        }
        if (realGameResults != null) {
            realGameResults.clear();
        }

    }


    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        switch (action) {

            //??投注记录
            case TOUZHU_RECORD: {
                if (recordList.isRefreshing()) {
                    recordList.stopRefresh();
                } else if (recordList.isPullLoading()) {
                    recordList.stopLoadMore();
                }
                empty.setVisibility(View.VISIBLE);
                recordList.setEmptyView(empty);
                CrazyResult<Object> result = response.result;
                if (result == null) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                Object regResult = result.result;
                LotteryRecordWraper reg = (LotteryRecordWraper) regResult;
                if (!reg.isSuccess()) {
                    showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                            getString(R.string.get_record_fail));
                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (reg.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(this);
                    }
                    return;
                }
                YiboPreference.instance(this).setToken(reg.getAccessToken());
                updateSumMoney(reg.getContent().getSumBuyMoney(), reg.getContent().getSumWinMoney());
                if (pageIndex == 1) {
                    listDatas.clear();
                }
                totalCountFromWeb = reg.getContent().getTotalCount();
                if (reg.getContent().getResults() != null) {
                    if (recordType == Constant.CAIPIAO_RECORD_STATUS) {
                        List<BcLotteryOrder> datas = new ArrayList<>();
                        for (BcLotteryOrder bc : reg.getContent().getResults()) {
                            //当前若是彩票投注记录，则过滤掉六合彩LHC的投注记录
                            if (!bc.getLotCode().equals("LHC")) {
                                datas.add(bc);
                            }
                        }
                        listDatas = handleListResult(listDatas, datas, response.url, pageIndex == 1);
                    } else {
                        listDatas.addAll(reg.getContent().getResults());
//                        listDatas = handleListResult(listDatas, reg.getContent().getResults(), response.url, pageIndex == 1);
                    }
                    recordAdapter.notifyDataSetChanged();
                    if (totalCountFromWeb <= listDatas.size()) {
                        recordList.setPullLoadEnable(false);
                    } else {
                        recordList.setPullLoadEnable(true);
                    }
                    if (response.pickType != CrazyResponse.CACHE_REQUEST) {
                        pageIndex++;
                    }
                }

                break;
            }

            //？
            case CANCEL_ORDER: {
                CrazyResult<Object> result = response.result;
                if (result == null) {
                    showToast(R.string.cancel_order_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    showToast(R.string.cancel_order_fail);
                    return;
                }
                Object regResult = result.result;
                CancelOrderWraper reg = (CancelOrderWraper) regResult;
                if (!reg.isSuccess()) {
                    showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                            getString(R.string.cancel_order_fail));
                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (reg.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(this);
                    }
                    return;
                }
                YiboPreference.instance(this).setToken(reg.getAccessToken());
                showToast(R.string.cancel_order_success);
                pageIndex = 1;
                actionRecords(false);
                break;
            }


            //账户记录
            case ACCOUNT_RECORD: {
                if (recordList.isRefreshing()) {
                    recordList.stopRefresh();
                } else if (recordList.isPullLoading()) {
                    recordList.stopLoadMore();
                }
                empty.setVisibility(View.VISIBLE);
                recordList.setEmptyView(empty);
                CrazyResult<Object> result = response.result;
                if (result == null) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                Object regResult = result.result;
                AccountRecordWraper reg = (AccountRecordWraper) regResult;
                if (!reg.isSuccess()) {
                    showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                            getString(R.string.get_record_fail));
                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (reg.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(this);
                    }
                    return;
                }
                YiboPreference.instance(this).setToken(reg.getAccessToken());
                if (pageIndex == 1) {
                    accountRecords.clear();
                }
                totalCountFromWeb = reg.getContent().getTotalCount();
                Utils.LOG(TAG, "pick type when get account record = " + response.pickType);
                if (response.pickType != CrazyResponse.CACHE_REQUEST) {
                    pageIndex++;
                }
                if (!reg.getContent().getResults().isEmpty()) {
                    accountRecords.addAll(reg.getContent().getResults());
                }
                if (totalCountFromWeb <= accountRecords.size()) {
                    recordList.setPullLoadEnable(false);
                } else {
                    recordList.setPullLoadEnable(true);
                }
                accountRecordAdapter.notifyDataSetChanged();
                break;
            }


            //旧体育投注记录
            case SPORT_RECORD: {
                if (recordList.isRefreshing()) {
                    recordList.stopRefresh();
                } else if (recordList.isPullLoading()) {
                    recordList.stopLoadMore();
                }
                empty.setVisibility(View.VISIBLE);
                recordList.setEmptyView(empty);
                CrazyResult<Object> result = response.result;
                if (result == null) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                Object regResult = result.result;
                SportOrderWraper reg = (SportOrderWraper) regResult;
                if (!reg.isSuccess()) {
                    showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                            getString(R.string.get_record_fail));
                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (reg.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(this);
                    }
                    return;
                }
                YiboPreference.instance(this).setToken(reg.getAccessToken());
                if (pageIndex == 1) {
                    sportOrders.clear();
                }
                if (!reg.getContent().isEmpty()) {
                    sportOrders.addAll(reg.getContent());
                }
                sportRecordAdapter.notifyDataSetChanged();
                if (pageSize <= reg.getContent().size()) {
                    recordList.setPullLoadEnable(true);
                    pageIndex++;
                } else {
                    recordList.setPullLoadEnable(false);
                }
                break;
            }


            //新体育投注记录
            case NEW_SPORT_RECORD: {
                if (recordList.isRefreshing()) {
                    recordList.stopRefresh();
                } else if (recordList.isPullLoading()) {
                    recordList.stopLoadMore();
                }
                empty.setVisibility(View.VISIBLE);
                recordList.setEmptyView(empty);
                CrazyResult<Object> result = response.result;
                if (result == null) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                Object regResult = result.result;
                NewSportOrderWrapper reg = (NewSportOrderWrapper) regResult;
                if (!reg.isSuccess()) {
                    showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                            getString(R.string.get_record_fail));
                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (reg.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(this);
                    }
                    return;
                }
                YiboPreference.instance(this).setToken(reg.getAccessToken());
                if (reg.getContent().getCurrentPageNo() == 1) {
                    newsportOrders.clear();
                }
                if (!reg.getContent().getList().isEmpty()) {
                    newsportOrders.addAll(reg.getContent().getList());
                }
                newsportRecordAdapter.notifyDataSetChanged();
                if (reg.getContent().getCurrentPageNo() < reg.getContent().getTotalPageCount()) {
                    recordList.setPullLoadEnable(true);
                    pageIndex++;
                } else {
                    recordList.setPullLoadEnable(false);
                }
                break;
            }


            //沙巴体育投注记录
            case SBSPORTGAME_RECORD: {
                if (recordList.isRefreshing()) {
                    recordList.stopRefresh();
                } else if (recordList.isPullLoading()) {
                    recordList.stopLoadMore();
                }
                empty.setVisibility(View.VISIBLE);
                recordList.setEmptyView(empty);
                CrazyResult<Object> result = response.result;
                if (result == null) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                Object regResult = result.result;
                SBSportOrderWraper reg = (SBSportOrderWraper) regResult;
                if (!reg.isSuccess()) {
                    showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                            getString(R.string.get_record_fail));
                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (reg.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(this);
                    }
                    return;
                }
                YiboPreference.instance(this).setToken(reg.getAccessToken());
                if (pageIndex == 1) {
                    sbsportOrders.clear();
                }
                if (reg.getContent() != null && !reg.getContent().getList().isEmpty()) {
                    sbsportOrders.addAll(reg.getContent().getList());
                }
                sbsportRecordAdapter.notifyDataSetChanged();
                if (pageSize <= reg.getContent().getList().size()) {
                    recordList.setPullLoadEnable(true);
                    pageIndex++;
                } else {
                    recordList.setPullLoadEnable(false);
                }
                break;
            }

            //真人或者电子投注记录
            case REAL_RECORD:
            case GAME_RECORD: {
                if (recordList.isRefreshing()) {
                    recordList.stopRefresh();
                } else if (recordList.isPullLoading()) {
                    recordList.stopLoadMore();
                }
                empty.setVisibility(View.VISIBLE);
                recordList.setEmptyView(empty);
                CrazyResult<Object> result = response.result;
                if (result == null) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    showToast(R.string.get_record_fail);
                    return;
                }
                Object regResult = result.result;
                String reg = (String) regResult;
                if (Utils.isEmptyString(reg)) {
                    showToast(R.string.get_record_fail);
                    return;
                }

                llSumRealBetMoney.setVisibility(View.VISIBLE);

                RealRecordResponse wraper = new Gson().fromJson(reg, RealRecordResponse.class);
                if (wraper != null) {
                    if (wraper.getCode() != 200 && !Utils.isEmptyString(wraper.getMessage())) {
                        showToast(!Utils.isEmptyString(wraper.getMessage()) ? wraper.getMessage() :
                                getString(R.string.get_record_fail));
//                        UsualMethod.loginWhenSessionInvalid(this);
                        return;
                    }
                    if (wraper.getCode() != 200) {
                        showToast(!Utils.isEmptyString(wraper.getMessage()) ? wraper.getMessage() :
                                getString(R.string.get_record_fail));
                        return;
                    }
                    updateSumMoney(wraper.getSumBet(), wraper.getSumWin());
                    updateRealBettingMoneyCount(wraper.getSumRealBet());
                    realGameResults.clear();
                    if (wraper.getData() != null) {
                        realGameResults.addAll(wraper.getData());
                    }
                    realGameRecordAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }
}
