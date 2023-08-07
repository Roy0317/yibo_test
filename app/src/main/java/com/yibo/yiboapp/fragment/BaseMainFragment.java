package com.yibo.yiboapp.fragment;


import com.yibo.yiboapp.mvvm.BaseFragmentKotlin;

/**
 * 主页各主题风格fragment父类
 * Created by johnson on 2018/3/30.
 */

public abstract class BaseMainFragment extends BaseFragmentKotlin {


    public interface MainHeaderDelegate { void onDelegate(int eventCode);}
    protected MainHeaderDelegate delegate;

    public void bindDelegate(MainHeaderDelegate delegate) {
        this.delegate = delegate;
    }

    public abstract void setOnlineCount(String count);

    public abstract void refreshNewMainPageLoginBlock(boolean isLogin, String accountName, double balance);
}
