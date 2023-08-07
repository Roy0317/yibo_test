package com.yibo.yiboapp.views.floatball;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.views.floatball.floatball.FloatBall;
import com.yibo.yiboapp.views.floatball.floatball.FloatBallCfg;
import com.yibo.yiboapp.views.floatball.floatball.StatusBarView;
import com.yibo.yiboapp.views.floatball.menu.FloatMenuCfg;
import com.yibo.yiboapp.views.floatball.menu.MenuItem;


import java.util.ArrayList;
import java.util.List;


public class FloatBallManager {
    public int mScreenWidth, mScreenHeight;

    private IFloatBallPermission mPermission;
    private OnFloatBallClickListener mFloatballClickListener;
    private WindowManager mWindowManager;
    private Context mContext;
    private FloatBall floatBall;
    //    private FloatMenu floatMenu;
    private FloatBallCfg ballCfg;
    private StatusBarView statusBarView;
    public int floatballX, floatballY;
    private boolean isShowing = false;
    private List<MenuItem> menuItems = new ArrayList<>();
    private Activity mActivity;
    private boolean isShowFloat = true;
    // 红包开关
    private boolean closeRedPacket = false;

    public boolean isCloseRedPacket() {
        return closeRedPacket;
    }

    public void setCloseRedPacket(boolean closeRedPacket) {
        this.closeRedPacket = closeRedPacket;
    }

    public FloatBallManager(Context application, FloatBallCfg ballCfg) {
        this(application, ballCfg, null);
    }

    public FloatBallManager(Context application, FloatBallCfg ballCfg, FloatMenuCfg menuCfg) {
        mContext = application.getApplicationContext();
        FloatBallUtil.inSingleActivity = false;
//        isShowFloat = YiboPreference.instance(mContext).isShowFloat();
        isShowFloat = "on".equals(UsualMethod.getConfigFromJson(mContext).getOnoff_chat());

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        this.ballCfg = ballCfg;
        computeScreenSize();
        floatBall = new FloatBall(mContext, this, ballCfg);
//        floatMenu = new FloatMenu(mContext, this, menuCfg);
        statusBarView = new StatusBarView(mContext, this);
    }

    public FloatBallManager(Activity activity, FloatBallCfg ballCfg) {
        this(activity, ballCfg, null);
    }

    public FloatBallManager(Activity activity, FloatBallCfg ballCfg, FloatMenuCfg menuCfg) {
        mActivity = activity;
        FloatBallUtil.inSingleActivity = true;
        mWindowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        this.ballCfg = ballCfg;
        computeScreenSize();
        floatBall = new FloatBall(mActivity, this, ballCfg);
//        floatMenu = new FloatMenu(mActivity, this, menuCfg);
        statusBarView = new StatusBarView(mActivity, this);
    }

    public void buildMenu() {
        inflateMenuItem();
    }


    /**
     * 添加一个菜单条目
     *
     * @param item
     */
    public FloatBallManager addMenuItem(MenuItem item) {
        menuItems.add(item);
        return this;
    }

    public int getMenuItemSize() {
        return menuItems != null ? menuItems.size() : 0;
    }

    /**
     * 设置菜单
     *
     * @param items
     */
    public FloatBallManager setMenu(List<MenuItem> items) {
        menuItems = items;
        return this;
    }

    private void inflateMenuItem() {
//        floatMenu.removeAllItemViews();
//        for (MenuItem item : menuItems) {
//            floatMenu.addItem(item);
//        }
    }

    public int getBallSize() {
        return floatBall.getSize();
    }

    public void computeScreenSize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point point = new Point();
            mWindowManager.getDefaultDisplay().getSize(point);
            mScreenWidth = point.x;
            mScreenHeight = point.y;
        } else {
            mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
            mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        }
    }


    /**
     * 重新加载 float窗快捷功能
     */
    public void reLoadFloatBean() {
        ballCfg.initList();
    }

    public int getStatusBarHeight() {
        return statusBarView.getStatusBarHeight();
    }

    public void onStatusBarHeightChange() {
        floatBall.onLayoutChange();
    }

    public void moveToVerticalCenter() {
        floatBall.moveToVerticalCenter();
    }


    public void show() {
        if (mActivity == null) {
            if (mPermission == null) {
                return;
            }
            if (!mPermission.hasFloatBallPermission(mContext)) {
                mPermission.onRequestFloatBallPermission();
                return;
            }
        }
        if (isShowing) return;
        if (mActivity == null && !isShowFloat) return;
        if (floatBall.getVisibility() == View.GONE) {
            floatBall.setVisibility(View.VISIBLE);
        }
        statusBarView.attachToWindow(mWindowManager);
        floatBall.attachToWindow(mWindowManager);
//        floatMenu.detachFromWindow(mWindowManager);
        isShowing = true;
    }

    public void closeMenu() {
//        floatMenu.closeMenu();
    }

    public void reset() {
        floatBall.setVisibility(View.VISIBLE);
        floatBall.postSleepRunnable();
//        floatMenu.detachFromWindow(mWindowManager);
    }


    /**
     * 点击悬浮按钮
     */
    public void onFloatBallClick() {
        if (menuItems != null && menuItems.size() > 0) {
//            floatMenu.attachToWindow(mWindowManager);
        } else {
            if (mFloatballClickListener != null) {
                mFloatballClickListener.onFloatBallClick();
            }
        }
    }



    /**
     * 点击展开的子item项
     */
    public void onChildClick(String str, int position) {
        if (mFloatballClickListener != null) {
            mFloatballClickListener.onChildClick(str, position);
        }
    }


    /**
     * 是否显示
     *
     * @param isShowFloat
     */
    public void setShowFloat(boolean isShowFloat) {
        this.isShowFloat = isShowFloat;
    }


    public void hide() {
        try {
            if (!isShowing) return;
            floatBall.detachFromWindow(mWindowManager);
//        floatMenu.detachFromWindow(mWindowManager);
            statusBarView.detachFromWindow(mWindowManager);
            isShowing = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        computeScreenSize();
        reset();
    }

    public void setPermission(IFloatBallPermission iPermission) {
        this.mPermission = iPermission;
    }

    public void setOnFloatBallClickListener(OnFloatBallClickListener listener) {
        mFloatballClickListener = listener;
    }

    public interface OnFloatBallClickListener {
        void onFloatBallClick();

        void onChildClick(String str, int postion);
    }

    public interface IFloatBallPermission {
        /**
         * request the permission of floatball,just use {@link #requestFloatBallPermission(Activity)},
         * or use your custom method.
         *
         * @return return true if requested the permission
         * @see #requestFloatBallPermission(Activity)
         */
        boolean onRequestFloatBallPermission();

        /**
         * detect whether allow  using floatball here or not.
         *
         * @return
         */
        boolean hasFloatBallPermission(Context context);

        /**
         * request floatball permission
         */
        void requestFloatBallPermission(Activity activity);
    }
}
