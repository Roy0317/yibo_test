package com.yibo.yiboapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.PickBankAccount;
import com.yibo.yiboapp.entify.PostBankWrapper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.utils.Utils;

import java.net.URLEncoder;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * @author johnson
 * 设置银行信息界面
 */
public class SettingBankActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<PostBankWrapper>> {

    XEditText username;
    XEditText bank_name;
    XEditText bank_address;
    XEditText cardno;
    XEditText pick_pwd;
    Button settingBtn;
    LinearLayout pick_pwd_layout;
    Spinner spinner;
    XEditText cardno1;
    String[] bankCards;
    TextView tvTips ;


    public static final int POST_BANK = 0x01;
    boolean postPickPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_bank);
        bankCards = getResources().getStringArray(R.array.bank_card);
        initView();
        final String bankJson = getIntent().getStringExtra("data");
        if (!Utils.isEmptyString(bankJson)) {
            PickBankAccount account = new Gson().fromJson(bankJson, PickBankAccount.class);
            if (account == null) {
                return;
            }
            username.setText(account.getUserName());
            bank_name.setText(account.getBankName());
            bank_address.setText(account.getBankAddress());
            cardno.setText(account.getCardNo());
            cardno1.setText(account.getCardNo());
            postPickPwd = Utils.isEmptyString(account.getRepPwd());
            if (postPickPwd) {
                pick_pwd_layout.setVisibility(View.VISIBLE);
            } else {
                pick_pwd_layout.setVisibility(View.GONE);
            }
        }
        if (!UsualMethod.getConfigFromJson(this).getDraw_money_user_name_modify().
                equalsIgnoreCase("on")) {
            username.setEnabled(false);
        } else {
            username.setEnabled(true);
        }

        SysConfig sysConfig = UsualMethod.getConfigFromJson(this);
        String add_bank_card_tips = sysConfig.getAdd_bank_card_tips();
        if (TextUtils.isEmpty(add_bank_card_tips)){
            tvTips.setVisibility(View.GONE);
        }else{
            tvTips.setVisibility(View.VISIBLE);
            tvTips.setText(add_bank_card_tips);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!bankCards[position].equals("其他")) {
                    bank_name.setText(bankCards[position]);
                } else {
                    bank_name.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void postBank() {

        String name = username.getText().toString().trim();
        String bank = bank_name.getText().toString().trim();
        String address = bank_address.getText().toString().trim();
        String cardNum = cardno.getText().toString().trim();
        String cardNum1 = cardno1.getText().toString().trim();
        String pickPwd = pick_pwd.getText().toString().trim();

        if (Utils.isEmptyString(bank)) {
            showToast("请提供银行名称");
            return;
        }

        if (bank.length() > 20) {
            showToast("银行名称不能超过20个字符");
            return;
        }

        if (Utils.isEmptyString(cardNum)) {
            showToast("请提供银行帐号");
            return;
        }

        if (Utils.isEmptyString(cardNum1)) {
            showToast("请再次输入银行账号");
            return;
        }

        if (!cardNum.equals(cardNum1)) {
            showToast("两次银行账号输入不一致，请重新输入");
            return;
        }

        if (Utils.isEmptyString(pickPwd) && postPickPwd) {
            showToast("请输入提款密码");
            return;
        }

        try {
            StringBuilder configUrl = new StringBuilder();
            configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.POST_BANK_DATA_URL);
            configUrl.append("?userName=").append(URLEncoder.encode(name, "utf-8"));
            configUrl.append("&bankName=").append(URLEncoder.encode(bank, "utf-8"));
            configUrl.append("&cardNo=").append(URLEncoder.encode(cardNum, "utf-8"));
            configUrl.append("&bankAddress=").append(URLEncoder.encode(address, "utf-8"));
            if (postPickPwd) {
                configUrl.append("&repPwd=").append(pickPwd);
            }
            CrazyRequest<CrazyResult<PostBankWrapper>> request = new AbstractCrazyRequest.Builder().
                    url(configUrl.toString())
                    .seqnumber(POST_BANK)
                    .headers(Urls.getHeader(this))
                    .placeholderText(getString(R.string.post_pick_moneying))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<PostBankWrapper>() {
                    }.getType()))
                    .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(this, request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.setting_btn) {
            postBank();
        }
    }

    @Override
    protected void initView() {
        super.initView();
        spinner = findViewById(R.id.spinner);
        cardno1 = findViewById(R.id.cardno1);
        tvTips = findViewById(R.id.tv_tips);

        tvMiddleTitle.setText("设置银行信息");
        username = (XEditText) findViewById(R.id.user_name);
        bank_name = (XEditText) findViewById(R.id.bank_name);
        bank_address = (XEditText) findViewById(R.id.bank_address);
        cardno = (XEditText) findViewById(R.id.cardno);
        pick_pwd = (XEditText) findViewById(R.id.pick_pwd);
        settingBtn = (Button) findViewById(R.id.setting_btn);
        settingBtn.setOnClickListener(this);
        pick_pwd_layout = (LinearLayout) findViewById(R.id.pick_pwd_layout);

    }

    public static void createIntent(Context context, String dataJson, int requestCode) {
        Intent intent = new Intent(context, SettingBankActivity.class);
        intent.putExtra("data", dataJson);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<PostBankWrapper>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == POST_BANK) {
            CrazyResult<PostBankWrapper> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.acquire_fail);
                return;
            }
            Object regResult = result.result;
            PostBankWrapper reg = (PostBankWrapper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            showToast(R.string.setting_bank_data_success);
            setResult(RESULT_OK);
            finish();
        }
    }
}
