package com.yibo.yiboapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.yibo.yiboapp.Event.FinishEvent;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.views.gestureview.DefaultPatternSettingActivity;
import com.yibo.yiboapp.views.gestureview.util.PatternHelper;

import org.greenrobot.eventbus.EventBus;


/**
 * @author: soxin
 * @version: 1
 * @project: game
 * @package: com.yibo.yiboapp.ui
 * @description:
 * @date: 2019-10-01
 * @time: 19:57
 */
public class CheckUserMessageActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_check_user_messages);
        initView();
    }


    @Override
    protected void initView() {
        super.initView();
        final YiboPreference instance = YiboPreference.instance(this);
        final EditText userEt = findViewById(R.id.user_name);
        final EditText passEt = findViewById(R.id.user_pwd);
        tvMiddleTitle.setText("确认信息");

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userEt.getText().toString().trim();
                String password = passEt.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    ToastUtils.showShort("请输入用户名");
                } else if (TextUtils.isEmpty(password)) {
                    ToastUtils.showShort("请输入用户密码");
                } else if (userName.equals(instance.getUsername()) &&
                        password.equals(instance.getPwd())) {
                    ToastUtils.showShort("验证成功,密码已清空,请重新设置手势密码");
                    startActivity(new Intent(CheckUserMessageActivity.this, DefaultPatternSettingActivity.class));
                    //清空手势密码
                    new PatternHelper().saveToStorage("");
                    YiboPreference.instance(CheckUserMessageActivity.this).setUserGesture(false);
                    EventBus.getDefault().post(new FinishEvent());
                    finish();
                } else {
                    ToastUtils.showShort("信息验证失败");
                }
            }
        });


    }
}
