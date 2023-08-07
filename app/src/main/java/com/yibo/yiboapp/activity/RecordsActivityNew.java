package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.yibo.yiboapp.entify.BigPanBean;
import com.yibo.yiboapp.entify.CancelOrderWraper;
import com.yibo.yiboapp.entify.ChessBetBean;
import com.yibo.yiboapp.entify.ChessBetResultWraper;
import com.yibo.yiboapp.entify.EsportDataKt;
import com.yibo.yiboapp.entify.EsportDataRow;
import com.yibo.yiboapp.entify.HunterGameData;
import com.yibo.yiboapp.entify.LotteryRecordWraper;
import com.yibo.yiboapp.entify.NewSportOrderBean;
import com.yibo.yiboapp.entify.NewSportOrderWrapper;
import com.yibo.yiboapp.entify.RealRecordResponse;
import com.yibo.yiboapp.entify.SBSportOrder;
import com.yibo.yiboapp.entify.SBSportOrderWraper;
import com.yibo.yiboapp.entify.SportOrder;
import com.yibo.yiboapp.entify.SportOrderWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.ThirldSpordData;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.SportTableContainer;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.CustomDatePicker;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;


/**
 * Author: Ray
 * created on 2018/10/19
 * description : 各种投注记录的页面
 */
public class RecordsActivityNew extends BaseActivity implements View.OnClickListener, SessionResponse.Listener<CrazyResult> {

    //给所有查看投注记录动作时使用
    public static void createIntent(Context context, String name, int status, String cpBianma) {
        Intent intent = new Intent(context, RecordsActivityNew.class);
        intent.putExtra("cp_name", name);
        intent.putExtra("recordType", status);
        intent.putExtra("cpBianma", cpBianma);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //针对体育投注页面的跳转接口
    public static void createIntent(Context context, String name, int status, int ballType, boolean fromSportBetPage) {
        Intent intent = new Intent(context, RecordsActivityNew.class);
        intent.putExtra("cp_name", name);
        intent.putExtra("recordType", status);
        intent.putExtra("ballType", ballType);
        intent.putExtra("fromSportBetPage", fromSportBetPage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private RecordAdapter recordAdapter;
    private List<BcLotteryOrder> listDatas;
    private List<AccountRecord> accountRecords;
    private List<SportOrder> sportOrders;
    private List<NewSportOrderBean> newsportOrders;
    private List<SBSportOrder> sbsportOrders;
    private List<RealRecordResponse.RealRecordDetail> realGameResults;
    private List<ChessBetBean> chessGameResults;
    private List<ThirldSpordData.RowsBean> ThirldSports;
    private List<HunterGameData.RowsBean> HunterGames;
    private List<BigPanBean.ContentBean.BigPanlistBean> bigpanResults;
    private List<EsportDataRow> esportDataRows;


    private long totalCountFromWeb;

    private AccountRecordAdapter accountRecordAdapter;
    private SportRecordAdapter sportRecordAdapter;
    private ChessGameRecordAdapter chessGameRecordAdapter;
    private NewSportRecordAdapter newsportRecordAdapter;
    private SBSportRecordAdapter sbsportRecordAdapter;
    private RealGameRecordAdapter realGameRecordAdapter;
    private BigPanRecordAdapter bigPanRecordAdapter;
    private HunterGameAdapter hunterGameAdapter;
    private NewThirldSportRecordAdapter newThirldSportRecordAdapter;
    private EsportRecordAdapter esportRecordAdapter;

    private String statusCategory = "all";
    private String dateTime = "today";
    private String cpBianma = "";//查询的彩票编码

    public static final int UPDATE_MENU_TABS = 0x01;
    public static final int TOUZHU_RECORD = 0x01;
    public static final int CANCEL_ORDER = 0x02;
    public static final int ACCOUNT_RECORD = 0x03;
    public static final int SPORT_RECORD = 0x04;
    public static final int REAL_RECORD = 0x05;
    public static final int GAME_RECORD = 0x06;
    public static final int SBSPORTGAME_RECORD = 0x07;
    public static final int MYBIGPAN_RECORD = 0x08;
    public static final int NEW_SPORT_RECORD = 0x09;
    public static final int CHESS_RECORD = 0x10;
    public static final int BIG_PAN = 0x11;
    public static final int THIRLD_SPORT = 0x12;
    public static final int HUNTER_GAME = 0x13;
    public static final int ESPORT = 0x14;

    private LinearLayout llSearchLayout;

    private XListView recordList;

    private EmptyListView empty;

    private LinearLayout ll_value_bet;
    private TextView sumBetMoneyUI;
    private TextView valueBetMoneyUI;
    private TextView sumWinMoneyUI;
    private TextView sumzhongjiangMoneyUI;

    private int recordType;

    private int pageIndex = 1;

    private int pageSize = 20;

    //真人投注过滤条件
    private String realPlatform = "";
    //棋牌投注平台过滤条件
    private String chessPlatform = "";

    //电子投注过滤条件
    private String gamePlatform = "";

    //状态 时间 彩种的三个布局
    private LinearLayout llState;
    private LinearLayout llTime;
    private LinearLayout llLotteryType;


    private TextView tvState;//状态
    private TextView tvType;//彩种

    private TextView tvStateTitle;//状态
    private TextView tvTypeTitle;//彩种

    private TextView tvStartTime;//开始时间
    private TextView tvEndTime;//结束时间


    private String startTime;//开始时间
    private String endTime;//结束时间

    private MenuHandler menuHandler;

    private String now;//当前时间

    private int ballCategory;//球种 0-全部 1-足球 2-篮球

    private PopupWindow popupWindowState;//状态的popupWindow

    private PopupWindow popupWindowType;//类型的popupWindow
    private PopupWindow popupWindowPlatform;//平台的popupWindow

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
                recordType == Constant.CHESS_GAME_RECORD_STATUS ||
                recordType == Constant.THIRLD_SPORT_RECORD ||
                recordType == Constant.HUNTER_RECORD ||
                recordType == Constant.ESPORT_RECORD
        ) {

            SysConfig config = UsualMethod.getConfigFromJson(this);
            View view = LayoutInflater.from(this).inflate(R.layout.touzhu_attach_header, null);
            ll_value_bet = (LinearLayout) view.findViewById(R.id.ll_value_bet);
            sumBetMoneyUI = (TextView) view.findViewById(R.id.total_betmoney);
            valueBetMoneyUI = (TextView) view.findViewById(R.id.value_betmoney);
            sumWinMoneyUI = (TextView) view.findViewById(R.id.total_winmoney);
            sumzhongjiangMoneyUI = (TextView) view.findViewById(R.id.total_zhongjiang);
            if (config.getMobile_v3_bet_order_detail_total().equalsIgnoreCase("on")) {
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

        cpBianma = getIntent().getStringExtra("cpBianma");
        int type = getIntent().getIntExtra("ballType", SportTableContainer.FOOTBALL_PAGE);
        boolean fromSportBetPage = getIntent().getBooleanExtra("fromSportBetPage", false);
        if (fromSportBetPage) {
            ballCategory = type == 0 ? 1 : 2;
        }

        menuHandler = new MenuHandler(this);


        //初始化时间选择器
        initDatePicker();
        //初始化当前时间
        initCurrentTime();

        //请求记录
        requestRecord();

        SysConfig config = UsualMethod.getConfigFromJson(this);
        if (config != null) {
            if (!Utils.isEmptyString(config.getLottery_order_cancle_switch()) &&
                    config.getLottery_order_cancle_switch().equalsIgnoreCase("on")) {
                cpcdOpen = true;
            }
        }

        //直接访问接口
        actionRecords(true);

        flContent.addView(mContentView);

    }

    //初始化控件
    private void initFindViewById() {
        //头部搜索布局
        llSearchLayout = (LinearLayout) findViewById(R.id.ll_search);
        //状态
        llState = (LinearLayout) findViewById(R.id.ll_state);
        //时间
        llTime = (LinearLayout) findViewById(R.id.ll_time);
        //彩种
        llLotteryType = (LinearLayout) findViewById(R.id.ll_lottery_type);
        //状态
        tvState = (TextView) findViewById(R.id.tv_state);
        //类型
        tvType = (TextView) findViewById(R.id.tv_type);
        //开始时间
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        //结束时间
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        //彩种 球种 平台
        tvTypeTitle = (TextView) findViewById(R.id.tv_type_title);
        tvStateTitle = (TextView) findViewById(R.id.tv_state_title);
        //内容部分
        flContent = (FrameLayout) findViewById(R.id.fl_content);


        //取消和确定的按钮
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);

        tvState.setOnClickListener(this);
        tvType.setOnClickListener(this);

    }

    private void requestRecord() {
        //请求记录
        if (recordType == Constant.ACCOUNT_CHANGE_RECORD_STATUS) {
            accountRecords = new ArrayList<>();
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
        } else if (recordType == Constant.CHESS_GAME_RECORD_STATUS) {
            chessGameResults = new ArrayList<>();
            chessGameRecordAdapter = new ChessGameRecordAdapter(this, chessGameResults, R.layout.real_game_item);
            recordList.setAdapter(chessGameRecordAdapter);
        } else if (recordType == Constant.BIG_PAN_RECORD_STATUS) {
            bigpanResults = new ArrayList<>();
            bigPanRecordAdapter = new BigPanRecordAdapter(this, bigpanResults, R.layout.big_pan_item);
            recordList.setAdapter(bigPanRecordAdapter);
        } else if (recordType == Constant.THIRLD_SPORT_RECORD) {
            ThirldSports = new ArrayList<>();
            newThirldSportRecordAdapter = new NewThirldSportRecordAdapter(this, ThirldSports, R.layout.thirld_sport_data_item);
            recordList.setAdapter(newThirldSportRecordAdapter);
        } else if (recordType == Constant.HUNTER_RECORD) {
            HunterGames = new ArrayList<>();
            hunterGameAdapter = new HunterGameAdapter(this, HunterGames, R.layout.hunter_data_item);
            recordList.setAdapter(hunterGameAdapter);
        } else if (recordType == Constant.ESPORT_RECORD){
            esportDataRows = new ArrayList<>();
            esportRecordAdapter = new EsportRecordAdapter(this,esportDataRows,R.layout.esport_data_item);
            recordList.setAdapter(esportRecordAdapter);
        } else {
            listDatas = new ArrayList<>();
            recordAdapter = new RecordAdapter(this, listDatas, R.layout.touzhu_record_item);
            recordList.setAdapter(recordAdapter);
        }
    }

    /**
     * 获取棋牌投注记录
     *
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param platformType 平台类型 nb,ky
     */
    private void getChessRecords(String startTime, String endTime, String platformType, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        try {
            if (platformType.equalsIgnoreCase("全部")) {
                platformType = "";
            } else if (platformType.equalsIgnoreCase("NB记录")) {
                platformType = "NB";
            } else if (platformType.equalsIgnoreCase("开元记录")) {
                platformType = "KY";
            } else if (platformType.equalsIgnoreCase("YG记录")) {
                platformType = "YG";
            } else if (platformType.equalsIgnoreCase("乐游记录")) {
                platformType = "LEG";
            } else if (platformType.equalsIgnoreCase("YB记录")) {
                platformType = "YB";
            } else if (platformType.equalsIgnoreCase("百胜记录")) {
                platformType = "BS";
            }
            configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.CHESS_BET_RECORD_URL);
            configUrl.append("?startTime=").append(URLEncoder.encode(startTime + ":00", "utf-8")).append("&");
            configUrl.append("endTime=").append(URLEncoder.encode(endTime + ":00", "utf-8")).append("&");
            configUrl.append("pageSize=").append(60).append("&");
            configUrl.append("pageNumber=").append(pageIndex).append("&");
            int type = UsualMethod.convertChessMainPlatformValue(platformType);
            configUrl.append("type=").append(type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(CHESS_RECORD)
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
     * 获取大转盘记录
     *
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param platformType 平台类型 nb,ky
     */
    private void getBigpan(String startTime, String endTime, String platformType, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        int type = 0;
        try {
            if (platformType.equalsIgnoreCase("全部")) {
                type = 0;
            } else if (platformType.equalsIgnoreCase("现金")) {
                type = 2;
            } else if (platformType.equalsIgnoreCase("商品")) {
                type = 3;
            } else if (platformType.equalsIgnoreCase("积分")) {
                type = 4;
            }
            configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.BIG_PAN_RECORD_LIST);
            configUrl.append("?startTime=").append(URLEncoder.encode(startTime, "utf-8")).append("&");
            configUrl.append("endTime=").append(URLEncoder.encode(endTime, "utf-8")).append("&");
            configUrl.append("pageSize=").append(60).append("&");
            configUrl.append("pageNumber=").append(pageIndex).append("&");
            if (type != 0)
                configUrl.append("type=").append(type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(BIG_PAN)
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
     * 初始化当前时间
     */
    private void initCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date date = new Date();
        //当前时间
        now = sdf.format(date);
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
            case R.id.tv_state:
                tvState.setTextColor(getResources().getColor(R.color.colorPrimary));
                popupWindowState.showAsDropDown(v);
                break;
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
                llSearchLayout.setVisibility(View.GONE);
                pageIndex = 1;
                actionRecords(true);
                break;
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

    private void setLinearLayoutState() {
        //根据不同的投注类型选择彩种信息
        llState.setVisibility(View.VISIBLE);
        llTime.setVisibility(View.VISIBLE);
        llLotteryType.setVisibility(View.VISIBLE);

        switch (recordType) {
            case Constant.CAIPIAO_RECORD_STATUS:
                //全部显示
                break;
            case Constant.LHC_RECORD_STATUS:
                llLotteryType.setVisibility(View.GONE);
                llState.setVisibility(View.GONE);
                break;
            case Constant.CHESS_GAME_RECORD_STATUS:
                llLotteryType.setVisibility(View.GONE);
                break;
            case Constant.SPORTS_RECORD_STATUS:
            case Constant.SBSPORTS_RECORD_STATUS:
            case Constant.OLD_SPORTS_RECORD_STATUS:
            case Constant.REAL_PERSON_RECORD_STATUS:
            case Constant.ELECTRIC_GAME_RECORD_STATUS:
                llState.setVisibility(View.GONE);
                break;
            case Constant.ACCOUNT_CHANGE_RECORD_STATUS:
                llState.setVisibility(View.GONE);
                llLotteryType.setVisibility(View.GONE);
                break;
            case Constant.BIG_PAN_RECORD_STATUS:
                llState.setVisibility(View.GONE);
                break;
        }

        //点击弹出不同弹出框信息
        TouzhuThreadPool.getInstance().addTask(new FigureTabMenuContent(this, recordType));
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
                //获取存储在本地preference中的彩种信息
                if (weakReference.get() == null) {
                    return;
                }
                Type listType = new TypeToken<ArrayList<LotteryData>>() {
                }.getType();
                List<LotteryData> lotteryDatas = new Gson().fromJson(
                        YiboPreference.instance(weakReference.get()).getLotterys(), listType);
                if (lotteryDatas != null) {
                    List<String> cpDatas = new ArrayList<>();
                    cpDatas.add("全部,all");
                    if (!lotteryDatas.isEmpty()) {
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
            } else if (recordType == Constant.SPORTS_RECORD_STATUS || recordType == Constant.SBSPORTS_RECORD_STATUS ||
                    recordType == Constant.OLD_SPORTS_RECORD_STATUS) {
                tabMap.put("球种", getResources().getStringArray(R.array.qiuzhong_categories));
            } else if (recordType == Constant.REAL_PERSON_RECORD_STATUS) {
                tabMap.put("平台", getResources().getStringArray(R.array.realren_platform_categories));
            } else if (recordType == Constant.ELECTRIC_GAME_RECORD_STATUS) {
                tabMap.put("平台", getResources().getStringArray(R.array.game_platform_categories));
            } else if (recordType == Constant.CHESS_GAME_RECORD_STATUS) {
                tabMap.put("棋牌平台", getResources().getStringArray(R.array.chess_platform_categories));
            } else if (recordType == Constant.BIG_PAN_RECORD_STATUS) {
                tabMap.put("类型", getResources().getStringArray(R.array.bigpan_record_category));
            }

            Message msg = menuHandler.obtainMessage(UPDATE_MENU_TABS, tabMap);
            menuHandler.sendMessageDelayed(msg, 100);
        }
    }

    //线程异步handler
    private static class MenuHandler extends Handler {
        private WeakReference<RecordsActivityNew> mReference;

        private MenuHandler(RecordsActivityNew context) {
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


    /**
     * 初始化popWindow
     *
     * @param menuData 列表数据参数
     */
    private void initPlayDropMenu(LinkedHashMap<String, Object> menuData) {

        //状态
        String[] state = {};
        //彩种
        String[] type = {};
        for (Map.Entry<String, Object> entry : menuData.entrySet()) {
            String key = entry.getKey();
            //状态
            if (key.equals("状态") || key.equalsIgnoreCase("棋牌平台")) {
                state = (String[]) entry.getValue();
            } else {
                type = (String[]) entry.getValue();
            }
            //类型
            switch (key) {
                case "彩种":
                    tvTypeTitle.setText("彩种：");
                    tvType.setText("请选择彩种");
                    break;
                case "球种":
                    tvTypeTitle.setText("球种：");
                    tvType.setText("请选择球种");
                    break;
                case "平台":
                    tvTypeTitle.setText("平台：");
                    tvType.setText("请选择平台");
                    break;
                case "棋牌平台":
                    tvStateTitle.setText("平台：");
                    tvState.setText("请选择平台");
                    break;
                case "类型":
                    tvTypeTitle.setText("类型：");
                    tvType.setText("请选择类型");
                    break;
            }
            tvType.setText("全部");
        }

        //状态
        if (llState.getVisibility() != View.GONE) {
            popupWindowState = new PopupWindow(this);
            initPopupWindowContent(popupWindowState, state, tvState, 1);
        }

        //平台 彩种 球种
        if (llLotteryType.getVisibility() != View.GONE) {
            popupWindowType = new PopupWindow(this);
            initPopupWindowContent(popupWindowType, type, tvType, 2);
        }


    }


    /**
     * 初始化popupWindow内容
     *
     * @param popupWindow
     * @param arrays
     * @param tv
     * @param flag
     */
    private void initPopupWindowContent(final PopupWindow popupWindow, final String[] arrays, TextView tv, final int flag) {

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
                if (flag == 1) {
                    tvState.setTextColor(getResources().getColor(R.color.system_default_color));
                } else {
                    tvType.setTextColor(getResources().getColor(R.color.system_default_color));
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = arrays[position];
                String itemValue = "";
                if (!Utils.isEmptyString(item) && item.contains(",")) {
                    String[] split = item.split(",");
                    if (split.length != 2) {
                        return;
                    }
                    item = split[0];
                    itemValue = split[1];
                }

                if (flag == 1) {
                    tvState.setText(item);
                } else {
                    tvType.setText(item);
                }
                adapter.setSelectPosition(position);
                popupWindow.dismiss();


                if (flag == 1 && tvStateTitle.getText().equals("状态：")) {
                    statusCategory = itemValue;
                } else if (flag == 1 && tvStateTitle.getText().equals("平台")) {
                    chessPlatform = item;
                }

                boolean tag = flag == 2;
                if (!tag) return;
                if (tvTypeTitle.getText().equals("彩种：")) {
                    cpBianma = !itemValue.equals("all") ? itemValue : "";
                    tvMiddleTitle.setText(getString(R.string.touzhu_record) + "(" + item + ")");
                }
                if (tvTypeTitle.getText().equals("类型：")) {
                    tvMiddleTitle.setText("中奖记录" + "(" + item + ")");
                    chessPlatform = item;
                } else if (tvTypeTitle.getText().equals("球种：")) {
                    ballCategory = position;
                } else if (tvTypeTitle.getText().equals("平台：")) {
                    if (position == 0) {
                        gamePlatform = "";
                        realPlatform = "";
                    } else {
                        gamePlatform = item;
                        realPlatform = item;
                    }
                }

            }
        });


    }


    public class MyBaseAdapter extends BaseAdapter {

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

            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(RecordsActivityNew.this, R.layout.adapter_item_popwindow, null);
                holder.textView = (TextView) convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
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


    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
        @Override
        public void onEmptyListviewClick() {
            pageIndex = 1;
            actionRecords(true);
        }
    };


    /**
     * 列表下拉，上拉监听器
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
            getAccountRecords(startTime, endTime, pageIndex, pageSize, showDialog);
        } else if (recordType == Constant.SPORTS_RECORD_STATUS) {
            getSportRecords(startTime, endTime, ballCategory, Constant.RECORD_TYPE_ALL, showDialog, false);
        } else if (recordType == Constant.OLD_SPORTS_RECORD_STATUS) {
            getSportRecords(startTime, endTime, ballCategory, Constant.RECORD_TYPE_ALL, showDialog, true);
        } else if (recordType == Constant.SBSPORTS_RECORD_STATUS) {
//            String time = Utils.formatTime(System.currentTimeMillis(),"yyyy-MM-dd");
            getsbSportRecords(startTime, endTime, 1, 20, ballCategory, "", true);
        } else if (recordType == Constant.REAL_PERSON_RECORD_STATUS) {
            getRealPersonRecords(startTime, endTime, realPlatform, showDialog);
        } else if (recordType == Constant.ELECTRIC_GAME_RECORD_STATUS) {
            getGameRecords(startTime, endTime, gamePlatform, showDialog);
        } else if (recordType == Constant.CHESS_GAME_RECORD_STATUS) {
            getChessRecords(startTime, endTime, chessPlatform, showDialog);
        } else if (recordType == Constant.BIG_PAN_RECORD_STATUS) {
            getBigpan(startTime, endTime, chessPlatform, showDialog);
        } else if (recordType == Constant.THIRLD_SPORT_RECORD) {
            getThirdSportRecord(startTime, endTime, chessPlatform, showDialog);
        } else if (recordType == Constant.HUNTER_RECORD) {
            getHunterRecord(startTime, endTime, chessPlatform, showDialog);
        } else if (recordType == Constant.ESPORT_RECORD) {
            getSportRecords(startTime, endTime, chessPlatform, showDialog);
        } else {
            getRecords(statusCategory, startTime, endTime, cpBianma, pageIndex, pageSize, showDialog);
        }
        if (!showDialog) {
            startProgress();
        }
    }

    /**
     * @param time 时间
     * @param
     * @return
     */
    private String encodeTime(String time, int flag) {

        if (flag == 1) {
            if (!tvStartTime.getText().equals("请选择开始时间")) {
                time = tvStartTime.getText().toString() + ":00";
            }
        } else {
            if (!tvEndTime.getText().equals("请选择结束时间")) {
                time = tvEndTime.getText().toString() + ":00";
            }
        }


        String encodeTime = "";
        try {
            encodeTime = URLEncoder.encode(time, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodeTime;
    }


    /**
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param page       页码
     * @param pageSize
     * @param showDialog
     */
    private void getAccountRecords(String startTime, String endTime, int page, int pageSize, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        String encodeStartTime = encodeTime(startTime, 1);
        String encodeEndTime = encodeTime(endTime, 2);

        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ACCOUNT_CHANGE_RECORD_URL);

        configUrl.append("?startTime=").append(encodeStartTime).append("&")
                .append("endTime=").append(encodeEndTime).append("&")
                .append("pageNumber=").append(page).append("&").
                append("pageSize=").append(pageSize);


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
     * @param startTime  开始时间   时间类型 1-今天 2-昨天 3-一周 4-近30天
     * @param endTime    结束时间
     * @param sportType  球类 0-全部 1-足球 2-篮球
     * @param recordType 记录类型 1-全部 2-会员赢钱 3-未开奖 4-未成功
     * @param isOldSport 是否旧体育记录
     */
    private void getSportRecords(String startTime, String endTime, int sportType, int recordType, boolean showDialog,
                                 boolean isOldSport) {

        StringBuilder configUrl = new StringBuilder();
        String encodeStartTime = encodeTime(startTime, 1);
        String encodeEndTime = encodeTime(endTime, 2);
        if (isOldSport) {


            configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.OLD_SPORT_RECORDS);
            configUrl.append("?sportType=").append(sportType).append("&");
            configUrl.append("startTime=").append(encodeStartTime).append("&");
            configUrl.append("endTime=").append(encodeEndTime).append("&");
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
            configUrl.append("startTime=").append(encodeStartTime).append("&");
            configUrl.append("endTime=").append(encodeEndTime).append("&");
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

    private void getsbSportRecords(String startTime, String endTime, int pageNumber, int pageSize,
                                   int type, String orderno, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();

        String encodeStartTime = encodeTime(startTime, 1);
        String encodeEndTime = encodeTime(endTime, 2);

        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SBSPORT_RECORDS);

        configUrl.append("?startTime=").append(encodeStartTime).append("&");
        configUrl.append("endTime=").append(encodeEndTime).append("&");
        configUrl.append("transId=").append(orderno).append("&");
        configUrl.append("type=").append(type).append("&");
        configUrl.append("currentPageNo=").append(pageNumber).append("&");
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
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param platformType 平台类型 ag,og,bbin,....
     */
    private void getRealPersonRecords(String startTime, String endTime, String platformType, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.REAL_BET_RECORD_URL);


        String encodeStartTime = encodeTime(startTime, 1);
        String encodeEndTime = encodeTime(endTime, 2);

        int type = UsualMethod.convertRealMainPlatformValue(platformType);
        configUrl.append("?startTime=").append(encodeStartTime).append("&");
        configUrl.append("endTime=").append(encodeEndTime).append("&");
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
     * 获取三方体育
     *
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param platformType 平台类型 ag,og,bbin,....
     */
    private void getThirdSportRecord(String startTime, String endTime, String platformType, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_THIRD_SPORT_RECORD);


        String encodeStartTime = encodeTime(startTime, 1);
        String encodeEndTime = encodeTime(endTime, 2);

        int type = UsualMethod.convertRealMainPlatformValue(platformType);
        configUrl.append("?startTime=").append(encodeStartTime).append("&");
        configUrl.append("endTime=").append(encodeEndTime).append("&");
//        configUrl.append("liveType=").append(type == 0 ? "" : type);

        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(THIRLD_SPORT)
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
     * 获取捕鱼
     *
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param platformType 平台类型 ag,og,bbin,....
     */
    private void getHunterRecord(String startTime, String endTime, String platformType, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_HUNTER_RECORD);


        String encodeStartTime = encodeTime(startTime, 1);
        String encodeEndTime = encodeTime(endTime, 2);

//        int type = UsualMethod.convertRealMainPlatformValue(platformType);
        configUrl.append("?startTime=").append(encodeStartTime).append("&");
        configUrl.append("endTime=").append(encodeEndTime).append("&");
//        configUrl.append("liveType=").append(type == 0 ? "" : type);

        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(HUNTER_GAME)
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
     * @param
     * @param gamePlatform 记录类型 1-全部 2-会员赢钱 3-未开奖 4-未成功
     */
    private void getGameRecords(String startTime, String endTime, String gamePlatform, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();

        String encodeStartTime = encodeTime(startTime, 1);
        String encodeEndTime = encodeTime(endTime, 2);

        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GAME_BET_RECORD_URL);
//        configUrl.append("?dateType=").append(dateTime).append("&");
        int gameType = UsualMethod.convertEgameValue(gamePlatform);
        configUrl.append("?startTime=").append(encodeStartTime).append("&");
        configUrl.append("endTime=").append(encodeEndTime).append("&");
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

    private void getSportRecords(String startTime, String endTime, String gamePlatform, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();

        String encodeStartTime = encodeTime(startTime, 1);
        String encodeEndTime = encodeTime(endTime, 2);

        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_ESPORT_RECORD);
//        int type = UsualMethod.convertRealMainPlatformValue(platformType);
        configUrl.append("?startTime=").append(encodeStartTime).append("&");
        configUrl.append("endTime=").append(encodeEndTime).append("&");
//        configUrl.append("liveType=").append(type == 0 ? "" : type);

        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(ESPORT)
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
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param lotcode   彩种编码
     * @param page      页码
     * @param pageSize  每页条数
     */
    private void getRecords(String queryType, String startTime, String endTime, String lotcode, int page, int pageSize, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();


        String encodeStartTime = encodeTime(startTime, 1);
        String encodeEndTime = encodeTime(endTime, 2);

        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_RECORD_URL_V2);

        configUrl.append("?queryType=").append(queryType).append("&");
        configUrl.append("startTime=").append(encodeStartTime).append("&");
        configUrl.append("endTime=").append(encodeEndTime).append("&");
        configUrl.append("lotCode=").append(lotcode).append("&");
        configUrl.append("page=").append(page).append("&");
        configUrl.append("rows=").append(pageSize);
        Log.e("whw", configUrl.toString() + "");

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
            TextView playName = holder.getView(R.id.play_name);
            playName.setVisibility(View.GONE);
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
            if (!TextUtils.isEmpty(item.getCreateTimeStr())) {
                tzTime.setText(item.getCreateTimeStr());
            } else {
                tzTime.setText(Utils.formatBeijingTime(item.getCreateTime()));
            }
            playName.setText(String.format("玩法:%s", item.getPlayName()));
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
     * 转盘记录adapter
     */
    public class BigPanRecordAdapter extends LAdapter<BigPanBean.ContentBean.BigPanlistBean> {

        Context context;
        SimpleDateFormat sFormat;

        public BigPanRecordAdapter(Context mContext, List<BigPanBean.ContentBean.BigPanlistBean> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            sFormat = new SimpleDateFormat("yyyy-MM-dd");
        }


        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final BigPanBean.ContentBean.BigPanlistBean item) {
            TextView remark = holder.getView(R.id.remark);
            TextView content = holder.getView(R.id.content);
            TextView time = holder.getView(R.id.time);
            remark.setText(item.getRemark());
            content.setText(item.getProductName());
            if (!TextUtils.isEmpty(item.getCreateDatetime()))
                time.setText(sFormat.format(Long.parseLong(item.getCreateDatetime())));


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
            statusName.setText(UsualMethod.convertResultStatus(item.getResultStatus()));

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
     * 新体育记录adapter
     */
    public class NewThirldSportRecordAdapter extends LAdapter<ThirldSpordData.RowsBean> {

        Context context;
        DecimalFormat decimalFormat;

        public NewThirldSportRecordAdapter(Context mContext, List<ThirldSpordData.RowsBean> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            decimalFormat = new DecimalFormat("0.00");
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final ThirldSpordData.RowsBean item) {

            TextView tv_winmoney = holder.getView(R.id.tv_winmoney);
            TextView tv_title = holder.getView(R.id.tv_title);
            TextView tv_bettingmoney = holder.getView(R.id.tv_bettingmoney);
            TextView tv_time = holder.getView(R.id.time);
            tv_bettingmoney.setText(item.getBettingMoney() + "");
            tv_winmoney.setText(item.getWinMoney() - item.getBettingMoney() + "");
            tv_time.setText(Utils.formatTime(item.getBettingTime()));
            tv_title.setText(item.getPlatformType());
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThirldSportDetailsActivity.createIntent(context, item);
                }
            });
        }

    }


    /**
     * 捕鱼记录adapter
     */
    public class HunterGameAdapter extends LAdapter<HunterGameData.RowsBean> {

        Context context;
        DecimalFormat decimalFormat;

        public HunterGameAdapter(Context mContext, List<HunterGameData.RowsBean> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            decimalFormat = new DecimalFormat("0.00");
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final HunterGameData.RowsBean item) {
            TextView tv_winmoney = holder.getView(R.id.tv_winmoney);
            TextView tv_title = holder.getView(R.id.tv_title);
            TextView tv_content = holder.getView(R.id.tv_content);
            TextView tv_bettingmoney = holder.getView(R.id.tv_bettingmoney);
            TextView tv_time = holder.getView(R.id.time);
            tv_bettingmoney.setText(item.getBettingMoney() + "");
            tv_winmoney.setText(item.getWinMoney() + "");
            tv_content.setText("");
            tv_time.setText(Utils.formatTime(item.getBettingTime()));
            tv_title.setText(item.getGameName());


        }

    }

    public class EsportRecordAdapter extends LAdapter<EsportDataRow> {

        Context context;
        DecimalFormat decimalFormat;

        public EsportRecordAdapter(Context mContext, List<EsportDataRow> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            decimalFormat = new DecimalFormat("0.00");
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, EsportDataRow item) {
            TextView tv_winmoney = holder.getView(R.id.tv_winmoney);
            TextView tv_title = holder.getView(R.id.tv_title);
            TextView tv_content = holder.getView(R.id.tv_content);
            TextView tv_bettingmoney = holder.getView(R.id.tv_bettingmoney);
            TextView tv_time = holder.getView(R.id.time);
            tv_bettingmoney.setText(item.getBettingMoney() + "");
            tv_winmoney.setText(item.getWinMoney() + "");
            tv_content.setText("");
            tv_time.setText(Utils.formatTime(item.getBettingTime()));
            tv_title.setText(item.getGameName());
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
                String a = item.getBetOddsTypeName() + "--" +
                        (!Utils.isEmptyString(item.getHomeName()) ? item.getHomeName() : "") + " "
                        + ((item.getHdp() != null && item.getHdp() != 0) ? item.getHdp() : "") + "@" + item.getBetOdds();
                project_name.setText(a);
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
                RealElectricRecordDetailActivity.createIntent(RecordsActivityNew.this, json);
            });
        }

    }

    /**
     * 棋牌记录adapter
     */
    public class ChessGameRecordAdapter extends LAdapter<ChessBetBean> {

        Context context;

        public ChessGameRecordAdapter(Context mContext, List<ChessBetBean> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final ChessBetBean item) {

            RelativeLayout itemView = holder.getView(R.id.item);
            TextView platformName = holder.getView(R.id.platform_type);
            TextView winState = holder.getView(R.id.winState);
            TextView touzhuFee = holder.getView(R.id.tz_fee);
            TextView timeTV = holder.getView(R.id.time);


            platformName.setText(String.format(getString(R.string.platform_type_format),
                    item.getPlatformType()));
            winState.setText("盈亏: " + (item.getWinMoney() - item.getBettingMoney()) + "元");
            if (item.getWinMoney() > 0) {
                winState.setTextColor(getResources().getColor(R.color.blue_color));
            } else {
                winState.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            touzhuFee.setText("投注金额: " + item.getBettingMoney());
            timeTV.setText(Utils.formatTime(item.getBettingTime()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String content = new Gson().toJson(item, ChessBetBean.class);
                    ChessOrderDetailActivity.createIntent(context, content);
                }
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
        if (isFinishing()) {
            return;
        }
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_CANCEL_ORDER_URL);
        configUrl.append("?orderId=").append(orderId).append("&");
        configUrl.append("lotCode=").append(lotCode);
        CrazyRequest<CrazyResult<CancelOrderWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(CANCEL_ORDER)
                .listener(this)
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

    private void updateSumMoney(double betMoney, double winMoney ,double valueMoney) {
        sumBetMoneyUI.setText(String.format("%.2f元", betMoney));
        if (valueMoney == -1){
            ll_value_bet.setVisibility(View.GONE);
        }else {
            ll_value_bet.setVisibility(View.VISIBLE);
        }
        valueBetMoneyUI.setText(String.format("%.2f元", valueMoney));
        sumWinMoneyUI.setText(String.format("%.2f元", winMoney - betMoney));
        sumzhongjiangMoneyUI.setText(String.format("%.2f元", winMoney));
    }


    @Override
    public void onResponse(SessionResponse<CrazyResult> response) {
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
                updateSumMoney(reg.getContent().getSumBuyMoney(), reg.getContent().getSumWinMoney(),-1);
                if (pageIndex == 1) {
                    if (listDatas == null) {
                        listDatas = new ArrayList<>();
                    } else {
                        listDatas.clear();
                    }
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
//                        listDatas = handleListResult(listDatas, datas, response.url, pageIndex == 1);
                        listDatas.addAll(reg.getContent().getResults());
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

                sportOrders.clear();
                if (!reg.getContent().isEmpty()) {
                    sportOrders.addAll(reg.getContent());
                }
                sportRecordAdapter.notifyDataSetChanged();
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
                    //超时或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (reg.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(this);
                    }
                    return;
                }
                YiboPreference.instance(this).setToken(reg.getAccessToken());
                newsportOrders.clear();
                if (!reg.getContent().getList().isEmpty()) {
                    newsportOrders.addAll(reg.getContent().getList());
                }
                newsportRecordAdapter.notifyDataSetChanged();
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

                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (reg.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(this);
                    }
                    return;
                }
                YiboPreference.instance(this).setToken(reg.getAccessToken());
                sbsportOrders.clear();
                if (reg.getContent() != null && !reg.getContent().getList().isEmpty()) {
                    sbsportOrders.addAll(reg.getContent().getList());
                }
                sbsportRecordAdapter.notifyDataSetChanged();
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
                    updateSumMoney(wraper.getSumBet(), wraper.getSumWin(),-1);
                    realGameResults.clear();
                    if (wraper.getData() != null) {
                        realGameResults.addAll(wraper.getData());
                    }
                    realGameRecordAdapter.notifyDataSetChanged();
                }
                break;
            }
            case CHESS_RECORD: {
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
                ChessBetResultWraper wraper = new Gson().fromJson(reg, ChessBetResultWraper.class);
                if (wraper != null) {
                    if (wraper.getAggsData() != null) {
                        updateSumMoney(wraper.getAggsData().getBettingMoneyCount(), wraper.getAggsData().getWinMoneyCount(),wraper.getAggsData().getRealBettingMoneyCount());
                    }
                    chessGameResults.clear();
                    if (wraper.getRows() != null && !wraper.getRows().isEmpty()) {
                        if (wraper.getRows() != null) {
                            chessGameResults.addAll(wraper.getRows());
                        }
                    }
                    chessGameRecordAdapter.notifyDataSetChanged();
                }
                break;
            }
            case THIRLD_SPORT: {
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
                ThirldSpordData wraper = new Gson().fromJson(reg, ThirldSpordData.class);
                if (wraper != null) {
                    if (wraper.getAggsData() != null) {
                        updateSumMoney(wraper.getAggsData().getBettingMoneyCount(), wraper.getAggsData().getWinMoneyCount(),wraper.getAggsData().getRealBettingMoneyCount());
                    }
                    ThirldSports.clear();
                    if (wraper.getRows() != null && !wraper.getRows().isEmpty()) {
                        if (wraper.getRows() != null) {
                            ThirldSports.addAll(wraper.getRows());
                        }
                    }
                    newThirldSportRecordAdapter.notifyDataSetChanged();
                }
                break;
            }
            case HUNTER_GAME: {
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
                HunterGameData wraper = new Gson().fromJson(reg, HunterGameData.class);
                if (wraper != null) {
                    if (wraper.getAggsData() != null) {
                        updateSumMoney(wraper.getAggsData().getBettingMoneyCount(), wraper.getAggsData().getWinMoneyCount(),wraper.getAggsData().getRealBettingMoneyCount());
                    }
                    HunterGames.clear();
                    if (wraper.getRows() != null && !wraper.getRows().isEmpty()) {
                        if (wraper.getRows() != null) {
                            HunterGames.addAll(wraper.getRows());
                        }
                    }
                    hunterGameAdapter.notifyDataSetChanged();
                }
                break;
            }
            case ESPORT :{
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
                EsportDataKt wraper = new Gson().fromJson(reg, EsportDataKt.class);
                if (wraper != null) {
                    if (wraper.getAggsData() != null) {
                        updateSumMoney(wraper.getAggsData().getBettingMoneyCount(), wraper.getAggsData().getWinMoneyCount(),wraper.getAggsData().getRealBettingMoneyCount());
                    }
                    esportDataRows.clear();
                    if (wraper.getRows() != null && !wraper.getRows().isEmpty()) {
                        if (wraper.getRows() != null) {
                            esportDataRows.addAll(wraper.getRows());
                        }
                    }
                    esportRecordAdapter.notifyDataSetChanged();
                }
                break;
            }
            case BIG_PAN: {
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
                BigPanBean wraper = new Gson().fromJson(reg, BigPanBean.class);
                if (wraper != null) {
                    BigPanBean.ContentBean bean = wraper.getContent();
                    if (bean == null)
                        return;
                    if (pageIndex == 1) {
                        bigpanResults.clear();
                    }
                    if (bean.getList() != null && !bean.getList().isEmpty()) {
                        if (bean.getList() != null) {
                            if (bean.isHasNext()) {
                                pageIndex++;
                            }
                            bigpanResults.addAll(bean.getList());
                        }
                    }
                    bigPanRecordAdapter.notifyDataSetChanged();
                }
                break;
            }

        }
    }

}
