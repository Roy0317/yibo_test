package com.example.anuo.immodule.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.fragment.ImageViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :13/06/2019
 * Desc  :com.example.anuo.immodule.activity
 */
public class ImageViewActivity extends AppCompatActivity{
    private ArrayList<String> imageList;
    private List<Fragment> fragList;
    private ViewPager imageVp;
    private TextView currentTv;
    private TextView totalTv;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.containsKey("images")) {
                imageList = bundle.getStringArrayList("images");
            }
            if (bundle.containsKey("clickedIndex")) {
                currentPage = bundle.getInt("clickedIndex");
            }
        }
        setContentView(R.layout.activity_images_view);
        findView();
        init();
    }

    private void init() {
        totalTv.setText("/" + imageList.size());
        fragList = new ArrayList<Fragment>();
        for (int i = 0; i < imageList.size(); i++) {
            ImageViewFragment imageVF = new ImageViewFragment();
            imageVF.setImageUrl(imageList.get(i));
            fragList.add(imageVF);
        }
        // 类似缓存
        imageVp.setOffscreenPageLimit(imageList.size());
        imageVp.setAdapter(new ImageViewFPAdapter(getSupportFragmentManager()));
        imageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                currentPage = index;
                currentTv.setText((index + 1) + "");
                fragList.get(currentPage).onPause(); // 调用切换前Fargment的onPause()
                if (fragList.get(index).isAdded()) {
                    fragList.get(index).onResume(); // 调用切换后Fargment的onResume()
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        imageVp.setCurrentItem(currentPage);
        currentTv.setText((currentPage + 1) + "");
    }

    protected void findView() {
        imageVp = (ViewPager) findViewById(R.id.images_vp);
        currentTv = (TextView) findViewById(R.id.imageView_current_tv);
        totalTv = (TextView) findViewById(R.id.imageView_total_tv);
    }

    class ImageViewFPAdapter extends FragmentPagerAdapter {
        protected FragmentManager fm;

        public ImageViewFPAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragList.size();
        }
    }

    @Override
    protected void onDestroy() {
        if (fragList.size() > 0) {
            for (Fragment fragment : fragList)
                fragment.onDestroy();
        }
        super.onDestroy();
    }
}
