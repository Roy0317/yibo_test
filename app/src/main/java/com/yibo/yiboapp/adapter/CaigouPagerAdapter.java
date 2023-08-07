package com.yibo.yiboapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import java.util.List;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.adapter
 * @description: ${DESP}
 * @date: 2019/1/15
 * @time: 下午3:20
 */
public class CaigouPagerAdapter extends FragmentPagerAdapter {


    private List<Fragment> fragmentList;

    public CaigouPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


}
