package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.utils.AESUtils;
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
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.LoginResultWrap;
import com.yibo.yiboapp.entify.RegConfig;
import com.yibo.yiboapp.entify.RegConfigWrapper;
import com.yibo.yiboapp.entify.RegisterResult;
import com.yibo.yiboapp.entify.RegisterResultWrapper;
import com.yibo.yiboapp.entify.Sitekey;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.SysConfigWraper;
import com.yibo.yiboapp.interfaces.OnVertifyResultLinsenter;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.ui.RegFieldView;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.GoogleRecaptchaDialog;
import com.yibo.yiboapp.views.WebVertifyDialog;

import org.greenrobot.eventbus.EventBus;

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
 * @author johnson
 * register page
 */
public class RegisterActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>>, OnVertifyResultLinsenter {


    public static final String TAG = RegisterActivity.class.getSimpleName();

    private boolean isAgent = false; //true:代理注册

    private String agentBtnTxt;

    XEditText account;
    XEditText password;
    XEditText confirm_pwd;
    TextView serviceOnline;
    TextView loginDirect;
    WebVertifyDialog pictureVertifyDialog;

    Button regBtn;
    LinearLayout infosLayout;
    List<RegFieldView> regViews;
    RegFieldView vcodeView;

    public static final int REG_CONFIG_REQUEST = 0x01;
    public static final int REGISTER_REQUEST = 0x02;
    public static final int GUEST_REGISTER_REQUEST = 0x03;
    public static final int SYS_CONFIG_REQUEST = 0x08;
    public static final int ROBOT_CALL_BACK = 0x09;

    private SysConfig sc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        isAgent = this.getIntent().getBooleanExtra("isAgent", false);
        sc = UsualMethod.getConfigFromJson(this);
        initView();

        regViews = new ArrayList<>();
//        Picasso.with(this).load(R.drawable.default_placeholder_picture).into(vertifyCodeImg);
        //先获取注册配置信息
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                actionGetRegConfig();
            }
        }, 500);
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void createIntent(Context context, boolean isAgent, String agentBtn) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra("isAgent", isAgent);
        intent.putExtra("agentBtnTxt", agentBtn);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        account = (XEditText) findViewById(R.id.account);
        password = (XEditText) findViewById(R.id.password);
        confirm_pwd = (XEditText) findViewById(R.id.confirm_password);
        serviceOnline = (TextView) findViewById(R.id.serve_online);
        loginDirect = (TextView) findViewById(R.id.account_exist_login);
        regBtn = (Button) findViewById(R.id.reg_btn);

        regBtn.setOnClickListener(this);
        loginDirect.setOnClickListener(this);
        serviceOnline.setOnClickListener(this);

        serviceOnline.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        serviceOnline.getPaint().setAntiAlias(true);

        loginDirect.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        loginDirect.getPaint().setAntiAlias(true);
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
                    String sysConfig = YiboPreference.instance(RegisterActivity.this).getSysConfig();
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


    }

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
        String passwordStr = password.getText().toString().trim();
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


        for (RegFieldView re : regViews) {
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


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.reg_btn:
//                robot();
                robotWebView();
                break;
            case R.id.serve_online:
                SysConfig config = UsualMethod.getConfigFromJson(this);
                String version = config.getOnline_service_open_switch();
                if (!version.isEmpty()) {
                    boolean success = UsualMethod.viewService(this, version);
                    if (!success) {
                        showToast("没有在线客服链接地址，无法打开");
                    }
                }
                break;
            case R.id.account_exist_login:
                LoginActivity.createIntent(this);
                finish();
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
                            actionRegister(true, validate);
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

    private void actionSyncVcodeImage() {
        if (vcodeView == null) {
            return;
        }
        if (!vcodeView.isShowVCode()) {
            return;
        }
        vcodeView.updateImage();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == REG_CONFIG_REQUEST) {

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
                        RegFieldView re = regViews.get(i);
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
                        YiboPreference.instance(this).savePwd(password.getText().toString().trim());
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
                if (configJson.equals(YiboPreference.instance(this).getSysConfig())){
                    // 当前保存的系统配置已经是最新的了
                    return;
                }
                YiboPreference.instance(this).saveConfig(configJson);
                YiboPreference.instance(this).saveYjfMode(stw.getContent().getYjf());
                //保存系统版本号版本号
                YiboPreference.instance(this).saveGameVersion(stw.getContent().getVersion());
            }
        }else if (action == ROBOT_CALL_BACK) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("人机验证失败");
                return;
            }
            if (!result.crazySuccess) {
                showToast("人机验证失败");
                return;
            }
            register();
        }
    }

    //更新注册提交字段界面
    private void updateRegisterFields(List<RegConfig> regConfigList) {
        if (regConfigList == null || regConfigList.isEmpty()) {
//            SysConfig sc = UsualMethod.getConfigFromJson(this);
////            if (sc != null) {
//            if ("on".equals(sc.getOn_off_recaptcha_verify()) && "on".equals(sc.getOn_off_recaptcha_verify_hide_code()))
//                return;
////            }
            if (!Utils.isEmptyString(sc.getOnoff_mobile_verify_code()) && sc.getOnoff_mobile_verify_code().equalsIgnoreCase("on")) {
                //显示验证码区
                vcodeView = (RegFieldView) LayoutInflater.from(this).inflate(R.layout.reg_field_layout, null);
                vcodeView.setRegName("验证码");
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
                RegFieldView regView = (RegFieldView) LayoutInflater.from(this).inflate(R.layout.reg_field_layout, null);
                regView.setRegex(rc.getRegex());
                regView.setRequire(rc.getRequiredVal());
                regView.setValidate(rc.getValidateVal());
                regView.setKey(rc.getKey());
                regView.setRegName(rc.getName());
                infosLayout.addView(regView);
                regViews.add(regView);
            }
        }
        //显示验证码区
        SysConfig sc = UsualMethod.getConfigFromJson(this);
//        if (sc != null) {
//        if ("on".equals(sc.getOn_off_recaptcha_verify()) && "on".equals(sc.getOn_off_recaptcha_verify_hide_code()))
//            return;
//        }
        if (!Utils.isEmptyString(sc.getOnoff_mobile_verify_code()) && sc.getOnoff_mobile_verify_code().equalsIgnoreCase("on")) {
            vcodeView = (RegFieldView) LayoutInflater.from(this).inflate(R.layout.reg_field_layout, null);
            vcodeView.setRegName("验证码");
            vcodeView.setRegex("");
            vcodeView.setRequire(2);
            vcodeView.setValidate(2);
            vcodeView.setShowVCode(true);
            vcodeView.setKey("verifyCode");
            infosLayout.addView(vcodeView);
            regViews.add(vcodeView);
        }

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
//                        register();
//                        return;
//                    }
                    if (!Utils.isEmptyString(sitkey)) {
                        showGoogleCaptcha(sitkey);
                    }else {
//                        showToast("人机验证公钥为空或未正确配置，请联系客服");
                        register();
                    }
                }else {
                    showToast("人机验证码ID获取失败！");
                }
            } else {
                showToast("发起注册失败，请检测网络！！");
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

                            register();
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

                    }else {
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


    //注册
    void register() {

        final CrazyRequest request = UsualMethod.startAsyncConfig(this, SYS_CONFIG_REQUEST);
        RequestManager.getInstance().startRequest(this, request);
        SysConfig sc = UsualMethod.getConfigFromJson(this);
        if (sc != null && "on".equals(sc.getOn_off_recaptcha_verify())) {
            StringBuilder params = new StringBuilder();


            String accountStr = account.getText().toString().trim();
            String passwordStr = password.getText().toString().trim();
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

            for (RegFieldView re : regViews) {
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












    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (regViews != null) {
            regViews.clear();
            regViews = null;
        }
        Captcha.getInstance().destroy();
    }


    void pictureVertify() {
        pictureVertifyDialog = new WebVertifyDialog(this, this, 1);
        pictureVertifyDialog.show();
    }

    @Override
    public void onAccess() {
        actionRegister(false, "");
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onMaxFailed() {

    }




}
