package com.yibo.yiboapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yibo.yiboapp.fragment.AddMemberFragment;
import com.yibo.yiboapp.fragment.RecommendationFragment;

/**
 * Author: Ray
 * created on 2018年10月13日14:39:14
 * description :我的推荐页面tabLayout适配器
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

     private RecommendationFragment recommendationFragment;

     private AddMemberFragment addMemberFragment;

    public MyPagerAdapter(FragmentManager fm,RecommendationFragment recommendationFragment,
                   AddMemberFragment addMemberFragment) {
        super(fm);
        this.recommendationFragment = recommendationFragment;
        this.addMemberFragment = addMemberFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return recommendationFragment;
            case 1:
                return addMemberFragment;
        }
        return recommendationFragment;
    }


    @Override
    public int getCount() {
        return 2;
    }
}
