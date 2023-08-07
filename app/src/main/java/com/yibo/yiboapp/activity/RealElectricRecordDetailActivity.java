package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.RealRecordResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RealElectricRecordDetailActivity extends BaseActivity {

    private static final String DETAIL = "detail";
    public static void createIntent(Context context, String json){
        Intent intent = new Intent(context, RealElectricRecordDetailActivity.class);
        intent.putExtra(DETAIL, json);
        context.startActivity(intent);
    }

    private TextView textClose;
    private TextView textOrderNo;
    private TextView textMemberAccount;
    private TextView textBetMoney;
    private TextView textBetDatetime;
    private TextView textGameType;
    private TextView textPlatform;
    private TextView textGameContent;
    private TextView textGameCode;
    private TextView textTableNo;
    private TextView textWinMoney;
    private TextView textNetMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_electric_record_detail);

        initView();
        String json = getIntent().getStringExtra(DETAIL);
        RealRecordResponse.RealRecordDetail detail = new Gson().fromJson(json, RealRecordResponse.RealRecordDetail.class);
        showRecordDetail(detail);
    }

    @Override
    protected void initView() {
        textClose = findViewById(R.id.text_close);
        textClose.setOnClickListener(v -> { onBackPressed(); });
        textOrderNo = findViewById(R.id.text_order_no);
        textMemberAccount = findViewById(R.id.text_member_account);
        textBetMoney = findViewById(R.id.text_bet_money);
        textBetDatetime = findViewById(R.id.text_bet_datetime);
        textGameType = findViewById(R.id.text_game_type);
        textPlatform = findViewById(R.id.text_platform);
        textGameContent = findViewById(R.id.text_bet_content);
        textGameCode = findViewById(R.id.text_game_code);
        textTableNo = findViewById(R.id.text_table_no);
        textWinMoney = findViewById(R.id.text_win_money);
        textNetMoney = findViewById(R.id.text_net_money);
    }

    private void showRecordDetail(RealRecordResponse.RealRecordDetail detail){
        textOrderNo.setText(String.format(getString(R.string.record_order_no), detail.getOrderId()));
        textMemberAccount.setText(detail.getAccount());
        textBetMoney.setText(String.format("%.0f", detail.getBettingMoney()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        textBetDatetime.setText(sdf.format(new Date(detail.getBettingTime())));
        textGameType.setText(detail.getGameType());
        textPlatform.setText(detail.getPlatformType());
        textGameContent.setText(detail.getBettingContent());
        textGameCode.setText(detail.getGameCode());
        textTableNo.setText("");
        textWinMoney.setText(String.format("%.0f", detail.getWinMoney()));
        textNetMoney.setText(String.format("%.0f", detail.getWinMoney() - detail.getBettingMoney()));
    }
}