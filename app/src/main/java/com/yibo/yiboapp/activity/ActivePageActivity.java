package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.fragment.ActivePageContainerFragment;

/**
 * 优惠活动
 */
public class ActivePageActivity extends BaseActivity {

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, ActivePageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_page);
        initView();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.relativeParent, new ActivePageContainerFragment())
                .commit();
    }

    @Override
    protected void initView() {
        super.initView();

        tvMiddleTitle.setText("优惠活动");
        tvBackText.setVisibility(View.VISIBLE);
        tvRightText.setVisibility(View.GONE);
        tvBackText.setOnClickListener(v -> { onBackPressed();});
    }
}
