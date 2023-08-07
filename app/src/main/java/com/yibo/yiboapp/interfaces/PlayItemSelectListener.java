package com.yibo.yiboapp.interfaces;

import android.view.View;

import com.yibo.yiboapp.entify.BallonItem;

/**
 * Author: Ray
 * created on 2018/12/7
 * description ://彩票球列表项中 功能view,真正的号码球选择事件
 */

public interface PlayItemSelectListener {
    void onBallonClick(BallonItem item);

    void onViewClick(View view);
}
