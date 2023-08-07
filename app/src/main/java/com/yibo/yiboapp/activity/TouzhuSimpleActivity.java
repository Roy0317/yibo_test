package com.yibo.yiboapp.activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.anuo.immodule.activity.ChatMainActivity;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.Event.WebChatPhotoEvent;
import com.yibo.yiboapp.data.CacheRepository;
import com.yibo.yiboapp.entify.BoundsBean;
import com.yibo.yiboapp.entify.LotteryDownBean;
import com.yibo.yiboapp.entify.LotteryLast;
import com.yibo.yiboapp.entify.MemberHeaderWraper;
import com.yibo.yiboapp.entify.NoticeResult;
import com.yibo.yiboapp.entify.NoticeResultWraper;
import com.yibo.yiboapp.fragment.JiangjinSimpleFragment;
import com.yibo.yiboapp.fragment.PeilvSimpleFragment;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.NumbersScroolAdapter;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.DatabaseUtils;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.PeilvPlayData;
import com.yibo.yiboapp.data.PlayCodeConstants;
import com.yibo.yiboapp.data.UpdateListener;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BetToken;
import com.yibo.yiboapp.entify.CountDown;
import com.yibo.yiboapp.entify.LhcServerTimeWrapper;
import com.yibo.yiboapp.entify.LocCountDownWraper;
import com.yibo.yiboapp.entify.LocPlaysWraper;
import com.yibo.yiboapp.entify.MemInfoWraper;
import com.yibo.yiboapp.entify.Meminfo;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.entify.PeilvWebResult;
import com.yibo.yiboapp.entify.PeilvWebResultWraper;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SavedGameData;
import com.yibo.yiboapp.entify.SubPlayItem;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.WinLostWraper;
import com.yibo.yiboapp.interfaces.CurrentLotteryDataChangeListener;
import com.yibo.yiboapp.interfaces.PeilvListener;
import com.yibo.yiboapp.interfaces.RuleSelectCallback;
import com.yibo.yiboapp.interfaces.SimpleClearListener;
import com.yibo.yiboapp.interfaces.SixMarkPeilvListener;
import com.yibo.yiboapp.manager.BankingManager;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.network.RetrofitFactory;
import com.yibo.yiboapp.ui.PopupListMenu;
import com.yibo.yiboapp.ui.SimpleCaizhongWindow;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.ChatRoomUtils;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.utils.WindowUtils;
import com.yibo.yiboapp.views.NoticeDialog;
import com.yibo.yiboapp.views.WebChatRoomDialog;
import com.yibo.yiboapp.views.floatball.FloatBallManager;
import com.yibo.yiboapp.views.floatball.floatball.FloatBallCfg;
import com.yibo.yiboapp.widget.WheelView;
import com.yibo.yiboapp.views.InterceptionLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.CustomDialogManager;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:Ray
 * Create:2018年12月03日14:29:38
 * Description:
 * 投注页面简约版本
 * 赔率版本（信用） PeilvSimpleFragment
 * 奖金版本（官方） JiangjinSimpleFragment
 */

public class TouzhuSimpleActivity extends AppCompatBaseActivity implements UpdateListener,
        SessionResponse.Listener<CrazyResult<Object>> {

    private static final String TAG = "TouzhuSimpleActivity";
    SysConfig config = UsualMethod.getConfigFromJson(this);

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, TouzhuSimpleActivity.class);
        context.startActivity(intent);
    }

    public static void createIntent(Context context, String lotteryJson, String gameCode,
                                    boolean isPeilvVersion, String cpVerison) {
        createIntent(context, lotteryJson, gameCode, false, isPeilvVersion, cpVerison);
    }

    public static void createIntent(Context context, String lotteryJson, String gameCode,
                                    boolean needRefresh, boolean isPeilvVersion, String cpVerison) {
        Intent intent = new Intent(context, TouzhuSimpleActivity.class);
        intent.putExtra("lottery", lotteryJson);
        intent.putExtra("gameCode", gameCode);
        intent.putExtra("needRefresh", needRefresh);
        intent.putExtra("isPeilvVersion", isPeilvVersion);
        intent.putExtra("cpVersion", cpVerison);
        context.startActivity(intent);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (numberGridView == null) return;
            if (msg.what == 0x123) {
                if ((czCode.equals("6") || czCode.equals("66") || czCode.equals("666"))) {
                    updateGridview(lunarYear);
                } else {
                    updateGridview(-1);
                }
                return;
            }
            if (numberGridView.getChildAt(msg.what) == null) return;
            if (numberGridView.getChildAt(msg.what) != null) {
                WheelView wheelView = (WheelView) numberGridView.getChildAt(msg.what).findViewById(R.id.scollball);
                wheelView.scroll(180, 20000);

            }
        }
    };


    List<String> haoMaList;
    String lotType;
    public static final int LAST_RESULT_REQUEST = 0x01;
    public static final int COUNT_DOWN_REQUEST = 0x02;
    public static final int ACQUIRE_PLAYS_REQUEST = 0x05;
    public static final int JIANJIN_TOKEN_BETS_REQUEST = 0x07;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 0x08;
    public static final int WIN_LOST_REQUEST_CODE = 0x09;
    public static final int ACCOUNT_REQUEST = 0x10;
    public static final int GET_HEADER = 0x11;


    /**
     * 赔率版本Fragment
     */
    private PeilvSimpleFragment peilvSimpleFragment;
    /**
     * 奖金版本Fragment
     */
    private JiangjinSimpleFragment jiangjinSimpleFragment;

    private TextView balanceTV;
    /**
     * 上一期开奖期号
     */
    private TextView kjResult;
    /**
     * 最新一期六合彩开奖时的农历年
     */
    private int lunarYear;
    /**
     * 开奖结果不正常时的文字显示
     */
    private TextView unNormalTV;
    /**
     * 开奖结果号码列表
     */
    private GridView numberGridView;

    /**
     * 用户名称
     */
    private TextView tv_account_name;

    private TextView playText;

    /**
     * 标记tab的选中状态，方便设置
     */
    private boolean tabStateArr = false;

    /**
     * 屏幕的宽高
     */
    public static int screen_width = 0;
    public static int screen_height = 0;
    private PopupWindow playPopupWindow = null;


    private String selectPlayCode = "01";
    private String selectSubPlayCode = "1";
    private String selectPlayName = "";
    private String selectSubPlayName = "";


    /**
     * 最小奖金
     */
    private float minBounds;
    /**
     * 最大奖金
     */
    private float maxBounds;

    /**
     * 最多投注号码个数
     */
    private int maxBetNumber;

    /**
     * 最小返水
     */
    private float minRakeback;
    private String winExample = "";
    private String detailDesc = "";
    private String playMethod = "";
    /**
     * 当前期号
     */
    private String currentQihao;
    /**
     * 彩票版本
     */
    private String cpVersion;

    /**
     * 是否赔率版
     */
    private boolean isPeilvVersion;
    /**
     * 是否在奖金版本中选中了六合彩，十分六合彩
     */
    private boolean lhcSelect;

    private FloatBallManager mFloatballManager;

    private List<PlayItem> playRules = new ArrayList<>();//彩票玩法
    private LotteryData currentLotteryData;
    private String czCode;//彩票类型代号
    private String cpCode;//彩票编码
    private String cpName;//彩票名称
    private long cpDuration;//彩票开奖间隔时间
    private int ago;//开奖时间与封盘时间差,单位秒
    private CountDownTimer endlineTouzhuTimer;
    private CountDownTimer lastResultAskTimer;//查询最后开奖结果的倒计时器
    private TextView tv_play_text_head;
    SimpleDraweeView iv_image;
    private String leftMoneyName;
    private String accountName;
    private boolean Firstinto = true;
    private SoundPool soundPool;
    int music;
    String lastOpenResult = "";//上一期开奖结果
    private boolean isOnOriginalChat;

    public static final int SYNC_LHC_TIME = 0x100;
    public static final int GET_LOTTERY_NOTICE = 0x200;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.touzhu_simple_layout);
        isPeilvVersion = getIntent().getBooleanExtra("isPeilvVersion", false);
        cpVersion = getIntent().getStringExtra("cpVersion");
        isOnOriginalChat = Utils.onOrOffOriginalChat(this);
        if (Utils.isEmptyString(cpVersion)) {
            cpVersion = YiboPreference.instance(this).getGameVersion();
        }
        initView();
        final String lottery = getIntent().getStringExtra("lottery");
        String gameCode = getIntent().getStringExtra("gameCode");
        boolean needRefresh = getIntent().getBooleanExtra("needRefresh", false);

        if (!Utils.isEmptyString(lottery)) {
            initOnGetLotteryJson(lottery);
        }
        if (!TextUtils.isEmpty(gameCode) && needRefresh) {
            UsualMethod.syncLotteryPlaysByCode(TouzhuSimpleActivity.this, gameCode,
                    false, ACQUIRE_PLAYS_REQUEST, TouzhuSimpleActivity.this);
        }
    }

    private void initOnGetLotteryJson(String lottery) {
        LotteryData lotteryData = new Gson().fromJson(lottery, LotteryData.class);
        currentLotteryData = lotteryData;
        updateLotteryConstants(lotteryData);
        //初始化大小玩法显示栏
        if (playRules == null || playRules.isEmpty()) {
            showToast("没有玩法，请检查平台是否开启玩法");
            return;
        }
        PlayItem defaultPlayItem = playRules.get(0);
        if (defaultPlayItem != null) {
            selectPlayCode = defaultPlayItem.getCode();
            selectPlayName = defaultPlayItem.getName();
            List<SubPlayItem> subRules = defaultPlayItem.getRules();
            if (subRules != null && !subRules.isEmpty()) {
                maxBetNumber = subRules.get(0).getMaxBetNum();
                selectSubPlayCode = subRules.get(0).getCode();
                selectSubPlayName = subRules.get(0).getName();
                firstFetchPlayID(subRules);
            }
        }
    }

    private void firstFetchPlayID(final List<SubPlayItem> subRules) {
        CacheRepository.getInstance().loadLotteryPlayInfoJson(this, String.valueOf(subRules.get(0).getPalyId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(String json) {
                        Utils.LOG(TAG, "find the backup of play info");
                        BoundsBean boundsBean = new Gson().fromJson(json, BoundsBean.class);
                        minBounds = (float) boundsBean.getData().getBonusOdds();
//                        initFragments(rules);
                        firstFetchRemotePlayID(subRules, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.LOG(TAG, "can't find the backup of play info");
                        e.printStackTrace();
                        firstFetchRemotePlayID(subRules, true);
                    }
                });
    }

    private void firstFetchRemotePlayID(final List<SubPlayItem> rules, boolean showLoading) {
        ApiParams params = new ApiParams();
        params.put("playId", rules.get(0).getPalyId());
        HttpUtil.get(TouzhuSimpleActivity.this, Urls.GET_PLAY_INFO, params, showLoading, 60 * 60 * 1000 * 24, result -> {
            if (result.isSuccess()) {
                CacheRepository.getInstance().savePlayInfo(getApplicationContext(), rules.get(0).getPalyId(), result.getContent());
                BoundsBean boundsBean = new Gson().fromJson(result.getContent(), BoundsBean.class);
                minBounds = (float) boundsBean.getData().getBonusOdds();
            } else {
                minBounds = rules.get(0).getMinBonusOdds();
            }
            initFragments(rules);
        });
    }

    private void againFetchPlayID(PlayItem playItem, SubPlayItem item, long playId) {
        CacheRepository.getInstance().loadLotteryPlayInfoJson(this, String.valueOf(playId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(String json) {
                        Utils.LOG(TAG, "find the backup of play info");
                        BoundsBean boundsBean = new Gson().fromJson(json, BoundsBean.class);
                        minBounds = (float) boundsBean.getData().getBonusOdds();
                        refreshFragments(playItem, item);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Utils.LOG(TAG, "can't find the backup of play info");
                        againFetchRemotePlayID(playItem, item, playId);
                    }
                });
    }

    private void againFetchRemotePlayID(PlayItem playItem, SubPlayItem item, long playId) {
        ApiParams params = new ApiParams();
        params.put("playId", playId);
        HttpUtil.get(TouzhuSimpleActivity.this, Urls.GET_PLAY_INFO, params, false, "加载中...", 24 * 60 * 60 * 1000, result -> {
            if (result.isSuccess()) {
                CacheRepository.getInstance().savePlayInfo(TouzhuSimpleActivity.this, playId, result.getContent());
                BoundsBean boundsBean = new Gson().fromJson(result.getContent(), BoundsBean.class);
                minBounds = (float) boundsBean.getData().getBonusOdds();
            } else {
                minBounds = item.getMinBonusOdds();
            }
            refreshFragments(playItem, item);
        });
    }

    private void initFragments(List<SubPlayItem> rules) {
        maxBounds = rules.get(0).getMaxBounsOdds();
        minRakeback = rules.get(0).getMinRakeback();
        winExample = rules.get(0).getWinExample();
        detailDesc = rules.get(0).getDetailDesc();
        playMethod = rules.get(0).getPlayMethod();

        soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
        music = soundPool.load(this, R.raw.scroolsound, 1);
        initPlaySeletor();
        if (/*UsualMethod.isPeilvVersion(this)*/isPeilvVersion) {
            peilvSimpleFragment = new PeilvSimpleFragment();
            peilvSimpleFragment.setCpBianHao(cpCode);
            peilvSimpleFragment.setPeilvListener(new TouzhuPeilvListener());
            androidx.fragment.app.FragmentManager fm = this.getSupportFragmentManager();
            androidx.fragment.app.FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, peilvSimpleFragment);
            ft.commit();

            startProgress();
            getKaiJianResult(cpCode);
            UsualMethod.getCountDownByCpcode(TouzhuSimpleActivity.this, cpCode, COUNT_DOWN_REQUEST);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    peilvSimpleFragment.onPlayRuleSelected(cpVersion,
                            czCode, selectPlayCode, selectSubPlayCode,
                            selectPlayName, selectSubPlayName, maxBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                }
            }, 500);

        } else {
            jiangjinSimpleFragment = new JiangjinSimpleFragment();
            jiangjinSimpleFragment.setBetListener(new TouzhuJianjinListener());
            jiangjinSimpleFragment.setCpBianHao(cpCode);
            FragmentManager fm = this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, jiangjinSimpleFragment);
            ft.commit();

            startProgress();
            getKaiJianResult(cpCode);//获取开奖结果
            UsualMethod.getCountDownByCpcode(TouzhuSimpleActivity.this, cpCode, COUNT_DOWN_REQUEST);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    jiangjinSimpleFragment.onPlayRuleSelected(/*YiboPreference.instance(TouzhuActivity.this)
                                    .getGameVersion()*/cpVersion, czCode, selectPlayCode, selectSubPlayCode,
                            selectPlayName, selectSubPlayName, minBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                }
            }, 500);
        }
        //首次使用时，提示引导摇一摇投注功能
        //将玩法显示在左下方
        refreshPlayRuleExpandListView();
        startSyncLhcServerTime();
    }

    private void refreshFragments(PlayItem playItem, SubPlayItem item) {
        selectPlayCode = playItem.getCode();
        selectPlayName = playItem.getName();
        maxBetNumber = item.getMaxBetNum();
        selectSubPlayCode = item.getCode();
        selectSubPlayName = item.getName();
        maxBounds = item.getMaxBounsOdds();
        minRakeback = item.getMinRakeback();
        winExample = item.getWinExample();
        detailDesc = item.getDetailDesc();
        playMethod = item.getPlayMethod();
        //更新玩法栏内容及状态

        //如果大玩法下中有一项小玩法，说明点击的是大玩法。需要将小玩法名称用大玩法代替
        String palyNameShowInView = selectSubPlayName;
        if (playItem.getRules().size() == 1) {
            palyNameShowInView = selectPlayName;
        }

        //重新刷新投注面板
        if (isPeilvVersion || lhcSelect) {
            startSyncLhcServerTime();
            peilvSimpleFragment.onPlayRuleSelected(lhcSelect ? String.valueOf(Constant.lottery_identify_V2) :
                            cpVersion, czCode, selectPlayCode, selectSubPlayCode,
                    selectPlayName, palyNameShowInView, maxBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
        } else {
            jiangjinSimpleFragment.onPlayRuleSelected(cpVersion, czCode, selectPlayCode, selectSubPlayCode,
                    selectPlayName, selectSubPlayName, minBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
        }
    }

    /**
     * 同步六合彩服务器当前时间
     */
    public void startSyncLhcServerTime() {
        if (!UsualMethod.isXGSixMark(this.cpCode)) {
            return;
        }
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.get_server_bettime_for_lhc);
        configUrl.append("?lotCode=").append(cpCode);
        CrazyRequest<CrazyResult<LhcServerTimeWrapper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(SYNC_LHC_TIME)
                .headers(Urls.getHeader(this))
                .cachePeroid(30 * 1000)
                .listener(this)
                .shouldCache(false).placeholderText(getString(R.string.get_peilv_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LhcServerTimeWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    @Override
    protected void initView() {
        super.initView();
        //小助手
        tvRightText.setText(getString(R.string.little_assistant));
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setOnClickListener(this);
        tvSecondTitle.setVisibility(View.GONE);
//        rightIcon.setVisibility(isPeilvVersion?View.GONE:View.VISIBLE);
        titleLayout.setOnClickListener(this);
        tvMiddleTitle.setOnClickListener(this);
        titleIndictor.setVisibility(View.VISIBLE);
        iv_image = (SimpleDraweeView) findViewById(R.id.iv_image);
        balanceTV = (TextView) findViewById(R.id.balance);
        playText = (TextView) findViewById(R.id.playname);
        tv_play_text_head = (TextView) findViewById(R.id.tv_play_text_head);
        tv_account_name = (TextView) findViewById(R.id.tv_account_name);
        iv_image.setOnClickListener(this);
        findViewById(R.id.btn_recharge).setOnClickListener(this);
        findViewById(R.id.btn_withdraw).setOnClickListener(this);
        /**
         * 开奖结果面板view
         */
        InterceptionLinearLayout openLayout = (InterceptionLinearLayout) findViewById(R.id.notice_layout);
        openLayout.setOnClickListener(this);
        kjResult = (TextView) findViewById(R.id.kaijian_result);
        unNormalTV = (TextView) findViewById(R.id.unnormal_open_result);
        numberGridView = (GridView) findViewById(R.id.open_numbers);
        /**
         * 初始化悬浮球
         */
        if (UsualMethod.getConfigFromJson(this) != null &&
                UsualMethod.getConfigFromJson(this).getOnoff_chat().equalsIgnoreCase("on") && UsualMethod.getConfigFromJson(this).getChat_icon_in_betpage_switch().equals("on")) {
            initFloatBall();
        }

        if (Utils.isEmptyString(YiboPreference.instance(this).getUserHeader()))
            UsualMethod.syncHeader(this, GET_HEADER, false, this);
        else
            UsualMethod.LoadUserImage(this, iv_image);

        //获取彩票公告
        getLotteryNotice();
    }

    private void getLotteryNotice() {
        StringBuilder popUrl = new StringBuilder();
        popUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.NOTICE_URL_V2);
        popUrl.append("?code=9");
        CrazyRequest<CrazyResult<NoticeResultWraper>> popRequest = new AbstractCrazyRequest.Builder().
                url(popUrl.toString())
                .seqnumber(GET_LOTTERY_NOTICE)
                .headers(Urls.getHeader(this))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<NoticeResultWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, popRequest);
    }

    public ChatRoomUtils chatRoomUtils; //主页的聊天室方法

    /**
     * 初始化悬浮球
     */
    private void initFloatBall() {
        //初始化悬浮球配置，定义好悬浮球大小和icon的drawable
        FloatBallCfg ballCfg = new FloatBallCfg(this, ConvertUtils.dp2px(60),
                ContextCompat.getDrawable(this, R.drawable.icon_chat_room_mormal), FloatBallCfg.Gravity.RIGHT_CENTER);
        //设置悬浮球不半隐藏
        ballCfg.setHideHalfLater(false);
        mFloatballManager = new FloatBallManager(this, ballCfg);

        mFloatballManager.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
            @Override
            public void onFloatBallClick() {
                if (isOnOriginalChat) {
                    //TODO 进入原生聊天室授权页面
                    if (UsualMethod.checkIsLogin(TouzhuSimpleActivity.this)) {
                        Intent intent = new Intent(TouzhuSimpleActivity.this, ChatMainActivity.class);
                        intent.putExtra("lotteryVersion1", Constant.VERSION_1);
                        intent.putExtra("lotteryVersion2", Constant.VERSION_2);
                        intent.putExtra("baseUrl", Urls.BASE_URL);
                        //将主系统的域名，线路方式，host,native-flag传入聊天室
                        intent = fillIntent(intent);
//                        intent.putExtra("isModule", true);
                        startActivity(intent);
                    }
                } else {
                    //读取聊天室链接
                    if (!YiboPreference.instance(TouzhuSimpleActivity.this).isLogin()) {
                        if (config != null && config.getNewmainpage_switch().equals("on")) {
                            LoginAndRegisterActivity.createIntent(TouzhuSimpleActivity.this, YiboPreference.instance(getApplicationContext()).getUsername(), "", 0);
                        }else{
                            LoginActivity.createIntent(TouzhuSimpleActivity.this, YiboPreference.instance(getApplicationContext()).getUsername(), "");
                        }
                        return;
                    }
                    if (config != null && !TextUtils.isEmpty(config.getChat_foreign_link())) {
                        UsualMethod.viewLink(TouzhuSimpleActivity.this, config.getChat_foreign_link().trim());
                        return;
                    }
                    if (chatRoomUtils == null) {
                        chatRoomUtils = new ChatRoomUtils(TouzhuSimpleActivity.this);
                    } else {
                        chatRoomUtils.showDialog();
                    }
                }
            }

            @Override
            public void onChildClick(String str, int postion) {

            }
        });
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //只有activity被添加到windowmanager上以后才可以调用show方法。
        if (mFloatballManager != null) {
            mFloatballManager.show();
        }

    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mFloatballManager != null) {
            mFloatballManager.hide();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        accountWeb();
    }

    @Override
    protected void onStart() {
        super.onStart();
        createOpenResultTimer();
    }

    private void syncWinlost(boolean showDialog) {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.WIN_LOST_URL);
        CrazyRequest<CrazyResult<WinLostWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(WIN_LOST_REQUEST_CODE)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .placeholderText(getString(R.string.loading))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<WinLostWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    public void accountWeb() {
        Utils.LOG(TAG, "acount web ------");
        StringBuilder accountUrls = new StringBuilder();
        accountUrls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.MEMINFO_URL);
        CrazyRequest<CrazyResult<MemInfoWraper>> accountRequest = new AbstractCrazyRequest.Builder().
                url(accountUrls.toString())
                .seqnumber(ACCOUNT_REQUEST)
                .listener(this)
                .headers(Urls.getHeader(this))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MemInfoWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, accountRequest);
    }

    private void refreshPlayRuleExpandListView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isPeilvVersion || lhcSelect) {
                    peilvSimpleFragment.setRuleSelectCallback(new RuleSelectListener());
                    peilvSimpleFragment.updatePlayRules(playRules);
                } else {
                    jiangjinSimpleFragment.setRuleSelectCallback(new RuleSelectListener());
                    jiangjinSimpleFragment.updatePlayRules(playRules);
                }
            }
        }, 200);
    }

    private final class RuleSelectListener implements RuleSelectCallback {
        @Override
        public void onRuleCallback(PlayItem playItem, SubPlayItem item, long playId) {
            if (playItem == null || item == null) {
                return;
            }
            //若此玩法在后台配置中处于关闭状态，则直接返回U
            if (!playItem.isOpenStatus()) {
                showToast(R.string.play_disable_from_web);
                return;
            }

            againFetchPlayID(playItem, item, playId);
        }
    }


    private final class TouzhuJianjinListener implements JiangjinSimpleFragment.BetListener {

        @Override
        public void onBetPost() {
            actionJianjinTouzhu();
        }
    }

    private final class TouzhuPeilvListener implements PeilvListener {
        @Override
        public void onPeilvAcquire(String playCode, boolean showDialog) {
            getPeilvData(czCode, playCode, false);
        }

        @Override
        public void onBetPost(List<PeilvPlayData> selectDatas, boolean isMulSelect, String money, int count, double totalMoney, String playRule, String winMoney) {
            if (UsualMethod.getConfigFromJson(TouzhuSimpleActivity.this).getConfirm_dialog_before_bet().equalsIgnoreCase("on")) {
                showConfirmBetDialog(selectDatas, isMulSelect, money, count, totalMoney, playRule, winMoney);
            } else {
                realPeilvPostBets(selectDatas, isMulSelect, money);
            }
        }
    }


    //奖金版投注动作
    private void actionJianjinTouzhu() {
        CrazyRequest tokenRequest = UsualMethod.buildBetsTokenRequest(this, JIANJIN_TOKEN_BETS_REQUEST);
        RequestManager.getInstance().startRequest(this, tokenRequest);
    }

    private void updateLotteryConstants(LotteryData lotteryData) {
        if (lotteryData == null) {
            return;
        }
        playRules = lotteryData.getRules();
        czCode = lotteryData.getCzCode();
        cpCode = lotteryData.getCode();
        cpName = lotteryData.getName();
        cpDuration = lotteryData.getDuration();
        ago = lotteryData.getAgo() != null ? lotteryData.getAgo().intValue() : 0;
        LogUtils.e("ago:" + ago);
        tvMiddleTitle.setText(lotteryData.getName());
        Firstinto = true;
    }

    /**
     * 开始获取最近开奖结果
     *
     * @param bianHao 彩种编码
     */
    private void getKaiJianResult(String bianHao) {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_LAST_RESULT_V2_URL);
        configUrl.append("?lotCode=").append(bianHao);
        configUrl.append("&version=").append(YiboPreference.instance(this).getGameVersion());
        CrazyRequest<CrazyResult<LotteryDownBean>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(LAST_RESULT_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LotteryDownBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    private void initPlaySeletor() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;

        int[] location = new int[2];
        Animation animation = new TranslateAnimation(0, 0, location[1], location[1]);
        animation.setDuration(500);
    }


    /**
     * 创建查询开奖结果倒计时
     *
     * @param duration
     */
    private void createLastResultTimer(final long duration) {
        lastResultAskTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Utils.LOG(TAG, "ago aaaa:" + duration);
                createLastResultTimer(duration);
                lastResultAskTimer.start();
                Utils.LOG(TAG, "开始获取上一期开奖结果-----------");
                getKaiJianResult(cpCode);
            }
        };
    }

    /**
     * 创建离结束投注倒计时
     *
     * @param duration     离当前期号投注结束的时长，毫秒级
     * @param currentQihao 当前期号
     */
    private void createEndlineTouzhuTimer(final long duration, final String currentQihao) {
        endlineTouzhuTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                //第%1$s期截止时间:
                tv_play_text_head.setText(String.format(getString(R.string.kaijian_deadline_format_time),
                        String.valueOf(currentQihao)));

                //截止时间
                playText.setText(Utils.int2Time(millisUntilFinished));

            }

            public void onFinish() {
                playText.setText(getString(R.string.stop_touzhu));
                endlineTouzhuTimer = null;//置空当前期号倒计时器
                boolean warmRemind = YiboPreference.instance(TouzhuSimpleActivity.this)
                        .isWarmRemind();
                if (warmRemind) {
                    //当前期数投注时间到时，继续请求同步服务器上下一期号及离投注结束倒计时时间
//                    showToast(R.string.sync_next_qihao_dataing);
//                    弹框提示当前期号投注结束
                    showToastTouzhuEndlineDialog(currentQihao);
                }
                startProgress();
                UsualMethod.getCountDownByCpcode(TouzhuSimpleActivity.this, cpCode, COUNT_DOWN_REQUEST);
                //截止下注后再过一段开封盘时间ago，获取一次开奖结果
                Utils.LOG(TAG, "ago ===== " + ago);
                //截止下注时先获取一次开奖结果
                getKaiJianResult(cpCode);
            }
        };
    }

    private void createOpenResultTimer() {
        //开始定时请求第前期的开奖结果
        if (lastResultAskTimer != null) {
            lastResultAskTimer.cancel();
            lastResultAskTimer = null;
        }
        createLastResultTimer(5 * 1000);
        lastResultAskTimer.start();
    }

    /**
     * 上一期的开奖号码
     *
     * @param currentHaoMa 返回当前开奖结果是否与上一期的相同
     */
    private void handleAcqureLastResult(String currentHaoMa) {
        //奖金版时需要是截止下注后定期去获取一次最近那一期的开奖结果；因为可能刚截止还不能获取到开奖号码
//        if (!isPeilvVersion && !lhcSelect) {
        boolean isEffective = true;
        if (!Utils.isEmptyString(currentHaoMa) && currentHaoMa.contains(",")) {
            List<String> haomas1 = Utils.splitString(currentHaoMa, ",");
            for (String hm : haomas1) {
                if (!Utils.isNumeric(hm) || hm.equalsIgnoreCase("?") || lastOpenResult.equalsIgnoreCase(currentHaoMa)) {
//                    if ((currentHaoMa.equalsIgnoreCase(lastOpenResult)) ||
//                            currentHaoMa.contains("?") || Utils.isEmptyString(lastOpenResult)) {
//                        //开始定时请求第前期的开奖结果
//                        if (lastResultAskTimer != null) {
//                            lastResultAskTimer.cancel();
//                            lastResultAskTimer = null;
//                        }
//                        int ago_offset = 6;
//                        Utils.LOG(TAG, "ask open result timely..........");
//                        createLastResultTimer((ago_offset) * 1000);
//                        lastResultAskTimer.start();
//                    }
                    isEffective = false;
                }
            }
        }
//        }
        // 先取消掉现有的计时器
        if (lastResultAskTimer != null) {
            lastResultAskTimer.cancel();
            lastResultAskTimer = null;
        }
        if (!isEffective) {
            //如果结果是无效结果，需要重新开启定时器获取结果
            int ago_offset = 6;
            Utils.LOG(TAG, "ask open result timely..........");
            createLastResultTimer((ago_offset) * 1000);
            lastResultAskTimer.start();
        }
        lastOpenResult = currentHaoMa;
    }

    private void setFengPan(boolean isFengPan) {
        if (peilvSimpleFragment != null) {
            peilvSimpleFragment.setFengPan(isFengPan);
        }
        if (jiangjinSimpleFragment != null) {
            jiangjinSimpleFragment.setFengPan(isFengPan);
        }
    }

    private Disposable disposable;


    /**
     * @param duration 剩余封盘时间段，单位秒
     */
    public void setFengPaningOperation(final int duration) {
        //如果封盘之后无法下注
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(duration + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) {
                        return duration - aLong;
                    }
                })
                //封盘过程中
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        //执行过程中为封盘状态
                        //执行过程中下一期为封盘状态
                        tv_play_text_head.setTextColor(Color.RED);
                        tv_play_text_head.setText(String.format(getString(R.string.kaijian_deadline_format_open_time),
                                String.valueOf(Long.parseLong(currentQihao) + 1)));
                        setFengPan(true);
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        //截止时间
                        playText.setText(Utils.int2Time(aLong * 1000));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //执行结束之后为不封盘状态
                        setFengPan(false);
                        tv_play_text_head.setTextColor(getResources().getColor(R.color.black_text_color));
                        //封盘时间倒计时结束后，先请求一次开奖结果
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Utils.LOG(TAG, "get result after fenpang -------");
                                getKaiJianResult(cpCode);
                            }
                        }, 2000);
                        startProgress();
                        UsualMethod.getCountDownByCpcode(TouzhuSimpleActivity.this, cpCode, COUNT_DOWN_REQUEST);
                    }
                });
    }

    /**
     * 弹框投注时间已到
     *
     * @param currentQihao
     */
    CustomConfirmDialog ccd;

    private void showToastTouzhuEndlineDialog(String currentQihao) {

        //如果当前activity不在栈顶
//
//        System.out.println("当前Activity是否在栈顶:" +  Utils.isActivityOnTop(this));
//        System.out.println("当前栈顶的Activity:"+ActivityUtils.getTopActivity().getLocalClassName());
//        System.out.println("当前页面的名称:"+TouzhuSimpleActivity.this.getLocalClassName());

//        System.out.println("当前activity是否在前台："+Utils.isForeground(this));

//        if (!Utils.isActivityOnTop(this)) {
//            return;
//        }

        //判断当前activity如果不在前台
        if (!Utils.isForeground(TouzhuSimpleActivity.this)) {
            return;
        }

        if (ccd != null) {
            ccd.dismiss();
        }
        ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(1);
        String content = String.format(getString(R.string.touzhu_toast_when_timeup), currentQihao);
        ccd.setContent(content);
        ccd.setToastShow(true);
        ccd.setMiddleBtnText("好的");
        ccd.setToastBtnText("不再提示");
        ccd.setOnToastBtnClick(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                boolean hasAsk = YiboPreference.instance(TouzhuSimpleActivity.this).isWarmRemind();
                YiboPreference.instance(TouzhuSimpleActivity.this).setWarmRemind(!hasAsk);
            }
        });
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
//                startProgress();
//                UsualMethod.getCountDownByCpcode(TouzhuSimpleActivity.this, cpBianHao, COUNT_DOWN_REQUEST);
            }
        });
        ccd.setCanceledOnTouchOutside(false);
        ccd.setCancelable(false);
        ccd.createDialog();
    }


    private void updatePlayMenuState() {
        tabStateArr = !tabStateArr;
    }


    @Override
    public void onBottomUpdate(int zhushuOrOrderNum, double totalMoney) {
        if (isPeilvVersion || lhcSelect) {
            if (peilvSimpleFragment == null) {
                return;
            }
            peilvSimpleFragment.updateBottom(zhushuOrOrderNum, totalMoney);
        } else {
            if (jiangjinSimpleFragment == null) {
                return;
            }
            jiangjinSimpleFragment.updateBottom(zhushuOrOrderNum, totalMoney, minBounds);
        }
    }

    @Override
    public void onClearView() {
        clearZhushuBottonView();
    }

    @Override
    public void onCartUpdate() {
        iconCount.setText(String.valueOf(DatabaseUtils.getInstance(this).getOrderCount()));
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == LAST_RESULT_REQUEST) {
            CrazyResult<Object> result = response.result;
            unNormalTV.setText("");
            //显示文字
            unNormalTV.setVisibility(View.VISIBLE);
            //隐藏彩种
            numberGridView.setVisibility(View.GONE);

            if (result == null) {
                showToast(getString(R.string.acquire_last_result_fail));
                //更新开奖结果
                kjResult.setText("第?????期：暂无开奖结果");
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.acquire_last_result_fail));
                //更新开奖结果
                kjResult.setText("第?????期：暂无开奖结果");
                return;
            }
            Object regResult = result.result;
            LotteryDownBean reg = (LotteryDownBean) regResult;
            Utils.LOG(TAG, "update resu reg == " + new Gson().toJson(reg, LotteryDownBean.class));
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : getString(R.string.get_open_results_fail));
                //更新开奖结果
                kjResult.setText("第?????期：暂无开奖结果");
                return;
            }
            //更新开奖结果
            lunarYear = reg.getLast().getYear();
            updateLastResult(reg.getLast(), reg.getBalance());
        } else if (action == COUNT_DOWN_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.acquire_current_qihao_fail));
                currentQihao = "???????";
                if (endlineTouzhuTimer != null) {
                    endlineTouzhuTimer.cancel();
                    endlineTouzhuTimer = null;
                }
                playText.setText("停止下注");
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.acquire_current_qihao_fail));
                currentQihao = "???????";
                if (endlineTouzhuTimer != null) {
                    endlineTouzhuTimer.cancel();
                    endlineTouzhuTimer = null;
                }
                playText.setText(("停止下注"));
                return;
            }
            Object regResult = result.result;
            LocCountDownWraper reg = (LocCountDownWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_current_qihao_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                currentQihao = "???????";
                if (endlineTouzhuTimer != null) {
                    endlineTouzhuTimer.cancel();
                    endlineTouzhuTimer = null;
                }
                playText.setText(("停止下注"));
                return;
            }


            if (reg.isSuccess()) {
                CountDown content = reg.getContent();
                if (content == null ||
                        TextUtils.isEmpty(content.getQiHao()) ||
                        content.getServerTime() == 0 && content.getActiveTime() == 0) {
                    showToast(getString(R.string.acquire_current_qihao_fail));
                    currentQihao = "???????";
                    if (endlineTouzhuTimer != null) {
                        endlineTouzhuTimer.cancel();
                        endlineTouzhuTimer = null;
                    }
                    playText.setText(String.format(getString(R.string.kaijian_deadline_format),
                            String.valueOf(currentQihao), "停止下注"));
                    return;
                }
            }

            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //更新当前这期离结束投注的倒计时显示
            updateCurrenQihaoCountDown(reg.getContent());
        } else if (action == ACQUIRE_PLAYS_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null || !result.crazySuccess) {
                showToast("同步玩法失败");
                return;
            }

            Object regResult = result.result;
            LocPlaysWraper reg = (LocPlaysWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : "同步玩法失败");
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                String json = new Gson().toJson(reg.getContent(), LotteryData.class);
                String gameCode = reg.getContent().getCode();
                CacheRepository.getInstance().saveLotteryPlayJson(getApplicationContext(), gameCode, json);
                //更新彩种变量及标题
                updateLotteryConstants(reg.getContent());
                //初始化大小玩法显示栏
                PlayItem defaultPlayItem = playRules.get(0);

                if (defaultPlayItem != null) {
                    selectPlayCode = defaultPlayItem.getCode();
                    selectPlayName = defaultPlayItem.getName();
                    List<SubPlayItem> rules = defaultPlayItem.getRules();
                    if (rules != null && !rules.isEmpty()) {
                        maxBetNumber = rules.get(0).getMaxBetNum();
                        selectSubPlayCode = rules.get(0).getCode();
                        selectSubPlayName = rules.get(0).getName();
                        minBounds = rules.get(0).getMinBonusOdds();
                        maxBounds = rules.get(0).getMaxBounsOdds();
                        minRakeback = rules.get(0).getMinRakeback();
                        winExample = rules.get(0).getWinExample();
                        detailDesc = rules.get(0).getDetailDesc();
                        playMethod = rules.get(0).getPlayMethod();
                    }
                }

                if (isPeilvVersion || lhcSelect) {
                    if (peilvSimpleFragment == null) {
                        peilvSimpleFragment = new PeilvSimpleFragment();
                    }
                    peilvSimpleFragment.setCpBianHao(cpCode);
                    peilvSimpleFragment.setPeilvListener(new TouzhuPeilvListener());
                    androidx.fragment.app.FragmentManager fm = this.getSupportFragmentManager();
                    androidx.fragment.app.FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment, peilvSimpleFragment);
                    ft.commit();
                    startProgress();
                    getKaiJianResult(cpCode);
                    UsualMethod.getCountDownByCpcode(TouzhuSimpleActivity.this, cpCode, COUNT_DOWN_REQUEST);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            peilvSimpleFragment.onPlayRuleSelected(String.valueOf(Constant.lottery_identify_V2),
                                    czCode, selectPlayCode, selectSubPlayCode,
                                    selectPlayName, selectSubPlayName, maxBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                        }
                    }, 200);
                } else {
                    if (jiangjinSimpleFragment == null) {
                        jiangjinSimpleFragment = new JiangjinSimpleFragment();
                    }
                    jiangjinSimpleFragment.setBetListener(new TouzhuJianjinListener());
                    jiangjinSimpleFragment.setCpBianHao(cpCode);
                    FragmentManager fm = this.getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment, jiangjinSimpleFragment);
                    ft.commit();

                    startProgress();
                    getKaiJianResult(cpCode);//获取开奖结果
                    UsualMethod.getCountDownByCpcode(TouzhuSimpleActivity.this, cpCode, COUNT_DOWN_REQUEST);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            jiangjinSimpleFragment.onPlayRuleSelected(cpVersion, czCode, selectPlayCode, selectSubPlayCode,
                                    selectPlayName, selectSubPlayName, minBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                        }
                    }, 200);
                }
                onBottomUpdate(0, 0);
                //将玩法显示在左下方
                refreshPlayRuleExpandListView();
            }
        } else if (action == JIANJIN_TOKEN_BETS_REQUEST) {

            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.dobets_token_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.dobets_token_fail);
                return;
            }
            Object regResult = result.result;
            BetToken reg = (BetToken) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : getString(R.string.dobets_token_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //获取下注口令后开始下注
            String data = wrapBets(reg.getContent());
            if(!TextUtils.isEmpty(data))
                postBets(data);
        } else if (action == WIN_LOST_REQUEST_CODE) {

            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_data_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.acquire_data_fail);
                return;
            }
            Object regResult = result.result;
            WinLostWraper reg = (WinLostWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : getString(R.string.acquire_data_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            showWinlostDialog(reg);
        } else if (action == ACCOUNT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult = result.result;
            MemInfoWraper reg = (MemInfoWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            //更新帐户名，余额等信息
            updateAccount(reg.getContent());
        } else if (action == SYNC_LHC_TIME) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
//                showToast(R.string.dobets_fail);
                return;
            }
            if (!result.crazySuccess) {
//                showToast(R.string.dobets_fail);
                return;
            }
            Object regResult = result.result;
            LhcServerTimeWrapper reg = (LhcServerTimeWrapper) regResult;
            if (!reg.isSuccess()) {
//                showToast(!Utils.isEmptyString(reg.getMsg())?reg.getMsg():getString(R.string.dobets_fail));
//                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
//                //所以此接口当code == 0时表示帐号被踢，或登录超时
//                if (reg.getCode() == 0) {
//                    UsualMethod.loginWhenSessionInvalid(this);
//                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
//            showToast(R.string.dobets_success);
            peilvSimpleFragment.setLhcServerTime(reg.getContent());
        } else if (action == GET_HEADER) {

            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
//                String errorString = Urls.parseResponseResult(result.error);
//                showToast(Utils.isEmptyString(errorString) ? this.getString(R.string.request_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            MemberHeaderWraper reg = (MemberHeaderWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
//            UsualMethod.getConfigFromJson(getContext());
            YiboPreference.instance(this).setUserHeader(reg.getContent());
            //更新头像
            UsualMethod.LoadUserImage(this, iv_image);

        }else if (action == GET_LOTTERY_NOTICE){
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult = result.result;
            NoticeResultWraper reg = (NoticeResultWraper) regResult;
            if (!reg.isSuccess() || reg.getContent() == null || reg.getContent().isEmpty()) {
                return;
            }
            showMutilMessageDialog(reg.getContent());
        }
    }

    /**
     * 展示多条信息
     *
     * @param content
     */
    private void showMutilMessageDialog(List<NoticeResult> content) {
        NoticeDialog dialog = new NoticeDialog(this);
        dialog.setContent(content)
                .setTitle("彩票公告")
                .initView(this)
                .show();
    }

    //更新帐户相关信息
    private void updateAccount(Meminfo meminfo) {
        if (meminfo == null) {
            return;
        }

        balanceTV.setVisibility(View.VISIBLE);
        if (!Utils.isEmptyString(meminfo.getBalance())) {
            leftMoneyName = String.format("%.2f元", Double.parseDouble(meminfo.getBalance()));
            balanceTV.setText(leftMoneyName);
        }
        accountName = !Utils.isEmptyString(meminfo.getAccount()) ? meminfo.getAccount() : "暂无名称";
        tv_account_name.setText(String.format("账户:%s", accountName));

    }

    private void updateAccountMoney(float meminfo) {
        leftMoneyName = String.format("%.2f元", meminfo);
        balanceTV.setText(leftMoneyName);
    }

    private void saveCurrentLotData() {
        SavedGameData data = new SavedGameData();
        data.setGameModuleCode(SavedGameData.LOT_GAME_MODULE);
        data.setAddTime(System.currentTimeMillis());
        data.setLotName(cpName);
        data.setLotCode(cpCode);
        data.setLotType(czCode);
        data.setPlayName(selectPlayName);
        data.setPlayCode(selectPlayCode);
        data.setSubPlayName(selectSubPlayName);
        data.setDuration(cpDuration);
        data.setSubPlayCode(selectSubPlayCode);
        data.setCpVersion(cpVersion);
        UsualMethod.localeGameData(this, data);
    }

    //提交投注
    private String wrapBets(String token) {
        List<OrderDataInfo> cartOrders = DatabaseUtils.getInstance(this).getCartOrders();
        if (cartOrders == null || cartOrders.isEmpty()) {
            showToast(R.string.noorder_please_touzhu_first);
            return null;
        }
        //构造下注POST数据
        try {
            JSONArray betsArray = new JSONArray();
            for (OrderDataInfo order : cartOrders) {
                StringBuilder sb = new StringBuilder();
                sb.append(cpCode).append("|");
                sb.append(order.getSubPlayCode()).append("|");
                sb.append(UsualMethod.convertPostMode(order.getMode())).append("|");
                sb.append(order.getBeishu()).append("|");
                sb.append(order.getNumbers());
                betsArray.put(sb.toString());
            }

            JSONObject content = new JSONObject();
            content.put("lotCode", cpCode);
            content.put("qiHao", "");
            content.put("token", token);
            content.put("bets", betsArray);
            String postJson = content.toString();
            return postJson;
        } catch (Exception e) {
            e.printStackTrace();
            showToast("注单信息有误，请重新下注");
            return null;
        }
    }

    private void postBets(String data){
        ApiParams params = new ApiParams();
        params.put("data", data);
        HttpUtil.postForm(this, Urls.DO_BETS_URL, params, true, getString(R.string.bet_ongoing), new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if(result.isSuccess()){
                    YiboPreference.instance(TouzhuSimpleActivity.this).setToken(result.getAccessToken());
                    showToast(R.string.dobets_success);
                    saveCurrentLotData();
                    accountWeb();
                    //下注成功后清除选择的号码并刷新页面
                    jiangjinSimpleFragment.clearViewAfterBetSuccess();
                }else {
                    showToast(TextUtils.isEmpty(result.getMsg()) ? "下注时出现错误，请重新下注" : result.getMsg());
                    if (result.getMsg().contains("登陆超时")) {
                        UsualMethod.loginWhenSessionInvalid(TouzhuSimpleActivity.this);
                    }
                }
            }
        });
    }

    private String compareQihao = "";

    /**
     * 更新开奖结果
     *
     * @param lotteryLast
     */
    private void updateLastResult(LotteryLast lotteryLast, float balance) {
        if (lotteryLast == null) {
            return;
        }
        Utils.LOG(TAG, "update result == " + lotteryLast.getHaoMa());
        if (Utils.isEmptyString(lotteryLast.getQiHao()) || Utils.isEmptyString(lotteryLast.getHaoMa())) {
            return;
        }
        if (!lotteryLast.getHaoMa().contains(",")) {
            return;
        }
        //先判断开奖结果是否有效，如果有效则不需要继续获取开奖结果
        handleAcqureLastResult(lotteryLast.getHaoMa());
        List<String> haomas1 = Utils.splitString(lotteryLast.getHaoMa(), ",");
        for (String hm : haomas1) {
            if (!Utils.isNumeric(hm) || hm.equalsIgnoreCase("?")) {
                if (lotteryLast.getQiHao().length() > 4) {
                    String newQiHao = lotteryLast.getQiHao().substring(lotteryLast.getQiHao().length() - 4);
                    kjResult.setText("第" + newQiHao + "期：开奖中...");
                } else {
                    kjResult.setText("第???" + "期：开奖中...");
                }
                return;
            }
        }
        //获取到正常的开奖号码后将定时获取开奖号码定时器取消
//        if (!sameResultToLast) {
//            Utils.LOG(TAG, "update resu cancel timer of ask result when get new haoma");
//            if (lastResultAskTimer != null) {
//                lastResultAskTimer.cancel();
//                lastResultAskTimer = null;
//            }
//        }
        compareQihao = lotteryLast.getQiHao();
        String newQiHao = lotteryLast.getQiHao().substring(lotteryLast.getQiHao().length() - 4);
        String old = kjResult.getText().toString();
        kjResult.setText(String.format(getString(R.string.qihao_string3), newQiHao));
        if (!Utils.isEmptyString(lotteryLast.getHaoMa()) && lotteryLast.getHaoMa().contains(",")) {
            Utils.LOG(TAG, "ago THE open haoma === " + lotteryLast.getHaoMa() + ",bianhao = " + cpCode);
            String[] split = lotteryLast.getHaoMa().split(",");
            int first = 0;
            int second = 0;
            int third = 0;
            int total = 0;

            if (split.length >= 3) {
                first = Integer.parseInt(split[0]);
                second = Integer.parseInt(split[1]);
                third = Integer.parseInt(split[2]);
                total = first + second + third;
            }
            //快三增加大小单双
            if (czCode.equals("10") || czCode.equals("100") || czCode.equals("58")) {
                boolean isOff = UsualMethod.getConfigFromJson(this).getK3_baozi_daXiaoDanShuang().equals("off");

                if (isOff && first == second && first == third) {
                    //开关关闭 三个相同显示豹子
                    lotteryLast.setHaoMa(lotteryLast.getHaoMa() + ",豹,豹");
                } else {
                    if (total >= 11) {
                        lotteryLast.setHaoMa(lotteryLast.getHaoMa() + ",大");
                    } else {
                        lotteryLast.setHaoMa(lotteryLast.getHaoMa() + ",小");
                    }
                    if (total % 2 == 0) {
                        lotteryLast.setHaoMa(lotteryLast.getHaoMa() + ",双");
                    } else {
                        lotteryLast.setHaoMa(lotteryLast.getHaoMa() + ",单");
                    }
                }
                //添加总和
                lotteryLast.setHaoMa(lotteryLast.getHaoMa() + "," + String.valueOf(total));

            }
            //PC蛋蛋增加总和数
            else if (czCode.equalsIgnoreCase("11") || czCode.equalsIgnoreCase("7") || czCode.equals("57")) {
                lotteryLast.setHaoMa(first + ",+," + second + ",+," + third + ",=," + total);
            }


            List<String> haomas = Utils.splitString(lotteryLast.getHaoMa(), ",");
            numberGridView.setVisibility(View.VISIBLE);
            unNormalTV.setVisibility(View.GONE);
            if (!old.equals(String.format(getString(R.string.qihao_string3), newQiHao))) {
                Utils.LOG(TAG, "ago update result with animation----");
                updateNumberGridView(haomas, czCode);
            } else {
                if (Firstinto)
                    updateNumberGridView(haomas, czCode);
            }
        } else {
            numberGridView.setVisibility(View.GONE);
            unNormalTV.setVisibility(View.VISIBLE);
            unNormalTV.setText(lotteryLast.getHaoMa());
        }
        //更新完开奖结果后，延迟5秒获取一下帐户余额
//        if (balance > 0) {
//            updateAccountMoney(balance);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                accountWeb();
            }
        }, 5000);
//        }
    }

    /**
     * 根据开奖结果显示号码view
     *
     * @param haoMaList
     * @param lotType
     */
    private void updateNumberGridView(List<String> haoMaList, String lotType) {
        if (haoMaList == null || haoMaList.isEmpty()) {
            return;
        }

        this.haoMaList = haoMaList;
        this.lotType = lotType;
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
//        int screenWidth = dm.widthPixels;
//        int column = (int) ((screenWidth - Utils.dip2px(this,120))/
//                Utils.dip2px(this,30));
//        Utils.LOG(TAG,"the figure out column == "+column);

        int column = 10;

//        numberGridView.setNumColumns(column);
//        numberGridView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                KaijianListActivity.createIntent(TouzhuSimpleActivity.this, cpName, cpBianHao, cpTypeCode);
//            }
//        });

        if (czCode.equals("6") || czCode.equals("66") || czCode.equals("666")) {
            updateGridview(lunarYear);
        } else {
            updateGridview(-1);
        }


        Utils.setListViewHeightBasedOnChildren(numberGridView, column);
        if (!Firstinto) {
            selectCaizhong(lotType, YiboPreference.instance(this).getGameVersion());
        } else {
            Firstinto = false;
        }
    }

    //deprecated, 改由接口LOTTERY_LAST_RESULT_V2_URL取得上一期奖期的本命年来计算号码生肖了
//    private void getOpenResultDetails() {
//        ApiParams params = new ApiParams();
//        params.put("lotCode", cpBianHao);
//        params.put("page", 1);
//        params.put("row", 5);
//        HttpUtil.get(this, Urls.OPEN_RESULT_DETAIL_URL, params, false, new HttpCallBack() {
//            @Override
//            public void receive(NetworkResult result) {
//                if (result.isSuccess()) {
//
//                    List<OpenResultDetail> kjList = new Gson().fromJson(result.getContent(), new TypeToken<List<OpenResultDetail>>() {
//                    }.getType());
//
//                    String kj = compareQihao;
//                    String date;
//                    if (kjList.get(0).getQiHao().equals(kj)) {
//                        date = kjList.get(0).getDate();
//                    } else if (kjList.get(1).getQiHao().equals(kj)) {
//                        date = kjList.get(1).getDate();
//                    } else if (kjList.get(2).getQiHao().equals(kj)) {
//                        date = kjList.get(2).getDate();
//                    } else if (kjList.get(3).getQiHao().equals(kj)) {
//                        date = kjList.get(3).getDate();
//                    } else if (kjList.get(4).getQiHao().equals(kj)) {
//                        date = kjList.get(4).getDate();
//                    } else {
//                        date = "";
//                    }
//                    updateGridview(date);
//                }
//            }
//        });
//    }


    private void updateGridview(int lunarYear) {
        NumbersScroolAdapter mScroolAdapter = new NumbersScroolAdapter(this, haoMaList,
                R.layout.touzhu_number_simple_gridview_item, lotType, cpCode, lunarYear);
        numberGridView.setAdapter(mScroolAdapter);
    }

    /**
     * 更新当前期数倒计时时间
     *
     * @param countDown
     */
    private void updateCurrenQihaoCountDown(CountDown countDown) {
        if (countDown == null) {
            return;
        }
        //创建开奖周期倒计时器
        long serverTime = countDown.getServerTime();
        long activeTime = countDown.getActiveTime();
        currentQihao = countDown.getQiHao();
        if (peilvSimpleFragment != null)
            peilvSimpleFragment.setCurrentQihao(currentQihao);
        long duration = Math.abs(activeTime - serverTime);
        if (endlineTouzhuTimer != null) {
            endlineTouzhuTimer.cancel();
            endlineTouzhuTimer = null;
        }

        if (UsualMethod.getConfigFromJson(this).getNative_fenpang_bet_switch().equalsIgnoreCase("on")) {
            createEndlineTouzhuTimer(duration, countDown.getQiHao());
            //开始离投注结束时间倒计时
            endlineTouzhuTimer.start();
        } else {
            Utils.LOG(TAG, "current qihao open duration = " + countDown.getQiHao() + "," + duration);
            if (duration > ago * 1000) {
                duration = duration - ago * 1000;
                createEndlineTouzhuTimer(duration, countDown.getQiHao());
                //开始离投注结束时间倒计时
                endlineTouzhuTimer.start();
            } else {
                if (!UsualMethod.getConfigFromJson(TouzhuSimpleActivity.this).getNative_fenpang_bet_switch().equalsIgnoreCase("on")) {
                    //当前时间差小于开封盘时间时；说明处于封盘中，直接倒计时剩下的封盘时间即可
                    setFengPaningOperation((int) (duration / 1000));
                } else {
                    createEndlineTouzhuTimer(duration, countDown.getQiHao());
                    //开始离投注结束时间倒计时
                    endlineTouzhuTimer.start();
                }
            }
        }
    }

    private void clearZhushuBottonView() {
        if (isPeilvVersion || lhcSelect) {
            peilvSimpleFragment.updateBottom(0, 0);
        } else {
            jiangjinSimpleFragment.updateBottom(0, 0, 0);
        }
    }

    /**
     * 根据小玩法及彩种编码获取赔率信息
     *
     * @param lotCategoryType 彩种类型编码
     * @param playCode        玩法代号
     * @param showDialog
     */
    private void getPeilvData(String lotCategoryType, String playCode, boolean showDialog) {
        RetrofitFactory.INSTANCE.api().getPvOddsUrl(playCode,lotCategoryType).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<PeilvWebResultWraper>() {
                            @Override
                            public void onSubscribe(Disposable d) {compositeDisposable.add(d);}

                            @Override
                            public void onSuccess(PeilvWebResultWraper reg) {
                                if(!playCode.equals(peilvSimpleFragment.selectRuleCode)){
                                    return;
                                }

                                if (!reg.isSuccess()) {
                                    if (TextUtils.isEmpty(reg.getMsg()) || reg.getMsg().equals(getString(R.string.no_peilv_data))) {
                                        showToast("没有赔率数据，请联系客服");
                                        YiboPreference.instance(getApplicationContext()).setToken(reg.getAccessToken());
                                        peilvSimpleFragment.updatePlayArea();
                                        return;
                                    }

                                    showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : getString(R.string.acquire_peilv_fail));
                                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                                    if (reg.getCode() == 0) {
                                        UsualMethod.loginWhenSessionInvalid(getApplicationContext());
                                    }
                                    return;
                                }
                                Utils.LOG(TAG, "getPeilvData end = " + System.currentTimeMillis());
                                YiboPreference.instance(getApplicationContext()).setToken(reg.getAccessToken());
                                //更新赔率面板号码区域赔率等数据
                                peilvSimpleFragment.updatePlayArea(reg.getContent());
                            }

                            @Override
                            public void onError(Throwable e) {showToast(R.string.acquire_peilv_fail);}
                        });
    }

    SimpleCaizhongWindow window;

    //点击标题栏彩种，下拉出所有彩种
    private void clickTitleDown() {

        if (window == null) {
            window = new SimpleCaizhongWindow(mLayout, this, this);
            window.setCurrentLotteryData(currentLotteryData);
            window.setCurrentLotteryDataChangeListener(new CurrentLotteryDataChangeListener() {
                @Override
                public void onCurrentLotteryChangeListener(LotteryData lotteryData) {
                    currentLotteryData = lotteryData;
                    czCode = lotteryData.getCzCode();
                    cpCode = lotteryData.getCode();
                    if ((czCode.equals("6") || czCode.equals("66") || czCode.equals("666"))) {
                        updateGridview(lunarYear);
                    } else {
                        updateGridview(-1);
                    }
                    //每次切换之后刚开始都不会封盘
                    setFengPan(false);
                    //停止之前的计时器
                    if (disposable != null) {
                        disposable.dispose();
                    }
                }
            });
            window.setSimpleClearListener(new SimpleClearListener() {
                @Override
                public void onClearAndUpdateListener() {
                    if (!isPeilvVersion && !lhcSelect) {
                        if (jiangjinSimpleFragment != null) {
                            jiangjinSimpleFragment.clearZhushuBottonView();
                            jiangjinSimpleFragment.updateBottomClearBtn();
                        }
                    }
                }
            });
            window.setSixMarkPeilvListener(new SixMarkPeilvListener() {
                @Override
                public void onLhcSelectAndisPeilvVersionChangeListener(boolean lhcSelect, boolean peilvVersion) {
                    TouzhuSimpleActivity.this.lhcSelect = lhcSelect;
                    TouzhuSimpleActivity.this.isPeilvVersion = peilvVersion;
                }
            });
            window.initView(this);
        }

        window.showAsDropDown(mLayout);
        WindowUtils.setWindowAttributes(this, 0.4f);


    }

    public void refreshLotteryPlayRules(String gameCode, boolean showLoading) {
        CacheRepository.getInstance().loadLotteryPlayJson(this, gameCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(String json) {
                        Utils.LOG(TAG, "find the backup of play rules");
                        initOnGetLotteryJson(json);
                        UsualMethod.syncLotteryPlaysByCode(TouzhuSimpleActivity.this, gameCode,
                                false, ACQUIRE_PLAYS_REQUEST, TouzhuSimpleActivity.this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.LOG(TAG, "can't find the backup of play rules");
                        e.printStackTrace();
                        UsualMethod.syncLotteryPlaysByCode(TouzhuSimpleActivity.this, gameCode,
                                true, ACQUIRE_PLAYS_REQUEST, TouzhuSimpleActivity.this);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:
                if (Utils.shiwanFromMobile(this)) {
                    Toast.makeText(this, "操作权限不足，请联系客服！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intentCharge = BankingManager.Companion.openChargePage(this, accountName, leftMoneyName);
                startActivity(intentCharge);
                break;
            case R.id.btn_withdraw:
                if (Utils.shiwanFromMobile(this)) {
                    Toast.makeText(this, "操作权限不足，请联系客服！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = BankingManager.Companion.openWithdrawPage(this, 0f);
                startActivity(intent);
                break;
            case R.id.iv_image:
                UserCenterActivity.createIntent(this);
                break;
            case R.id.notice_layout:
                KaijianListActivity.createIntent(this, cpName, cpCode, czCode);
                break;
            case R.id.clear_btn:
                if (/*UsualMethod.isPeilvVersion(this)*/isPeilvVersion || lhcSelect) {
                    peilvSimpleFragment.onPlayClean(false);
                } else {
                    jiangjinSimpleFragment.onPlayClean(false);
                }
                break;
            case R.id.touzhu_btn:
                if (/*UsualMethod.isPeilvVersion(this)*/isPeilvVersion || lhcSelect) {
                    peilvSimpleFragment.onPlayTouzhu();
                } else {
                    jiangjinSimpleFragment.onPlayTouzhu();
                }
                break;
            case R.id.clickable_title:
            case R.id.middle_title:
                clickTitleDown();
                break;
            case R.id.history_results:
                break;
            case R.id.setting:
            case R.id.zhushu:
            case R.id.jianjing:
            case R.id.total_fee:
                if (/*UsualMethod.isPeilvVersion(this)*/isPeilvVersion || lhcSelect) {
                    peilvSimpleFragment.showZhudangWindow();
                } else {
//                    jiangjinFragment.showAdjustWindow(true);
                }
                break;
            case R.id.right_text:
                showTouZhuMoreMenu();
                break;
            case R.id.play_selector:
//                updatePlayMenuState();
//                if (tabStateArr) {// 判断是否需要关闭弹出层
//                    showPlayPopupWindow();
//                } else {
//                    if (playPopupWindow != null && playPopupWindow.isShowing()) {
//                        playPopupWindow.dismiss();
//                    }
//                }
                break;
            case R.id.right_icon:
                CaipiaoOrderActivity.createIntent(this, czCode, cpCode, cpName, cpVersion);
                break;
        }
        super.onClick(v);
    }

    // 语音结束时的回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
                && resultCode == RESULT_OK) {
            // 取得语音的字符
            ArrayList<String> results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (int i = 0; i < results.size(); i++) {
                Utils.LOG(TAG, "words result = " + results.get(i));
            }
        } else if (requestCode == WebChatRoomDialog.CHOOSE_REQUEST_CODE) {
            WebChatPhotoEvent webChatPhotoEvent = new WebChatPhotoEvent(requestCode, resultCode, data);
            EventBus.getDefault().post(webChatPhotoEvent); //聊天室发送图片之后回调
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 显示右上角更多菜单
     */
    private void showTouZhuMoreMenu() {

        String[] arrays = getResources().getStringArray(R.array.touzhu_more_array);
        final String cpVersion = YiboPreference.instance(TouzhuSimpleActivity.this).getGameVersion();
        final List<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < arrays.length; i++) {
            if (i == arrays.length - 1) {
                if ("on".equals(UsualMethod.getConfigFromJson(this).getShow_lottery_trend())) {
                    arrayList.add("走势图");
                }
            }

            arrayList.add(arrays[i]);
        }
        arrayList.add("长龙");
        arrays = arrayList.toArray(new String[arrayList.size()]);
        PopupListMenu menu = new PopupListMenu(this, arrays);
        menu.setBackground(R.drawable.caipiao_item_bg);
        menu.setDimEffect(true);
        menu.setAnimation(true);
        menu.setOnItemClickListener(new PopupListMenu.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                switch (arrayList.get(position)) {
                    case "投注记录":
                        actionTouzhuRecord(cpCode);
                        break;
                    case "历史开奖":
                        KaijianListActivity.createIntent(TouzhuSimpleActivity.this, cpName, cpCode, czCode);
                        break;
                    case "玩法说明":
                        String url = Urls.BASE_URL + Urls.PORT + Urls.LOTTERY_RULES_URL +
                                "?lotCode=" + cpCode + "&source=1";
                        ActiveDetailActivity.createIntent(TouzhuSimpleActivity.this,
                                "", "玩法说明", url);
//                        actionPlayProfile();
                        break;
                    case "今日盈亏":
//                        if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V3))
//                                ||cpVersion.equals(String.valueOf(Constant.lottery_identify_V1))) {
//                            actionZuihao();
//                        }else{
                        //同步今日输赢数据
                        syncWinlost(true);
//                        }
                        break;
                    case "设置":
//                        if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V3))
//                                ||cpVersion.equals(String.valueOf(Constant.lottery_identify_V1))) {
//                            //同步今日输赢数据
//                            syncWinlost(true);
//                        }else{
                        AppSettingActivity.createIntent(TouzhuSimpleActivity.this);
//                        }
                        break;
                    case "走势图":
                        Intent intent = new Intent(TouzhuSimpleActivity.this, KefuActivity.class);
                        intent.putExtra("url", Urls.BASE_URL + Urls.ZHOUSHITU + "?lotCode=" + cpCode);
                        intent.putExtra("title", "走势图");
                        startActivity(intent);
                        break;

                    case "长龙":
                        Intent intent1 = new Intent(TouzhuSimpleActivity.this, LongLonngActivity.class);
                        intent1.putExtra("code", cpCode);
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
            }
        });
        menu.show(rightLayout, 0, 5);
    }

    private void showAfterBetDialog() {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(2);
        String content = "下注成功！";
        ccd.setContent(content);
        ccd.setTitle("温馨提示");
        ccd.setLeftBtnText("查看记录");
        ccd.setRightBtnText("继续下注");
        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                actionTouzhuRecord(cpCode);
                ccd.dismiss();
            }
        });
        ccd.setRightBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        ccd.setCanceledOnTouchOutside(false);
        ccd.setCancelable(false);
        ccd.createDialog();
    }


    private void showConfirmBetDialog(final List<PeilvPlayData> selectDatas, final boolean isMulSelect, final String money, int count
            , double totalMoney, final String playRule, final String winMoney) {


        final Dialog dialog = new AlertDialog.Builder(this).create();

        View view = View.inflate(this, R.layout.simple_bet_dialog, null);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);

        Button btn_confirm_bet = (Button) view.findViewById(R.id.btn_confirm_bet);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
        XListView xListView = (XListView) view.findViewById(R.id.xlistview);
        TextView tv_bets_and_money = (TextView) view.findViewById(R.id.tv_bets_and_money);
        LinearLayout llHeader = (LinearLayout) view.findViewById(R.id.ll_simple_bet_dialog_header);
        LinearLayout ll_multiple_selection = (LinearLayout) view.findViewById(R.id.ll_multiple_selection);
        TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
        TextView tv_numbers = (TextView) view.findViewById(R.id.tv_numbers);
        TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
        TextView tv_win_money = (TextView) view.findViewById(R.id.tv_win_money);
        tv_win_money.setText(getString(R.string.xiazhu_keying_format, winMoney));

        //多选情况下隐藏listView
        if (isMulSelect) {
            ll_multiple_selection.setVisibility(View.VISIBLE);
            llHeader.setVisibility(View.VISIBLE);
            xListView.setVisibility(View.GONE);
        } else {
            //单选情况下显示listView
            ll_multiple_selection.setVisibility(View.GONE);
            llHeader.setVisibility(View.GONE);
            xListView.setVisibility(View.VISIBLE);
        }

        tv_bets_and_money.setText(String.format(Locale.CHINA, "共%d注,共%.2f元", count, totalMoney));
//        tv_bets_and_money.setText("共" + count + "注,共" + totalMoney + "元");

        btn_confirm_bet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realPeilvPostBets(selectDatas, isMulSelect, money);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });


        if (isMulSelect) {
            //类型
            if (null == selectDatas.get(0).getItemName() || selectDatas.get(0).getItemName().length() == 0) {
                tv_type.setText(playRule);
            } else {
                tv_type.setText(String.format(Locale.CHINA, "%s-%s", playRule, selectDatas.get(0).getItemName()));
            }
            //号码
            StringBuilder balloonCount = new StringBuilder();
            for (int i = 0; i < selectDatas.size(); i++) {
                if (i == selectDatas.size() - 1) {
                    balloonCount.append(selectDatas.get(i).getNumber());
                } else {
                    balloonCount.append(selectDatas.get(i).getNumber()).append(",");
                }
            }

            tv_numbers.setText(balloonCount.toString());

            //钱数
            float tvMoney = selectDatas.get(0).getMoney();
            tv_money.setText(String.valueOf(tvMoney));

        } else {
            xListView.setPullRefreshEnable(false);
            xListView.setPullLoadEnable(false);
            xListView.addHeaderView(View.inflate(this, R.layout.simple_bet_dialog_header, null));
            xListView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return selectDatas.size();
                }

                @Override
                public Object getItem(int position) {
                    return selectDatas.get(position);
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
                        convertView = View.inflate(TouzhuSimpleActivity.this, R.layout.simple_bet_dialog_list_item, null);
                        holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
                        holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
                        holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
                        convertView.setTag(holder);
                    } else {
                        holder = (Holder) convertView.getTag();
                    }
                    PeilvPlayData item = selectDatas.get(position);

                    if (null == item.getItemName() || 0 == item.getItemName().length()) {
                        holder.tvType.setText(playRule);
                    } else {
                        holder.tvType.setText(String.format(Locale.CHINA, "%s-%s", playRule, item.getItemName()));
                    }

                    holder.tvNumber.setText(item.getNumber());
                    holder.tvMoney.setText(String.valueOf(item.getMoney()));
                    return convertView;
                }

                class Holder {

                    TextView tvType;
                    TextView tvNumber;
                    TextView tvMoney;
                }

            });
        }


    }


    private void showWinlostDialog(WinLostWraper wraper) {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(1);

        String content = "";
        if (wraper != null && wraper.getContent() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("今日消费:").append(wraper.getContent().getAllBetAmount()).append("元").append("\n");
            sb.append("今日中奖:").append(wraper.getContent().getAllWinAmount()).append("元").append("\n");
            sb.append("今日盈亏:").append(wraper.getContent().getYingkuiAmount()).append("元").append("\n");
            content = sb.toString();
        }
        ccd.setContent(content);
        ccd.setTitle("今日输赢");
        ccd.setMiddleBtnText("好的");
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        ccd.setCanceledOnTouchOutside(false);
        ccd.setCancelable(false);
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    private void actionZuihao() {
        boolean success = jiangjinSimpleFragment.prepareBetOrders();
        if (!success) {
            return;
        }
        List<OrderDataInfo> data = DatabaseUtils.getInstance(this).getCartOrders();
        if (data == null || data.isEmpty()) {
            showToast(R.string.please_touzhu_first);
            return;
        }
        Type listType = new TypeToken<ArrayList<OrderDataInfo>>() {
        }.getType();
        String zhuJsons = new Gson().toJson(data, listType);
        BraveZuiHaoActivity.createIntent(this, zhuJsons, cpCode, cpName, selectPlayName,
                selectPlayCode, selectSubPlayName, selectSubPlayCode, cpDuration);
    }

    //查看投注记录
    private void actionTouzhuRecord(String cpBianma) {
        RecordsActivityNew.createIntent(this, cpName,
                UsualMethod.isSixMark(this, cpBianma) ? Constant.LHC_RECORD_STATUS :
                        Constant.CAIPIAO_RECORD_STATUS, cpBianma);
    }

    private void actionPlayProfile() {
        PlayRuleProfileActivity.createIntent(this, winExample, playMethod, detailDesc);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (playPopupWindow != null && playPopupWindow.isShowing()) {
                playPopupWindow.dismiss();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 赔率版下单投注
     *
     * @param betDatas
     * @param money,若金额不为空，说明是多选状态
     */
    private void realPeilvPostBets(List<PeilvPlayData> betDatas, boolean mulSelect, String money) {
        if (betDatas == null || betDatas.isEmpty()) {
            return;
        }

        if (maxBetNumber != 0) {
            Map<String, Integer> map = new HashMap<>(20);
            for(PeilvPlayData playData: betDatas){
                if(map.containsKey(playData.getItemName())){
                    map.put(playData.getItemName(), map.get(playData.getItemName())+1);
                }else {
                    map.put(playData.getItemName(), 1);
                }

                if(map.get(playData.getItemName()) > maxBetNumber){
                    showToast("投注失败,超过最大限制码数[" + maxBetNumber + "]码");
                    return;
                }
            }
        }

        //构造下注POST数据
        try {
            String url;
            ApiParams params = new ApiParams();
            String betIp = ChatSpUtils.instance(this).getBET_IP();
            if (UsualMethod.isSixMark(this, cpCode)) {
                url = Urls.DO_SIX_MARK_URL;
                //六合彩连码四全中最多只能选4个号码
                if (PlayCodeConstants.lianma.equals(selectPlayCode) && "siqz".equalsIgnoreCase(selectSubPlayCode)) {
                    if (betDatas.size() > 4) {
                        new AlertDialog.Builder(this)
                                .setTitle("请重选号码")
                                .setMessage("最多选择 4个号码")
                                .setPositiveButton("确认", (dialog, which) -> dialog.dismiss())
                                .create()
                                .show();
                        return;
                    }
                }

                JSONArray betsArray = null;
                if (mulSelect) {
                    PeilvPlayData pData = betDatas.get(0);
                    int minSelectCount = pData.getPeilvData() != null ? pData.getPeilvData().getMinSelected() : 0;
                    if (minSelectCount > betDatas.size()) {
                        showToast(String.format(getString(R.string.min_select_numbers_format), minSelectCount));
                        return;
                    }


                    //若是连码情况，则所选号码不能大于10个
                    if (this.selectPlayCode.equals(PlayCodeConstants.lianma) && betDatas.size() > 10) {
                        showToast(String.format(Locale.CHINA, "所选球数请勿大于%d", 10));
                        return;
                    } else if (this.selectPlayCode.equals(PlayCodeConstants.lianxiao) && betDatas.size() > 6) {
                        showToast(String.format(Locale.CHINA, "所选球数请勿大于%d", 6));
                        return;
                    } else if (this.selectPlayCode.equals(PlayCodeConstants.weishulian) && betDatas.size() > 6) {
                        showToast(String.format(Locale.CHINA, "所选球数请勿大于%d", 6));
                        return;
                    }

                    if (selectPlayCode.equals(PlayCodeConstants.quanbuzhong)) {
                        switch (minSelectCount) {
                            case 3:
                            case 4:
                                if (betDatas.size() > 8) {
                                    showToast("选球数请勿大于8位!");
                                    return;
                                }
                                break;
                            case 5:
                            case 6:
                            case 7:
                                if (betDatas.size() > 10) {
                                    showToast(String.format("选球数请勿大于%d位!", 10));
                                    return;
                                }
                                break;
                            case 8:
                            case 9:
                                if (betDatas.size() > minSelectCount + 3) {
                                    showToast(String.format("选球数请勿大于%d位!", minSelectCount + 3));
                                    return;
                                }
                                break;
                            case 10:
                                if (betDatas.size() > 13) {
                                    showToast(String.format("选球数请勿大于%d位!", 13));
                                    return;
                                }
                                break;
                            case 11:
                            case 12:
                                if (betDatas.size() > minSelectCount + 2) {
                                    showToast(String.format("选球数请勿大于%d位!", minSelectCount + 2));
                                    return;
                                }
                                break;
                            default:
                                break;
                        }
                    } else if (selectPlayCode.equalsIgnoreCase(PlayCodeConstants.lianma)) {
                        if (betDatas.size() > 10) {
                            showToast(String.format("号码数请勿大于%d位!", 10));
                            return;
                        }
                    }


                    List<String> numbers = new ArrayList<>();
                    for (PeilvPlayData data : betDatas) {
                        numbers.add(data.getNumber());
                    }
                    if (!selectPlayCode.equals(PlayCodeConstants.zhi_xuan_str) && !selectPlayCode.equals(PlayCodeConstants.zhi_xuan_str_zx)) {
                        Collections.sort(numbers);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < numbers.size(); i++) {
                        String num = numbers.get(i);
                        sb.append(num.trim());
                        if (i != numbers.size() - 1) {
                            sb.append(",");
                        }
                    }
//                    Utils.LOG(TAG,"the sort number = "+sb.toString());
                    //从多个赔率数据中根据用户选择的选项数，选择出选项数正确的赔率数据
                    betsArray = new JSONArray();
                    PeilvWebResult webResult = UsualMethod.getPeilvData(getApplicationContext(), cpCode, selectPlayCode, betDatas.size(), pData);
                    if (webResult != null) {
                        //先判断下注的总金额是否在最大和最小下注金额之
                        if (!Utils.isEmptyString(money)) {
                            float moneyFloat = Float.parseFloat(money);
                            boolean rightMoney = moneyLimit(webResult.getMaxBetAmmount(), webResult.getMinBetAmmount(), moneyFloat);
                            if (!rightMoney) {
                                return;
                            }
                        }
                        JSONObject item = new JSONObject();
                        item.put("haoma", sb.toString());
                        item.put("money", money);
                        item.put("markSixId", webResult.getId());
                        item.put("betIp", betIp);
                        betsArray.put(item);
                    }
                } else {
                    betsArray = new JSONArray();
                    for (PeilvPlayData data : betDatas) {
                        //先判断下注的总金额是否在最大和最小下注金额之前
                        if (data.getPeilvData() != null) {
                            PeilvWebResult peilvData = data.getPeilvData();
                            if (peilvData != null) {
                                boolean rightMoney = moneyLimit(peilvData.getMaxBetAmmount(), peilvData.getMinBetAmmount(), data.getMoney());
                                if (!rightMoney) {
                                    return;
                                }
                            }
                        }
                        JSONObject item = new JSONObject();
                        item.put("haoma", UsualMethod.getPeilvPostNumbers(data));
                        item.put("money", String.valueOf((int) data.getMoney()));
                        item.put("markSixId", String.valueOf(data.getPeilvData() != null ? data.getPeilvData().getId() : 0));
                        item.put("betIp", betIp);
                        betsArray.put(item);
                    }
                    Utils.LOG(TAG, "the betsarray == " + betsArray.toString());
                }

                JSONObject dataPost = new JSONObject();
                dataPost.put("pour", betsArray);
                dataPost.put("lotCode", cpCode);
                dataPost.put("playCode", selectSubPlayCode);
                dataPost.put("state", mulSelect ? 1 : 2);
                dataPost.put("qiHao", currentQihao);
                //构造下注crazy request
                params.put("data", dataPost.toString());
            } else {
                url = Urls.DO_PEILVBETS_URL;
                String betJson = "";
                PeilvPlayData pData = betDatas.get(0);
                if (mulSelect) {
                    int minSelectCount = pData.getPeilvData() != null ? pData.getPeilvData().getMinSelected() : 0;
                    if (minSelectCount > betDatas.size()) {
                        showToast(String.format(getString(R.string.min_select_numbers_format), minSelectCount));
                        return;
                    }

                    //时时彩,分分彩系列---组选六不能大于八位
                    if (this.selectPlayCode.equals(PlayCodeConstants.zuxuan_liu_peilv) && betDatas.size() > 8) {
                        showToast(String.format(Locale.CHINA, "最多选择%d个号码", 8));
                        return;
                    }

                    //选4任选选5任选最多只能选6个号码
                    if (this.selectSubPlayCode.equals(PlayCodeConstants.xuansirenxuan) && betDatas.size() > 6) {

                        showToast(String.format(Locale.CHINA, "最多选择%d个号码", 6));
                        return;
                    }
                    if (this.selectSubPlayCode.equals(PlayCodeConstants.xuanwurenxuan) && betDatas.size() > 6) {
                        showToast(String.format(Locale.CHINA, "最多选择%d个号码", 6));
                        return;
                    }
                    //若是连码情况，则所选号码不能大于10个
                    if (this.czCode.equals("12")) {
                        if (this.selectPlayCode.equals(PlayCodeConstants.lianma_peilv_klsf) && betDatas.size() > 8) {
                            showToast(String.format(Locale.CHINA, "所选球数请勿大于%d", 8));
                            return;
                        }
                    } else if (selectPlayCode.equals(PlayCodeConstants.zuxuan_liu_peilv) && betDatas.size() > 8) {
                        showToast(String.format(Locale.CHINA, "所选球数请勿大于%d", 8));
                        return;
                    }

                    if (this.selectPlayCode.equals(PlayCodeConstants.lianxiao) && betDatas.size() > 6) {
                        showToast(String.format("所选球数请勿大于%d", 6));
                        return;
                    }


                    List<String> numbers = new ArrayList<>();
                    JSONArray betsArray = new JSONArray();
                    for (PeilvPlayData data : betDatas) {
                        numbers.add(data.getNumber());
                    }

                    if (!selectPlayCode.equals(PlayCodeConstants.zhi_xuan_str) && !selectPlayCode.equals(PlayCodeConstants.zhi_xuan_str_zx)) {
                        Collections.sort(numbers);
                    } else {
                        //十一选五直选
                        int differentBallNum = 0;
                        for (int i = 0; i < betDatas.size(); i++) {
                            String firstName = betDatas.get(0).getItemName();
                            if (!betDatas.get(i).getItemName().equals(firstName)) {
                                differentBallNum++;
                            }
                        }

                        if (differentBallNum == 0) {
                            ToastUtils.showShort("下注拖胆号码错误");
                            return;
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < numbers.size(); i++) {
                        String num = numbers.get(i);
                        sb.append(num.trim());
                        if (i != numbers.size() - 1) {
                            sb.append(",");
                        }
                    }
                    Utils.LOG(TAG, "the sort number = " + sb.toString());
                    String name = sb.toString();

                    JSONObject item = new JSONObject();
                    item.put("name", name);
                    item.put("money", money);
                    item.put("betIp", betIp);
                    //从多个赔率数据中根据用户选择的选项数，选择出选项数正确的赔率数据

                    PeilvWebResult webResult = UsualMethod.getPeilvData(this, cpCode, selectPlayCode, betDatas.size(), pData);
                    if (webResult != null) {
                        //先判断下注的总金额是否在最大和最小下注金额之前
                        if (!Utils.isEmptyString(money)) {
                            float moneyFloat = Float.parseFloat(money);
                            boolean rightMoney = moneyLimit(webResult.getMaxBetAmmount(), webResult.getMinBetAmmount(), moneyFloat);
                            if (!rightMoney) {
                                return;
                            }
                        }
                        item.put("oddsId", String.valueOf(webResult.getId()));
                        item.put("odds", String.valueOf(webResult.getOdds()));
                        item.put("rate", String.valueOf(webResult.getRakeBack()));
                    }
                    betsArray.put(item);
                    betJson = betsArray.toString();
                } else {
                    JSONArray betsArray = new JSONArray();
                    for (PeilvPlayData data : betDatas) {

                        //先判断下注的总金额是否在最大和最小下注金额之前
                        if (data.getPeilvData() != null) {
                            PeilvWebResult peilvData = data.getPeilvData();
                            if (peilvData != null) {
                                boolean rightMoney = moneyLimit(peilvData.getMaxBetAmmount(), peilvData.getMinBetAmmount(), data.getMoney());
                                if (!rightMoney) {
                                    return;
                                }
                            }
                        }
                        JSONObject item = new JSONObject();
                        item.put("name", UsualMethod.getPeilvPostNumbers(data));
                        item.put("money", String.valueOf((int) data.getMoney()));
                        item.put("oddsId", String.valueOf(data.getPeilvData() != null ? data.getPeilvData().getId() : 0));
                        item.put("rate", String.valueOf(data.getPeilvData() != null ? data.getPeilvData().getRakeBack() : 0));
                        item.put("betIp", betIp);
                        betsArray.put(item);
                    }
                    betJson = betsArray.toString();
                }
                //构造下注crazy request
                params.put("data", betJson);
                params.put("lotCode", cpCode);
                params.put("qiHao", currentQihao);
                params.put("lotType", czCode);
                params.put("playCode", selectSubPlayCode);
                params.put("groupCode", selectPlayCode);
                params.put("stId", pData.getPeilvData().getStationId());
            }

            HttpUtil.postForm(this, url, params, true, getString(R.string.bet_ongoing), new HttpCallBack() {
                @Override
                public void receive(NetworkResult result) {
                    if(result.isSuccess()){
                        YiboPreference.instance(TouzhuSimpleActivity.this).setToken(result.getAccessToken());
                        showToast(R.string.dobets_success);
                        accountWeb();
                        //下注成功后保存这次下注的彩种，玩法等相关数据到内存
                        saveCurrentLotData();
                        //下注成功后清除选择号码的记录
                        peilvSimpleFragment.onPlayClean(true);
                        showAfterBetDialog();
                    }else {
                        showToast(TextUtils.isEmpty(result.getMsg()) ? "下注时出现错误，请重新下注" : result.getMsg());
                        if (result.getMsg().contains("登陆超时")) {
                            UsualMethod.loginWhenSessionInvalid(TouzhuSimpleActivity.this);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean moneyLimit(float maxMoney, float minMoney, float money) {
        //先判断下注的总金额是否在最大和最小下注金额之前
//        if (!Utils.isEmptyString(money)) {
//            int moneyInt = Integer.parseInt(money);
        if (money < minMoney) {
            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
            ToastUtils.showShort("投注金额不能小于" + minMoney + "元");
//            showToast(String.format(getString(R.string.touzhu_money_min_limit_format), minMoney));
            return false;
        }
        if (money > maxMoney) {
//            showToast(String.format(getString(R.string.touzhu_money_max_limit_format), maxMoney));
            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
            ToastUtils.showShort("投注金额不能大于" + maxMoney + "元");
            return false;
        }
//        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playRules != null) {
            playRules.clear();
            playRules = null;
        }
        if (endlineTouzhuTimer != null) {
            endlineTouzhuTimer.cancel();
        }
        if (lastResultAskTimer != null) {
            lastResultAskTimer.cancel();
            lastResultAskTimer = null;
        }
        handler = null;
        DatabaseUtils.getInstance(this).deleteAllOrder();
    }

    //根据彩种决定列表长度
    void selectCaizhong(String cpCode, String cpVersion) {
        int count = UsualMethod.ballCount(cpCode, cpVersion);
        startScrool(count);
    }

    //开始播放声音并滚动
    void startScrool(int count) {
        if (YiboPreference.instance(this).isKaiJiangSoundAllow())
            soundPool.play(music, 1, 1, 0, 0, 1);
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                handler.sendEmptyMessageDelayed(i, 100);
            } else {
                handler.sendEmptyMessageDelayed(i, 500 + i * 200);
            }

            if (i == count - 1) {
                handler.sendEmptyMessageDelayed(0x123, i * 200 + 5000);
            }
        }
    }


}
