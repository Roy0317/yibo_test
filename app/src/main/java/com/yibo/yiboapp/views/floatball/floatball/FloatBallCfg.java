package com.yibo.yiboapp.views.floatball.floatball;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.yibo.yiboapp.activity.MainActivity;
import com.yibo.yiboapp.entify.VIPCenterBean;
import com.yibo.yiboapp.views.FloatMenuAdapter;
import com.yibo.yiboapp.widget.CircleScaleLayoutManager;


import java.util.ArrayList;
import java.util.List;

public class FloatBallCfg {

//
//    private int[] images = {R.drawable.icon_float_recharge, R.drawable.icon_float_onlieservice,
//            R.drawable.icon_float_gamehall, R.drawable.icon_float_message, R.drawable.icon_chating_room, R.drawable.icon_float_bet,
//            R.drawable.icon_float_add};

    private String[] codes = {"cz", "zxkf", "yxdt", "zndx_item", "lts", "tz", "tjgd"};

    private String[] strings = {"充值", "在线客服", "游戏大厅", "站内短信", "聊天室", "投注", ""};

    private Context ctx;
    private FloatMenuAdapter adapterLeft;
    private FloatMenuAdapter adapterRight;

    public Drawable mIcon;
    public Drawable mIconPress;
    public boolean showClose;
    public String url;
    public int bgColor;
    public int mSize;
    public List<VIPCenterBean> beanList;
    /**
     * 标记悬浮球所处于屏幕中的位置
     *
     * @see Gravity#LEFT_TOP
     * @see Gravity#LEFT_CENTER
     * @see Gravity#LEFT_BOTTOM
     * @see Gravity#RIGHT_TOP
     * @see Gravity#RIGHT_CENTER
     * @see Gravity#RIGHT_BOTTOM
     */
    public Gravity mGravity;

    //第一次显示的y坐标偏移量，左上角是原点。

    public int mOffsetY = 0;
    public boolean mHideHalfLater = true;


    public void setmOffsetY(int mOffsetY) {
        this.mOffsetY = mOffsetY;
    }


    public FloatBallCfg(Context ctx, int size, Drawable icon, Gravity gravity) {
        this(ctx, size, icon, gravity, 0, false);
    }

    public FloatBallCfg(Context ctx, int size, Drawable icon, Drawable mIconPress, int bgColor, Gravity gravity) {
        this(ctx, size, icon, gravity, 0, false);
        this.bgColor = bgColor;
        this.mIconPress = mIconPress;
    }

    public FloatBallCfg(Context ctx, int size, Drawable icon, Gravity gravity, int offsetY, boolean showClose) {
        mSize = size;
        mIcon = icon;
        mGravity = gravity;
        mOffsetY = offsetY;
        this.showClose = showClose;
        this.ctx = ctx;
        initList();
    }

    public FloatBallCfg(MainActivity ctx, int size, String imageUrl, Gravity gravity) {
        this(ctx, size, null, gravity, 0, false);
        this.url = imageUrl;
    }


    public void initList() {
        if (beanList == null)
            beanList = new ArrayList<>();
        else
            beanList.clear();
    }


    public void setGravity(Gravity gravity) {
        mGravity = gravity;
    }

    public void setHideHalfLater(boolean hideHalfLater) {
        mHideHalfLater = hideHalfLater;
    }

    public enum Gravity {
        LEFT_TOP(android.view.Gravity.LEFT | android.view.Gravity.TOP),
        LEFT_CENTER(android.view.Gravity.LEFT | android.view.Gravity.CENTER),
        LEFT_BOTTOM(android.view.Gravity.LEFT | android.view.Gravity.BOTTOM),
        RIGHT_TOP(android.view.Gravity.RIGHT | android.view.Gravity.TOP),
        RIGHT_CENTER(android.view.Gravity.RIGHT | android.view.Gravity.CENTER),
        RIGHT_BOTTOM(android.view.Gravity.RIGHT | android.view.Gravity.BOTTOM);

        int mValue;

        Gravity(int gravity) {
            mValue = gravity;
        }

        public int getGravity() {
            return mValue;
        }
    }


//    /**
//     * 重新加载背景和浮动图片
//     */
//    public void reloadBgAndColor() {
//        mIcon = SkinResourcesUtils.getDrawable(R.drawable.icon_float);
//        mIconPress = SkinResourcesUtils.getDrawable(R.drawable.icon_float_press);
//        bgColor = SkinResourcesUtils.getColor(R.color.color_float_bg);
//    }


    public FloatMenuAdapter getAdapter(CircleScaleLayoutManager layoutManager, boolean isLeft, FloatMenuAdapter.OnItemClickListener onItemClickListener) {
        if (isLeft) {
            if (adapterLeft == null) {
                adapterLeft = new FloatMenuAdapter(ctx, beanList, true);
                adapterLeft.setOnItemClickListener(onItemClickListener);
            }
            layoutManager.setGravity(CircleScaleLayoutManager.LEFT);
            return adapterLeft;
        } else {
            if (adapterRight == null) {
                adapterRight = new FloatMenuAdapter(ctx, beanList, false);
                adapterRight.setOnItemClickListener(onItemClickListener);
            }
            layoutManager.setGravity(CircleScaleLayoutManager.RIGHT);
            return adapterRight;
        }
    }

}
