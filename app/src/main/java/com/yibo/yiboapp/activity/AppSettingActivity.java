package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.CacheRepository;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.CheckUpdateBean;
import com.yibo.yiboapp.entify.CheckUpdateWraper;
import com.yibo.yiboapp.entify.LoginOutWraper;
import com.yibo.yiboapp.entify.MainPopupTime;
import com.yibo.yiboapp.entify.RealDomainWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.SysConfigWraper;
import com.yibo.yiboapp.route.LDNetActivity.RouteCheckingActivity;
import com.yibo.yiboapp.utils.DateUtils;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.RouteUrlChooseDialog;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.RequestQueue;
import crazy_wrapper.Crazy.dialog.ActionSheetDialog;
import crazy_wrapper.Crazy.dialog.OnOperItemClickL;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

public class AppSettingActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {

    private CheckBox autoLoginBox;
    private RelativeLayout autoLoginView;
    private RelativeLayout soundLayout;
    private RelativeLayout kaijiangsoundlayout;
    private RelativeLayout yyyLayout;
    private RelativeLayout mRlAutoUpdate;
    private RelativeLayout rl_wenxintishi;
    private RelativeLayout rl_app_route_checking;
    private RelativeLayout start_up_activity_count_down;
    private RelativeLayout start_new_work_service;
    private RelativeLayout rl_safety_center;

    private CheckBox yyyBox;
    private CheckBox orderSound;//下注声音
    private CheckBox kaijiangSound;//开奖声音
    private CheckBox startUpCoundDown;//启动页倒计时开关
    private CheckBox startNetwork;//网路监测开关

    private CheckBox mCbAutoUpdate;//自动更新
    private CheckBox cb_wenxintishi;//温馨提示
    private View mVersionView;
    private View mClearView;
    private View mClearBrowserView;
    private View aboutusView;
    private TextView tv_browser;
    private TextView tv_main_popup; //首页弹窗时间间隔
    private ImageView img_main_popup; //首页弹窗时间间隔箭头
    private int checkUpdateCount = 0;//检测自动更新的次数
    private TextView tv_choose_route;


    Button exitBtn;
    MyHandler myHandler;

    public static final int lOGINOUT_REQUEST = 0x01;
    public static final int SYS_CONFIG_REQUEST = 0x08;
    public static final int CHECK_UPDATE_REQUEST = 0x02;
    private RouteUrlChooseDialog chooseDialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_app_setting);
        initView();
        myHandler = new MyHandler(this);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.app_setting));
        initChildView();
    }

    private void initChildView() {

        //自动更新
        mRlAutoUpdate = (RelativeLayout) findViewById(R.id.rl_auto_check_update);
        autoLoginView = (RelativeLayout) findViewById(R.id.app_auto_login_layout);
        rl_wenxintishi = (RelativeLayout) findViewById(R.id.rl_wenxintishi);
        rl_app_route_checking = findViewById(R.id.rl_app_route_checking);

        autoLoginBox = (CheckBox) autoLoginView.findViewById(R.id.auto_login_checkbox);
        yyyLayout = (RelativeLayout) findViewById(R.id.yaoyiyao);
        soundLayout = (RelativeLayout) findViewById(R.id.touzhu_sound);
        kaijiangsoundlayout = (RelativeLayout) findViewById(R.id.rl_kaijiangshengyin);
        yyyBox = (CheckBox) yyyLayout.findViewById(R.id.yyy_checkbox);
        orderSound = (CheckBox) findViewById(R.id.sound_checkbox);
        kaijiangSound = (CheckBox) findViewById(R.id.cb_kaijiangshengyin);
        cb_wenxintishi = (CheckBox) findViewById(R.id.cb_wenxintishi);
        startUpCoundDown = findViewById(R.id.cb_count_down);
        startNetwork = findViewById(R.id.cb_network_service);
        mVersionView = findViewById(R.id.rl_app_setting_version);
        mClearView = findViewById(R.id.clear_cache_layout);
        mClearBrowserView = findViewById(R.id.clear_browser_layout);
        aboutusView = findViewById(R.id.rl_app_setting_about);
        tv_browser = (TextView) findViewById(R.id.tv_browser);
        mCbAutoUpdate = (CheckBox) findViewById(R.id.cb_auto_check_update);
        tv_main_popup = findViewById(R.id.act_setting_main_popup_text);
        img_main_popup = findViewById(R.id.act_setting_main_popup_img);
        start_up_activity_count_down = findViewById(R.id.start_up_activity_count_down);
        start_new_work_service = findViewById(R.id.start_new_work_service);
        rl_safety_center = findViewById(R.id.rl_safety_center);

        tv_choose_route = findViewById(R.id.tv_choose_route);
        tv_choose_route.setText(YiboPreference.instance(this).getCHOOSE_ROUTE_NAME());
        findViewById(R.id.rl_app_route_choose).setOnClickListener(v -> {
            List<RealDomainWraper.ContentBean> contentBeans = new Gson().fromJson(YiboPreference.instance(AppSettingActivity.this).getROUTE_URLS(), new TypeToken<ArrayList<RealDomainWraper.ContentBean>>() {
            }.getType());
            if (contentBeans == null || contentBeans.isEmpty()) {
                showToast(R.string.no_route_urls);
                return;
            }
            if (chooseDialog == null) {
                chooseDialog = new RouteUrlChooseDialog(AppSettingActivity.this, contentBeans);
                chooseDialog.setChooseListener(new RouteUrlChooseDialog.OnRouteChooseListener() {
                    @Override
                    public void onChoose(@NonNull RealDomainWraper.ContentBean contentBean, int position) {
                        tv_choose_route.setText(TextUtils.isEmpty(contentBean.getName()) ? "" : contentBean.getName());
                    }
                    @Override
                    public void onAutoRouteFailed() {}
                });
            }
            chooseDialog.show();
        });

        exitBtn = (Button) findViewById(R.id.exit);
        mRlAutoUpdate.setOnClickListener(this);
        autoLoginView.setOnClickListener(this);
        yyyLayout.setOnClickListener(this);
        mVersionView.setOnClickListener(this);
        mClearView.setOnClickListener(this);
        mClearBrowserView.setOnClickListener(this);
        aboutusView.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        soundLayout.setOnClickListener(this);
        kaijiangsoundlayout.setOnClickListener(this);
        rl_wenxintishi.setOnClickListener(this);
        rl_app_route_checking.setOnClickListener(this);
        start_up_activity_count_down.setOnClickListener(this);
        start_new_work_service.setOnClickListener(this);

        String gesture_pwd_switch = UsualMethod.getConfigFromJson(this).getGesture_pwd_switch();
        if ("on".equals(gesture_pwd_switch)) {
            rl_safety_center.setOnClickListener(this);
        } else {
            rl_safety_center.setVisibility(View.GONE);
            findViewById(R.id.divider_safety_center).setVisibility(View.GONE);
        }

        findViewById(R.id.act_setting_main_popup_layout).setOnClickListener(this);
        setPopupStatus(UsualMethod.showMainPopupSpaceTime());
        autoLoginBox.setChecked(YiboPreference.instance(this).isAutoLogin());
        yyyBox.setChecked(YiboPreference.instance(this).isVirateAllow());
        orderSound.setChecked(YiboPreference.instance(this).isButtonSoundAllow());
        kaijiangSound.setChecked(YiboPreference.instance(this).isKaiJiangSoundAllow());
        isOpenkaijiangSound = YiboPreference.instance(this).isKaiJiangSoundAllow();
        mCbAutoUpdate.setChecked(YiboPreference.instance(this).isAutoCheckUpdate());
        cb_wenxintishi.setChecked(YiboPreference.instance(this).isWarmRemind());
        startUpCoundDown.setChecked(YiboPreference.instance(this).isShowCountDown());
        startNetwork.setChecked(YiboPreference.instance(this).isStartNetworkService());


        SysConfig config = UsualMethod.getConfigFromJson(this);
        String on_off = config.getMulti_broswer();
        if ("off".equals(on_off)) {
            mClearBrowserView.setVisibility(View.GONE);
        } else {
            mClearBrowserView.setVisibility(View.VISIBLE);
        }

        browsertext();//显示默认浏览器

        autoLoginBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                YiboPreference.instance(AppSettingActivity.this).setAutoLogin(b);
            }
        });

        yyyBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                YiboPreference.instance(AppSettingActivity.this).setVibrateAllow(b);
            }
        });

        orderSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                YiboPreference.instance(AppSettingActivity.this).setButtonSoundAllow(b);
            }
        });

        kaijiangSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                YiboPreference.instance(AppSettingActivity.this).setKaiJiangSoundAllow(isChecked);
            }
        });

        mCbAutoUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                YiboPreference.instance(AppSettingActivity.this).setAutoCheckUpdate(isChecked);
            }
        });

        cb_wenxintishi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                YiboPreference.instance(AppSettingActivity.this).setWarmRemind(isChecked);
            }
        });

        startUpCoundDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                YiboPreference.instance(AppSettingActivity.this).setShowCountDown(isChecked);
            }
        });
        startNetwork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                YiboPreference.instance(AppSettingActivity.this).setStartNetworkService(isChecked);
            }
        });


    }

    private void actionExit() {
        final CrazyRequest request = UsualMethod.startAsyncConfig(this, SYS_CONFIG_REQUEST);
        RequestManager.getInstance().startRequest(this, request);
        UsualMethod.actionLoginOut(this, lOGINOUT_REQUEST, true, this);
    }


    private boolean isOpenkaijiangSound;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_wenxintishi:
                boolean warmRemind = YiboPreference.instance(this).isWarmRemind();
                YiboPreference.instance(this).setWarmRemind(!warmRemind);
                cb_wenxintishi.setChecked(YiboPreference.instance(this).isWarmRemind());
                break;
            case R.id.app_auto_login_layout:
                boolean autoLogin = YiboPreference.instance(this).isAutoLogin();
                YiboPreference.instance(this).setAutoLogin(!autoLogin);
                autoLoginBox.setChecked(YiboPreference.instance(this).isAutoLogin());
                break;
            case R.id.yaoyiyao:
                boolean yyystate = YiboPreference.instance(this).isVirateAllow();
                YiboPreference.instance(this).setVibrateAllow(!yyystate);
                yyyBox.setChecked(YiboPreference.instance(this).isVirateAllow());
                break;
            case R.id.touzhu_sound:
                boolean isSoundOpened = YiboPreference.instance(this).isButtonSoundAllow();
                YiboPreference.instance(this).setButtonSoundAllow(!isSoundOpened);
                orderSound.setChecked(YiboPreference.instance(this).isButtonSoundAllow());
                break;
            case R.id.rl_kaijiangshengyin:

                boolean isOpenSound = YiboPreference.instance(this).isKaiJiangSoundAllow();
                YiboPreference.instance(this).setKaiJiangSoundAllow(!isOpenSound);
                kaijiangSound.setChecked(YiboPreference.instance(this).isKaiJiangSoundAllow());

//                isOpenkaijiangSound = !isOpenkaijiangSound;
//                YiboPreference.instance(this).setKaiJiangSoundAllow(isOpenkaijiangSound);
//                kaijiangSound.setChecked(isOpenkaijiangSound);
                break;
            //自动更新
            case R.id.rl_auto_check_update:
                boolean isAutoUpDate = YiboPreference.instance(this).isAutoCheckUpdate();
                mCbAutoUpdate.setChecked(!isAutoUpDate);
                YiboPreference.instance(this).setAutoCheckUpdate(!isAutoUpDate);

                break;
            case R.id.exit:
                actionExit();
                break;
//            case R.id.remember_pwd_layout:
//                Intent mIntentAbout = new Intent(AppSettingActivity.this, AboutActivity.class);
//                mIntentAbout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(mIntentAbout);
//                break;
            case R.id.rl_app_setting_version:
                checkUpdateCount = 0;
                actionCheckVersion(0, true);
                break;
            case R.id.clear_cache_layout:
                //清除Glide内存缓存，必须在主线程操作
                Glide.get(this).clearMemory();
                RequestQueue.getInstance().getCache().evictAll();
                new Thread(new ClearGlideRunnale(this)).start();
                YiboPreference.instance(this).setLauncherImg("");
                CacheRepository.getInstance().clearData(getApplicationContext());
                break;
            case R.id.clear_browser_layout:
                clear();
                break;
            case R.id.rl_app_setting_about:
                AboutActivity.createIntent(this);
                break;
            case R.id.rl_app_route_checking:
                startActivity(new Intent(this, RouteCheckingActivity.class));
                break;
            case R.id.act_setting_main_popup_layout: //首页弹窗时间间隔
                final String[] finalSplit = Constant.DEFAULT_MAIN_POPUP_SPACE_TIME.split(",");
                final ActionSheetDialog dialog = new ActionSheetDialog(this, finalSplit, null);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        try {
                            String temp = finalSplit[position];
                            int time = 5000;
                            if (temp.equals("1分钟")) {
                                time = 60000;
                            } else if (temp.equals("5分钟")) {
                                time = 5 * 60000;
                            } else if (temp.equals("10分钟")) {
                                time = 10 * 60000;
                            } else if (temp.equals("30分钟")) {
                                time = 30 * 60000;
                            } else if (temp.equals("1小时")) {
                                time = 60 * 60000;
                            }
                            MainPopupTime.MainPopupTimeDetail detail = new MainPopupTime.MainPopupTimeDetail("",
                                    time, DateUtils.getSysTime());
                            UsualMethod.setMainPopupTime(detail);
                            setPopupStatus(temp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case R.id.start_up_activity_count_down:
                boolean isShowStartUp = YiboPreference.instance(this).isShowCountDown();
                startUpCoundDown.setChecked(!isShowStartUp);
                YiboPreference.instance(this).setShowCountDown(!isShowStartUp);
                break;
            case R.id.start_new_work_service:
                boolean isStartNetworkService = YiboPreference.instance(this).isStartNetworkService();
                startNetwork.setChecked(!isStartNetworkService);
                YiboPreference.instance(this).setStartNetworkService(!isStartNetworkService);
                break;
            case R.id.rl_safety_center:
                //试玩账号不可以设置手势密码
                if (YiboPreference.instance(this).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                    showToast("操作权限不足，请联系客服");
                    return;
                }
                startActivity(new Intent(this, GesturePswViewSetActivity.class));
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    private void setPopupStatus(String time) {
        if (!time.equals("默认")) {
            tv_main_popup.setVisibility(View.VISIBLE);
            img_main_popup.setVisibility(View.GONE);
            tv_main_popup.setText(UsualMethod.showMainPopupSpaceTime());
        } else {
            tv_main_popup.setVisibility(View.GONE);
            img_main_popup.setVisibility(View.VISIBLE);
        }
    }

    private void checkUpdate(String curVersion, String bundleId, boolean showDialog, int signVersion) {
        CrazyRequest checkRequest = UsualMethod.checkUpdate(this, curVersion, bundleId, showDialog, CHECK_UPDATE_REQUEST, signVersion);
        RequestManager.getInstance().startRequest(this, checkRequest);
    }

    private void actionCheckVersion(int signVersion, boolean isShow) {
        //请求后台获取更新内容
        checkUpdate(Utils.getVersionName(this), BuildConfig.APPLICATION_ID, isShow, signVersion);
    }


    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == lOGINOUT_REQUEST) {
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
        } else if (action == SYS_CONFIG_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.get_system_config_fail);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
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
            }

        } else if (action == CHECK_UPDATE_REQUEST) {
            checkUpdateCount++;
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.check_version_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.check_version_fail);
                return;
            }
            Object regResult = result.result;
            CheckUpdateWraper reg = (CheckUpdateWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.check_version_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            CheckUpdateBean content = reg.getContent();
            if (content != null) {
                String updateUrl = content.getUrl();
                if (Utils.isEmptyString(updateUrl)) {
                    if (checkUpdateCount == 1) {
                        actionCheckVersion(1, false);
                        return;
                    } else {
                        showToast("没有新版本更新地址，请重试");
                    }
                }
                String updateContent = content.getContent();
                UsualMethod.showUpdateContentDialog(this, content.getVersion(), updateContent, updateUrl);
            } else {
                if (checkUpdateCount == 1) {
                    actionCheckVersion(1, false);
                    return;
                }
                showToast(R.string.check_version_fail);
            }
        }
    }

    private final class MyHandler extends Handler {

        private WeakReference<AppSettingActivity> mReference;
        private AppSettingActivity ap;

        public MyHandler(AppSettingActivity ap) {
            mReference = new WeakReference<>(ap);
            if (mReference != null) {
                this.ap = mReference.get();
            }
        }

        public void handleMessage(Message message) {
            if (ap == null) {
                return;
            }
            showToast(R.string.clear_cache_success);
        }
    }

    private final class ClearGlideRunnale implements Runnable {

        Context context;

        ClearGlideRunnale(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            Glide.get(context).clearDiskCache();
            myHandler.sendEmptyMessage(0);
        }
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, AppSettingActivity.class);
        context.startActivity(intent);
    }


    void clear() {
        SharedPreferences sp = getSharedPreferences("browser", 0);
        sp.edit().clear().commit();
        Toast.makeText(this, "清除默认支付浏览器成功", Toast.LENGTH_SHORT).show();
        tv_browser.setText("");
    }


    void browsertext() {
        SharedPreferences sp = getSharedPreferences("browser", 0);
        String browser = sp.getString("browser", "no");
        String str = "";
        switch (browser) {
            case "0":
                str = "系统默认浏览器";
                break;

            case "1":
                str = "UC浏览器";
                break;
            case "2":
                str = "QQ浏览器";
                break;
            case "3":
                str = "谷歌浏览器";
                break;
            case "4":
                str = "火狐浏览器";
                break;
        }
        if (!"".equals(str)) {
            tv_browser.setText("(" + str + ")");
        } else {
            tv_browser.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
