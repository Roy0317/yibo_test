package com.yibo.yiboapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Author: Ray
 * created on 2018年10月13日14:44:31
 * description : 我的推荐下面 通用的Fragment
 */
public abstract class RecommendationBaseFragment extends Fragment {
    /**
     * Fragment是否已经绑定
     */
    protected boolean isViewInitiated;
    /**
     * 用户是否可见
     */
    protected boolean isVisibleToUser;
    /**
     * 是否绑定数据
     */
    protected boolean isDataInitiated;

    /**
     * 碎片附属的activity
     */
    protected Activity activity;

    /**
     * 跟布局
     */
    protected View view;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        activity = getActivity();
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initView(inflater);
        initWidget();
        return view;
    }

    /**
     * 绑定控件
     */
    public abstract void initWidget();

    /**
     * 绑定布局
     *
     * @param inflater
     * @return
     */
    public abstract View initView(LayoutInflater inflater);

    /**
     * 是时候绑定数据了
     */
    public abstract void fetchData();

    /**
     * 是时候准备数据了
     */
    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }


    public void showToast(String content) {
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
    }


    protected <T extends View> T find(int id) {
        return (T) view.findViewById(id);
    }


}
