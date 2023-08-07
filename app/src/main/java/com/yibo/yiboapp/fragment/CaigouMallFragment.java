package com.yibo.yiboapp.fragment;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.MyCaiGouPagerAdapter;
import com.yibo.yiboapp.utils.Utils;


/**
 * A fragment representing a list of Items.
 * 底部第二个Fragment 购彩大厅页面
 * <p/>
 *
 * @author johnson
 */
public class CaigouMallFragment extends Fragment {

    public static final String TAG = CaigouMallFragment.class.getSimpleName();

    private String[] tabStr = {"全部彩种", "高频彩", "低频彩"};
    private Drawable[] drawables;

    TabLayout tabLayout;
    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.logAll(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        initData();
        initViews(view);
        return view;
    }

    private void initData() {
        Resources resources = getActivity().getResources();
        drawables = new Drawable[]{resources.getDrawable(R.drawable.icon_qbcz),
                resources.getDrawable(R.drawable.icon_gpc)
                , resources.getDrawable(R.drawable.icon_dpc)};
    }

    private void initViews(View rootView) {
        tabLayout = rootView.findViewById(R.id.tabLayout);
        mViewPager = rootView.findViewById(R.id.my_viewpager);

        PagerAdapter mPagerAdapter = new MyCaiGouPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
//        tabLayout.setupWithViewPager(mViewPager);
        initTabLayout();
    }


    private void initTabLayout() {

        for (int i = 0; i < tabStr.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            textView.setTextSize(14);
            textView.setText(tabStr[i]);
            textView.setLayoutParams(params);
            Drawable drawable = drawables[i];
            textView.setCompoundDrawablePadding(ConvertUtils.dp2px(3));//设置图片和text之间的间距
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            textView.setPadding(ConvertUtils.dp2px(3), 0, 0, 0);
            tab.setCustomView(textView);
            tabLayout.addTab(tab);
        }

    }
}
