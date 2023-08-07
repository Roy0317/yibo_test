package com.yibo.yiboapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.simon.widget.ToastUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpUtil;

public class RechargeCouponActivity extends BaseActivity {


    EditText etCardNumber;
    EditText etPassword;
    ImageView ivOnOff;
    TextView btnConfirm;
    private boolean isShowPassword = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_recharge_coupon);
        initView();
        initListener();
    }


    @Override
    protected void initView() {
        super.initView();

        etCardNumber = findViewById(R.id.et_card_number);
        etPassword = findViewById(R.id.et_password);
        ivOnOff = findViewById(R.id.iv_on_off);
        btnConfirm = findViewById(R.id.btnConfirm);

        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("使用记录");
        tvRightText.setOnClickListener(v -> {
            startActivity(new Intent(this, RechargeCardRecordActivity.class));
        });
        tvMiddleTitle.setText("充值卡中心");
    }

    protected void initListener() {

        // 确认充值
        btnConfirm.setOnClickListener(v -> {
            String s = etCardNumber.getText().toString();
            String s1 = etPassword.getText().toString();
            if (TextUtils.isEmpty(s)) {
                ToastUtil.showToast(this, "卡号不能为空");
            } else if (TextUtils.isEmpty(s1)) {
                ToastUtil.showToast(this, "密码不能为空");
            } else {
                //调用确认
                useRechargeCard(s, s1);
            }
        });

        //隐藏显示密码
        ivOnOff.setOnClickListener(v -> {
            String s1 = etPassword.getText().toString();
            if (!isShowPassword) {
                ivOnOff.setImageResource(R.drawable.icon4);
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ivOnOff.setImageResource(R.drawable.icon3);
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            etPassword.setSelection(s1.length());
            isShowPassword = !isShowPassword;
        });

        RxTextView.textChanges(etPassword).subscribe(charSequence -> {
            if (TextUtils.isEmpty(charSequence.toString())) {
                ivOnOff.setVisibility(View.GONE);
            } else {
                ivOnOff.setVisibility(View.VISIBLE);
            }
        }).isDisposed();


    }

    private void useRechargeCard(String s, String s1) {
        ApiParams params = new ApiParams();
        params.put("card", s);
        params.put("password", s1);
        HttpUtil.get(this, Urls.GET_RECHARGE_CARD, params, true, result -> {
            if (result.isSuccess()) {
                ToastUtil.showToast(this, TextUtils.isEmpty(result.getMsg()) ? "提交成功" : result.getMsg());
                clearText();
            } else {
                ToastUtil.showToast(this, TextUtils.isEmpty(result.getMsg()) ? "提交失败" : result.getMsg());
            }
        });
    }

    private void clearText() {
        etPassword.setText("");
        etCardNumber.setText("");
    }
}
