package com.yibo.yiboapp.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.example.anuo.immodule.BuildConfig;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.utils.AESUtils;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.view.CommonDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaConfiguration;
import com.netease.nis.captcha.CaptchaListener;
import com.simon.utils.LogUtil;
import com.snail.antifake.jni.EmulatorDetectUtil;
import com.yibo.yiboapp.Event.LoginEvent;
import com.yibo.yiboapp.Event.VervificationSuccessEvent;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.LoginResult;
import com.yibo.yiboapp.entify.LoginResultWrap;
import com.yibo.yiboapp.entify.RealDomainWraper;
import com.yibo.yiboapp.entify.RegConfig;
import com.yibo.yiboapp.entify.RegConfigWrapper;
import com.yibo.yiboapp.entify.RegisterResult;
import com.yibo.yiboapp.entify.RegisterResultWrapper;
import com.yibo.yiboapp.entify.Sitekey;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.SysConfigWraper;
import com.yibo.yiboapp.interfaces.OnVertifyResultLinsenter;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.route.LDNetActivity.RouteCheckingActivity;
import com.yibo.yiboapp.ui.RegFieldViewNew;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.GoogleRecaptchaDialog;
import com.yibo.yiboapp.views.RouteUrlChooseDialog;
import com.yibo.yiboapp.views.WebVertifyDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * login screen
 *
 * @author johnson
 */
public class LoginAndRegisterActivity extends BaseActivity implements OnCheckedChangeListener,
        SessionResponse.Listener<CrazyResult<Object>>, OnVertifyResultLinsenter {

    private static final String TAG = LoginAndRegisterActivity.class.getSimpleName();
    public static final int TYPE_LOGIN = 0;
    public static final int TYPE_REGISTER = 1;
    public static final int TYPE_GUESS = 2;
    int type = 0;
    private ImageView rootBackground;

    private XEditText username;
    private LinearLayout cleanText;
    private XEditText password;
    private ImageView switchPwd;

    RelativeLayout vcode_layout;
    private XEditText vcodeInput;
    private ImageView vcodeImg;

    private CheckBox rememberPwd;
    private Button loginBtn;
    //    private TextView registerNow;
    private TextView forgetPwd;
    private TextView freePlay;
    TextView onlineCustom;
    SimpleDraweeView logoView;
    private TextView actionBack;
    private TextView agentReg;
    private TextView route_checking;
    WebVertifyDialog pictureVertifyDialog;
    Drawable userNameLeftDrawable;
    Drawable userPwdLeftDrawable;
    Drawable rightDrawable;
    private TextView tv_choose_route;

    public static final int GUEST_REGISTER_REQUEST = 0x02;
    public static final int GET_CAPTCHA_ID = 0x03;
    public static final int SYS_CONFIG_REQUEST = 0x08;
    public static final int ROBOT_CALL_BACK = 0x09;
    private CommonDialog fixDomainUrlDialog;
    private SysConfig sc;
    private RouteUrlChooseDialog chooseDialog;

    //注册stephen  start=====
    private boolean isAgent = false; //true:代理注册
    private String agentBtnTxt;

    XEditText account;
    XEditText registerPassword;
    XEditText confirm_pwd;
    TextView serviceOnline;
//    WebVertifyDialog pictureVertifyDialog;

    ConstraintLayout llLogin;
    Button regBtn;
    LinearLayout infosLayout;
    List<RegFieldViewNew> regViews;
    RegFieldViewNew vcodeView;

    public static final int REG_CONFIG_REQUEST = 0x04;
    public static final int REGISTER_REQUEST = 0x05;
//    public static final int GUEST_REGISTER_REQUEST = 0x06;

    View loginSwitch;
    View loginView;
    View registerSwitch;
    View registerView;
    Guideline percentLine;
    boolean isClicklogin = true;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.login_and_register);
        /*statusBar透明*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
//        StatusBarUtil.immersive(window, Color.WHITE);
        isAgent = this.getIntent().getBooleanExtra("isAgent", false);
        type = this.getIntent().getIntExtra("type", TYPE_LOGIN);

        EventBus.getDefault().register(this);
        sc = UsualMethod.getConfigFromJson(this);
        initView();
        getVerifySwitch();

        regViews = new ArrayList<>();
        //先获取注册配置信息
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                actionGetRegConfig();
            }
        }, 500);
        initNewMainPageLogin();
    }

    private void initNewMainPageLogin() {
        long durationTime = 300L;
        llLogin = findViewById(R.id.ll_login);
        loginSwitch = findViewById(R.id.login_switch);
        loginView = findViewById(R.id.ll_login_content);
        registerSwitch = findViewById(R.id.register_switch);
        registerView = findViewById(R.id.ll_register_content);
        loginSwitch.setOnClickListener(v -> {
            if (loginSwitch.getAlpha() == 1) {
                type = TYPE_LOGIN;
                changePercentLine(true);
                ObjectAnimator.ofFloat(loginSwitch, "Alpha", 0f).setDuration(durationTime).start();
                ObjectAnimator.ofFloat(loginView, "Alpha", 1f).setDuration(durationTime).start();
                ObjectAnimator.ofFloat(registerSwitch, "Alpha", 1f).setDuration(durationTime).start();
                ObjectAnimator.ofFloat(registerView, "Alpha", 0f).setDuration(durationTime).start();
            }
        });
        registerSwitch.setOnClickListener(v -> {
            if (registerSwitch.getAlpha() == 1) {
                type = TYPE_REGISTER;
                changePercentLine(false);
                ObjectAnimator.ofFloat(registerSwitch, "Alpha", 0f).setDuration(durationTime).start();
                ObjectAnimator.ofFloat(registerView, "Alpha", 1f).setDuration(durationTime).start();
                ObjectAnimator.ofFloat(loginSwitch, "Alpha", 1f).setDuration(durationTime).start();
                ObjectAnimator.ofFloat(loginView, "Alpha", 0f).setDuration(durationTime).start();
            }
        });
        if (type == 0) {
            loginSwitch.performClick();
        } else if (type == 1) {
            registerSwitch.performClick();
        }
    }

    private void getVerifySwitch() {
        HttpUtil.get(this, Urls.GET_VERIFY_SWITCH, null, false, "获取配置", result -> {
            if (result.isSuccess()) {
                try {
                    String content = result.getContent();
                    JSONObject jsonObject = new JSONObject(content);
                    sc.setOn_off_recaptcha_verify(jsonObject.optString("on_off_recaptcha_verify"));
                    sc.setNative_vertify_captcha_switch(jsonObject.optString("native_vertify_captcha_switch"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!Utils.isEmptyString(username.getText().toString().trim())) {
            Drawable drawable = getResources().getDrawable(R.drawable.new_page_delete_drawable);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            username.setCompoundDrawables(null, null, drawable, null);
        }
        if (!Utils.isEmptyString(password.getText().toString().trim())) {
            Drawable drawable = getResources().getDrawable(R.drawable.new_page_delete_drawable);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            password.setCompoundDrawables(null, null, drawable, null);
        }
    }

    protected void initView() {
        super.initView();
        rootBackground = findViewById(R.id.rootBackgroundImg);
        if (sc.getLogin_reg_background_image_url() != null && !TextUtils.isEmpty(sc.getLogin_reg_background_image_url())) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.new_page_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);
            Glide.with(this)
                    .load(sc.getLogin_reg_background_image_url().trim())
                    .apply(options)
                    .into(rootBackground);
        }

        tvMiddleTitle.setText(getString(R.string.login));
        tvRightText.setVisibility(View.GONE);
        tvBackText.setVisibility(View.VISIBLE);
        tvBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPress();
            }
        });
        username = (XEditText) findViewById(R.id.user_name);
        username.setDrawableRightListener(new XEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                username.setText("");
            }
        });
        username.addTextChangedListener(usernameWather);
        cleanText = (LinearLayout) findViewById(R.id.clean_text);
        password = (XEditText) findViewById(R.id.user_pwd);
        password.addTextChangedListener(passwordWather);
        password.setDrawableRightListener(new XEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                password.setText("");
            }
        });

        vcodeInput = (XEditText) findViewById(R.id.vcode_input);
        vcodeImg = (ImageView) findViewById(R.id.vcode_img);
        vcodeImg.setOnClickListener(this);
        vcode_layout = (RelativeLayout) findViewById(R.id.vcode_layout);
        ((TextView) findViewById(R.id.tv_versio_name)).setText("当前版本号:" + Utils.getVersionName(this) + "(" + BuildConfig.apk_code + ")");
        tv_choose_route = findViewById(R.id.tv_choose_route);

        String routerName = "";
        routerName = YiboPreference.instance(this).getCHOOSE_ROUTE_NAME();
        if (TextUtils.isEmpty(routerName)) {
            routerName = "当前线路:" + "线路1(默认)";
        } else {
            routerName = "当前线路:" + YiboPreference.instance(this).getCHOOSE_ROUTE_NAME();
        }
        tv_choose_route.setText(routerName);
        findViewById(R.id.rl_choose_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RealDomainWraper.ContentBean> contentBeans = new Gson().fromJson(YiboPreference.instance(LoginAndRegisterActivity.this).getROUTE_URLS(), new TypeToken<ArrayList<RealDomainWraper.ContentBean>>() {
                }.getType());
                if (contentBeans == null || contentBeans.isEmpty()) {
                    showToast(R.string.no_route_urls);
                    return;
                }
                if (chooseDialog == null) {
                    chooseDialog = new RouteUrlChooseDialog(LoginAndRegisterActivity.this, contentBeans);
                    chooseDialog.setChooseListener(new RouteUrlChooseDialog.OnRouteChooseListener() {
                        @Override
                        public void onChoose(@NonNull RealDomainWraper.ContentBean contentBean, int position) {
                            tv_choose_route.setText("当前线路:" + (TextUtils.isEmpty(contentBean.getName()) ? "" : contentBean.getName()));
                        }
                        @Override
                        public void onAutoRouteFailed() {}
                    });
                }
                chooseDialog.show();
            }
        });

        switchPwd = (ImageView) findViewById(R.id.switch_pwd);
        rememberPwd = (CheckBox) findViewById(R.id.remember_pwd);
        loginBtn = (Button) findViewById(R.id.login_btn);
//        registerNow = (TextView) findViewById(R.id.register_now);
        forgetPwd = (TextView) findViewById(R.id.forget_pwd);
        freePlay = (TextView) findViewById(R.id.guest_login);
        onlineCustom = (TextView) findViewById(R.id.online_custom);
        logoView = (SimpleDraweeView) findViewById(R.id.logo);
        route_checking = findViewById(R.id.route_checking);

        route_checking.setOnClickListener(this);
        freePlay.setOnClickListener(this);
        onlineCustom.setOnClickListener(this);

        ivMoreMenu.setVisibility(View.GONE);
        cleanText.setOnClickListener(this);
        switchPwd.setOnClickListener(this);
        rememberPwd.setOnCheckedChangeListener(this);
        loginBtn.setOnClickListener(this);
//        registerNow.setOnClickListener(this);
        forgetPwd.setOnClickListener(this);
        agentReg = findViewById(R.id.agent_reg);
        actionBack = findViewById(R.id.back);
        actionBack.setOnClickListener(this);

        userNameLeftDrawable = getResources().getDrawable(R.drawable.new_page_account_drawable);
        userNameLeftDrawable.setBounds(0, 0, userNameLeftDrawable.getMinimumWidth(), userNameLeftDrawable.getMinimumHeight());
        userPwdLeftDrawable = getResources().getDrawable(R.drawable.new_page_password_drawable);
        userPwdLeftDrawable.setBounds(0, 0, userPwdLeftDrawable.getMinimumWidth(), userPwdLeftDrawable.getMinimumHeight());
        rightDrawable = getResources().getDrawable(R.drawable.new_page_delete_drawable);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

//        registerNow.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        registerNow.getPaint().setAntiAlias(true);

        route_checking.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        route_checking.getPaint().setAntiAlias(true);

        freePlay.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        freePlay.getPaint().setAntiAlias(true);

        forgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        forgetPwd.getPaint().setAntiAlias(true);

        onlineCustom.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        onlineCustom.getPaint().setAntiAlias(true);

        if (sc != null && !TextUtils.isEmpty(sc.getOnoff_show_remember_psd()) && "on".equalsIgnoreCase(sc.getOnoff_show_remember_psd())) {
            rememberPwd.setVisibility(View.VISIBLE);
            rememberPwd.setChecked(YiboPreference.instance(this).isRememberPwd());
        } else {
            rememberPwd.setVisibility(View.GONE);
        }

        //若是来自设置页面中的退出登录，则跳转到登录页时填入帐号密码
        String name = getIntent().getStringExtra("name");
        String pwd = getIntent().getStringExtra("pwd");
        username.setText(name);
        if (sc != null && !TextUtils.isEmpty(sc.getOnoff_show_remember_psd()) && "on".equalsIgnoreCase(sc.getOnoff_show_remember_psd())) {
            password.setText(pwd);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String sysConfig = YiboPreference.instance(LoginAndRegisterActivity.this).getSysConfig();
                if (!Utils.isEmptyString(sysConfig)) {
                    SysConfig sc = new Gson().fromJson(sysConfig, SysConfig.class);
                    if (!Utils.isEmptyString(sc.getOnoff_mobile_guest_register()) &&
                            sc.getOnoff_mobile_guest_register().equals("on")) {
                        freePlay.setVisibility(View.VISIBLE);
                    } else {
                        freePlay.setVisibility(View.GONE);
                    }
                    if (!Utils.isEmptyString(sc.getOnline_customer_showphone()) &&
                            sc.getOnline_customer_showphone().equals("on")) {
//                        onlineCustom.setVisibility(View.VISIBLE);
                        serviceOnline.setVisibility(View.VISIBLE);
                    } else {
//                        onlineCustom.setVisibility(View.GONE);
                        serviceOnline.setVisibility(View.GONE);
                    }

                    if (sc.getNative_mobile_agent_register_enter().equals("on")) {
                        agentReg.setVisibility(View.VISIBLE);
                        agentReg.setOnClickListener(LoginAndRegisterActivity.this); //注册代理
                    }

//                    if (!Utils.isEmptyString(sc.getOnoff_register()) &&
//                            sc.getOnoff_register().equals("on") && !"on".equals(sc.getOnoff_mobile_app_reg())) {
//                        registerNow.setVisibility(View.VISIBLE);
//                    } else {
//                        registerNow.setVisibility(View.GONE);
//                    }
                    //更新LOGO
//                    updateWebLogo(logoView, sc.getLogo_for_login().trim());
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse(sc.getLogo_for_login().trim()))
                            .setAutoPlayAnimations(true)//设置为true将循环播放Gif动画
                            .build();
                    logoView.setController(controller);

                    //是否显示验证码,根据开关
                    if (!Utils.isEmptyString(sc.getOnoff_mobile_verify_code()) && sc.getOnoff_mobile_verify_code().equalsIgnoreCase("on")) {
                        vcode_layout.setVisibility(View.VISIBLE);
                        fillVcodeImage();
                    } else {
                        vcode_layout.setVisibility(View.GONE);
                    }
                }
            }
        }, 200);
        logoView.setOnClickListener(this);

        account = (XEditText) findViewById(R.id.account);
        registerPassword = (XEditText) findViewById(R.id.password);
        confirm_pwd = (XEditText) findViewById(R.id.confirm_password);
        serviceOnline = (TextView) findViewById(R.id.serve_online);
        regBtn = (Button) findViewById(R.id.reg_btn);

        regBtn.setOnClickListener(this);
        serviceOnline.setOnClickListener(this);

        serviceOnline.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        serviceOnline.getPaint().setAntiAlias(true);

        infosLayout = (LinearLayout) findViewById(R.id.infos);

        tvRightText.setVisibility(View.GONE);

        if (isAgent) {
            tvMiddleTitle.setText(getString(R.string.agent_register));
            agentBtnTxt = this.getIntent().getStringExtra("agentBtnTxt");
            findViewById(R.id.act_bottom_view).setVisibility(View.GONE);
        } else {
            tvMiddleTitle.setText(getString(R.string.register_str));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String sysConfig = YiboPreference.instance(LoginAndRegisterActivity.this).getSysConfig();
                    if (!Utils.isEmptyString(sysConfig)) {
                        SysConfig sc = new Gson().fromJson(sysConfig, SysConfig.class);
                        if (!Utils.isEmptyString(sc.getOnoff_mobile_guest_register()) &&
                                sc.getOnoff_mobile_guest_register().equals("on")) {
                            tvRightText.setVisibility(View.VISIBLE);
                            tvRightText.setText(getString(R.string.play_free));
                        } else {
                            tvRightText.setVisibility(View.GONE);
                        }
                    }
                }
            }, 200);
        }

        percentLine = findViewById(R.id.percent_line);
    }

    public void changePercentLine(boolean isLogin) {
        long durationTime = 300L;
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) percentLine.getLayoutParams();
        if (isLogin) {
            ValueAnimator animator;
            if (!Utils.isEmptyString(sc.getOnoff_register()) &&
                    sc.getOnoff_register().equals("on") && !"on".equals(sc.getOnoff_mobile_app_reg())) {
                animator = ValueAnimator.ofFloat(0.12f, 0.88f).setDuration(durationTime);
            } else {
                registerSwitch.setVisibility(View.GONE);
                registerView.setVisibility(View.GONE);
                llLogin.setBackgroundResource(R.drawable.new_login_full_bg);
                animator = ValueAnimator.ofFloat(0.12f, 1.0f).setDuration(durationTime);
            }
            animator.addUpdateListener(animation -> {
                params.guidePercent = (Float) animation.getAnimatedValue();
                percentLine.setLayoutParams(params);
            });
            animator.start();
        } else {
            ValueAnimator animator = ValueAnimator.ofFloat(0.88f, 0.12f).setDuration(durationTime);
            animator.addUpdateListener(animation -> {
                params.guidePercent = (Float) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: " + (Float) animation.getAnimatedValue());
                percentLine.setLayoutParams(params);
            });
            animator.start();
        }
    }

    public void fillVcodeImage() {
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOGIN_VCODE_IMAGE_URL);
        if (Utils.isEmptyString(urls.toString())) {
            return;
        }
        GlideUrl gu = UsualMethod.getGlide(this, urls.toString());
        RequestOptions options = new RequestOptions().skipMemoryCache(true).placeholder(R.drawable.default_placeholder_picture)
                .error(R.drawable.default_placeholder_picture).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(getApplicationContext()).load(gu)
                .apply(options)
//                placeholder(R.drawable.default_placeholder_picture)
//                .error(R.drawable.default_placeholder_picture)
//                .diskCacheStrategy( DiskCacheStrategy.NONE )//禁用磁盘缓存
                .into(vcodeImg);
    }

//
//    private void updateWebLogo(final ImageView bg, String url) {
//
//        if (Utils.isEmptyString(url)) {
//            return;
//        }
//        String imgUrl = url.trim();
//        logoView.setVisibility(View.VISIBLE);
//
//        if (imgUrl.endsWith("gif") || imgUrl.endsWith("GIF")) {
//            Glide.with(this).load(imgUrl.trim()).asGif()
//                    .dontAnimate() //去掉显示动画
//                    .placeholder(R.drawable.default_picture)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(bg);
//        } else {
//            Glide.with(this).load(imgUrl.trim()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    Drawable drawable = new BitmapDrawable(resource);
//                    bg.setBackground(drawable);
//                }
//            });
//        }
//
//    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
        if (arg0.getId() == R.id.remember_pwd) {
            YiboPreference.instance(this).setRememberPwd(isChecked);
            if (!YiboPreference.instance(this).isRememberPwd()) {
                YiboPreference.instance(this).savePwd("");
            }
        }
    }
//
//
//    private void showModeList(final Context context) {
//        final String[] stringItems = getResources().getStringArray(R.array.caipiao_mode);
//        if (stringItems.length == 0) {
//            return;
//        }
////        View decorView = getWindow().getDecorView();
////        LinearLayout elv = BallonAnimator.find(decorView, R.id.viewid);
//        final ActionSheetDialog dialog = new ActionSheetDialog(context, stringItems, null);
//        dialog.title("我的标题");
//        dialog.isTitleShow(true).show();
//        dialog.setOnOperItemClickL(new OnOperItemClickL() {
//            @Override public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
//                dialog.dismiss();
//                YiboPreference.instance(context).setAccountMode(position);
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.clean_text:
                actionCleanText();
                break;
            case R.id.vcode_img:
                fillVcodeImage();
                break;
            case R.id.switch_pwd:
//                switchPwdState();
                break;
            case R.id.login_btn:
                isClicklogin = true;
//                robot();
                robotWebView();
                break;
//            case R.id.register_now:
            //stephen 切換註冊
//                actionRegister();
//                break;

            case R.id.route_checking:
                RouteCheckingActivity.createIntent(this);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.agent_reg:
                RegisterActivity.createIntent(this, true, getResources().getString(R.string.back_login));
                break;
            case R.id.forget_pwd:
//                actionForgetPwd();
                break;
            case R.id.guest_login:
//                actionLogin(false);
//                SysConfig sc = new Gson().fromJson(YiboPreference.instance(this).getSysConfig(),SysConfig.class);
//                YiboPreference.instance(this).setLoginState(true);
//                MainActivity.createIntent(this,sc.getMobileIndex());
//                finish();
                UsualMethod.registGuest(this, GUEST_REGISTER_REQUEST);
                break;
            case R.id.back_text:
                finish();
                break;
            case R.id.online_custom:
                SysConfig config = UsualMethod.getConfigFromJson(this);
                String version = config.getOnline_service_open_switch();
                if (!version.isEmpty()) {
                    boolean success = UsualMethod.viewService(this, version);
                    if (!success) {
                        showToast("没有在线客服链接地址，无法打开");
                    }
                }
                break;
            case R.id.logo:
                if (BuildConfig.DEBUG) {
                    if (fixDomainUrlDialog == null) {
                        String domainUrl = ChatSpUtils.instance(LoginAndRegisterActivity.this).getDomainUrl();
                        if (TextUtils.isEmpty(domainUrl)) {
                            domainUrl = BuildConfig.domain_url;
                        }
                        fixDomainUrlDialog = CommonDialog.create(LoginAndRegisterActivity.this, "修改主域名", domainUrl, "修改并重启", new CommonDialog.DialogClickListener() {
                            @Override
                            public void onInputListener(View view, String input) {
                                if (TextUtils.isEmpty(input)) {
                                    return;
                                }
                                StringBuffer url = new StringBuffer("https://");
                                if (input.startsWith("yibo") || input.startsWith("t4.")) {
                                    url.append(input).append(".com");
                                } else if (input.startsWith("http") || input.endsWith(".com")) {
                                    url = new StringBuffer(input);
                                } else {
                                    url.append("sky").append(input).append(".yb876.com");
                                }
                                ChatSpUtils.instance(LoginAndRegisterActivity.this).setDomainUrl(url.toString());
                                //重启应用
                                new Handler().postDelayed(() -> {
                                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
                                    LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(LaunchIntent);
                                }, 100);
                            }

                            @Override
                            public void onClick(View v) {

                            }
                        }, "取消", new CommonDialog.DialogClickListener() {
                            @Override
                            public void onInputListener(View view, String input) {

                            }

                            @Override
                            public void onClick(View v) {

                            }
                        }, false, false, true, false, true, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, true);
                    }
                    fixDomainUrlDialog.showAndInput();
                }
                break;

            //Stephen
            case R.id.reg_btn:
//                SysConfig sc = UsualMethod.getConfigFromJson(this);
                isClicklogin = false;
//                robot();
                robotWebView();
                break;
            case R.id.serve_online:
//                SysConfig config = UsualMethod.getConfigFromJson(this);
                String versionTest = sc.getOnline_service_open_switch();
                if (!versionTest.isEmpty()) {
                    boolean success = UsualMethod.viewService(this, versionTest);
                    if (!success) {
                        showToast("没有在线客服链接地址，无法打开");
                    }
                }
                break;
            case R.id.right_text:
                UsualMethod.registGuest(this, GUEST_REGISTER_REQUEST);
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证id
     */
    private void getCaptchaId() {
        HttpUtil.get(this, Urls.GET_CAPTCHA_ID, null, true, "获取验证码ID", result -> {
            if (result.isSuccess()) {
                String content = result.getContent();
                //解密id
                if (!TextUtils.isEmpty(content)) {
                    String id = AESUtils.decrypt(content, ConfigCons.CAPTCHA_VERTIFY_KEY, ConfigCons.CAPTCHA_VERTIFY_IV);
                    nativeVertify(id);
                } else {
                    showToast("验证码ID获取失败！");
                }
            } else {
                showToast("验证码ID获取失败！");
            }
        });
    }

    /**
     * 开启原生验证
     */
    private void nativeVertify(String id) {
        // 创建验证码回调监听器
        CaptchaListener captchaListener = new CaptchaListener() {
            @Override
            public void onReady() {

            }

            @Override
            public void onValidate(String result, String validate, String msg) {
                LogUtil.e("Captcha", "validate=" + validate + "---result=" + result + "---mas=" + msg);
                if (!TextUtils.isEmpty(validate)) {
                    //成功
                    LogUtil.e("Captcha", "成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (type) {
                                case TYPE_LOGIN:
                                    actionLogin(true, validate);
                                    break;
                                case TYPE_REGISTER:
                                    actionRegister(true, validate);
                                    break;
                            }
                        }
                    });
                } else {
                    //失败w
                    LogUtil.e("Captcha", "失败");
                }
            }

            //建议直接打印错误码，便于排查问题
            @Override
            public void onError(int code, String msg) {
                LogUtil.e("Captcha", "验证出错，错误码:" + code + " 错误信息:" + msg);
            }

            @Override
            public void onClose(Captcha.CloseType closeType) {
                if (closeType == Captcha.CloseType.USER_CLOSE) {
                    LogUtil.e("Captcha", "用户关闭验证码");
                } else if (closeType == Captcha.CloseType.VERIFY_SUCCESS_CLOSE) {
                    LogUtil.e("Captcha", "校验通过，流程自动关闭");
                }
            }
        };

        // 创建构建验证码的配置类，可配置详细选项请参看上面SDK接口 验证码属性配置类：CaptchaConfiguration
        CaptchaConfiguration configuration = new CaptchaConfiguration.Builder()
                .captchaId(id)// 验证码业务id
                // 验证码类型，默认为常规验证码（滑块拼图、图中点选、短信上行），如果要使用智能无感知请设置该类型，否则无需设置
//                .mode(CaptchaConfiguration.ModeType.MODE_INTELLIGENT_NO_SENSE)
                .listener(captchaListener) //设置验证码回调监听器
                .build(this); // Context，请使用Activity实例的Context
        //初始化并显示
        Captcha.getInstance().init(configuration).validate();
    }


    private void actionLogin(boolean nativeVertify, String validateCode) {

        String name = username.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String vcode = vcodeInput.getText().toString().trim();

        if (Utils.isEmptyString(name)) {
            showToast(R.string.please_input_username);
            return;
        }

        if (Utils.isEmptyString(pwd)) {
            showToast(R.string.please_input_password);
            return;
        }

        SysConfig sc = null;
        String sysConfig = YiboPreference.instance(LoginAndRegisterActivity.this).getSysConfig();
        if (!Utils.isEmptyString(sysConfig)) {
            sc = new Gson().fromJson(sysConfig, SysConfig.class);
            if (!Utils.isEmptyString(sc.getOnoff_mobile_verify_code()) && sc.getOnoff_mobile_verify_code().equalsIgnoreCase("on")) {
                if (Utils.isEmptyString(vcode)) {
                    showToast(R.string.please_input_vcode);
                    return;
                }
            }
        }

        final CrazyRequest request = UsualMethod.startAsyncConfig(this, SYS_CONFIG_REQUEST);
        RequestManager.getInstance().startRequest(this, request);
        UsualMethod.actionLogin(name, pwd, nativeVertify ? validateCode : null, this, vcode, true, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if(result == null){
                    showToast(R.string.login_fail);
                    return;
                }

                LoginResult loginResult = new Gson().fromJson(result.getContent(), LoginResult.class);
                if(!result.isSuccess()){
                    showToast(Utils.isEmptyString(result.getMsg()) ? getString(R.string.login_fail) : result.getMsg());
                    long accountId = loginResult.getAccountId();
                    int code = result.getCode();
                    if (code == 555) {
                        VerificationSetActivity.createIntent(LoginAndRegisterActivity.this, accountId, username.getText().toString().trim(), password.getText().toString().trim(), true);
                        return;
                    }
                    if (code == 666) {
                        DoVerificationActivity.createIntent(LoginAndRegisterActivity.this, accountId, username.getText().toString().trim(), password.getText().toString().trim(), true);
                        return;
                    }
                    showToast(Utils.isEmptyString(result.getMsg()) ? getString(R.string.login_fail) : result.getMsg());
                    //登录失败时显示图片验证码
                    vcode_layout.setVisibility(View.VISIBLE);
                    fillVcodeImage();
                }else {
                    YiboPreference.instance(LoginAndRegisterActivity.this).setToken(result.getAccessToken());
                    YiboPreference.instance(LoginAndRegisterActivity.this).setAccountId(loginResult.getAccountId());
                    YiboPreference.instance(LoginAndRegisterActivity.this).setLoginState(true);

                    //记住密码
                    if (YiboPreference.instance(LoginAndRegisterActivity.this).isRememberPwd()) {
                        YiboPreference.instance(LoginAndRegisterActivity.this).savePwd(password.getText().toString().trim());
                    } else {
                        YiboPreference.instance(LoginAndRegisterActivity.this).savePwd("");
                    }

                    int accountType = loginResult.getAccountType();
                    YiboPreference.instance(LoginAndRegisterActivity.this).setAccountMode(accountType);
                    //自动登录的情况下，要记住帐号
                    //if (YiboPreference.instance(this).isAutoLogin()) {
                    if (!Utils.isEmptyString(loginResult.getAccount())) {
                        YiboPreference.instance(LoginAndRegisterActivity.this).saveUsername(loginResult.getAccount());
                    } else {
                        YiboPreference.instance(LoginAndRegisterActivity.this).saveUsername(username.getText().toString().trim());
                    }
                    setResult(RESULT_OK);
                    EventBus.getDefault().post(new LoginEvent());
                    finish();
                }
            }
        });
    }

    /**
     * type 0 = 登录   type 1 = 注册  type 2 = 試玩
     */
    public static void createIntent(Context context, String name, String pwd, int type) {
        Intent intent = new Intent(context, LoginAndRegisterActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("pwd", pwd);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    private void actionCleanText() {
        username.setText("");
    }

    TextWatcher usernameWather = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = s.toString().trim();
            if (Utils.isEmptyString(name)) {
                username.setCompoundDrawables(userNameLeftDrawable, null, null, null);
            } else {
                username.setCompoundDrawables(userNameLeftDrawable, null, rightDrawable, null);
            }
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void afterTextChanged(Editable arg0) {
        }
    };

    TextWatcher passwordWather = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = s.toString().trim();
            if (Utils.isEmptyString(name)) {
                password.setCompoundDrawables(userPwdLeftDrawable, null, null, null);
            } else {
                password.setCompoundDrawables(userPwdLeftDrawable, null, rightDrawable, null);
            }
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void afterTextChanged(Editable arg0) {
        }
    };

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == GUEST_REGISTER_REQUEST) {
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
                    YiboPreference.instance(this).saveUsername(username.getText().toString().trim());
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
                SysConfig sc = new Gson().fromJson(YiboPreference.instance(this).getSysConfig(), SysConfig.class);
                MainActivity.createIntent(this);
                EventBus.getDefault().post(new LoginEvent());
                finish();
            }

            //Stephen 以下為合併註冊新增
        } else if (action == REG_CONFIG_REQUEST) {

            CrazyResult<Object> result = response.result;
            if (result == null) {
                ToastUtils.showShort(R.string.get_regconfig_fail);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                ToastUtils.showShort(Utils.isEmptyString(errorString) ? getString(R.string.get_regconfig_fail) : errorString);
                return;
            }

            Object regResult = result.result;

            RegConfigWrapper reg = (RegConfigWrapper) regResult;
            if (!reg.isSuccess()) {
                ToastUtils.showShort(Utils.isEmptyString(reg.getMsg()) ?
                        getString(R.string.get_regconfig_fail) : reg.getMsg());
                return;
            }

            List<RegConfig> regLists = reg.getContent();

            YiboPreference.instance(this).setToken(reg.getAccessToken());
            updateRegisterFields(regLists);
            //开始异步获取验证码图片
            if (!Utils.isEmptyString(sc.getOnoff_mobile_verify_code()) && sc.getOnoff_mobile_verify_code().equalsIgnoreCase("on")) {
                actionSyncVcodeImage();
            }
        } else if (action == REGISTER_REQUEST || action == GUEST_REGISTER_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                ToastUtils.showShort(R.string.register_fail);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                ToastUtils.showShort(Utils.isEmptyString(errorString) ? getString(R.string.register_fail) : errorString);
                return;
            }

            Object regResult = result.result;
            RegisterResultWrapper reg = (RegisterResultWrapper) regResult;
            if (!reg.isSuccess()) {
                ToastUtils.showShort(Utils.isEmptyString(reg.getMsg()) ?
                        getString(R.string.register_fail) : reg.getMsg());
                actionSyncVcodeImage();
                return;
            }

            YiboPreference.instance(this).setToken(reg.getAccessToken());
            YiboPreference.instance(this).setLoginState(true);

            //获取注册帐户相关信息
            RegisterResult content = reg.getContent();
            if (content != null) {
                if (isAgent) {
                    ArrayList<String> arr = new ArrayList<>();
                    for (int i = 0; i < regViews.size() - 1; i++) {
                        RegFieldViewNew re = regViews.get(i);
                        String txt = re.getRegName() + " : " + re.getValueString();
                        arr.add(txt);
                    }
                    LookThroughActivity.createIntent(this, arr, agentBtnTxt);
                } else {
                    int accountType = content.getAccountType();
                    YiboPreference.instance(this).setAccountMode(accountType);
                    YiboPreference.instance(this).saveGameVersion(content.getCpVersion());
                    //自动登录的情况下，要记住帐号密码
                    if (YiboPreference.instance(this).isAutoLogin()) {
//                    Utils.LOG(TAG,"the account ====== "+content.getAccount());
                        if (!Utils.isEmptyString(content.getAccount())) {
                            YiboPreference.instance(this).saveUsername(content.getAccount());
                        } else {
                            YiboPreference.instance(this).saveUsername(
                                    account.getText().toString().trim());
                        }
                        YiboPreference.instance(this).savePwd(registerPassword.getText().toString().trim());
                    }
                    SysConfig sc = new Gson().fromJson(YiboPreference.instance(this).getSysConfig(), SysConfig.class);
                    MainActivity.createIntent(this);
                    EventBus.getDefault().post(new LoginEvent());
                }

                finish();
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
                if (configJson.equals(YiboPreference.instance(this).getSysConfig())) {
                    // 当前保存的系统配置已经是最新的了
                    return;
                }
                YiboPreference.instance(this).saveConfig(configJson);
                YiboPreference.instance(this).saveYjfMode(stw.getContent().getYjf());
                //保存系统版本号版本号
                YiboPreference.instance(this).saveGameVersion(stw.getContent().getVersion());
            }
        } else if (action == ROBOT_CALL_BACK) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("人机验证失败");
                return;
            }
            if (!result.crazySuccess) {
                showToast("人机验证失败");
                return;
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isClicklogin) {
                        login();
                    } else {
                        register();
                    }
                }
            }, 300);
        }
    }

    private void onBackPress() {
        SysConfig config1 = UsualMethod.getConfigFromJson(this);
        String not_login_permission = config1.getNot_login_permission();
        if (!TextUtils.isEmpty(not_login_permission) && not_login_permission.equals("on")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("是否退出程序")
                    .setPositiveButton("确定", (dialog, which) ->
                            ActivityUtils.finishAllActivities())
                    .setNegativeButton("取消", (dialog, which) ->
                            dialog.dismiss()).create().show();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onBackPress();
    }

    void pictureVertify() {
        //修改 type 0登入 1註冊 2試玩
        pictureVertifyDialog = new WebVertifyDialog(this, this, type);
        pictureVertifyDialog.show();
    }

    @Override
    public void onAccess() {
        if (type == TYPE_LOGIN) {
            actionLogin(false, "");
        } else if (type == TYPE_REGISTER) {
            actionRegister(false, "");
        }
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onMaxFailed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //回收验证码资源
        Captcha.getInstance().destroy();
        EventBus.getDefault().unregister(this);
        if (regViews != null) {
            regViews.clear();
            regViews = null;
        }
    }

    @Subscribe
    public void onEvent(VervificationSuccessEvent event) {
        YiboPreference.instance(this).setToken(event.getAccessToken());
        YiboPreference.instance(this).setLoginState(true);
        YiboPreference.instance(this).setAccountMode(Constant.ACCOUNT_PLATFORM_MEMBER);
        //记住密码
        if (YiboPreference.instance(this).isRememberPwd()) {
            YiboPreference.instance(this).savePwd(event.getPassword());
        } else {
            YiboPreference.instance(this).savePwd("");
        }

        //自动登录的情况下，要记住帐号
        //if (YiboPreference.instance(this).isAutoLogin()) {
        YiboPreference.instance(this).saveUsername(event.getUsername());
        //}
        setResult(RESULT_OK);
        EventBus.getDefault().post(new LoginEvent());
        finish();
    }

    //以下为注册=======================================================

    /**
     * 获取注册提交字段配置
     */
    private void actionGetRegConfig() {
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.REG_CONFIG_URL);
        if (isAgent) {
            urls.append("?proxy=1");
        }
        CrazyRequest<CrazyResult<RegConfigWrapper>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString()).cachePeroid(60 * 1000).shouldCache(true)
                .seqnumber(REG_CONFIG_REQUEST)
                //.listener()
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.regconfig_ongoing))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<RegConfigWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    /**
     * 提交注册
     */
    private void actionRegister(boolean nativeVertify, String validateCode) {
        StringBuilder params = new StringBuilder();
        String accountStr = account.getText().toString().trim();
        String passwordStr = registerPassword.getText().toString().trim();
        String rpasswordStr = confirm_pwd.getText().toString().trim();

        if (Utils.isEmptyString(accountStr)) {
            showToast("请输入帐号");
            return;
        }
        if (!Utils.limitAccount(accountStr)) {
            showToast(R.string.account_limit);
            return;
        }
        if (Utils.isEmptyString(passwordStr)) {
            showToast("请设置密码");
            return;
        }
//        if (!Utils.limitPwd(passwordStr)) {
//            showToast(getString(R.string.password_limit));
//            return;
//        }
        if (Utils.isEmptyString(rpasswordStr)) {
            showToast("请再次确认密码");
            return;
        }
        if (!passwordStr.equals(rpasswordStr)) {
            showToast("两次密码设置不一致");
            return;
        }

        if (passwordStr.equals(accountStr)) {
            showToast("账号和密码不能相同");
            return;
        }

        params.append("account").append("=").append(accountStr).append("&");
        params.append("password").append("=").append(passwordStr).append("&");
        params.append("rpassword").append("=").append(rpasswordStr).append("&");
        if (nativeVertify) {
            params.append("validateCode=").append(validateCode).append("&");
        }

        for (RegFieldViewNew re : regViews) {
            if (re == null) {
                continue;
            }
            String valueString = re.getValueString();
            //检查必输配置
            if (Utils.isEmptyString(valueString) && re.getIsRequire() == 2) {
                showToast("请输入" + re.getRegName());
                return;
            }
            //校验输入
            if (re.isValidate() == 2 && !valueString.matches(re.getRegex())
                    && !Utils.isEmptyString(re.getRegex())) {
                if (!Utils.isEmptyString(valueString)) {
                    showToast("请输入正确格式的" + re.getRegName());
                    return;
                }
            }
            try {
                params.append(re.getKey()).append("=").append(URLEncoder.encode(valueString, "utf-8")).append("&");
            } catch (Exception e) {
                //.........
                Utils.writeLogToFile(TAG, e.getLocalizedMessage());
            }
        }

        if (params.length() > 0 && params.toString().endsWith("&")) {
            params = params.deleteCharAt(params.length() - 1);
        }

        Utils.LOG(TAG, "the register params = " + params.toString());
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.REGISTER_URL)
                .append("?").append(params.toString().trim());
        if (isAgent) {
            urls.append("&proxy=1");
        }
        CrazyRequest<CrazyResult<RegisterResultWrapper>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(REGISTER_REQUEST)
                //.listener()
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.register_ongoing))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<RegisterResultWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    //更新注册提交字段界面
    private void updateRegisterFields(List<RegConfig> regConfigList) {
        if (regConfigList == null || regConfigList.isEmpty()) {
//            SysConfig sc = UsualMethod.getConfigFromJson(this);
////            if (sc != null) {
////                if ("on".equals(sc.getOn_off_recaptcha_verify()) && "on".equals(sc.getOn_off_recaptcha_verify_hide_code()))
////                    return;
////            }
            if (!Utils.isEmptyString(sc.getOnoff_mobile_verify_code()) && sc.getOnoff_mobile_verify_code().equalsIgnoreCase("on")) {
                //显示验证码区
                vcodeView = (RegFieldViewNew) LayoutInflater.from(this).inflate(R.layout.reg_field_layout_new, null);
                vcodeView.setRegName("验证码", 2);
                vcodeView.setRegex("");
                vcodeView.setRequire(2);
                vcodeView.setValidate(2);
                vcodeView.setShowVCode(true);
                vcodeView.setKey("verifyCode");
                infosLayout.addView(vcodeView);
                regViews.add(vcodeView);
            }
            return;
        }

        regViews.clear();
        for (RegConfig rc : regConfigList) {
            if (rc.getShowVal() == 2) {
                RegFieldViewNew regView = (RegFieldViewNew) LayoutInflater.from(this).inflate(R.layout.reg_field_layout_new, null);
                regView.setRegName(rc.getName(), rc.getRequiredVal());
                regView.setRegex(rc.getRegex());
                regView.setRequire(rc.getRequiredVal());
                regView.setValidate(rc.getValidateVal());
                regView.setKey(rc.getKey());
                regView.setShowVCode(false);
                infosLayout.addView(regView);
                regViews.add(regView);
            }
        }
        //显示验证码区
        SysConfig sc = UsualMethod.getConfigFromJson(this);
//        if (sc != null) {
//            if ("on".equals(sc.getOn_off_recaptcha_verify()) && "on".equals(sc.getOn_off_recaptcha_verify_hide_code()))
//                return;
//        }
        if (!Utils.isEmptyString(sc.getOnoff_mobile_verify_code()) && sc.getOnoff_mobile_verify_code().equalsIgnoreCase("on")) {
            vcodeView = (RegFieldViewNew) LayoutInflater.from(this).inflate(R.layout.reg_field_layout_new, null);
            vcodeView.setRegName("验证码", 2);
            vcodeView.setRegex("");
            vcodeView.setRequire(2);
            vcodeView.setValidate(2);
            vcodeView.setShowVCode(true);
            vcodeView.setKey("verifyCode");
            infosLayout.addView(vcodeView);
            regViews.add(vcodeView);
        }
    }

    private void actionSyncVcodeImage() {
        if (vcodeView == null) {
            return;
        }
        if (!vcodeView.isShowVCode()) {
            return;
        }
        vcodeView.updateImage();
    }


    private void robotWebView(){
        // 判断是否为模拟器
        if (sc.getApp_emulator_login().equalsIgnoreCase("off") && EmulatorDetectUtil.isEmulator(this)){
            showToast("模拟器不允许此操作，请联系管理员！");
            return;
        }
        HttpUtil.get(this, Urls.GET_GOOGLE_ROBOT, null, true, "正在校验中", result -> {
            if (result.isSuccess()) {
                String content = result.getContent();
                if (!"".equals(content)) {
                    Sitekey key = new Gson().fromJson(content, Sitekey.class);
                    String sitkey = key.getSiteKey();
//                    if(!key.isGoogleRobotOnOff()){
//                        if (isClicklogin) {
//                            login();
//                        } else {
//                            register();
//                        }
//                        return;
//                    }
                    if (!Utils.isEmptyString(sitkey)) {
                        showGoogleCaptcha(sitkey);
                    }else {
//                        showToast("人机验证公钥为空或未正确配置，请联系客服");
                        if (isClicklogin) {
                            login();
                        } else {
                            register();
                        }
                    }
                }else {
                    showToast("人机验证码ID获取失败！");
                }
            } else {
                showToast("发起请求失败，请检测网络!！");
            }
        });
    }

    private void showGoogleCaptcha(String siteKey){
        GoogleRecaptchaDialog dialog = new GoogleRecaptchaDialog(this, siteKey);
        dialog.setCallback(new GoogleRecaptchaDialog.GoogleCaptchaCallback() {
            @Override
            public void onError(String message) {
                runOnUiThread(() ->{ showToast(message); });
            }

            @Override
            public void onVerified(String token) {
                runOnUiThread(() -> {
                    dialog.dismiss();
                    googleRobotCallBack(token);
                });
            }
        });
        dialog.show();
        dialog.showRecaptcha();
    }

    void googleRobotCallBack(String token) {
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_GOOGLE_ROBOT_CALLBACK);
        urls.append("?token=").append(token);
        CrazyRequest<CrazyResult<LoginResultWrap>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(ROBOT_CALL_BACK)
                .headers(Urls.getHeader(this))
                .placeholderText("验证中")
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LoginResultWrap>() {
                }.getType()))
                .loadMethod(true ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }


    /**
     * 进行谷歌人机验证
     */
    void robot() {
        HttpUtil.get(this, Urls.GET_GOOGLE_ROBOT + "?type=2", null, true, "正在校验中", result -> {
            if (result.isSuccess()) {
                String content = result.getContent();
                if (!"".equals(content)) {
                    Sitekey key = new Gson().fromJson(content, Sitekey.class);
                    String sitkey = key.getSiteKey();
                    if (!key.isGoogleRobotOnOff()) {
                        if (isClicklogin) {
                            login();
                        } else {
                            register();
                        }
                        return;
                    }
                    if (!"".equals(sitkey)) {

                        SafetyNet.getClient(this).verifyWithRecaptcha(sitkey).addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                                String token = recaptchaTokenResponse.getTokenResult();
                                rebotCallBack(token);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e instanceof ApiException) {
                                    // An error occurred when communicating with the
                                    // reCAPTCHA service. Refer to the status code to
                                    // handle the error appropriately.
                                    ApiException apiException = (ApiException) e;
                                    int statusCode = apiException.getStatusCode();
                                    showToast("人机验证后台配置错误，请联系客服"+CommonStatusCodes
                                            .getStatusCodeString(statusCode));
                                } else {
                                    // A different, unknown type of error occurred.
                                    showToast("人机验证后台配置错误，请联系客服");
                                }
                            }
                        });

                    }else{
                        showToast("人机验证公钥为空或未正确配置，请联系客服");
                    }

                }
            } else {
                showToast("验证码ID获取失败！");
            }
        });


    }

    /**
     * 把人机验证结果传给后台
     *
     * @param token
     */
    void rebotCallBack(String token) {

        //新版登录
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_GOOGLE_ROBOT_CALLBACK);
        urls.append("?token=").append(token).append("&type=2");
        String toke = YiboPreference.instance(this).getToken();
//        StringBuilder urls = new StringBuilder();
//        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOGIN_URL);
//        urls.append("?username=").append(username).append("&password=").append(pwd);

        CrazyRequest<CrazyResult<LoginResultWrap>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(ROBOT_CALL_BACK)
                .headers(Urls.getHeader(this))
                .placeholderText("验证中")
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LoginResultWrap>() {
                }.getType()))
                .loadMethod(true ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
//        HttpUtil.get(this, Urls.GET_GOOGLE_ROBOT_CALLBACK + "?token=" + token + "&type=2", null, true, "获取人机验证", result -> {
//            if (result.isSuccess()) {
//                login();
//            } else {
//                showToast("人机验证失败！");
//            }
//        });


    }

    /**
     * 登陆方法
     */
    void login() {
        if (sc != null && "on".equals(sc.getOn_off_recaptcha_verify())) {
            String name = username.getText().toString().trim();
            String pwd = password.getText().toString().trim();

            if (Utils.isEmptyString(name)) {
                showToast(R.string.please_input_username);
                return;
            }

            if (Utils.isEmptyString(pwd)) {
                showToast(R.string.please_input_password);
                return;
            }
            // 判断是否启用原生验证
//            if (sc != null && "on".equalsIgnoreCase(sc.getNative_vertify_captcha_switch())) {
////                        nativeVertify();
//                //先获取验证ID
//                getCaptchaId();
//            } else {
                //网页验证
                pictureVertify();
//            }
        } else {
            actionLogin(false, "");
        }


    }

    //注册
    void register() {
        if (sc != null && "on".equals(sc.getOn_off_recaptcha_verify())) {
            StringBuilder params = new StringBuilder();

            String accountStr = account.getText().toString().trim();
            String passwordStr = registerPassword.getText().toString().trim();
            String rpasswordStr = confirm_pwd.getText().toString().trim();

            if (Utils.isEmptyString(accountStr)) {
                showToast("请输入帐号");
                return;
            }
            if (!Utils.limitAccount(accountStr)) {
                showToast(R.string.account_limit);
                return;
            }
            if (Utils.isEmptyString(passwordStr)) {
                showToast("请设置密码");
                return;
            }

            if (Utils.isEmptyString(rpasswordStr)) {
                showToast("请再次确认密码");
                return;
            }
            if (!passwordStr.equals(rpasswordStr)) {
                showToast("两次密码设置不一致");
                return;
            }

            for (RegFieldViewNew re : regViews) {
                if (re == null) {
                    continue;
                }
                String valueString = re.getValueString();
                //检查必输配置
                if (Utils.isEmptyString(valueString) && re.getIsRequire() == 2) {
                    showToast("请输入" + re.getRegName());
                    re.requestFocus();
                    return;
                }
                //校验输入
                if (re.isValidate() == 2 && !valueString.matches(re.getRegex())
                        && !Utils.isEmptyString(re.getRegex())) {
                    if (!Utils.isEmptyString(valueString)) {
                        showToast("请输入正确格式的" + re.getRegName());
                        return;
                    }
                }
                try {
                    params.append(re.getKey()).append("=").append(URLEncoder.encode(valueString, "utf-8")).append("&");
                } catch (Exception e) {
                    //.........
                }
            }
//            if (sc != null && "on".equalsIgnoreCase(sc.getNative_vertify_captcha_switch())) {
//                //先获取验证ID
//                getCaptchaId();
//            } else {
                //网页验证
                pictureVertify();
//            }
        } else {
            actionRegister(false, "");
        }
    }
}

