package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.yibo.yiboapp.ui.ScrollTextView;
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

public class RedPacketNewActivity extends BaseActivity  implements SessionResponse.Listener<CrazyResult<Object>> {

    private ScrollTextView infoTv;
    private TextView showStatusTv;
    private TextView timeTv1;
    private TextView timeTv2;
    private TextView timeTv3;
    private TextView timeTv4;
    private TextView timeTv5;
    private TextView timeTv6;
    private LinearLayout mView;
    private LinearLayout windata_fake;

    private long redPacketId; //红包ID
    private boolean isGrabPacket = false;//正在请求红包

    private CountDownTimer timer;

    public static final int VALID_REDPACKET_REQUEST = 0x01;
    public static final int GRAB_REDPACKET_REQUEST = 0x02;
    public static final int RECORD_REDPACKET_REQUEST = 0x03;


    public static void createIntent(Context context) {
        Intent intent = new Intent(context, RedPacketNewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_red_packet_new);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        mView = findViewById(R.id.act_red_packet_new_main);
        infoTv = findViewById(R.id.act_red_packet_new_info);
        showStatusTv = findViewById(R.id.act_red_packet_status);
        timeTv1 = findViewById(R.id.act_red_packet_time_1);
        timeTv2 = findViewById(R.id.act_red_packet_time_2);
        timeTv3 = findViewById(R.id.act_red_packet_time_3);
        timeTv4 = findViewById(R.id.act_red_packet_time_4);
        timeTv5 = findViewById(R.id.act_red_packet_time_5);
        timeTv6 = findViewById(R.id.act_red_packet_time_6);
        windata_fake = findViewById(R.id.windata_fake);
        initPop();
        findViewById(R.id.act_red_packet_new_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEnd) {
                    redPacketView.showAtLocation(mView, Gravity.CENTER, 0, 0);
                    isShowPop = true;
                    changeBgColor(true);
                } else {
                    showToast("活动已结束");
                }
            }
        });
        findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.act_red_packet_new_rule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //点击红包规则
                StringBuilder configUrl = new StringBuilder();
                configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.RED_PACKET_RULE_URL);
                ActiveDetailActivity.createIntent(RedPacketNewActivity.this,"", "红包规则" , configUrl.toString());
                //UsualMethod.viewLink(RedPacketNewActivity.this, configUrl.toString());
            }
        });

        String showFakeNotice = UsualMethod.getConfigFromJson(this).getSwitch_win_notice_inredpacket_page();
        if (!Utils.isEmptyString(showFakeNotice) && showFakeNotice.equalsIgnoreCase("on")) {
            windata_fake.setVisibility(View.VISIBLE);
        }else{
            windata_fake.setVisibility(View.GONE);
        }
        getValidPacket(false);

    }




    private PopupWindow redPacketView;
    private LinearLayout redPacketLayout;
    private boolean isShowPop = false;

    private void initPop() {
        View popView = this.getLayoutInflater().inflate(R.layout.popup_red_packet, null);
        redPacketView = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        redPacketView.setOutsideTouchable(false);
        redPacketView.setFocusable(false);
        setPopWindowDismiss(redPacketView);
        redPacketView.setAnimationStyle(R.style.adjust_window_anim);
        redPacketLayout = popView.findViewById(R.id.popup_red_packet_layout);
        popView.findViewById(R.id.popup_red_packet_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(RedPacketNewActivity.this, R.anim.red_packet_anim);
                redPacketLayout.startAnimation(anim);

                if (!isGrabPacket) {
                    isGrabPacket = true;
                    grabPacket(redPacketId);
                }

            }
        });
    }

    public void setPopWindowDismiss(PopupWindow pop) {
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeBgColor(false);
            }
        });
    }

    //弹窗的时候变背景颜色
    public void changeBgColor(boolean pop) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (pop) { //弹窗出现的时候
            lp.alpha = 0.3f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);
        } else { //弹窗消失
            lp.alpha = 1f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);
        }
    }

    @Override
    public void onBackPressed() {
        if (!isShowPop) {
            super.onBackPressed();
        }
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
                        showStatus("距离运气红包结束:", Utils.int2Time(millisUntilFinished));
                    }

                    public void onFinish() {
                        isEnd = true;
                        showStatus("活动已结束", "");
                        getValidPacket(false);
                    }
                };
                timer.start();
            } else {
                isEnd = true;
                showStatus("活动已结束", "");
            }
        } else {  //活动还未开始
            long duration = beginDatetime - millis;
            timer = new CountDownTimer(duration, 1000) {
                public void onTick(long millisUntilFinished) {
                    showStatus("距离运气红包开始:", Utils.int2Time(millisUntilFinished));
                }

                public void onFinish() {
                    isEnd = true;
                    showStatus("活动已结束", "");
                    getValidPacket(false);
                }
            };
            timer.start();
        }

    }

    private void showStatus(String title, String time) {
        if (TextUtils.isEmpty(time)) {
            showStatusTv.setText(title);
            timeTv1.setText("0");
            timeTv2.setText("0");
            timeTv3.setText("0");
            timeTv4.setText("0");
            timeTv5.setText("0");
            timeTv6.setText("0");
        } else {
            if (time.contains("天")) {
                String[] temp = time.split("天");
                showStatusTv.setText(title+ temp[0] + "天");
                setTimeTv(temp[1]);
            } else {
                showStatusTv.setText(title + "0天");
                setTimeTv(time);
            }
        }
    }

    private void setTimeTv(String time){
        String[] temp  = time.split(":");
        timeTv1.setText(temp[0].substring(0,1));
        timeTv2.setText(temp[0].substring(1));
        timeTv3.setText(temp[1].substring(0,1));
        timeTv4.setText(temp[1].substring(1));
        timeTv5.setText(temp[2].substring(0,1));
        timeTv6.setText(temp[2].substring(1));
    }

    private float winMoney = 10;

    /**
     * 抢红包
     *
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

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == GRAB_REDPACKET_REQUEST) { //抢红包
            redPacketLayout.clearAnimation();
            redPacketView.dismiss();
            isShowPop = false;
            isGrabPacket = false;
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
            GrabPacketWraper reg = (GrabPacketWraper) regResult;
            if (!reg.isSuccess()) {
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }else{
                    if (!Utils.isEmptyString(reg.getMsg())) {
                        showToast(reg.getMsg());
                    }else{
                        showToast(getString(R.string.grab_rp_fail));
                    }
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() > 0) {
                showAfterGrabDialog("恭喜您抢到了 " + reg.getContent() + "元");
//                showToast("恭喜您抢到了 " + reg.getContent() + "元");
                //grabFakeRecord(redPacketId);
            } else {
                showAfterGrabDialog("很遗憾您没有抢到红包");
//                showToast("很遗憾您没有抢到红包" );
            }

        } else if (action == VALID_REDPACKET_REQUEST) { //获取可用红包信息
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
                RedPacketResult redPacketResult = reg.getContent();
                redPacketId = redPacketResult.getId();
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                createTimer(redPacketResult.getBeginDatetime(), redPacketResult.getEndDatetime());
            }
            grabFakeRecord(redPacketId);
        } else if (action == RECORD_REDPACKET_REQUEST) {
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
            setWinnerData(reg.getContent());

        }
    }

    private void showAfterGrabDialog(String content) {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(1);
        ccd.setContent(content);
        ccd.setTitle("温馨提示");
        ccd.setMiddleBtnText("好的");
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        ccd.setCanceledOnTouchOutside(false);
        ccd.setCancelable(false);
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
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
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<GradFakeRecordWraper>(){}.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this,request);
    }


    private void setWinnerData(final List<FakePacketModel> recordBeens) {
        StringBuilder sb = new StringBuilder();
        for (FakePacketModel tempBean : recordBeens) {
            //String str = tempBean.getAccount() + "喜中:" + Mytools.getMoney(tempBean.getMoney() + "", true);
            String str = String.format("%s:%.2f元", tempBean.getAccount(),tempBean.getMoney());

            sb.append(str + "    ");
        }
        infoTv.setText(sb.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

}
