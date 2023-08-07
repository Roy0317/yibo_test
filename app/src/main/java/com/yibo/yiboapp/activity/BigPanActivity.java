package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.TouzhuThreadPool;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.ActiveRecord;
import com.yibo.yiboapp.entify.AwardRecordWraper;
import com.yibo.yiboapp.entify.BigWheelData;
import com.yibo.yiboapp.entify.BigWheelWraper;
import com.yibo.yiboapp.entify.CouJianResult;
import com.yibo.yiboapp.entify.CouJianResultWraper;
import com.yibo.yiboapp.entify.ExchangeResults;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.LuckPanView;

import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * 大转盘
 */
public class BigPanActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {

    public static final int BIG_WHEEL_DATA = 0x01;
    public static final int BIG_WHEEL_RECORD = 0x02;
    public static final int BIG_WHEEL_COUJIAN = 0x03;
    public static final int MY_AWRAD_RECORDS = 0x04;

    TextView awardRecordTxt;
    private List<String> awardNames;
    PanRecyclerAdapter panRecordsAdapter;
    long activeId = 0;
    int activePay = 0;
    CouJianResult couJianResult;//每次抽奖的中奖结果
    int MAX_LOOP_TIMES = 4;//轮动几次

    LuckPanView panView;
    ImageView startBtn;

    TextView activityRule;
    TextView activityCondition;
    TextView activityNotices;
    TextView tv_integral;//积分

    Handler updateHandler;
    int currentLoopTimes = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bigwheel_activity);
        initView();
        updateHandler = new Handler();
        awardRecordTxt = (TextView) findViewById(R.id.hit_user_tv);
        awardNames = new ArrayList<>();
        panView = findViewById(R.id.rotatePan);
        activityRule = (TextView) findViewById(R.id.game_rule_tv);
        activityCondition = (TextView) findViewById(R.id.game_condition_tv);
        activityNotices = (TextView) findViewById(R.id.game_notices_tv);
        tv_integral = findViewById(R.id.tv_integral);

        panRecordsAdapter = new PanRecyclerAdapter(R.layout.bigpan_record_item, awardNames);
//        panView.setLayoutManager(new GridLayoutManager(this, 4));
//        panRecordsAdapter.setFocusPos(-1);
//        panView.setAdapter(panRecordsAdapter);
        startBtn = (ImageView) findViewById(R.id.start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //转动转盘之前先获取之次抽奖应中的奖项数据。
                actionPickBigWheel();
            }
        });

        panView.setOnLuckPanAnimatorEndListener(new LuckPanView.OnLuckPanAnimatorEndListener() {
            @Override
            public void onLuckPanAnimatorEnd(String choicePrizeVo) {
                showAwardsDialog(choicePrizeVo);
            }
        });

        loadScoreData();
        loadData();
    }

    private void loadScoreData() {
        HttpUtil.get(this, Urls.EXCHANGE_CONFIG_URL, null, false, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    ExchangeResults exchangeConfigResultWraper = new Gson().fromJson(result.getContent(), ExchangeResults.class);
                    tv_integral.setText("积分：" + exchangeConfigResultWraper.getScore());
                }
            }
        });
    }

    private void startUpdateView() {
        if (couJianResult == null) {
            return;
        }
        startBtn.setEnabled(false);
        int stopPos = (int) couJianResult.getIndex();
        Utils.LOG(TAG, "the final stop pos = " + stopPos);
        stopPos--;//抽奖的中奖奖项index是从1开始的，要减1处理
        int totalLoopTimes = awardNames.size() * MAX_LOOP_TIMES + stopPos;
        int newPos = panRecordsAdapter.getFocusPos();
        newPos++;
        currentLoopTimes++;
        int posInOneLoop = newPos % awardNames.size();
        panRecordsAdapter.setFocusPos(posInOneLoop);
        panRecordsAdapter.notifyDataSetChanged();
        if (currentLoopTimes < totalLoopTimes) {
            updateHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startUpdateView();
                }
            }, 100);
        } else {
            startBtn.setEnabled(true);
            showAwardsDialog(couJianResult.getAwardName());
        }
    }

    private void loadData() {
        StringBuilder lotteryUrl = new StringBuilder();
        lotteryUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.BIG_WHEEL_DATA_URL);
        CrazyRequest<CrazyResult<BigWheelWraper>> lotteryRequest = new AbstractCrazyRequest.Builder().
                url(lotteryUrl.toString())
                .seqnumber(BIG_WHEEL_DATA)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .refreshAfterCacheHit(true)
                .placeholderText(getString(R.string.acquire_award_ongoing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<BigWheelWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, lotteryRequest);
    }

    private void actionPickBigWheel() {
        if (activeId == 0) {
            showToast("没有活动ID，无法抽奖");
            return;
        }
        StringBuilder lotteryUrl = new StringBuilder();
        lotteryUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.BIG_WHEEL_ACTION_URL);
        lotteryUrl.append("?activeId=").append(activeId);
        lotteryUrl.append("&activePay=").append(activePay);
        CrazyRequest<CrazyResult<CouJianResultWraper>> lotteryRequest = new AbstractCrazyRequest.Builder().
                url(lotteryUrl.toString())
                .seqnumber(BIG_WHEEL_COUJIAN)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .refreshAfterCacheHit(true)
                .placeholderText(getString(R.string.coujian_going))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<CouJianResultWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, lotteryRequest);
    }

    /**
     * 获取中奖记录
     */
    private void actionAwardRecord() {
        StringBuilder lotteryUrl = new StringBuilder();
        lotteryUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.BIG_WHEEL_AWARD_RECORD_URL);
        lotteryUrl.append("?activeId=").append(activeId);
        CrazyRequest<CrazyResult<AwardRecordWraper>> lotteryRequest = new AbstractCrazyRequest.Builder().
                url(lotteryUrl.toString())
                .seqnumber(BIG_WHEEL_RECORD)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .refreshAfterCacheHit(true)
                .placeholderText(getString(R.string.coujian_going))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<AwardRecordWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, lotteryRequest);
    }


    private void showAwardsDialog(String awards) {

        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(1);
        ccd.setContent(awards);
        ccd.setMiddleBtnText("好的");
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        ccd.createDialog();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("幸运大转盘");
        tvRightText.setText("中奖记录");
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordsActivityNew.createIntent(getApplicationContext(), "中奖记录", Constant.BIG_PAN_RECORD_STATUS, "");
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.right_text) {

        }
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, BigPanActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == BIG_WHEEL_DATA) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("获取失败");
                return;
            }
            if (!result.crazySuccess) {
                showToast("获取失败");
                return;
            }
            Object regResult = result.result;
            BigWheelWraper reg = (BigWheelWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        "获取失败");
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            BigWheelData bigWheelData = reg.getContent();
            if (bigWheelData != null) {
                activeId = bigWheelData.getActiveId();
                activePay = bigWheelData.isActivePay() ? 1 : 0;
                if (bigWheelData.getAwardNames() != null) {
                    List<String> names = bigWheelData.getAwardNames();
                    String[] namesList = new String[names.size()];
                    for (int i = 0; i < names.size(); i++) {
                        String c = names.get(i);
                        if (!Utils.isEmptyString(c)) {
                            namesList[i] = c;
                        }
                    }
                    panView.setPrizeVoList(names);
                    activityRule.setText(Html.fromHtml(!Utils.isEmptyString(bigWheelData.getActivity_rule()) ? bigWheelData.getActivity_rule() : ""));
                    activityCondition.setText(Html.fromHtml(!Utils.isEmptyString(bigWheelData.getActivity_condition()) ? bigWheelData.getActivity_condition() : ""));
                    activityNotices.setText(Html.fromHtml(!Utils.isEmptyString(bigWheelData.getActivity_notices()) ? bigWheelData.getActivity_notices() : ""));
                }
                //获取中奖记录
                actionAwardRecord();
            }
        } else if (action == BIG_WHEEL_COUJIAN) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("获取失败");
                return;
            }
            if (!result.crazySuccess) {
                showToast("获取失败");
                return;
            }
            Object regResult = result.result;
            CouJianResultWraper reg = (CouJianResultWraper) regResult;
            if (!reg.isSuccess()) {
                ToastUtils.showShort(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : "抽奖失败");
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            couJianResult = reg.getContent();
            if (couJianResult != null) {
                Utils.LOG(TAG, "coujian = " + couJianResult.getAwardName() + ",index = " + couJianResult.getIndex());
            }

            loadScoreData();
            updateHandler.post(new Runnable() {
                @Override
                public void run() {
                    currentLoopTimes = -1;
//                    panRecordsAdapter.setFocusPos(-1);
//                    startUpdateView();
                    panView.start(couJianResult.getIndex());
                }
            });
        } else if (action == BIG_WHEEL_RECORD) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult = result.result;
            AwardRecordWraper reg = (AwardRecordWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : "抽奖中奖记录失败");
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            formAwardRecordStr(reg.getContent());
        }
    }

    public void formAwardRecordStr(List<ActiveRecord> records) {
        TouzhuThreadPool touzhuThreadPool = TouzhuThreadPool.getInstance();
        touzhuThreadPool.addTask(() -> {
            if (records == null || records.isEmpty()) {
                runOnUiThread(() -> awardRecordTxt.setText("暂无中奖记录"));
            } else {
                StringBuilder sb = new StringBuilder();
                int size = Math.min(records.size() / 3, 200);
                for (int i = 0; i <= size; i++) {
                    ActiveRecord record = records.get(i);
                    sb.append(!Utils.isEmptyString(Utils.hideChar(record.getAccount(), 3)) ? Utils.hideChar(record.getAccount(), 3) : "暂无姓名").append("    ");
                    sb.append(record.getProductName()).append(";       ");
//                    sb.append(Utils.formatTime(record.getCreateDatetime())).append(";        ");
                }
                runOnUiThread(() -> {
                    awardRecordTxt.setText(sb);
                    awardRecordTxt.requestFocus();
                });
            }
        });
    }


    public static class PanRecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        Context context;
        int focusPos;//聚集的点，即中奖奖项Item所在的位置

        public int getFocusPos() {
            return focusPos;
        }

        public void setFocusPos(int focusPos) {
            this.focusPos = focusPos;
        }

        public PanRecyclerAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
            context = mContext;
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {

            final LinearLayout layout = helper.getView(R.id.item);
            final ImageView ivThumb = helper.getView(R.id.img);
            final TextView tvName = helper.getView(R.id.name);
            ivThumb.setBackgroundResource(R.drawable.fee_convert_icon);
            tvName.setText(!Utils.isEmptyString(item) ? item : "暂无奖项名称");
            if (focusPos == -1) {
                layout.setBackgroundResource(R.drawable.bigpan_normal_bg);
            } else {
                if (focusPos == helper.getLayoutPosition()) {
                    layout.setBackgroundResource(R.drawable.bigpan_focus_bg);
                } else {
                    layout.setBackgroundResource(R.drawable.bigpan_normal_bg);
                }
            }
        }
    }

}
