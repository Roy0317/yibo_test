package com.yibo.yiboapp.views.gestureview;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;
import com.yibo.yiboapp.Event.FinishEvent;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.BaseActivity;
import com.yibo.yiboapp.activity.CheckUserMessageActivity;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.views.gestureview.util.PatternHelper;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * @author: soxin
 * @version: 1
 * @project: trunk
 * @package: com.yibo.yiboapp.views.gestureview
 * @description:
 * @date: 2019-10-07
 * @time: 19:32
 */
public class DefaultPatternCheckingActivity extends BaseActivity {

    private int type = 0;
    private boolean isError = false;

    //关闭手势密码
    public static final int CLOSE_GESTURE = 0;
    //修改手势密码
    public static final int UPDATE_GESTURE = 1;
    //从后台切换回来
    public static final int BACKGROUND_GESTURE = 2;

    private PatternHelper patternHelper;
    private TextView textMsg;
    private SimpleDraweeView imageHead;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_pattern_checking);
        type = getIntent().getIntExtra("type", 0);
        PatternLockerView patternLockerView = findViewById(R.id.patternLockerView);
        TextView tv_forget_password = findViewById(R.id.tv_forget_password);
        textMsg = findViewById(R.id.textMsg);
        imageHead = findViewById(R.id.header) ;
        EventBus.getDefault().register(this);

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DefaultPatternCheckingActivity.this, CheckUserMessageActivity.class));
            }
        });

        YiboPreference.instance(this).setUserCheckedGesture(true);
        patternLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView patternLockerView) {

            }

            @Override
            public void onChange(PatternLockerView patternLockerView, List<Integer> list) {

            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitIndexList) {
                isError = !isPatternOk(hitIndexList);
                view.updateStatus(isError);

                updateMsg();

                if (type == CLOSE_GESTURE) {
                    YiboPreference.instance(DefaultPatternCheckingActivity.this).setUserCheckedGesture(isError);
                }
            }

            @Override
            public void onClear(PatternLockerView patternLockerView) {
                finishIfNeeded();
            }
        });

        this.textMsg.setText("验证手势密码");
        this.patternHelper = new PatternHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UsualMethod.LoadUserImage(this, imageHead);
    }

    @Override
    public void onBackPressed() {
        if (type == CLOSE_GESTURE || type == UPDATE_GESTURE){
            super.onBackPressed();
        }
    }


    private boolean isPatternOk(List<Integer> hitIndexList) {
        this.patternHelper.validateForChecking(hitIndexList);
        return this.patternHelper.isOk();
    }

    private void updateMsg() {
        this.textMsg.setText(patternHelper.getMessage());

        if (this.patternHelper.isOk()) {
            textMsg.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        } else {
            textMsg.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
    }

    private void finishIfNeeded() {
        if (this.patternHelper.isFinish()) {
            if (type == CLOSE_GESTURE) {
                finish();
            } else if (type == UPDATE_GESTURE) {
                if (!isError) {
                    startActivity(new Intent(this, DefaultPatternSettingActivity.class));
                }
                finish();
            } else if (type == BACKGROUND_GESTURE) {
                if (isError) {
                    ToastUtils.showShort("手势验证失败，请重新登录");
                    new PatternHelper().saveToStorage("");
                    YiboPreference.instance(this).setUserGesture(false);
                    YiboPreference.instance(this).setUserCheckedGesture(false);
                    YiboPreference.instance(this).saveUsername("");
                    YiboPreference.instance(this).savePwd("");
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                finish();

            }

        }
    }

    @Subscribe
    public void onFinishEvent(FinishEvent finishEvent){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
