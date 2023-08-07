package com.yibo.yiboapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.yibo.yiboapp.fragment.CaigouMallAllFragment;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.adapter
 * @description: ${DESP}
 * @date: 2019/1/10
 * @time: 下午3:52
 */
public class MyCaiGouPagerAdapter extends FragmentStatePagerAdapter {

    public MyCaiGouPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CaigouMallAllFragment.newInstance(position);
    }


    @Override
    public int getCount() {
        return 3;
    }
}
