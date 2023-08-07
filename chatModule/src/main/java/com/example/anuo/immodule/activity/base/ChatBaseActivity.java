package com.example.anuo.immodule.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.StatusBarUtil;
import com.example.anuo.immodule.utils.ToastUtils;
import com.example.anuo.immodule.view.ProgressWheel;
import com.simon.widget.RepeatedClickHandler;


import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
 * Date  :2019/6/5
 * Desc  :com.example.anuo.immodule.activity
 */
public abstract class ChatBaseActivity extends AppCompatActivity implements View.OnClickListener, IChatBaseView {
    protected LinearLayout mLayout = null;
    protected TextView tvBackText;
    protected TextView tvMiddleTitle;
    protected TextView tvRightText;
    protected TextView tvSecondRightText;
    protected TextView tvSecondTitle;
    protected LinearLayout middle_layout;
    ProgressWheel progressWheel;
    protected RelativeLayout rightLayout;
    protected RelativeLayout rightIcon;
    protected TextView iconCount;
    protected ImageView titleIndictor;
    protected LinearLayout titleLayout;
    protected ImageView ivMoreMenu;
    protected TextView ivRedTips;


    private RepeatedClickHandler repeatedClickHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CommonUtils.useTransformBar(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        onSetContentPre();
        setContentView(onCreate_(savedInstanceState));
        StatusBarUtil.immersive(this);
        initView();
        repeatedClickHandler = new RepeatedClickHandler();
        // 根据子类需要决定是否注册EventBus
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        // 初始化presenter
        initPresenter();
        //初始化监听
        initListener();
        //初始化数据
        initData(savedInstanceState);


    }

    /**
     * 在setContentView之前调用，需要在子类中重写
     */
    public void onSetContentPre() {
    }

    /**
     * 该类是否使用EventBus
     *
     * @return
     */
    protected abstract boolean useEventBus();

    protected abstract int onCreate_(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initListener();

    protected ChatBasePresenter initPresenter() {
        return null;
    }


    protected void initView() {
        mLayout = (LinearLayout) findViewById(R.id.title);
        if (mLayout != null) {
            StatusBarUtil.setPaddingSmart(this, mLayout);
            tvBackText = (TextView) mLayout.findViewById(R.id.back_text);
            middle_layout = (LinearLayout) mLayout.findViewById(R.id.middle_layout);
            tvMiddleTitle = (TextView) middle_layout.findViewById(R.id.middle_title);
            tvRightText = (TextView) mLayout.findViewById(R.id.right_text);
            tvSecondRightText = (TextView) mLayout.findViewById(R.id.second_right_text);
            tvSecondTitle = (TextView) findViewById(R.id.second_title);
            progressWheel = (ProgressWheel) mLayout.findViewById(R.id.progress_wheel);
            ivMoreMenu = (ImageView) mLayout.findViewById(R.id.iv_more_menu);
            ivRedTips =mLayout.findViewById(R.id.iv_red_tips);


            tvBackText.setOnClickListener(this);
            tvMiddleTitle.setOnClickListener(this);
            tvRightText.setOnClickListener(this);
            ivMoreMenu.setOnClickListener(this);
            tvSecondRightText.setOnClickListener(this);
            tvBackText.setVisibility(View.VISIBLE);
            tvRightText.setVisibility(View.GONE);

            rightLayout = (RelativeLayout) findViewById(R.id.right_layout);

            rightIcon = (RelativeLayout) findViewById(R.id.right_icon);
            iconCount = (TextView) rightIcon.findViewById(R.id.count);
            titleIndictor = (ImageView) findViewById(R.id.title_indictor);
            titleLayout = (LinearLayout) findViewById(R.id.clickable_title);
            rightIcon.setOnClickListener(this);
            titleIndictor.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back_text) {
            hideKeyboard(this, v);
            finish();
        }
        if (repeatedClickHandler != null) {
            repeatedClickHandler.handleRepeatedClick(v);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param view
     */
    public void hideKeyboard(Context context, View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager == null) return;
            if (inputMethodManager.isActive())
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    protected void showToast(String showText) {
        if (TextUtils.isEmpty(showText)) {
            return;
        }
        ToastUtils.showToast(this, showText);
    }

    protected void showToast(int showText) {
        ToastUtils.showToast(this, showText);
    }

    protected void startProgress() {
        if (progressWheel != null) {
            progressWheel.setVisibility(View.VISIBLE);
            progressWheel.spin();
        }
    }

    public void stopProgress() {
        if (progressWheel != null) {
            progressWheel.setVisibility(View.GONE);
            if (progressWheel.isSpinning()) {
                progressWheel.stopSpinning();
            }
        }
    }

    @Override
    public void gotoActivity(Class aClass) {
        startActivity(new Intent(this, aClass));
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res != null) {
            Configuration config = res.getConfiguration();
            if (config != null && config.fontScale != 1.0f) {
                config.fontScale = 1.0f;
                res.updateConfiguration(config, res.getDisplayMetrics());
            }
        }
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }


    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }
}
