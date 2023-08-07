package com.yibo.yiboapp.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.anuo.immodule.activity.ChatMainActivity;
import com.example.anuo.immodule.utils.StatusBarUtil;
import com.example.anuo.immodule.view.CommonDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.Event.LoginEvent;
import com.yibo.yiboapp.Event.VerifyEvent;
import com.yibo.yiboapp.Event.VervificationSuccessEvent;
import com.yibo.yiboapp.Event.WebChatPhotoEvent;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.CheckUpdateBean;
import com.yibo.yiboapp.entify.CheckUpdatePasswordResponse;
import com.yibo.yiboapp.entify.CheckUpdateWraper;
import com.yibo.yiboapp.entify.FuncResult;
import com.yibo.yiboapp.entify.GeneralActiveWraper;
import com.yibo.yiboapp.entify.LoginOutWraper;
import com.yibo.yiboapp.entify.LoginResult;
import com.yibo.yiboapp.entify.LoginResultWrap;
import com.yibo.yiboapp.entify.MemberHeaderWraper;
import com.yibo.yiboapp.entify.NoticeResult;
import com.yibo.yiboapp.entify.NoticeResultWraper;
import com.yibo.yiboapp.entify.RealDomainWraper;
import com.yibo.yiboapp.entify.RegisterResult;
import com.yibo.yiboapp.entify.RegisterResultWrapper;
import com.yibo.yiboapp.entify.SignResultWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.SysConfigWraper;
import com.yibo.yiboapp.entify.UnreadMsgCountWraper;
import com.yibo.yiboapp.fragment.ActivePageContainerFragment;
import com.yibo.yiboapp.fragment.BaseMainFragment;
import com.yibo.yiboapp.fragment.CaigouMallFragment;
import com.yibo.yiboapp.fragment.KaijianFragment;
import com.yibo.yiboapp.fragment.OldClassicMainFragment;
import com.yibo.yiboapp.fragment.PersonCenterFragment;
import com.yibo.yiboapp.interfaces.FileResultCallback;
import com.yibo.yiboapp.manager.BankingManager;
import com.yibo.yiboapp.manager.ManagerFactory;
import com.yibo.yiboapp.mvvm.banking.DayBalanceActivity;
import com.yibo.yiboapp.mvvm.main.MainSimpleFragmentKotlin;
import com.yibo.yiboapp.mvvm.password.ChangePwdActivity;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.services.MessagePushService;
import com.yibo.yiboapp.services.NetworkCheckingService;
import com.yibo.yiboapp.ui.ActiveStationActivity;
import com.yibo.yiboapp.ui.PopupFuncAdapter;
import com.yibo.yiboapp.ui.SignInActivity;
import com.yibo.yiboapp.ui.SpecialTab;
import com.yibo.yiboapp.ui.SpecialTabRound;
import com.yibo.yiboapp.utils.ChatRoomUtils;
import com.yibo.yiboapp.utils.StartActivityUtils;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.NavigationHeaderView;
import com.yibo.yiboapp.views.NoticeDialog;
import com.yibo.yiboapp.views.RouteUrlChooseDialog;
import com.yibo.yiboapp.views.SimpleTwoButtonDialog;
import com.yibo.yiboapp.views.floatball.FloatBallManager;
import com.yibo.yiboapp.views.floatball.floatball.FloatBallCfg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.CustomDialogManager;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.dialog.WebviewJumpListener;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

import static com.yibo.yiboapp.data.Urls.LOG_SYSTEM_CRASH_LOG_URL;


public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, UsualMethod.ChannelListener,
        SessionResponse.Listener<CrazyResult<Object>>{

    public static final String TAG = MainActivity.class.getSimpleName();
    SysConfig config = UsualMethod.getConfigFromJson(this);

    NavigationController mNavigationController;//底部菜单栏控制器
    NavigationView navView;
    DrawerLayout drawerLayout;
    BottomMenuEvent bottomMenuEvent;//底部菜单栏切换选择事件
    public NavigationHeaderView header;//侧边菜单头部
//    public MenuHeader header;//侧边菜单头部

    public static final int TOUZHU_HALL = 0;
    public static final int CAIGOU_HALL = 1;
    public static final int ADD_BUTTON_ITEM = 2;
    public static final int KAIJIAN_NOTICE = 3;
    public static final int PERSON_CENTER = 4;

    BaseMainFragment mainFragment;
    CaigouMallFragment caigouMallFragment;
    ActivePageContainerFragment activePageContainerFragment;
    KaijianFragment kaijianFragment;
    Fragment currentFragment;
    PersonCenterFragment personCenterFragment;

    boolean hasShowNoticeDialog = false;
    CountDownTimer exitTimer = null;
    int isFirstBackPress = 0;
    boolean isInTouzhu = false;
    PopupWindow funcWindow;
    PopupFuncAdapter adapter;
    int screenWidth;
    int screenHeight;

    public static final int UNREAD_MSG_REQUEST = 0x06;
    public static final int ONLINE_COUNT_REQUEST = 0x11;
    public static final int SYS_CONFIG_REQUEST = 0x08;
    public static final int GET_HEADER = 0x55;
    public static final int SIGN_REQUEST = 0x09;
    public static final int CHECK_UPDATE_REQUEST = 0x10;
    public static final int NOTICE_POP_REQUEST = 0x013;
    public static final int lOGINOUT_REQUEST = 0x014;
    public static final int GUEST_REGISTER_REQUEST = 0x02;
    public static final int CHECK_UPDATE_PASSWORD = 0x20;

    boolean fromWelcome;
    MyBroadcastReceiver myBroadcastReceiver;
    private boolean isStartService = false;
    private FloatBallManager mFloatballManager;//悬浮球类管理器
    private FloatBallManager mFloatballManager2;//悬浮球类管理器
    private FloatBallManager mFloatballManager3;//中秋悬浮球图标
    private FloatBallManager mFloatballManager4;//临时活动图标
    String multi_list_dialog_switch;//多功能列表式弹窗
    private String forceUpdate;//强制更新
    private String imageUrl;//图片的url
    private String imageLinkUrl;//临时活动点击跳转的url
    private String activeName;//活动名称
    private ImageView iv_gesture_guide_view;//引导图

    public static final int GENERAL_URL_REQ = 1;
    private int checkUpdateCount = 0;//检测自动更新的次数

    public ChatRoomUtils chatRoomUtils; //主页的聊天室方法
    private boolean isOnOriginalChat;
    private CommonDialog msgDialog;
    private RouteUrlChooseDialog chooseDialog;
    private boolean isChangeRoute = false;
    private ImageView iv_tips_bg;
    private Disposable disposableUpdatePwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
//        YiboPreference.instance(this).setVerifyToken("");
        if (TextUtils.isEmpty(YiboPreference.instance(this).getSysConfig())) {
            // 无系统配置的时候首先获取系统配置并且重置页面
            isChangeRoute = true;
            getSysConfig();
        }
//        CaigouMallContainerFragment.getTimeThread().start();

        iv_gesture_guide_view = findViewById(R.id.iv_gesture_guide_view);
        iv_tips_bg = findViewById(R.id.iv_tips_bg);
        iv_tips_bg.setOnClickListener(this);
        //是否显示首次安装的引导图
        boolean firstInstall = YiboPreference.instance(this).isFirstInstall();
        if (firstInstall) {
            iv_tips_bg.setVisibility(View.VISIBLE);
        } else {
            iv_tips_bg.setVisibility(View.GONE);
        }
        isOnOriginalChat = Utils.onOrOffOriginalChat(this);
        fromWelcome = getIntent().getBooleanExtra("welcome", false);
        initView();
        initDrawer();

        SysConfig config = UsualMethod.getConfigFromJson(this);
        PageNavigationView pageBottomTabLayout = (PageNavigationView) findViewById(R.id.tab);
        PageNavigationView.CustomBuilder builder = pageBottomTabLayout.custom();
        builder.addItem(newItem(R.drawable.tabbar_home, R.drawable.tabbar_home_selected, getString(R.string.touzhu_hall)));
        if("on".equalsIgnoreCase(config.getBuy_lottery_change_sale_activity())){
            builder.addItem(newItem(R.drawable.icon_activity_gray, R.drawable.icon_activity_red, "优惠活动"));
        }else {
            builder.addItem(newItem(R.drawable.tabbar_discover, R.drawable.tabbar_discover_selected, getString(R.string.goucai_hall)));
        }
        mNavigationController = builder
                .addItem(newRoundItem(R.mipmap.add_button_float, R.mipmap.add_button_float, ""))
                .addItem(newItem(R.drawable.tabbar_message_center, R.drawable.tabbar_message_center_selected, getString(R.string.kaijian_notice)))
                .addItem(newItem(R.drawable.tabbar_profile, R.drawable.tabbar_profile_selected, getString(R.string.person_center)))
                .build();
        bottomMenuEvent = new BottomMenuEvent();
        mNavigationController.addTabItemSelectedListener(bottomMenuEvent);
        mainFragment = figureMainStyleFromConfig();
        mainFragment.bindDelegate(code -> {
            if (tipLogin(true)) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        caigouMallFragment = new CaigouMallFragment();
        activePageContainerFragment = new ActivePageContainerFragment();
        kaijianFragment = new KaijianFragment();
        personCenterFragment = new PersonCenterFragment();
        kaijianFragment.setChannelListener(this);

        FragmentManager fm = this.getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, mainFragment);
        ft.commit();
        currentFragment = mainFragment;
        isInTouzhu = true;

        boolean flushConfig = getIntent().getBooleanExtra("flushConfig", false);
        if (!flushConfig) {
//            updateViewsAfterLoginRegisterOrPickConfig();
        }
        updateSlideMenus();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        //上传崩溃日志
        crashUpload();
        actionLHCMark();
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updateMSG");
        registerReceiver(myBroadcastReceiver, intentFilter);

        //本地消息推送
        startMessagePushService();
        /**
         * 初始化聊天室的悬浮球
         */
        if (UsualMethod.getConfigFromJson(this) != null &&
                UsualMethod.getConfigFromJson(this).getOnoff_chat().equalsIgnoreCase("on")) {
            initFloatBall();
            //原生聊天室授权
//            if (tipLogin(false) && isOnOriginalChat && !SocketManager.instance(this).isConnecting()) {
//                UsualMethod.authorization(this);
//            }
        }
        /**
         * 初始化红包悬浮
         */
        if (UsualMethod.getConfigFromJson(this) != null &&
                UsualMethod.getConfigFromJson(this).getOnoff_member_mobile_red_packet().equals("on")) {
            initFloatBallhongbao();
        }
        /**
         * 初始化临时活动
         */
        if (UsualMethod.getConfigFromJson(this) != null &&
                UsualMethod.getConfigFromJson(this).getShow_temp_activity_switch().equals("on")) {
//            initTempActivityBall();

            getGeneralActivity(false);
        }
        EventBus.getDefault().register(this);
        iv_gesture_guide_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_gesture_guide_view.setVisibility(View.GONE);
                YiboPreference.instance(MainActivity.this).saveFirstInstall(false);
            }
        });
        handleAutoLogin();
    }

    private void handleAutoLogin() {
        if (!YiboPreference.instance(this).isAutoLogin()) {
            UsualMethod.loginWhenSessionInvalid(this);
            return;
        }

        boolean loginView = false;

        if (config != null && config.getOnoff_mobile_notlogged_to_loginview() != null) {
            loginView = config.getOnoff_mobile_notlogged_to_loginview().equals("on") || config.getNot_login_permission().equals("on");
        }

        if (!YiboPreference.instance(this).isLogin()) {
            // 未登陆状态时
            if (loginView) {
                ToastUtils.showShort("请先登录");
                actionLoginOrRegisterActivity(YiboPreference.instance(getApplicationContext()).getUsername(), "", 0);
                return;
            }
            // 自动登录
            if (!Utils.isEmptyString(YiboPreference.instance(this).getPwd()) && "off".equalsIgnoreCase(config.getOn_off_recaptcha_verify())
                    && "off".equalsIgnoreCase(config.getOnoff_mobile_verify_code())) {
                // 密码不为空 && 验证码未开启 && 行为验证未开启 才能进行自动登陆
                String username = YiboPreference.instance(this).getUsername();
                String pwd = YiboPreference.instance(this).getPwd();
                UsualMethod.actionLogin(username, pwd, null, this, "", false, new HttpCallBack() {
                    @Override
                    public void receive(NetworkResult result) {
                        if (result == null) {
                            showToast(R.string.login_fail);
                            return;
                        }

                        LoginResult loginResult = new Gson().fromJson(result.getContent(), LoginResult.class);
                        if (!result.isSuccess()) {
                            String errorString = Urls.parseResponseResult(result.getMsg());
                            showToast(Utils.isEmptyString(errorString) ? getString(R.string.login_fail) : errorString);
                            YiboPreference.instance(MainActivity.this).setLoginState(false);

                            long accountId = loginResult.getAccountId();
                            int code = result.getCode();
                            String username = YiboPreference.instance(MainActivity.this).getUsername();
                            String password = YiboPreference.instance(MainActivity.this).getPwd();
                            if (code == 555) {
                                VerificationSetActivity.createIntent(MainActivity.this, accountId, username, password, false);
                                return;
                            }
                            if (code == 666) {
                                DoVerificationActivity.createIntent(MainActivity.this, accountId, username, password, false);
                                return;
                            }
                            //登录成功后，同步登录帐户后的UI状态
                            toggleLoginView(indexBottom);
                            return;
                        }


                        YiboPreference.instance(MainActivity.this).setToken(result.getAccessToken());
                        YiboPreference.instance(MainActivity.this).setAccountId(loginResult.getAccountId());
                        YiboPreference.instance(MainActivity.this).setLoginState(true);
                        YiboPreference.instance(MainActivity.this).setAccountMode(loginResult.getAccountType());
                        //登录成功后，获取头像等数据
                        header.syncHeaderWebDatas(MainActivity.this);
                        header.updateCircleHeader();
                        //登录成功后，同步登录帐户后的UI状态
                        toggleLoginView(indexBottom);
                        //登录成功后，获取未读消息数
                        actionMsg();
                    }
                });
            }
        }
    }


    /**
     * 开启后台消息推送服务
     */
    private void startMessagePushService() {
        boolean isMessagePushOpen = YiboPreference.instance(this).getMessagePush();
        if (isMessagePushOpen) {
            startService(new Intent(this, MessagePushService.class));
            isStartService = true;
        }
    }

    /**
     * 开启提交检测服务
     */
    private void startNetworkCheckingService() {
        Intent intent = new Intent(this, NetworkCheckingService.class);
        intent.putExtra(NetworkCheckingService.ACTION, NetworkCheckingService.ACTION_DEFAULT);
        startService(intent);
    }

    private void startMainBackupJob() {
        Intent intent = new Intent(this, NetworkCheckingService.class);
        intent.putExtra(NetworkCheckingService.ACTION, NetworkCheckingService.ACTION_MAIN_BACKUP);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //检测新版本
        checkUpdateCount = 0;//检测自动更新的次数
        checkVersion(0);
        toggleLoginView(0);
        if (UsualMethod.getConfigFromJson(getApplicationContext()) != null && "on".equals(UsualMethod.getConfigFromJson(getApplicationContext()).getSwitch_mainpage_online_count())) {
            actionCount();//在线人数
        }

        //网路监测服务
        if (!isRunService(this, "com.yibo.yiboapp.services.NetworkCheckingService")) {
            startNetworkCheckingService();
            startMainBackupJob();
        }
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
                    if (UsualMethod.checkIsLogin(MainActivity.this)) {
                        Intent intent = new Intent(MainActivity.this, ChatMainActivity.class);
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
                    if (!YiboPreference.instance(getApplicationContext()).isLogin()) {
                        actionLoginOrRegisterActivity(YiboPreference.instance(getApplicationContext()).getUsername(), "", 0);
                        return;
                    }

                    if (config != null && !TextUtils.isEmpty(config.getChat_foreign_link())) {
                        UsualMethod.viewLink(MainActivity.this, config.getChat_foreign_link().trim());
                        return;
                    }

                    if (chatRoomUtils == null) {
                        chatRoomUtils = new ChatRoomUtils(MainActivity.this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRequestChatUrl(LoginEvent event) {
        if (chatRoomUtils != null) {
            chatRoomUtils = null;
        }
        //登录成功后，获取未读消息数
        actionMsg();
        //登入成功后，更新header内容
        updateSlideMenus();
        header.syncHeaderWebDatas(this);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //只有activity被添加到windowmanager上以后才可以调用show方法。
        if (mFloatballManager != null) {
            mFloatballManager.show();
        }

        if (mFloatballManager3 != null) {
            mFloatballManager3.show();
        }
        if (mFloatballManager4 != null) {
            mFloatballManager4.show();
        }
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mFloatballManager != null) {
            mFloatballManager.hide();
        }
        if (mFloatballManager3 != null) {
            mFloatballManager3.hide();
        }
        if (mFloatballManager4 != null) {
            mFloatballManager4.hide();
        }

    }

    private void crashUpload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("crashTime", YiboPreference.instance(MainActivity.this).getLastCrashTime());
                map.put("platform", "0");
                map.put("domain_url", BuildConfig.domain_url);
                map.put("osVersion", "android/" + android.os.Build.VERSION.RELEASE);
                map.put("appVersion", Utils.getVersionName(MainActivity.this));
                map.put("buildModel", Utils.getBuildModel());
                map.put("username", YiboPreference.instance(MainActivity.this).getUsername());
                String applicationId = BuildConfig.APPLICATION_ID;
                String subName = applicationId.substring(applicationId.lastIndexOf(".") + 1);
                map.put("stationId", subName);
                String path = Utils.createFilepath(Utils.DIR_CATEGORY.LOG, "crashDump.txt");
                final File file = new File(path);
                if (!file.exists()) {
                    return;
                }
                UsualMethod.postFile(MainActivity.this, LOG_SYSTEM_CRASH_LOG_URL,
                        map, file, new FileResultCallback() {
                            @Override
                            public void fileResult(boolean success, String json) {
                                Utils.LOG(TAG, "success = " + success + ";result json = " + json);
                                //上传成功后，删除本地日志文件
                                if (success) {
                                    file.delete();
                                }
                            }
                        });
            }
        }).start();
    }

    /**
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        SpecialTab mainTab = new SpecialTab(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(getResources().getColor(R.color.bottom_tabbar_check_color_default));
        mainTab.setTextCheckedColor(getResources().getColor(R.color.bottom_tabbar_check_color_select));
        return mainTab;
    }

    /**
     * 圆形tab
     */
    private BaseTabItem newRoundItem(int drawable, int checkedDrawable, String text) {
        SpecialTabRound mainTab = new SpecialTabRound(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(getResources().getColor(R.color.bottom_tabbar_check_color_default));
        mainTab.setTextCheckedColor(getResources().getColor(R.color.bottom_tabbar_check_color_select));
        return mainTab;
    }

    /**
     * 根据后台配置的主页风格样式选择fragment
     *
     * @return
     */
    private BaseMainFragment figureMainStyleFromConfig() {
        if (config != null) {
            if (config.getNative_style_code().equals(String.valueOf(Constant.OLD_CLASSIC_STYLE))) {
                return new OldClassicMainFragment();
            } else if (config.getNative_style_code().equals(String.valueOf(Constant.EASY_LIGHT_APP_STYLE1))) {
                return new MainSimpleFragmentKotlin();
            }
        }
        return new OldClassicMainFragment();
    }

    private void actionPopNotice() {
        CrazyRequest<CrazyResult<NoticeResultWraper>> popRequest = null;
        if (/*!YiboPreference.instance(this).isPopNotices()*/!hasShowNoticeDialog) {
            StringBuilder popUrl = new StringBuilder();
            popUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ACQURIE_NOTICE_POP_URL);
            popUrl.append("?code=19");
            popRequest = new AbstractCrazyRequest.Builder().
                    url(popUrl.toString())
                    .seqnumber(NOTICE_POP_REQUEST)
                    .headers(Urls.getHeader(this))
                    .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<NoticeResultWraper>() {
                    }.getType()))
                    .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                    .create();
        }
        RequestManager.getInstance().startRequest(this, popRequest);
    }

    void actionMsg() {
        //未读消息数
        StringBuilder msgUrl = new StringBuilder();
        msgUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.UNREAD_MSG_COUNT_URL);
        CrazyRequest<CrazyResult<UnreadMsgCountWraper>> unreadRequest = new AbstractCrazyRequest.Builder().
                url(msgUrl.toString())
                .seqnumber(UNREAD_MSG_REQUEST)
                .headers(Urls.getHeader(this))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<
                        UnreadMsgCountWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, unreadRequest);
    }


    void actionCount() {
        //未读消息数
        StringBuilder msgUrl = new StringBuilder();
        msgUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ONLINE_COUNT);
        CrazyRequest<CrazyResult<UnreadMsgCountWraper>> unreadRequest = new AbstractCrazyRequest.Builder().
                url(msgUrl.toString())
                .seqnumber(ONLINE_COUNT_REQUEST)
                .headers(Urls.getHeader(this))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<UnreadMsgCountWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, unreadRequest);
    }


    private void checkVersion(int version) {
        boolean isAutoUpdate = YiboPreference.instance(this).isAutoCheckUpdate();

        if (config != null) {
            forceUpdate = config.getForce_update_app();
        } else {
            forceUpdate = "off";
        }

        //强更开关关闭并且自动监测更新关闭的时候， 不检测更新
        if (forceUpdate.equalsIgnoreCase("off") && !isAutoUpdate) {
            return;
        }
        CrazyRequest checkRequest = UsualMethod.checkUpdate(this, Utils.getVersionName(this),
                BuildConfig.APPLICATION_ID, false, CHECK_UPDATE_REQUEST, version);
        RequestManager.getInstance().startRequest(this, checkRequest);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean flushConfig = getIntent().getBooleanExtra("flushConfig", false);
        CrazyRequest configRequest = null;
        if (flushConfig) {
            configRequest = UsualMethod.startAsyncConfig(this, SYS_CONFIG_REQUEST);
        }
        RequestManager.getInstance().startRequest(this, configRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (UsualMethod.isSpaceMainPopup()) {
            UsualMethod.updateMainPopupTime();
            hasShowNoticeDialog = false;
            //获取公告弹窗内容
            actionPopNotice();
        }

        if((indexBottom == TOUZHU_HALL || indexBottom == PERSON_CENTER) && YiboPreference.instance(this).isLogin()){
            Observable.timer(2000L, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {
                            disposableUpdatePwd = d;
                        }

                        @Override
                        public void onNext(@NotNull Long aLong) { }

                        @Override
                        public void onError(@NotNull Throwable e) { }

                        @Override
                        public void onComplete() {
                            checkNeedUpdatePassword();
                        }
                    });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(disposableUpdatePwd != null && !disposableUpdatePwd.isDisposed()){
            disposableUpdatePwd.dispose();
        }
    }

    private void checkNeedUpdatePassword(){
        if(YiboPreference.instance(this).isLogin()){
            long lastCheckTime = YiboPreference.instance(this).getUpdatePasswordTimestamp();
            if(System.currentTimeMillis() > lastCheckTime + 86400*1000){
                String url = Urls.BASE_URL + Urls.PORT + Urls.NEED_UPDATE_PASSWORD;
                CrazyRequest<CrazyResult<LoginResultWrap>> request = new AbstractCrazyRequest.Builder()
                        .url(url)
                        .seqnumber(CHECK_UPDATE_PASSWORD)
                        .headers(Urls.getHeader(this))
                        .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                        .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                        .convertFactory(GsonConverterFactory.create(new TypeToken<CheckUpdatePasswordResponse>() {
                        }.getType()))
                        .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                        .create();
                RequestManager.getInstance().startRequest(this, request);
            }
        }
    }

    private void getSysConfig() {
        CrazyRequest crazyRequest = UsualMethod.startAsyncConfig(this, SYS_CONFIG_REQUEST);
        RequestManager.getInstance().startRequest(this, crazyRequest);
    }


    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        if (navView != null) {
            navView.setNavigationItemSelectedListener(this);//设置监听
            navView.setItemIconTintList(null);//保持图标本色
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this,
                    drawerLayout,
                    R.string.drawer_open,
                    R.string.drawer_close
            );
            //该方法会自动和actionBar关联, 将开关的图片显示在了actionBar上
            toggle.syncState();
            drawerLayout.addDrawerListener(toggle);
        }
        initMenuHeader();
    }

    private void initMenuHeader() {
        if (navView == null || navView.getHeaderView(0) == null) {
            return;
        }
        header = (NavigationHeaderView) navView.getHeaderView(0);
//        header = (MenuHeader) navView.getHeaderView(0);
    }

    private void updateMessageCenterItem(int unreadCount) {
        MenuItem item = navView.getMenu().getItem(1);
        SubMenu subMenu = item.getSubMenu();
        MenuItem messageItem = subMenu.getItem(8);
        if (unreadCount > 0 && messageItem.getTitle().equals("我的站内信")) {
            messageItem.setTitle("我的站内信(" + unreadCount + ")");
            if (config != null && "on".equalsIgnoreCase(config.getOnoff_mobile_unread_msg_popups())) {
                if (msgDialog == null) {
                    msgDialog = CommonDialog.create(this, "消息", String.format("您有%s条未读消息！", unreadCount), "查看", new CommonDialog.DialogClickListener() {
                        @Override
                        public void onInputListener(View view, String input) {
                        }

                        @Override
                        public void onClick(View v) {
                            if (!YiboPreference.instance(MainActivity.this).isLogin()) {
                                actionLoginOrRegisterActivity(YiboPreference.instance(getApplicationContext()).getUsername(), "", 0);
                                return;
                            }
                            MessageCenterActivity.createIntent(MainActivity.this);
                        }
                    }, "取消", new CommonDialog.DialogClickListener() {
                        @Override
                        public void onInputListener(View view, String input) {
                        }

                        @Override
                        public void onClick(View v) {
                            msgDialog.dismiss();
                        }
                    }, true, false, true);
                }
                msgDialog.setDialogMessage(String.format("您有%s条未读消息！", unreadCount), true);
                msgDialog.show();
            }
        } else {
            messageItem.setTitle("我的站内信");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WebChatPhotoEvent webChatPhotoEvent = new WebChatPhotoEvent(requestCode, resultCode, data);
        EventBus.getDefault().post(webChatPhotoEvent); //聊天室发送图片之后回调
    }

    private final class MenuItemClickListener implements MenuItem.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String title = item.getTitle().toString();
            if (title.equals("彩票投注记录")) {
                RecordsActivityNew.createIntent(MainActivity.this, "彩票投注记录", Constant.CAIPIAO_RECORD_STATUS, "");
            } else if (title.equals("六合投注记录")) {
                RecordsActivity.createIntent(MainActivity.this, "六合投注记录", Constant.LHC_RECORD_STATUS, "LHC");
            }/* else if (title.equals("旧体育投注记录")) {
                RecordsActivity.createIntent(MainActivity.this, "旧体育投注记录", Constant.OLD_SPORTS_RECORD_STATUS, "");
            }*/ else if (title.equals("体育投注记录")) {
                RecordsActivity.createIntent(MainActivity.this, "体育投注记录", Constant.SPORTS_RECORD_STATUS, "");
            } else if (title.equals("沙巴体育投注记录")) {
                RecordsActivity.createIntent(MainActivity.this, "沙巴体育投注记录", Constant.SBSPORTS_RECORD_STATUS, "");
            } else if (title.equals("真人投注记录")) {
                RecordsActivity.createIntent(MainActivity.this, "真人投注记录", Constant.REAL_PERSON_RECORD_STATUS, "");
            } else if (title.equals("电子游戏记录")) {
                RecordsActivityNew.createIntent(MainActivity.this, "电子游戏记录", Constant.ELECTRIC_GAME_RECORD_STATUS, "");
            } else if (title.equals("棋牌游戏记录")) {
                RecordsActivityNew.createIntent(MainActivity.this, "棋牌游戏记录", Constant.CHESS_GAME_RECORD_STATUS, "");
            } else if (title.equals("每日输赢")){
                startActivity(new Intent(MainActivity.this, DayBalanceActivity.class));
            } else if (title.equals("用户帐变记录")) {
                ZhangbianInfoActivity.createIntent(MainActivity.this, "用户帐变记录", Constant.ACCOUNT_CHANGE_RECORD_STATUS, "");
            } else if (title.equals("帐户明细记录")) {
                AccountDetailListActivity.createIntent(MainActivity.this);
            }
            return false;
        }
    }


    //根据后台配置动态更新侧边栏中的 操作记录 menu项
    private void updateSlideMenus() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MenuItem item = navView.getMenu().getItem(0);
                SubMenu subMenu = item.getSubMenu();
                subMenu.clear();

                if (config != null) {
                    Menu menu = navView.getMenu();
                    navView.setItemBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.bg_new_main_page_menu_item));
                    for (int i = 0; i < menu.size(); i++) {
                        menu.getItem(i).setTitle(null);
                    }
                    boolean isGuest = YiboPreference.instance(MainActivity.this).getAccountMode()
                            == Constant.ACCOUNT_PLATFORM_TEST_GUEST;
                    if (!isGuest) {
                        if (!Utils.isEmptyString(config.getOnoff_lottery_game()) &&
                                config.getOnoff_lottery_game().equals("on")) {
                            MenuItem cpItem = subMenu.add("彩票投注记录");
                            cpItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            cpItem.setIcon(R.drawable.caipiao_record_icon2_new);
                        }
                        if (!Utils.isEmptyString(config.getOnoff_liu_he_cai()) &&
                                config.getOnoff_liu_he_cai().equals("on")) {
                            MenuItem sixItem = subMenu.add("六合投注记录");
                            sixItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            sixItem.setIcon(R.drawable.lhc_record_icon2_new);
                        }

                        if (!Utils.isEmptyString(config.getOnoff_sports_game()) &&
                                config.getOnoff_sports_game().equals("on")) {
                            MenuItem realItem = subMenu.add("体育投注记录");
                            realItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            realItem.setIcon(R.drawable.sport_record_icon2_new);
                        }

//                        if (!Utils.isEmptyString(sc.getNew_onoff_sports_game()) &&
//                                sc.getNew_onoff_sports_game().equals("on")) {
//                            MenuItem realItem = subMenu.add("新体育投注记录");
//                            realItem.setOnMenuItemClickListener(new MenuItemClickListener());
//                            realItem.setIcon(R.drawable.sport_record_icon);
//                        }

                        if (!Utils.isEmptyString(config.getOnoff_shaba_sports_game()) &&
                                config.getOnoff_shaba_sports_game().equals("on")) {
                            MenuItem realItem = subMenu.add("沙巴体育投注记录");
                            realItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            realItem.setIcon(R.drawable.sb_sport_record_icon2_new);
                        }
                        if (!Utils.isEmptyString(config.getOnoff_zhen_ren_yu_le()) &&
                                config.getOnoff_zhen_ren_yu_le().equals("on") && !isGuest) {
                            MenuItem elecItem = subMenu.add("真人投注记录");
                            elecItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            elecItem.setIcon(R.drawable.realman_record_icon2_new);
                        }
                        if (!Utils.isEmptyString(config.getOnoff_dian_zi_you_yi()) &&
                                config.getOnoff_dian_zi_you_yi().equals("on") && !isGuest) {
                            MenuItem sportItem = subMenu.add("电子游戏记录");
                            sportItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            sportItem.setIcon(R.drawable.egame_record_icon2_new);
                        }
                        if (!Utils.isEmptyString(config.getOnoff_chess()) &&
                                config.getOnoff_chess().equals("on") && !isGuest) {
                            MenuItem sportItem = subMenu.add("棋牌游戏记录");
                            sportItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            sportItem.setIcon(R.drawable.chess_game_record_icon_new);
                        }
                        MenuItem balanceItem = subMenu.add("每日输赢");
                        balanceItem.setOnMenuItemClickListener(new MenuItemClickListener());
                        balanceItem.setIcon(R.drawable.icon_day_balance);

                        if ((!Utils.isEmptyString(config.getOnoff_change_money()) &&
                                config.getOnoff_change_money().equals("on")) && !isGuest &&
                                (!Utils.isEmptyString(config.getIosExamine()) ||
                                        !config.getIosExamine().equals("off"))) {
                            MenuItem sportItem = subMenu.add("用户帐变记录");
                            sportItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            sportItem.setIcon(R.drawable.account_change_record_icon2_new);
                        }
                        if ((!Utils.isEmptyString(config.getIosExamine()) ||
                                !config.getIosExamine().equals("off"))) {
                            //添加帐户明细记录
                            MenuItem sportItem = subMenu.add("帐户明细记录");
                            sportItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            sportItem.setIcon(R.drawable.account_detail_reord_icon2_new);
                        }
                    } else {
                        if (!Utils.isEmptyString(config.getOnoff_lottery_game()) &&
                                config.getOnoff_lottery_game().equals("on")) {
                            MenuItem cpItem = subMenu.add("彩票投注记录");
                            cpItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            cpItem.setIcon(R.drawable.caipiao_record_icon2_new);
                        }
                        if (!Utils.isEmptyString(config.getOnoff_liu_he_cai()) &&
                                config.getOnoff_liu_he_cai().equals("on")) {
                            MenuItem sixItem = subMenu.add("六合投注记录");
                            sixItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            sixItem.setIcon(R.drawable.lhc_record_icon2_new);
                        }
                        if ((!Utils.isEmptyString(config.getIosExamine()) ||
                                !config.getIosExamine().equals("off"))) {
                            //添加帐户明细记录
                            MenuItem sportItem = subMenu.add("帐户明细记录");
                            sportItem.setOnMenuItemClickListener(new MenuItemClickListener());
                            sportItem.setIcon(R.drawable.account_detail_reord_icon2_new);
                        }
                    }

                    //是否显示积分兑换入口
                    boolean visible = !Utils.isEmptyString(config.getExchange_score()) && config.getExchange_score().equals("on");
                    MenuItem personCenter = navView.getMenu().getItem(1);
                    SubMenu pMenu = personCenter.getSubMenu();


                    MenuItem exchange = pMenu.findItem(R.id.exchange_score);
                    MenuItem exchangeRecord = pMenu.findItem(R.id.exchange_score_record);
                    exchange.setVisible(visible);
                    exchangeRecord.setVisible(visible);

                    //是否显示挖矿活动
                    MenuItem wakuang = pMenu.findItem(R.id.item_mining);
                    boolean waVisible = !Utils.isEmptyString(config.getOnoff_mining()) && config.getOnoff_mining().equals("on");
                    wakuang.setVisible(waVisible);


                    //是否显示大转盘
                    boolean visible2 = !Utils.isEmptyString(config.getOnoff_turnlate()) && config.getOnoff_turnlate().equals("on");
                    boolean visibleYesj = !Utils.isEmptyString(config.getOnoff_money_income()) && config.getOnoff_money_income().equals("on");
                    boolean visibleMrjj = !Utils.isEmptyString(config.getOne_bonus_onoff()) && config.getOne_bonus_onoff().equals("on");
                    boolean visibleZzzy = !Utils.isEmptyString(config.getWeek_deficit_onoff()) && config.getWeek_deficit_onoff().equals("on");
                    MenuItem turnPan = navView.getMenu().getItem(1);
                    SubMenu turnMenu = turnPan.getSubMenu();
                    MenuItem pan = turnMenu.findItem(R.id.luck_pan);
                    pan.setVisible(visible2);
                    MenuItem yesj = turnMenu.findItem(R.id.item_yesj);
                    yesj.setVisible(visibleYesj);
                    MenuItem mrjj = turnMenu.findItem(R.id.item_mrjj);
                    mrjj.setVisible(visibleMrjj);
                    MenuItem zzzy = turnMenu.findItem(R.id.item_zzzy);
                    zzzy.setVisible(visibleZzzy);

                    //是否显示充值卡和代金券
                    boolean visibleRechargeCard = config.getRecharge_card_onoff().equals("on");
                    boolean visibleCoupon = config.getCoupons_onoff().equals("on");

                    MenuItem mi = turnMenu.findItem(R.id.item_recharge_card);
                    mi.setVisible(visibleRechargeCard);

                    MenuItem mi1 = turnMenu.findItem(R.id.item_coupon);
                    mi1.setVisible(visibleCoupon);


                    //更新额度转换按钮显示与否
                    boolean zr = !Utils.isEmptyString(config.getOnoff_zhen_ren_yu_le()) && config.getOnoff_zhen_ren_yu_le().equals("on");
                    boolean dz = !Utils.isEmptyString(config.getOnoff_dian_zi_you_yi()) && config.getOnoff_dian_zi_you_yi().equals("on");
                    boolean sb = !Utils.isEmptyString(config.getOnoff_shaba_sports_game()) && config.getOnoff_shaba_sports_game().equals("on");
                    boolean nb = !Utils.isEmptyString(config.getOnoff_nb_game()) && config.getOnoff_nb_game().equals("on");
                    boolean ky = !Utils.isEmptyString(config.getOnoff_ky_game()) && config.getOnoff_ky_game().equals("on");
                    boolean tt = !Utils.isEmptyString(config.getOnoff_tt_lottery_game()) && config.getOnoff_tt_lottery_game().equals("on");
                    boolean visible4 = false;
                    if (zr || dz || sb || nb || ky || tt) {
                        visible4 = true;
                    }
                    MenuItem convert = navView.getMenu().getItem(1);
                    SubMenu convertSubMenu = convert.getSubMenu();
                    MenuItem convertMenu = convertSubMenu.findItem(R.id.fee_convert_item);
                    convertMenu.setVisible(visible4);

                    //是否显示红包
                    boolean visible3 = !Utils.isEmptyString(config.getOnoff_member_mobile_red_packet()) && config.getOnoff_member_mobile_red_packet().equals("on");
                    MenuItem redpacket = navView.getMenu().getItem(1);
                    SubMenu redpacketMenu = redpacket.getSubMenu();
                    MenuItem rp = redpacketMenu.findItem(R.id.redpacket);
                    rp.setVisible(visible3);

                    //是否显示免提直充
                    boolean visibleDirect = "on".equals(config.getOn_off_direct_charge());
                    redpacketMenu.findItem(R.id.direct_charge).setVisible(visibleDirect);

                    redpacketMenu.findItem(R.id.donate).setVisible("on".equals(config.getOn_off_member_donate()));

                    //是否显示代理管理
                    boolean dailimanager = !Utils.isEmptyString(config.getOnoff_all_level_fixed()) && config.getOnoff_all_level_fixed().equals("on");
                    if (!dailimanager) {
                        MenuItem daili = navView.getMenu().getItem(2);
                        SubMenu dailiMenu = daili.getSubMenu();
                        dailiMenu.clear();
                    }

                }
                RecyclerView rv = navView.findViewById(R.id.design_navigation_view);
                rv.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                    @Override
                    public void onChildViewAttachedToWindow(View view) {
                        if (view instanceof TextView && ((TextView) view).getText().toString().isEmpty()) {
                            view.getLayoutParams().height = 30;
                        }
                    }

                    @Override
                    public void onChildViewDetachedFromWindow(View view) {

                    }
                });
            }
        }, 200);

    }

    @Override
    protected void initView() {
        super.initView();
        if (config != null) {
            String basic_info_website_name = config.getBasic_info_website_name();
            if (!Utils.isEmptyString(basic_info_website_name)) {
                tvBackText.setVisibility(View.VISIBLE);
                tvBackText.setOnClickListener(null);
                tvBackText.setText(basic_info_website_name);
                tvBackText.setBackground(null);
            }
        }
//        tvBackText.setText(getString(R.string.app_name));
        navView = (NavigationView) findViewById(R.id.view_nav);
        drawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);

        if (mLayout != null && config != null && config.getNewmainpage_switch().equals("on")) {
            Window window = this.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            StatusBarUtil.immersive(window, Color.WHITE);
            mLayout.setBackground(new ColorDrawable(Color.WHITE));
            String basic_info_website_name = config.getBasic_info_website_name();
            if (!Utils.isEmptyString(basic_info_website_name)) {
                tvMiddleTitle.setVisibility(View.VISIBLE);
                tvMiddleTitle.setOnClickListener(null);
                tvMiddleTitle.setText(basic_info_website_name);
                tvMiddleTitle.setTextColor(Color.parseColor("#333333"));
                int[][] states = new int[1][];
                states[0] = new int[]{};
                ImageViewCompat.setImageTintList(
                        ivMoreMenu,
                        new ColorStateList(states, new int[]{Color.parseColor("#666666")})
                );
            }
        } else {
            tvMiddleTitle.setVisibility(View.GONE);
            tvMiddleTitle.setText("");
        }
    }

    /**
     * @param context
     * @param fromWelcome
     */
    public static void createIntent(Context context, boolean fromWelcome) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("welcome", fromWelcome);
        context.startActivity(intent);
    }

    public static void createIntent(Context context) {
        createIntent(context, false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_mining:
                Intent miningIntent = new Intent(this, MiningActivity.class);
                startActivity(miningIntent);
                break;
            case R.id.exchange_score_record:
                PointExchangeRecordActivity.createIntent(this);
                break;
            case R.id.login_pwd_change:
                ChangePwdActivity.Companion.createIntent(this, true);
                break;
            case R.id.item_zhgl:
                Intent intentZH = BankingManager.Companion.openAccountManagerPage(this, false);
                startActivity(intentZH);
                break;
            case R.id.item_yesj:
                this.startActivity(new Intent(this, JijinActivity.class));
                break;
            case R.id.item_mrjj:
                Intent intent1 = new Intent(this, JiajiangActivity.class);
                intent1.putExtra("title", "每日加奖活动");
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.setCookie(Urls.BASE_URL + Urls.MEIRIJIAJIANG, "SESSION=" + YiboPreference.instance(this).getToken());//cookies是在HttpClient中获得的cookie
                CookieSyncManager.getInstance().sync();
                intent1.putExtra("url", Urls.BASE_URL + Urls.MEIRIJIAJIANG);
                startActivity(intent1);
                break;
            case R.id.item_zzzy:
                Intent intent = new Intent(this, JiajiangActivity.class);
                intent.putExtra("title", "周周转运活动");
                CookieSyncManager.createInstance(getApplicationContext());
                CookieManager cookieManager1 = CookieManager.getInstance();
                cookieManager1.setAcceptCookie(true);
                cookieManager1.setCookie(Urls.BASE_URL + Urls.ZHOUZHOUZHUANYUN, "SESSION=" + YiboPreference.instance(getApplicationContext()).getToken());//cookies是在HttpClient中获得的cookie
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager1.flush();
                } else {
                    CookieSyncManager.createInstance(getApplicationContext());
                    CookieSyncManager.getInstance().sync();
                }
                intent.putExtra("url", Urls.BASE_URL + Urls.ZHOUZHOUZHUANYUN);
                startActivity(intent);
                break;
            case R.id.account_pwd_change:
                ChangePwdActivity.Companion.createIntent(this, false);
                break;
            case R.id.message_center:
                MessageCenterActivity.createIntent(this);
                break;
            case R.id.app_setting:
                AppSettingActivity.createIntent(this);
                break;
            case R.id.suggestion:
                SuggestionFeedbackActivity.createIntent(this);
                break;
            case R.id.contact_us:
                String version = config.getOnline_service_open_switch();
                if (!version.isEmpty()) {
                    boolean success = UsualMethod.viewService(this, version);
                    if (!success) {
                        showToast("没有在线客服链接地址，无法打开");
                    }
                }
                break;
            case R.id.exit_login:
                final CrazyRequest request = UsualMethod.startAsyncConfig(this, SYS_CONFIG_REQUEST);
                RequestManager.getInstance().startRequest(this, request);
                UsualMethod.actionLoginOut(this, lOGINOUT_REQUEST, true, this);
                break;
            case R.id.exchange_score:
                ExchangeScoreActivity.createIntent(this, header.getAccountName(), header.getBalance() + ""); //header.getLeftMoneyName()
                break;
            case R.id.fee_convert_item:
                if (YiboPreference.instance(this).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                    showToast("操作权限不足，请联系客服！");
                    return true;
                }
                QuotaConvertActivity.createIntent(this);
                break;
            case R.id.active_item:
                if (Utils.isEmptyString(UsualMethod.getConfigFromJson(this).getWap_active_activity_link()))
                    ActivePageActivity.createIntent(this);
                else
                    UsualMethod.viewLink(this, UsualMethod.getConfigFromJson(this).getWap_active_activity_link().trim());

                break;
            case R.id.luck_pan:
                if (!YiboPreference.instance(this).isLogin()) {
                    showToast("请先登录再重试");
                    return false;
                }
                if (YiboPreference.instance(this).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                    showToast("操作权限不足，请联系客服！");
                    return false;
                }
                BigPanActivity.createIntent(this);
                break;
            case R.id.redpacket:
                if (!YiboPreference.instance(getApplicationContext()).isLogin()) {
                    actionLoginOrRegisterActivity(YiboPreference.instance(getApplicationContext()).getUsername(), "", 0);
                    return false;
                }
                if (Utils.isTestPlay(this, false) && config.getOn_off_guest_redperm().equals("off")) {
                    showToast("操作权限不足，请联系客服！");
                    return false;
                } else {
                    StartActivityUtils.goRedPacket(this);
                }
                break;
            case R.id.direct_charge:
                Intent intent2 = new Intent(this, DirectChargeActivity.class);
                startActivity(intent2);
                break;
            case R.id.donate:
                startActivity(new Intent(this, DonateActivity.class));
                break;
            case R.id.userlist:
                startActivity(new Intent(this, MemberListActivity.class));
                break;
            case R.id.teamview:
                if (Utils.isTestPlay(this)) {
                    return false;
                } else {
                    startActivity(new Intent(this, TeamOverViewListActivity.class));
                }
                break;
            case R.id.my_recommand:
                if (Utils.isTestPlay(this)) {
                    return false;
                } else {
                    startActivity(new Intent(this, MyRecommendationActivity.class));
                }
                break;

            case R.id.active_hall:
                ActiveStationActivity.createIntent(this);
                break;
            case R.id.item_coupon:
                startActivity(new Intent(this, CouponActivity.class));
                break;
            case R.id.item_recharge_card:
                startActivity(new Intent(this, RechargeCouponActivity.class));
                break;

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        }, 1000);
        return true;
    }


    private void toggleLoginView(int index) {
        if (index == 2) return;
        if (!YiboPreference.instance(this).isLogin()) {
            tvRightText.setVisibility(View.VISIBLE);
            ivMoreMenu.setVisibility(View.GONE);
            tvRightText.setBackground(null);
            if (ManagerFactory.INSTANCE.getUserManager().allowRegisterSwitch()) {
                if (config != null) {
                    String sort = config.getRegister_btn_pos_sort();
                    if (Utils.isEmptyString(sort) || sort.equalsIgnoreCase("front")) {
                        tvRightText.setText(getString(R.string.register_str));
                        tvSecondRightText.setText(getString(R.string.login));
                    } else {
                        tvRightText.setText(getString(R.string.login));
                        tvSecondRightText.setText(getString(R.string.register_str));
                    }
                    tvRightText.setOnClickListener(this);
                }
                tvSecondRightText.setVisibility(View.VISIBLE);
                mLayout.findViewById(R.id.second_right_text_divider).setVisibility(View.VISIBLE);
                tvSecondRightText.setBackground(null);
            } else {
                tvRightText.setText("登录");
                tvSecondRightText.setVisibility(View.GONE);
            }

            if (config != null && config.getNewmainpage_switch().equals("on")) {
                tvBackText.setVisibility(View.VISIBLE);
                tvBackText.setBackground(ContextCompat.getDrawable(this, R.mipmap.icon_message));
                tvBackText.setText("     ");
                tvBackText.setOnClickListener((View v) -> {
                    if (YiboPreference.instance(this).isLogin()) {
                        MessageCenterActivity.createIntent(MainActivity.this);
                    } else {
                        actionLoginOrRegisterActivity(YiboPreference.instance(getApplicationContext()).getUsername(), "", 0);
                    }
                });
                tvRightText.setVisibility(View.GONE);
                tvSecondRightText.setVisibility(View.GONE);
                tvThirdRightText.setTextColor(Color.parseColor("#666666"));
            } else {
                mLayout.findViewById(R.id.third_right_text_divider).setVisibility(View.VISIBLE);
            }
            if (config != null && !Utils.isEmptyString(config.getOnoff_mobile_guest_register()) &&
                    config.getOnoff_mobile_guest_register().equals("on")) {
                mLayout.findViewById(R.id.third_right_text_divider).setVisibility(View.VISIBLE);
                tvThirdRightText.setVisibility(View.VISIBLE);
            } else {
                mLayout.findViewById(R.id.third_right_text_divider).setVisibility(View.GONE);
                tvThirdRightText.setVisibility(View.GONE);
            }
            tvThirdRightText.setText("免费试玩");
            tvThirdRightText.setOnClickListener((View v) -> UsualMethod.registGuest(this, GUEST_REGISTER_REQUEST));

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            if (index == 4) {
                ivMoreMenu.setVisibility(View.GONE);
                tvRightText.setText("设置");
                tvRightText.setVisibility(View.VISIBLE);
                tvRightText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, AppSettingActivity.class));
                    }
                });
            } else {
                ivMoreMenu.setVisibility(View.VISIBLE);
                tvRightText.setText("");
                tvRightText.setVisibility(View.GONE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            tvSecondRightText.setVisibility(View.GONE);
            tvSecondRightText.setText("");
            if (config != null && config.getNewmainpage_switch().equals("on")) {
                tvBackText.setVisibility(View.VISIBLE);
                tvBackText.setBackground(ContextCompat.getDrawable(this, R.mipmap.icon_message));
                tvBackText.setText("     ");
                tvBackText.setOnClickListener((View v) -> MessageCenterActivity.createIntent(MainActivity.this));
            }
            mLayout.findViewById(R.id.second_right_text_divider).setVisibility(View.GONE);
            mLayout.findViewById(R.id.third_right_text_divider).setVisibility(View.GONE);
            tvThirdRightText.setVisibility(View.GONE);
        }
        if (config != null && !config.getNative_style_code().equals(String.valueOf(Constant.EASY_LIGHT_APP_STYLE1))) {
            new Handler().postDelayed(() ->
                    refreshNewMainPageLoginBlock(YiboPreference.instance(MainActivity.this).isLogin(),
                            header.getAccountName(), header.getBalance()),
                    500);
        }
    }

    @Override
    public void onClick(View v) {
        if (iv_tips_bg != null && iv_tips_bg.getVisibility() == View.VISIBLE) {
            YiboPreference.instance(this).saveFirstInstall(false);
            iv_tips_bg.setVisibility(View.GONE);
        }
        switch (v.getId()) {
            case R.id.iv_more_menu:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.right_text:
                if (!YiboPreference.instance(this).isLogin()) {
                    if (config != null) {
                        String sort = config.getRegister_btn_pos_sort();
                        if (Utils.isEmptyString(sort) || sort.equalsIgnoreCase("front")) {
                            if (ManagerFactory.INSTANCE.getUserManager().allowRegisterSwitch()) {
                                actionLoginOrRegisterActivity("", "", 1);
                            } else {
                                actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), YiboPreference.instance(this).getPwd(), 0);
                            }
                        } else {
                            actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), YiboPreference.instance(this).getPwd(), 0);
                        }
                    }
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.second_right_text:
                if (!YiboPreference.instance(this).isLogin()) {
                    if (config != null) {
                        String sort = config.getRegister_btn_pos_sort();
                        if (Utils.isEmptyString(sort) || sort.equalsIgnoreCase("front")) {
                            actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), YiboPreference.instance(this).getPwd(), 0);
                        } else {
                            actionLoginOrRegisterActivity("", "", 1);
                        }
                    }
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.back_text:
                if (tipLogin(true)) {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                break;
            default:
                break;
        }
    }

    private boolean tipLogin(boolean show) {
        boolean isLogin = YiboPreference.instance(this).isLogin();
        if (show && !isLogin) {
            showToast(R.string.please_login_first);
            Utils.shakeView(this, tvRightText);
        }
        return isLogin;
    }

    @Override
    public void onCaiPiaoItemClick(String cpCode) {
        if (Utils.isEmptyString(cpCode)) {
            return;
        }
    }

    @Override
    public void onKaiJianItemClick(String name, String cpCode, String cpType) {
        KaijianListActivity.createIntent(this, name, cpCode, cpType);
    }

    @Override
    public void onGoucaiItemClick(String name, String cpCode) {
//        gotoTouzhuPageByCode(cpCode);
    }

    private int indexBottom = 0;

    private class BottomMenuEvent implements OnTabItemSelectedListener {

        @Override
        public void onSelected(int index, int old) {
            Utils.LOG(TAG, " index,old=" + index + "," + old);
            Fragment to = currentFragment;
            if (index == TOUZHU_HALL) {
//                tvBackText.setVisibility(View.GONE);
                tvRightText.setVisibility(View.VISIBLE);
                to = mainFragment;
                isInTouzhu = true;
                switchContent(currentFragment, to);
                mLayout.setVisibility(View.VISIBLE);
                checkNeedUpdatePassword();
                if(disposableUpdatePwd != null && !disposableUpdatePwd.isDisposed()){
                    disposableUpdatePwd.dispose();
                }
            } else if (index == CAIGOU_HALL) {
                SysConfig config = UsualMethod.getConfigFromJson(MainActivity.this);
                if("on".equalsIgnoreCase(config.getBuy_lottery_change_sale_activity())){
                    to = activePageContainerFragment;
                }else {
                    to = caigouMallFragment;
                }
                isInTouzhu = false;
                tvRightText.setVisibility(View.VISIBLE);
                switchContent(currentFragment, to);
                mLayout.setVisibility(View.VISIBLE);
            } else if (index == ADD_BUTTON_ITEM) {
                if (iv_tips_bg != null && iv_tips_bg.getVisibility() == View.VISIBLE) {
                    YiboPreference.instance(MainActivity.this).saveFirstInstall(false);
                    iv_tips_bg.setVisibility(View.GONE);
                }
                //点击加号
                isInTouzhu = false;
                Utils.LOG(TAG, "add button item click");
                mNavigationController.setSelect(old);
                if (!fromWelcome) {
                    showFuncWindow();
                } else {
                    fromWelcome = !fromWelcome;
                }
            } else if (index == KAIJIAN_NOTICE) {
                isInTouzhu = false;
                tvRightText.setVisibility(View.VISIBLE);
                to = kaijianFragment;
                switchContent(currentFragment, to);
                mLayout.setVisibility(View.VISIBLE);
            } else if (index == PERSON_CENTER) {
                isInTouzhu = false;
                boolean hasLogin = tipLogin(true);
                if (!hasLogin) {
                    UsualMethod.loginWhenSessionInvalid(MainActivity.this);
                    mNavigationController.setSelect(old);
                    return;
                }

                ivMoreMenu.setVisibility(View.GONE);
                tvRightText.setVisibility(View.VISIBLE);
                tvRightText.setText("设置");
                tvRightText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, AppSettingActivity.class));
                    }
                });

                tvRightText.setVisibility(View.GONE);
                to = personCenterFragment;
                switchContent(currentFragment, to);
                mLayout.setVisibility(View.GONE);
                checkNeedUpdatePassword();
                if(disposableUpdatePwd != null && !disposableUpdatePwd.isDisposed()){
                    disposableUpdatePwd.dispose();
                }
            }
            toggleLoginView(index);
            indexBottom = index;
        }

        @Override
        public void onRepeat(int index) {
            Utils.LOG(TAG, "onRepeat index=" + index);
        }
    }

    private List<FuncResult> buildFuncDatas() {

        List<FuncResult> datas = new ArrayList<>();

        FuncResult changeRoute = new FuncResult();
        changeRoute.setImgID(R.drawable.icon_change_route);
        changeRoute.setTitle("切换线路");
        datas.add(changeRoute);

        if (config != null) {
            if (!Utils.isEmptyString(config.getNative_mobile_agent_register_enter()) && config.getNative_mobile_agent_register_enter().equals("on")) { //代理注册
                FuncResult agentRe = new FuncResult();
                agentRe.setImgID(R.mipmap.agent_reg);
                agentRe.setTitle(getResources().getString(R.string.agent_register));
                datas.add(agentRe);
            }

            if (!Utils.isEmptyString(config.getOnoff_member_mobile_red_packet()) &&
                    config.getOnoff_member_mobile_red_packet().equals("on")) {
                FuncResult redpacket = new FuncResult();
                redpacket.setImgID(R.mipmap.redpacket_icon);
                redpacket.setTitle("抢红包");
                datas.add(redpacket);
            }
            if (!Utils.isEmptyString(config.getOnoff_turnlate()) &&
                    config.getOnoff_turnlate().equals("on")) {
                FuncResult bigpan = new FuncResult();
                bigpan.setImgID(R.mipmap.big_pan_icon);
                bigpan.setTitle("大转盘");
                datas.add(bigpan);
            }
            if (!Utils.isEmptyString(config.getExchange_score()) &&
                    config.getExchange_score().equals("on")) {
                FuncResult scorechange = new FuncResult();
                scorechange.setImgID(R.mipmap.score_exchange_icon);
                scorechange.setTitle("积分兑换");
                datas.add(scorechange);


                FuncResult funcResult = new FuncResult();
                funcResult.setImgID(R.mipmap.score_exchange_icon);
                funcResult.setTitle("积分兑换记录");
                datas.add(funcResult);

            }

            if (!Utils.isEmptyString(config.getSwitch_backto_computer()) &&
                    config.getSwitch_backto_computer().equalsIgnoreCase("on")) {
                FuncResult backComputer = new FuncResult();
                backComputer.setImgID(R.mipmap.back_computer);
                backComputer.setTitle("返回电脑版");
                datas.add(backComputer);
            }

            if (!Utils.isEmptyString(config.getMobile_web_index_slide_images())) {
                FuncResult backComputer = new FuncResult();
                backComputer.setImgID(R.drawable.member_headers);
                backComputer.setImgUrl(config.getMobile_web_index_slide_images().trim());
                if (!Utils.isEmptyString(config.getMobile_web_index_slide_url_name())) {
                    backComputer.setTitle(config.getMobile_web_index_slide_url_name());
                } else {
                    backComputer.setTitle("首页浮动链接");
                }
                datas.add(backComputer);
            }
            if (!Utils.isEmptyString(config.getOnoff_all_level_fixed()) && config.getOnoff_all_level_fixed().equals("on")) {
                if (!Utils.isEmptyString(config.getShow_agent_manager_mainpage()) && config.getShow_agent_manager_mainpage().equals("on")) {
                    FuncResult backComputer = new FuncResult();
                    backComputer.setImgID(R.mipmap.manual_bet_icon);
                    backComputer.setTitle("代理管理");
                    datas.add(backComputer);
                }
            }


            if (!Utils.isEmptyString(config.getForeign_game_hall_link_switch()) &&
                    config.getForeign_game_hall_link_switch().equalsIgnoreCase("on")) {
                FuncResult backComputer = new FuncResult();
                backComputer.setImgID(R.drawable.foreign_game_link);
                backComputer.setTitle("游戏大厅");
                datas.add(backComputer);
            }

            if (!Utils.isEmptyString(config.getOpen_lottery_website_switch()) &&
                    config.getOpen_lottery_website_switch().equalsIgnoreCase("on")) {
                FuncResult backComputer = new FuncResult();
                backComputer.setImgID(R.drawable.open_lottery_wapp);
                backComputer.setTitle("开奖网");
                datas.add(backComputer);
            }


        }

        FuncResult stationmsg = new FuncResult();
        stationmsg.setImgID(R.mipmap.mssage_icon);
        stationmsg.setTitle("站内信");
        datas.add(stationmsg);
        FuncResult activeitem = new FuncResult();
        activeitem.setImgID(R.mipmap.active_icon);
        activeitem.setTitle("优惠活动");
        datas.add(activeitem);

        if (config != null && !Utils.isEmptyString(config.getOnoff_sign_in()) && config.getOnoff_sign_in().equals("on")) {
            FuncResult backComputer = new FuncResult();
            // backComputer.setImgID(R.mipmap.sign_in);
            backComputer.setImgID(R.drawable.ic_sign_in);
            backComputer.setTitle("签到");
            datas.add(backComputer);
        }

        return datas;
    }

    private void jumpPageWithFunTtile(String title) {
        if (title.equals("抢红包")) {
            if (!YiboPreference.instance(this).isLogin()) {
                actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), "", 0);
                return;
            }
            if (Utils.isTestPlay(this, false) && UsualMethod.getConfigFromJson(this).getOn_off_guest_redperm().equals("off")) {
                showToast("操作权限不足，请联系客服！");
                return;
            }
            StartActivityUtils.goRedPacket(this);
        } else if (title.equals("大转盘")) {
            if (!YiboPreference.instance(getApplicationContext()).isLogin()) {
                actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), "", 0);
                return;
            }
            BigPanActivity.createIntent(this);
        } else if (title.equals("积分兑换")) {
            if (!YiboPreference.instance(getApplicationContext()).isLogin()) {
                actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), "", 0);
                return;
            }
            ExchangeScoreActivity.createIntent(this, "", "");
        } else if (title.equals("积分兑换记录")) {
            if (!YiboPreference.instance(getApplicationContext()).isLogin()) {
                actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), "", 0);
                return;
            }
            PointExchangeRecordActivity.createIntent(this);
        } else if (title.equals("代金券中心")) {
            if (!YiboPreference.instance(getApplicationContext()).isLogin()) {
                actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), "", 0);
                return;
            }
            startActivity(new Intent(this, CouponActivity.class));

        } else if (title.equals("充值卡中心")) {
            if (!YiboPreference.instance(getApplicationContext()).isLogin()) {
                actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), "", 0);
                return;
            }
            startActivity(new Intent(this, RechargeCouponActivity.class));
        } else if (title.equals("站内信")) {
            if (!YiboPreference.instance(this).isLogin()) {
                actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), "", 0);
                return;
            }
            MessageCenterActivity.createIntent(this);
        } else if (title.equals("优惠活动")) {
            if (Utils.isEmptyString(UsualMethod.getConfigFromJson(this).getWap_active_activity_link()))
                ActivePageActivity.createIntent(this);
            else
                UsualMethod.viewLink(this, UsualMethod.getConfigFromJson(this).getWap_active_activity_link().trim());
        } else if (title.equals("返回电脑版")) {


            String single_jump_pc_domain_url = UsualMethod.getConfigFromJson(this).getSingle_jump_pc_domain_url();
            if (Utils.isEmptyString(single_jump_pc_domain_url)) {
                UsualMethod.viewLink(this, Urls.BASE_URL + Urls.PORT + "/?toPC=1");
            } else {
                UsualMethod.viewLink(this, single_jump_pc_domain_url);
            }

        } else if (title.equals("首页浮动链接")) {
            if (!Utils.isEmptyString(config.getMobile_web_index_slide_url())) {
                UsualMethod.viewLink(this, config.getMobile_web_index_slide_url().trim());
            } else {
                showToast("没有配置浮动链接，请联系客服");
            }
        } else if (title.equals("代理管理")) {
            if (!YiboPreference.instance(getApplicationContext()).isLogin()) {
                actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), "", 0);
                return;
            }
            showListDialog();

        } else if (title.equals(UsualMethod.getConfigFromJson(this).getMobile_web_index_slide_url_name())) {
            if (!Utils.isEmptyString(config.getMobile_web_index_slide_url())) {
                UsualMethod.viewLink(this, config.getMobile_web_index_slide_url().trim());
            } else {
                showToast("没有配置浮动链接，请联系客服");
            }
        } else if (title.equals("游戏大厅")) {
            if (!Utils.isEmptyString(config.getForeign_game_hall_link())) {
                UsualMethod.viewLink(this, config.getForeign_game_hall_link().trim());
            } else {
                showToast("没有配置游戏大厅链接，请联系客服");
            }
        } else if (title.equals("开奖网")) {
            if (!Utils.isEmptyString(config.getOpen_lottery_website_url())) {
                UsualMethod.viewLink(this, config.getOpen_lottery_website_url().trim());
            } else {
                showToast("没有配置开奖网链接，请联系客服");
            }
        } else if (title.equals(getResources().getString(R.string.agent_register))) {
            RegisterActivity.createIntent(this, true, getResources().getString(R.string.back_main));
        } else if (title.equals("签到")) {
//            UsualMethod.actionSign(this, SIGN_REQUEST, this);
            if (!YiboPreference.instance(getApplicationContext()).isLogin()) {
                actionLoginOrRegisterActivity(YiboPreference.instance(this).getUsername(), "", 0);
                return;
            }
            startActivity(new Intent(this, SignInActivity.class));
        } else if (title.equals("切换线路")) {
            List<RealDomainWraper.ContentBean> contentBeans = new Gson().fromJson(YiboPreference.instance(this).getROUTE_URLS(), new TypeToken<ArrayList<RealDomainWraper.ContentBean>>() {
            }.getType());
            if (contentBeans == null || contentBeans.isEmpty()) {
                showToast(R.string.no_route_urls);
                return;
            }
            if (chooseDialog == null) {
                chooseDialog = new RouteUrlChooseDialog(this, contentBeans);
                chooseDialog.setChooseListener(new RouteUrlChooseDialog.OnRouteChooseListener() {
                    @Override
                    public void onChoose(@NonNull RealDomainWraper.ContentBean contentBean, int position) {
                        isChangeRoute = true;
                        getSysConfig();
                    }
                    @Override
                    public void onAutoRouteFailed() {}
                });
            }
            chooseDialog.show();
        }
    }

    private void showFuncWindow() {
        if (funcWindow != null && funcWindow.isShowing()) {
            return;
        }
        if (fromWelcome) {
            return;
        }
        if (adapter == null) {
            adapter = new PopupFuncAdapter(this, buildFuncDatas(), R.layout.popup_func_item);
            adapter.setPopupCallback(new PopupFuncAdapter.PopupCallback() {
                @Override
                public void onItemClick(int pos, String title) {
                    jumpPageWithFunTtile(title);
                    if (funcWindow != null && funcWindow.isShowing()) {
                        funcWindow.dismiss();
                    }
                }
            });
        }
        final View view = LayoutInflater.from(this).inflate(R.layout.popup_func_window, null);
        final GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setAdapter(adapter);
//        View emptyView = view.findViewById(R.id.empty_place);
        Button closeBtn = (Button) view.findViewById(R.id.close_btn);
//        emptyView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (funcWindow != null && funcWindow.isShowing()) {
//                    funcWindow.dismiss();
//                }
//            }
//        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (funcWindow != null && funcWindow.isShowing()) {
                    funcWindow.dismiss();
                }
            }
        });
        funcWindow = new PopupWindow(view, screenWidth, screenHeight);
        funcWindow.setOutsideTouchable(true);
        funcWindow.update();
        funcWindow.setTouchable(true);
        funcWindow.setAnimationStyle(R.style.adjust_window_anim);
        funcWindow.setBackgroundDrawable(new BitmapDrawable());
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    if (funcWindow != null && funcWindow.isShowing()) {
                        funcWindow.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
        funcWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hidenorshow(false);
            }
        });
        funcWindow.showAtLocation(findViewById(R.id.layout_drawer), Gravity.NO_GRAVITY, 0, 0);
        hidenorshow(true);

    }

    public void switchContent(Fragment from, Fragment to) {
        if (currentFragment != to) {
            currentFragment = to;
            androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.fragment, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

    private void createTimer() {
        if (exitTimer == null) {
            exitTimer = new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    isFirstBackPress = 0;
                }
            };
        }
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    /**
     * 双击退出
     */
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
            ToastUtils.showShort(R.string.press_again_exit);
            firstTime = secondTime;
        } else {
            //清除登陆状态
            YiboPreference.instance(this).setLoginState(false);
            ActivityUtils.finishAllActivities();
        }
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
//        RequestManager.getInstance().afterRequest(response);

        if (!handleCrazyResult(response)) {
            return;
        }
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == UNREAD_MSG_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.request_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            UnreadMsgCountWraper reg = (UnreadMsgCountWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //更新侧边站内信未读消息数
            int unReadCount = reg.getContent();
            mNavigationController.setMessageNumber(4, unReadCount);
            updateMessageCenterItem(unReadCount);
        } else if (action == SYS_CONFIG_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                if (isChangeRoute) {
                    isChangeRoute = false;
                    showToast("线路-" + YiboPreference.instance(this).getCHOOSE_ROUTE_NAME() + "-异常！");
                    return;
                }
                showToast(R.string.get_system_config_fail);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                if (isChangeRoute) {
                    isChangeRoute = false;
                    showToast("线路-" + YiboPreference.instance(this).getCHOOSE_ROUTE_NAME() + "-异常！");
                    return;
                }
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.get_system_config_fail) : errorString);
                return;
            }
            SysConfigWraper stw = (SysConfigWraper) result.result;
            if (!stw.isSuccess()) {
                showToast(Utils.isEmptyString(stw.getMsg()) ?
                        getString(R.string.get_system_config_fail) : stw.getMsg());
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (stw.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                if (stw.getCode() == 544){
                    UsualMethod.showVerifyActivity(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(stw.getAccessToken());
            //存储系统配置项
            if (stw.getContent() != null) {
                String configJson = new Gson().toJson(stw.getContent(), SysConfig.class);
                if (configJson.equals(YiboPreference.instance(this).getSysConfig())){
                    // 当前保存的系统配置已经是最新的了
                    return;
                }
                YiboPreference.instance(this).saveConfig(configJson);
                YiboPreference.instance(this).saveYjfMode(stw.getContent().getYjf());
                //保存系统版本号版本号
                YiboPreference.instance(this).saveGameVersion(stw.getContent().getVersion());
                if (isChangeRoute) {
                    isChangeRoute = false;
                    // 重置当前页面
//                    this.recreate();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            }
            //更新侧边内容，如操作记录的显示与否
//            updateViewsAfterLoginRegisterOrPickConfig();
        } else if (action == SIGN_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.sign_fail);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.sign_fail) : errorString);
                return;
            }
            SignResultWraper stw = (SignResultWraper) result.result;
            if (!stw.isSuccess()) {
                showToast(Utils.isEmptyString(stw.getMsg()) ?
                        getString(R.string.sign_fail) : stw.getMsg());
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (stw.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(stw.getAccessToken());
            if (stw.getContent() != null) {
                if (stw.getContent().getScore() == 0) {
                    showToast("签到成功");
                    return;
                } else {
                    long days = stw.getContent().getDays();
                    if (days > 0) {
                        String content = "恭喜您已连续签到" + days + "天，获得积分" + stw.getContent().getScore();
                        UsualMethod.showSignSuccessDialog(this, content);
                    } else {
                        showToast("签到成功");
                        return;
                    }
                }
            }

        } else if (action == CHECK_UPDATE_REQUEST) {
            checkUpdateCount++;
            CrazyResult<Object> result = response.result;
            if (result == null) {
//                showToast(R.string.check_version_fail);
                return;
            }
            if (!result.crazySuccess) {
//                showToast(R.string.check_version_fail);
                return;
            }
            Object regResult = result.result;
            CheckUpdateWraper reg = (CheckUpdateWraper) regResult;
            if (!reg.isSuccess()) {
//                showToast(!Utils.isEmptyString(reg.getMsg())?reg.getMsg():
//                        getString(R.string.check_version_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            CheckUpdateBean content = reg.getContent();
            final boolean isForcedUpdate = UsualMethod.getConfigFromJson(this).getForce_update_app().equalsIgnoreCase("on");
            if (content != null) {
                String updateUrl = content.getUrl();
                if (Utils.isEmptyString(updateUrl)) {
                    if (checkUpdateCount == 1) {
                        checkVersion(1);
                    }
//                    showToast("没有新版本更新地址，请重试");
                    return;
                }
                String updateContent = content.getContent();
                UsualMethod.showUpdateContentDialog(this, content.getVersion(), updateContent, updateUrl, new UsualMethod.UpdateDialogEvent() {
                    @Override
                    public void onDialogEvent() {
                        if (isForcedUpdate) {
                            System.exit(0);
                        }
                    }
                });
            } else {
                if (checkUpdateCount == 1) {
                    checkVersion(1);
                }
//                showToast(R.string.check_version_fail);
            }
        } else if (action == NOTICE_POP_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }

            if (!result.crazySuccess) {
                return;
            }
            Object regResult = result.result;
            NoticeResultWraper reg = (NoticeResultWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //显示公告内容
//            boolean ispop = YiboPreference.instance(MainActivity.this).isPopNotices();
            if (!hasShowNoticeDialog) {
                multi_list_dialog_switch = UsualMethod.getConfigFromJson(this).getMulti_list_dialog_switch();
                if (reg.getContent() != null && !reg.getContent().isEmpty()) {
                    if (multi_list_dialog_switch.equals("on")) {
                        showMutilMessageDialog(reg.getContent());
                    } else {
                        showNoticeDialog(reg.getContent().get(0).getTitle(), reg.getContent().get(0).getContent());
                    }
                    hasShowNoticeDialog = true;
                }
//                if (reg.getContent() != null && !reg.getContent().isEmpty()) {
//                    showNoticeDialog(reg.getContent().get(0).getTitle(),
//                            reg.getContent().get(0).getContent());
//                    hasShowNoticeDialog = true;
//                }
            }
        } else if (action == lOGINOUT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.loginout_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.loginout_fail);
                return;
            }
            Object regResult = result.result;
            LoginOutWraper reg = (LoginOutWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.loginout_fail));
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.isContent()) {
                UsualMethod.loginWhenSessionInvalid(this);
            } else {
                showToast(R.string.loginout_fail);
            }
        } else if (action == GET_HEADER) {
            {
                CrazyResult<Object> result = response.result;
                if (result == null) {
                    return;
                }
                if (!result.crazySuccess) {
//                String errorString = Urls.parseResponseResult(result.error);
//                showToast(Utils.isEmptyString(errorString) ? context.getString(R.string.request_fail) : errorString);
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
            }
        } else if (action == GENERAL_URL_REQ) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.acquire_fail);
                return;
            }
            Object regResult = result.result;
            GeneralActiveWraper reg = (GeneralActiveWraper) regResult;
            if (!reg.isSuccess()) {
//                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
//                        getString(R.string.acquire_fail));
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (!Utils.isEmptyString(reg.getContent().getUrl())) {

                imageLinkUrl = reg.getContent().getUrl();
                imageUrl = reg.getContent().getImageUrl();
                activeName = reg.getContent().getName();
                initTempActivityBall();
//                GeneralActiveActivity.createIntent(this, Urls.BASE_URL + Urls.PORT + reg.getContent().getUrl(), reg.getContent().getName());
            }
        } else if (action == ONLINE_COUNT_REQUEST) {

            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.acquire_fail);
                return;
            }
            UnreadMsgCountWraper regResult = (UnreadMsgCountWraper) result.result;

            String online_count_fake = UsualMethod.getConfigFromJson(this).getOnline_count_fake();


            int fakeCount = 0;
            if (!Utils.isEmptyString(online_count_fake)) {
                try {
                    fakeCount = Integer.parseInt(online_count_fake);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    fakeCount = 0;
                }
            }
            int onlineCount = regResult.getContent() + fakeCount;
            Constant.FAKE_COUNT = onlineCount;
            mainFragment.setOnlineCount(onlineCount + "");
            initFakeOnlieCount();
        }else if(action == CHECK_UPDATE_PASSWORD){
            CrazyResult<Object> result = response.result;
            if (result == null || !result.crazySuccess) {
                return;
            }

            Object regResult = result.result;
            CheckUpdatePasswordResponse res = (CheckUpdatePasswordResponse) regResult;
            if (!res.isSuccess()) {
                if(!TextUtils.isEmpty(res.getMsg()) && res.getMsg().contains("登陆超时")){
                    showToast(res.getMsg());
                    UsualMethod.loginWhenSessionInvalid(this);
                }
            }else {
                if(res.isNeedUpgrade()){
                    showUpdatePasswordDialog("为了您的账户安全，您的密码需要升级，请修改密码再继续使用!");
                }else if(res.isNeed_update_member_login_password()){
                    showUpdatePasswordDialog("您已经长期没有更换密码，为了您的账户安全，请定期更换密码。");
                }
            }
        }else if (action == GUEST_REGISTER_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.register_fail);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.register_fail) : errorString);
                return;
            }

            Object regResult = result.result;
            RegisterResultWrapper reg = (RegisterResultWrapper) regResult;
            if (!reg.isSuccess()) {
                showToast(Utils.isEmptyString(reg.getMsg()) ?
                        getString(R.string.register_fail) : reg.getMsg());
                return;
            }

            YiboPreference.instance(this).setToken(reg.getAccessToken());
            YiboPreference.instance(this).setLoginState(true);

            //获取注册帐户相关信息
            RegisterResult content = reg.getContent();
            if (content != null) {
                int accountType = content.getAccountType();
                YiboPreference.instance(this).setAccountMode(accountType);
                if (!Utils.isEmptyString(content.getAccount())) {
                    YiboPreference.instance(this).saveUsername(content.getAccount());
                } else {
                    YiboPreference.instance(this).saveUsername("");
                }
                YiboPreference.instance(this).saveGameVersion(content.getCpVersion());
                //自动登录的情况下，要记住帐号密码
                if (accountType != Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                    if (YiboPreference.instance(this).isAutoLogin()) {
                        if (!Utils.isEmptyString(content.getAccount())) {
                            YiboPreference.instance(this).saveUsername(content.getAccount());
                        }
                    }
                }
                EventBus.getDefault().post(new LoginEvent());
                toggleLoginView(0);
            }
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
                .initView(this)
                .show();

    }

    /**
     * 展示单条信息
     *
     * @param content
     */
    private void showMessageDialog(List<NoticeResult> content) {
        NoticeDialog dialog = new NoticeDialog(this);
        dialog.setContent(content)
                .initView(this)
                .show();

    }


    private void showNoticeDialog(String title, String content) {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(1);
        ccd.setTitle(title);
        ccd.setContent(content);
        ccd.setMiddleBtnText("确定");
//        ccd.setToastShow(true);
        ccd.setHtmlContent(true);
        ccd.setBaseUrl(Urls.BASE_URL);
        ccd.setWebviewJumpListener(new WebviewJumpListener() {
            @Override
            public void webJumpEvent(String url) {
                if (!url.equals(Urls.BASE_URL) && !Utils.isEmptyString(url)) {
                    ActiveDetailActivity.createIntent(MainActivity.this, "", "弹窗活动", url);
                }
            }
        });
        ccd.setOnToastBtnClick(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
//                YiboPreference.instance(MainActivity.this).setPopNotices(true);
            }
        });
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    private void showUpdatePasswordDialog(String message){
        SimpleTwoButtonDialog dialog = new SimpleTwoButtonDialog(this,
                "请修改密码", message, "立即前往", "以后再说");
        dialog.setListener(isLeft -> {
            dialog.dismiss();
            if(isLeft){
                ChangePwdActivity.Companion.createIntent(this, true);
            }else {
                YiboPreference.instance(this).setUpdatePasswordTimestamp(System.currentTimeMillis());
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exitTimer != null) {
            exitTimer.cancel();
            exitTimer = null;
        }
        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
        }

        if (isStartService) {
            stopService(new Intent(this, MessagePushService.class));
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //停止提交检测服务
        stopService(new Intent(this, NetworkCheckingService.class));
        //修改交检测服务 FLAG 预设开启
        YiboPreference.instance(this).setStartNetworkService(true);
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            actionMsg();
        }
    }


    /**
     * 初始化悬浮球
     */
    private void initFloatBallhongbao() {
        //初始化悬浮球配置，定义好悬浮球大小和icon的drawable
        FloatBallCfg ballCfg1 = new FloatBallCfg(this, ConvertUtils.dp2px(70),
                getResources().getDrawable(R.drawable.icon_float_redpacket_new), FloatBallCfg.Gravity.RIGHT_BOTTOM, -ConvertUtils.dp2px(90), true);
        //设置悬浮球不半隐藏
        ballCfg1.setHideHalfLater(false);
        mFloatballManager2 = new FloatBallManager(this, ballCfg1, null);

        mFloatballManager2.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
            @Override
            public void onFloatBallClick() {
                if (!YiboPreference.instance(getApplicationContext()).isLogin()) {
                    actionLoginOrRegisterActivity(YiboPreference.instance(MainActivity.this).getUsername(), "", 0);
                    return;
                }
                if (Utils.isTestPlay(MainActivity.this, false) && UsualMethod.getConfigFromJson(MainActivity.this).getOn_off_guest_redperm().equals("off")) {
                    showToast("操作权限不足，请联系客服！");
                    return;
                }
                StartActivityUtils.goRedPacket(MainActivity.this);
                // RedPacketRainActivity.createIntent(getApplicationContext());
            }

            @Override
            public void onChildClick(String str, int postion) {

            }
        });
        mFloatballManager2.show();
    }

    private void getGeneralActivity(boolean showDialog) {
        if (this.isFinishing()) {
            return;
        }
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GENERAL_ACTIVITY_URL);
        CrazyRequest<CrazyResult<GeneralActiveWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(GENERAL_URL_REQ)
                .headers(Urls.getHeader(this))
                .refreshAfterCacheHit(false)
                .shouldCache(false).placeholderText(getString(R.string.forward_jumping))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<GeneralActiveWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }


    /**
     * 初始化临时活动链接
     */
    private void initTempActivityBall() {
        //初始化悬浮球配置，定义好悬浮球大小和icon的drawable
        FloatBallCfg ballCfg1 = new FloatBallCfg(this, ConvertUtils.dp2px(80), imageUrl, FloatBallCfg.Gravity.LEFT_CENTER);
        ballCfg1.setmOffsetY(20);
        //设置悬浮球不半隐藏
        ballCfg1.setHideHalfLater(false);
        mFloatballManager4 = new FloatBallManager(this, ballCfg1);
        mFloatballManager4.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
            @Override
            public void onFloatBallClick() {
                if (activeName.contains("矿")) {
                    Intent intent = new Intent(getApplicationContext(), MiningActivity.class);
                    startActivity(intent);
                    return;
                } else {
                    if (!TextUtils.isEmpty(imageLinkUrl)) {
                        GeneralActiveActivity.createIntent(MainActivity.this, Urls.BASE_URL + Urls.PORT + imageLinkUrl, activeName);
                    } else {
                        ToastUtils.showShort("没有活动链接，请检查是否配置");
                    }
                }
            }

            @Override
            public void onChildClick(String str, int postion) {

            }
        });
        mFloatballManager4.show();
    }

    /**
     * 初始化中秋悬浮球
     */
//    private void initzhongqiuFloatBall() {
//        //初始化悬浮球配置，定义好悬浮球大小和icon的drawable
//        FloatBallCfg ballCfg1 = new FloatBallCfg(this, ConvertUtils.dp2px(80),
//                ContextCompat.getDrawable(this, R.drawable.zhongqiu_icon), FloatBallCfg.Gravity.LEFT_BOTTOM);
//        //设置悬浮球不半隐藏
//        ballCfg1.setHideHalfLater(false);
//        mFloatballManager3 = new FloatBallManager(this, ballCfg1);
//
//        mFloatballManager3.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
//            @Override
//            public void onFloatBallClick() {
//                if (tipLogin()) {
//                    Intent intent = new Intent(MainActivity.this, MidAutumnActivity.class);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onChildClick(String str, int postion) {
//
//            }
//        });
//    }

    public void hidenorshow(boolean bool) {
        if (bool) {
            if (mFloatballManager2 != null) {
                mFloatballManager2.hide();
            }

        } else {
            if (mFloatballManager2 != null) {
                if (isInTouzhu && !mFloatballManager2.isCloseRedPacket()) {
                    mFloatballManager2.show();
                }
            }
        }

    }

    private void showListDialog() {
        final String[] list_String = {"团队总览", "用户列表", "我的推荐"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("代理管理");
        builder.setItems(list_String, new DialogInterface.OnClickListener() {//列表对话框；
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (list_String[which]) {
                    case "团队总览":
                        if (YiboPreference.instance(MainActivity.this).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                            showToast("操作权限不足，请联系客服！");
                            return;
                        }
                        startActivity(new Intent(MainActivity.this, TeamOverViewListActivity.class));
                        break;
                    case "用户列表":
                        if (YiboPreference.instance(MainActivity.this).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                            showToast("操作权限不足，请联系客服！");
                            return;
                        }
                        startActivity(new Intent(MainActivity.this, MemberListActivity.class));
                        break;
                    case "我的推荐":
                        if (YiboPreference.instance(MainActivity.this).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                            showToast("操作权限不足，请联系客服！");
                            return;
                        }
                        startActivity(new Intent(MainActivity.this, MyRecommendationActivity.class));
                        break;
                }


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * lhc彩种获取
     */
    private void actionLHCMark() {
        //内部弹对话框显示进度条
        HttpUtil.get(this, Urls.IS_SIXMARK, null, false, result -> {
            if (result.isSuccess()) {
                YiboPreference.instance(this).setToken(result.getAccessToken());
                YiboPreference.instance(this).saveSixMark(result.getContent());
            } else {

            }

        });
    }

    //虚假人数执行线程
    void initFakeOnlieCount() {
        Handler timeHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boolean jiaorjian = (int) (1 + Math.random() * (2 - 1 + 1)) == 1;
                int count = (int) (1 + Math.random() * (10 - 1 + 1));
                if (jiaorjian) {
                    Constant.FAKE_COUNT = Constant.FAKE_COUNT + count;
                } else {
                    Constant.FAKE_COUNT = Constant.FAKE_COUNT - count;
                }
                mainFragment.setOnlineCount(Constant.FAKE_COUNT + "");
                timeHandler.postDelayed(this, 10000);
            }
        };

        timeHandler.postDelayed(runnable, 10000);
    }

    public void refreshNewMainPageLoginBlock(boolean isLogin, String accountName, double balance){
        if(mainFragment != null)
            mainFragment.refreshNewMainPageLoginBlock(isLogin, accountName, balance);
    }

    @Subscribe
    public void onEvent(VervificationSuccessEvent event) {
        if (event.isIslogin()) {
            return;
        }
        YiboPreference.instance(this).setToken(event.getAccessToken());
        YiboPreference.instance(this).setLoginState(true);
        //登录成功后，获取头像等数据
        header.syncHeaderWebDatas(this);
        refreshNewMainPageLoginBlock(YiboPreference.instance(this).isLogin(), header.getAccountName(), header.getBalance());
        //登录成功后，同步登录帐户后的UI状态
        toggleLoginView(indexBottom);
        //登录成功后，获取未读消息数
        actionMsg();
    }
    @Subscribe
    public void onEvent(VerifyEvent event){
        if (event.isSuccess()){
            recreate();
        }else{
            showToast("连线逾时，请切换更稳定的网路环境后重启动APP");
        }
    }

    //确认提交检测服务 是否注销
    public boolean isRunService(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void actionLoginOrRegisterActivity(String userName, String password, int type) {
        if (config != null && config.getNewmainpage_switch().equals("on")) {
            LoginAndRegisterActivity.createIntent(this, userName, password, type);
        } else {
            if (type == 0) {
                LoginActivity.createIntent(this, userName, password);
            } else if (type == 1) {
                RegisterActivity.createIntent(this);
            }
        }
    }
}
