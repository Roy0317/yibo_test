package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.WelcomePictureAdapter;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 欢迎页面
 *
 * @author johnson
 */
public class WlecomePictureActivity extends BaseActivity implements OnClickListener, OnPageChangeListener {



    private YiboPreference preference;
    private ViewPager viewPager;
    private WelcomePictureAdapter vpAdapter;
    private List<View> views;
    private TextView button;
    private static final int[] pics = {R.drawable.welcome_one, R.drawable.welcome_two, R.drawable.
            welcome_three,R.drawable.welcome_four};


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_picture);
        preference = YiboPreference.instance(this);
        initView();
    }

    public void initView() {
        super.initView();
        views = new ArrayList<View>();
        LinearLayout.LayoutParams mParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < pics.length; i++) {
            ImageView imageView = new ImageView(this);

            RequestOptions requestOptions = new RequestOptions().fitCenter();

            Glide.with(this).load(pics[i]).apply(requestOptions).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(mParams);
            views.add(imageView);
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        vpAdapter = new WelcomePictureAdapter(views);
        viewPager.setAdapter(vpAdapter);
        viewPager.setOnPageChangeListener(this);
        button = (TextView) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                actionMain();
            }
        });

        SysConfig sysConfig = UsualMethod.getConfigFromJson(this);
        if (sysConfig != null) {
            String startText = sysConfig.getWelcome_page_startapp_text();
            if (!Utils.isEmptyString(startText)) {
                button.setText(startText);
            }else{
                button.setText(getString(R.string.startapp_now));
            }
        }

    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, WlecomePictureActivity.class);
        context.startActivity(intent);
    }

    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    @Override public void onPageScrollStateChanged(int arg0) {
    }

    @Override public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override public void onPageSelected(int arg0) {
        if (arg0 == pics.length - 1) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    @Override public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
    }

    private void actionMain() {
        preference.setHasShowPicture(true);
        MainActivity.createIntent(this,true);
        finish();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        views.clear();
    }
}
