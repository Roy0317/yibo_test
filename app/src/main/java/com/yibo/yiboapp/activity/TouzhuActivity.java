package com.yibo.yiboapp.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.anuo.immodule.activity.ChatMainActivity;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.Event.WebChatPhotoEvent;
import com.yibo.yiboapp.data.CacheRepository;
import com.yibo.yiboapp.entify.NoticeResult;
import com.yibo.yiboapp.entify.NoticeResultWraper;
import com.yibo.yiboapp.entify.OpenResultDetail;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.fragment.JiangjinFragment;
import com.yibo.yiboapp.fragment.PeilvFragment;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.NumbersAdapter;
import com.yibo.yiboapp.adapter.PlayRuleExpandAdapter;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.DatabaseUtils;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.PeilvPlayData;
import com.yibo.yiboapp.data.PlayCodeConstants;
import com.yibo.yiboapp.data.UpdateListener;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BcLotteryData;
import com.yibo.yiboapp.entify.BetToken;
import com.yibo.yiboapp.entify.CountDown;
import com.yibo.yiboapp.entify.LastResultWraper;
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
import com.yibo.yiboapp.entify.WinLostWraper;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.network.RetrofitFactory;
import com.yibo.yiboapp.ui.CaizhongWindow;
import com.yibo.yiboapp.ui.PopupListMenu;
import com.yibo.yiboapp.utils.ChatRoomUtils;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.NoticeDialog;
import com.yibo.yiboapp.views.WebChatRoomDialog;
import com.yibo.yiboapp.views.floatball.FloatBallManager;
import com.yibo.yiboapp.views.floatball.floatball.FloatBallCfg;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
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
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by johnson on 2017/9/23.
 * 投注主页面，包括赔率和奖金版本(以fragment 嵌入)
 * 主页面功能: 玩法选择，开奖结果及开奖历史展现
 */

public class TouzhuActivity extends BaseActivity implements UpdateListener,
        SessionResponse.Listener<CrazyResult<Object>> {

    public static final String TAG = TouzhuActivity.class.getSimpleName();
    LinearLayout playSelector;
    TextView balanceTV;
    //开奖结果面板view
    LinearLayout openLayout;
    TextView kjResult;//上一期开奖期号
    TextView unNormalTV;//开奖结果不正常时的文字显示
    GridView numberGridView;//开奖结果号码列表
    private TextView playText;

    Animation animation;
    private boolean tabStateArr = false;// 标记tab的选中状态，方便设置

    // 屏幕的宽高
    public static int screen_width = 0;
    public static int screen_height = 0;
    PopupWindow playPopupWindow = null;
    CaizhongWindow caiZhongSelectorWindow;


    //玩法菜单view
    int selectedMainPlayPosition;//大玩法选择的列表位置
    int selectedSubPlayPosition;//小玩法选择的列表位置


    private FloatBallManager mFloatballManager;//悬浮球了管理器

    int maxBetNumber;//最多投注个数
    String selectPlayCode = "01";
    String selectRuleCode = "1";
    String selectPlayName = "";
    String selectSubPlayName = "";
    float minBounds;//最小奖金
    float maxBounds;//最大奖金
    float minRakeback;//最小返水
    String winExample = "";
    String detailDesc = "";
    String playMethod = "";

    //彩种相关信息
    List<PlayItem> playRules = new ArrayList<>();//彩票玩法
    String cpTypeCode;//彩票类型代号
    String cpName;//彩票名称
    long cpDuration;//彩票开奖间隔时间
    String cpBianHao;//彩票编码
    int ago;//开奖时间与封盘时间差,单位秒
    int ago_offset = 2;//开奖时间与封盘时间差偏移秒
    CountDownTimer endlineTouzhuTimer;
    CountDownTimer lastResultAskTimer;//查询最后开奖结果的倒计时器
    JiangjinFragment jiangjinFragment;
    PeilvFragment peilvFragment;

    public static final int SYNC_LHC_TIME = 0x100;
    public static final int GET_LOTTERY_NOTICE = 0x200;

    String currentQihao;//当前期号
    String cpVersion;
    private String currentGameCode = "";

    public static final int LAST_RESULT_REQUEST = 0x01;
    public static final int COUNT_DOWN_REQUEST = 0x02;
    public static final int ACQUIRE_PLAYS_REQUEST = 0x05;
    public static final int JIANJIN_TOKEN_BETS_REQUEST = 0x07;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 0x08;
    public static final int WIN_LOST_REQUEST_CODE = 0x09;
    public static final int ACCOUNT_REQUEST = 0x10;
    boolean isPeilvVersion;//是否赔率版
    boolean lhcSelect;//是否在奖金版本中选中了六合彩，十分六合彩

    PopupWindow gestureShakePop;
    private boolean isOnOriginalChat;
    String lastOpenResult = "";//上一期开奖结果

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.touzhu_layout);
        isPeilvVersion = getIntent().getBooleanExtra("isPeilvVersion", false);
        cpVersion = getIntent().getStringExtra("cpVersion");
        isOnOriginalChat = Utils.onOrOffOriginalChat(this);
        if (Utils.isEmptyString(cpVersion)) {
            cpVersion = YiboPreference.instance(this).getGameVersion();
        }
        initView();
        final String lottery = getIntent().getStringExtra("lottery");
        if (!Utils.isEmptyString(lottery)) {
            initOnGetLotteryJson(lottery);
        }

        currentGameCode = getIntent().getStringExtra("gameCode");
        boolean needRefresh = getIntent().getBooleanExtra("needRefresh", false);
        if(!TextUtils.isEmpty(currentGameCode) && needRefresh){
            UsualMethod.syncLotteryPlaysByCode(this, currentGameCode, false, ACQUIRE_PLAYS_REQUEST, this);
        }

        initPlaySeletor();
        if (/*UsualMethod.isPeilvVersion(this)*/isPeilvVersion) {
            peilvFragment = new PeilvFragment();

            peilvFragment.setCpBianHao(cpBianHao);
            peilvFragment.setPeilvListener(new TouzhuPeilvListener());
            FragmentManager fm = this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, peilvFragment);
            ft.commit();

            startProgress();
            getKaiJianResult(cpBianHao);
            UsualMethod.getCountDownByCpcode(TouzhuActivity.this, cpBianHao, COUNT_DOWN_REQUEST);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    peilvFragment.onPlayRuleSelected(/*YiboPreference.instance(TouzhuActivity.this)
                                    .getGameVersion()*/cpVersion,
                            cpTypeCode, selectPlayCode, selectRuleCode,
                            selectPlayName, selectSubPlayName, maxBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                }
            }, 200);

        } else {
            jiangjinFragment = new JiangjinFragment();
            jiangjinFragment.setBetListener(new TouzhuJianjinListener());
            jiangjinFragment.setCpBianHao(cpBianHao);
            FragmentManager fm = this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, jiangjinFragment);
            ft.commit();

            startProgress();
            getKaiJianResult(cpBianHao);//获取开奖结果
            UsualMethod.getCountDownByCpcode(TouzhuActivity.this, cpBianHao, COUNT_DOWN_REQUEST);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    jiangjinFragment.onPlayRuleSelected(/*YiboPreference.instance(TouzhuActivity.this)
                                    .getGameVersion()*/cpVersion, cpTypeCode, selectPlayCode, selectRuleCode,
                            selectPlayName, selectSubPlayName, minBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                }
            }, 200);
        }
        //首次使用时，提示引导摇一摇投注功能
        //将玩法显示在左下方
        refreshPlayRuleExpandListView();

        startSyncLhcServerTime();

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

    @Override
    protected void onResume() {
        super.onResume();
        accountWeb();
    }

    public ChatRoomUtils chatRoomUtils; //主页的聊天室方法

    private void initOnGetLotteryJson(String lottery){
        LotteryData lotteryData = new Gson().fromJson(lottery, LotteryData.class);
        updateLotteryConstants(lotteryData);
        //初始化大小玩法显示栏
        if (playRules == null) {
            return;
        }
        if (playRules.isEmpty()) {
            showToast("没有玩法，请检查平台是否开启玩法");
            return;
        }
        PlayItem defaultPlayItem = playRules.get(0);
        if (defaultPlayItem != null) {
            selectPlayCode = defaultPlayItem.getCode();
            selectPlayName = defaultPlayItem.getName();
            List<SubPlayItem> rules = defaultPlayItem.getRules();
            if (rules != null && !rules.isEmpty()) {
                maxBetNumber = rules.get(0).getMaxBetNum();
                selectRuleCode = rules.get(0).getCode();
                selectSubPlayName = rules.get(0).getName();
                minBounds = rules.get(0).getMinBonusOdds();
                maxBounds = rules.get(0).getMaxBounsOdds();
                minRakeback = rules.get(0).getMinRakeback();
                winExample = rules.get(0).getWinExample();
                detailDesc = rules.get(0).getDetailDesc();
                playMethod = rules.get(0).getPlayMethod();
            }
        }
    }

    private void refreshOnGetLotteryData(LotteryData lotteryData){
        updateLotteryConstants(lotteryData);
        //初始化大小玩法显示栏
        PlayItem defaultPlayItem = playRules.get(0);
        if (defaultPlayItem != null) {
            selectPlayCode = defaultPlayItem.getCode();
            selectPlayName = defaultPlayItem.getName();
            List<SubPlayItem> rules = defaultPlayItem.getRules();
            if (rules != null && !rules.isEmpty()) {
                maxBetNumber = rules.get(0).getMaxBetNum();
                selectRuleCode = rules.get(0).getCode();
                selectSubPlayName = rules.get(0).getName();
                minBounds = rules.get(0).getMinBonusOdds();
                maxBounds = rules.get(0).getMaxBounsOdds();
                minRakeback = rules.get(0).getMinRakeback();
                winExample = rules.get(0).getWinExample();
                detailDesc = rules.get(0).getDetailDesc();
                playMethod = rules.get(0).getPlayMethod();
            }
        }
        //切换完彩种后大小玩法选择的位置要归0
        selectedMainPlayPosition = 0;
        selectedSubPlayPosition = 0;
        if (isPeilvVersion || lhcSelect) {
            if (peilvFragment == null) {
                peilvFragment = new PeilvFragment();
            }
            peilvFragment.setCpBianHao(cpBianHao);
            peilvFragment.setPeilvListener(new TouzhuPeilvListener());
            FragmentManager fm = this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, peilvFragment);
            ft.commit();
            startProgress();
            getKaiJianResult(cpBianHao);
            UsualMethod.getCountDownByCpcode(TouzhuActivity.this, cpBianHao, COUNT_DOWN_REQUEST);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    peilvFragment.onPlayRuleSelected(String.valueOf(Constant.lottery_identify_V2),
                            cpTypeCode, selectPlayCode, selectRuleCode,
                            selectPlayName, selectSubPlayName, maxBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                }
            }, 200);
        } else {
            if (jiangjinFragment == null) {
                jiangjinFragment = new JiangjinFragment();
            }
            jiangjinFragment.setBetListener(new TouzhuJianjinListener());
            jiangjinFragment.setCpBianHao(cpBianHao);
            FragmentManager fm = this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, jiangjinFragment);
            ft.commit();

            startProgress();
            getKaiJianResult(cpBianHao);//获取开奖结果
            UsualMethod.getCountDownByCpcode(TouzhuActivity.this, cpBianHao, COUNT_DOWN_REQUEST);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    jiangjinFragment.onPlayRuleSelected(cpVersion, cpTypeCode, selectPlayCode, selectRuleCode,
                            selectPlayName, selectSubPlayName, minBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
                }
            }, 200);
        }
        onBottomUpdate(0, 0);
        //将玩法显示在左下方
        refreshPlayRuleExpandListView();
    }

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
                    if (UsualMethod.checkIsLogin(TouzhuActivity.this)) {
                        Intent intent = new Intent(TouzhuActivity.this, ChatMainActivity.class);
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
                    if (!YiboPreference.instance(TouzhuActivity.this).isLogin()) {
                        SysConfig sc = UsualMethod.getConfigFromJson(TouzhuActivity.this);
                        if (sc != null && sc.getNewmainpage_switch().equals("on")) {
                            LoginAndRegisterActivity.createIntent(TouzhuActivity.this, YiboPreference.instance(getApplicationContext()).getUsername(), "", 0);
                        }else{
                            LoginActivity.createIntent(TouzhuActivity.this, YiboPreference.instance(getApplicationContext()).getUsername(), "");
                        }
                        return;
                    }
                    SysConfig config = UsualMethod.getConfigFromJson(TouzhuActivity.this);
                    if (config != null && !TextUtils.isEmpty(config.getChat_foreign_link())) {
                        UsualMethod.viewLink(TouzhuActivity.this, config.getChat_foreign_link().trim());
                        return;
                    }
                    if (chatRoomUtils == null) {
                        chatRoomUtils = new ChatRoomUtils(TouzhuActivity.this);
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
                    peilvFragment.setRuleSelectCallback(new RuleSelectListener());
                    peilvFragment.updatePlayRules(playRules);
                } else {
                    jiangjinFragment.setRuleSelectCallback(new RuleSelectListener());
                    jiangjinFragment.updatePlayRules(playRules);
                }
            }
        }, 200);
    }

    private final class RuleSelectListener implements PlayRuleExpandAdapter.RuleSelectCallback {
        @Override
        public void onRuleCallback(PlayItem playItem, SubPlayItem item) {
            if (playItem == null || item == null) {
                return;
            }
            //若此玩法在后台配置中处于关闭状态，则直接返回
            if (!playItem.isOpenStatus()) {
                showToast(R.string.play_disable_from_web);
                return;
            }

            selectPlayCode = playItem.getCode();
            selectPlayName = playItem.getName();
            maxBetNumber = item.getMaxBetNum();
            selectRuleCode = item.getCode();
            selectSubPlayName = item.getName();
            minBounds = item.getMinBonusOdds();
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
                peilvFragment.onPlayRuleSelected(lhcSelect ? String.valueOf(Constant.lottery_identify_V2) :
                                cpVersion, cpTypeCode, selectPlayCode, selectRuleCode,
                        selectPlayName, palyNameShowInView, maxBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
            } else {
                jiangjinFragment.onPlayRuleSelected(cpVersion, cpTypeCode, selectPlayCode, selectRuleCode,
                        selectPlayName, selectSubPlayName, minBounds, minRakeback, currentQihao, cpName, cpDuration, maxBetNumber);
            }
        }
    }

    /**
     * 同步六合彩服务器当前时间
     */
    public void startSyncLhcServerTime() {
        if (!UsualMethod.isXGSixMark(this.cpBianHao)) {
            return;
        }
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.get_server_bettime_for_lhc);
        configUrl.append("?lotCode=").append(cpBianHao);

        CrazyRequest<CrazyResult<LhcServerTimeWrapper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(SYNC_LHC_TIME)
                .headers(Urls.getHeader(this))
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


    private void showGestureWindow() {

        if (gestureShakePop == null) {
            View showPupWindow = LayoutInflater.from(this).inflate(R.layout.gesture_shake_window, null);
            showPupWindow.findViewById(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gestureShakePop != null && gestureShakePop.isShowing()) {
                        gestureShakePop.dismiss();
                        gestureShakePop = null;
                    }
                }
            });
            gestureShakePop = new PopupWindow(showPupWindow, screen_width, screen_height);
            /* 设置触摸外面时消失 */
            gestureShakePop.setOutsideTouchable(true);
            gestureShakePop.update();
            gestureShakePop.setTouchable(true);
            gestureShakePop.setBackgroundDrawable(new BitmapDrawable());
            /* 设置点击menu以外其他地方以及返回键退出 */
            showPupWindow.setFocusableInTouchMode(true);
        }

        setDimEffect(gestureShakePop);
        gestureShakePop.showAsDropDown(findViewById(R.id.title), 0, 0);
        YiboPreference.instance(this).setShowShakeGesture(true);
    }

    private final class TouzhuJianjinListener implements JiangjinFragment.BetListener {

        @Override
        public void onBetPost() {
            actionJianjinTouzhu();
        }
    }

    private final class TouzhuPeilvListener implements PeilvFragment.PeilvListener {
        @Override
        public void onPeilvAcquire(String playCode, boolean showDialog) {
            getPeilvData(cpTypeCode, playCode, false);
        }

        @Override
        public void onBetPost(List<PeilvPlayData> selectDatas, boolean isMulSelect, String money) {
            if (UsualMethod.getConfigFromJson(TouzhuActivity.this).getConfirm_dialog_before_bet().equalsIgnoreCase("on")) {
                showConfirmBetDialog(selectDatas, isMulSelect, money);
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
        cpTypeCode = lotteryData.getCzCode();
        cpBianHao = lotteryData.getCode();
        cpName = lotteryData.getName();
        cpDuration = lotteryData.getDuration();
        ago = lotteryData.getAgo() != null ? lotteryData.getAgo().intValue() : 0;
        tvMiddleTitle.setText(lotteryData.getName());
    }

    /**
     * 开始获取最近开奖结果
     *
     * @param bianHao 彩种编码
     */
    private void getKaiJianResult(String bianHao) {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_LAST_RESULT_URL);
        configUrl.append("?code=").append(bianHao);
        CrazyRequest<CrazyResult<LastResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(LAST_RESULT_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LastResultWraper>() {
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
        playSelector.getLocationOnScreen(location);// 获取控件在屏幕中的位置,方便展示Popupwindow
        animation = new TranslateAnimation(0, 0, location[1], location[1]);
        animation.setDuration(500);
    }

    @Override
    protected void initView() {
        super.initView();
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setOnClickListener(this);
        tvSecondTitle.setVisibility(View.GONE);
//        rightIcon.setVisibility(isPeilvVersion?View.GONE:View.VISIBLE);
        titleLayout.setOnClickListener(this);
        tvMiddleTitle.setOnClickListener(this);
        titleIndictor.setVisibility(View.VISIBLE);

        balanceTV = (TextView) findViewById(R.id.balance);
        playSelector = (LinearLayout) findViewById(R.id.play_selector);
        playText = (TextView) playSelector.findViewById(R.id.playname);
        playSelector.setOnClickListener(this);

        openLayout = (LinearLayout) findViewById(R.id.notice_layout);
        openLayout.setOnClickListener(this);
        kjResult = (TextView) findViewById(R.id.kaijian_result);
        unNormalTV = (TextView) findViewById(R.id.unnormal_open_result);
        numberGridView = (GridView) findViewById(R.id.open_numbers);
        numberGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KaijianListActivity.createIntent(TouzhuActivity.this, cpName, cpBianHao, cpTypeCode);
            }
        });


        /**
         * 初始化悬浮球
         */
        if (UsualMethod.getConfigFromJson(this) != null &&
                UsualMethod.getConfigFromJson(this).getOnoff_chat().equalsIgnoreCase("on") && UsualMethod.getConfigFromJson(this).getChat_icon_in_betpage_switch().equals("on")) {
            initFloatBall();
        }
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
                Utils.LOG(TAG, "开始获取上一期开奖结果-----------");
                lastResultAskTimer = null;
                //当前期数投注时间到时，继续请求同步服务器上下一期号及离投注结束倒计时时间
//                showToast(R.string.sync_last_resulting);
                //获取下一期的倒计时的同时，获取上一期的开奖结果
                getKaiJianResult(cpBianHao);
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
                String newQihao = currentQihao;
                if (currentQihao.startsWith("20") && currentQihao.length() > 7) {
                    newQihao = currentQihao.substring(4);
                }
                long time = millisUntilFinished;
                playText.setText(String.format(getString(R.string.kaijian_deadline_format),
                        String.valueOf(newQihao), Utils.int2Time(time)));
            }

            public void onFinish() {
                String newQihao = currentQihao;
                if (currentQihao.startsWith("20") && currentQihao.length() > 7) {
                    newQihao = currentQihao.substring(4);
                }
//                playText.setText("第" + newQihao + "期:" + getString(R.string.stop_touzhu));
                endlineTouzhuTimer = null;//置空当前期号倒计时器
                boolean isWarmRemind = YiboPreference.instance(TouzhuActivity.this)
                        .isWarmRemind();
                if (isWarmRemind) {
//                    弹框提示当前期号投注结束
                    showToastTouzhuEndlineDialog(currentQihao);
                }
                //当前期数投注时间到时，继续请求同步服务器上下一期号及离投注结束倒计时时间
                startProgress();
                UsualMethod.getCountDownByCpcode(TouzhuActivity.this, cpBianHao, COUNT_DOWN_REQUEST);
                //截止下注后再过一段开封盘时间ago，获取一次开奖结果
                Utils.LOG(TAG, "ago ===== " + ago);
                //截止下注时先获取一次开奖结果
                getKaiJianResult(cpBianHao);
            }
        };
    }

    private Disposable disposable;

    //

    /**
     * @param currentQihao
     * @param duration     剩余封盘时间段，单位秒
     */
    public void setFengPaningOperation(final String currentQihao, final int duration) {
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
//                        playText.setText(String.format(getString(R.string.kaijian_deadline_format),
//                                String.valueOf(Long.parseLong(currentQihao) + 1)));

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
                        playText.setText(String.format(getString(R.string.kaijian_deadline_format_open),
                                String.valueOf(Long.parseLong(currentQihao) + 1), Utils.int2Time(aLong * 1000)));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //执行结束之后为不封盘状态
                        setFengPan(false);
                        //获取下一期结果
                        startProgress();
                        UsualMethod.getCountDownByCpcode(TouzhuActivity.this, cpBianHao, COUNT_DOWN_REQUEST);
                        //封盘时间倒计时结束后，先请求一次开奖结果
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Utils.LOG(TAG, "get result after fenpang -------");
                                getKaiJianResult(cpBianHao);
                            }
                        }, 2000);
                    }
                });
    }


    private void setFengPan(boolean isFengPan) {
        if (peilvFragment != null) {
            peilvFragment.setFengPan(isFengPan);
        }
        if (jiangjinFragment != null) {
            jiangjinFragment.setFengPan(isFengPan);
        }
    }

    /**
     * 弹框投注时间已到
     *
     * @param currentQihao
     */
    CustomConfirmDialog ccd;

    private void showToastTouzhuEndlineDialog(String currentQihao) {

        //如果当前activity不在栈顶
//        if (Utils.isActivityOnTop(this)) {
//            return;
//        }
        //判断当前activity如果不在前台
        if (!Utils.isForeground(TouzhuActivity.this)) {
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
                boolean hasAsk = YiboPreference.instance(TouzhuActivity.this).isWarmRemind();
                YiboPreference.instance(TouzhuActivity.this).setWarmRemind(!hasAsk);
            }
        });
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
//                startProgress();
//                UsualMethod.getCountDownByCpcode(TouzhuActivity.this, cpBianHao, COUNT_DOWN_REQUEST);
            }
        });
        ccd.setCanceledOnTouchOutside(false);
        ccd.setCancelable(false);
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    /**
     * 展示大小玩法选择的弹出面板
     */
//    private void showPlayPopupWindow() {
//
//        if (playPopupWindow == null) {
//            showPupWindow = LayoutInflater.from(this).inflate(R.layout.play_rule_popup_layout, null);
//            initPlayWindow(showPupWindow);
//            groupListView = (ListView) showPupWindow.findViewById(R.id.listView1);
//            childListView = (ListView) showPupWindow.findViewById(R.id.listView2);
//            groupAdapter = new PlayRuleMenuGroupAdapter(this, playRules,R.layout.group_item_layout);
//            groupListView.setAdapter(groupAdapter);
//            childAdapter = new PlayRuleMenuChilddapter(this,
//                    playRules.get(selectedMainPlayPosition).getRules(),R.layout.child_item_layout);
//            childListView.setAdapter(childAdapter);
//        }else{
//            groupAdapter = new PlayRuleMenuGroupAdapter(this, playRules,R.layout.group_item_layout);
//            childAdapter = new PlayRuleMenuChilddapter(this,
//                    playRules.get(selectedMainPlayPosition).getRules(),R.layout.child_item_layout);
//            groupAdapter.setSelectPos(selectedMainPlayPosition);
//            childAdapter.setSelectPos(selectedSubPlayPosition);
//            groupListView.setAdapter(groupAdapter);
//            childListView.setAdapter(childAdapter);
//        }
//
//        setDimEffect(playPopupWindow);
////        groupListView.setOnItemClickListener(new MainPlayItemClick());
////        childListView.setOnItemClickListener(new SubPlayItemClick());
//
//        showPupWindow.setAnimation(animation);
//        showPupWindow.startAnimation(animation);
//        playPopupWindow.showAsDropDown(playSelector,0,5);
//    }

    /**
     * 初始化 PopupWindow
     *
     * @param view
     */
    public void initPlayWindow(View view) {
        /* 第一个参数弹出显示view 后两个是窗口大小 */
        playPopupWindow = new PopupWindow(view, screen_width, screen_height / 2);
        /* 设置触摸外面时消失 */
        playPopupWindow.setOutsideTouchable(true);
        playPopupWindow.update();
        playPopupWindow.setTouchable(true);
        playPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        /* 设置点击menu以外其他地方以及返回键退出 */
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    if (playPopupWindow != null && playPopupWindow.isShowing()) {
                        playPopupWindow.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void updatePlayMenuState() {
        tabStateArr = !tabStateArr;
    }


    @Override
    public void onBottomUpdate(int zhushuOrOrderNum, double totalMoney) {
        if (isPeilvVersion || lhcSelect) {
            if (peilvFragment == null) {
                return;
            }
            peilvFragment.updateBottom(zhushuOrOrderNum, totalMoney);
        } else {
            if (jiangjinFragment == null) {
                return;
            }
            jiangjinFragment.updateBottom(zhushuOrOrderNum, totalMoney, minBounds);
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
            LastResultWraper reg = (LastResultWraper) regResult;
            if (!reg.isSuccess()) {
//                showToast(!Utils.isEmptyString(reg.getMsg())?reg.getMsg():
//                        getString(R.string.acquire_last_result_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                //更新开奖结果
                kjResult.setText("第?????期：暂无开奖结果");
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //更新开奖结果
            updateLastResult(reg.getContent());
        } else if (action == COUNT_DOWN_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
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
            if (!result.crazySuccess) {
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
                playText.setText(String.format(getString(R.string.kaijian_deadline_format),
                        String.valueOf(currentQihao), "停止下注"));
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //更新当前这期离结束投注的倒计时显示
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
                refreshOnGetLotteryData(reg.getContent());
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
            peilvFragment.setLhcServerTime(reg.getContent());
        } else if (action == GET_LOTTERY_NOTICE){
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
            String leftMoneyName = String.format("可用余额:%.2f元", Double.parseDouble(meminfo.getBalance()));
            balanceTV.setText(leftMoneyName);
        }
    }

    private void saveCurrentLotData() {
        SavedGameData data = new SavedGameData();
        data.setGameModuleCode(SavedGameData.LOT_GAME_MODULE);
        data.setAddTime(System.currentTimeMillis());
        data.setLotName(cpName);
        data.setLotCode(cpBianHao);
        data.setLotType(cpTypeCode);
        data.setPlayName(selectPlayName);
        data.setPlayCode(selectPlayCode);
        data.setSubPlayName(selectSubPlayName);
        data.setDuration(cpDuration);
        data.setSubPlayCode(selectRuleCode);
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
                sb.append(cpBianHao).append("|");
                sb.append(order.getSubPlayCode()).append("|");
                sb.append(UsualMethod.convertPostMode(order.getMode())).append("|");
                sb.append(order.getBeishu()).append("|");
                sb.append(order.getNumbers());
                betsArray.put(sb.toString());
            }

            JSONObject content = new JSONObject();
            content.put("lotCode", cpBianHao);
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
                    YiboPreference.instance(TouzhuActivity.this).setToken(result.getAccessToken());
                    showToast(R.string.dobets_success);
                    saveCurrentLotData();
                    accountWeb();
                    //下注成功后清除选择的号码并刷新页面
                    jiangjinFragment.clearViewAfterBetSuccess();
                }else {
                    showToast(TextUtils.isEmpty(result.getMsg()) ? "下注时出现错误，请重新下注" : result.getMsg());
                    if (result.getMsg().contains("登陆超时")) {
                        UsualMethod.loginWhenSessionInvalid(TouzhuActivity.this);
                    }
                }
            }
        });
    }

    String compareResult;

    /**
     * 更新开奖结果
     *
     * @param bcLotteryData
     */
    private void updateLastResult(BcLotteryData bcLotteryData) {
        if (bcLotteryData == null) {
            return;
        }
        if (Utils.isEmptyString(bcLotteryData.getQiHao()) || Utils.isEmptyString(bcLotteryData.getHaoMa())) {
            return;
        }
        //先判断开奖结果是否有效，如果有效则不需要继续获取开奖结果
        handleAcqureLastResult(bcLotteryData.getHaoMa());
        compareResult = bcLotteryData.getQiHao();
        kjResult.setText(String.format(getString(R.string.qihao_string3), bcLotteryData.getQiHao()));
        if (!Utils.isEmptyString(bcLotteryData.getHaoMa()) && bcLotteryData.getHaoMa().contains(",")) {
            List<String> haomas = Utils.splitString(bcLotteryData.getHaoMa(), ",");
            numberGridView.setVisibility(View.VISIBLE);
            unNormalTV.setVisibility(View.GONE);
            updateNumberGridView(haomas, cpTypeCode);
        } else {
            numberGridView.setVisibility(View.GONE);
            unNormalTV.setVisibility(View.VISIBLE);
            unNormalTV.setText(bcLotteryData.getHaoMa());
        }
        //更新完开奖结果后，延迟5秒获取一下帐户余额
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                accountWeb();
            }
        }, 5000);
    }

    private void handleAcqureLastResult(String currentHaoMa) {
        //奖金版时需要是截止下注后定期去获取一次最近那一期的开奖结果；因为可能刚截止还不能获取到开奖号码
        boolean isEffective = true;
        if (!Utils.isEmptyString(currentHaoMa) && currentHaoMa.contains(",")) {
            List<String> haomas1 = Utils.splitString(currentHaoMa, ",");
            for (String hm : haomas1) {
                if (!Utils.isNumeric(hm) || hm.equalsIgnoreCase("?") || lastOpenResult.equalsIgnoreCase(currentHaoMa)) {
                    isEffective = false;
                }
            }
        }
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
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        int screenWidth = dm.widthPixels;
//        int column = (int) ((screenWidth - Utils.dip2px(this,120))/
//                Utils.dip2px(this,30));
//        Utils.LOG(TAG,"the figure out column == "+column);


        if (cpTypeCode.equals("6") || cpTypeCode.equals("66")) {
            getOpenResultDetails(haoMaList, lotType);
        } else {
            updateGridView(haoMaList, lotType, null);
        }


    }


    private void getOpenResultDetails(final List<String> haoMaList, final String lotType) {
        ApiParams params = new ApiParams();
        params.put("lotCode", cpBianHao);
        params.put("page", 1);
        params.put("row", 5);
        HttpUtil.get(this, Urls.OPEN_RESULT_DETAIL_URL, params, false, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {

                    List<OpenResultDetail> kjList = new Gson().fromJson(result.getContent(), new TypeToken<List<OpenResultDetail>>() {
                    }.getType());

                    String kj = compareResult;
                    String date;
                    if (kjList.get(0).getQiHao().equals(kj)) {
                        date = kjList.get(0).getDate();
                    } else if (kjList.get(1).getQiHao().equals(kj)) {
                        date = kjList.get(1).getDate();
                    } else if (kjList.get(2).getQiHao().equals(kj)) {
                        date = kjList.get(2).getDate();
                    } else if (kjList.get(3).getQiHao().equals(kj)) {
                        date = kjList.get(3).getDate();
                    } else if (kjList.get(4).getQiHao().equals(kj)) {
                        date = kjList.get(4).getDate();
                    } else {
                        date = "";
                    }
                    updateGridView(haoMaList, lotType, date);
                }
            }
        });
    }


    private void updateGridView(List<String> haoMaList, String lotType, String date) {
        numberGridView.setNumColumns(10);
        NumbersAdapter numbersAdapter = new NumbersAdapter(this, haoMaList, R.layout.touzhu_number_gridview_item, lotType, cpBianHao);
        numbersAdapter.setShowShenxiao(true);
        numbersAdapter.setDate(date);
        numberGridView.setAdapter(numbersAdapter);
        Utils.setListViewHeightBasedOnChildren(numberGridView, 10);
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
        long duration = Math.abs(activeTime - serverTime);
        currentQihao = countDown.getQiHao();
        if (peilvFragment != null)
            peilvFragment.setCurrentQihao(currentQihao);
        Utils.LOG(TAG, "current qihao open duration = " + countDown.getQiHao() + "," + duration);
        if (endlineTouzhuTimer != null) {
            endlineTouzhuTimer.cancel();
            endlineTouzhuTimer = null;
        }
        if (UsualMethod.getConfigFromJson(this).getNative_fenpang_bet_switch().equalsIgnoreCase("on")) {
            createEndlineTouzhuTimer(duration, countDown.getQiHao());
            //开始离投注结束时间倒计时
            endlineTouzhuTimer.start();
        } else {
            if (duration > ago * 1000) {
                duration = duration - ago * 1000;
                createEndlineTouzhuTimer(duration, countDown.getQiHao());
                //开始离投注结束时间倒计时
                endlineTouzhuTimer.start();
            } else {
                if (!UsualMethod.getConfigFromJson(TouzhuActivity.this).getWaitfor_openbet_after_bet_deadline().equalsIgnoreCase("on")) {
                    //当前时间差小于开封盘时间时；说明处于封盘中，直接倒计时剩下的封盘时间即可
                    setFengPaningOperation(currentQihao, (int) (duration / 1000));
                } else {
                    createEndlineTouzhuTimer(duration, countDown.getQiHao());
                    //开始离投注结束时间倒计时
                    endlineTouzhuTimer.start();
                }
            }
        }
    }

    private final class FrameDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            updatePlayMenuState();
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1.0f;
            getWindow().setAttributes(params);
        }
    }

    public void setDimEffect(PopupWindow popupWindow) {
        popupWindow.setOnDismissListener(new FrameDismissListener());
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
    }


    private void clearZhushuBottonView() {
        if (isPeilvVersion || lhcSelect) {
            peilvFragment.updateBottom(0, 0);
        } else {
            jiangjinFragment.updateBottom(0, 0, 0);
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
                        if(!playCode.equals(peilvFragment.selectRuleCode)){
                            return;
                        }

                        if (!reg.isSuccess()) {
                            if (TextUtils.isEmpty(reg.getMsg()) || reg.getMsg().equals(getString(R.string.no_peilv_data))) {
                                showToast("没有赔率数据，请联系客服");
                                YiboPreference.instance(getApplicationContext()).setToken(reg.getAccessToken());
                                peilvFragment.updatePlayArea();
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
                        YiboPreference.instance(getApplicationContext()).setToken(reg.getAccessToken());
                        //更新赔率面板号码区域赔率等数据
                        peilvFragment.updatePlayArea(reg.getContent());
                    }

                    @Override
                    public void onError(Throwable e) {showToast(R.string.acquire_peilv_fail);}
                });
    }

    //点击标题栏彩种，下拉出所有彩种
    private void clickTitleDown() {

        //每次切换之后刚开始都不会封盘
        setFengPan(false);
        //停止之前的计时器
        if (disposable != null) {
            disposable.dispose();
        }

        caiZhongSelectorWindow = new CaizhongWindow(this);
        caiZhongSelectorWindow.setCaizhongSelectListener(new CaizhongWindow.CaizhongSelectListener() {
            @Override
            public void onLotterySelect(LotteryData data) {
                if (data == null) {
                    return;
                }
                if (UsualMethod.isSixMark(getApplicationContext(), data.getCode())) {
                    lhcSelect = true;
                    isPeilvVersion = true;
                } else {
                    lhcSelect = false;
                    if (UsualMethod.isPeilvVersionMethod(TouzhuActivity.this)) {
                        isPeilvVersion = true;
                    } else {
                        isPeilvVersion = false;
                    }
                }
                if (!isPeilvVersion && !lhcSelect) {
                    if (jiangjinFragment != null) {
                        jiangjinFragment.clearZhushuBottonView();
                        jiangjinFragment.updateBottomClearBtn();
                    }
                }
//                if (!UsualMethod.isXGSixMark(data.getCode())) {
//                    peilvFragment.setLhcServerTime(0);
//                }
                //重新选择彩种后，需要重新获取彩种对应的玩法
                currentGameCode = data.getCode();
                refreshLotteryPlayRules(currentGameCode, true);
            }
        });
        caiZhongSelectorWindow.setData(cpBianHao);
        caiZhongSelectorWindow.showWindow(findViewById(R.id.title));
    }

    private void refreshLotteryPlayRules(String gameCode, boolean showLoading){
        CacheRepository.getInstance().loadLotteryPlayJson(this, gameCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) { compositeDisposable.add(d); }

                    @Override
                    public void onSuccess(String json) {
                        Utils.LOG(TAG, "find the backup of play rules");
                        LotteryData lotteryData = new Gson().fromJson(json, LotteryData.class);
                        refreshOnGetLotteryData(lotteryData);
                        UsualMethod.syncLotteryPlaysByCode(TouzhuActivity.this, gameCode,
                                false, ACQUIRE_PLAYS_REQUEST, TouzhuActivity.this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.LOG(TAG, "can't find the backup of play rules");
                        e.printStackTrace();
                        UsualMethod.syncLotteryPlaysByCode(TouzhuActivity.this, gameCode,
                                true, ACQUIRE_PLAYS_REQUEST, TouzhuActivity.this);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notice_layout:
                KaijianListActivity.createIntent(this, cpName, cpBianHao, cpTypeCode);
                break;
            case R.id.clear_btn:
                if (/*UsualMethod.isPeilvVersion(this)*/isPeilvVersion || lhcSelect) {
                    peilvFragment.onPlayClean(false);
                } else {
                    jiangjinFragment.onPlayClean(false);
                }
                break;
            case R.id.touzhu_btn:
                if (/*UsualMethod.isPeilvVersion(this)*/isPeilvVersion || lhcSelect) {
                    peilvFragment.onPlayTouzhu();
                } else {
                    jiangjinFragment.onPlayTouzhu();
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
                    peilvFragment.showZhudangWindow();
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
                CaipiaoOrderActivity.createIntent(this, cpTypeCode, cpBianHao, cpName, cpVersion);
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
        final ArrayList<String> arrayList = new ArrayList<>();
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
        final String cpVersion = YiboPreference.instance(TouzhuActivity.this).getGameVersion();
        PopupListMenu menu = new PopupListMenu(this, arrays);
        menu.setBackground(R.drawable.caipiao_item_bg);
        menu.setDimEffect(true);
        menu.setAnimation(true);
        menu.setOnItemClickListener(new PopupListMenu.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                switch (arrayList.get(position)) {
                    case "投注记录":
                        actionTouzhuRecord(cpBianHao);
                        break;
                    case "历史开奖":
                        KaijianListActivity.createIntent(TouzhuActivity.this, cpName, cpBianHao, cpTypeCode);
                        break;
                    case "玩法说明":
                        String url = Urls.BASE_URL + Urls.PORT + Urls.LOTTERY_RULES_URL +
                                "?lotCode=" + cpBianHao + "&source=1";
                        ActiveDetailActivity.createIntent(TouzhuActivity.this,
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
                        AppSettingActivity.createIntent(TouzhuActivity.this);
//                        }
                        break;
                    case "走势图":
                        Intent intent = new Intent(TouzhuActivity.this, KefuActivity.class);
                        intent.putExtra("url", Urls.BASE_URL + Urls.ZHOUSHITU + "?lotCode=" + cpBianHao);
                        intent.putExtra("title", "走势图");
                        startActivity(intent);
                        break;

                    case "长龙":
                        Intent intent1 = new Intent(TouzhuActivity.this, LongLonngActivity.class);
                        intent1.putExtra("code", cpBianHao);
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
                actionTouzhuRecord(cpBianHao);
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
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }


    private void showConfirmBetDialog(final List<PeilvPlayData> selectDatas, final boolean isMulSelect, final String money) {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(2);
        String content = "是否提交下注？";
        ccd.setContent(content);
        ccd.setTitle("温馨提示");
        ccd.setLeftBtnText("取消");
        ccd.setRightBtnText("确定");
        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        ccd.setRightBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                realPeilvPostBets(selectDatas, isMulSelect, money);
                ccd.dismiss();
            }
        });
        ccd.setCanceledOnTouchOutside(false);
        ccd.setCancelable(false);
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    private void showWinlostDialog(WinLostWraper wraper) {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(1);

        String content = "";
        if (wraper != null && wraper.getContent() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("今日消费:").append(BigDecimal.valueOf(wraper.getContent().getAllBetAmount())).append("元").append("\n");
            sb.append("今日中奖:").append(BigDecimal.valueOf(wraper.getContent().getAllWinAmount())).append("元").append("\n");
            sb.append("今日盈亏:").append(BigDecimal.valueOf(wraper.getContent().getYingkuiAmount())).append("元").append("\n");
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

//    private void actionZuihao() {
//        boolean success = jiangjinFragment.prepareBetOrders();
//        if (!success) {
//            return;
//        }
//        List<OrderDataInfo> data = DatabaseUtils.getInstance(this).getCartOrders();
//        if (data == null || data.isEmpty()) {
//            showToast(R.string.please_touzhu_first);
//            return;
//        }
//        Type listType = new TypeToken<ArrayList<OrderDataInfo>>() {
//        }.getType();
//        String zhuJsons = new Gson().toJson(data, listType);
//        BraveZuiHaoActivity.createIntent(this, zhuJsons, cpBianHao, cpName, selectPlayName,
//                selectPlayCode, selectSubPlayName, selectRuleCode, cpDuration);
//    }

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

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, TouzhuActivity.class);
        context.startActivity(intent);
    }

    public static void createIntent(Context context, String lotteryJson, String gameCode,
                                    boolean isPeilvVersion, String cpVerison) {
        createIntent(context, lotteryJson, gameCode, false, isPeilvVersion, cpVerison);
    }

    public static void createIntent(Context context, String lotteryJson, String gameCode,
                                    boolean needRefresh, boolean isPeilvVersion, String cpVerison) {
        Intent intent = new Intent(context, TouzhuActivity.class);
        intent.putExtra("lottery", lotteryJson);
        intent.putExtra("gameCode", gameCode);
        intent.putExtra("needRefresh", needRefresh);
        intent.putExtra("isPeilvVersion", isPeilvVersion);
        intent.putExtra("cpVersion", cpVerison);
        context.startActivity(intent);
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
            for(PeilvPlayData playData: betDatas) {
                if (map.containsKey(playData.getItemName())) {
                    map.put(playData.getItemName(), map.get(playData.getItemName()) + 1);
                } else {
                    map.put(playData.getItemName(), 1);
                }

                if (map.get(playData.getItemName()) > maxBetNumber) {
                    showToast("投注失败,超过最大限制码数[" + maxBetNumber + "]码");
                    return;
                }
            }
        }


        boolean isMulSelect = mulSelect;
        //构造下注POST数据
        try {
            String url;
            ApiParams params = new ApiParams();
            String betIp = ChatSpUtils.instance(this).getBET_IP();
            if (UsualMethod.isSixMark(this, cpBianHao)) {
                url = Urls.DO_SIX_MARK_URL;
                //六合彩连码四全中最多只能选4个号码
                if (PlayCodeConstants.lianma.equals(selectPlayCode) && "siqz".equalsIgnoreCase(selectRuleCode)) {
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
                if (isMulSelect) {
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


//                    else if (this.selectPlayCode.equals(PlayCodeConstants.quanbuzhong) && betDatas.size() > 10) {
//                        showToast(String.format(Locale.CHINA,"所选球数请勿大于%d", 10));
//                        return;
//                    }


                    //若是全不中玩法，则所选号码不能大于各子玩法指定的数
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
                    PeilvWebResult webResult = UsualMethod.getPeilvData(this, cpBianHao, selectPlayCode, betDatas.size(), pData);
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
                        item.put("betIp", betIp);
                        item.put("markSixId", webResult.getId());
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
                        item.put("betIp", betIp);
                        item.put("markSixId", String.valueOf(data.getPeilvData() != null ? data.getPeilvData().getId() : 0));
                        betsArray.put(item);
                    }
                    Utils.LOG(TAG, "the betsarray == " + betsArray.toString());
                }

                //构造下注crazy request
                JSONObject dataPost = new JSONObject();
                dataPost.put("pour", betsArray);
                dataPost.put("lotCode", cpBianHao);
                dataPost.put("playCode", selectRuleCode);
                dataPost.put("state", isMulSelect ? 1 : 2);
                dataPost.put("qiHao", currentQihao);
                params.put("data", dataPost.toString());
            } else {
                url = Urls.DO_PEILVBETS_URL;
                String betJson = "";
                PeilvPlayData pData = betDatas.get(0);
                if (isMulSelect) {
                    int minSelectCount = pData.getPeilvData() != null ? pData.getPeilvData().getMinSelected() : 0;
                    if (minSelectCount > betDatas.size()) {
                        showToast(String.format(getString(R.string.min_select_numbers_format), minSelectCount));
                        return;
                    }

//                    //将所选号码根据玩法标签名分类
//                    Map<String,List<PeilvPlayData>> datas = new LinkedHashMap();
//                    try {
//                        List<PeilvPlayData> list;
//                        for (int i = 0; i < betDatas.size(); i++) {
//                            PeilvPlayData item = betDatas.get(i);
//                            if (item == null) {
//                                continue;
//                            }
//                            //将每项赛事数据依据标签值-数据项一一对应装入Map
//                            if (!datas.containsKey(item.getItemName())) {
//                                list = new ArrayList<>();
//                                list.add(item);
//                                datas.put(item.getItemName(), list);
//                            }else{
//                                List<PeilvPlayData> m = (List<PeilvPlayData>) datas.get(item.getItemName());
//                                m.add(item);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    if (datas.size() > 1) {
//
//                        List<String> tagNums = new ArrayList<>();
//                        for (Map.Entry entry : datas.entrySet()) {
//                            StringBuilder sb = new StringBuilder();
//                            for (PeilvPlayData num : (List<PeilvPlayData>)entry.getValue()) {
//                                sb.append(num.getNumber()).append(",");
//                            }
//                            sb = sb.deleteCharAt(sb.length());
//                            tagNums.add(sb.toString());
//                        }
//
//                        //如果所选号码在多个玩法标签中，则组合所有下注号码
//                        List<String> comboResult = new ArrayList<>();
//                        UsualMethod.combination(arr, loop,comboResult);
//
//
//                        JSONObject item = new JSONObject();
//                        item.put("name", name);
//                        item.put("money", money);
//                        //从多个赔率数据中根据用户选择的选项数，选择出选项数正确的赔率数据
//
//                        PeilvWebResult webResult = UsualMethod.getPeilvData(cpBianHao, selectPlayCode, betDatas.size(), pData);
//                        if (webResult != null) {
//                            //先判断下注的总金额是否在最大和最小下注金额之前
//                            if (!Utils.isEmptyString(money)) {
//                                float moneyFloat = Float.parseFloat(money);
//                                boolean rightMoney = moneyLimit(webResult.getMaxBetAmmount(), webResult.getMinBetAmmount(), moneyFloat);
//                                if (!rightMoney) {
//                                    return;
//                                }
//                            }
//                            item.put("oddsId", String.valueOf(webResult.getId()));
//                            item.put("rate", String.valueOf(webResult.getRakeBack()));
//                        }
//                        betsArray.put(item);
//
//                    }else{
//
//                    }

                    //时时彩,分分彩系列---组选六不能大于八位
                    if (this.selectRuleCode.equals(PlayCodeConstants.zuxuan_liu) && betDatas.size() > 8) {
                        showToast(String.format(Locale.CHINA, "最多选择%d个号码", 8));
                        return;
                    }

                    //选4任选选5任选最多只能选6个号码
                    if (this.selectRuleCode.equals(PlayCodeConstants.xuansirenxuan) && betDatas.size() > 6) {

                        showToast(String.format(Locale.CHINA, "最多选择%d个号码", 6));
                        return;
                    }
                    if (this.selectRuleCode.equals(PlayCodeConstants.xuanwurenxuan) && betDatas.size() > 6) {
                        showToast(String.format(Locale.CHINA, "最多选择%d个号码", 6));
                        return;
                    }

                    //若是连码情况，则所选号码不能大于10个
                    if (this.cpTypeCode.equals("12")) {
                        if (this.selectPlayCode.equals(PlayCodeConstants.lianma_peilv_klsf) && betDatas.size() > 8) {
                            showToast(String.format("所选球数请勿大于%d", 8));
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

                    //如果不是十一选五直选
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

                    PeilvWebResult webResult = UsualMethod.getPeilvData(this, cpBianHao, selectPlayCode, betDatas.size(), pData);
                    if (webResult != null) {
                        //先判断下注的总金额是否在最大和最小下注金额之前
                        if (!Utils.isEmptyString(money)) {
                            float moneyFloat = Float.parseFloat(money);
                            boolean rightMoney = moneyLimit(webResult.getMaxBetAmmount(), webResult.getMinBetAmmount(), moneyFloat);
                            if (!rightMoney) {
                                return;
                            }
                        }
                        item.put("odds", String.valueOf(webResult.getOdds()));
                        item.put("oddsId", String.valueOf(webResult.getId()));
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
                params.put("lotCode", cpBianHao);
                params.put("groupCode", selectPlayCode);
                params.put("playCode", selectRuleCode);
                params.put("qiHao", currentQihao);
                params.put("lotType", cpTypeCode);
                params.put("stId", pData.getPeilvData().getStationId());
            }

            HttpUtil.postForm(this, url, params, true, getString(R.string.bet_ongoing), new HttpCallBack() {
                @Override
                public void receive(NetworkResult result) {
                    if(result.isSuccess()){
                        YiboPreference.instance(TouzhuActivity.this).setToken(result.getAccessToken());
                        showToast(R.string.dobets_success);
                        accountWeb();
                        //下注成功后保存这次下注的彩种，玩法等相关数据到内存
                        saveCurrentLotData();
                        //下注成功后清除选择号码的记录
                        peilvFragment.onPlayClean(true);
                        showAfterBetDialog();
                    }else {
                        showToast(TextUtils.isEmpty(result.getMsg()) ? "下注时出现错误，请重新下注" : result.getMsg());
                        if (result.getMsg().contains("登陆超时")) {
                            UsualMethod.loginWhenSessionInvalid(TouzhuActivity.this);
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
        }
        DatabaseUtils.getInstance(this).deleteAllOrder();
    }

}
