package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.UpdateBankResponse;

import java.net.URLEncoder;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

public class PickMoneyUpdateBankActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>>{

    private static final int UPDATE_BANK = 1;
    private static final String USER_NAME = "user_name";

    public static Intent createIntent(Context context, String userName){
        Intent intent = new Intent(context, PickMoneyUpdateBankActivity.class);
        intent.putExtra(USER_NAME, userName);
        return intent;
    }

    private EditText editUserName;
    private EditText editOldBankCard;
    private EditText editNewBankName;
    private EditText editNetSpot;
    private EditText editNewBankCard;
    private EditText editPassword;
    private Spinner spinnerBank;
    private Button buttonNext;

    private String[] bankNames;
    private boolean isInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_money_update_bank);
        initView();
        initParam();
    }

    @Override
    protected void initView() {
        super.initView();
        editUserName = findViewById(R.id.editUserName);
        editOldBankCard = findViewById(R.id.editOldBankCard);
        editNewBankName = findViewById(R.id.editNewBankName);
        editNetSpot = findViewById(R.id.editNetSpot);
        editNewBankCard = findViewById(R.id.editNewBankCard);
        editPassword = findViewById(R.id.editPassword);
        spinnerBank = findViewById(R.id.spinnerBank);
        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
    }

    private void initParam(){
        tvMiddleTitle.setText("绑定新的银行卡");
        String userName = getIntent().getStringExtra(USER_NAME);
        if(!TextUtils.isEmpty(userName)){
            editUserName.setText(userName);
        }
        bankNames = getResources().getStringArray(R.array.bank_card);
        spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if("其他".equals(bankNames[position])){
                    editNewBankName.setText("");
                    editNewBankName.requestFocus();
                }else {
                    editNewBankName.setText(bankNames[position]);
                    if(isInit) {
                        editNetSpot.requestFocus();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        new Handler().postDelayed(() -> { isInit = true; }, 1000);
    }

    private boolean checkAllInput(){
        if(TextUtils.isEmpty(editOldBankCard.getText().toString())){
            showToast("旧银行卡号为必填栏位");
            return false;
        }else if(TextUtils.isEmpty(editNewBankName.getText().toString())){
            showToast("新提款银行为必填栏位");
            return false;
        }else if(TextUtils.isEmpty(editNewBankCard.getText().toString())){
            showToast("新银行卡号为必填栏位");
            return false;
        }else if(TextUtils.isEmpty(editPassword.getText().toString())){
            showToast("提款密码为必填栏位");
            return false;
        }

        return true;
    }

    private void updateBankCard(String userName, String oldBank, String bankName,
                                String bankAddress, String cardNo, String repPwd){
        try {
            StringBuilder configUrl = new StringBuilder();
            configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.UPDATE_BANK_DATA_URL);
            configUrl.append("?userName=").append(URLEncoder.encode(userName, "utf-8"));
            configUrl.append("&oldBank=").append(URLEncoder.encode(oldBank, "utf-8"));
            configUrl.append("&bankName=").append(URLEncoder.encode(bankName, "utf-8"));
            if(!TextUtils.isEmpty(bankAddress)){
                configUrl.append("&bankAddress=").append(URLEncoder.encode(bankAddress, "utf-8"));
            }
            configUrl.append("&cardNo=").append(URLEncoder.encode(cardNo, "utf-8"));
            configUrl.append("&repPwd=").append(URLEncoder.encode(repPwd, "utf-8"));

            CrazyRequest<CrazyResult<UpdateBankResponse>> request = new AbstractCrazyRequest.Builder()
                    .url(configUrl.toString())
                    .seqnumber(UPDATE_BANK)
                    .headers(Urls.getHeader(this))
                    .placeholderText(getString(R.string.post_pick_moneying))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<UpdateBankResponse>() {}.getType()))
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
        if(v.getId() == R.id.buttonNext){
            if(checkAllInput()){
                String userName = editUserName.getText().toString();
                String oldBank = editOldBankCard.getText().toString();
                String bankName = editNewBankName.getText().toString();
                String bankAddress = editNetSpot.getText().toString();
                String cardNo = editNewBankCard.getText().toString();
                String repPwd = editPassword.getText().toString();
                updateBankCard(userName, oldBank, bankName, bankAddress, cardNo, repPwd);
            }
        }
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }

        int action = response.action;
        if (action == UPDATE_BANK) {
            CrazyResult<Object> result = response.result;
            if (result == null || !result.crazySuccess) {
                showToast(R.string.update_bank_card_failure);
                return;
            }

            Object regResult = result.result;
            UpdateBankResponse reg = (UpdateBankResponse) regResult;
            if (!reg.isSuccess()) {
                if(TextUtils.isEmpty(reg.getMsg())){
                    showToast(R.string.update_bank_card_failure);
                }else {
                    showToast(reg.getMsg());
                }
            }else {
                showToast(R.string.update_bank_card_success);
                new Handler().postDelayed(() -> {
                    setResult(RESULT_OK);
                    finish();
                }, 800);
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
        }
    }
}