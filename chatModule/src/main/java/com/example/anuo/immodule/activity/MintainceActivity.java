package com.example.anuo.immodule.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;


public class MintainceActivity extends ChatBaseActivity {

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.maintaince_page));
        String cause = getIntent().getStringExtra("cause");
        if (TextUtils.isEmpty(cause)) {
            cause = "网站正在维护中。。。";
        }
        TextView causeTV = (TextView) findViewById(R.id.cause);
        causeTV.setText(cause);
    }


    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected int onCreate_(Bundle savedInstanceState) {
        return R.layout.maintaince_page;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {

    }

    /**
     * @param ctx
     */
    public static void createIntent(Context ctx, String cause) {
        Intent intent = new Intent(ctx, MintainceActivity.class);
        intent.putExtra("cause", cause);
        ctx.startActivity(intent);
    }
}
