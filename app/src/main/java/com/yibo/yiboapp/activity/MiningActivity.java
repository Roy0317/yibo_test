package com.yibo.yiboapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.MiningActiveBean;
import com.yibo.yiboapp.entify.WakuangListBean;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.MiningDialog;

import java.util.List;

public class MiningActivity extends BaseActivity {

    ImageView titleIndictor;
    ImageView ivSearch;
    ImageView tvKaicai1;
    ImageView tvKaicai2;
    ImageView tvKaicai3;
    ImageView tvKaicai4;
    ImageView tvKaicai5;
    ImageView tvKaicai6;
    ImageView tvKaicai7;
    TextView tvSubTitle1;
    TextView tvSubTitle2;
    TextView tvSubTitle3;
    TextView tvSubTitle4;
    TextView tvSubTitle5;
    TextView tvSubTitle6;
    TextView tvSubTitle7;
    TextView tv_amount;
    TextView tv_active;
    TextView tv_rule;

    List<WakuangListBean> listBeans;
    Dialog dialog;
    String id;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mining_active);
        initView();
        initData();
    }

    protected void initView() {
        super.initView();

        titleIndictor = findViewById(R.id.title_indictor);
        ivSearch = findViewById(R.id.iv_search);
        tvKaicai1 = findViewById(R.id.tv_kaicai1);
        tvKaicai2 = findViewById(R.id.tv_kaicai2);
        tvKaicai3 = findViewById(R.id.tv_kaicai3);
        tvKaicai4 = findViewById(R.id.tv_kaicai4);
        tvKaicai5 = findViewById(R.id.tv_kaicai5);
        tvKaicai6 = findViewById(R.id.tv_kaicai6);
        tvKaicai7 = findViewById(R.id.tv_kaicai7);
        tvSubTitle1 = findViewById(R.id.tv_sub_title1);
        tvSubTitle2 = findViewById(R.id.tv_sub_title2);
        tvSubTitle3 = findViewById(R.id.tv_sub_title3);
        tvSubTitle4 = findViewById(R.id.tv_sub_title4);
        tvSubTitle5 = findViewById(R.id.tv_sub_title5);
        tvSubTitle6 = findViewById(R.id.tv_sub_title6);
        tvSubTitle7 = findViewById(R.id.tv_sub_title7);
        tv_amount = findViewById(R.id.tv_amount);
        tv_active = findViewById(R.id.tv_active);
        tv_rule = findViewById(R.id.tv_rule);
        tvMiddleTitle.setText(getString(R.string.mining_active));

        ivSearch.setOnClickListener(this);
        tvKaicai1.setOnClickListener(this);
        tvKaicai2.setOnClickListener(this);
        tvKaicai3.setOnClickListener(this);
        tvKaicai4.setOnClickListener(this);
        tvKaicai5.setOnClickListener(this);
        tvKaicai6.setOnClickListener(this);
        tvKaicai7.setOnClickListener(this);
    }

    private void initHistoryDialog() {
        dialog = new MiningDialog(this, listBeans);
        dialog.show();
    }

    private void initData() {
        HttpUtil.get(this, Urls.WAKUANGLIST, null, false, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    MiningActiveBean miningActiveBean = new Gson().fromJson(result.getContent(), MiningActiveBean.class);
                    int color1 = MiningActivity.this.getResources().getColor(R.color.txt_color_mining_title);
                    int color2 = MiningActivity.this.getResources().getColor(R.color.red);

                    //王者
                    SpanUtils.with(tvSubTitle1).append(getDepositStr(miningActiveBean.getDepositType())).setForegroundColor(color1).append(miningActiveBean.getKingMineValue() + "").
                            setForegroundColor(color2).append("元即刻开采").setForegroundColor(color1).create();
                    SpanUtils.with(tvSubTitle2).append(getDepositStr(miningActiveBean.getDepositType())).setForegroundColor(color1).append(miningActiveBean.getStarshineMineValue() + "").
                            setForegroundColor(color2).append("元即刻开采").setForegroundColor(color1).create();
                    SpanUtils.with(tvSubTitle3).append(getDepositStr(miningActiveBean.getDepositType())).setForegroundColor(color1).append(miningActiveBean.getDiamondMineValue() + "").
                            setForegroundColor(color2).append("元即刻开采").setForegroundColor(color1).create();
                    SpanUtils.with(tvSubTitle4).append(getDepositStr(miningActiveBean.getDepositType())).setForegroundColor(color1).append(miningActiveBean.getPlatinumMineValue() + "").
                            setForegroundColor(color2).append("元即刻开采").setForegroundColor(color1).create();
                    SpanUtils.with(tvSubTitle5).append(getDepositStr(miningActiveBean.getDepositType())).setForegroundColor(color1).append(miningActiveBean.getGoldMineValue() + "").
                            setForegroundColor(color2).append("元即刻开采").setForegroundColor(color1).create();
                    SpanUtils.with(tvSubTitle6).append(getDepositStr(miningActiveBean.getDepositType())).setForegroundColor(color1).append(miningActiveBean.getSilverMineValue() + "").
                            setForegroundColor(color2).append("元即刻开采").setForegroundColor(color1).create();
                    SpanUtils.with(tvSubTitle7).append(getDepositStr(miningActiveBean.getDepositType())).setForegroundColor(color1).append(miningActiveBean.getBronzeMineValue() + "").
                            setForegroundColor(color2).append("元即刻开采").setForegroundColor(color1).create();


                    id = miningActiveBean.getId();
                    tv_amount.setText(getDepositStr(miningActiveBean.getDepositType()) + miningActiveBean.getYestodayDepost() + "元");
                    tv_active.setText(miningActiveBean.getActiveContent() == null ? "" : miningActiveBean.getActiveContent());
                    tv_rule.setText(miningActiveBean.getActiveRule() == null ? "" : Html.fromHtml(miningActiveBean.getActiveRule()));
                } else {
                    ToastUtils.showShort(TextUtils.isEmpty(result.getMsg()) ? "活动暂未开启" : result.getMsg());
                }
            }
        });

    }

    private String getDepositStr(String depositType) {
        String str = "";
        switch (depositType) {
            case "1":
                str = "昨日累计充值";
                break;
            case "2":
                str = "今日累计充值";
                break;
            case "3":
                str = "今日累计打码";
                break;
            case "4":
                str = "昨日累计打码";
                break;
            case "5":
                str = "今日首充";
                break;
        }
        return str;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.iv_search:
                HttpUtil.get(this, Urls.GET_AWARD_LIST, null, true, new HttpCallBack() {
                    @Override
                    public void receive(NetworkResult result) {
                        if (result.isSuccess()) {
                            listBeans = new Gson().fromJson(result.getContent(), new TypeToken<List<WakuangListBean>>() {
                            }.getType());
                            initHistoryDialog();
                        }
                    }
                });
                break;
//            类型对应值  1：钻石矿，2：黄金矿，3：白银矿，4，青铜矿 ，5:铂金矿，6:星耀矿，7:王者矿
            case R.id.tv_kaicai1:
                getKaiCaiPlay(id, 7);
                break;
            case R.id.tv_kaicai2:
                getKaiCaiPlay(id, 6);
                break;
            case R.id.tv_kaicai3:
                getKaiCaiPlay(id, 1);
                break;
            case R.id.tv_kaicai4:
                getKaiCaiPlay(id, 5);
                break;
            case R.id.tv_kaicai5:
                getKaiCaiPlay(id, 2);
                break;
            case R.id.tv_kaicai6:
                getKaiCaiPlay(id, 3);
                break;
            case R.id.tv_kaicai7:
                getKaiCaiPlay(id, 4);
                break;
        }
    }

    /**
     * 立即开采
     *
     * @param id
     * @param awardType
     */
    private void getKaiCaiPlay(String id, int awardType) {
        ApiParams apiParams = new ApiParams();
        apiParams.put("id", id);
        apiParams.put("awardType", awardType);
        HttpUtil.get(this, Urls.WAKUANG_PLAY, apiParams, true, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    String content = result.getContent();
                    ToastUtils.showShort("恭喜获得价值：" + content + "元的矿产");
                } else {
                    ToastUtils.showShort(TextUtils.isEmpty(result.getMsg()) ? "开采失败" : result.getMsg());
                }
            }
        });

    }


}
