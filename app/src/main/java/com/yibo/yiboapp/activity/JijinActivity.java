package com.yibo.yiboapp.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.IncomeData;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.views.RiseNumberTextView;

public class JijinActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvLeiji;
    private TextView tvWanFen;
    private TextView tvQiRi;
    private TextView tvYUjiNian;
    private TextView tvYujiYue;
    private RiseNumberTextView tvShouyi;
    private TextView tvZongJinE;
    private TextView tvYinCang;
    private TextView tvBack;
    private TextView tvZhangdan;
    private TextView tvDayi;
    private RelativeLayout rlkefu;
    private SwipeRefreshLayout refreshLayout;
    private boolean isShow = true;
    private String zongjine;
    IncomeData incomeData;
    private String url="";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_jijin);
        initView();//初始化控件
        loadData();//从接口获取数据
//        loadUrl();//获取在线答疑的链接+
    }

    @Override
    protected void initView() {
        super.initView();
        tvLeiji = findViewById(R.id.tv_leijishouyi);
        tvWanFen = findViewById(R.id.tv_wanfenshouyi);
        tvQiRi = findViewById(R.id.tv_qirinianhua);
        tvYUjiNian = findViewById(R.id.tv_yujinian);
        tvYujiYue = findViewById(R.id.tv_yujiyue);
        tvShouyi = findViewById(R.id.tv_shouyi);
        tvZongJinE = findViewById(R.id.tv_zongjine);
        tvYinCang = findViewById(R.id.tv_yincang);
        tvBack = findViewById(R.id.tv_back);
        tvZhangdan = findViewById(R.id.tv_zhangdan);
        tvDayi = findViewById(R.id.tv_dayi);
        rlkefu = findViewById(R.id.rl_lianxikefu);
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        tvYinCang.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvZhangdan.setOnClickListener(this);
        tvDayi.setOnClickListener(this);
        rlkefu.setOnClickListener(this);

    }


    //从接口获取数据
    void loadData() {
        HttpUtil.postForm(this, Urls.INCOME_MONEY_URL, null, true, getString(R.string.get_recording), new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                if (result.isSuccess()) {
                    incomeData = new Gson().fromJson(result.getContent(), IncomeData.class);
                    Log.e("123321", "receive: "+result.getContent());
                    if (incomeData != null) {
                        updateUI();
                    } else {

                        return;
                    }
                }else {
                    showToast(result.getMsg()+"");
                }
            }


        });
    }
    //从接口获取数据
//    void loadUrl() {
//        HttpUtil.postForm(this, Urls.INCOME_QUESTION_URL, null, true, getString(R.string.get_recording), result -> {
//            Log.e("123123", result.getContent());
//            if (result.isSuccess()) {}
//
//        });
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yincang:
                changeJinE();
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_zhangdan:
                Intent intent = new Intent(this, JijinZhangdanActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_dayi:
                intent = new Intent(this, KefuActivity.class);
                intent.putExtra("title", "余额生金");
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.setCookie(Urls.BASE_URL+Urls.INCOME_QUESTION_URL, "SESSION="+YiboPreference.instance(this).getToken());//cookies是在HttpClient中获得的cookie
                CookieSyncManager.getInstance().sync();
                intent.putExtra("url", Urls.BASE_URL+Urls.INCOME_QUESTION_URL);
                startActivity(intent);
                break;
            case R.id.rl_lianxikefu:
                UsualMethod.viewService(this);
                break;
        }

    }

    //切换显示隐藏余额
    void changeJinE() {
        String str;
        if (isShow) {
            isShow = false;
            str = "总金额***元";
            tvZongJinE.setText(str);
            tvYinCang.setBackgroundResource(R.drawable.jijin_bukejian);
        } else {
            isShow = true;
            str = "总金额" + zongjine + "元";
            tvZongJinE.setText(str);
            tvYinCang.setBackgroundResource(R.drawable.jijin_kejian);
        }
    }


    void updateUI() {
        zongjine = incomeData.getMoney();
        tvYujiYue.setText(incomeData.getIncomeMon());
        tvYUjiNian.setText(incomeData.getIncomeYear());
        tvZongJinE.setText("总金额" + incomeData.getMoney() + "元");
        tvLeiji.setText(incomeData.getHisIncome() == null ? "0" : incomeData.getHisIncome());
        tvWanFen.setText(incomeData.getScaleEnd());
        tvQiRi.setText(incomeData.getSevendayRate());
        tvShouyi.withNumber(new Float(incomeData.getYesterdayRecord()));
        tvShouyi.setDuration(1500L);
        tvShouyi.start();
    }
}
