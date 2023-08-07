package com.example.anuo.immodule.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.bean.LoginBean;
import com.example.anuo.immodule.bean.LoginChatBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.interfaces.iview.IChatLoginView;
import com.example.anuo.immodule.presenter.ChatLoginPresenter;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.ChatSpUtils;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :21/06/2019
 * Desc  :com.example.anuo.immodule.activity
 */
public class ChatLoginActivity extends ChatBaseActivity implements IChatLoginView {
    LinearLayout ll_chat_content;
    AutoCompleteTextView etUserName;
    LinearLayout cleanText;
    LinearLayout above;
    LinearLayout userRightLayout;
    AutoCompleteTextView etUserPwd;
    ImageView switchPwd;
    LinearLayout pwdRightLayout;
    LinearLayout inputArea;
    Button loginBtn;
    private ChatLoginPresenter presenter;
    private String userName;
    private String userPwd;
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private boolean CAN_RECORD_AUDIO = true;
    private boolean CAN_USE_CAMERA = true;
    public static final int SDK_PERMISSION_REQUEST = 10086;
    private String permissionInfo = "";
    private boolean isModule = true;
    private String roomId;

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected int onCreate_(Bundle savedInstanceState) {
        return R.layout.activity_chat_login;
    }

    @Override
    protected void initView() {
        super.initView();

        ll_chat_content = findViewById(R.id.ll_chat_content);
        etUserName = findViewById(R.id.user_name);
        cleanText = findViewById(R.id.clean_text);
        above = findViewById(R.id.above);
        userRightLayout = findViewById(R.id.user_right_layout);
        etUserPwd = findViewById(R.id.user_pwd);
        switchPwd = findViewById(R.id.switch_pwd);
        pwdRightLayout = findViewById(R.id.pwd_right_layout);
        inputArea = findViewById(R.id.input_area);
        loginBtn = findViewById(R.id.login_btn);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //申请权限
        permissionInfo = presenter.getPermissions();
        roomId = getIntent().getStringExtra("roomId");
//        if (ChatSpUtils.instance(this).isLogin()) {
//            gotoActivity(ChatMainActivity.class);
//            finish();
//        }
//        isModule = getIntent().getBooleanExtra("isModule", false);
//
//        ChatSpUtils.instance(this).setMainAppBaseUrl(this.getIntent().getStringExtra("baseUrl"));
//        ChatSpUtils.instance(this).setLotteryVersion(this.getIntent().getIntExtra("lotteryVersion1", 1), this.getIntent().getIntExtra("lotteryVersion2", 2));
        if (isModule) {
            ll_chat_content.setVisibility(View.GONE);
            presenter.authorization();
        }
    }

    @Override
    protected void initListener() {
        loginBtn.setOnClickListener(this);
    }

    @Override
    protected ChatBasePresenter initPresenter() {
        presenter = new ChatLoginPresenter(this, this);
        return presenter;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.login_btn) {
            userName = etUserName.getText().toString();
            userPwd = etUserPwd.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                presenter.showToast("用户名不能为空！");
                return;
            }
            if (TextUtils.isEmpty(userPwd)) {
                presenter.showToast("密码不能为空！");
                return;
            }
            presenter.login(userName, userPwd);

        }
    }

    /**
     * 用户登录app成功回调
     *
     * @param loginBean
     */
    @Override
    public void onLogin(LoginBean loginBean) {
        ChatSpUtils.instance(this).setToken(loginBean.getAccessToken());
        ChatSpUtils.instance(this).setLoginState(true);

        //记住密码
        if (ChatSpUtils.instance(this).isRememberPwd()) {
            ChatSpUtils.instance(this).savePwd(etUserPwd.getText().toString().trim());
        } else {
            ChatSpUtils.instance(this).savePwd("");
        }
        LoginBean.ContentBean content = loginBean.getContent();
        if (content != null) {
            int accountType = content.getAccountType();
            ChatSpUtils.instance(this).setAccountMode(accountType);
            //自动登录的情况下，要记住帐号
            //if (YiboPreference.instance(this).isAutoLogin()) {
            if (!TextUtils.isEmpty(content.getAccount())) {
                ChatSpUtils.instance(this).saveUsername(content.getAccount());
            } else {
                ChatSpUtils.instance(this).saveUsername(etUserName.getText().toString().trim());
            }
            //}
            presenter.authorization();
        }
    }

    /**
     * 作为Module时自动跳转至授权，如果失败的话需要退出当前页面
     */
    @Override
    public void onLoginFail() {
        if (isModule) {
            finish();
        }
    }

    private LoginChatBean loginChatBean;

    /**
     * 用户登录聊天室成功回调
     *
     * @param loginChatBean
     */
    @Override
    public void onLoginChat(LoginChatBean loginChatBean) {
        // 储存聊天信息
        this.loginChatBean = loginChatBean;
        LoginChatBean.SourceBean userTmp = loginChatBean.getSource();
        ChatSpUtils.instance(this).setUserId(userTmp.getSelfUserId());
        ChatSpUtils.instance(this).setStationId(userTmp.getStationId());
        presenter.getSysConfig(userTmp.getSelfUserId(), userTmp.getStationId(), userTmp.getUserType());
    }

    @Override
    public void onLoginSuc() {
        Intent intent = new Intent(this, ChatMainActivity.class);
        intent.putExtra(ConfigCons.LOGIN_CHAT_INFO, loginChatBean);
        intent.putExtra("roomId", roomId);
        startActivity(intent);
        finish();
    }

//    @TargetApi(23)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        try {
//            switch (requestCode) {
//                case SDK_PERMISSION_REQUEST:
//                    Map<String, Integer> perms = new HashMap<String, Integer>();
//                    // Initial
//                    for (String s : permissionInfo.split("Deny")) {
//                        perms.put(s.trim(), PackageManager.PERMISSION_GRANTED);
//                    }
//                    // Fill with results
//                    for (int i = 0; i < permissions.length; i++)
//                        perms.put(permissions[i], grantResults[i]);
//                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//                            perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        // Permission Denied
//                        CAN_WRITE_EXTERNAL_STORAGE = false;
//                        presenter.showToast("禁用文件读写权限将导致发送图片功能无法使用!");
//                    }
//
//                    if (perms.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        CAN_USE_CAMERA = false;
//                        presenter.showToast("禁用相机权限将导致拍照发送功能无法使用！");
//                    }
//                    if (perms.get(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                        CAN_RECORD_AUDIO = false;
//                        presenter.showToast("禁用录制音频权限将导致语音功能无法使用!");
//                    }
//                    break;
//                default:
//                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
