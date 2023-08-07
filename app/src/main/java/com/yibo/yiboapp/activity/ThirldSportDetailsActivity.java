package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.ThirldSpordData;
import com.yibo.yiboapp.utils.Utils;

import java.io.Serializable;

/**
 * 代金券页面
 * craate
 */
public class ThirldSportDetailsActivity extends BaseActivity {
    TextView tv_bettingmoney;
    TextView time;
    TextView tv_content;
    TextView tv_winmoney;
    TextView tv_yingkui;
    TextView tv_tittle;
    TextView tv_user_name;
    private ThirldSpordData.RowsBean rowsBean;

    public static void createIntent(Context context, ThirldSpordData.RowsBean rowsBean) {
        Intent intent = new Intent(context, ThirldSportDetailsActivity.class);
        intent.putExtra("rowsBean", (Serializable) rowsBean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_thirld_detail);
        super.initView();
        tvMiddleTitle.setText("第三方体育投注记录详情");
        rowsBean = (ThirldSpordData.RowsBean) getIntent().getSerializableExtra("rowsBean");
        initView();
    }

    protected void initView() {
        tv_bettingmoney = findViewById(R.id.tv_bettingmoney);
        time = findViewById(R.id.time);
        tv_content = findViewById(R.id.tv_content);
        tv_winmoney = findViewById(R.id.tv_winmoney);
        tv_yingkui = findViewById(R.id.tv_yingkui);
        tv_tittle = findViewById(R.id.tv_title);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_bettingmoney.setText(rowsBean.getBettingMoney() + "");
        time.setText(Utils.formatTime(rowsBean.getBettingTime()));
        tv_winmoney.setText(rowsBean.getWinMoney() + "");
        tv_yingkui.setText(rowsBean.getWinMoney() - rowsBean.getBettingMoney() + "");
        tv_content.setText(Html.fromHtml(rowsBean.getBettingContent()));
        tv_tittle.setText(rowsBean.getPlatformType());
        tv_user_name.setText(rowsBean.getUsername());


    }


}
