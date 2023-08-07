package com.example.anuo.immodule.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.LotteryCountDownBean;
import com.example.anuo.immodule.bean.base.BetInfo;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.ChatSysConfig;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.TimeUtils;
import com.example.anuo.immodule.utils.ToastUtils;
import com.google.gson.reflect.TypeToken;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :17/06/2019
 * Desc  :com.example.anuo.immodule.view
 */
public class BetSlipDialog extends Dialog implements View.OnClickListener, SessionResponse.Listener<CrazyResult<Object>>, CompoundButton.OnCheckedChangeListener {
    private static final int GET_COUNT_DOWN = 0x15;
    private final Context context;
    private final ChatSysConfig chatSysConfig;
    private CountDownTimer timer;
    private String currentQihao;
    private boolean fenpaning;
    private String initQiHao;//跟单注单期号
    private String qiHao;
    private int model;

    /**
     * 设置新的弹窗内容并显示
     *
     * @param betinfo
     */
    public void setBetinfo(BetInfo betinfo) {
        this.betinfo = betinfo;
        initUI();
    }

    private BetInfo betinfo;
    ImageView dialogIvLotteryType;
    TextView dialogTvLotteryName;
    TextView dialogTvLotteryQihao;
    ImageView dialogIvCancel;
    TextView dialogTvLotteryTime;
    TextView dialogTvLotteryPlay;
    TextView dialogTvLotteryContent;
    TextView dialogTvLotteryZhushu;
    TextView dialogTvLotteryAmount;
    EditText dialogEtSingleAmount;
    Button dialogBtnConfirm;
    ImageView dialogIvClear;
    LinearLayout ll_gf_bottom;
    LinearLayout ll_xy_bottom;
    CheckBox cb_yuan;
    CheckBox cb_jiao;
    CheckBox cb_fen;
    EditText dialog_et_beishu;
    Button dialog_btn_confirm_gf;
    ImageView dialogIvClear_gf;
    TextView tv_warning;

    public BetSlipDialog(@NonNull Context context, BetInfo betInfo) {
        super(context, R.style.BetSlipDialog);
        this.context = context;
        this.betinfo = betInfo;
        chatSysConfig = ChatSpUtils.instance(context).getChatSysConfig();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //提前设置Dialog的一些样式
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(R.layout.dialog_bet_slip);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 5;// 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);//点击外部Dialog消失
        initUI();
    }


    private void initUI() {
        dialogIvLotteryType = findViewById(R.id.dialog_iv_lottery_type);
        dialogTvLotteryName = findViewById(R.id.dialog_tv_lottery_name);
        dialogTvLotteryQihao = findViewById(R.id.dialog_tv_lottery_qihao);
        dialogIvCancel = findViewById(R.id.dialog_iv_cancel);
        dialogTvLotteryTime = findViewById(R.id.dialog_tv_lottery_time);
        dialogTvLotteryPlay = findViewById(R.id.dialog_tv_lottery_play);
        dialogTvLotteryContent = findViewById(R.id.dialog_tv_lottery_content);
        dialogTvLotteryZhushu = findViewById(R.id.dialog_tv_lottery_zhushu);
        dialogTvLotteryAmount = findViewById(R.id.dialog_tv_lottery_amount);
        dialogEtSingleAmount = findViewById(R.id.dialog_et_single_amount);
        dialogBtnConfirm = findViewById(R.id.dialog_btn_confirm);
        dialogIvClear = findViewById(R.id.dialog_iv_clear);
        ll_gf_bottom = findViewById(R.id.ll_gf_bottom);
        ll_xy_bottom = findViewById(R.id.ll_xy_bottom);
        cb_yuan = findViewById(R.id.cb_yuan);
        cb_jiao = findViewById(R.id.cb_jiao);
        cb_fen = findViewById(R.id.cb_fen);
        dialog_et_beishu = findViewById(R.id.dialog_et_beishu);
        dialog_btn_confirm_gf = findViewById(R.id.dialog_btn_confirm_gf);
        dialogIvClear_gf = findViewById(R.id.dialog_iv_clear_gf);
        tv_warning = findViewById(R.id.tv_warning);

        if (betinfo == null)
            return;
//        boolean peilv = YiboPreferenceUtils.instance(context).isPeilv();
        //1.官方 2.信用
        if (betinfo.getVersion().equals("1")) {
            ll_gf_bottom.setVisibility(View.VISIBLE);
            ll_xy_bottom.setVisibility(View.GONE);
        } else {
            ll_xy_bottom.setVisibility(View.VISIBLE);
            ll_gf_bottom.setVisibility(View.GONE);
            dialogTvLotteryAmount.setText(betinfo.getLottery_amount());
        }
        dialogTvLotteryName.setText(betinfo.getLottery_type());
        String lottery_qihao = betinfo.getLottery_qihao();
        initQiHao = lottery_qihao;
        tv_warning.setVisibility(View.GONE);
        dialogBtnConfirm.setEnabled(true);
        int length = lottery_qihao.length();
        if (length > 6) {
            lottery_qihao = lottery_qihao.substring(length - 6, length);
        }
        CommonUtils.updateLocImage(context, dialogIvLotteryType, betinfo.getLotCode(), betinfo.getLotIcon());
        dialogTvLotteryQihao.setText("期号：" + lottery_qihao + "期");

        // 获取投注倒计时
        getCountDown(betinfo.getLotCode(), betinfo.getVersion());
        try {
            dialogTvLotteryPlay.setText(betinfo.getLottery_play());
            dialogTvLotteryContent.setText(betinfo.getLottery_content());
            dialogTvLotteryZhushu.setText(betinfo.getLottery_zhushu());
            float zhushu = Float.parseFloat(betinfo.getLottery_zhushu());
            float amount = Float.parseFloat(betinfo.getLottery_amount());
            dialogTvLotteryAmount.setText((float) Math.round(amount * 100) / 100 + "");
            if (zhushu == 0f) {
                dialogEtSingleAmount.setText((float) Math.round(amount * 100) / 100 + "");
            } else {
                String s = floatToInt(amount / zhushu);
                dialogEtSingleAmount.setText(s);
                dialogEtSingleAmount.setSelection(s.length());
            }
            dialogIvCancel.setOnClickListener(this);
            dialogBtnConfirm.setOnClickListener(this);
            dialogIvClear.setOnClickListener(this);
            dialogEtSingleAmount.addTextChangedListener(singleAmountWatcher);
            cb_yuan.setOnCheckedChangeListener(this);
            cb_jiao.setOnCheckedChangeListener(this);
            cb_fen.setOnCheckedChangeListener(this);
            dialogIvClear_gf.setOnClickListener(this);
            dialog_btn_confirm_gf.setOnClickListener(this);
            // 该注单原有模式
            model = betinfo.getModel();
            String switch_currency_unit_show = chatSysConfig.getSwitch_currency_unit_show();
            if (!TextUtils.isEmpty(switch_currency_unit_show)) {
                String[] split = switch_currency_unit_show.split(",");
                if (split[0].split(":")[1].equals("1")) {
                    cb_yuan.setVisibility(View.VISIBLE);
                } else {
                    cb_yuan.setVisibility(View.GONE);
                }
                if (split[1].split(":")[1].equals("1")) {
                    cb_jiao.setVisibility(View.VISIBLE);
                } else {
                    cb_jiao.setVisibility(View.GONE);
                }
                if (split[2].split(":")[1].equals("1")) {
                    cb_fen.setVisibility(View.VISIBLE);
                } else {
                    cb_fen.setVisibility(View.GONE);
                }
            }

            if (model == 1) {
                if (cb_yuan.getVisibility() == View.VISIBLE) {
                    cb_yuan.setChecked(true);
                } else if (cb_jiao.getVisibility() == View.VISIBLE) {
                    cb_jiao.setChecked(true);
                } else {
                    cb_fen.setChecked(true);
                }
            } else if (betinfo.getModel() == 10) {
                if (cb_jiao.getVisibility() == View.VISIBLE) {
                    cb_jiao.setChecked(true);
                } else if (cb_yuan.getVisibility() == View.VISIBLE) {
                    cb_yuan.setChecked(true);
                } else {
                    cb_fen.setChecked(true);
                }
            } else if (betinfo.getModel() == 100) {
                if (cb_fen.getVisibility() == View.VISIBLE) {
                    cb_fen.setChecked(true);
                } else if (cb_yuan.getVisibility() == View.VISIBLE) {
                    cb_yuan.setChecked(true);
                } else {
                    cb_jiao.setChecked(true);
                }
            }
            dialog_et_beishu.addTextChangedListener(beiShuWatcher);
            dialog_et_beishu.setText("1");
            dialog_et_beishu.setSelection(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前彩种投注倒计时
     *
     * @param lotCode
     * @param version
     */
    private void getCountDown(String lotCode, String version) {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(ConfigCons.YUNJI_BASE_URL).append(ConfigCons.PORT).append(ConfigCons.LOTTERY_COUNTDOWN_URL);
        configUrl.append("?lotCode=").append(lotCode);
//        configUrl.append("&version=").append(version);
        CrazyRequest<CrazyResult<LotteryCountDownBean>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(GET_COUNT_DOWN)
                .headers(CommonUtils.getChatHeader(context))
                .shouldCache(false)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LotteryCountDownBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request, this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_btn_confirm) {
            // 跟单
            String amount = dialogEtSingleAmount.getText().toString();
            if (TextUtils.isEmpty(amount)) {
                ToastUtils.showToast(context, "请输入金额!");
                return;
            }
            if (onFollowBetListener != null) {
                onFollowBetListener.onFollowBet(amount, qiHao, 1);
            }
            CommonUtils.hideSoftInput(context, dialogEtSingleAmount);
            dismiss();
        } else if (i == R.id.dialog_iv_cancel) {
            dismiss();

        } else if (i == R.id.dialog_iv_clear) {
            dialogEtSingleAmount.setText("");
            dialogTvLotteryAmount.setText("0");
        } else if (i == R.id.dialog_iv_clear_gf) {
            dialog_et_beishu.setText("");
            dialogTvLotteryAmount.setText("0");
        } else if (i == R.id.dialog_btn_confirm_gf) {
            String beishu = dialog_et_beishu.getText().toString();
            String amount = dialogTvLotteryAmount.getText().toString();
            if (!cb_yuan.isChecked() && !cb_jiao.isChecked() && !cb_fen.isChecked()) {
                ToastUtils.showToast(context, "请选择模式");
                return;
            }
            if (TextUtils.isEmpty(beishu)) {
                ToastUtils.showToast(context, "请输入倍数");
                return;
            }
            Float totalAmount = (float) Math.round(Integer.parseInt(beishu) * Float.parseFloat(amount) * 100) / 100;
            String floatToInt = floatToInt(totalAmount);
            if (onFollowBetListener != null) {
                onFollowBetListener.onFollowBet(beishu, qiHao, mode);
            }
            CommonUtils.hideSoftInput(context, dialogEtSingleAmount);
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    private TextWatcher singleAmountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (betinfo.getVersion().equals("1")) {
                return;
            }
            if (TextUtils.isEmpty(s)) {
                dialogTvLotteryAmount.setText("0");
                return;
            }
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    dialogEtSingleAmount.setText(s);
                    dialogEtSingleAmount.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                dialogEtSingleAmount.setText(s);
                dialogEtSingleAmount.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    dialogEtSingleAmount.setText(s.subSequence(0, 1));
                    dialogEtSingleAmount.setSelection(1);
                    return;
                }
            }
            // 单注最大投注金额限制为500000
            if (Float.parseFloat(s.toString()) > 500000) {
                dialogEtSingleAmount.setText("500000");
                float f = 500000 * Float.parseFloat(dialogTvLotteryZhushu.getText().toString());
                String amount = floatToInt(f);
                dialogTvLotteryAmount.setText(amount);
                dialogEtSingleAmount.setSelection(6);
            } else {
                float amount = Float.parseFloat(s.toString()) * Float.parseFloat(dialogTvLotteryZhushu.getText().toString());
                String value = floatToInt(amount);
                dialogTvLotteryAmount.setText(value);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher beiShuWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!betinfo.getVersion().equals("1")) {
                return;
            }
            if (TextUtils.isEmpty(s)) {
                dialogTvLotteryAmount.setText("0");
                return;
            }
            if (s.toString().startsWith("0")) {
                dialog_et_beishu.setText("");
                dialogTvLotteryAmount.setText("0");
                return;
            }
            int parseInt = Integer.parseInt(s.toString());
            // 最大跟单倍数为为500000倍
            if (parseInt > 500000) {
                dialog_et_beishu.setText("500000");
                dialog_et_beishu.setSelection(6);
                parseInt = 500000;
            }
            // 用于计算的模式
            float calcuMode = 1f;
            if (mode == 1) {
                calcuMode = 1f;
            } else if (mode == 10) {
                calcuMode = 0.1f;
            } else if (mode == 100) {
                calcuMode = 0.01f;
            }
            float amount = parseInt * Integer.parseInt(betinfo.getLottery_zhushu()) * calcuMode * 2;
            amount = (float) Math.round(amount * 100) / 100;
            String s1 = floatToInt(amount);
            dialogTvLotteryAmount.setText(s1);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    /**
     * 将整数型的Float转换成整数
     *
     * @param f
     * @return
     */
    private String floatToInt(float f) {
        String s = String.valueOf(f);
        if (s.endsWith(".0") || s.endsWith(".00")) {
            s = s.split("\\.")[0];
        }
        return s;
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (response == null) {
            return;
        }
        CrazyResult<Object> result = response.result;
        switch (response.action) {
            case GET_COUNT_DOWN:
                if (result == null) {
                    ToastUtils.showToast(context, "投注时间获取失败!");
                    return;
                }
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    ToastUtils.showToast(context, TextUtils.isEmpty(errorString) ? "投注时间获取失败!" : errorString);
                    return;
                }
                LotteryCountDownBean lotteryCountDownBean = (LotteryCountDownBean) result.result;
                if (!lotteryCountDownBean.isSuccess()) {
                    ToastUtils.showToast(context, "投注时间获取失败!");
                    return;
                }
                updateCurrenQihaoCountDown(lotteryCountDownBean.getContent());
                break;
        }
    }

    /**
     * 更新当前期数倒计时时间
     *
     * @param countDown
     */
    private void updateCurrenQihaoCountDown(LotteryCountDownBean.ContentBean countDown) {
        if (countDown == null) {
            return;
        }

        if(!countDown.getQiHao().equals(initQiHao)){
            tv_warning.setVisibility(View.VISIBLE);
            dialogBtnConfirm.setEnabled(false);
            return;
        }else {
            tv_warning.setVisibility(View.GONE);
            dialogBtnConfirm.setEnabled(true);
        }

        //创建开奖周期倒计时器¬
        long serverTime = countDown.getServerTime();
        long activeTime = countDown.getActiveTime();
        long duration = Math.abs(activeTime - serverTime);
        long nextStartTime = countDown.getNextStartTime();
        long startTime = countDown.getPreStartTime();
        qiHao = countDown.getQiHao();
        dialogTvLotteryQihao.setText("期号：" + (qiHao.substring(qiHao.length() - 6, qiHao.length())) + "期");

        //封盘逻辑
        currentQihao = countDown.getQiHao();
        if (startTime - serverTime < 0) {//处于开盘中
            fenpaning = false;
            duration = Math.abs(activeTime - serverTime);
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            createEndlineTouzhuTimer(duration, countDown.getQiHao());
            //开始离投注结束时间倒计时
            timer.start();
        } else {
            dialogTvLotteryTime.setText("封盘中");
            if (dialogBtnConfirm.isEnabled()) {
                dialogBtnConfirm.setEnabled(false);
            }
            if (dialog_btn_confirm_gf.isEnabled()) {
                dialog_btn_confirm_gf.setEnabled(false);
            }
        }
    }

    private void createEndlineTouzhuTimer(long duration, String qiHao) {
        timer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String time = TimeUtils.secToTime((int) (millisUntilFinished / 1000));
                dialogTvLotteryTime.setText(time);
                if (!dialogBtnConfirm.isEnabled()) {
                    dialogBtnConfirm.setEnabled(true);
                }
                if (!dialog_btn_confirm_gf.isEnabled()) {
                    dialog_btn_confirm_gf.setEnabled(true);
                }
            }

            @Override
            public void onFinish() {
                if (dialogBtnConfirm.isEnabled()) {
                    dialogBtnConfirm.setEnabled(false);
                }
                if (dialog_btn_confirm_gf.isEnabled()) {
                    dialog_btn_confirm_gf.setEnabled(false);
                }
            }
        };
    }

    private OnFollowBetListener onFollowBetListener;

    public void setOnFollowBetListener(OnFollowBetListener onFollowBetListener) {
        this.onFollowBetListener = onFollowBetListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.cb_yuan) {
                setCurrentMode(1);
                cb_jiao.setChecked(false);
                cb_fen.setChecked(false);
            } else if (buttonView.getId() == R.id.cb_jiao) {
                setCurrentMode(10);
                cb_yuan.setChecked(false);
                cb_fen.setChecked(false);
            } else if (buttonView.getId() == R.id.cb_fen) {
                setCurrentMode(100);
                cb_yuan.setChecked(false);
                cb_jiao.setChecked(false);
            }
            String beishu = dialog_et_beishu.getText().toString();
            if (!TextUtils.isEmpty(beishu)) {
                dialog_et_beishu.setText(beishu);
                dialog_et_beishu.setSelection(beishu.length());
            }
        }
    }

    private int mode = 1;

    private void setCurrentMode(int mode) {
        this.mode = mode;
    }

    public interface OnFollowBetListener {
        void onFollowBet(String betMoney, String qihao, int model);
    }
}
