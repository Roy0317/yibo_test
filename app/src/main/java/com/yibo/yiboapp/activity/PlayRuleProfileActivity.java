package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.utils.Utils;


//玩法说明页
public class PlayRuleProfileActivity extends BaseActivity {

    public static final String TAG = PlayRuleProfileActivity.class.getSimpleName();
    TextView descTV;
    TextView playMethodTV;
    TextView winTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_rule_profile);
        initView();

        String detailDesc = getIntent().getStringExtra("detailDesc");
        String playMethod = getIntent().getStringExtra("playMethod");
        String windExample = getIntent().getStringExtra("windExample");

        if (!Utils.isEmptyString(detailDesc)) {
            updateTxt(descTV,detailDesc);
        }else{
            findViewById(R.id.detail_desc_name).setVisibility(View.GONE);
        }

        if (!Utils.isEmptyString(playMethod)) {
            updateTxt(playMethodTV,playMethod);
        }else{
            findViewById(R.id.play_method_name).setVisibility(View.GONE);
        }

        if (!Utils.isEmptyString(windExample)) {
            updateTxt(winTV,windExample);
        }else{
            findViewById(R.id.win_example_name).setVisibility(View.GONE);
        }

    }

    private void updateTxt(TextView tv,String notices) {
        String html = "<html><head></head><body>"+notices+"</body></html>";
        tv.setText(Html.fromHtml(html, null, null));
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.play_rule_profile));
        descTV = (TextView) findViewById(R.id.detail_desc);
        playMethodTV = (TextView) findViewById(R.id.play_method);
        winTV = (TextView) findViewById(R.id.win_example);
    }

    public static void createIntent(Context context, String winExample,
                                    String playMethod,String detailDesc) {
        Intent intent = new Intent(context, PlayRuleProfileActivity.class);
        intent.putExtra("windExample", winExample);
        intent.putExtra("playMethod", playMethod);
        intent.putExtra("detailDesc", detailDesc);
        context.startActivity(intent);
    }

}
