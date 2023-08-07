package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yibo.yiboapp.R;


public class OfficeActiveActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_active);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, OfficeActiveActivity.class);
        context.startActivity(intent);
    }
}
