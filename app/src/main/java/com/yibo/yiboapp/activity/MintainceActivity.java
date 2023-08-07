package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.utils.Utils;

public class MintainceActivity extends BaseActivity {

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.maintaince_page));
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.maintaince_page);
        initView();
        String cause = getIntent().getStringExtra("cause");
        if (Utils.isEmptyString(cause)) {
            cause = "网站正在维护中。。。";
        }
        TextView causeTV = (TextView) findViewById(R.id.cause);
        causeTV.setText(cause);
    }

    @Override
    public void onClick(View v) {
//        super.onClick(v);
        if (v.getId() == R.id.back_text) {
            finish();
//            System.exit(0);
        }
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
