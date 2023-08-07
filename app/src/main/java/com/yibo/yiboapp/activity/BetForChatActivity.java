package com.yibo.yiboapp.activity;

import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anuo.immodule.constant.EventCons;
import com.example.anuo.immodule.event.CommonEvent;
import com.example.anuo.immodule.utils.LogUtils;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.fragment.fragment.BettingMainFragment;
import com.yibo.yiboapp.fragment.fragment.LotteryChooseFragment;
import com.yibo.yiboapp.utils.FragmentUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Author:anuo
 * Date  :30/07/2019
 * Desc  :com.yibo.yiboapp.ui.bet
 */
public class BetForChatActivity extends BaseActivity {
    FrameLayout fl_bfc_container;
    LinearLayout ll_bfc_title;
    TextView tv_bfc_current_lottery;
    TextView tv_bfc_choose_lottery;

    private LotteryChooseFragment lotteryChooseFragment;
    private BettingMainFragment bettingMainFragment;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        // Note that flag changes must happen *before* the content view is set.
        setContentView(R.layout.activity_bet_for_chat);
//        AndroidBug5497Workaround.assistActivity(this);
        EventBus.getDefault().register(this);
//        SoftHideKeyBoardUtil.assistActivity(this);

        Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        Window window = getWindow();
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值
        windowLayoutParams.width = (int) (display.getWidth() * 1.0); // 宽度设置为屏幕的1.0
        windowLayoutParams.height = (int) (display.getHeight() * 0.5); // 高度设置为屏幕的0.6
        windowLayoutParams.dimAmount = 0.0f;
        windowLayoutParams.gravity = Gravity.BOTTOM;
        initUI();
    }

    private void initUI() {
        fl_bfc_container = findViewById(R.id.fl_bfc_container);
        ll_bfc_title = findViewById(R.id.ll_bfc_title);
        tv_bfc_current_lottery = findViewById(R.id.tv_bfc_current_lottery);
        tv_bfc_choose_lottery = findViewById(R.id.tv_bfc_choose_lottery);

        initListener();
        lotteryChooseFragment = new LotteryChooseFragment();
        FragmentUtil.init(this);
        FragmentUtil.addFragment(lotteryChooseFragment, false, R.id.fl_bfc_container);
    }


    private void initListener() {
//        fl_bfc_outside.setOnClickListener(this);
        tv_bfc_choose_lottery.setOnClickListener(this);
//        if (mHomeListen == null) {
//            mHomeListen = new HomeListener(this);
//        }
//        mHomeListen.setInterface(new HomeListener.KeyFun() {
//            @Override
//            public void recent() {
//                LogUtil.e("聊天投注", "recent");
//                //moveTaskToBack(false);
//                //finish();
//                isRecent = true;
//            }
//
//            @Override
//            public void longHome() {
//                LogUtil.e("聊天投注","longHome");
//                finish();
//            }
//
//            @Override
//            public void home() {
//                LogUtil.e("聊天投注", "home");
//            }
//        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            LogUtils.e("聊天投注", "onTouchEvent ACTION_OUTSIDE");
            moveTaskToBack(true);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_bfc_choose_lottery) {
            FragmentUtil.init(this);
            FragmentUtil.removeAndShow(bettingMainFragment, lotteryChooseFragment);
            bettingMainFragment = null;
            ll_bfc_title.setVisibility(View.INVISIBLE);
            tv_bfc_choose_lottery.setEnabled(false);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            LogUtils.e(this, "onDestroy");
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        // 将返回键事件拦截
//        super.onBackPressed();
        moveTaskToBack(true);
    }


    //点击彩种选择
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CommonEvent event) {
        switch (event.getTag()) {
            case EventCons.CHOOSE_LOTTERY:
                LotteryData data = (LotteryData) event.getData();
                ll_bfc_title.setVisibility(View.VISIBLE);
                tv_bfc_choose_lottery.setEnabled(true);
                tv_bfc_current_lottery.setText(data.getName());
                FragmentUtil.init(this);

                boolean isPeilvVersion = UsualMethod.isSixMark(this,data.getCode()) || UsualMethod.isPeilvVersionMethod(this);
                YiboPreference.instance(this).setIsPeilv(isPeilvVersion);
                System.out.println("是否是赔率："+isPeilvVersion);

                if (bettingMainFragment == null){
                    bettingMainFragment = new BettingMainFragment();
                }
                //是否是赔率版本
                bettingMainFragment.setPeilv(isPeilvVersion);
                //设置彩票数据
                bettingMainFragment.setLotteryData(data);
                FragmentUtil.addShowAndHide(bettingMainFragment, lotteryChooseFragment, R.id.fl_bfc_container);
                break;
            case EventCons.FINISH_ACTIVITY:
                this.finish();
                break;
            case EventCons.MOVE_TO_BACK:
                moveTaskToBack(true);
                break;
        }
    }
}
