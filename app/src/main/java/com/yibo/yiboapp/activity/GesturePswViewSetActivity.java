package com.yibo.yiboapp.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.views.gestureview.DefaultPatternCheckingActivity;
import com.yibo.yiboapp.views.gestureview.DefaultPatternSettingActivity;


public class GesturePswViewSetActivity extends BaseActivity {

    public Switch swGesturePsw;
    private View flModifyPsw;
    private View flGestureLock;
    private TextView tv_time_setting;
    private boolean gestureTrue;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gesture_psw_set_view);
        initView();

    }

    @Override
    protected void initView() {
        super.initView();
        tvBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvMiddleTitle.setText("安全中心");
        swGesturePsw = findViewById(R.id.sw_gesture_psw);
        flModifyPsw = findViewById(R.id.fl_modify_psw);
        flGestureLock = findViewById(R.id.fl_gesture_lock);
        tv_time_setting = findViewById(R.id.tv_time_setting);
        LinearLayout ll_content_view = findViewById(R.id.ll_content_view);


        String gesture_pwd_switch = UsualMethod.getConfigFromJson(this).getGesture_pwd_switch();
        if (gesture_pwd_switch.equalsIgnoreCase("on")){
            ll_content_view.setVisibility(View.VISIBLE);
        }else{
            ll_content_view.setVisibility(View.GONE);
        }

        String sptimeLock = YiboPreference.instance(this).getSptimeLock();

        if (sptimeLock.equals("10000")) {
            sptimeLock = "10s";
        } else {
            sptimeLock = sptimeLock.substring(0, sptimeLock.length() - 3) + "s";
        }

        tv_time_setting.setText(sptimeLock);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //手势密码是否设置了内容
        gestureTrue = YiboPreference.instance(this).userIsSetGesture();
        swGesturePsw.setOnCheckedChangeListener(null);
        //是否开启了手势密码
        boolean b = YiboPreference.instance(this).userIsCheckedGesture();
        swGesturePsw.setChecked(b);
        if (b) {
            flModifyPsw.setVisibility(View.VISIBLE);
            flGestureLock.setVisibility(View.VISIBLE);
        } else {
            flModifyPsw.setVisibility(View.GONE);
            flGestureLock.setVisibility(View.GONE);
        }

        swGesturePsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                YiboPreference.instance(GesturePswViewSetActivity.this).setUserCheckedGesture(isChecked);
                if (isChecked) {
                    //打开要判断是否本地内存有记录
                    if (gestureTrue) {
                        //ture.将开关打开并显示下列选项
                        flModifyPsw.setVisibility(View.VISIBLE);
                        flGestureLock.setVisibility(View.VISIBLE);
                    } else {
                        //false . 先重新输入手势密码
                        //再开显示功能选项
//
                        startActivity(new Intent(GesturePswViewSetActivity.this, DefaultPatternSettingActivity.class));

                        flModifyPsw.setVisibility(View.VISIBLE);
                        flGestureLock.setVisibility(View.VISIBLE);
                    }
                } else {
                    //当要关闭时，先输入确认手势密码的正确才能关闭
                    startActivity(new Intent(GesturePswViewSetActivity.this, DefaultPatternCheckingActivity.class).putExtra("type", DefaultPatternCheckingActivity.CLOSE_GESTURE));
                    flModifyPsw.setVisibility(View.GONE);
                    flGestureLock.setVisibility(View.GONE);
                }
            }
        });


        //手势密码开关
        swGesturePsw.setOnClickListener(this);
        //修改手势密码
        flModifyPsw.setOnClickListener(this);
        //锁屏时间
        flGestureLock.setOnClickListener(this);

    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_modify_psw://修改手势密码
                Intent intent = new Intent(this, DefaultPatternCheckingActivity.class);
                intent.putExtra("type", DefaultPatternCheckingActivity.UPDATE_GESTURE);
                startActivity(intent);
                break;
            case R.id.fl_gesture_lock://时间设定
                setTime();
                break;
        }
    }

    public void setTime() {
        final String[] strTimeDialog = {"10s", "30s", "60s", "90s", "120s"};


        AlertDialog.Builder gestureDialog = new AlertDialog.Builder(this);
        gestureDialog.setTitle("时间设定");
        gestureDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        gestureDialog.setItems(strTimeDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tv_time_setting.setText(strTimeDialog[which]);

                String time = strTimeDialog[which].substring(0, strTimeDialog[which].indexOf("s")) + "000";  //将时间转化成毫秒
                YiboPreference.instance(GesturePswViewSetActivity.this).setSpTimeLock(time);
            }
        });


        gestureDialog.show();
    }
}
