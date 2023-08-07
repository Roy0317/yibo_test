package com.yibo.yiboapp.utils;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.simon.base.BaseFragment;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class PageFragmentManager {

    private final FragmentActivity mActivity;
    private final int mContainerId;
    private BaseFragment nowFragment; //当前显示的fragment
    private final LinkedHashMap<String, TabInfo> mTabs = new LinkedHashMap<>();

    static final class TabInfo {
        private final String tag;// tab卡标签
        private final Class<?> clss;// fragment.class
        private final Bundle args;// tab卡其他数据
        private BaseFragment fragment;// fragment

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    public PageFragmentManager(FragmentActivity activity, int containerId) {
        mActivity = activity;// 上下文
        mContainerId = containerId;// tab内容布局id
    }


    /**
     * 添加tab选项卡
     *
     * @param id   tab卡标签名字
     * @param clss 要添加的fragment
     * @param args 其他信息
     */
    public void addTab(int id, Class<?> clss, Bundle args) {
        String tag = String.valueOf(id);
        TabInfo info = new TabInfo(tag, clss, args);
        info.fragment = (BaseFragment) mActivity.getSupportFragmentManager().findFragmentByTag(tag);
        mTabs.put(tag, info);
    }

    /**
     * 隐藏所有的tab,但是id显示
     *
     * @param id
     */
    public void setCurrentTab(int id) {
        try {
            if (mTabs != null && mTabs.size() > 0) {
                Iterator<String> it = mTabs.keySet().iterator();
                while (it.hasNext()) {
                    TabInfo tabInfo = mTabs.get(it.next());
                    if (tabInfo != null &&
                            !tabInfo.tag.equals(String.valueOf(id)) && tabInfo.fragment != null) {
                        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                        tabInfo.fragment.onPause();
                        ft.hide(tabInfo.fragment);
                        ft.commitAllowingStateLoss();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ShowTab(id);
    }

    /**
     * 设置当前tab卡标签及内容
     *
     * @param id 当前tab标签视图
     */
    private void ShowTab(int id) {
        if (mActivity.isFinishing())
            return;
        // 安装fragment
        TabInfo newTab = mTabs.get(String.valueOf(id));
        if (newTab != null) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            if (newTab.fragment == null) {
                newTab.fragment = (BaseFragment) Fragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
                ft.add(mContainerId, newTab.fragment, newTab.tag);// 第一次添加到窗口
            } else {
                ft.show(newTab.fragment);
            }
            nowFragment = newTab.fragment;
//            newTab.fragment.Resume();
            ft.commitAllowingStateLoss();
        }
    }

    public Fragment getFragment(int id) {
        String tag = String.valueOf(id);
        TabInfo newTab = mTabs.get(tag);
        return newTab.fragment;
    }


    /**
     * 当前显示的fragment
     *
     * @return
     */
    public BaseFragment getNowFragment() {
        return nowFragment;
    }

    /**
     * 获取fragmen添加的容器的数量
     *
     * @return
     */
    public int getAddedNum() {
        int addNum = 0;
        try {
            if (mTabs != null && mTabs.size() > 0) {
                Iterator<String> it = mTabs.keySet().iterator();
                while (it.hasNext()) {
                    TabInfo tabInfo = mTabs.get(it.next());
                    if (tabInfo != null && tabInfo.fragment != null) {
                        if (tabInfo.fragment.isAdded()) addNum++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addNum;
    }
}
