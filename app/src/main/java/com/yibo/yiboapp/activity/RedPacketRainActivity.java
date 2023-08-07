package com.yibo.yiboapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.FakePacketModel;
import com.yibo.yiboapp.entify.GrabPacketWraper;
import com.yibo.yiboapp.entify.GradFakeRecordWraper;
import com.yibo.yiboapp.entify.RedPacketResult;
import com.yibo.yiboapp.entify.RedPacketWraper;
import com.yibo.yiboapp.ui.RedPacketsLayout;
import com.yibo.yiboapp.utils.Utils;

import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.CustomDialogManager;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;


public class RedPacketRainActivity extends BaseActivity implements
        View.OnClickListener,SessionResponse.Listener<CrazyResult<Object>> {
    private RedPacketsLayout redRainView;

    LinearLayout noticeLayout;
    TextView scroll_money;

    TextView countTimerTV;
    private int totalmoney = 0;
    AlertDialog.Builder ab;

    public static final int VALID_REDPACKET_REQUEST = 0x01;
    public static final int GRAB_REDPACKET_REQUEST = 0x02;
    public static final int RECORD_REDPACKET_REQUEST = 0x03;

    CountDownTimer timer;
    long redPacketId;
    RedPacketResult redPacketResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rain_red_packet);
        initView();
        ab = new AlertDialog.Builder(RedPacketRainActivity.this);
        noticeLayout = (LinearLayout) findViewById(R.id.notice_layout);
        scroll_money = (TextView) findViewById(R.id.scroll_money);
        countTimerTV = (TextView) findViewById(R.id.downtimer);
        redRainView.setRainViewEvent(new RedPacketsLayout.RainViewEvent() {
            @Override
            public void onRedPacket() {
                grabPacket(redPacketId);
            }
        });
        getValidPacket(true);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("红包规则");
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //点击红包规则
                StringBuilder configUrl = new StringBuilder();
                configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.RED_PACKET_RULE_URL);
                ActiveDetailActivity.createIntent(RedPacketRainActivity.this,"", "红包规则" , configUrl.toString());
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        redRainView = (RedPacketsLayout) findViewById(R.id.packets_layout);
    }


    private void updateNotices(List<FakePacketModel> notices) {
        if (notices == null || notices.isEmpty()) {
            noticeLayout.setVisibility(View.GONE);
            return;
        }
        String str = "";
        for (FakePacketModel bean : notices) {
            str += bean.getAccount();
//            str += !Utils.isEmptyString(bean.getAccount()) ? Utils.hideTailChar(bean.getAccount(), 4) : "";
            str += ":"+String.format("%.2f元", bean.getMoney());
        }
        noticeLayout.setVisibility(View.VISIBLE);
        scroll_money.setText(str);
    }

    /**
     * 获取可用红包信息
     * @param showDialog
     */
    private void getValidPacket(boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.VALID_RED_PACKET_URL);
        CrazyRequest<CrazyResult<RedPacketWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(VALID_REDPACKET_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(false).placeholderText(getString(R.string.acquire_rp_ongoing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<RedPacketWraper>(){}.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this,request);
    }

    private void grabFakeRecord(long rpId) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.FAKE_PACKET_DATAS);
        configUrl.append("?redPacketId=" + rpId);
        CrazyRequest<CrazyResult<GradFakeRecordWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(RECORD_REDPACKET_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(false).placeholderText(getString(R.string.acquire_rp_ongoing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<GradFakeRecordWraper>(){}.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this,request);
    }

    /**
     * 抢红包
     * @param rpId
     */
    private void grabPacket(long rpId) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GRAB_RED_PACKET_URL);
        configUrl.append("?redPacketId=" + rpId);
        CrazyRequest<CrazyResult<GrabPacketWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(GRAB_REDPACKET_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(false).placeholderText(getString(R.string.acquire_rp_ongoing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<GrabPacketWraper>(){}.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this,request);
    }


    /**
     * 开始下红包雨
     */
    private void startRedRain() {
        redRainView.post(new Runnable() {
            @Override
            public void run() {
                redRainView.startRain();
            }
        });
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, RedPacketRainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 停止下红包雨
     */
    private void stopRedRain() {
        totalmoney = 0;//金额清零
        redRainView.stopRain();
    }

    Handler handler = new Handler(){
        public void handleMessage(Message message) {
            if (message.what == 1) {
                String content = (String) message.obj;
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (redPacketResult != null) {
                    //createTimer(Math.abs(redPacketResult.getEndDatetime() - /*redPacket.getBeginDatetime()*/System.currentTimeMillis()));
                    createTimer(redPacketResult.getBeginDatetime(), redPacketResult.getEndDatetime());
                    if (timer != null) {
                        timer.start();
                    }
                }
                if (!Utils.isEmptyString(content)) {
                    noticeLayout.setVisibility(View.VISIBLE);
                    scroll_money.setText(content);
                }else{
                    noticeLayout.setVisibility(View.GONE);
                }
            }
        }
    };

    private void startUpdateRecordThread(final List<FakePacketModel> recordBeens) {

        if (recordBeens == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = "";
                if (recordBeens != null && !recordBeens.isEmpty()) {
                    for (FakePacketModel bean : recordBeens) {
                        str += String.format("%s:%.2f元", bean.getAccount(),bean.getMoney());
                    }
                }
                Message msg = new Message();
                msg.what = 1;
                msg.obj = str;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == VALID_REDPACKET_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_rp_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.acquire_rp_fail);
                return;
            }
            Object regResult =  result.result;
            RedPacketWraper reg = (RedPacketWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg())?reg.getMsg():
                        getString(R.string.acquire_rp_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                redPacketResult = reg.getContent();
                redPacketId = redPacketResult.getId();
                tvMiddleTitle.setText(!Utils.isEmptyString(redPacketResult.getTitle())?
                        redPacketResult.getTitle():getString(R.string.red_packet_activity));

//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                createTimer(redPacketResult.getBeginDatetime(), redPacketResult.getEndDatetime());

                //红包获取到后，开始红包雨
                if (redPacketResult.getBeginDatetime() <= System.currentTimeMillis()) { //活动已经开始
                    startRedRain();
                }
                //异步获取当前用户已抢的红包金额
                grabFakeRecord(redPacketResult.getId());
            }
        }else if (action == RECORD_REDPACKET_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult =  result.result;
            GradFakeRecordWraper reg = (GradFakeRecordWraper) regResult;
            if (!reg.isSuccess()) {
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            startUpdateRecordThread(reg.getContent());

        }else if (action == GRAB_REDPACKET_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult =  result.result;
            GrabPacketWraper reg = (GrabPacketWraper) regResult;
            if (!reg.isSuccess()) {
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }else{
                    if (!Utils.isEmptyString(reg.getMsg())) {
                        showRobPacketSuccessDialog(reg.getMsg());
                    }else{
                        showToast(getString(R.string.grab_rp_fail));
                    }
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() > 0) {
                showRobPacketSuccessDialog("恭喜您抢到了 " + reg.getContent() + "元");
            }
        }
    }

    /**
     * 提示抢到红包框
     * @param message
     */
    private void showRobPacketSuccessDialog(String message) {

        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(1);
        ccd.setTitle("红包提醒");
        ccd.setContent(message);
        ccd.setMiddleBtnText("好的");
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    private boolean isEnd = false;//抢红包活动是否结束

    private void createTimer(final long beginDatetime, long endDatetime) {
        isEnd = false;
        long millis = System.currentTimeMillis();
        if (beginDatetime <= millis) { //活动已经开始
            if (endDatetime >= millis) {  //活动正在进行
                long duration = endDatetime - millis;
                timer = new CountDownTimer(duration, 1000) {
                    public void onTick(long millisUntilFinished) {
                        countTimerTV.setText("距离活动结束:"+Utils.int2Time(millisUntilFinished));
                    }

                    public void onFinish() {
                        isEnd = true;
                        countTimerTV.setText("活动已结束");
                        stopRedRain();
                        getValidPacket(false);
                    }
                };
                timer.start();
            } else {
                isEnd = true;
                countTimerTV.setText("活动已结束");
            }
        } else {  //活动还未开始
            long duration = beginDatetime - millis;
            timer = new CountDownTimer(duration, 1000) {
                public void onTick(long millisUntilFinished) {
                    countTimerTV.setText("距离活动开始还差:" + Utils.int2Time(millisUntilFinished));
                }

                public void onFinish() {
                    isEnd = true;
                    countTimerTV.setText("活动已结束");
                    stopRedRain();
                    getValidPacket(false);
                }
            };
            timer.start();
        }

    }



    private void createTimer(final long duration) {
        timer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished;
                countTimerTV.setText("距离活动结束:"+Utils.int2Time(time));
            }
            public void onFinish() {
                countTimerTV.setText("活动已截止");
                stopRedRain();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRedRain();
        if (timer != null){
            timer.cancel();
            timer = null;
        }

    }
}
