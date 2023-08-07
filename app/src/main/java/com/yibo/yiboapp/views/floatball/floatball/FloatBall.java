package com.yibo.yiboapp.views.floatball.floatball;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.views.FloatMenuAdapter;
import com.yibo.yiboapp.views.floatball.FloatBallManager;
import com.yibo.yiboapp.views.floatball.FloatBallUtil;
import com.yibo.yiboapp.views.floatball.MotionVelocityUtil;
import com.yibo.yiboapp.views.floatball.runner.ICarrier;
import com.yibo.yiboapp.views.floatball.runner.OnceRunnable;
import com.yibo.yiboapp.views.floatball.runner.ScrollRunner;
import com.yibo.yiboapp.widget.CircleScaleLayoutManager;
import com.yibo.yiboapp.widget.ScrollHelper;


public class FloatBall extends FrameLayout implements ICarrier {

    private Context context;
    private FloatBallManager floatBallManager;
    private ImageView imageView;
    private ImageView closeView;
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager windowManager;
    private boolean isFirst = true;
    private boolean isAdded = false;
    private int mTouchSlop;
    /**
     * flag a touch is click event
     */
    private boolean isClick;
    private int mDownX, mDownY, mLastX, mLastY;
    private int mSize;
    private ScrollRunner mRunner;
    private int mVelocityX, mVelocityY;
    private MotionVelocityUtil mVelocity;
    private boolean sleep = false;
    private FloatBallCfg mConfig;
    private boolean mHideHalfLater = true;
    private boolean mLayoutChanged = false;
    private int mSleepX = -1;
    private OnceRunnable mSleepRunnable = new OnceRunnable() {
        @Override
        public void onRun() {
            if (mHideHalfLater && !sleep && isAdded) {
                sleep = true;
                moveToEdge(false, sleep);
                mSleepX = mLayoutParams.x;
            }
        }
    };

    /**
     * Recycle flag a touch is click event
     */
    private RecyclerView recyclerView;
    private CircleScaleLayoutManager layoutManager;
    private int mRecycleDownX, mRecycleDownY;
    private boolean isRecyclerViewClick; //点击RecyclerView
    private boolean isVisiblit = false;


    public FloatBall(Context context, FloatBallManager floatBallManager, FloatBallCfg config) {
        super(context);
        this.floatBallManager = floatBallManager;
        mConfig = config;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        imageView = new ImageView(context);
        closeView = new ImageView(context);
        closeView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_close_blue));
        closeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                floatBallManager.setCloseRedPacket(true);
                floatBallManager.hide();
            }
        });
        final Drawable icon = mConfig.mIcon;
        mSize = mConfig.mSize;
        if (icon==null){
            if (!TextUtils.isEmpty(mConfig.url)){

                RequestOptions options = new RequestOptions().error(R.drawable.icon_home_page_activit_icon);

                Glide.with(context).load(Urls.BASE_URL + Urls.PORT +mConfig.url)
                        .apply(options)
                        .into(imageView);
            }else{
                Glide.with(context).load(R.drawable.icon_home_page_activit_icon).into(imageView);
            }
        }else{
            imageView.setImageDrawable(icon);
        }
        addView(imageView, new ViewGroup.LayoutParams(mSize, mSize));
        addView(closeView,new ViewGroup.LayoutParams(ConvertUtils.dp2px(20),ConvertUtils.dp2px(20)));
        if (mConfig.showClose){
            closeView.setVisibility(VISIBLE);
        }else {
            closeView.setVisibility(GONE);
        }
        initLayoutParams(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mRunner = new ScrollRunner(this);
        mVelocity = new MotionVelocityUtil(context);
        if (!(context instanceof Activity))
            initRecycler(context);
    }


    private void initLayoutParams(Context context) {
        mLayoutParams = FloatBallUtil.getLayoutParams(context);
    }


    private void initRecycler(Context context) {
        recyclerView = new RecyclerView(context);
        recyclerView.setBackgroundColor(mConfig.bgColor);
        recyclerView.setVisibility(GONE);
        layoutManager = new CircleScaleLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mConfig.getAdapter(layoutManager, isLeft(), new FloatMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                clickAction(v, pos);
            }
        }));


        recyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    int action = motionEvent.getAction();
                    int x = (int) motionEvent.getRawX();
                    int y = (int) motionEvent.getRawY();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            mRecycleDownX = x;
                            mRecycleDownY = y;
                            isRecyclerViewClick = true;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int totalDeltaX = x - mRecycleDownX;
                            int totalDeltaY = y - mRecycleDownY;
                            if ((Math.abs(totalDeltaX) > mTouchSlop || Math.abs(totalDeltaY) > mTouchSlop)) {
                                isRecyclerViewClick = false;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (isRecyclerViewClick) {
                                goneRecyclerView();
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;

            }
        });

    }


    /**
     * 点击item执行
     *
     * @param v
     * @param pos
     */
    private void clickAction(View v, int pos) {
        ScrollHelper.smoothScrollToTargetView(recyclerView, v);
        String code = "";
        try {
            code = mConfig.beanList.get(pos).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        floatBallManager.onChildClick(code, pos);
        goneRecyclerView();
    }


    /**
     * 主题更改，需要更换浮动按钮图片和滑动背景
     */
    public void refreshByTheme() {
        imageView.setImageDrawable(mConfig.mIcon);
        recyclerView.setBackgroundColor(mConfig.bgColor);
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            onConfigurationChanged(null);
        }
    }

    public void attachToWindow(WindowManager windowManager) {
        this.windowManager = windowManager;
        if (!isAdded) {
            if (!(context instanceof Activity)) {
                WindowManager.LayoutParams params = FloatBallUtil.getLayoutParams(context);
                params.height = floatBallManager.mScreenHeight;
                params.width = floatBallManager.mScreenWidth;
                windowManager.addView(recyclerView, params);
            }
            windowManager.addView(this, mLayoutParams);
            isAdded = true;
        }
    }

    public void detachFromWindow(WindowManager windowManager) {
        this.windowManager = null;
        if (isAdded) {
            removeSleepRunnable();
            if (getContext() instanceof Activity) {
                windowManager.removeViewImmediate(this);
            } else {
                isVisiblit = false;
                recyclerView.setVisibility(GONE);
                windowManager.removeView(recyclerView);
                windowManager.removeView(this);
            }
            isAdded = false;
            sleep = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        int curX = mLayoutParams.x;
        if (sleep && curX != mSleepX && !mRunner.isRunning()) {
            sleep = false;
            postSleepRunnable();
        }
        if (mRunner.isRunning()) {
            mLayoutChanged = false;
        }
        if (height != 0 && isFirst || mLayoutChanged) {
            if (isFirst && height != 0) {
                location(width, height);
            } else {
                moveToEdge(false, sleep);
            }
            isFirst = false;
            mLayoutChanged = false;
        }
    }

    private void location(int width, int height) {
        FloatBallCfg.Gravity cfgGravity = mConfig.mGravity;
        mHideHalfLater = mConfig.mHideHalfLater;
        int gravity = cfgGravity.getGravity();
        int x;
        int y;
        int topLimit = 0;
        int bottomLimit = floatBallManager.mScreenHeight - height;
        int statusBarHeight = floatBallManager.getStatusBarHeight();
        if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            x = 0;
        } else {
            x = floatBallManager.mScreenWidth - width;
        }
        if ((gravity & Gravity.TOP) == Gravity.TOP) {
            y = topLimit;
        } else if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            y = floatBallManager.mScreenHeight - height - statusBarHeight;
        } else {
            y = floatBallManager.mScreenHeight / 2 - height / 2 - statusBarHeight;
        }
        y = mConfig.mOffsetY != 0 ? y + mConfig.mOffsetY : y;
        if (y < 0) y = topLimit;
        if (y > bottomLimit)
            y = topLimit;
        onLocation(x, y);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mLayoutChanged = true;
        floatBallManager.onConfigurationChanged(newConfig);
        moveToEdge(false, false);
        postSleepRunnable();
    }

    public void onLayoutChange() {
        mLayoutChanged = true;
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        mVelocity.acquireVelocityTracker(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                if (context instanceof Activity)
//                    imageView.setImageResource(R.drawable.icon_chat_room_move);
//                else
//                    imageView.setImageDrawable(mConfig.mIconPress);

                touchDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                if (context instanceof Activity)
//                    imageView.setImageResource(R.drawable.icon_chat_room_mormal);
//                else
//                    imageView.setImageDrawable(mConfig.mIcon);
                touchUp();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void touchDown(int x, int y) {
        mDownX = x;
        mDownY = y;
        mLastX = mDownX;
        mLastY = mDownY;
        isClick = true;
        removeSleepRunnable();
    }

    private void touchMove(int x, int y) {
        int totalDeltaX = x - mDownX;
        int totalDeltaY = y - mDownY;
        int deltaX = x - mLastX;
        int deltaY = y - mLastY;

        if ((Math.abs(totalDeltaX) > mTouchSlop || Math.abs(totalDeltaY) > mTouchSlop) && !isVisiblit) {
            isClick = false;
        }
        mLastX = x;
        mLastY = y;
        if (!isClick) {
            onMove(deltaX, deltaY);
        }
    }

    private void touchUp() {
        mVelocity.computeCurrentVelocity();
        mVelocityX = (int) mVelocity.getXVelocity();
        mVelocityY = (int) mVelocity.getYVelocity();
        mVelocity.releaseVelocityTracker();
        if (sleep) {
            wakeUp();
        } else {
            if (isClick) {
                onClick();
            } else {
                moveToEdge(true, false);
            }
        }
        mVelocityX = 0;
        mVelocityY = 0;
    }

    public void moveToVerticalCenter() {
        int screenheight = floatBallManager.mScreenHeight;
        int height = getHeight();
        int halfHeight = height / 2;
        int centerY = (screenheight / 2 - halfHeight);
        moveTo(mLayoutParams.x, centerY);
    }


    private void moveToX(boolean smooth, int destX) {
        int statusBarHeight = floatBallManager.getStatusBarHeight();
        final int screenHeight = floatBallManager.mScreenHeight - statusBarHeight;
        int height = getHeight();
        int destY = 0;
        if (mLayoutParams.y < 0) {
            destY = 0 - mLayoutParams.y;
        } else if (mLayoutParams.y > screenHeight - height) {
            destY = screenHeight - height - mLayoutParams.y;
        }
        if (smooth) {
            int dx = destX - mLayoutParams.x;
            int duration = getScrollDuration(Math.abs(dx));
            mRunner.start(dx, destY, duration);
        } else {
            onMove(destX - mLayoutParams.x, destY);
            postSleepRunnable();
        }
    }

    private void wakeUp() {
        final int screenWidth = floatBallManager.mScreenWidth;
        int width = getWidth();
        int halfWidth = width / 2;
        int centerX = (screenWidth / 2 - halfWidth);
        int destX;
        destX = mLayoutParams.x < centerX ? 0 : screenWidth - width;
        sleep = false;
        moveToX(true, destX);
    }

    private void moveToEdge(boolean smooth, boolean forceSleep) {
        //屏幕宽度
        final int screenWidth = floatBallManager.mScreenWidth;
        //控件宽度
        int width = getWidth();
        int halfWidth = width / 2;
        //中心点x坐标
        int centerX = (screenWidth / 2 - halfWidth);
        int destX;
        final int minVelocity = mVelocity.getMinVelocity();
        //判断当前点在屏幕左边还是右边
        if (mLayoutParams.x < centerX) {
            sleep = forceSleep ? true : Math.abs(mVelocityX) > minVelocity && mVelocityX < 0 || mLayoutParams.x < 0;
            sleep = false;
            destX = sleep ? -halfWidth : 0;
        } else {
            sleep = forceSleep ? true : Math.abs(mVelocityX) > minVelocity && mVelocityX > 0 || mLayoutParams.x > screenWidth - width;
            sleep = false;
            destX = sleep ? screenWidth - halfWidth : screenWidth - width;
        }
        if (sleep) {
            mSleepX = destX;
        }
        moveToX(smooth, destX);
    }

    private int getScrollDuration(int distance) {
        return (int) (250 * (1.0f * distance / 800));
    }

    private void onMove(int deltaX, int deltaY) {
        mLayoutParams.x += deltaX;
        mLayoutParams.y += deltaY;
        if (windowManager != null) {
            windowManager.updateViewLayout(this, mLayoutParams);
        }
    }

    public void onLocation(int x, int y) {
        mLayoutParams.x = x;
        mLayoutParams.y = y;
        if (windowManager != null) {
            windowManager.updateViewLayout(this, mLayoutParams);
        }
    }

    public void onMove(int lastX, int lastY, int curX, int curY) {
        onMove(curX - lastX, curY - lastY);
    }

    @Override
    public void onDone() {
        postSleepRunnable();
    }

    private void moveTo(int x, int y) {
        mLayoutParams.x += x - mLayoutParams.x;
        mLayoutParams.y += y - mLayoutParams.y;
        if (windowManager != null) {
            windowManager.updateViewLayout(this, mLayoutParams);
        }
    }

    public int getSize() {
        return mSize;
    }

    private void onClick() {
        floatBallManager.floatballX = mLayoutParams.x;
        floatBallManager.floatballY = mLayoutParams.y;
        floatBallManager.onFloatBallClick();

        if (!(context instanceof Activity)) {
            if (!isVisiblit) {
                isVisiblit = true;
                recyclerView.setVisibility(VISIBLE);
                recyclerView.setAdapter(mConfig.getAdapter(layoutManager, isLeft(), new FloatMenuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        clickAction(v, pos);
                    }
                }));
//                AnimateUtil.runLayoutAnimation(recyclerView);
                recyclerView.smoothScrollToPosition(2);
            } else {
                goneRecyclerView();
            }
        }
    }


    /**
     * 当前悬浮点是否在屏幕左边
     *
     * @return
     */
    private boolean isLeft() {
        final int screenWidth = floatBallManager.mScreenWidth;
        //控件宽度
        int width = getWidth();
        int halfWidth = width / 2;
        //中心点x坐标
        int centerX = (screenWidth / 2 - halfWidth);
        //判断当前点在屏幕左边还是右边
        if (mLayoutParams.x < centerX) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 隐藏RecycleView
     */
    public void goneRecyclerView() {
        recyclerView.setVisibility(GONE);
        isVisiblit = false;
    }


    private void removeSleepRunnable() {
        mSleepRunnable.removeSelf(this);
    }

    public void postSleepRunnable() {
        if (mHideHalfLater && !sleep && isAdded) {
            mSleepRunnable.postDelaySelf(this, 3000);
        }
    }
}
