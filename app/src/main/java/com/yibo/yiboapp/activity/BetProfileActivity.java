package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yibo.yiboapp.R;


public class BetProfileActivity extends BaseActivity {

    public static final String TAG = BetProfileActivity.class.getSimpleName();
    TextView descTV;
    TextView playMethodTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_profile);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.play_rule_profile));
        descTV = (TextView) findViewById(R.id.detail_desc);
        playMethodTV = (TextView) findViewById(R.id.play_method);
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, BetProfileActivity.class);
        context.startActivity(intent);
    }
}
