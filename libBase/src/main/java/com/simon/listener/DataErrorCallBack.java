package com.simon.listener;

/**
 * 获取网络数据失败调用接口
 *
 * @author simon
 */
public interface DataErrorCallBack {

    /**
     * 在一次获取数据
     */
    void onRetry();

}
