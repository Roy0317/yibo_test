package com.yibo.yiboapp.data;

/**
 * Created by johnson on 2017/9/26.
 */

public interface UpdateListener {
    void onBottomUpdate(int zhushu, double totalMoney);
    void onCartUpdate();
    void onClearView();
}
