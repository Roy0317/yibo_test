package com.yibo.yiboapp.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.MidAutumnJiangpinAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.FakeBean;
import com.yibo.yiboapp.entify.MidAutumnActiveBean;
import com.yibo.yiboapp.entify.MyPrizeDataBean;

import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.ChouJiangDialog;
import com.yibo.yiboapp.views.DiceSurface;
import com.yibo.yiboapp.views.MidAutumnPrizeDialog;


import java.util.List;

public class MidAutumnActivity extends BaseActivity implements View.OnClickListener {
    private DiceSurface dice;//抽奖游戏对象
    private ChouJiangDialog chouJiangDialog;//抽奖弹窗
    private MidAutumnActiveBean bean;//活动实体
    private RecyclerView recyclerView;//奖品展示recyclerview
    private List<FakeBean> rows;//状元榜
    private MidAutumnJiangpinAdapter midAutumnJiangpinAdapter;
    private List<MyPrizeDataBean> myPrizeData;//我的奖品
    private TextView tv_guize;//活动规则
    private TextView tv_shengming;//活动声明
    private TextView tv_time;//活动倒计时
    private TextView tv_cishu;//剩余次数
    private String[] myPrize = new String[]{"博到", "奖品", "时间", "状态"};
    private String[] campionList = new String[]{"排名", "昵称", "官衔", "次数"};
    private int cishu;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_autumn);
        dice = (DiceSurface) findViewById(R.id.diceSurface);
        tv_shengming = findViewById(R.id.tv_shengming);
        tv_guize = findViewById(R.id.tv_guize);
        tv_time = findViewById(R.id.tv_time);
        tv_cishu = findViewById(R.id.tv_cishu);
        dice.setHandler(handler);
        chouJiangDialog = new ChouJiangDialog(this);
        recyclerView = findViewById(R.id.rc_midautu);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        initdata();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void clickMe(View v) {
        switch (v.getId()) {
            case R.id.dianji:
                dice.shiWan();
                break;
            case R.id.choujiang:
                play();

                break;
//我的奖项
            case R.id.tv_wode:

              getMyPrizeData(true);
                break;
//活动细则
            case R.id.tv_xize:
                MidAutumnPrizeDialog dialog2 = new MidAutumnPrizeDialog(this, R.layout.dialog_autumn_xize);
                dialog2.initDialog().show();
                break;
//状元榜
            case R.id.tv_zhuangyuan:
                if (rows == null) {
                    getPrizeData(true);
                } else {
                    MidAutumnPrizeDialog dialog = new MidAutumnPrizeDialog(this, rows, campionList, MidAutumnPrizeDialog.CHAMPION_LIST);
                    dialog.initDialog().show();
                }
                break;
            case R.id.back:
                finish();
                break;

            default:

                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int[] res = dice.getResult();
                    cultRes(res);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void cultRes(int[] res) {


        ShowDialog(res);
    }

    void ShowDialog(int[] res) {
        chouJiangDialog.show();
        chouJiangDialog.showJieguo(res);
        chouJiangDialog.setOnExitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chouJiangDialog.dismiss();
            }
        });
        chouJiangDialog.setOnPlayListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chouJiangDialog.dismiss();
                dice.shiWan();
            }
        });
    }


    void play() {
        ApiParams params = new ApiParams();
        params.put("id", bean.getId());
        HttpUtil.postForm(this, Urls.BOBIN_PLAY, params, true, "请稍候..", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    String a = result.getContent();
                    a = a.replace("[", "").replace("]", "").replace(",", "");
                    char[] b = a.toCharArray();
                    dice.choujiang(b);
                    cishu--;
                    tv_cishu.setText("剩余抽奖次数" + cishu + "次");
                } else {
                    showToast(result.getMsg());
                }

            }
        });


    }

    void initdata() {
        HttpUtil.postForm(this, Urls.GET_BOBING_ACTIVE, null, false, "请稍候..", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    bean = new Gson().fromJson(result.getContent(), MidAutumnActiveBean.class);
                    midAutumnJiangpinAdapter = new MidAutumnJiangpinAdapter(getApplicationContext(), bean.getAwardList());
                    recyclerView.setAdapter(midAutumnJiangpinAdapter);
                    tv_guize.setText(bean.getActiveRole());
                    tv_shengming.setText(bean.getActiveRemark());
                    cishu = bean.getPlayNum();
                    tv_cishu.setText("剩余抽奖次数" + cishu + "次");
                    createTimer(bean.getEndDatetime());
                } else {
                    showToast(result.getMsg());
                    findViewById(R.id.choujiang).setVisibility(View.GONE);
                }
            }
        });
        getPrizeData(false);
    }

    private void getPrizeData(final boolean show) {
        ApiParams params = new ApiParams();
        params.put("type", 4);

        HttpUtil.postForm(this, Urls.GET_NATIVE_FAKE_DATA_NEW, params, false, "请稍候..", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    rows =  new Gson().fromJson(result.getContent(), new TypeToken<List<FakeBean>>() {}.getType());

                    if (show) {
                        MidAutumnPrizeDialog dialog = new MidAutumnPrizeDialog(MidAutumnActivity.this, rows, campionList, MidAutumnPrizeDialog.CHAMPION_LIST);
                        dialog.initDialog().show();
                    }

                } else {
                    showToast(result.getMsg());

                }

            }
        });
    }


    private void getMyPrizeData(final boolean show) {
        HttpUtil.postForm(this, Urls.GET_BOBIN_AWARDLIST, null, false, "请稍候..", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    myPrizeData = new Gson().fromJson(result.getContent(), new TypeToken<List<MyPrizeDataBean>>() {
                    }.getType());
                    if (show) {
                        MidAutumnPrizeDialog dialog = new MidAutumnPrizeDialog(MidAutumnActivity.this, myPrizeData, myPrize, MidAutumnPrizeDialog.MY_PRIZE, 0);
                        dialog.initDialog().show();
                    }

                } else {
                    showToast(result.getMsg());
                }
                result.getContent();

            }
        });


    }

    private void createTimer(long duration) {
        duration = duration - System.currentTimeMillis();
        if (duration <= 0) {
            tv_time.setText("活动已截止");
        } else {
            timer = new CountDownTimer(duration, 1000) {
                public void onTick(long millisUntilFinished) {
                    tv_time.setText("距离活动结束:" + Utils.int2Time(millisUntilFinished));
                }

                public void onFinish() {
                    tv_time.setText("活动已截止");

                }
            };
            timer.start();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}