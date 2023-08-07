package com.yibo.yiboapp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.fragment.AddMemberFragment;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.fragment.RecommendationFragment;
import com.yibo.yiboapp.adapter.MyPagerAdapter;
import com.yibo.yiboapp.utils.Utils;

/**
 * Author: Ray
 * created on 2018年10月12日20:44:10
 * description : "我的推荐"页面
 */
public class MyRecommendationActivity extends AppCompatActivity {


    private RecommendationFragment recommendationFragment;

    private AddMemberFragment addMemberFragment;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_my_recommendation);
        initView(bundle);
    }


    /**
     * 初始化布局文件
     */
    protected void initView(Bundle bundle) {
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_return);
        if (!Utils.isEmptyString(UsualMethod.getConfigFromJson(this).getOnoff_adduser_front())
                && UsualMethod.getConfigFromJson(this).getOnoff_adduser_front().equals("on")) {
            mTabLayout.setVisibility(View.VISIBLE);
        } else {
            mTabLayout.setVisibility(View.GONE);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回时候隐藏键盘
                hideKeyboard(MyRecommendationActivity.this, v);
                finish();
            }
        });

        if (bundle == null) {
            addMemberFragment = new AddMemberFragment();
            recommendationFragment = new RecommendationFragment();
        }

        PagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), recommendationFragment, addMemberFragment);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    /**
     * 隐藏键盘
     *
     * @param context 上下文
     * @param view    点击哪个控件让键盘消失
     */
    public void hideKeyboard(Context context, View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager == null) return;
            if (inputMethodManager.isActive())
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e("隐藏系统键盘Error", e.toString());
        }

    }
}
